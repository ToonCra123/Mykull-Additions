package net.mykull.mykulladditions.common.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.mykull.mykulladditions.Registration;
import net.mykull.mykulladditions.common.energy.AdaptedEnergyStorage;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class GeneratorBlockEntity extends BlockEntity {

    public static final String ITEMS_TAG = "Inventory";
    public static final String ENERGY_TAG = "Energy";


    public static final int GENERATE = 50;
    public static final int MAXTRANSFER = 1000;
    public static final int CAPACITY = 100000;

    public static int SLOT_COUNT = 1;
    public static int SLOT = 0;

    private ItemStackHandler items = createItemHandler();
    private Lazy<IItemHandler> itemHandler = Lazy.of(() -> items);

    private final EnergyStorage energy = createEnergyStorage();
    private final Lazy<IEnergyStorage> energyHandler = Lazy.of(() -> new AdaptedEnergyStorage(energy) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return 0;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return 0;
        }

        @Override
        public boolean canExtract() {
            return true;
        }

        @Override
        public boolean canReceive() {
            return false;
        }
    });

    private int burnTime;

    public GeneratorBlockEntity(BlockPos pos, BlockState blockState) {
        super(Registration.GENERATOR_BLOCK_ENTITY.get(), pos, blockState);
    }

    public void tickServer() {
        generateEnergy();
        distributeEnergy();
    }

    private void generateEnergy() {
        if (energy.getEnergyStored() < energy.getMaxEnergyStored()) {
            if (burnTime <= 0) {
                ItemStack fuel = items.getStackInSlot(SLOT);
                if (fuel.isEmpty()) {
                    // No fuel
                    return;
                }
                setBurnTime(fuel.getBurnTime(RecipeType.SMELTING));
                if (burnTime <= 0) {
                    // Not a fuel
                    return;
                }
                items.extractItem(SLOT, 1, false);
            } else {
                setBurnTime(burnTime-1);
                energy.receiveEnergy(GENERATE, false);
            }
            setChanged();
        }
    }

    private void setBurnTime(int bt) {
        if (bt == burnTime) {
            return;
        }
        burnTime = bt;
        if (getBlockState().getValue(BlockStateProperties.POWERED) != burnTime > 0) {
            level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, burnTime > 0));
        }
        setChanged();
    }

    private void distributeEnergy() {
        for (Direction direction : Direction.values()) {
            if (energy.getEnergyStored() <= 0) {
                return;
            }
            IEnergyStorage energy = level.getCapability(Capabilities.EnergyStorage.BLOCK, getBlockPos().relative(direction), direction);
            if (energy != null) {
                if (energy.canReceive()) {
                    int received = energy.receiveEnergy(Math.min(this.energy.getEnergyStored(), MAXTRANSFER), false);
                    this.energy.extractEnergy(received, false);
                    setChanged();
                }
            }
        }
    }

    public ItemStackHandler getItems() {
        return items;
    }

    public int getStoredPower() {
        return energy.getEnergyStored();
    }

    @Nonnull
    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(SLOT_COUNT) {

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return (stack.getBurnTime(RecipeType.SMELTING) > 0);
            }

            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    @Nonnull
    private EnergyStorage createEnergyStorage() {
        return new EnergyStorage(CAPACITY, MAXTRANSFER, MAXTRANSFER);
    }


    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(ITEMS_TAG, items.serializeNBT(registries));
        tag.put(ENERGY_TAG, energy.serializeNBT(registries));
    }


    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(ITEMS_TAG)) {
            items.deserializeNBT(registries, tag.getCompound(ITEMS_TAG));
        }
        if (tag.contains(ENERGY_TAG)) {
            energy.deserializeNBT(registries, tag.get(ENERGY_TAG));
        }
    }

    public IItemHandler getItemHandler() {
        return itemHandler.get();
    }

    public IEnergyStorage getEnergyHandler() {
        return energyHandler.get();
    }
}
