package me.bricked.volttech;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = VoltTech.MODID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER;
    
    static {
        BUILDER = new ModConfigSpec.Builder();
        BUILDER.push("Harvester");
        HARVESTER_TICK_DELAY = BUILDER.
                comment("How many ticks between each block in the Harvester's zone being checked").
                defineInRange("harvesterTickDelay", 1, 0, 20);
        BUILDER.pop();
        BUILDER.push("Block_Breaker");
        BLOCK_BREAKER_TICK_DELAY = BUILDER.
                comment("How many ticks between each time the Block Breaker checks for a new block after breaking").
                defineInRange("blockBreakerTickDelay", 5, 0, 20);
        BUILDER.pop();
        BUILDER.push("Block_Placer");
        BLOCK_PLACER_TICK_DELAY = BUILDER.
                comment("How many ticks between each block in the Block Placer checking for a block").
                defineInRange("blockPlacerTickDelay", 5, 0, 20);
        BUILDER.pop();
        BUILDER.push("Solar");
        SOLAR_GENERATOR_RAIN_MULTIPLIER = BUILDER.
                comment("A multiplier of the generation rate while raining (lower number = generates less while raining)").
                defineInRange("solarGeneratorRainMultiplier", 0.5, 0, 1);
        BUILDER.pop();
        BUILDER.push("Heat");
        HEAT_GENERATOR_ICE_TICKS = BUILDER.
                comment("How many ticks Ice lasts for in the Heat Generator").
                defineInRange("heatGeneratorIceTicks", 200, 0, 5000);
        HEAT_GENERATOR_ICE_MULTIPLIER = BUILDER.
                comment("How much to multiply the output of the Heat Generator when consuming Ice").
                defineInRange("heatGeneratorIceMultiplier", 2.f, 1.f, 20);
        HEAT_GENERATOR_WARM_DIM_MULTIPLIER = BUILDER.
                comment("How much to multiply the output of the Heat Generator when in an ultrawarm dimension like the Nether").
                defineInRange("heatGeneratorWarmDimMultiplier", 1.5f, 1.f, 20);
        BUILDER.pop();
        BUILDER.push("Mini_Reactor");
        MINI_REACTOR_WATER_CAPACITY = BUILDER.
                comment("The amount of water in mB that the Mini Reactor will hold").
                defineInRange("miniReactorWaterCapacity", 16000, 1000, Integer.MAX_VALUE);
        MINI_REACTOR_WATER_USAGE = BUILDER.
                comment("The amount of water in mB that the Mini Reactor should use when it consumes Uranium").
                defineInRange("miniReactorWaterUsage", 1000, 10, Integer.MAX_VALUE);
        MINI_REACTOR_URANIUM_FE_PER_TICK = BUILDER.
                comment("The amount of Forge Energy that should be generated per tick while consuming Uranium").
                defineInRange("miniReactorFEPerTick", 8000, 1, Integer.MAX_VALUE);
        MINI_REACTOR_URANIUM_TICK_DURATION = BUILDER.
                comment("The amount of Forge Energy that should be generated per tick while consuming Uranium. Multiply FEPerTick * TickDuration for FE per Uranium Ingot").
                defineInRange("miniReactorUraniumTickDuration", 500, 1, Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("Temporal_Accelerator");
        TEMPORAL_ACCELERATOR_ACCELERATE_MULT = BUILDER.
                comment("The amount of extra times the Temporal Accelerator will run it's target block's tick method").
                defineInRange("temporalAcceleratorAccelerateMult", 8, 1, 128);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static final ModConfigSpec.DoubleValue SOLAR_GENERATOR_RAIN_MULTIPLIER;
    public static final ModConfigSpec.IntValue HARVESTER_TICK_DELAY;
    public static final ModConfigSpec.IntValue BLOCK_BREAKER_TICK_DELAY;
    public static final ModConfigSpec.IntValue BLOCK_PLACER_TICK_DELAY;
    public static final ModConfigSpec.IntValue HEAT_GENERATOR_ICE_TICKS;
    public static final ModConfigSpec.DoubleValue HEAT_GENERATOR_ICE_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue HEAT_GENERATOR_WARM_DIM_MULTIPLIER;

    public static final ModConfigSpec.IntValue MINI_REACTOR_WATER_CAPACITY;
    public static final ModConfigSpec.IntValue MINI_REACTOR_WATER_USAGE;
    public static final ModConfigSpec.IntValue MINI_REACTOR_URANIUM_FE_PER_TICK;
    public static final ModConfigSpec.IntValue MINI_REACTOR_URANIUM_TICK_DURATION;

    public static final ModConfigSpec.IntValue TEMPORAL_ACCELERATOR_ACCELERATE_MULT;

    static final ModConfigSpec SPEC;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
    }
}
