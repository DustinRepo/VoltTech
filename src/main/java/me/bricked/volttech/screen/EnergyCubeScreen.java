package me.bricked.volttech.screen;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.menu.EnergyCubeMenu;
import me.bricked.volttech.util.RenderUtil;
import me.bricked.volttech.util.StringifyUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.joml.Vector4i;

public class EnergyCubeScreen extends AbstractContainerScreen<EnergyCubeMenu> {

    public EnergyCubeScreen(EnergyCubeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    private final ResourceLocation TEXTURE = VoltTech.resourceLocation("textures/gui/energy_storage_block_gui.png");
    private final Vector4i feBarPosition = new Vector4i(158, 15, 168, 78);

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        int feBarWidth = feBarPosition.z - feBarPosition.x;
        int feBarHeight = feBarPosition.w - feBarPosition.y;
        float fePercent = Mth.clamp((float) menu.getStoredFE() / (float) menu.getMaxFE(), 0, 1);
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
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawStringWithBackdrop(Minecraft.getInstance().font, StringifyUtil.translate("gui.max_in", StringifyUtil.stringifyEnergy(menu.getMaxInput())), this.leftPos + 30, this.topPos + 18, 0, 0xffffffff);
        guiGraphics.drawStringWithBackdrop(Minecraft.getInstance().font, StringifyUtil.translate("gui.max_out", StringifyUtil.stringifyEnergy(menu.getMaxOutput())), this.leftPos + 30, this.topPos + 28, 0, 0xffffffff);
        if (RenderUtil.isHovered(mouseX - leftPos, mouseY - topPos, feBarPosition))
            guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, Component.literal("%s / %s".formatted(StringifyUtil.stringifyEnergy(menu.getStoredFE()), StringifyUtil.stringifyEnergy(menu.getMaxFE()))), mouseX, mouseY);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);

    }
}
