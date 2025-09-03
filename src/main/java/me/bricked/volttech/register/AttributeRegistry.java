package me.bricked.volttech.register;

import me.bricked.volttech.VoltTech;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.BooleanAttribute;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AttributeRegistry {
    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, VoltTech.MODID);
    public static Holder<Attribute> PHASE;
    public static Holder<Attribute> EVASION;
    public static Holder<Attribute> JETPACK_FLIGHT;
    public static Holder<Attribute> JETPACK_SPEED;
    public static void registerAttributes(IEventBus modEventBus) {
        PHASE = ATTRIBUTES.register(
                "phase",
                () -> new BooleanAttribute(VoltTech.MODID + ".phase", false).
                        setSyncable(true));
        EVASION = ATTRIBUTES.register(
                "evasion",
                () -> new PercentageAttribute(VoltTech.MODID + ".evasion", 0.0, 0.0, 1.0).
                        setSyncable(true));
        JETPACK_FLIGHT = ATTRIBUTES.register(
                "jetpack_flight",
                () -> new BooleanAttribute(VoltTech.MODID + ".jetpack_flight", false).
                        setSyncable(true));
        JETPACK_SPEED = ATTRIBUTES.register(
                "jetpack_speed",
                () -> new PercentageAttribute(VoltTech.MODID + ".jetpack_speed", 0.025, 0.0, 1024.0, 1000.0).
                        setSyncable(true));
        ATTRIBUTES.register(modEventBus);
    }
}
