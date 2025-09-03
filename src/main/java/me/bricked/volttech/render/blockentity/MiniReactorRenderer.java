package me.bricked.volttech.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.bricked.volttech.blockentity.MiniReactorBlockEntity;
import me.bricked.volttech.register.ItemRegistry;
import me.bricked.volttech.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.Optional;

public class MiniReactorRenderer implements BlockEntityRenderer<MiniReactorBlockEntity> {
    public MiniReactorRenderer(BlockEntityRendererProvider.Context context) {}
    @Override
    public void render(MiniReactorBlockEntity miniReactorBlockEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay, Vec3 vec3) {
        boolean isRunning = miniReactorBlockEntity.getConsumeTicksLeft() > 0;
        FluidTank waterTank = miniReactorBlockEntity.getWaterTank();
        float waterPercent = (float) waterTank.getFluidAmount() / waterTank.getCapacity();
        if (waterTank.getFluidAmount() > 0)
            RenderUtil.renderFluid(poseStack, multiBufferSource, waterTank.getFluid().getFluid(), new AABB(1.01f / 16.f, 1.01f / 16.f, 1.01f / 16.f, 14.99f / 16.f, (14.99f * waterPercent) / 16.f, 14.99f / 16.f), light, overlay);

        light = LightTexture.FULL_BRIGHT;
        ItemStack stack = ItemRegistry.URANIUM_INGOT.toStack();
        ResourceLocation consumedItem = miniReactorBlockEntity.getConsumedItem();
        if (!consumedItem.getPath().equalsIgnoreCase("air")) {
            Optional<Holder.Reference<Item>> ref = BuiltInRegistries.ITEM.get(consumedItem);
            if (ref.isPresent()) {
                stack = ref.get().value().getDefaultInstance();
            }
        }
        if (isRunning) {
            Level level = miniReactorBlockEntity.getLevel();
            poseStack.pushPose();
            float time = (level.getGameTime() + partialTick) % 360;
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.mulPose(Axis.YP.rotationDegrees(time * 50));
            poseStack.mulPose(Axis.XN.rotationDegrees(time * 2));
            poseStack.mulPose(Axis.ZN.rotationDegrees(time * 2));
            poseStack.scale(0.5f, 0.5f, 0.5f);
            Minecraft.getInstance().getItemRenderer().renderStatic(
                    stack,
                    ItemDisplayContext.FIXED,
                    light,
                    overlay,
                    poseStack,
                    multiBufferSource,
                    level,
                    0
            );
            poseStack.popPose();
        }
    }
}
