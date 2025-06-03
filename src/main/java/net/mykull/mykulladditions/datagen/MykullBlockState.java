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

    public MykullBlockState(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MykullsAdditions.MODID, exFileHelper);
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

    private void registerCable() {
        BlockModelBuilder model = models().getBuilder("cable")
                .parent(models().getExistingFile(mcLoc("cube")))
                .customLoader((builder, helper) -> new CableLoaderBuilder(CableModelLoader.GENERATOR_LOADER, builder, helper))
                .end();
        simpleBlock(Registration.CABLE_BLOCK.get(), model);
    }


    @Override
    protected void registerStatesAndModels() {
        simpleBlock(Registration.COMPLEX_BLOCK.get());
        simpleBlock(Registration.SIMPLE_BLOCK.get());
        registerGenerator();
        registerCable();
    }



    public static class CableLoaderBuilder extends CustomLoaderBuilder<BlockModelBuilder> {

        public CableLoaderBuilder(ResourceLocation loader, BlockModelBuilder parent, ExistingFileHelper existingFileHelper) {
            super(loader, parent, existingFileHelper, false);
        }

        @Override
        public JsonObject toJson(JsonObject json) {
            JsonObject obj = super.toJson(json);
            return obj;
        }
    }
}
