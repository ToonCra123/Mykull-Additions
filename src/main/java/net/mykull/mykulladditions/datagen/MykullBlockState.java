package net.mykull.mykulladditions.datagen;

import com.google.gson.JsonObject;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.mykull.mykulladditions.MykullsAdditions;
import net.mykull.mykulladditions.Registration;
import net.mykull.mykulladditions.common.cables.client.CableModelLoader;
import net.mykull.mykulladditions.multiblocks.reactor.glass.ReactorGlassModelLoader;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.BiConsumer;

public class MykullBlockState extends BlockStateProvider {

    public static final ResourceLocation BOTTOM = ResourceLocation.fromNamespaceAndPath(MykullsAdditions.MODID, "block/complex_block");
    public static final ResourceLocation TOP = ResourceLocation.fromNamespaceAndPath(MykullsAdditions.MODID, "block/complex_block");
    public static final ResourceLocation SIDE = ResourceLocation.fromNamespaceAndPath(MykullsAdditions.MODID, "block/complex_block");

    public static final ResourceLocation CONTROL_ROD_TOP = ResourceLocation.fromNamespaceAndPath(MykullsAdditions.MODID, "block/reactor/control_rod");
    public static final ResourceLocation CONTROL_ROD_SIDE = ResourceLocation.fromNamespaceAndPath(MykullsAdditions.MODID, "block/reactor/casing_corner");

    public static final ResourceLocation FUEL_ROD_TOP = ResourceLocation.fromNamespaceAndPath(MykullsAdditions.MODID, "block/reactor/fuel_rod_top");
    public static final ResourceLocation FUEL_ROD_SIDE = ResourceLocation.fromNamespaceAndPath(MykullsAdditions.MODID, "block/reactor/fuel_rod_side");

    public MykullBlockState(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MykullsAdditions.MODID, exFileHelper);
    }

    private void registerControlRod() {
        simpleBlock(Registration.REACTOR_CONTROL_ROD.get(),
                models().cube(
                        "reactor_control_rod", // This is the model name — usually matches block ID
                        CONTROL_ROD_TOP,
                        CONTROL_ROD_TOP,
                        CONTROL_ROD_SIDE,
                        CONTROL_ROD_SIDE,
                        CONTROL_ROD_SIDE,
                        CONTROL_ROD_SIDE
                ).texture("particle", CONTROL_ROD_SIDE)
        );
    }

    private void registerFuelRod() {
        simpleBlock(Registration.REACTOR_FUEL_ROD.get(),
                models().cube(
                        "reactor_fuel_rod", // This is the model name — usually matches block ID
                        FUEL_ROD_TOP,
                        FUEL_ROD_TOP,
                        FUEL_ROD_SIDE,
                        FUEL_ROD_SIDE,
                        FUEL_ROD_SIDE,
                        FUEL_ROD_SIDE
                ).texture("particle", FUEL_ROD_SIDE)
        );
    }

    private void registerGenerator() {
        BlockModelBuilder modelOn = models().cube(Registration.GENERATOR_BLOCK.getId().getPath()+"_on", BOTTOM, TOP, modLoc("block/generator_front_on"), SIDE, SIDE, SIDE).texture("particle", SIDE);
        BlockModelBuilder modelOff = models().cube(Registration.GENERATOR_BLOCK.getId().getPath()+"_off", BOTTOM, TOP, modLoc("block/generator_front_off"), SIDE, SIDE, SIDE).texture("particle", SIDE);
        directionBlock(Registration.GENERATOR_BLOCK.get(), (state, builder) -> {
            builder.modelFile(state.getValue(BlockStateProperties.POWERED) ? modelOn : modelOff);
        });
    }

    private VariantBlockStateBuilder directionBlock(Block block, BiConsumer<BlockState, ConfiguredModel.Builder<?>> model) {
        VariantBlockStateBuilder builder = getVariantBuilder(block);
        builder.forAllStates(state -> {
            ConfiguredModel.Builder<?> bld = ConfiguredModel.builder();
            model.accept(state, bld);
            applyRotationBld(bld, state.getValue(BlockStateProperties.FACING));
            return bld.build();
        });
        return builder;
    }

    private void applyRotationBld(ConfiguredModel.Builder<?> builder, Direction direction) {
        switch (direction) {
            case DOWN -> builder.rotationX(90);
            case UP -> builder.rotationX(-90);
            case NORTH -> { }
            case SOUTH -> builder.rotationY(180);
            case WEST -> builder.rotationY(270);
            case EAST -> builder.rotationY(90);
        }
    }

    private void registerEnergyCable() {
        BlockModelBuilder model = models().getBuilder("cable")
                .customLoader((builder, helper) -> new CableLoaderBuilder(CableModelLoader.GENERATOR_LOADER, builder, helper, "energy"))
                .end();
        simpleBlock(Registration.CABLE_BLOCK.get(), model);
    }

    private void registerReactorGlass() {
        BlockModelBuilder model = models().getBuilder("reactor_glass")
                .customLoader((builder, helper) -> new GlassLoaderBuilder(ReactorGlassModelLoader.GENERATOR_LOADER, builder, helper))
                .end();
        simpleBlock(Registration.REACTOR_GLASS.get(), model);
    }


    @Override
    protected void registerStatesAndModels() {
        simpleBlock(Registration.URANIUM_ORE.get());
        simpleBlock(Registration.COMPLEX_BLOCK.get());
        simpleBlock(Registration.SIMPLE_BLOCK.get());


        simpleBlock(Registration.REACTOR_CONTROLLER.get());
        registerControlRod();
        registerFuelRod();

        registerGenerator();
        registerEnergyCable();
        registerReactorGlass();

    }



    public static class CableLoaderBuilder extends CustomLoaderBuilder<BlockModelBuilder> {

        private final String type;

        public CableLoaderBuilder(ResourceLocation loader, BlockModelBuilder parent, ExistingFileHelper existingFileHelper, String type) {
            super(loader, parent, existingFileHelper, false);
            this.type = type;
        }

        @Override
        public JsonObject toJson(JsonObject json) {
            JsonObject obj = super.toJson(json);

            obj.addProperty("type", type);
            return obj;
        }
    }

    public static class GlassLoaderBuilder extends CustomLoaderBuilder<BlockModelBuilder> {


        public GlassLoaderBuilder(ResourceLocation loader, BlockModelBuilder parent, ExistingFileHelper existingFileHelper) {
            super(loader, parent, existingFileHelper, false);
        }

        @Override
        public JsonObject toJson(JsonObject json) {
            return super.toJson(json);
        }
    }
}
