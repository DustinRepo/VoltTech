package me.bricked.volttech.screen;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.menu.SpatialCrateMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;

public class SpatialCrateScreen extends AbstractContainerScreen<SpatialCrateMenu> {
    public SpatialCrateScreen(SpatialCrateMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 356;
        this.imageHeight = 278;
        this.titleLabelY = 3;
    }

    private final ResourceLocation TEXTURE = VoltTech.resourceLocation("textures/gui/mega_container_gui.png");

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight, 512, 512);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderSlot(GuiGraphics guiGraphics, Slot slot) {
        ItemStack slotStack = slot.getItem();
        if (slotStack.isEmpty() || slot.container instanceof Inventory || slot.getItem().getCount() <= 64) {
            super.renderSlot(guiGraphics, slot);
            return;
        }
        // Render a fake slot with 1 item so it doesn't show a count and render the count ourselves
        // this is needed because in Screen::renderSlot the count gets capped to ItemStack::getMaxStackSize
        Slot fakeSlot = new Slot(new SimpleContainer(190), slot.getContainerSlot(), slot.x, slot.y);
        fakeSlot.set(slotStack.getItem().getDefaultInstance());
        super.renderSlot(guiGraphics, fakeSlot);
        int stackCount = slotStack.getCount();
        String count = String.valueOf(stackCount);
        if (stackCount > 1000) {
            count = "%.1fk".formatted(stackCount / 1000.f);
        }
        Matrix3x2fStack poseStack = guiGraphics.pose();
        if (stackCount > 1) {
            poseStack.pushMatrix();
            poseStack.translate(0.0F, 0.0F, poseStack);
            guiGraphics.drawStringWithBackdrop(font, Component.literal(count), slot.x + 19 - 2 - font.width(count), slot.y + 6 + 3, 0, 16777215);
            poseStack.translate(0.0F, 0.0F, poseStack);
            poseStack.popMatrix();
        }
    }
}
