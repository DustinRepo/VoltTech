package me.bricked.volttech.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.*;

import java.lang.Math;
import java.util.function.Consumer;

public class RenderUtil {
    public static FluidSpriteInfo FORGE_ENERGY_SPRITE = new FluidSpriteInfo(getSpriteInfo(new FluidStack(Fluids.WATER, 1)).sprite(), 0xff00ff33);;

    public static boolean isHovered(float mouseX, float mouseY, Vector4i vector4i) {
        return isHovered(mouseX, mouseY, vector4i.x, vector4i.y, vector4i.z - vector4i.x, vector4i.w - vector4i.y);
    }

    public static boolean isHovered(float mouseX, float mouseY, float x, float y, float width, float height) {
        return x <= mouseX && x + width > mouseX && y <= mouseY && y + height > mouseY;
    }

    public static void renderFluidInGui(GuiGraphics guiGraphics, FluidSpriteInfo fluidSpriteInfo, int x, int y, int width, int height) {
        renderFluidInGui(guiGraphics, x, y, width, height, fluidSpriteInfo.sprite(), fluidSpriteInfo.tintColor());
    }

    public static void directionRotate(PoseStack poseStack, Direction direction) {
        switch (direction) {
            case UP -> poseStack.mulPose(Axis.XP.rotationDegrees(180));
            case NORTH -> poseStack.mulPose(Axis.XP.rotationDegrees(90));
            case SOUTH -> poseStack.mulPose(Axis.XP.rotationDegrees(-90));
            case WEST -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(90));
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
            }
            case EAST -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(90));
                poseStack.mulPose(Axis.XP.rotationDegrees(-90));
            }
        }
    }

    public static void renderFluidInGui(GuiGraphics guiGraphics, int x, int y, int width, int height, TextureAtlasSprite sprite, int color) {
        guiGraphics.pose().pushMatrix();
        for (int dy = 0; dy < height; dy += 16) {
            for (int dx = 0; dx < width; dx += 16) {
                int drawHeight = Math.min(16, height - dy);
                int drawWidth = Math.min(16, width - dx);
                guiGraphics.blitSprite(
                        RenderPipelines.GUI_TEXTURED,
                        sprite,
                        x + dx, y + dy,
                        drawWidth, drawHeight,
                        color
                );
            }
        }
        guiGraphics.pose().popMatrix();
    }

    public static void renderFluid(PoseStack poseStack, MultiBufferSource multiBufferSource, Fluid fluid, AABB cuboid, int light, int overlay) {
        RenderType renderType = Sheets.translucentItemSheet();
        VertexConsumer consumer = multiBufferSource.getBuffer(renderType);
        FluidSpriteInfo spriteInfo = getSpriteInfo(fluid);
        TextureAtlasSprite sprite = spriteInfo.sprite();

        Vector3f vec = new Vector3f(0, 1, 0);

        float minX = (float) cuboid.minX;
        float minY = (float) cuboid.minY;
        float minZ = (float) cuboid.minZ;
        float maxX = (float) cuboid.maxX;
        float maxY = (float) cuboid.maxY;
        float maxZ = (float) cuboid.maxZ;

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        int color = spriteInfo.tintColor();
        poseStack.pushPose();
        PoseStack.Pose pose = poseStack.last();

        // Top face
        consumer.addVertex(pose, minX, maxY, minZ).setColor(color).setUv(u0, v0).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());
        consumer.addVertex(pose, minX, maxY, maxZ).setColor(color).setUv(u0, v1).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());
        consumer.addVertex(pose, maxX, maxY, maxZ).setColor(color).setUv(u1, v1).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());
        consumer.addVertex(pose, maxX, maxY, minZ).setColor(color).setUv(u1, v0).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());

        // Bottom face
        consumer.addVertex(pose, minX, minY, minZ).setColor(color).setUv(u0, v0).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());
        consumer.addVertex(pose, maxX, minY, minZ).setColor(color).setUv(u1, v0).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());
        consumer.addVertex(pose, maxX, minY, maxZ).setColor(color).setUv(u1, v1).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());
        consumer.addVertex(pose, minX, minY, maxZ).setColor(color).setUv(u0, v1).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());

        // North face
        consumer.addVertex(pose, minX, minY, minZ).setColor(color).setUv(u0, v1).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());
        consumer.addVertex(pose, minX, maxY, minZ).setColor(color).setUv(u0, v0).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());
        consumer.addVertex(pose, maxX, maxY, minZ).setColor(color).setUv(u1, v0).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());
        consumer.addVertex(pose, maxX, minY, minZ).setColor(color).setUv(u1, v1).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());

        // South face
        consumer.addVertex(pose, maxX, minY, maxZ).setColor(color).setUv(u0, v1).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());
        consumer.addVertex(pose, maxX, maxY, maxZ).setColor(color).setUv(u0, v0).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());
        consumer.addVertex(pose, minX, maxY, maxZ).setColor(color).setUv(u1, v0).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());
        consumer.addVertex(pose, minX, minY, maxZ).setColor(color).setUv(u1, v1).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());

        // West face
        consumer.addVertex(pose, minX, minY, maxZ).setColor(color).setUv(u0, v1).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());
        consumer.addVertex(pose, minX, maxY, maxZ).setColor(color).setUv(u0, v0).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());
        consumer.addVertex(pose, minX, maxY, minZ).setColor(color).setUv(u1, v0).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());
        consumer.addVertex(pose, minX, minY, minZ).setColor(color).setUv(u1, v1).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());

        // East face
        consumer.addVertex(pose, maxX, minY, minZ).setColor(color).setUv(u0, v1).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());
        consumer.addVertex(pose, maxX, maxY, minZ).setColor(color).setUv(u0, v0).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());
        consumer.addVertex(pose, maxX, maxY, maxZ).setColor(color).setUv(u1, v0).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());
        consumer.addVertex(pose, maxX, minY, maxZ).setColor(color).setUv(u1, v1).setOverlay(overlay).setLight(light).setNormal(pose, vec.x(), vec.y(), vec.z());

        poseStack.popPose();
    }

    public static void drawFilledBox(PoseStack poseStack, MultiBufferSource source, AABB bb, int color) {
        Matrix4f pose = poseStack.last().pose();
        RenderType renderType = RenderType.DEBUG_QUADS;
        VertexConsumer consumer = source.getBuffer(renderType);
        float minX = (float)bb.minX;
        float minY = (float)bb.minY;
        float minZ = (float)bb.minZ;
        float maxX = (float)bb.maxX;
        float maxY = (float)bb.maxY;
        float maxZ = (float)bb.maxZ;
        Consumer<Vec3> vertex = (pos) -> consumer
                .addVertex(pose, (float) pos.x, (float) pos.y, (float) pos.z)
                .setColor(color);

        // Bottom (-Y)
        vertex.accept(new Vec3(minX, minY, minZ));
        vertex.accept(new Vec3(maxX, minY, minZ));
        vertex.accept(new Vec3(maxX, minY, maxZ));
        vertex.accept(new Vec3(minX, minY, maxZ));

        // Top (+Y)
        vertex.accept(new Vec3(minX, maxY, minZ));
        vertex.accept(new Vec3(minX, maxY, maxZ));
        vertex.accept(new Vec3(maxX, maxY, maxZ));
        vertex.accept(new Vec3(maxX, maxY, minZ));

        // Front (-Z)
        vertex.accept(new Vec3(minX, minY, minZ));
        vertex.accept(new Vec3(minX, maxY, minZ));
        vertex.accept(new Vec3(maxX, maxY, minZ));
        vertex.accept(new Vec3(maxX, minY, minZ));

        // Back (+Z)
        vertex.accept(new Vec3(maxX, minY, maxZ));
        vertex.accept(new Vec3(maxX, maxY, maxZ));
        vertex.accept(new Vec3(minX, maxY, maxZ));
        vertex.accept(new Vec3(minX, minY, maxZ));

        // Left (-X)
        vertex.accept(new Vec3(minX, minY, maxZ));
        vertex.accept(new Vec3(minX, maxY, maxZ));
        vertex.accept(new Vec3(minX, maxY, minZ));
        vertex.accept(new Vec3(minX, minY, minZ));

        // Right (+X)
        vertex.accept(new Vec3(maxX, minY, minZ));
        vertex.accept(new Vec3(maxX, maxY, minZ));
        vertex.accept(new Vec3(maxX, maxY, maxZ));
        vertex.accept(new Vec3(maxX, minY, maxZ));
    }

    public static FluidSpriteInfo getSpriteInfo(Fluid fluid) {
        return getSpriteInfo(new FluidStack(fluid, 1));
    }

    public static FluidSpriteInfo getSpriteInfo(FluidStack fluidStack) {
        if (fluidStack.isEmpty())
            return null;
        Fluid fluid = fluidStack.getFluid();
        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
        ResourceLocation spriteLocation = extensions.getStillTexture(fluidStack);
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(spriteLocation);
        int tintColor = extensions.getTintColor();
        return new FluidSpriteInfo(sprite, tintColor);
    }

    public record FluidSpriteInfo(TextureAtlasSprite sprite, int tintColor){}
}
