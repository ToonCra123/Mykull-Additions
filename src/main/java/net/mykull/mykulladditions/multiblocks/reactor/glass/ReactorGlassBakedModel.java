package net.mykull.mykulladditions.multiblocks.reactor.glass;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.mykull.mykulladditions.MykullsAdditions;
import net.mykull.mykulladditions.multiblocks.reactor.ReactorGlassBlock;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static net.mykull.mykulladditions.multiblocks.reactor.glass.ConnectedPatterns.SpriteIdx.*;
import static net.mykull.mykulladditions.tools.BakedModelHelper.bakeQuad;
import static net.mykull.mykulladditions.tools.BakedModelHelper.v;

// TODO: MAKE MODULAR
public class ReactorGlassBakedModel implements IDynamicBakedModel {

    private final IGeometryBakingContext context;

    private TextureAtlasSprite spriteNone;
    private TextureAtlasSprite spriteEnd;
    private TextureAtlasSprite spriteCorner;
    private TextureAtlasSprite spriteStraight;
    private TextureAtlasSprite spriteThree;
    private TextureAtlasSprite spriteCross;

    static {
        // For all possible patterns we define the sprite to use and the rotation. Note that each
        // pattern looks at the existance of a cable section for each of the four directions
        // excluding the one we are looking at.
        ConnectedPatterns.PATTERNS.put(ConnectedPatterns.Pattern.of(false, false, false, false), ConnectedPatterns.QuadSetting.of(SPRITE_NONE, 0));
        ConnectedPatterns.PATTERNS.put(ConnectedPatterns.Pattern.of(true, false, false, false), ConnectedPatterns.QuadSetting.of(SPRITE_END, 3));
        ConnectedPatterns.PATTERNS.put(ConnectedPatterns.Pattern.of(false, true, false, false), ConnectedPatterns.QuadSetting.of(SPRITE_END, 0));
        ConnectedPatterns.PATTERNS.put(ConnectedPatterns.Pattern.of(false, false, true, false), ConnectedPatterns.QuadSetting.of(SPRITE_END, 1));
        ConnectedPatterns.PATTERNS.put(ConnectedPatterns.Pattern.of(false, false, false, true), ConnectedPatterns.QuadSetting.of(SPRITE_END, 2));
        ConnectedPatterns.PATTERNS.put(ConnectedPatterns.Pattern.of(true, true, false, false), ConnectedPatterns.QuadSetting.of(SPRITE_CORNER, 0));
        ConnectedPatterns.PATTERNS.put(ConnectedPatterns.Pattern.of(false, true, true, false), ConnectedPatterns.QuadSetting.of(SPRITE_CORNER, 1));
        ConnectedPatterns.PATTERNS.put(ConnectedPatterns.Pattern.of(false, false, true, true), ConnectedPatterns.QuadSetting.of(SPRITE_CORNER, 2));
        ConnectedPatterns.PATTERNS.put(ConnectedPatterns.Pattern.of(true, false, false, true), ConnectedPatterns.QuadSetting.of(SPRITE_CORNER, 3));
        ConnectedPatterns.PATTERNS.put(ConnectedPatterns.Pattern.of(false, true, false, true), ConnectedPatterns.QuadSetting.of(SPRITE_STRAIGHT, 0));
        ConnectedPatterns.PATTERNS.put(ConnectedPatterns.Pattern.of(true, false, true, false), ConnectedPatterns.QuadSetting.of(SPRITE_STRAIGHT, 1));
        ConnectedPatterns.PATTERNS.put(ConnectedPatterns.Pattern.of(true, true, true, false), ConnectedPatterns.QuadSetting.of(SPRITE_THREE, 0));
        ConnectedPatterns.PATTERNS.put(ConnectedPatterns.Pattern.of(false, true, true, true), ConnectedPatterns.QuadSetting.of(SPRITE_THREE, 1));
        ConnectedPatterns.PATTERNS.put(ConnectedPatterns.Pattern.of(true, false, true, true), ConnectedPatterns.QuadSetting.of(SPRITE_THREE, 2));
        ConnectedPatterns.PATTERNS.put(ConnectedPatterns.Pattern.of(true, true, false, true), ConnectedPatterns.QuadSetting.of(SPRITE_THREE, 3));
        ConnectedPatterns.PATTERNS.put(ConnectedPatterns.Pattern.of(true, true, true, true), ConnectedPatterns.QuadSetting.of(SPRITE_CROSS, 0));
    }

    public ReactorGlassBakedModel(IGeometryBakingContext context) {
        this.context = context;
    }

