package net.mykull.mykulladditions.multiblocks.reactor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.mykull.mykulladditions.Registration;
import net.mykull.mykulladditions.multiblocks.reactor.part.ReactorMultiblockPart;
import net.mykull.mykulladditions.multiblocks.reactor.part.ReactorPartEnum;

public class ReactorPartBlockEntity extends ReactorMultiblockPart {
    public ReactorPartBlockEntity(BlockPos pos, BlockState blockState) {
        super(Registration.REACTOR_CASINNG_ENTITY.get(), pos, blockState);
    }

    @Override
    public ReactorPartEnum getPartType() {
        return ReactorPartEnum.CASING;
    }
}
