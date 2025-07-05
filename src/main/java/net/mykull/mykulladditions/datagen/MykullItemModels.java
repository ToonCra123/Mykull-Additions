package net.mykull.mykulladditions.datagen;

import net.minecraft.data.PackOutput;
import net.mykull.mykulladditions.MykullsAdditions;
import net.mykull.mykulladditions.Registration;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MykullItemModels extends ItemModelProvider {
    public MykullItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MykullsAdditions.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(Registration.URANIUM_ORE.getId().getPath(), modLoc("block/uranium_ore"));
        withExistingParent(Registration.SIMPLE_BLOCK.getId().getPath(), modLoc("block/simple_block"));
        withExistingParent(Registration.COMPLEX_BLOCK.getId().getPath(), modLoc("block/complex_block"));
        withExistingParent(Registration.GENERATOR_BLOCK.getId().getPath(), modLoc("block/generator_block_off"));
        withExistingParent(Registration.CABLE_BLOCK.getId().getPath(), modLoc("block/cable"));
        withExistingParent(Registration.REACTOR_CONTROLLER.getId().getPath(), modLoc("block/reactor_controller"));
        withExistingParent(Registration.REACTOR_CASING.getId().getPath(), modLoc("block/reactor_casing"));
        basicItem(Registration.RAW_URANIUM.get());
        basicItem(Registration.URANIUM_INGOT.get());
    }
}
