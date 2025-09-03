package me.bricked.volttech.screen;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.menu.CombustionGeneratorMenu;
import me.bricked.volttech.register.BlockRegistry;
import me.bricked.volttech.util.Constraints;
import me.bricked.volttech.util.RenderUtil;
import me.bricked.volttech.util.StringifyUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.joml.Vector4i;

import java.util.ArrayList;

public class CombustionGeneratorScreen extends AbstractContainerScreen<CombustionGeneratorMenu> {
    public CombustionGeneratorScreen(CombustionGeneratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.titleLabelY = 4;
        this.constraints = Constraints.get(BlockRegistry.COMBUSTION_GENERATOR.get());
    }
    private final Constraints constraints;
    private final ResourceLocation TEXTURE = VoltTech.resourceLocation("textures/gui/combustion_generator_gui.png");
    private final Vector4i feBarPosition = new Vector4i(158, 15, 168, 78);
    private final Vector4i progressBarPosition = new Vector4i(62, 54, 114, 70);

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        int feBarWidth = feBarPosition.z - feBarPosition.x;
        int feBarHeight = feBarPosition.w - feBarPosition.y;
        int progressBarWidth = progressBarPosition.z - progressBarPosition.x;
        int progressBarHeight = progressBarPosition.w - progressBarPosition.y;
        float progressPercent = Mth.clamp((float) menu.getBurnTicksLeft() / (float) menu.getMaxBurnTicks(), 0, 1);
        float fePercent = Mth.clamp((float) menu.getStoredFE() / (float) constraints.maxCapacity(), 0, 1);
        progressBarWidth = (int) (progressBarWidth * progressPercent);
        feBarHeight = (int) (feBarHeight * fePercent);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight, 256, 256);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,TEXTURE, this.leftPos + progressBarPosition.x, this.topPos + progressBarPosition.y, 0, 166, progressBarWidth, progressBarHeight, progressBarWidth, progressBarHeight, 256, 256);
        RenderUtil.renderFluidInGui(guiGraphics, RenderUtil.FORGE_ENERGY_SPRITE, this.leftPos + feBarPosition.x, this.topPos + feBarPosition.w - feBarHeight, feBarWidth, feBarHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Font font = Minecraft.getInstance().font;
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawStringWithBackdrop(font, StringifyUtil.translate("gui.generator_status"), this.leftPos + 10, this.topPos + 18, 0, 0xffffffff);

        if (menu.getStoredFE() >= constraints.maxCapacity()) {
            guiGraphics.drawStringWithBackdrop(font, StringifyUtil.translate("gui.generator_full"), this.leftPos + 10, this.topPos + 28, 0, 0xff00aa00);
        } else if (menu.getBurnTicksLeft() > 0) {
            guiGraphics.drawStringWithBackdrop(font, StringifyUtil.translate("gui.generating_pertick", StringifyUtil.stringifyEnergy(constraints.usageOrGeneration())), this.leftPos + 10, this.topPos + 28, 0, 0xff00ff50);
            guiGraphics.drawStringWithBackdrop(font, StringifyUtil.translate("gui.coal_generator_left_to_burn", StringifyUtil.stringifyEnergy(menu.getBurnTicksLeft() * constraints.usageOrGeneration())), this.leftPos + 10, this.topPos + 38, 0, 0xff00ff50);
        } else {
            guiGraphics.drawStringWithBackdrop(font, StringifyUtil.translate("gui.coal_generator_no_fuel", StringifyUtil.stringifyEnergy(constraints.usageOrGeneration())), this.leftPos + 10, this.topPos + 28, 0, 0xffff5000);
        }
        if (RenderUtil.isHovered(mouseX - leftPos, mouseY - topPos, feBarPosition)) {
            float percent = ((float) menu.getStoredFE() / constraints.maxCapacity()) * 100.f;
            ArrayList<FormattedCharSequence> tooltips = new ArrayList<>();
            tooltips.add(Component.literal("%s/%s".formatted(StringifyUtil.stringifyEnergy(menu.getStoredFE()), StringifyUtil.stringifyEnergy(constraints.maxCapacity()))).getVisualOrderText());
            tooltips.add(Component.literal("%.2f".formatted(percent) + "%").getVisualOrderText());
            guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, tooltips, mouseX, mouseY);
        }
        if (RenderUtil.isHovered(mouseX - leftPos, mouseY - topPos, progressBarPosition)) {
            float percent = ((float) menu.getBurnTicksLeft() / menu.getMaxBurnTicks()) * 100.f;
            ArrayList<FormattedCharSequence> tooltips = new ArrayList<>();
            tooltips.add(Component.literal("%.1f".formatted(percent) + "%").getVisualOrderText());
            tooltips.add(StringifyUtil.translate("gui.ticks_left", menu.getBurnTicksLeft()).getVisualOrderText());
            guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, tooltips, mouseX, mouseY);
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
