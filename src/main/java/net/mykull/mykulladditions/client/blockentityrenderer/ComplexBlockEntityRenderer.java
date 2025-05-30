package net.mykull.mykulladditions.client.blockentityrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.mykull.mykulladditions.common.blockentities.ComplexBlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

public class ComplexBlockEntityRenderer implements BlockEntityRenderer<ComplexBlockEntity> {
    public ComplexBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ComplexBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
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

/*
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
    }*/
}
