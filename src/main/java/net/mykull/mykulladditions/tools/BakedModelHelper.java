package net.mykull.mykulladditions.tools;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.pipeline.QuadBakingVertexConsumer;

public class BakedModelHelper {


    // works only for 16x16 textures gunna kms sprites dont say their size
    public static BakedQuad bakeQuadUV(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, int rotation) {
        return switch (rotation) {
            case 0 -> bakeQuadUV(v1, v2, v3, v4, sprite);
            case 1 -> bakeQuadUV(v2, v3, v4, v1, sprite);
            case 2 -> bakeQuadUV(v3, v4, v1, v2, sprite);
            case 3 -> bakeQuadUV(v4, v1, v2, v3, sprite);
            default -> bakeQuadUV(v1, v2, v3, v4, sprite);
        };
    }

    // works only for 16x16 textures gunna kms sprites dont say their size
    public static BakedQuad bakeQuadUV(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite) {
        Vec3 normal = v3.subtract(v2).cross(v1.subtract(v2)).normalize();

        QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer();
        builder.setSprite(sprite);
        builder.setDirection(Direction.getNearest(normal.x, normal.y, normal.z));
        putVertex(builder, normal, v1.x, v1.y, v1.z, 0, 0, sprite);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 0, 0.125f, sprite);
        putVertex(builder, normal, v3.x, v3.y, v3.z, 1, 0.125f, sprite);
        putVertex(builder, normal, v4.x, v4.y, v4.z, 1, 0, sprite);
        return builder.bakeQuad();
    }

    public static BakedQuad bakeQuad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, int rotation) {
        return switch (rotation) {
            case 0 -> bakeQuad(v1, v2, v3, v4, sprite);
            case 1 -> bakeQuad(v2, v3, v4, v1, sprite);
            case 2 -> bakeQuad(v3, v4, v1, v2, sprite);
            case 3 -> bakeQuad(v4, v1, v2, v3, sprite);
            default -> bakeQuad(v1, v2, v3, v4, sprite);
        };
    }

    public static BakedQuad bakeQuad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite) {
        Vec3 normal = v3.subtract(v2).cross(v1.subtract(v2)).normalize();

        QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer();
        builder.setSprite(sprite);
        builder.setDirection(Direction.getNearest(normal.x, normal.y, normal.z));
        putVertex(builder, normal, v1.x, v1.y, v1.z, 0, 0, sprite);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 0, 1, sprite);
        putVertex(builder, normal, v3.x, v3.y, v3.z, 1, 1, sprite);
        putVertex(builder, normal, v4.x, v4.y, v4.z, 1, 0, sprite);
        return builder.bakeQuad();
    }

    private static void putVertex(VertexConsumer builder, Position normal,
                                  double x, double y, double z, float u, float v,
                                  TextureAtlasSprite sprite) {
        float iu = sprite.getU(u);
        float iv = sprite.getV(v);
        builder.addVertex((float) x, (float)y, (float)z)
                .setUv(iu, iv)
                .setUv2(0, 0)
                .setColor(1.0f, 1.0f, 1.0f, 1.0f)
                .setNormal((float) normal.x(), (float) normal.y(), (float) normal.z());
    }

    public static Vec3 v(double x, double y, double z) {
        return new Vec3(x, y, z);
    }
}