package me.bricked.volttech.screen;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.menu.SolarGeneratorMenu;
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

public class SolarGeneratorScreen extends AbstractContainerScreen<SolarGeneratorMenu> {
    public SolarGeneratorScreen(SolarGeneratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.constraints = Constraints.get(BlockRegistry.SOLAR_GENERATOR.get());
    }
    private final Constraints constraints;
    private final ResourceLocation TEXTURE = VoltTech.resourceLocation("textures/gui/solar_generator_gui.png");


    private final Vector4i feBarPosition = new Vector4i(158, 15, 168, 78);

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        int feBarWidth = feBarPosition.z - feBarPosition.x;
        int feBarHeight = feBarPosition.w - feBarPosition.y;
        float fePercent = Mth.clamp((float) menu.getStoredFE() / (float) constraints.maxCapacity(), 0, 1);
        feBarHeight = (int) (feBarHeight * fePercent);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight, 256, 256);
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
        if (!menu.canSeeSky())
            guiGraphics.drawStringWithBackdrop(font, StringifyUtil.translate("gui.solar_no_sky"), this.leftPos + 10, this.topPos + 28, 0, 0xffff2525);
        else if (!menu.isDay())
            guiGraphics.drawStringWithBackdrop(font, StringifyUtil.translate("gui.solar_no_sun"), this.leftPos + 10, this.topPos + 28, 0, 0xffff2525);
        else if (menu.getStoredFE() == constraints.maxCapacity())
            guiGraphics.drawStringWithBackdrop(font, StringifyUtil.translate("gui.generator_full"), this.leftPos + 10, this.topPos + 28, 0, 0xff00aa00);
        else
            guiGraphics.drawStringWithBackdrop(font, StringifyUtil.translate("gui.generating_pertick", StringifyUtil.stringifyEnergy(menu.getGenerationRate())), this.leftPos + 8, this.topPos + 28, 0, 0xff00ff50);

        if (RenderUtil.isHovered(mouseX - leftPos, mouseY - topPos, feBarPosition)) {
            float percent = ((float) menu.getStoredFE() / constraints.maxCapacity()) * 100.f;
            ArrayList<FormattedCharSequence> tooltips = new ArrayList<>();
            tooltips.add(Component.literal("%s/%s".formatted(StringifyUtil.stringifyEnergy(menu.getStoredFE()), StringifyUtil.stringifyEnergy(constraints.maxCapacity()))).getVisualOrderText());
            tooltips.add(Component.literal("%.2f".formatted(percent) + "%").getVisualOrderText());
            guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, tooltips, mouseX, mouseY);
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
