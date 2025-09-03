package me.bricked.volttech.register;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.entity.MultiversePortalEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class EntityRegistry {
    private static DeferredRegister.Entities ENTITIES = DeferredRegister.createEntities(VoltTech.MODID);
    public static Supplier<EntityType<MultiversePortalEntity>> MULTIVERSE_PORTAL;

    public static void registerEntities(IEventBus modEventBus) {
        MULTIVERSE_PORTAL = ENTITIES.registerEntityType(
                "multiverse_portal",
                MultiversePortalEntity::new,
                MobCategory.MISC,
                builder -> builder.
                        sized(1, 2).
                        noSummon().
                        updateInterval(10)
        );
        ENTITIES.register(modEventBus);
    }
}
