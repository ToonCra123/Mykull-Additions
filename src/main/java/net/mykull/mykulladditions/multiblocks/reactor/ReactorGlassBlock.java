package net.mykull.mykulladditions.multiblocks.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class ReactorGlassBlock extends Block {

    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    public static final BooleanProperty R_NORTH = BooleanProperty.create("r_north");
    public static final BooleanProperty R_SOUTH = BooleanProperty.create("r_south");
    public static final BooleanProperty R_WEST = BooleanProperty.create("r_west");
    public static final BooleanProperty R_EAST = BooleanProperty.create("r_east");
    public static final BooleanProperty R_UP = BooleanProperty.create("r_up");
    public static final BooleanProperty R_DOWN = BooleanProperty.create("r_down");

    public ReactorGlassBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        return calculateState(world, pos, defaultBlockState());
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return calculateState(level, pos, state);
    }

    @Override
    protected boolean skipRendering(BlockState state, BlockState adjacentState, Direction direction) {
        if (adjacentState.is(this)) {
            return true;
        }
        return super.skipRendering(state, adjacentState, direction);
    }

    @Nonnull
    public static BlockState calculateState(LevelAccessor world, BlockPos pos, BlockState state) {
        boolean north = getConnectorType(world, pos, Direction.NORTH);
        boolean south = getConnectorType(world, pos, Direction.SOUTH);
        boolean west = getConnectorType(world, pos, Direction.WEST);
        boolean east = getConnectorType(world, pos, Direction.EAST);
        boolean up = getConnectorType(world, pos, Direction.UP);
        boolean down = getConnectorType(world, pos, Direction.DOWN);

        boolean r_north = shouldRenderSide(world, pos, state, Direction.NORTH);
        boolean r_south = shouldRenderSide(world, pos, state, Direction.SOUTH);
        boolean r_west = shouldRenderSide(world, pos, state, Direction.WEST);
        boolean r_east = shouldRenderSide(world, pos, state, Direction.EAST);
        boolean r_up = shouldRenderSide(world, pos, state, Direction.UP);
        boolean r_down = shouldRenderSide(world, pos, state, Direction.DOWN);

        return state
                .setValue(NORTH, north)
                .setValue(SOUTH, south)
                .setValue(WEST, west)
                .setValue(EAST, east)
                .setValue(UP, up)
                .setValue(DOWN, down)
                .setValue(R_NORTH, r_north)
                .setValue(R_SOUTH, r_south)
                .setValue(R_WEST, r_west)
                .setValue(R_EAST, r_east)
                .setValue(R_UP, r_up)
                .setValue(R_DOWN, r_down);
    }

    private static boolean getConnectorType(BlockGetter world, BlockPos connectorPos, Direction facing) {
        BlockPos pos = connectorPos.relative(facing);
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        return block instanceof ReactorGlassBlock;
    }

    private static boolean shouldRenderSide(BlockGetter world, BlockPos pos, BlockState state, Direction dir) {
        BlockPos neighborPos = pos.relative(dir);
        BlockState neighborState = world.getBlockState(neighborPos);

        // If neighbor block is the same glass block or another transparent glass block, don't render the side
        return neighborState.is(Blocks.AIR);

        // Otherwise render
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NORTH, SOUTH, WEST, EAST, UP, DOWN, R_NORTH, R_SOUTH, R_WEST, R_EAST, R_UP, R_DOWN);
    }
}
