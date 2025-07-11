package net.mykull.mykulladditions.common.cables.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.mykull.mykulladditions.MykullsAdditions;
import net.mykull.mykulladditions.common.cables.ConnectorType;
import net.mykull.mykulladditions.common.cables.blocks.CableBlock;
import net.mykull.mykulladditions.common.cables.client.ConnectedPatterns.Pattern;
import net.mykull.mykulladditions.common.cables.client.ConnectedPatterns.QuadSetting;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static net.mykull.mykulladditions.common.cables.ConnectorType.*;
import static net.mykull.mykulladditions.common.cables.client.ConnectedPatterns.SpriteIdx.*;
import static net.mykull.mykulladditions.tools.BakedModelHelper.v;
import static net.mykull.mykulladditions.tools.CableBakedModelHelper.*;

public class CableBakedModel implements IDynamicBakedModel {

    private final IGeometryBakingContext context;

    private TextureAtlasSprite spriteNoneCable;
    private TextureAtlasSprite spriteNormalCable;
    private TextureAtlasSprite spriteSide;

    private final String TYPE;

    static {
        // For all possible patterns we define the sprite to use and the rotation. Note that each
        // pattern looks at the existance of a cable section for each of the four directions
        // excluding the one we are looking at.
        ConnectedPatterns.PATTERNS.put(Pattern.of(false, false, false, false), QuadSetting.of(SPRITE_NONE, 0));
        ConnectedPatterns.PATTERNS.put(Pattern.of(true, false, false, false), QuadSetting.of(SPRITE_END, 3));
        ConnectedPatterns.PATTERNS.put(Pattern.of(false, true, false, false), QuadSetting.of(SPRITE_END, 0));
        ConnectedPatterns.PATTERNS.put(Pattern.of(false, false, true, false), QuadSetting.of(SPRITE_END, 1));
        ConnectedPatterns.PATTERNS.put(Pattern.of(false, false, false, true), QuadSetting.of(SPRITE_END, 2));
        ConnectedPatterns.PATTERNS.put(Pattern.of(true, true, false, false), QuadSetting.of(SPRITE_CORNER, 0));
        ConnectedPatterns.PATTERNS.put(Pattern.of(false, true, true, false), QuadSetting.of(SPRITE_CORNER, 1));
        ConnectedPatterns.PATTERNS.put(Pattern.of(false, false, true, true), QuadSetting.of(SPRITE_CORNER, 2));
        ConnectedPatterns.PATTERNS.put(Pattern.of(true, false, false, true), QuadSetting.of(SPRITE_CORNER, 3));
        ConnectedPatterns.PATTERNS.put(Pattern.of(false, true, false, true), QuadSetting.of(SPRITE_STRAIGHT, 0));
        ConnectedPatterns.PATTERNS.put(Pattern.of(true, false, true, false), QuadSetting.of(SPRITE_STRAIGHT, 1));
        ConnectedPatterns.PATTERNS.put(Pattern.of(true, true, true, false), QuadSetting.of(SPRITE_THREE, 0));
        ConnectedPatterns.PATTERNS.put(Pattern.of(false, true, true, true), QuadSetting.of(SPRITE_THREE, 1));
        ConnectedPatterns.PATTERNS.put(Pattern.of(true, false, true, true), QuadSetting.of(SPRITE_THREE, 2));
        ConnectedPatterns.PATTERNS.put(Pattern.of(true, true, false, true), QuadSetting.of(SPRITE_THREE, 3));
        ConnectedPatterns.PATTERNS.put(Pattern.of(true, true, true, true), QuadSetting.of(SPRITE_CROSS, 0));
    }

    public CableBakedModel(IGeometryBakingContext context, String type) {
        this.context = context;
        TYPE = type;
    }

    private void initTextures() {
        if (spriteNormalCable == null || spriteNoneCable == null || spriteSide == null) {
            try {
                spriteNormalCable = getTexture("block/cable/" + TYPE + "/normal");
                spriteNoneCable = getTexture("block/cable/" + TYPE + "/none");
                spriteSide = getTexture("block/cable/" + TYPE + "/side");
            } catch (Exception e) {
                MykullsAdditions.LOGGER.warn("Failed to load cable textures for JEI render: " + TYPE, e);
            }
        }
    }

