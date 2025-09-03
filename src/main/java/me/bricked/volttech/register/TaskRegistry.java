package me.bricked.volttech.register;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.task.ConstraintConfigTask;
import me.bricked.volttech.task.UpgradeablesConfigTask;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterConfigurationTasksEvent;

@EventBusSubscriber(modid = VoltTech.MODID)
public class TaskRegistry {

    @SubscribeEvent
    public static void registerConfigurationTasks(final RegisterConfigurationTasksEvent event) {
        event.register(new ConstraintConfigTask());
        event.register(new UpgradeablesConfigTask());
    }
}
