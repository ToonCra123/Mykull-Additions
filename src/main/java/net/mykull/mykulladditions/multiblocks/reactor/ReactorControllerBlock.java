package net.mykull.mykulladditions.multiblocks.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

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

            if(be != null && be instanceof ReactorControllerBlockEntity reactor) {
                if (reactor.formed) {
                    sp.sendSystemMessage(Component.literal("Reactor is formed"));
                } else {
                    sp.sendSystemMessage(Component.literal("Reactor is not formed"));
                }
            }
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }
}