    private void initTextures() {
        if (spriteNone == null) {
            spriteStraight = getTexture("block/reactor/glass/straight");
            spriteNone = getTexture("block/reactor/glass/none");
            spriteEnd = getTexture("block/reactor/glass/end");
            spriteCorner = getTexture("block/reactor/glass/corner");
            spriteThree = getTexture("block/reactor/glass/three");
            spriteCross = getTexture("block/reactor/glass/cross");
        }
    }

    private TextureAtlasSprite getSpriteNormal(ConnectedPatterns.SpriteIdx idx) {
        initTextures();
        return switch (idx) {
            case SPRITE_NONE -> spriteNone;
            case SPRITE_END -> spriteEnd;
            case SPRITE_STRAIGHT -> spriteStraight;
            case SPRITE_CORNER -> spriteCorner;
            case SPRITE_THREE -> spriteThree;
            case SPRITE_CROSS -> spriteCross;
        };
    }

    private TextureAtlasSprite getTexture(String path) {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(ResourceLocation.fromNamespaceAndPath(MykullsAdditions.MODID, path));
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData extraData, @Nullable RenderType renderType) {
        initTextures();
        List<BakedQuad> quads = new ArrayList<>();
        if (side == null && (renderType == null || renderType.equals(RenderType.translucent()))) {

            boolean north, south, west, east, up, down;
            boolean r_north, r_south, r_west, r_east, r_up, r_down;
            if (state != null) {
                north = state.getValue(ReactorGlassBlock.NORTH);
                south = state.getValue(ReactorGlassBlock.SOUTH);
                west = state.getValue(ReactorGlassBlock.WEST);
                east = state.getValue(ReactorGlassBlock.EAST);
                up = state.getValue(ReactorGlassBlock.UP);
                down = state.getValue(ReactorGlassBlock.DOWN);

                r_north = state.getValue(ReactorGlassBlock.R_NORTH);
                r_south = state.getValue(ReactorGlassBlock.R_SOUTH);
                r_west = state.getValue(ReactorGlassBlock.R_WEST);
                r_east = state.getValue(ReactorGlassBlock.R_EAST);
                r_up = state.getValue(ReactorGlassBlock.R_UP);
                r_down = state.getValue(ReactorGlassBlock.R_DOWN);
            } else {
                // in inventory
                north = south = west = east = up = down = false;
                r_north = r_south = r_west = r_east = r_up = r_down = true;
            }

            Function<ConnectedPatterns.SpriteIdx, TextureAtlasSprite> spriteGetter = this::getSpriteNormal;

            // fucked up rn
            // Up Hopefully
            if (r_up) {
                ConnectedPatterns.QuadSetting pattern = ConnectedPatterns.findPattern(west, south, east, north);
                quads.add(bakeQuad(v(0, 1, 1), v(1, 1, 1), v(1, 1, 0), v(0, 1, 0), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }
            // fucked up rn
            //down
            if (r_down) {
                ConnectedPatterns.QuadSetting pattern = ConnectedPatterns.findPattern(west, north, east, south);
                quads.add(bakeQuad(v(0, 0, 0), v(1, 0, 0), v(1, 0, 1), v(0, 0, 1), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }

            // good
            //north
            if(r_north) {
                ConnectedPatterns.QuadSetting pattern = ConnectedPatterns.findPattern(west, up, east, down);
                quads.add(bakeQuad(v(0, 1, 0), v(1, 1, 0), v(1, 0, 0), v(0, 0, 0), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }
            // good
            // south
            if (r_south) {
                ConnectedPatterns.QuadSetting pattern = ConnectedPatterns.findPattern(west, down, east, up);
                quads.add(bakeQuad(v(0, 0, 1), v(1, 0, 1), v(1, 1, 1), v(0, 1, 1), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }

            // fucked up rn
            // east
            if (r_east) {
                ConnectedPatterns.QuadSetting pattern = ConnectedPatterns.findPattern(down, north, up, south);
                quads.add(bakeQuad(v(1, 0, 0), v(1, 1, 0), v(1, 1, 1), v(1, 0, 1), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }
            // fucked up rn
            // west
            if(r_west) {
                ConnectedPatterns.QuadSetting pattern = ConnectedPatterns.findPattern(down, south, up, north);
                quads.add(bakeQuad(v(0, 0, 0), v(0, 0, 1), v(0, 1, 1), v(0, 1, 0), spriteGetter.apply(pattern.sprite()), pattern.rotation()));
            }

        }
        return quads;
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData data) {

        // IDK what this does
        return ChunkRenderTypeSet.of(RenderType.translucent());
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
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply((ResourceLocation.fromNamespaceAndPath("minecraft", "missingno")));
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }
}
