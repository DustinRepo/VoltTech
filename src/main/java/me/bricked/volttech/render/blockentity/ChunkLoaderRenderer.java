package me.bricked.volttech.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.bricked.volttech.blockentity.ChunkLoaderBlockEntity;
import me.bricked.volttech.register.CustomModelRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;

public class ChunkLoaderRenderer implements BlockEntityRenderer<ChunkLoaderBlockEntity> {
    public ChunkLoaderRenderer(BlockEntityRendererProvider.Context context) {}
    @Override
    public void render(ChunkLoaderBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, Vec3 vec3) {
        poseStack.pushPose();
        float time = (blockEntity.getLevel().getGameTime() + partialTick) % 360;
        poseStack.translate(0.5, 0.7, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(time * 10));
        poseStack.mulPose(Axis.XN.rotationDegrees(time * 10));
        poseStack.mulPose(Axis.ZN.rotationDegrees(time * 10));
        Minecraft.getInstance().getItemRenderer().renderStatic(
                CustomModelRegistry.CHUNK_LOADER_CORE,
                ItemDisplayContext.FIXED,
                LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                blockEntity.getLevel(),
                0);
        poseStack.translate(-0.5, -0.7, -0.5);
        poseStack.popPose();
    }
}
