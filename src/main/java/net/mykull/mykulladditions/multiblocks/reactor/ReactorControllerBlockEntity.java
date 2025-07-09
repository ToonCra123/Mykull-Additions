package net.mykull.mykulladditions.multiblocks.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.mykull.mykulladditions.Registration;
import net.mykull.mykulladditions.multiblocks.reactor.part.ReactorMultiblockPart;
import net.mykull.mykulladditions.multiblocks.reactor.part.ReactorPartTypes;

public class ReactorControllerBlockEntity extends ReactorMultiblockPart {

    public ReactorMBLogic loadedLogic;

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
        if (reactorController.logic == null) return;

        reactorController.logic.tick();
    }



    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (reactorController != null && reactorController.logic != null) {
            reactorController.logic.saveAdditional(tag, registries);
        }
    }

    @Override
    protected void tryAttachMultiblock() {
        super.tryAttachMultiblock();
        if(this.reactorController != null) {
            this.reactorController.setLogic(loadedLogic);
        }
    }
}
