package net.mykull.mykulladditions.multiblocks.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.mykull.mykulladditions.MykullsAdditions;
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
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {

        if (player instanceof ServerPlayer sp) {
            BlockEntity be = level.getBlockEntity(pos);

            if(be instanceof ReactorMultiblockPart reactor) {
                if (reactor.getController() instanceof ReactorMBController r)
                    MykullsAdditions.LOGGER.debug("Position of bottom left {} {} {}", r.getBBleft().getX(), r.getBBleft().getY(), r.getBBleft().getZ());
            }
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }
}
