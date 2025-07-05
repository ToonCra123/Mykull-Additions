package net.mykull.mykulladditions.multiblocks.reactor;

import net.minecraft.core.BlockPos;
import net.mykull.lib.multiblock.IMultiblockPart;
import net.mykull.lib.multiblock.MultiblockController;

public class ReactorMBController extends MultiblockController {


    // Two posit
    private BlockPos bottomLeftBB = null;
    private BlockPos topRightBB = null;


    @Override
    public boolean attachBlock(IMultiblockPart part) {
        BlockPos partPos = part.getWorldPosition();

        // first block
        if (topRightBB == null && bottomLeftBB == null) {
            topRightBB = bottomLeftBB = partPos;
        }

        return super.attachBlock(part);
    }

    @Override
    public boolean isMachineWhole() {



        return false;
    }
}
