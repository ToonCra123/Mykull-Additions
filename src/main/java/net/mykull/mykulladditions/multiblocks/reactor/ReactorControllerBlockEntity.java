package net.mykull.mykulladditions.multiblocks.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.mykull.mykulladditions.Registration;
import net.mykull.mykulladditions.multiblocks.reactor.part.ReactorMultiblockPart;
import net.mykull.mykulladditions.multiblocks.reactor.part.ReactorPartTypes;

public class ReactorControllerBlockEntity extends ReactorMultiblockPart {
    public ReactorControllerBlockEntity(BlockPos pos, BlockState blockState) {
        super(Registration.REACTOR_CONTROLLER_ENTITY.get(), pos, blockState);
    }

    @Override
    public ReactorPartTypes getPartType() {
        return ReactorPartTypes.CONTROLLER;
    }

    public void tickServer() {
        if (reactorController == null) return;
        if (!reactorController.formed) return;

        if (reactorController.logic == null) {
            reactorController.logic = new ReactorMBLogic();
        }

        reactorController.logic.tick();
    }
}
