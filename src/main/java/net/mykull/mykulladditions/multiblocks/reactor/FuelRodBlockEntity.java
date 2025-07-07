package net.mykull.mykulladditions.multiblocks.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.mykull.mykulladditions.Registration;
import net.mykull.mykulladditions.multiblocks.reactor.part.ReactorMultiblockPart;
import net.mykull.mykulladditions.multiblocks.reactor.part.ReactorPartTypes;

public class FuelRodBlockEntity extends ReactorMultiblockPart {
    public FuelRodBlockEntity(BlockPos pos, BlockState blockState) {
        super(Registration.REACTOR_FUEL_ROD_ENTITY.get(), pos, blockState);
    }

    @Override
    public ReactorPartTypes getPartType() {
        return ReactorPartTypes.FUEL_ROD;
    }
}
