package net.mykull.mykulladditions.multiblocks.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.mykull.mykulladditions.MykullsAdditions;
import net.mykull.mykulladditions.common.blockentities.GeneratorBlockEntity;
import net.mykull.mykulladditions.multiblocks.reactor.part.ReactorMultiblockPart;
import org.jetbrains.annotations.Nullable;

public class ReactorControllerBlock extends Block implements EntityBlock {

    public ReactorControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ReactorControllerBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return null;
        } else {
            return (lvl, pos, st, be) -> {
                if (be instanceof ReactorControllerBlockEntity controllerBlockEntity) {
                    controllerBlockEntity.tickServer();
                }
            };
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {

        if (player instanceof ServerPlayer sp) {
            BlockEntity be = level.getBlockEntity(pos);

            if(be instanceof ReactorMultiblockPart reactor) {
                if (reactor.getController() instanceof ReactorMBController r) {
                    MykullsAdditions.LOGGER.debug("Size of multiblock Width: {} Depth: {} Height: {}", r.getWidth(), r.getDepth(), r.getHeight());
                    MykullsAdditions.LOGGER.debug("Formed State: {}", r.formed);
                }
            }
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }
}
