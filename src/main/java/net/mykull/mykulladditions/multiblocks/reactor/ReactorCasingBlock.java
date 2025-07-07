package net.mykull.mykulladditions.multiblocks.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.mykull.mykulladditions.multiblocks.reactor.states.CasingBlockStateTypes;
import org.jetbrains.annotations.Nullable;

public class ReactorCasingBlock extends Block implements EntityBlock {

    public static final EnumProperty<CasingBlockStateTypes> CASING_TYPE = EnumProperty.create("casing_type", CasingBlockStateTypes.class);


    public ReactorCasingBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(CASING_TYPE, CasingBlockStateTypes.UNFORMED));
    }
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ReactorCasingBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CASING_TYPE);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.defaultBlockState().setValue(CASING_TYPE, CasingBlockStateTypes.UNFORMED);
    }
}
