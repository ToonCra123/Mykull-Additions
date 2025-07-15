package net.mykull.mykulladditions.multiblocks.reactor.io;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.mykull.mykulladditions.common.blockentities.GeneratorBlockEntity;
import net.mykull.mykulladditions.common.containers.GeneratorContainer;
import net.mykull.mykulladditions.multiblocks.reactor.Fuel;
import net.mykull.mykulladditions.multiblocks.reactor.client.FuelInputContainer;
import org.jetbrains.annotations.Nullable;

public class FuelIOBlock extends Block implements EntityBlock {

    public static final String SCREEN = "mykullsadditions.screen.reactor_fuel";

    public FuelIOBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof FuelIOBlockEntity f) {
                if (f.shouldGUI()) {
                    MenuProvider containerProvider = new MenuProvider() {
                        @Override
                        public Component getDisplayName() {
                            return Component.translatable(SCREEN);
                        }

                        @Override
                        public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
                            return new FuelInputContainer(windowId, playerEntity, pos);
                        }
                    };
                    player.openMenu(containerProvider, buf -> buf.writeBlockPos(pos));
                } else {
                    player.displayClientMessage(Component.literal("Reactor is not formed."), true);
                }
            } else {
                throw new IllegalStateException("FuelIOBlockEntity is missing at " + pos);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FuelIOBlockEntity(pos, state);
    }
}
