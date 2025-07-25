package net.mykull.mykulladditions.multiblocks.reactor.glass;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.mykull.mykulladditions.MykullsAdditions;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

import java.util.function.Function;

public class ReactorGlassModelLoader implements IGeometryLoader<ReactorGlassModelLoader.GlassModelGeometry> {
    public static final ResourceLocation GENERATOR_LOADER = ResourceLocation.fromNamespaceAndPath(MykullsAdditions.MODID, "glassloader");

    public static void register(ModelEvent.RegisterGeometryLoaders event) {
        event.register(GENERATOR_LOADER, new ReactorGlassModelLoader());
    }

    @Override
    public ReactorGlassModelLoader.GlassModelGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        return new GlassModelGeometry();
    }

    public static class GlassModelGeometry implements IUnbakedGeometry<ReactorGlassModelLoader.GlassModelGeometry> {

        GlassModelGeometry() {
        }

        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
            return new ReactorGlassBakedModel(context);
        }
    }
}
