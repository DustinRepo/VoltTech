package me.bricked.volttech.render.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import me.bricked.volttech.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.EmptyBlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Set;

public record FluidTankSpecialRenderer(ModelPart modelPart) implements SpecialModelRenderer<ItemStack> {
    @Override
    public void render(ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int overlay, boolean foil) {
        poseStack.pushPose();
        applyTransforms(itemDisplayContext, poseStack);
        FluidUtil.getFluidHandler(itemStack).ifPresent(iFluidHandlerItem -> {
            FluidStack fluidStack = iFluidHandlerItem.getFluidInTank(0);
            Fluid fluid = fluidStack.getFluid();
            float fillPercent = (float) fluidStack.getAmount() / iFluidHandlerItem.getTankCapacity(0);
            int fluidLight = fluid.getFluidType().getLightLevel();
            int light = packedLight;
            if (fluidLight > 0)
                light = Math.max(LightTexture.pack(fluidLight, LightTexture.FULL_SKY), packedLight);
            if (fluidStack.getAmount() > 0)
                RenderUtil.renderFluid(poseStack, multiBufferSource, fluid, new AABB(3.01f / 16.f, 1.01f / 16.f, 3.01f / 16.f, 12.99f / 16.f, (14.99f * fillPercent) / 16.f, 12.99f / 16.f), light, OverlayTexture.NO_OVERLAY);
        });
        Block block = ((BlockItem) itemStack.getItem()).getBlock();
        BlockStateModel model = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(block.defaultBlockState());
        ModelBlockRenderer.renderModel(poseStack.last(), multiBufferSource, model, 1f, 1f, 1f, packedLight, overlay, EmptyBlockAndTintGetter.INSTANCE, BlockPos.ZERO, block.defaultBlockState());
        poseStack.popPose();
    }

    private void applyTransforms(ItemDisplayContext itemDisplayContext, PoseStack poseStack) {
        switch (itemDisplayContext) {
            case FIRST_PERSON_RIGHT_HAND, FIRST_PERSON_LEFT_HAND -> {
                poseStack.translate(0.25f, 0.35f, 0.0f);
                poseStack.scale(0.5f, 0.5f, 0.5f);
            }
            case THIRD_PERSON_RIGHT_HAND, THIRD_PERSON_LEFT_HAND -> {
                poseStack.translate(0.25f, 0.3f, 0.1f);
                poseStack.scale(0.5f, 0.5f, 0.5f);
            }
            case GUI -> {
                poseStack.translate(-0.025f, 0.15f, 0);
                poseStack.mulPose(Axis.XP.rotationDegrees(25.f));
                poseStack.mulPose(Axis.YP.rotationDegrees(45.f));
                poseStack.scale(0.75f, 0.75f, 0.75f);
            }
            case GROUND -> {
                poseStack.scale(0.25f, 0.25f, 0.25f);
                poseStack.translate(1.5f, -0.75f, 1.5f);
            }
        }
    }

    @Override
    public ItemStack extractArgument(ItemStack stack) {
        return stack;
    }

    @Override
    public void getExtents(Set<Vector3f> points) {
        modelPart.getExtentsForGui(new PoseStack(), points);
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> CODEC = MapCodec.unit(new Unbaked());

        @Override
        public @Nullable SpecialModelRenderer<?> bake(EntityModelSet entityModelSet) {
            // Use ModelLayers.CONDUIT_SHELL just to get a ModelPart for getExtentsForGui
            // Without populating extents from a model the ItemEntity doesn't display
            return new FluidTankSpecialRenderer(entityModelSet.bakeLayer(ModelLayers.CONDUIT_SHELL));
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked> type() {
            return CODEC;
        }
    }
}
