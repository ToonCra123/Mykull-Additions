package net.mykull.mykulladditions.multiblocks.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
import net.mykull.mykulladditions.multiblocks.reactor.client.FuelInputContainer;
import net.mykull.mykulladditions.multiblocks.reactor.client.MainReactorContainer;
import net.mykull.mykulladditions.multiblocks.reactor.io.FuelIOBlockEntity;
import net.mykull.mykulladditions.multiblocks.reactor.part.ReactorMultiblockPart;
import org.jetbrains.annotations.Nullable;

public class ReactorControllerBlock extends Block implements EntityBlock {
    public static final String SCREEN = "mykullsadditions.screen.reactor_main";


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

        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ReactorControllerBlockEntity f) {
                if (f.shouldGUI()) {
                    MenuProvider containerProvider = new MenuProvider() {
                        @Override
                        public Component getDisplayName() {
                            return Component.translatable(SCREEN);
                        }

                        @Override
                        public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
                            return new MainReactorContainer(windowId, playerEntity, pos);
                        }
                    };
                    player.openMenu(containerProvider, buf -> buf.writeBlockPos(pos));
                } else {
                    player.displayClientMessage(Component.literal("Reactor is not formed."), true);
                }
            } else {
                throw new IllegalStateException("Ummm... is missing at " + pos);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
