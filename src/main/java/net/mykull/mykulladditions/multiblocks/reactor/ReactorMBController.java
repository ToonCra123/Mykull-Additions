package net.mykull.mykulladditions.multiblocks.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.mykull.lib.multiblock.IMultiblockPart;
import net.mykull.lib.multiblock.MultiblockController;
import net.mykull.mykulladditions.MykullsAdditions;
import net.mykull.mykulladditions.Registration;
import net.mykull.mykulladditions.multiblocks.reactor.part.ReactorPartTypes;

import java.util.HashSet;
import java.util.Set;

// TODO: ABSTRACT INTO CUBIODMULTIBLOCK CLASS
public class ReactorMBController extends MultiblockController {

    public ReactorMBLogic logic = null;

    public static final Set<Block> VALID_INTERIOR_BLOCKS = Set.of(Blocks.AIR, Registration.REACTOR_FUEL_ROD.get());

    private final Set<BlockPos> controllerList = new HashSet<>();
    private final Set<BlockPos> casingList = new HashSet<>();
    private final Set<BlockPos> controlRodList = new HashSet<>();
    private final Set<BlockPos> fuelRodList = new HashSet<>();

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
        // Initial Conditions
        if (controllerList.size() != 1) return false;

        if (casingList.isEmpty()) return false;

        // Checks for size restraints, going to change to config
        if (getWidth() < 3 || getDepth() < 3 || getHeight() < 3) return false;
        if (getWidth() > 16 || getDepth() > 16 || getHeight() > 16) return false;


        // Check Frame
        int minX = bottomLeftBB.getX();
        int minY = bottomLeftBB.getY();
        int minZ = bottomLeftBB.getZ();
        int maxX = topRightBB.getX();
        int maxY = topRightBB.getY();
        int maxZ = topRightBB.getZ();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);

                    int boundsHit = 0;
                    if (x == minX || x == maxX) boundsHit++;
                    if (y == minY || y == maxY) boundsHit++;
                    if (z == minZ || z == maxZ) boundsHit++;

                    boolean isCorner = (boundsHit == 3);
                    boolean isEdge = (boundsHit == 2);
                    boolean isFace = (boundsHit == 1);
                    boolean isInside = (boundsHit == 0);

                    if (isEdge || isCorner) {
                        if (!casingList.contains(pos)) {
                            MykullsAdditions.LOGGER.debug("Missing edge casing at: {}", pos);
                            return false;
                        }
                    } else if(isFace) {
                        // Up Face case
                        if(y == maxY) {
                            if (!casingList.contains(pos) && !controlRodList.contains(pos)) {
                                MykullsAdditions.LOGGER.debug("Missing Up Face casing at: {}", pos);
                                return false;
                            }
                        } else {
                            if(!casingList.contains(pos) && !controllerList.contains(pos)) {
                                MykullsAdditions.LOGGER.debug("Missing face casing at: {}", pos);
                                return false;
                            }
                        }
                    } else if (isInside) {
                        if(!VALID_INTERIOR_BLOCKS.contains(world.getBlockState(pos).getBlock())) {
                            MykullsAdditions.LOGGER.debug("Invalid Interior Block at: {}", pos);
                            return false;
                        }
                    }
                }
            }
        }

        for (BlockPos pos : controlRodList) {
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos neighbor = pos.relative(dir);
                if (controlRodList.contains(neighbor)) {
                    MykullsAdditions.LOGGER.debug("Control rods at {} and {} are adjacent!", pos, neighbor);
                    return false;
                }
            }
        }

        if ((fuelRodList.size() != (controlRodList.size() * (getHeight() - 2) ) ) ) return false;

        for (BlockPos controllerPos : controlRodList) {
            int x = controllerPos.getX();
            int z = controllerPos.getZ();
            int topY = controllerPos.getY();
            int bottomY = bottomLeftBB.getY();

            for (int y = topY - 1; y >= bottomY + 1; y--) {
                BlockPos checkPos = new BlockPos(x, y, z);
                Block block = world.getBlockState(checkPos).getBlock();

                if (block != Registration.REACTOR_FUEL_ROD.get()) {
                    MykullsAdditions.LOGGER.debug("Control rod not extending to base: {}", checkPos);
                    return false;
                }
            }
        }

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

        ReactorPartTypes e = (ReactorPartTypes) part.getPartType();
        switch (e) {
            case CONTROLLER -> controllerList.add(part.getWorldPosition());
            case CASING -> casingList.add(part.getWorldPosition());
            case CONTROL_ROD -> controlRodList.add(part.getWorldPosition());
            case FUEL_ROD -> fuelRodList.add(part.getWorldPosition());
        }

        // IDK this may be a bad thinng to add
        formed = isMachineWhole();
        super.onBlockAdded(part);
    }

    @Override
    public void detachBlock(IMultiblockPart part) {
        super.detachBlock(part);
        ReactorPartTypes e = (ReactorPartTypes) part.getPartType();
        switch (e) {
            case CONTROLLER -> controllerList.remove(part.getWorldPosition());
            case CASING -> casingList.remove(part.getWorldPosition());
            case CONTROL_ROD -> controlRodList.remove(part.getWorldPosition());
            case FUEL_ROD -> fuelRodList.remove(part.getWorldPosition());
        }
        recalculateBoundingBox();
        formed = isMachineWhole();
    }

    public int getWidth() {
        if (bottomLeftBB == null || topRightBB == null) return 0;
        return topRightBB.getX() - bottomLeftBB.getX() + 1;
    }

    public int getHeight() {
        if (bottomLeftBB == null || topRightBB == null) return 0;
        return topRightBB.getY() - bottomLeftBB.getY() + 1;
    }

    public int getDepth() {
        if (bottomLeftBB == null || topRightBB == null) return 0;
        return topRightBB.getZ() - bottomLeftBB.getZ() + 1;
    }
}
