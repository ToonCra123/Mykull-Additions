package net.mykull.mykulladditions.common.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.mykull.mykulladditions.Registration;
import net.mykull.mykulladditions.common.energy.AdaptedEnergyStorage;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class CableBlockEntity extends BlockEntity {

    private static class EnergyOutput {
        public BlockPos pos;
        public Direction dir;
        EnergyOutput() {
        }
    }

    public static final String ENERGY_TAG = "Energy";

    public static final int MAXTRANSFER = 1000;
    public static final int CAPACITY = 10000;

    private final EnergyStorage energy = createEnergyStorage();
    private final Lazy<IEnergyStorage> energyHandler = Lazy.of(() -> new AdaptedEnergyStorage(energy) {
        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return 0;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            setChanged();
            return super.receiveEnergy(maxReceive, simulate);
        }

        @Override
        public boolean canExtract() {
            return false;
        }

        @Override
        public boolean canReceive() {
            return true;
        }
    });



    // @todo fix me
    public CableBlockEntity(BlockPos pos, BlockState blockState) {
        super(Registration.CABLE_BLOCK_ENTITY.get(), pos, blockState);
    }

    private Set<EnergyOutput> outputs = null;

    public void tickServer() {
        if (energy.getEnergyStored() > 0) {
            // Only do something if we have energy
            checkOutputs();
            if (!outputs.isEmpty()) {
                // Distribute energy over all outputs
                int amount = energy.getEnergyStored() / outputs.size();
                for (EnergyOutput p : outputs) {
                    IEnergyStorage handler = level.getCapability(Capabilities.EnergyStorage.BLOCK, p.pos, p.dir);
                    if (handler != null) {
                        if (handler.canReceive()) {
                            int received = handler.receiveEnergy(amount, false);
                            energy.extractEnergy(received, false);
                        }
                    }
                }
            }
        }
    }


    public void markDirty() {
        traverse(worldPosition, cable -> cable.outputs = null);
    }

    private void checkOutputs() {
        if (outputs == null) {
            outputs = new HashSet<>();
            traverse(worldPosition, cable -> {
                // Check for all energy receivers around this position (ignore cables)
                for (Direction direction : Direction.values()) {
                    BlockPos p = cable.getBlockPos().relative(direction);
                    BlockEntity te = level.getBlockEntity(p);
                    if (te != null && !(te instanceof CableBlockEntity)) {
                        IEnergyStorage handler = level.getCapability(Capabilities.EnergyStorage.BLOCK, p, direction);
                        if (handler != null) {
                            if (handler.canReceive()) {
                                EnergyOutput lol = new EnergyOutput();
                                lol.pos = p;
                                lol.dir = direction;
                                outputs.add(lol);
                            }
                        }
                    }
                }
            });
        }
    }

    // This is a generic function that will traverse all cables connected to this cable
    // and call the given consumer for each cable.
    private void traverse(BlockPos pos, Consumer<CableBlockEntity> consumer) {
        Set<BlockPos> traversed = new HashSet<>();
        traversed.add(pos);
        consumer.accept(this);
        traverse(pos, traversed, consumer);
    }

    private void traverse(BlockPos pos, Set<BlockPos> traversed, Consumer<CableBlockEntity> consumer) {
        for (Direction direction : Direction.values()) {
            BlockPos p = pos.relative(direction);
            if (!traversed.contains(p)) {
                traversed.add(p);
                if (level.getBlockEntity(p) instanceof CableBlockEntity cable) {
                    consumer.accept(cable);
                    cable.traverse(p, traversed, consumer);
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(ENERGY_TAG, energy.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(ENERGY_TAG)) {
            energy.deserializeNBT(registries, tag.get(ENERGY_TAG));
        }
    }


    @Nonnull
    private EnergyStorage createEnergyStorage() {
        return new EnergyStorage(CAPACITY, MAXTRANSFER, MAXTRANSFER);
    }

    public IEnergyStorage getEnergyHandler() {
        return energyHandler.get();
    }
}
