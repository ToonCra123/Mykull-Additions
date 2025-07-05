package net.mykull.lib.multiblock;

import net.minecraft.core.BlockPos;

public interface IMultiblockPart {
    BlockPos getWorldPosition();
    void setController(MultiblockController controller);
    MultiblockController getController();
    void onAttached(MultiblockController controller);
    void onDetached();
}
