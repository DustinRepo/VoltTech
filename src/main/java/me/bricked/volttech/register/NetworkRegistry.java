package me.bricked.volttech.register;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.network.VoltTechClientPayloadHandler;
import me.bricked.volttech.network.VoltTechServerPayloadHandler;
import me.bricked.volttech.network.c2s.*;
import me.bricked.volttech.network.s2c.AddDimensionPayload;
import me.bricked.volttech.network.s2c.ConstraintsConfigPayload;
import me.bricked.volttech.network.s2c.UpgradeablesConfigPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = VoltTech.MODID)
public class NetworkRegistry {

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("volttech");

        // C2S
        registrar.playToServer(
                HarvesterOffsetsPayload.TYPE,
                HarvesterOffsetsPayload.STREAM_CODEC,
                VoltTechServerPayloadHandler::handleHarvesterSettings
        );
        registrar.playToServer(
                JetpackTogglePayload.TYPE,
                JetpackTogglePayload.STREAM_CODEC,
                VoltTechServerPayloadHandler::handleJetpackToggle
        );
        registrar.playToServer(
                SetPortalGunSeedPayload.TYPE,
                SetPortalGunSeedPayload.STREAM_CODEC,
                VoltTechServerPayloadHandler::handlePortalGunSeed
        );
        registrar.configurationToServer(
                ConstraintConfigAckPayload.TYPE,
                ConstraintConfigAckPayload.STREAM_CODEC,
                VoltTechServerPayloadHandler::handleConstraintsAckPayload
        );
        registrar.configurationToServer(
                UpgradeablesConfigAckPayload.TYPE,
                UpgradeablesConfigAckPayload.STREAM_CODEC,
                VoltTechServerPayloadHandler::handleUpgradeablesAckPayload
        );

        // S2C
        registrar.playToClient(
                AddDimensionPayload.TYPE,
                AddDimensionPayload.STREAM_CODEC,
                VoltTechClientPayloadHandler::handleAddDimension
        );
        registrar.configurationToClient(
                ConstraintsConfigPayload.TYPE,
                ConstraintsConfigPayload.STREAM_CODEC,
                VoltTechClientPayloadHandler::handleConstraintsConfig
        );
        registrar.configurationToClient(
                UpgradeablesConfigPayload.TYPE,
                UpgradeablesConfigPayload.STREAM_CODEC,
                VoltTechClientPayloadHandler::handleUpgradeablesConfig
        );
    }
}
