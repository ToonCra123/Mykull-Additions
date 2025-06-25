package net.mykull.mykulladditions.tools;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.pipeline.QuadBakingVertexConsumer;

import static net.mykull.mykulladditions.tools.BakedModelHelper.putVertex;

public class CableBakedModelHelper {


    public static BakedQuad bakeQuadConnector(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, int rotation) {
        return switch (rotation) {
            case 0 -> bakeQuadConnector(v1, v2, v3, v4, sprite);
            case 1 -> bakeQuadConnector(v2, v3, v4, v1, sprite);
            case 2 -> bakeQuadConnector(v3, v4, v1, v2, sprite);
            case 3 -> bakeQuadConnector(v4, v1, v2, v3, sprite);
            default -> bakeQuadConnector(v1, v2, v3, v4, sprite);
        };
    }

    // works only for 16x16 textures gunna kms sprites dont say their size
    public static BakedQuad bakeQuadConnector(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite) {
        Vec3 normal = v3.subtract(v2).cross(v1.subtract(v2)).normalize();

        QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer();
        builder.setSprite(sprite);
        builder.setDirection(Direction.getNearest(normal.x, normal.y, normal.z));
        putVertex(builder, normal, v1.x, v1.y, v1.z, 0.25f, 0, sprite);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 0.25f, 0.25f, sprite);
        putVertex(builder, normal, v3.x, v3.y, v3.z, 0.75f, 0.25f, sprite);
        putVertex(builder, normal, v4.x, v4.y, v4.z, 0.75f, 0, sprite);
        return builder.bakeQuad();
    }

    public static BakedQuad bakeQuadCenterConnnector(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, int rotation) {
        return switch (rotation) {
            case 0 -> bakeQuadCenterConnector(v1, v2, v3, v4, sprite);
            case 1 -> bakeQuadCenterConnector(v2, v3, v4, v1, sprite);
            case 2 -> bakeQuadCenterConnector(v3, v4, v1, v2, sprite);
            case 3 -> bakeQuadCenterConnector(v4, v1, v2, v3, sprite);
            default -> bakeQuadCenterConnector(v1, v2, v3, v4, sprite);
        };
    }

    // works only for 16x16 textures gunna kms sprites dont say their size
    public static BakedQuad bakeQuadCenterConnector(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite) {
        Vec3 normal = v3.subtract(v2).cross(v1.subtract(v2)).normalize();

        QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer();
        builder.setSprite(sprite);
        builder.setDirection(Direction.getNearest(normal.x, normal.y, normal.z));
        putVertex(builder, normal, v1.x, v1.y, v1.z, 0.25f, 0.25f, sprite);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 0.25f, 0.75f, sprite);
        putVertex(builder, normal, v3.x, v3.y, v3.z, 0.75f, 0.75f, sprite);
        putVertex(builder, normal, v4.x, v4.y, v4.z, 0.75f, 0.25f, sprite);
        return builder.bakeQuad();
    }

    public static BakedQuad bakeQuadNormal(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, int rotation) {
        return switch (rotation) {
            case 0 -> bakeQuadNormal(v1, v2, v3, v4, sprite);
            case 1 -> bakeQuadNormal(v2, v3, v4, v1, sprite);
            case 2 -> bakeQuadNormal(v3, v4, v1, v2, sprite);
            case 3 -> bakeQuadNormal(v4, v1, v2, v3, sprite);
            default -> bakeQuadNormal(v1, v2, v3, v4, sprite);
        };
    }

    // works only for 16x16 textures gunna kms sprites dont say their size
    public static BakedQuad bakeQuadNormal(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite) {
        Vec3 normal = v3.subtract(v2).cross(v1.subtract(v2)).normalize();

        QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer();
        builder.setSprite(sprite);
        builder.setDirection(Direction.getNearest(normal.x, normal.y, normal.z));
        putVertex(builder, normal, v1.x, v1.y, v1.z, 0, 0, sprite);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 0, 0.75f, sprite);
        putVertex(builder, normal, v3.x, v3.y, v3.z, 0.625f, 0.75f, sprite);
        putVertex(builder, normal, v4.x, v4.y, v4.z, 0.625f, 0, sprite);
        return builder.bakeQuad();
    }


    public static BakedQuad bakeQuadCenter(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, int rotation) {
        return switch (rotation) {
            case 0 -> bakeQuadCenter(v1, v2, v3, v4, sprite);
            case 1 -> bakeQuadCenter(v2, v3, v4, v1, sprite);
            case 2 -> bakeQuadCenter(v3, v4, v1, v2, sprite);
            case 3 -> bakeQuadCenter(v4, v1, v2, v3, sprite);
            default -> bakeQuadCenter(v1, v2, v3, v4, sprite);
        };
    }

    // works only for 16x16 textures gunna kms sprites dont say their size
    public static BakedQuad bakeQuadCenter(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite) {
        Vec3 normal = v3.subtract(v2).cross(v1.subtract(v2)).normalize();

        QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer();
        builder.setSprite(sprite);
        builder.setDirection(Direction.getNearest(normal.x, normal.y, normal.z));
        putVertex(builder, normal, v1.x, v1.y, v1.z, 0, 0, sprite);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 0, 0.75f, sprite);
        putVertex(builder, normal, v3.x, v3.y, v3.z, 0.75f, 0.75f, sprite);
        putVertex(builder, normal, v4.x, v4.y, v4.z, 0.75f, 0, sprite);
        return builder.bakeQuad();
    }
}
