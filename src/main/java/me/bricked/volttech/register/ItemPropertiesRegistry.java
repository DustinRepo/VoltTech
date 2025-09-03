package me.bricked.volttech.register;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.item.property.ChargedItemProperty;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;

@EventBusSubscriber(modid = VoltTech.MODID, value = Dist.CLIENT)
public class ItemPropertiesRegistry {
    @SubscribeEvent
    public static void registerItemProperties(RegisterConditionalItemModelPropertyEvent event) {
        event.register(VoltTech.resourceLocation("uncharged"), ChargedItemProperty.MAP_CODEC);
    }
}
