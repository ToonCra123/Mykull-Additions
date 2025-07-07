package net.mykull.mykulladditions.multiblocks.reactor.part;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.mykull.lib.multiblock.MultiblockController;
import net.mykull.mykulladditions.multiblocks.reactor.ReactorControllerBlockEntity;
import net.mykull.mykulladditions.multiblocks.reactor.ReactorMBController;
import net.mykull.mykulladditions.multiblocks.reactor.ReactorPartBlock;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public abstract class ReactorMultiblockPart extends BlockEntity implements IReactorMultiblockPart {
    public ReactorMBController reactorController = null;

    public ReactorMultiblockPart(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    // Logic for adding controller
    @Override
    public void onLoad() {
        if (level != null && !level.isClientSide) {
            tryAttachMultiblock();
        }
        super.onLoad();
    }

    private void tryAttachMultiblock() {
        if (reactorController != null) return;

        Set<ReactorMBController> foundControllers = new HashSet<>();

        for (Direction dir : Direction.values()) {
            BlockEntity neighbor = level.getBlockEntity(worldPosition.relative(dir));
            if (neighbor instanceof ReactorMultiblockPart part) {
                if (part.getController() != null) {
                    part.getController().attachBlock(this);
                    if (part.getController() instanceof ReactorMBController r) {
                        foundControllers.add(r);
                    }
                }
            }
        }


        if(foundControllers.isEmpty()) {
            ReactorMBController newController = new ReactorMBController(level);
            newController.attachBlock(this);
            this.reactorController = newController;
        } else if (foundControllers.size() == 1) {
            ReactorMBController r = foundControllers.iterator().next();
            r.attachBlock(this);
            this.reactorController = r;
        } else {
            handleMultiblockControllConflict(foundControllers);
        }
    }

    private void handleMultiblockControllConflict(Set<ReactorMBController> controllers) {
        ReactorMBController largest = controllers.stream()
                .max(Comparator.comparingInt(ReactorMBController::getSize))
                .orElse(null);

        if(largest != null) {
            largest.attachBlock(this);
            this.reactorController = largest;
            for (ReactorMBController r : controllers) {
                merge(r);
            }
        }
    }


    /// IMPORTANT SET CONTROLLER BEFORE MERGE
    public void merge(ReactorMBController other) {
        if (other == this.reactorController) return; // skip self-merge

        // Transfer all parts
        for (BlockPos pos : other.parts) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ReactorMultiblockPart part) {
                part.setController(this.reactorController); // update the part to point to this controller
                this.reactorController.attachBlock(part);      // add it to this controller
            }
        }

        other.invalidate();
    }

    @Override
    public void setRemoved() {
        if (this.reactorController != null) {
            this.reactorController.detachBlock(this);
            this.reactorController = null;
        }
        super.setRemoved();
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
