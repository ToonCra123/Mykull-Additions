package net.mykull.mykulladditions.multiblocks.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.mykull.lib.multiblock.IMultiblockPart;
import net.mykull.lib.multiblock.MultiblockController;
import net.mykull.mykulladditions.MykullsAdditions;
import net.mykull.mykulladditions.Registration;
import net.mykull.mykulladditions.multiblocks.reactor.part.ReactorPartTypes;
import net.mykull.mykulladditions.multiblocks.reactor.states.CasingBlockStateTypes;
import org.jetbrains.annotations.NotNull;

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
    private final Set<BlockPos> powerTapList = new HashSet<>();
    private final Set<BlockPos> fuelIOList = new HashSet<>();


    // Two posit
    private BlockPos bottomLeftBB = null;
    private BlockPos topRightBB = null;

    public boolean formed = false;

    public ReactorMBController(Level level) {
        super(level);
    }

    public int getSize() {
        return parts.size();
    }

    private static int maxWidth = 0;
    private static int maxDepth = 0;
    private static int maxHeight = 0;

    private int getMaxWidth() {
        if (maxWidth == 0) {
            int max = MykullsAdditions.CONFIG.get().reactorSize.maxWidth;
            if (max < 3) max = 16;
            maxWidth = max;
        }
        return maxWidth;
    }

    private int getMaxDepth() {
        if (maxDepth == 0) {
            int max = MykullsAdditions.CONFIG.get().reactorSize.maxDepth;
            if (max < 3) max = 16;
            maxDepth = max;
        }
        return maxDepth;
    }

    private int getMaxHeight() {
        if (maxHeight == 0) {
            int max = MykullsAdditions.CONFIG.get().reactorSize.maxHeight;
            if (max < 3) max = 16;
            maxHeight = max;
        }
        return maxHeight;
    }

    private void updateBlockStatesUnformed() {
        for (BlockPos pos : casingList) {
            BlockState currentState = world.getBlockState(pos);
            BlockState newState = currentState.setValue(ReactorCasingBlock.CASING_TYPE, CasingBlockStateTypes.UNFORMED);
            world.setBlock(pos, newState, Block.UPDATE_ALL);
        }
    }

    private void updateBlockStatesFormed() {

        int minX = bottomLeftBB.getX();
        int minY = bottomLeftBB.getY();
        int minZ = bottomLeftBB.getZ();
        int maxX = topRightBB.getX();
        int maxY = topRightBB.getY();
        int maxZ = topRightBB.getZ();

        for (BlockPos pos : casingList) {
            BlockState currentState = world.getBlockState(pos);
            if (currentState.getBlock() != Registration.REACTOR_CASING.get()) continue;

            int boundsHit = 0;
            if (pos.getX() == minX || pos.getX() == maxX) boundsHit++;
            if (pos.getY() == minY || pos.getY() == maxY) boundsHit++;
            if (pos.getZ() == minZ || pos.getZ() == maxZ) boundsHit++;

            boolean isCorner = (boundsHit == 3);
            boolean isEdge = (boundsHit == 2);
            boolean isFace = (boundsHit == 1);

            if (isCorner) {
                BlockState newState = currentState.setValue(ReactorCasingBlock.CASING_TYPE, CasingBlockStateTypes.CORNER);
                world.setBlock(pos, newState, Block.UPDATE_ALL);
            } else if (isFace) {
                BlockState newState = currentState.setValue(ReactorCasingBlock.CASING_TYPE, CasingBlockStateTypes.NONE);
                world.setBlock(pos, newState, Block.UPDATE_ALL);
            } else if (isEdge) {
                boolean xEdge = pos.getX() == minX || pos.getX() == maxX;
                boolean yEdge = pos.getY() == minY || pos.getY() == maxY;
                boolean zEdge = pos.getZ() == minZ || pos.getZ() == maxZ;

                CasingBlockStateTypes casingType = getCasingBlockStateTypes(xEdge, zEdge, yEdge);

                BlockState newState = currentState.setValue(ReactorCasingBlock.CASING_TYPE, casingType);
                world.setBlock(pos, newState, Block.UPDATE_ALL);
            }
        }
    }

    private static @NotNull CasingBlockStateTypes getCasingBlockStateTypes(boolean xEdge, boolean zEdge, boolean yEdge) {
        // Determine the axis the edge runs along
        if (!xEdge) {
            return CasingBlockStateTypes.HORIZONTAL_WE; // Edge runs along X (West-East)
        } else if (!zEdge) {
            return CasingBlockStateTypes.HORIZONTAL_NS; // Edge runs along Z (North-South)
        } else if (!yEdge) {
            return CasingBlockStateTypes.VERTICAL;       // Edge runs vertically
        } else {
            return CasingBlockStateTypes.NONE; // Fallback (shouldn’t happen if edge)
        }
    }

    public void setLogic(ReactorMBLogic logic) {
        this.logic = logic;
    }

    @Override
    public boolean isMachineWhole() {
        // Initial Conditions
        if (controllerList.size() != 1) return false;

        if (casingList.isEmpty()) return false;

        // Checks for size restraints, going to change to config
        if (getWidth() < 3 || getDepth() < 3 || getHeight() < 3) return false;
        if (getWidth() > getMaxWidth() || getDepth() > getMaxDepth() || getHeight() > getMaxHeight()) return false;


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
                            return false;
                        }
                    } else if(isFace) {
                        // Up Face case
                        if(y == maxY) {
                            if (!casingList.contains(pos) && !controlRodList.contains(pos)) {
                                return false;
                            }
                        } else {
                            if(!casingList.contains(pos) && !controllerList.contains(pos) && !powerTapList.contains(pos) && !fuelIOList.contains(pos)) {
                                return false;
                            }
                        }
                    } else if (isInside) {
                        if(!VALID_INTERIOR_BLOCKS.contains(world.getBlockState(pos).getBlock())) {
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
            case POWER_TAP -> powerTapList.add(part.getWorldPosition());
            case FUEL_IO -> fuelIOList.add(part.getWorldPosition());
        }

        // IDK this may be a bad thinng to add
        formed = isMachineWhole();

        if(formed) {
            updateBlockStatesFormed();
            if (this.logic != null) this.logic.initFuel(this.fuelRodList.size(), getModeratorsEfficiency());
        } else {
            updateBlockStatesUnformed();
        }

        super.onBlockAdded(part);
    }

    private double getModeratorsEfficiency() {
        return 1.0;
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
            case POWER_TAP -> powerTapList.remove(part.getWorldPosition());
            case FUEL_IO -> fuelIOList.remove(part.getWorldPosition());
        }
        recalculateBoundingBox();
        formed = isMachineWhole();

        if (formed) {
            updateBlockStatesFormed();
        } else {
            updateBlockStatesUnformed();
        }
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
