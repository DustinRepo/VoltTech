package me.bricked.volttech.event;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.register.AttributeRegistry;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderBlockScreenEffectEvent;

@EventBusSubscriber(modid = VoltTech.MODID)
public class RenderEvents {
    @SubscribeEvent
    public static void onRenderOverlay(RenderBlockScreenEffectEvent event) {
        Player player = event.getPlayer();
        if (player.getAttributeValue(AttributeRegistry.PHASE) >= 1) {
            event.setCanceled(true);
        }
    }
}
