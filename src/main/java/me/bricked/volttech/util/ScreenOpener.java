package me.bricked.volttech.util;

import me.bricked.volttech.screen.PortalGunScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public class ScreenOpener {
    public static void openPortalGunScreen(ItemStack itemStack) {
        PortalGunScreen screen = new PortalGunScreen(itemStack);
        Minecraft.getInstance().setScreen(screen);
    }
}
