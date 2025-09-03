package me.bricked.volttech.task;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.network.s2c.ConstraintsConfigPayload;
import me.bricked.volttech.network.s2c.UpgradeablesConfigPayload;
import me.bricked.volttech.util.Constraints;
import me.bricked.volttech.util.UpgradeData;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.configuration.ICustomConfigurationTask;

import java.util.function.Consumer;

public class UpgradeablesConfigTask implements ICustomConfigurationTask {
    public static final Type TYPE = new Type(VoltTech.resourceLocation("upgradeables_config_task"));

    @Override
    public void run(Consumer<CustomPacketPayload> sender) {
        VoltTech.getLogger().info("Starting upgradeables payload task");
        UpgradeablesConfigPayload payload = new UpgradeablesConfigPayload(UpgradeData.getDataMap());
        sender.accept(payload);
    }

    @Override
    public Type type() {
        return TYPE;
    }
}
