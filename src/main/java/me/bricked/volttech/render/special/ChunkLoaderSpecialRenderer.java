package me.bricked.volttech.render.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import me.bricked.volttech.register.BlockRegistry;
import me.bricked.volttech.register.CustomModelRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.EmptyBlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Set;

public record ChunkLoaderSpecialRenderer(ModelPart modelPart) implements SpecialModelRenderer<Void> {
    @Override
    public void render(Void v, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int overlay, boolean foil) {
        if (Minecraft.getInstance().level == null)
            return;
        poseStack.pushPose();
        ClientLevel level = Minecraft.getInstance().level;
        Block block = BlockRegistry.CHUNK_LOADER.get();
        BlockStateModel model = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(block.defaultBlockState());
        ModelBlockRenderer.renderModel(poseStack.last(), multiBufferSource, model, 1f, 1f, 1f, packedLight, overlay, EmptyBlockAndTintGetter.INSTANCE, BlockPos.ZERO, block.defaultBlockState());
        float time = (level.getGameTime() + Minecraft.getInstance().getBlockEntityRenderDispatcher().camera.getPartialTickTime()) % 360;
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
                multiBufferSource,
                level,
                0);
        poseStack.translate(-0.5, -0.7, -0.5);
        poseStack.popPose();
    }

    @Override
    public Void extractArgument(ItemStack stack) {
        return null;
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
            return new ChunkLoaderSpecialRenderer(entityModelSet.bakeLayer(ModelLayers.CONDUIT_SHELL));
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked> type() {
            return CODEC;
        }
    }
}
