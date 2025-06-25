package net.mykull.mykulladditions.client.blockentityrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.mykull.mykulladditions.MykullsAdditions;
import net.mykull.mykulladditions.common.blockentities.CableBlockEntity;
import net.mykull.mykulladditions.common.cables.ConnectorType;
import net.mykull.mykulladditions.common.cables.blocks.CableBlock;

public class CableBlockEntityRenderer implements BlockEntityRenderer<CableBlockEntity> {
    public CableBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(CableBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        HitResult result = Minecraft.getInstance().hitResult;

        if (result instanceof BlockHitResult blockHit) {
            BlockPos targetPos = blockHit.getBlockPos();

            // Only proceed if the player is looking at THIS block
            if (targetPos.equals(blockEntity.getBlockPos())) {

                //BlockState state = blockEntity.getBlockState();
                //ConnectorType type = state.getValue(CableBlock.NORTH);

                Vec3 hitLocation = blockHit.getLocation(); // Global hit position

                // Convert to block-local coordinates (0–1 range inside the block)
                double localX = hitLocation.x - targetPos.getX();
                double localY = hitLocation.y - targetPos.getY();
                double localZ = hitLocation.z - targetPos.getZ();
                Vec3 localHit = new Vec3(localX, localY, localZ);

                // Your VoxelShape (e.g. a wire segment or pipe part)
                VoxelShape partShape = CableBlock.SHAPE_BLOCK_UP;// example part

                // Check if the hit point is inside this shape
                if (voxelContains(partShape, localHit)) {
                    // Player is pointing at this voxel part
                    MykullsAdditions.LOGGER.debug("Hit Top");
                }
            }
        }
    }

    public static boolean voxelContains(VoxelShape shape, Vec3 localHit) {
        // Shapes are internally a set of AABBs — check if the point is in any of them
        for (AABB box : shape.toAabbs()) {
            if (box.contains(localHit)) {
                return true;
            }
        }
        return false;
    }
}
