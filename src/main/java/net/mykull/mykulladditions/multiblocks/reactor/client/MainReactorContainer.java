package net.mykull.mykulladditions.multiblocks.reactor.client;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.mykull.mykulladditions.Registration;
import net.mykull.mykulladditions.multiblocks.reactor.io.FuelIOBlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

import static net.mykull.mykulladditions.common.blockentities.GeneratorBlockEntity.SLOT;
import static net.mykull.mykulladditions.common.blockentities.GeneratorBlockEntity.SLOT_COUNT;
import static net.mykull.mykulladditions.multiblocks.reactor.ReactorMBLogic.INPUT_SLOT;
import static net.mykull.mykulladditions.multiblocks.reactor.ReactorMBLogic.OUTPUT_SLOT;

public class MainReactorContainer extends AbstractContainerMenu {


    private final BlockPos pos;
    private int power;

    public MainReactorContainer(int windowId, Player player, BlockPos pos) {
        super(Registration.REACTOR_FUEL_CONTAINER.get(), windowId);
        this.pos = pos;
        if (player.level().getBlockEntity(pos) instanceof FuelIOBlockEntity be) {
            addSlot(new SlotItemHandler(be.getItems(), INPUT_SLOT, 28, 27));
            addSlot(new SlotItemHandler(be.getItems(), OUTPUT_SLOT, 80, 25));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), pos), player, Registration.REACTOR_FUEL_IO.get());
    }
}
