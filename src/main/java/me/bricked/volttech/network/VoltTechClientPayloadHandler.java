package me.bricked.volttech.network;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.network.c2s.ConstraintConfigAckPayload;
import me.bricked.volttech.network.c2s.UpgradeablesConfigAckPayload;
import me.bricked.volttech.network.s2c.AddDimensionPayload;
import me.bricked.volttech.network.s2c.ConstraintsConfigPayload;
import me.bricked.volttech.network.s2c.UpgradeablesConfigPayload;
import me.bricked.volttech.util.Constraints;
import me.bricked.volttech.util.StringifyUtil;
import me.bricked.volttech.util.UpgradeData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class VoltTechClientPayloadHandler {
    public static void handleAddDimension(final AddDimensionPayload payload, final IPayloadContext context) {
        final LocalPlayer player = Minecraft.getInstance().player;
        if (player == null)
            return;
        player.connection.levels().add(payload.key());
    }

    public static void handleConstraintsConfig(final ConstraintsConfigPayload payload, final IPayloadContext context) {
        VoltTech.getLogger().info("[Client] Client receive constraints payload");
        Constraints.setConstraintsMap(payload.constraintsMap());
        context.reply(new ConstraintConfigAckPayload());
    }

    public static void handleUpgradeablesConfig(final UpgradeablesConfigPayload payload, final IPayloadContext context) {
        VoltTech.getLogger().info("[Client] Client receive upgradeables payload");
        HashMap<String, UpgradeData> newMap = payload.dataMap();
        HashMap<String, UpgradeData> oldMap = UpgradeData.getDataMap();

        ArrayList<String> mismatched = UpgradeData.getMismatches();
        mismatched.clear();
        newMap.forEach((s, upgradeData) -> {
            if (!oldMap.containsKey(s)) {
                // mismatch, inform the player
                mismatched.add(s);
            }
        });

        UpgradeData.setDataMap(payload.dataMap());
        context.reply(new UpgradeablesConfigAckPayload());
    }
}
