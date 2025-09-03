package me.bricked.volttech.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import me.bricked.volttech.blockentity.HarvesterBlockEntity;
import me.bricked.volttech.util.RenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class HarvesterRenderer implements BlockEntityRenderer<HarvesterBlockEntity> {
    public HarvesterRenderer(BlockEntityRendererProvider.Context context) {}
    @Override
    public void render(HarvesterBlockEntity harvesterBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1, Vec3 vec3) {
        if (!harvesterBlockEntity.isRenderBox())
            return;
        poseStack.pushPose();
        AABB box = new AABB(0, 0, 0, harvesterBlockEntity.getxSize(), harvesterBlockEntity.getySize(), harvesterBlockEntity.getzSize());
        box = box.move(BlockPos.ZERO.relative(harvesterBlockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING)));
        box = box.move(harvesterBlockEntity.getxOffset(), harvesterBlockEntity.getyOffset(), harvesterBlockEntity.getzOffset());
        RenderUtil.drawFilledBox(poseStack, multiBufferSource, box, 0x5000ff00);
        poseStack.popPose();
    }
}
