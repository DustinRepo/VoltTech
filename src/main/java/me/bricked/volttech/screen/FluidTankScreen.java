package me.bricked.volttech.screen;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.block.FluidTankBlock;
import me.bricked.volttech.menu.FluidTankMenu;
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
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.joml.Vector4i;

import java.util.ArrayList;

public class FluidTankScreen extends AbstractContainerScreen<FluidTankMenu> {
    public FluidTankScreen(FluidTankMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.titleLabelY -= 2;
        this.titleLabelX -= 2;
    }
    private final ResourceLocation TEXTURE = VoltTech.resourceLocation("textures/gui/fluid_tank_gui.png");

    private final Vector4i fluidTankPosition = new Vector4i(32, 14, 144, 78);

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight, 256, 256);
        FluidTank tank = menu.getFluidTankBlockEntity().getTank();
        int fluidBarWidth = fluidTankPosition.z - fluidTankPosition.x;
        int fluidBarHeight = fluidTankPosition.w - fluidTankPosition.y;
        int staticFluidBarHeight = fluidTankPosition.w - fluidTankPosition.y;
        if (!tank.isEmpty()) {
            float waterPercent = Mth.clamp((float) tank.getFluidAmount() / (float) tank.getCapacity(), 0, 1);
            fluidBarHeight = (int) (fluidBarHeight * waterPercent);
            RenderUtil.FluidSpriteInfo fluidSpriteInfo = RenderUtil.getSpriteInfo(tank.getFluid());
            RenderUtil.renderFluidInGui(guiGraphics, fluidSpriteInfo, this.leftPos + fluidTankPosition.x, this.topPos + fluidTankPosition.w - fluidBarHeight, fluidBarWidth, fluidBarHeight);
        }
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos + fluidTankPosition.x, this.topPos + fluidTankPosition.y, 0.0F, this.imageHeight, fluidBarWidth, staticFluidBarHeight, fluidBarWidth, staticFluidBarHeight, 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        if (RenderUtil.isHovered(mouseX - leftPos, mouseY - topPos, fluidTankPosition) && menu.getFluidTankBlockEntity() != null) {
            FluidTank tank = menu.getFluidTankBlockEntity().getTank();
            FluidTankBlock fluidTankBlock = (FluidTankBlock) menu.getFluidTankBlockEntity().getBlockState().getBlock();
            boolean isCreativeTank = fluidTankBlock.getTankTier() == FluidTankBlock.TankTier.CREATIVE;
            float percent = ((float) tank.getFluidAmount() / tank.getCapacity()) * 100.f;
            ArrayList<FormattedCharSequence> tooltips = new ArrayList<>();
            if (tank.isEmpty()) {
                tooltips.add(StringifyUtil.translate("gui.empty").getVisualOrderText());
            } else {
                if (isCreativeTank)
                    tooltips.add(StringifyUtil.translate("tooltip.infinite_contained_fluid", tank.getFluid().getFluidType().getDescription()).getVisualOrderText());
                else
                    tooltips.add(Component.literal("%s: %s".formatted(Language.getInstance().getOrDefault(tank.getFluid().getDescriptionId()), StringifyUtil.stringifyFluid(tank.getFluidAmount()))).getVisualOrderText());
                tooltips.add(Component.literal("%.1f".formatted(percent) + "%").getVisualOrderText());
            }
            guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, tooltips, mouseX, mouseY);
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
