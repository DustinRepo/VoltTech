package me.bricked.volttech.screen;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.blockentity.MiniReactorBlockEntity;
import me.bricked.volttech.menu.MiniReactorMenu;
import me.bricked.volttech.util.RenderUtil;
import me.bricked.volttech.util.StringifyUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.joml.Vector4i;

import java.util.ArrayList;

public class MiniReactorScreen extends AbstractContainerScreen<MiniReactorMenu> {
    public MiniReactorScreen(MiniReactorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }
    private final ResourceLocation TEXTURE = VoltTech.resourceLocation("textures/gui/mini_reactor_gui.png");

    private final Vector4i feBarPosition = new Vector4i(158, 15, 168, 78);
    private final Vector4i waterBarPosition = new Vector4i(144, 15, 154, 78);
    private final Vector4i progressBarPosition = new Vector4i(28, 68, 140, 78);

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        MiniReactorBlockEntity miniReactorBlockEntity = menu.getMiniReactorBlockEntity();
        IEnergyStorage energyStorage = miniReactorBlockEntity.getEnergyStorage(miniReactorBlockEntity.getBlockState(), null);
        FluidTank tank = miniReactorBlockEntity.getWaterTank();
        RenderUtil.FluidSpriteInfo fluidSpriteInfo = RenderUtil.getSpriteInfo(tank.getFluid());
        int waterBarWidth = waterBarPosition.z - waterBarPosition.x;
        int waterBarHeight = waterBarPosition.w - waterBarPosition.y;
        int feBarWidth = feBarPosition.z - feBarPosition.x;
        int feBarHeight = feBarPosition.w - feBarPosition.y;
        int progressBarWidth = progressBarPosition.z - progressBarPosition.x;
        int progressBarHeight = progressBarPosition.w - progressBarPosition.y;
        float waterPercent = Mth.clamp((float) tank.getFluidAmount() / (float) tank.getCapacity(), 0, 1);
        float fePercent = Mth.clamp((float) energyStorage.getEnergyStored() / (float) energyStorage.getMaxEnergyStored(), 0, 1);
        float progressPercent = Mth.clamp((float) miniReactorBlockEntity.getConsumeTicksLeft() / (float) miniReactorBlockEntity.getMaxConsumeTicks(), 0, 1);
        waterBarHeight = (int) (waterBarHeight * waterPercent);
        feBarHeight = (int) (feBarHeight * fePercent);
        progressBarWidth = (int) (progressBarWidth * progressPercent);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight, 256, 256);
        if (fluidSpriteInfo != null)
            RenderUtil.renderFluidInGui(guiGraphics, fluidSpriteInfo, this.leftPos + waterBarPosition.x, this.topPos + waterBarPosition.w - waterBarHeight, waterBarWidth, waterBarHeight);
        RenderUtil.renderFluidInGui(guiGraphics, RenderUtil.FORGE_ENERGY_SPRITE, this.leftPos + feBarPosition.x, this.topPos + feBarPosition.w - feBarHeight, feBarWidth, feBarHeight);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos + progressBarPosition.x, this.topPos + progressBarPosition.y, 0, 166, progressBarWidth, progressBarHeight, progressBarWidth, progressBarHeight, 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        MiniReactorBlockEntity miniReactorBlockEntity = menu.getMiniReactorBlockEntity();
        IEnergyStorage energyStorage = miniReactorBlockEntity.getEnergyStorage(miniReactorBlockEntity.getBlockState(), null);
        FluidTank tank = miniReactorBlockEntity.getWaterTank();

        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawStringWithBackdrop(font, StringifyUtil.translate("gui.generator_status"), this.leftPos + 30, this.topPos + 18, 0, 0xffffffff);
        if (miniReactorBlockEntity.getConsumeTicksLeft() > 0)
            guiGraphics.drawStringWithBackdrop(font, StringifyUtil.translate("gui.reactor_running"), this.leftPos + 30, this.topPos + 28, 0, 0xffffffff);
        else
            guiGraphics.drawStringWithBackdrop(font, StringifyUtil.translate("gui.reactor_idle"), this.leftPos + 30, this.topPos + 28, 0, 0xffffffff);
        if (RenderUtil.isHovered(mouseX - leftPos, mouseY - topPos, feBarPosition)) {
            float percent = ((float) energyStorage.getEnergyStored() / (float) energyStorage.getMaxEnergyStored()) * 100.f;
            ArrayList<FormattedCharSequence> tooltips = new ArrayList<>();
            tooltips.add(Component.literal("%s/%s".formatted(StringifyUtil.stringifyEnergy(energyStorage.getEnergyStored()), StringifyUtil.stringifyEnergy(energyStorage.getMaxEnergyStored()))).getVisualOrderText());
            tooltips.add(Component.literal("%.1f".formatted(percent) + "%").getVisualOrderText());
            guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, tooltips, mouseX, mouseY);
        }
        if (RenderUtil.isHovered(mouseX - leftPos, mouseY - topPos, waterBarPosition)) {
            float percent = ((float) tank.getFluidAmount() / tank.getCapacity()) * 100.f;
            ArrayList<FormattedCharSequence> tooltips = new ArrayList<>();
            tooltips.add(Component.literal("%s: %dmB".formatted(Language.getInstance().getOrDefault(tank.getFluid().getFluid().getFluidType().getDescriptionId()), tank.getFluidAmount())).getVisualOrderText());
            tooltips.add(Component.literal("%.1f".formatted(percent) + "%").getVisualOrderText());
            guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, tooltips, mouseX, mouseY);
        }
        if (RenderUtil.isHovered(mouseX - leftPos, mouseY - topPos, progressBarPosition)) {
            float percent = ((float) miniReactorBlockEntity.getConsumeTicksLeft() / miniReactorBlockEntity.getMaxConsumeTicks()) * 100.f;
            ArrayList<FormattedCharSequence> tooltips = new ArrayList<>();
            tooltips.add(Component.literal("%.1f".formatted(percent) + "%").getVisualOrderText());
            guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, tooltips, mouseX, mouseY);
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
