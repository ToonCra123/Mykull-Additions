package net.mykull.mykulladditions.multiblocks.reactor.io;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.mykull.mykulladditions.Registration;
import net.mykull.mykulladditions.multiblocks.reactor.part.ReactorMultiblockPart;
import net.mykull.mykulladditions.multiblocks.reactor.part.ReactorPartTypes;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class FuelIOBlockEntity extends ReactorMultiblockPart {

    private static final ItemStackHandler EMPTY_HANDLER = new ItemStackHandler(2);

    public FuelIOBlockEntity(BlockPos pos, BlockState blockState) {
        super(Registration.REACTOR_FUEL_IO_ENTITY.get(), pos, blockState);
    }

    public boolean shouldGUI() {
        return reactorController.formed;
    }

    public IItemHandler getItemHandler() {
        if (reactorController != null && reactorController.logic != null) {
            return reactorController.logic.getItemHandler();
        }
        return EMPTY_HANDLER;
    }

    public ItemStackHandler getItems() {
        if (reactorController != null && reactorController.logic != null) {
            return reactorController.logic.getItems();
        }
        return EMPTY_HANDLER;
    }

    @Override
    public ReactorPartTypes getPartType() {
        return ReactorPartTypes.FUEL_IO;
    }
}
