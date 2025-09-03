package me.bricked.volttech.screen;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.menu.PoweredFurnaceMenu;
import me.bricked.volttech.util.RenderUtil;
import me.bricked.volttech.util.StringifyUtil;
import net.minecraft.client.Minecraft;
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

public class PoweredFurnaceScreen extends AbstractContainerScreen<PoweredFurnaceMenu> {
    public PoweredFurnaceScreen(PoweredFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }
    private final ResourceLocation TEXTURE = VoltTech.resourceLocation("textures/gui/powered_furnace_gui.png");


    private final Vector4i feBarPosition = new Vector4i(158, 15, 168, 78);

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        int feBarWidth = feBarPosition.z - feBarPosition.x;
        int feBarHeight = feBarPosition.w - feBarPosition.y;
        float fePercent = Mth.clamp((float) menu.getStoredFE() / (float) menu.getMaxFE(), 0, 1);
        feBarHeight = (int) (feBarHeight * fePercent);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight, 256, 256);
        RenderUtil.renderFluidInGui(guiGraphics, RenderUtil.FORGE_ENERGY_SPRITE, this.leftPos + feBarPosition.x, this.topPos + feBarPosition.w - feBarHeight, feBarWidth, feBarHeight);
        int x = 28;
        int y = 35;
        for (int j = 0; j < 4; j++) {
            float progress = Mth.clamp((float) menu.getCookTime(j) / (float) menu.getMaxCookTime(j), 0, 1);
            int height = (int)(23 * progress);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos + x, this.topPos + y, 0.0F, this.imageHeight, 12, height, 12, height, 256, 256);
            x += 36;
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        if (RenderUtil.isHovered(mouseX - leftPos, mouseY - topPos, feBarPosition)) {
            float percent = ((float) menu.getStoredFE() / menu.getMaxFE()) * 100.f;
            ArrayList<FormattedCharSequence> tooltips = new ArrayList<>();
            tooltips.add(Component.literal("%s/%s".formatted(StringifyUtil.stringifyEnergy(menu.getStoredFE()), StringifyUtil.stringifyEnergy(menu.getMaxFE()))).getVisualOrderText());
            tooltips.add(Component.literal("%.2f".formatted(percent) + "%").getVisualOrderText());
            guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, tooltips, mouseX, mouseY);
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
