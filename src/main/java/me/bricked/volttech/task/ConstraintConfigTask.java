package me.bricked.volttech.task;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.network.s2c.ConstraintsConfigPayload;
import me.bricked.volttech.util.Constraints;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.network.ConfigurationTask;
import net.neoforged.neoforge.network.configuration.ICustomConfigurationTask;

import java.util.function.Consumer;

public class ConstraintConfigTask implements ICustomConfigurationTask {
    public static final ConfigurationTask.Type TYPE = new ConfigurationTask.Type(VoltTech.resourceLocation("constraint_config_task"));

    @Override
    public void run(Consumer<CustomPacketPayload> sender) {
        VoltTech.getLogger().info("Starting constraints payload task");
        ConstraintsConfigPayload payload = new ConstraintsConfigPayload(Constraints.getConstraintsMap());
        sender.accept(payload);
    }

    @Override
    public Type type() {
        return TYPE;
    }
}
