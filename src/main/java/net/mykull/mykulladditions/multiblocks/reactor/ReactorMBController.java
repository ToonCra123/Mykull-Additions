package net.mykull.mykulladditions.multiblocks.reactor;

import com.sun.jna.platform.win32.COM.IConnectionPoint;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.mykull.lib.multiblock.IMultiblockPart;
import net.mykull.lib.multiblock.MultiblockController;
import net.mykull.mykulladditions.multiblocks.reactor.part.ReactorMultiblockPart;
import net.mykull.mykulladditions.multiblocks.reactor.part.ReactorPartEnum;

import java.util.HashSet;
import java.util.Set;

public class ReactorMBController extends MultiblockController {


    private Set<BlockPos> controllerList = new HashSet<>();
    private Set<BlockPos> casingList = new HashSet<>();

    // Two posit
    private BlockPos bottomLeftBB = null;
    private BlockPos topRightBB = null;

    public boolean formed = false;

    public ReactorMBController(Level level) {
        super(level);
    }

    @Override
    public boolean attachBlock(IMultiblockPart part) {
        BlockPos partPos = part.getWorldPosition();

        // first block
        if (topRightBB == null && bottomLeftBB == null) {
            topRightBB = bottomLeftBB = partPos;
        }

        return super.attachBlock(part);
    }

    public int getSize() {
        return parts.size();
    }


    @Override
    public boolean isMachineWhole() {
       if (controllerList.size() != 1) return false;

       if (casingList.isEmpty()) return false;

       return true;
    }

    private void updateBoundingBoxes(BlockPos pos) {
        if (bottomLeftBB == null || topRightBB == null) {
            // First block — set both bounds to pos
            bottomLeftBB = pos;
            topRightBB = pos;
        } else {
            int minX = Math.min(bottomLeftBB.getX(), pos.getX());
            int minY = Math.min(bottomLeftBB.getY(), pos.getY());
            int minZ = Math.min(bottomLeftBB.getZ(), pos.getZ());

            int maxX = Math.max(topRightBB.getX(), pos.getX());
            int maxY = Math.max(topRightBB.getY(), pos.getY());
            int maxZ = Math.max(topRightBB.getZ(), pos.getZ());

            bottomLeftBB = new BlockPos(minX, minY, minZ);
            topRightBB = new BlockPos(maxX, maxY, maxZ);
        }
    }

    private void recalculateBoundingBox() {
        bottomLeftBB = null;
        topRightBB = null;

        for (BlockPos pos : parts) {
            updateBoundingBoxes(pos); // will expand properly
        }
    }

    @Override
    protected void onBlockAdded(IMultiblockPart part) {
        updateBoundingBoxes(part.getWorldPosition());

        ReactorPartEnum e = (ReactorPartEnum) part.getPartType();
        switch (e) {
            case CONTROLLER -> controllerList.add(part.getWorldPosition());
            case CASING -> casingList.add(part.getWorldPosition());
        }

        // IDK this may be a bad thinng to add
        formed = isMachineWhole();
        super.onBlockAdded(part);
    }

    @Override
    public void detachBlock(IMultiblockPart part) {

        ReactorPartEnum e = (ReactorPartEnum) part.getPartType();

        switch (e) {
            case CONTROLLER -> controllerList.remove(part.getWorldPosition());
            case CASING -> casingList.remove(part.getWorldPosition());
        }

        recalculateBoundingBox();
        super.detachBlock(part);
    }

    public BlockPos getBBleft() {
        return bottomLeftBB;
    }
}
