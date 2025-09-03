package me.bricked.volttech.event;

import me.bricked.volttech.VoltTechClient;
import me.bricked.volttech.network.c2s.JetpackTogglePayload;
import me.bricked.volttech.register.AttributeRegistry;
import me.bricked.volttech.util.Constraints;
import me.bricked.volttech.util.ItemUtil;
import me.bricked.volttech.util.StringifyUtil;
import me.bricked.volttech.util.UpgradeData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.ArrayList;
import java.util.Arrays;

@EventBusSubscriber
public class ClientEvents {

    @SubscribeEvent
    public static void connectToServer(ClientPlayerNetworkEvent.LoggingIn event) {
        ArrayList<String> mismatched = UpgradeData.getMismatches();
        if (!mismatched.isEmpty()) {
            event.getPlayer().displayClientMessage(StringifyUtil.translate("message.mismatch_upgradeables", Arrays.toString(mismatched.toArray())), false);
        }
    }

    @SubscribeEvent
    public static void disconnectFromServer(ClientPlayerNetworkEvent.LoggingOut event) {
        // Disconnected from server, reload our own configs
        Constraints.readConfigs();
        UpgradeData.readConfig();
        // Write config again incase the current config file is outdated
        // This combines the read config items and any default upgradeable items that aren't in the json
        UpgradeData.writeConfig();
        // Clear recipe map for JEI
        VoltTechClient.recipeMap = null;
    }

    @SubscribeEvent
    public static void postClientTick(ClientTickEvent.Post event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null)
            return;
        while (VoltTechClient.JETPACK_TOGGLE_MAPPING.get().consumeClick()) {
            int jetpackSlot = -1;
            for (int i = 0; i < EquipmentSlot.values().length; i++) {
                ItemStack itemStack = player.getItemBySlot(EquipmentSlot.values()[i]);
                if (ItemUtil.canBeJetpack(itemStack) && ItemUtil.hasJetpackCharge(itemStack)) {
                    jetpackSlot = i;
                    break;
                }
            }
            if (jetpackSlot != -1)
                ClientPacketDistributor.sendToServer(new JetpackTogglePayload(jetpackSlot));
        }
        if (player.getAttributeValue(AttributeRegistry.JETPACK_FLIGHT) >= 1.0) {
            boolean ascend = VoltTechClient.JETPACK_ASCEND.get().isDown();
            boolean descend = VoltTechClient.JETPACK_DESCEND.get().isDown();
            double vertical = 0.2 + (player.getAttributeValue(AttributeRegistry.JETPACK_SPEED) / 2.f);
            Vec3 movement = player.getDeltaMovement();

            if (ascend && !descend)
                movement = new Vec3(movement.x, vertical, movement.z);
            else if (descend && !ascend)
                movement = new Vec3(movement.x, -vertical, movement.z);
            else if (movement.y < 0)
                movement = new Vec3(movement.x, 0, movement.z);
            player.resetFallDistance();
            player.setDeltaMovement(movement);
        }
    }

}
