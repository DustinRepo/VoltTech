package me.bricked.volttech.screen;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.menu.WirelessEnergyTransmitterMenu;
import me.bricked.volttech.util.StringifyUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class WirelessEnergyTransmitterScreen extends AbstractContainerScreen<WirelessEnergyTransmitterMenu> {
    public WirelessEnergyTransmitterScreen(WirelessEnergyTransmitterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 116;
    }
    private final ResourceLocation TEXTURE = VoltTech.resourceLocation("textures/gui/wireless_transmitter_gui.png");

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {}

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Font font = Minecraft.getInstance().font;
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(font, StringifyUtil.translate("gui.place_location_cards"), this.leftPos + (this.imageWidth / 2), this.topPos + 3, 0xffffffff);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
