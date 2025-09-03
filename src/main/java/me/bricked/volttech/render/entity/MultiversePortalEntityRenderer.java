package me.bricked.volttech.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.bricked.volttech.entity.MultiversePortalEntity;
import me.bricked.volttech.register.CustomModelRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;

public class MultiversePortalEntityRenderer extends EntityRenderer<MultiversePortalEntity, PortalRenderState> {
    public MultiversePortalEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(PortalRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(renderState, poseStack, bufferSource, packedLight);
        // Cheat a little bit and just render a custom model applied to an ItemStack
        poseStack.pushPose();
        poseStack.translate(0, 0.5f, 0);
        poseStack.mulPose(Axis.YN.rotationDegrees(renderState.yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(renderState.pitch));
        if (renderState.pitch == 0)
            poseStack.translate(0, 0, 0.5);
        Minecraft.getInstance().getItemRenderer().renderStatic(
                CustomModelRegistry.MULTIVERSE_PORTAL,
                ItemDisplayContext.FIXED,
                LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                null,
                0);
        poseStack.popPose();
    }

    @Override
    protected boolean shouldShowName(MultiversePortalEntity entity, double distanceToCameraSq) {
        return distanceToCameraSq < 50;
    }

    @Override
    public PortalRenderState createRenderState() {
        return new PortalRenderState();
    }

    @Override
    public void extractRenderState(MultiversePortalEntity entity, PortalRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.yaw = entity.getYRot(partialTick);
        reusedState.pitch = entity.getXRot(partialTick);
    }
}