    // All textures are baked on a big texture atlas. This function gets the texture from that atlas
    private TextureAtlasSprite getTexture(String path) {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(ResourceLocation.fromNamespaceAndPath(MykullsAdditions.MODID, path));
    }

    private TextureAtlasSprite getSpriteNormal(ConnectedPatterns.SpriteIdx idx) {
        initTextures();
        return switch (idx) {
            case SPRITE_NONE, SPRITE_END, SPRITE_THREE, SPRITE_CORNER, SPRITE_CROSS -> spriteNoneCable;
            case SPRITE_STRAIGHT -> spriteNormalCable;
        };
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    @NotNull
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType layer) {
        initTextures();
        List<BakedQuad> quads = new ArrayList<>();
        if (side == null && (layer == null || RenderType.translucent().equals(layer) || RenderType.solid().equals(layer))) {
            // Called with the blockstate from our block. Here we get the values of the six properties and pass that to
            // our baked model implementation. If state == null we are called from the inventory and we use the default
            // values for the properties
            ConnectorType north, south, west, east, up, down;
            if (state != null) {
                north = state.getValue(CableBlock.NORTH);
                south = state.getValue(CableBlock.SOUTH);
                west = state.getValue(CableBlock.WEST);
                east = state.getValue(CableBlock.EAST);
                up = state.getValue(CableBlock.UP);
                down = state.getValue(CableBlock.DOWN);
            } else {
                // In inventory
                north = south = west = east = up = down = NONE;
            }

            TextureAtlasSprite spriteCable = spriteNormalCable;
            Function<ConnectedPatterns.SpriteIdx, TextureAtlasSprite> spriteGetter = this::getSpriteNormal;

            // 16 - 8

            double o = 0.3125;      // Thickness of the cable. .0 would be full block, .5 is infinitely thin.
            double p = .25;      // Thickness of the connector as it is put on the connecting block
            double q = .25;      // The wideness of the connector

            // For each side we either cap it off if there is no similar block adjacent on that side
            // or else we extend so that we touch the adjacent block:
            if (up == CABLE) {
                quads.add(bakeQuadNormal(v(1 - o, 1, o), v(1 - o, 1, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, 1 - o, o), spriteCable));
                quads.add(bakeQuadNormal(v(o, 1, 1 - o), v(o, 1, o), v(o, 1 - o, o), v(o, 1 - o, 1 - o), spriteCable));
                quads.add(bakeQuadNormal(v(o, 1, o), v(1 - o, 1, o), v(1 - o, 1 - o, o), v(o, 1 - o, o), spriteCable));
                quads.add(bakeQuadNormal(v(o, 1 - o, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, 1, 1 - o), v(o, 1, 1 - o), spriteCable));
            } else if (up == BLOCK) {
                quads.add(bakeQuadNormal(v(1 - o, 1 - p, o), v(1 - o, 1 - p, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, 1 - o, o), spriteCable));
                quads.add(bakeQuadNormal(v(o, 1 - p, 1 - o), v(o, 1 - p, o), v(o, 1 - o, o), v(o, 1 - o, 1 - o), spriteCable));
                quads.add(bakeQuadNormal(v(o, 1 - p, o), v(1 - o, 1 - p, o), v(1 - o, 1 - o, o), v(o, 1 - o, o), spriteCable));
                quads.add(bakeQuadNormal(v(o, 1 - o, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, 1 - p, 1 - o), v(o, 1 - p, 1 - o), spriteCable));

                quads.add(bakeQuadConnector(v(1 - q, 1 - p, q), v(1 - q, 1, q), v(1 - q, 1, 1 - q), v(1 - q, 1 - p, 1 - q), spriteSide, 2));
                quads.add(bakeQuadConnector(v(q, 1 - p, 1 - q), v(q, 1, 1 - q), v(q, 1, q), v(q, 1 - p, q), spriteSide, 2));
                quads.add(bakeQuadConnector(v(q, 1, q), v(1 - q, 1, q), v(1 - q, 1 - p, q), v(q, 1 - p, q), spriteSide, 1));
                quads.add(bakeQuadConnector(v(q, 1 - p, 1 - q), v(1 - q, 1 - p, 1 - q), v(1 - q, 1, 1 - q), v(q, 1, 1 - q), spriteSide, 3));

                quads.add(bakeQuadCenterConnector(v(q, 1 - p, q), v(1 - q, 1 - p, q), v(1 - q, 1 - p, 1 - q), v(q, 1 - p, 1 - q), spriteSide));
                quads.add(bakeQuadCenterConnector(v(q, 1, q), v(q, 1, 1 - q), v(1 - q, 1, 1 - q), v(1 - q, 1, q), spriteSide));
            } else {
                QuadSetting pattern = ConnectedPatterns.findPattern(west, south, east, north);
                quads.add(bakeQuadCenter(v(o, 1 - o, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, 1 - o, o), v(o, 1 - o, o), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }

            if (down == CABLE) {
                quads.add(bakeQuadNormal(v(1 - o, o, o), v(1 - o, o, 1 - o), v(1 - o, 0, 1 - o), v(1 - o, 0, o), spriteCable));
                quads.add(bakeQuadNormal(v(o, o, 1 - o), v(o, o, o), v(o, 0, o), v(o, 0, 1 - o), spriteCable));
                quads.add(bakeQuadNormal(v(o, o, o), v(1 - o, o, o), v(1 - o, 0, o), v(o, 0, o), spriteCable));
                quads.add(bakeQuadNormal(v(o, 0, 1 - o), v(1 - o, 0, 1 - o), v(1 - o, o, 1 - o), v(o, o, 1 - o), spriteCable));
            } else if (down == BLOCK) {
                quads.add(bakeQuadNormal(v(1 - o, o, o), v(1 - o, o, 1 - o), v(1 - o, p, 1 - o), v(1 - o, p, o), spriteCable));
                quads.add(bakeQuadNormal(v(o, o, 1 - o), v(o, o, o), v(o, p, o), v(o, p, 1 - o), spriteCable));
                quads.add(bakeQuadNormal(v(o, o, o), v(1 - o, o, o), v(1 - o, p, o), v(o, p, o), spriteCable));
                quads.add(bakeQuadNormal(v(o, p, 1 - o), v(1 - o, p, 1 - o), v(1 - o, o, 1 - o), v(o, o, 1 - o), spriteCable));

                quads.add(bakeQuadConnector(v(1 - q, 0, q), v(1 - q, p, q), v(1 - q, p, 1 - q), v(1 - q, 0, 1 - q), spriteSide)); // east
                quads.add(bakeQuadConnector(v(q, 0, 1 - q), v(q, p, 1 - q), v(q, p, q), v(q, 0, q), spriteSide)); // west
                quads.add(bakeQuadConnector(v(q, p, q), v(1 - q, p, q), v(1 - q, 0, q), v(q, 0, q), spriteSide, 3)); // north
                quads.add(bakeQuadConnector(v(q, 0, 1 - q), v(1 - q, 0, 1 - q), v(1 - q, p, 1 - q), v(q, p, 1 - q), spriteSide, 1)); // south

                quads.add(bakeQuadCenterConnector(v(q, p, 1 - q), v(1 - q, p, 1 - q), v(1 - q, p, q), v(q, p, q), spriteSide));
                quads.add(bakeQuadCenterConnector(v(q, 0, 1 - q), v(q, 0, q), v(1 - q, 0, q), v(1 - q, 0, 1 - q), spriteSide));
            } else {
                QuadSetting pattern = ConnectedPatterns.findPattern(west, north, east, south);
                quads.add(bakeQuadCenter(v(o, o, o), v(1 - o, o, o), v(1 - o, o, 1 - o), v(o, o, 1 - o), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }

            if (east == CABLE) {
                quads.add(bakeQuadNormal(v(1, 1 - o, 1 - o), v(1, 1 - o, o), v(1 - o, 1 - o, o), v(1 - o, 1 - o, 1 - o), spriteCable));
                quads.add(bakeQuadNormal(v(1, o, o), v(1, o, 1 - o), v(1 - o, o, 1 - o), v(1 - o, o, o), spriteCable));
                quads.add(bakeQuadNormal(v(1, 1 - o, o), v(1, o, o), v(1 - o, o, o), v(1 - o, 1 - o, o), spriteCable));
                quads.add(bakeQuadNormal(v(1, o, 1 - o), v(1, 1 - o, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, o, 1 - o), spriteCable));
            } else if (east == BLOCK) {
                quads.add(bakeQuadNormal(v(1 - p, 1 - o, 1 - o), v(1 - p, 1 - o, o), v(1 - o, 1 - o, o), v(1 - o, 1 - o, 1 - o), spriteCable));
                quads.add(bakeQuadNormal(v(1 - p, o, o), v(1 - p, o, 1 - o), v(1 - o, o, 1 - o), v(1 - o, o, o), spriteCable));
                quads.add(bakeQuadNormal(v(1 - p, 1 - o, o), v(1 - p, o, o), v(1 - o, o, o), v(1 - o, 1 - o, o), spriteCable));
                quads.add(bakeQuadNormal(v(1 - p, o, 1 - o), v(1 - p, 1 - o, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, o, 1 - o), spriteCable));

                quads.add(bakeQuadConnector(v(1 - p, 1 - q, 1 - q), v(1, 1 - q, 1 - q), v(1, 1 - q, q), v(1 - p, 1 - q, q), spriteSide, 2));
                quads.add(bakeQuadConnector(v(1 - p, q, q), v(1, q, q), v(1, q, 1 - q), v(1 - p, q, 1 - q), spriteSide, 2));
                quads.add(bakeQuadConnector(v(1 - p, 1 - q, q), v(1, 1 - q, q), v(1, q, q), v(1 - p, q, q), spriteSide, 2));
                quads.add(bakeQuadConnector(v(1 - p, q, 1 - q), v(1, q, 1 - q), v(1, 1 - q, 1 - q), v(1 - p, 1 - q, 1 - q), spriteSide, 2));

                quads.add(bakeQuadCenterConnector(v(1 - p, q, 1 - q), v(1 - p, 1 - q, 1 - q), v(1 - p, 1 - q, q), v(1 - p, q, q), spriteSide));
                quads.add(bakeQuadCenterConnector(v(1, q, 1 - q), v(1, q, q), v(1, 1 - q, q), v(1, 1 - q, 1 - q), spriteSide));
            } else {
                QuadSetting pattern = ConnectedPatterns.findPattern(down, north, up, south);
                quads.add(bakeQuadCenter(v(1 - o, o, o), v(1 - o, 1 - o, o), v(1 - o, 1 - o, 1 - o), v(1 - o, o, 1 - o), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }

            if (west == CABLE) {
                quads.add(bakeQuadNormal(v(o, 1 - o, 1 - o), v(o, 1 - o, o), v(0, 1 - o, o), v(0, 1 - o, 1 - o), spriteCable));
                quads.add(bakeQuadNormal(v(o, o, o), v(o, o, 1 - o), v(0, o, 1 - o), v(0, o, o), spriteCable));
                quads.add(bakeQuadNormal(v(o, 1 - o, o), v(o, o, o), v(0, o, o), v(0, 1 - o, o), spriteCable));
                quads.add(bakeQuadNormal(v(o, o, 1 - o), v(o, 1 - o, 1 - o), v(0, 1 - o, 1 - o), v(0, o, 1 - o), spriteCable));
            } else if (west == BLOCK) {
                quads.add(bakeQuadNormal(v(o, 1 - o, 1 - o), v(o, 1 - o, o), v(p, 1 - o, o), v(p, 1 - o, 1 - o), spriteCable));
                quads.add(bakeQuadNormal(v(o, o, o), v(o, o, 1 - o), v(p, o, 1 - o), v(p, o, o), spriteCable));
                quads.add(bakeQuadNormal(v(o, 1 - o, o), v(o, o, o), v(p, o, o), v(p, 1 - o, o), spriteCable));
                quads.add(bakeQuadNormal(v(o, o, 1 - o), v(o, 1 - o, 1 - o), v(p, 1 - o, 1 - o), v(p, o, 1 - o), spriteCable));

                quads.add(bakeQuadConnector(v(0, 1 - q, 1 - q), v(p, 1 - q, 1 - q), v(p, 1 - q, q), v(0, 1 - q, q), spriteSide));
                quads.add(bakeQuadConnector(v(0, q, q), v(p, q, q), v(p, q, 1 - q), v(0, q, 1 - q), spriteSide));
                quads.add(bakeQuadConnector(v(0, 1 - q, q), v(p, 1 - q, q), v(p, q, q), v(0, q, q), spriteSide));
                quads.add(bakeQuadConnector(v(0, q, 1 - q), v(p, q, 1 - q), v(p, 1 - q, 1 - q), v(0, 1 - q, 1 - q), spriteSide));

                quads.add(bakeQuadCenterConnector(v(p, q, q), v(p, 1 - q, q), v(p, 1 - q, 1 - q), v(p, q, 1 - q), spriteSide));
                quads.add(bakeQuadCenterConnector(v(0, q, q), v(0, q, 1 - q), v(0, 1 - q, 1 - q), v(0, 1 - q, q), spriteSide));
            } else {
                QuadSetting pattern = ConnectedPatterns.findPattern(down, south, up, north);
                quads.add(bakeQuadCenter(v(o, o, 1 - o), v(o, 1 - o, 1 - o), v(o, 1 - o, o), v(o, o, o), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }

            if (north == CABLE) {
                quads.add(bakeQuadNormal(v(o, 1 - o, o), v(1 - o, 1 - o, o), v(1 - o, 1 - o, 0), v(o, 1 - o, 0), spriteCable));
                quads.add(bakeQuadNormal(v(o, o, 0), v(1 - o, o, 0), v(1 - o, o, o), v(o, o, o), spriteCable));
                quads.add(bakeQuadNormal(v(1 - o, o, 0), v(1 - o, 1 - o, 0), v(1 - o, 1 - o, o), v(1 - o, o, o), spriteCable));
                quads.add(bakeQuadNormal(v(o, o, o), v(o, 1 - o, o), v(o, 1 - o, 0), v(o, o, 0), spriteCable));
            } else if (north == BLOCK) {
                quads.add(bakeQuadNormal(v(o, 1 - o, o), v(1 - o, 1 - o, o), v(1 - o, 1 - o, p), v(o, 1 - o, p), spriteCable));
                quads.add(bakeQuadNormal(v(o, o, p), v(1 - o, o, p), v(1 - o, o, o), v(o, o, o), spriteCable));
                quads.add(bakeQuadNormal(v(1 - o, o, p), v(1 - o, 1 - o, p), v(1 - o, 1 - o, o), v(1 - o, o, o), spriteCable));
                quads.add(bakeQuadNormal(v(o, o, o), v(o, 1 - o, o), v(o, 1 - o, p), v(o, o, p), spriteCable));

                quads.add(bakeQuadConnector(v(q, 1 - q, p), v(1 - q, 1 - q, p), v(1 - q, 1 - q, 0), v(q, 1 - q, 0), spriteSide, 3));
                quads.add(bakeQuadConnector(v(q, q, 0), v(1 - q, q, 0), v(1 - q, q, p), v(q, q, p), spriteSide, 1));
                quads.add(bakeQuadConnector(v(1 - q, q, 0), v(1 - q, 1 - q, 0), v(1 - q, 1 - q, p), v(1 - q, q, p), spriteSide, 1));
                quads.add(bakeQuadConnector(v(q, q, p), v(q, 1 - q, p), v(q, 1 - q, 0), v(q, q, 0), spriteSide, 3));

                quads.add(bakeQuadCenterConnector(v(q, q, p), v(1 - q, q, p), v(1 - q, 1 - q, p), v(q, 1 - q, p), spriteSide));
                quads.add(bakeQuadCenterConnector(v(q, q, 0), v(q, 1 - q, 0), v(1 - q, 1 - q, 0), v(1 - q, q, 0), spriteSide));
            } else {
                QuadSetting pattern = ConnectedPatterns.findPattern(west, up, east, down);
                quads.add(bakeQuadCenter(v(o, 1 - o, o), v(1 - o, 1 - o, o), v(1 - o, o, o), v(o, o, o), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }

            if (south == CABLE) {
                quads.add(bakeQuadNormal(v(o, 1 - o, 1), v(1 - o, 1 - o, 1), v(1 - o, 1 - o, 1 - o), v(o, 1 - o, 1 - o), spriteCable));
                quads.add(bakeQuadNormal(v(o, o, 1 - o), v(1 - o, o, 1 - o), v(1 - o, o, 1), v(o, o, 1), spriteCable));
                quads.add(bakeQuadNormal(v(1 - o, o, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, 1 - o, 1), v(1 - o, o, 1), spriteCable));
                quads.add(bakeQuadNormal(v(o, o, 1), v(o, 1 - o, 1), v(o, 1 - o, 1 - o), v(o, o, 1 - o), spriteCable));
            } else if (south == BLOCK) {
                quads.add(bakeQuadNormal(v(o, 1 - o, 1 - p), v(1 - o, 1 - o, 1 - p), v(1 - o, 1 - o, 1 - o), v(o, 1 - o, 1 - o), spriteCable));
                quads.add(bakeQuadNormal(v(o, o, 1 - o), v(1 - o, o, 1 - o), v(1 - o, o, 1 - p), v(o, o, 1 - p), spriteCable));
                quads.add(bakeQuadNormal(v(1 - o, o, 1 - o), v(1 - o, 1 - o, 1 - o), v(1 - o, 1 - o, 1 - p), v(1 - o, o, 1 - p), spriteCable));
                quads.add(bakeQuadNormal(v(o, o, 1 - p), v(o, 1 - o, 1 - p), v(o, 1 - o, 1 - o), v(o, o, 1 - o), spriteCable));

                quads.add(bakeQuadConnector(v(q, 1 - q, 1), v(1 - q, 1 - q, 1), v(1 - q, 1 - q, 1 - p), v(q, 1 - q, 1 - p), spriteSide, 1));
                quads.add(bakeQuadConnector(v(q, q, 1 - p), v(1 - q, q, 1 - p), v(1 - q, q, 1), v(q, q, 1), spriteSide, 3));
                quads.add(bakeQuadConnector(v(1 - q, q, 1 - p), v(1 - q, 1 - q, 1 - p), v(1 - q, 1 - q, 1), v(1 - q, q, 1), spriteSide, 3));
                quads.add(bakeQuadConnector(v(q, q, 1), v(q, 1 - q, 1), v(q, 1 - q, 1 - p), v(q, q, 1 - p), spriteSide, 1));

                quads.add(bakeQuadCenterConnector(v(q, 1 - q, 1 - p), v(1 - q, 1 - q, 1 - p), v(1 - q, q, 1 - p), v(q, q, 1 - p), spriteSide));
                quads.add(bakeQuadCenterConnector(v(q, 1 - q, 1), v(q, q, 1), v(1 - q, q, 1), v(1 - q, 1 - q, 1), spriteSide));
            } else {
                QuadSetting pattern = ConnectedPatterns.findPattern(west, down, east, up);
                quads.add(bakeQuadCenter(v(o, o, 1 - o), v(1 - o, o, 1 - o), v(1 - o, 1 - o, 1 - o), v(o, 1 - o, 1 - o), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }
        }

        return quads;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    // Because we can potentially mimic other blocks we need to render on all render types
    @Override
    @Nonnull
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        return ChunkRenderTypeSet.all();
    }

    @Nonnull
    @Override
    public TextureAtlasSprite getParticleIcon() {
        return spriteNormalCable == null
                ? Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply((ResourceLocation.fromNamespaceAndPath("minecraft", "missingno")))
                : spriteNormalCable;
    }

    // To let our cable/facade render correctly as an item (both in inventory and on the ground) we
    // get the correct transforms from the context
    @Nonnull
    @Override
    public ItemTransforms getTransforms() {
        return context.getTransforms();
    }

    @Nonnull
    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

}