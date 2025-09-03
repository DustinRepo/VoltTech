package me.bricked.volttech.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.bricked.volttech.blockentity.UpgradeTableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class UpgradeTableRenderer implements BlockEntityRenderer<UpgradeTableBlockEntity> {
    public UpgradeTableRenderer(BlockEntityRendererProvider.Context context) {}
    @Override
    public void render(UpgradeTableBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, Vec3 vec3) {
        ItemStack displayItem = Items.AIR.getDefaultInstance();
        ResourceLocation displayItemRL = blockEntity.getDisplayItem();
        if (!displayItemRL.getPath().equalsIgnoreCase("air")) {
            Optional<Holder.Reference<Item>> ref = BuiltInRegistries.ITEM.get(displayItemRL);
            if (ref.isPresent()) {
                displayItem = ref.get().value().getDefaultInstance();
            }
        }
        float time = (blockEntity.getLevel().getGameTime() + partialTick) % 360;
        poseStack.pushPose();
        poseStack.translate(0.5, 0.85, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(time * 5));
        poseStack.scale(0.75f, 0.75f, 0.75f);
        Minecraft.getInstance().getItemRenderer().renderStatic(
                displayItem,
                ItemDisplayContext.FIXED,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                blockEntity.getLevel(),
                0
        );
        poseStack.popPose();
    }
}
