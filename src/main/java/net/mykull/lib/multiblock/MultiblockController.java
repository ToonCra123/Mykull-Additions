package net.mykull.lib.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class MultiblockController {

    protected final Set<BlockPos> parts = new HashSet<>();
    protected Level world;

    public boolean attachBlock(IMultiblockPart part) {
        if (parts.add(part.getWorldPosition())) {
            part.setController(this);
            onBlockAdded(part);
            return true;
        }
        return false;
    }

    public void detachBlock(IMultiblockPart part) {
        parts.remove(part.getWorldPosition());
        part.onDetached();
    }

    /// @return If the multiblock is validly formed or not
    public abstract boolean isMachineWhole();

    /**
     *
     * @param part added to the controller
     */
    protected void onBlockAdded(IMultiblockPart part) {
        // Optional: do something like update bounding box
    }

    public Set<BlockPos> getPartPositions() {
        return Collections.unmodifiableSet(parts);
    }
}
