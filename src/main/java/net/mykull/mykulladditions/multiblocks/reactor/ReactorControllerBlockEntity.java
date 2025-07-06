package net.mykull.mykulladditions.multiblocks.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.mykull.lib.multiblock.MultiblockController;
import net.mykull.mykulladditions.Registration;
import net.mykull.mykulladditions.multiblocks.reactor.part.IReactorMultiblockPart;

import java.util.HashSet;
import java.util.Set;

public class ReactorControllerBlockEntity extends BlockEntity implements IReactorMultiblockPart {

    public boolean formed = false;
    public Set<BlockPos> casingPos = new HashSet<>();

    public ReactorMBController reactorController = null;


    public ReactorControllerBlockEntity(BlockPos pos, BlockState blockState) {
        super(Registration.REACTOR_CONTROLLER_ENTITY.get(), pos, blockState);
    }

    public void serverTick() {

    }

    // Logic for adding controller
    @Override
    public void onLoad() {

        super.onLoad();
    }

    @Override
    public BlockPos getWorldPosition() {
        return worldPosition;
    }

    @Override
    public void setController(MultiblockController controller) {
        if (controller instanceof ReactorMBController control) {
            reactorController = control;
        }
    }


    @Override
    public MultiblockController getController() {
        return reactorController;
    }

    @Override
    public void onAttached(MultiblockController controller) {

    }

    @Override
    public void onDetached() {

    }
}
