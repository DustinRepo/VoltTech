package me.bricked.volttech.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.bricked.volttech.blockentity.EnergyCableBlockEntity;
import me.bricked.volttech.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.EmptyBlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class EnergyCableRenderer implements BlockEntityRenderer<EnergyCableBlockEntity> {
    public EnergyCableRenderer(BlockEntityRendererProvider.Context context) {}
    @Override
    public void render(EnergyCableBlockEntity energyCableBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay, Vec3 vec3) {
        if (energyCableBlockEntity.hasCover()) {
            ResourceLocation coverLocation = ResourceLocation.parse(energyCableBlockEntity.getCoverBlockID());
            BuiltInRegistries.BLOCK.get(coverLocation).ifPresent(blockReference -> {
                Block block = blockReference.value();
                BlockState defaultState = block.defaultBlockState();
                BlockStateModel model = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(defaultState);
                ModelBlockRenderer.renderModel(poseStack.last(), multiBufferSource, model, 1f, 1f, 1f, light, overlay, EmptyBlockAndTintGetter.INSTANCE, BlockPos.ZERO, defaultState);
            });
        }
        poseStack.translate(0.5, 0.5, 0.5);
        for (Direction direction : Direction.values()) {
            if (!energyCableBlockEntity.isDirectionBlocked(direction))
                continue;
            poseStack.pushPose();

            Matrix4f pose = poseStack.last().pose();
            RenderType renderType = RenderType.DEBUG_QUADS;
            VertexConsumer consumer = multiBufferSource.getBuffer(renderType);

            float minX = -0.125f;
            float minZ = -0.125f;
            float maxX = 0.125f;
            float maxZ = 0.125f;
            float y = 0.126f;
            int color = 0x99ff0000;
            RenderUtil.directionRotate(poseStack, direction.getOpposite());
            consumer.addVertex(pose, minX, y, minZ).setColor(color);
            consumer.addVertex(pose, minX, y, maxZ).setColor(color);
            consumer.addVertex(pose, maxX, y, maxZ).setColor(color);
            consumer.addVertex(pose, maxX, y, minZ).setColor(color);
            RenderUtil.directionRotate(poseStack, direction);

            poseStack.popPose();
        }
    }
}
