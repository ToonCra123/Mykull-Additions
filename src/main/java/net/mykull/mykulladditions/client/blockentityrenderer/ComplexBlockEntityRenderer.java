package net.mykull.mykulladditions.client.blockentityrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.mykull.mykulladditions.MykullsAdditions;
import net.mykull.mykulladditions.common.blockentities.ComplexBlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ComplexBlockEntityRenderer implements BlockEntityRenderer<ComplexBlockEntity> {
    public ComplexBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ComplexBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        /*
        poseStack.pushPose();

        poseStack.translate(0.0f, 0.5f, 0.0f);

        renderBillboardQuadBright(poseStack, bufferSource.getBuffer(RenderType.translucent()), 0.5f, texture);

        poseStack.popPose();
        */

        // Item Rendering

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
        }

    }

    ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(MykullsAdditions.MODID, "textures/block/simple_block.png");

    private void renderTriangle(PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        VertexConsumer builder = bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture));
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();

        // Coordinates of triangle in local space
        float z = 0.0f;
        float size = 0.5f;

        Vector3f p1 = new Vector3f(-size, -size, z);
        Vector3f p2 = new Vector3f( size, -size, z);
        Vector3f p3 = new Vector3f( 0.0f,  size, z);

        builder.addVertex(matrix, p1.x(), p1.y(), p1.z())
                .setColor(255, 255, 255, 255)
                .setUv(0.0f, 1.0f)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv2(240, 240)
                .setNormal(0, 0, 1);

        builder.addVertex(matrix, p2.x(), p2.y(), p2.z())
                .setColor(255, 255, 255, 255)
                .setUv(1.0f, 1.0f)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv2(240, 240)
                .setNormal(0, 0, 1);

        builder.addVertex(matrix, p3.x(), p3.y(), p3.z())
                .setColor(255, 255, 255, 255)
                .setUv(0.0f, 0.5f)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv2(240, 240)
                .setNormal(0, 0, 1);
    }


    private static void renderBillboardQuadBright(PoseStack matrixStack, VertexConsumer builder, float scale, ResourceLocation texture) {
        int b1 = LightTexture.FULL_BRIGHT >> 16 & 65535;
        int b2 = LightTexture.FULL_BRIGHT & 65535;
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.95, 0.5);
        Quaternionf rotation = Minecraft.getInstance().gameRenderer.getMainCamera().rotation();
        matrixStack.mulPose(rotation);
        Matrix4f matrix = matrixStack.last().pose();
        builder.addVertex(matrix, -scale, -scale, 0.0f).setColor(255, 255, 255, 255).setUv(sprite.getU0(), sprite.getV0()).setUv2(b1, b2).setNormal(1, 0, 0);
        builder.addVertex(matrix, -scale, scale, 0.0f).setColor(255, 255, 255, 255).setUv(sprite.getU0(), sprite.getV1()).setUv2(b1, b2).setNormal(1, 0, 0);
        builder.addVertex(matrix, scale, scale, 0.0f).setColor(255, 255, 255, 255).setUv(sprite.getU1(), sprite.getV1()).setUv2(b1, b2).setNormal(1, 0, 0);
        builder.addVertex(matrix, scale, -scale, 0.0f).setColor(255, 255, 255, 255).setUv(sprite.getU1(), sprite.getV0()).setUv2(b1, b2).setNormal(1, 0, 0);
        matrixStack.popPose();
    }
}
