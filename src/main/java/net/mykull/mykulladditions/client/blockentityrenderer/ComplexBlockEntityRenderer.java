package net.mykull.mykulladditions.client.blockentityrenderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.mykull.mykulladditions.MykullsAdditions;
import net.mykull.mykulladditions.common.blockentities.ComplexBlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ComplexBlockEntityRenderer implements BlockEntityRenderer<ComplexBlockEntity> {

    private static final RenderType TRIANGLE_TYPE = RenderType.create(
            "custom_triangle",
            DefaultVertexFormat.NEW_ENTITY,
            VertexFormat.Mode.TRIANGLES, 256, false, true,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.RENDERTYPE_ENTITY_CUTOUT_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(
                            InventoryMenu.BLOCK_ATLAS, false, false))
                    .setLightmapState(RenderType.LIGHTMAP)
                    .setOverlayState(RenderType.OVERLAY)
                    .createCompositeState(false)
    );


    public ComplexBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ComplexBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        /*
        poseStack.pushPose();

        poseStack.translate(1.0f, 1.5f, 1.0f);


        renderTriangle(poseStack, bufferSource.getBuffer(TRIANGLE_TYPE), new Vector3f(), new Vector3f(), new Vector3f());

        poseStack.popPose();
        */

        // Item Rendering
        /*
        IItemHandler h = be.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, be.getBlockPos(), null);
        if (h != null) {
            ItemStack stack = h.getStackInSlot(ComplexBlockEntity.SLOT);

            if (!stack.isEmpty()) {
                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                long millis = System.currentTimeMillis();

                poseStack.pushPose();
                poseStack.pushPose();
                poseStack.scale(.5f, .5f, .5f);
                poseStack.translate(1f, 2.8f, 1f);
                float angle = ((millis / 45) % 360);
                poseStack.mulPose(Axis.YP.rotationDegrees(angle));
                itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, LightTexture.FULL_BRIGHT, packedOverlay, poseStack, bufferSource, Minecraft.getInstance().level, 0);
                poseStack.popPose();

                poseStack.translate(0, 0.5f, 0);
                poseStack.popPose();
            }
        }*/

    }

    ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(MykullsAdditions.MODID, "block/simple_block");

    private void renderTriangle(PoseStack poseStack, VertexConsumer builder, Vector3f point1, Vector3f point2, Vector3f point3) {

        int b1 = LightTexture.FULL_BRIGHT >> 16 & 65535;
        int b2 = LightTexture.FULL_BRIGHT & 65535;

        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();

        // Fetch actual sprite UVs from the atlas
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);
        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();
        float vm = (v0 + v1) / 2f; // mid V for top vertex

        float z = 0.0f;
        float size = 0.5f;

        Vector3f p1 = new Vector3f(-size, -size, z); // bottom left
        Vector3f p2 = new Vector3f( size, -size, z); // bottom right
        Vector3f p3 = new Vector3f( 0.0f,  size, z); // top middle

        builder.addVertex(matrix, p1.x(), p1.y(), p1.z())
                .setColor(255, 255, 255, 255)
                .setUv(u0, v1)
                .setUv1(OverlayTexture.NO_OVERLAY & 0xFFFF, (OverlayTexture.NO_OVERLAY >> 16) & 0xFFFF)
                .setUv2(b1, b2)
                //.setUv1(0, 0)
                //.setUv2(0, 0)
                .setNormal(0, 0, 1);

        builder.addVertex(matrix, p2.x(), p2.y(), p2.z())
                .setColor(255, 255, 255, 255)
                .setUv(u1, v1)
                .setUv1(OverlayTexture.NO_OVERLAY & 0xFFFF, (OverlayTexture.NO_OVERLAY >> 16) & 0xFFFF)
                .setUv2(b1, b2)
                //.setUv1(0, 0)
                //.setUv2(0, 0)
                .setNormal(0, 0, 1);

        builder.addVertex(matrix, p3.x(), p3.y(), p3.z())
                .setColor(255, 255, 255, 255)
                .setUv(u0, v0)
                .setUv1(OverlayTexture.NO_OVERLAY & 0xFFFF, (OverlayTexture.NO_OVERLAY >> 16) & 0xFFFF)
                .setUv2(b1, b2)
                //.setUv1(0, 0)
                //.setUv2(0, 0)
                .setNormal(0, 0, 1);


    }

    private void renderTriangleOutline(PoseStack poseStack, MultiBufferSource bufferSource) {
        VertexConsumer builder = bufferSource.getBuffer(RenderType.debugLineStrip(1.0f));
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();

        builder.addVertex(matrix, -0.5f, -0.5f, 0f).setColor(255, 0, 0, 255).setUv1(0, 0).setUv2(0, 0).setNormal(0, 0, 1);
        builder.addVertex(matrix, 0.5f, -0.5f, 0f).setColor(0, 255, 0, 255).setUv1(0, 0).setUv2(0, 0).setNormal(0, 0, 1);
        builder.addVertex(matrix, 0f, 0.5f, 0f).setColor(0, 0, 255, 255).setUv1(0, 0).setUv2(0, 0).setNormal(0, 0, 1);
        builder.addVertex(matrix, -0.5f, -0.5f, 0f).setColor(255, 0, 0, 255).setUv1(0, 0).setUv2(0, 0).setNormal(0, 0, 1);
    }


    private static void renderBillboardQuadBright(PoseStack matrixStack, VertexConsumer builder, float scale, ResourceLocation texture) {
        int b1 = LightTexture.FULL_BRIGHT >> 16 & 65535;
        int b2 = LightTexture.FULL_BRIGHT & 65535;
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.95, 0.5);
        // Quaternionf rotation = Minecraft.getInstance().gameRenderer.getMainCamera().rotation();
        // matrixStack.mulPose(rotation);
        Matrix4f matrix = matrixStack.last().pose();
        builder.addVertex(matrix, -scale, -scale, 0.0f).setColor(255, 255, 255, 255).setUv(sprite.getU0(), sprite.getV0()).setUv1(OverlayTexture.NO_OVERLAY & 0xFFFF, (OverlayTexture.NO_OVERLAY >> 16) & 0xFFFF).setUv2(b1, b2).setNormal(1, 0, 0);
        builder.addVertex(matrix, -scale, scale, 0.0f).setColor(255, 255, 255, 255).setUv(sprite.getU0(), sprite.getV1()).setUv1(OverlayTexture.NO_OVERLAY & 0xFFFF, (OverlayTexture.NO_OVERLAY >> 16) & 0xFFFF).setUv2(b1, b2).setNormal(1, 0, 0);
        builder.addVertex(matrix, scale, scale, 0.0f).setColor(255, 255, 255, 255).setUv(sprite.getU1(), sprite.getV1()).setUv1(OverlayTexture.NO_OVERLAY & 0xFFFF, (OverlayTexture.NO_OVERLAY >> 16) & 0xFFFF).setUv2(b1, b2).setNormal(1, 0, 0);
        builder.addVertex(matrix, scale, -scale, 0.0f).setColor(255, 255, 255, 255).setUv(sprite.getU1(), sprite.getV0()).setUv1(OverlayTexture.NO_OVERLAY & 0xFFFF, (OverlayTexture.NO_OVERLAY >> 16) & 0xFFFF).setUv2(b1, b2).setNormal(1, 0, 0);
        matrixStack.popPose();
    }
}
