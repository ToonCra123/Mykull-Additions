package net.mykull.mykulladditions.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemDisplayContext;
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
        withExistingParent(Registration.CABLE_BLOCK.getId().getPath(), modLoc("block/cable"))
                .transforms()
                .transform(ItemDisplayContext.GUI)
                    .rotation(30, 225, 0)
                    .scale(0.625f)
                    .end()
                .transform(ItemDisplayContext.GROUND)
                    .translation(0, 3, 0)
                    .scale(0.25f)
                    .end()
                .transform(ItemDisplayContext.FIXED)
                    .scale(0.5f)
                    .end()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                    .rotation(75, 45, 0)
                    .translation(0, 2.5f, 0)
                    .scale(0.375f)
                    .end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                    .rotation(0, 45, 0)
                    .scale(0.4f)
                    .end()
                .end(); // close transforms

        withExistingParent(Registration.REACTOR_CONTROLLER.getId().getPath(), modLoc("block/reactor_controller"));
        withExistingParent(Registration.REACTOR_CONTROL_ROD.getId().getPath(), modLoc("block/reactor_control_rod"));
        withExistingParent(Registration.REACTOR_FUEL_ROD.getId().getPath(), modLoc("block/reactor_fuel_rod"));
        basicItem(Registration.RAW_URANIUM.get());
        basicItem(Registration.URANIUM_INGOT.get());
    }
}
