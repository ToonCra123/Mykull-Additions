package net.mykull.mykulladditions.multiblocks.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.mykull.mykulladditions.Registration;
import net.mykull.mykulladditions.multiblocks.reactor.part.ReactorMultiblockPart;
import net.mykull.mykulladditions.multiblocks.reactor.part.ReactorPartTypes;

public class ControlRodBlockEntity extends ReactorMultiblockPart {
    public ControlRodBlockEntity(BlockPos pos, BlockState blockState) {
        super(Registration.REACTOR_CONTROL_ROD_ENTITY.get(), pos, blockState);
    }

    @Override
    public ReactorPartTypes getPartType() {
        return ReactorPartTypes.CONTROL_ROD;
    }
}
