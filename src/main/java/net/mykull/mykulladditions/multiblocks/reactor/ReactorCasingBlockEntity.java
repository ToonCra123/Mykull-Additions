package net.mykull.mykulladditions.multiblocks.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.mykull.mykulladditions.Registration;
import net.mykull.mykulladditions.multiblocks.reactor.part.ReactorMultiblockPart;
import net.mykull.mykulladditions.multiblocks.reactor.part.ReactorPartTypes;

public class ReactorCasingBlockEntity extends ReactorMultiblockPart {
    public ReactorCasingBlockEntity(BlockPos pos, BlockState blockState) {
        super(Registration.REACTOR_CASINNG_ENTITY.get(), pos, blockState);
    }

    @Override
    public ReactorPartTypes getPartType() {
        return ReactorPartTypes.CASING;
    }
}
