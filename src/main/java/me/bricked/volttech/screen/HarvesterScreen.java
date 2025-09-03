package me.bricked.volttech.screen;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.menu.HarvesterMenu;
import me.bricked.volttech.register.BlockRegistry;
import me.bricked.volttech.util.Constraints;
import me.bricked.volttech.util.RenderUtil;
import me.bricked.volttech.util.StringifyUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.joml.Vector4i;

import java.util.ArrayList;

public class HarvesterScreen extends AbstractContainerScreen<HarvesterMenu> {
    public HarvesterScreen(HarvesterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.constraints = Constraints.get(BlockRegistry.HARVESTER.get());
    }
    private final Constraints constraints;
    private final ResourceLocation TEXTURE = VoltTech.resourceLocation("textures/gui/harvester_gui.png");


    private final Vector4i feBarPosition = new Vector4i(238, 15, 248, 168);

    @Override
    protected void init() {
        this.imageWidth = 256;
        this.imageHeight = 256;
        super.init();
        this.addRenderableWidget(Button.builder(
                        Component.literal("<"),
                        button -> {
                            menu.setXOffset(menu.getXOffset() - 1);
                        }).
                bounds(this.leftPos + 10, this.topPos + 40, 20, 20).
                build()
        );
        this.addRenderableWidget(Button.builder(
                        Component.literal(">"),
                        button -> {
                            menu.setXOffset(menu.getXOffset() + 1);
                        }).
                bounds(this.leftPos + 30, this.topPos + 40, 20, 20).
                size(20, 20).
                build()
        );
        this.addRenderableWidget(Button.builder(
                        Component.literal("<"),
                        button -> {
                            menu.setYOffset(menu.getYOffset() - 1);
                        }).
                bounds(this.leftPos + 60, this.topPos + 40, 20, 20).
                build()
        );
        this.addRenderableWidget(Button.builder(
                        Component.literal(">"),
                        button -> {
                            menu.setYOffset(menu.getYOffset() + 1);
                        }).
                bounds(this.leftPos + 80, this.topPos + 40, 20, 20).
                size(20, 20).
                build()
        );
        this.addRenderableWidget(Button.builder(
                        Component.literal("<"),
                        button -> {
                            menu.setZOffset(menu.getZOffset() - 1);
                        }).
                bounds(this.leftPos + 110, this.topPos + 40, 20, 20).
                build()
        );
        this.addRenderableWidget(Button.builder(
                        Component.literal(">"),
                        button -> {
                            menu.setZOffset(menu.getZOffset() + 1);
                        }).
                bounds(this.leftPos + 130, this.topPos + 40, 20, 20).
                size(20, 20).
                build()
        );

        this.addRenderableWidget(Button.builder(
                        Component.literal("<"),
                        button -> {
                            menu.setXSize(menu.getXSize() - 1);
                        }).
                bounds(this.leftPos + 10, this.topPos + 88, 20, 20).
                build()
        );
        this.addRenderableWidget(Button.builder(
                        Component.literal(">"),
                        button -> {
                            menu.setXSize(menu.getXSize() + 1);
                        }).
                bounds(this.leftPos + 30, this.topPos + 88, 20, 20).
                size(20, 20).
                build()
        );
        this.addRenderableWidget(Button.builder(
                        Component.literal("<"),
                        button -> {
                            menu.setYSize(menu.getYSize() - 1);
                        }).
                bounds(this.leftPos + 60, this.topPos + 88, 20, 20).
                build()
        );
        this.addRenderableWidget(Button.builder(
                        Component.literal(">"),
                        button -> {
                            menu.setYSize(menu.getYSize() + 1);
                        }).
                bounds(this.leftPos + 80, this.topPos + 88, 20, 20).
                size(20, 20).
                build()
        );
        this.addRenderableWidget(Button.builder(
                        Component.literal("<"),
                        button -> {
                            menu.setZSize(menu.getZSize() - 1);
                        }).
                bounds(this.leftPos + 110, this.topPos + 88, 20, 20).
                build()
        );
        this.addRenderableWidget(Button.builder(
                        Component.literal(">"),
                        button -> {
                            menu.setZSize(menu.getZSize() + 1);
                        }).
                bounds(this.leftPos + 130, this.topPos + 88, 20, 20).
                size(20, 20).
                build()
        );
        this.addRenderableWidget(Button.builder(
                        getToggleButtonMessage(),
                        button -> {
                            menu.setShouldRenderBox(!menu.shouldRenderBox());
                            button.setMessage(getToggleButtonMessage());
                        }).
                bounds(this.leftPos + 151, this.topPos + 40, 20, 20).
                size(85, 20).
                build()
        );
    }

    private Component getToggleButtonMessage() {
        return menu.shouldRenderBox() ? StringifyUtil.translate("gui.show_box_true") : StringifyUtil.translate("gui.show_box_false");
    }

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
        guiGraphics.drawCenteredString(font, StringifyUtil.translate("gui.offsets"), this.leftPos + 80, this.topPos + 18, 0xffffffff);
        guiGraphics.drawCenteredString(font, "X: " + menu.getXOffset(), this.leftPos + 30, this.topPos + 28, 0xffffffff);
        guiGraphics.drawCenteredString(font, "Y: " + menu.getYOffset(), this.leftPos + 80, this.topPos + 28, 0xffffffff);
        guiGraphics.drawCenteredString(font, "Z: " + menu.getZOffset(), this.leftPos + 130, this.topPos + 28, 0xffffffff);

        guiGraphics.drawCenteredString(font, StringifyUtil.translate("gui.size"), this.leftPos + 80, this.topPos + 65, 0xffffffff);
        guiGraphics.drawCenteredString(font, "X: " + menu.getXSize(), this.leftPos + 30, this.topPos + 75, 0xffffffff);
        guiGraphics.drawCenteredString(font, "Y: " + menu.getYSize(), this.leftPos + 80, this.topPos + 75, 0xffffffff);
        guiGraphics.drawCenteredString(font, "Z: " + menu.getZSize(), this.leftPos + 130, this.topPos + 75, 0xffffffff);

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
