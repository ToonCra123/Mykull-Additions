package net.mykull.mykulladditions.multiblocks.reactor;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.mykull.lib.energy.AdaptedEnergyStorage;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ReactorMBLogic {

    public static final String ITEMS_TAG = "Inventory";
    public static final String ENERGY_TAG = "Energy";

    public static int CAPACITY = 10000000;
    public static int MAXTRANSFER = 10000000;

    public static final int SLOT_COUNT = 2;

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

    private ItemStackHandler items = createItemHandler();
    private Lazy<IItemHandler> itemHandler = Lazy.of(() -> items);

    public ReactorMBController controller;


    public ReactorMBLogic(ReactorMBController controller) {
        this.controller = controller;
    }

    public void tick() {
        // nothing jit

    }

    public IEnergyStorage getEnergyStorage() {
        return energyHandler.get();
    }

    private EnergyStorage createEnergyStorage() {
        return new EnergyStorage(CAPACITY, MAXTRANSFER, MAXTRANSFER);
    }

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(SLOT_COUNT) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return (stack.getBurnTime(RecipeType.SMELTING) > 0);
            }
        };
    }

    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains(ENERGY_TAG))
            energy.deserializeNBT(registries, tag.get(ENERGY_TAG));
        if (tag.contains(ITEMS_TAG))
            items.deserializeNBT(registries, tag.getCompound(ITEMS_TAG));
    }

    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put(ENERGY_TAG, energy.serializeNBT(registries));
        tag.put(ITEMS_TAG, items.serializeNBT(registries));
    }
}
