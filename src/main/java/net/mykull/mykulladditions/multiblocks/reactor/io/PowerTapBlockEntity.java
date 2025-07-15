package net.mykull.mykulladditions.multiblocks.reactor.io;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.mykull.mykulladditions.Registration;
import net.mykull.mykulladditions.multiblocks.reactor.ReactorMBLogic;
import net.mykull.mykulladditions.multiblocks.reactor.part.ReactorMultiblockPart;
import net.mykull.mykulladditions.multiblocks.reactor.part.ReactorPartTypes;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class PowerTapBlockEntity extends ReactorMultiblockPart {
    public PowerTapBlockEntity(BlockPos pos, BlockState blockState) {
        super(Registration.REACTOR_POWER_TAP_ENTITY.get(), pos, blockState);
    }

    public void tickServer() {
        if(level != null && this.reactorController != null && this.reactorController.logic != null) {
            ReactorMBLogic logic = this.reactorController.logic;

            for (Direction direction : Direction.values()) {
                if (logic.energy.getEnergyStored() <= 0) {
                    return;
                }
                IEnergyStorage energy = level.getCapability(Capabilities.EnergyStorage.BLOCK, getBlockPos().relative(direction), direction);
                if (energy != null) {
                    if (energy.canReceive()) {
                        int received = energy.receiveEnergy(Math.min(logic.energy.getEnergyStored(), ReactorMBLogic.MAXTRANSFER), false);
                        logic.energy.extractEnergy(received, false);
                        setChanged();
                    }
                }
            }
        }
    }

    public IEnergyStorage getEnergyHandler() {
        if(level != null && this.reactorController != null && this.reactorController.logic != null) {
            return this.reactorController.logic.getEnergyStorage();
        } else {
            return null;
        }
    }

    @Override
    public ReactorPartTypes getPartType() {
        return ReactorPartTypes.POWER_TAP;
    }
}
