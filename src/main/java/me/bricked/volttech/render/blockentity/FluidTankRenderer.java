package me.bricked.volttech.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import me.bricked.volttech.blockentity.FluidTankBlockEntity;
import me.bricked.volttech.util.RenderUtil;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public class FluidTankRenderer implements BlockEntityRenderer<FluidTankBlockEntity> {
    public FluidTankRenderer(BlockEntityRendererProvider.Context context) {}
    @Override
    public void render(FluidTankBlockEntity fluidTankBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay, Vec3 vec3) {
        FluidTank tank = fluidTankBlockEntity.getTank();
        Fluid fluid = tank.getFluid().getFluid();
        float fillPercent = (float) tank.getFluidAmount() / tank.getCapacity();
        int fluidLight = fluid.getFluidType().getLightLevel();
        int packedLight = light;
        if (fluidLight > 0)
            packedLight = LightTexture.pack(fluidLight, fluidLight);
        if (tank.getFluidAmount() > 0)
            RenderUtil.renderFluid(poseStack, multiBufferSource, fluid, new AABB(3.01f / 16.f, 1.01f / 16.f, 3.01f / 16.f, 12.99f / 16.f, (14.99f * fillPercent) / 16.f, 12.99f / 16.f), packedLight, overlay);
    }
}
