package net.mykull.mykulladditions.datagen;

import net.minecraft.data.PackOutput;
import net.mykull.mykulladditions.MykullsAdditions;
import net.mykull.mykulladditions.Registration;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MykullBlockState extends BlockStateProvider {

    public MykullBlockState(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MykullsAdditions.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(Registration.COMPLEX_BLOCK.get());
        simpleBlock(Registration.SIMPLE_BLOCK.get());
    }
}
