package me.bricked.volttech.screen;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.network.c2s.SetPortalGunSeedPayload;
import me.bricked.volttech.register.DataComponentRegistry;
import me.bricked.volttech.util.StringifyUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class PortalGunScreen extends Screen {
    public PortalGunScreen(ItemStack itemStack) {
        super(StringifyUtil.translate("gui.portal_gun_title"));
        this.itemStack = itemStack;
        this.minecraft = Minecraft.getInstance();
        this.imageWidth = 176;
        this.imageHeight = 133;
    }
    private final ResourceLocation TEXTURE = VoltTech.resourceLocation("textures/gui/portal_gun_gui.png");
    private final ItemStack itemStack;
    private int x, y;
    private final int imageWidth;
    private final int imageHeight;

    @Override
    protected void init() {
        super.init();
        this.x = (this.width - imageWidth) / 2;
        this.y = (this.height - imageHeight) / 2;
        String seedString = itemStack.getOrDefault(DataComponentRegistry.PORTAL_SEED, "");
        EditBox seedEdit;
        EditBox xEdit;
        EditBox yEdit;
        EditBox zEdit;
        this.addRenderableWidget(seedEdit = new EditBox(this.font, this.x + 2, this.y + 15, 120, 20, Component.literal(seedString)));
        this.addRenderableWidget(xEdit = new EditBox(this.font, this.x + (this.imageWidth / 2) - 80, this.y + 60, 50, 20, Component.literal("")));
        this.addRenderableWidget(yEdit = new EditBox(this.font, this.x + (this.imageWidth / 2) - 25, this.y + 60, 50, 20, Component.literal("")));
        this.addRenderableWidget(zEdit = new EditBox(this.font, this.x + (this.imageWidth / 2) + 30, this.y + 60, 50, 20, Component.literal("")));
        seedEdit.setMaxLength(32);
        if (!itemStack.getOrDefault(DataComponentRegistry.PORTAL_HOME, false))
            seedEdit.setValue(seedString);
        LocalPlayer player = minecraft.player;
        xEdit.setValue(String.valueOf(StringifyUtil.singlePointFormat.format(player.getX())));
        yEdit.setValue(String.valueOf(StringifyUtil.singlePointFormat.format(player.getY())));
        zEdit.setValue(String.valueOf(StringifyUtil.singlePointFormat.format(player.getZ())));
        this.addRenderableWidget(new Button.Builder(Component.translatable("gui.done"), button -> {
            if (player == null)
                return;
            int slot = player.getInventory().findSlotMatchingItem(this.itemStack);
            if (slot == -1) {
                player.displayClientMessage(StringifyUtil.translate("gui.portal_gun_slot_error"), false);
                return;
            }
            String seed = seedEdit.getValue();
            boolean sendHome = seed.isEmpty();
            if (seed.isEmpty())
                seed = "Home";
            SetPortalGunSeedPayload.PortalLocation location = new SetPortalGunSeedPayload.PortalLocation(Vec3.ZERO, false);
            String xString = xEdit.getValue();
            String yString = yEdit.getValue();
            String zString = zEdit.getValue();
            if (!xString.isEmpty() || !yString.isEmpty() || !zString.isEmpty()) {
                try {
                    double x = Double.parseDouble(xString);
                    double y = Double.parseDouble(yString);
                    double z = Double.parseDouble(zString);
                    location = new SetPortalGunSeedPayload.PortalLocation(new Vec3(x, y, z), true);
                } catch (NumberFormatException e) {
                    player.displayClientMessage(StringifyUtil.translate("gui.portal_gun_coordinates_error"), false);
                    return;
                }
            }
            SetPortalGunSeedPayload payload = new SetPortalGunSeedPayload(slot, seed, sendHome, location);
            ClientPacketDistributor.sendToServer(payload);
            minecraft.setScreen(null);
        }).pos(this.x + (this.imageWidth / 2) - 50, this.y + (this.imageHeight / 2) + 25).size(100, 20).build());
        this.addRenderableWidget(new Button.Builder(StringifyUtil.translate("gui.portal_gun_set_home"), button -> {
            seedEdit.setValue("");
        }).pos(this.x + 122, this.y + 15).size(50, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.x, this.y, 0.0F, 0.0F, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight, 256, 256);
        guiGraphics.drawCenteredString(font, StringifyUtil.translate("gui.portal_gun_seed"), this.x + (this.imageWidth / 2), this.y + 5, -12566464);
        guiGraphics.drawCenteredString(font, StringifyUtil.translate("gui.portal_gun_coordinates"), this.x + (this.imageWidth / 2), this.y + 40, -12566464);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
