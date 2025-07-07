package net.mykull.lib.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class MultiblockController {

    public final Set<BlockPos> parts = new HashSet<>();
    protected Level world;

    public MultiblockController(Level level) {
        this.world = level;
    }

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

    public void invalidate() {
        Iterator<BlockPos> it = parts.iterator();
        while (it.hasNext()) {
            BlockPos pos = it.next();
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof IMultiblockPart part) {
                part.setController(null); // disassociate
            }
            it.remove(); // safe remove from set
        }
    }

}
