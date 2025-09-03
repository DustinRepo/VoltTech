package me.bricked.volttech.register;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.blockentity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BlockEntityRegistry {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, VoltTech.MODID);

    public static Supplier<BlockEntityType<EnergyCableBlockEntity>> COPPER_ENERGY_CABLE_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<EnergyCableBlockEntity>> IRON_ENERGY_CABLE_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<EnergyCableBlockEntity>> GOLD_ENERGY_CABLE_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<EnergyCableBlockEntity>> DIAMOND_ENERGY_CABLE_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<EnergyCableBlockEntity>> EMERALD_ENERGY_CABLE_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<EnergyCableBlockEntity>> NETHERITE_ENERGY_CABLE_BLOCK_ENTITY;
    
    public static Supplier<BlockEntityType<FluidPipeBlockEntity>> COPPER_FLUID_PIPE_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<FluidPipeBlockEntity>> IRON_FLUID_PIPE_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<FluidPipeBlockEntity>> GOLD_FLUID_PIPE_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<FluidPipeBlockEntity>> DIAMOND_FLUID_PIPE_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<FluidPipeBlockEntity>> EMERALD_FLUID_PIPE_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<FluidPipeBlockEntity>> NETHERITE_FLUID_PIPE_BLOCK_ENTITY;

    public static Supplier<BlockEntityType<ItemPipeBlockEntity>> ITEM_PIPE_BLOCK_ENTITY;

    public static Supplier<BlockEntityType<FluidTankBlockEntity>> SMALL_FLUID_TANK_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<FluidTankBlockEntity>> MEDIUM_FLUID_TANK_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<FluidTankBlockEntity>> LARGE_FLUID_TANK_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<FluidTankBlockEntity>> MASSIVE_FLUID_TANK_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<FluidTankBlockEntity>> CREATIVE_FLUID_TANK_BLOCK_ENTITY;

    public static Supplier<BlockEntityType<EnergyCubeBlockEntity>> SMALL_ENERGY_CUBE_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<EnergyCubeBlockEntity>> MEDIUM_ENERGY_CUBE_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<EnergyCubeBlockEntity>> LARGE_ENERGY_CUBE_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<EnergyCubeBlockEntity>> MASSIVE_ENERGY_CUBE_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<EnergyCubeBlockEntity>> CREATIVE_ENERGY_CUBE_BLOCK_ENTITY;

    public static Supplier<BlockEntityType<HarvesterBlockEntity>> HARVESTER_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<BlockBreakerBlockEntity>> BLOCK_BREAKER_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<BlockPlacerBlockEntity>> BLOCK_PLACER_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<SolarGeneratorBlockEntity>> SOLAR_GENERATOR_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<CombustionGeneratorBlockEntity>> COMBUSTION_GENERATOR_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<HeatGeneratorBlockEntity>> HEAT_GENERATOR_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<MiniReactorBlockEntity>> MINI_REACTOR_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<TemporalAcceleratorBlockEntity>> TEMPORAL_ACCELERATOR_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<WirelessTransmitterBlockEntity>> WIRELESS_ENERGY_TRANSMITTER_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<SpatialCrateBlockEntity>> SPATIAL_CRATE_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<UpgradeTableBlockEntity>> UPGRADE_TABLE_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<PoweredFurnaceBlockEntity>> POWERED_FURNACE_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<CrusherBlockEntity>> CRUSHER_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<WirelessPlayerChargerBlockEntity>> WIRELESS_PLAYER_CHARGER_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<FoodMasherBlockEntity>> FOOD_MASHER_BLOCK_ENTITY;
    public static Supplier<BlockEntityType<ChunkLoaderBlockEntity>> CHUNK_LOADER_BLOCK_ENTITY;

    public static void registerBlockEntities(IEventBus modEventBus) {
        SMALL_ENERGY_CUBE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "small_energy_cube_block_entity",
                () -> new BlockEntityType<>(
                        (pos, state) -> new EnergyCubeBlockEntity(SMALL_ENERGY_CUBE_BLOCK_ENTITY.get(), pos, state),
                        BlockRegistry.SMALL_ENERGY_CUBE.get())
        );
        MEDIUM_ENERGY_CUBE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "medium_energy_cube_block_entity",
                () -> new BlockEntityType<>((pos, state) ->
                                new EnergyCubeBlockEntity(MEDIUM_ENERGY_CUBE_BLOCK_ENTITY.get(), pos, state),
                                BlockRegistry.MEDIUM_ENERGY_CUBE.get())
        );
        LARGE_ENERGY_CUBE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "large_energy_cube_block_entity",
                () -> new BlockEntityType<>((pos, state) ->
                                new EnergyCubeBlockEntity(LARGE_ENERGY_CUBE_BLOCK_ENTITY.get(), pos, state),
                                BlockRegistry.LARGE_ENERGY_CUBE.get())
        );
        MASSIVE_ENERGY_CUBE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "massive_energy_cube_block_entity",
                () -> new BlockEntityType<>((pos, state) ->
                                        new EnergyCubeBlockEntity(MASSIVE_ENERGY_CUBE_BLOCK_ENTITY.get(), pos, state),
                                BlockRegistry.MASSIVE_ENERGY_CUBE.get())
        );
        CREATIVE_ENERGY_CUBE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "creative_energy_cube_block_entity",
                () -> new BlockEntityType<>((pos, state) ->
                                        new EnergyCubeBlockEntity(CREATIVE_ENERGY_CUBE_BLOCK_ENTITY.get(), pos, state),
                                BlockRegistry.CREATIVE_ENERGY_CUBE.get())
        );
        COPPER_ENERGY_CABLE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "copper_energy_cable_block_entity",
                () -> new BlockEntityType<>((pos, blockState) ->
                                new EnergyCableBlockEntity(COPPER_ENERGY_CABLE_BLOCK_ENTITY.get(), pos, blockState),
                        BlockRegistry.COPPER_ENERGY_CABLE.get())
        );
        IRON_ENERGY_CABLE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "iron_energy_cable_block_entity",
                () -> new BlockEntityType<>((pos, blockState) ->
                                new EnergyCableBlockEntity(IRON_ENERGY_CABLE_BLOCK_ENTITY.get(), pos, blockState),
                        BlockRegistry.IRON_ENERGY_CABLE.get())
        );
        GOLD_ENERGY_CABLE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "gold_energy_cable_block_entity",
                () -> new BlockEntityType<>((pos, blockState) ->
                                new EnergyCableBlockEntity(GOLD_ENERGY_CABLE_BLOCK_ENTITY.get(), pos, blockState),
                        BlockRegistry.GOLD_ENERGY_CABLE.get())
        );
        DIAMOND_ENERGY_CABLE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "diamond_energy_cable_block_entity",
                () -> new BlockEntityType<>((pos, blockState) ->
                                new EnergyCableBlockEntity(DIAMOND_ENERGY_CABLE_BLOCK_ENTITY.get(), pos, blockState),
                        BlockRegistry.DIAMOND_ENERGY_CABLE.get())
        );
        EMERALD_ENERGY_CABLE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "emerald_energy_cable_block_entity",
                () -> new BlockEntityType<>((pos, blockState) ->
                                new EnergyCableBlockEntity(EMERALD_ENERGY_CABLE_BLOCK_ENTITY.get(), pos, blockState),
                        BlockRegistry.EMERALD_ENERGY_CABLE.get())
        );
        NETHERITE_ENERGY_CABLE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "netherite_energy_cable_block_entity",
                () -> new BlockEntityType<>((pos, blockState) ->
                                new EnergyCableBlockEntity(NETHERITE_ENERGY_CABLE_BLOCK_ENTITY.get(), pos, blockState),
                        BlockRegistry.NETHERITE_ENERGY_CABLE.get())
        );
        COPPER_FLUID_PIPE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "copper_fluid_pipe_block_entity",
                () -> new BlockEntityType<>((pos, blockState) ->
                                new FluidPipeBlockEntity(COPPER_FLUID_PIPE_BLOCK_ENTITY.get(), pos, blockState),
                        BlockRegistry.COPPER_FLUID_PIPE.get())
        );
        IRON_FLUID_PIPE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "iron_fluid_pipe_block_entity",
                () -> new BlockEntityType<>((pos, blockState) ->
                                new FluidPipeBlockEntity(IRON_FLUID_PIPE_BLOCK_ENTITY.get(), pos, blockState),
                        BlockRegistry.IRON_FLUID_PIPE.get())
        );
        GOLD_FLUID_PIPE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "gold_fluid_pipe_block_entity",
                () -> new BlockEntityType<>((pos, blockState) ->
                                new FluidPipeBlockEntity(GOLD_FLUID_PIPE_BLOCK_ENTITY.get(), pos, blockState),
                        BlockRegistry.GOLD_FLUID_PIPE.get())
        );
        DIAMOND_FLUID_PIPE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "diamond_fluid_pipe_block_entity",
                () -> new BlockEntityType<>((pos, blockState) ->
                                new FluidPipeBlockEntity(DIAMOND_FLUID_PIPE_BLOCK_ENTITY.get(), pos, blockState),
                        BlockRegistry.DIAMOND_FLUID_PIPE.get())
        );
        EMERALD_FLUID_PIPE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "emerald_fluid_pipe_block_entity",
                () -> new BlockEntityType<>((pos, blockState) ->
                                new FluidPipeBlockEntity(EMERALD_FLUID_PIPE_BLOCK_ENTITY.get(), pos, blockState),
                        BlockRegistry.EMERALD_FLUID_PIPE.get())
        );
        NETHERITE_FLUID_PIPE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "netherite_fluid_pipe_block_entity",
                () -> new BlockEntityType<>((pos, blockState) ->
                                new FluidPipeBlockEntity(NETHERITE_FLUID_PIPE_BLOCK_ENTITY.get(), pos, blockState),
                        BlockRegistry.NETHERITE_FLUID_PIPE.get())
        );
        ITEM_PIPE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "item_pipe_block_entity",
                () -> new BlockEntityType<>(
                        ItemPipeBlockEntity::new,
                        BlockRegistry.ITEM_PIPE.get())
        );
        SMALL_FLUID_TANK_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "small_fluid_tank_block_entity",
                () -> new BlockEntityType<>((pos, blockState) ->
                                new FluidTankBlockEntity(SMALL_FLUID_TANK_BLOCK_ENTITY.get(), pos, blockState),
                        BlockRegistry.SMALL_FLUID_TANK.get())
        );
        MEDIUM_FLUID_TANK_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "medium_fluid_tank_block_entity",
                () -> new BlockEntityType<>((pos, blockState) ->
                                new FluidTankBlockEntity(MEDIUM_FLUID_TANK_BLOCK_ENTITY.get(), pos, blockState),
                        BlockRegistry.MEDIUM_FLUID_TANK.get())
        );
        LARGE_FLUID_TANK_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "large_fluid_tank_block_entity",
                () -> new BlockEntityType<>((pos, blockState) ->
                                new FluidTankBlockEntity(LARGE_FLUID_TANK_BLOCK_ENTITY.get(), pos, blockState),
                        BlockRegistry.LARGE_FLUID_TANK.get())
        );
        MASSIVE_FLUID_TANK_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "massive_fluid_tank_block_entity",
                () -> new BlockEntityType<>((pos, blockState) ->
                                new FluidTankBlockEntity(MASSIVE_FLUID_TANK_BLOCK_ENTITY.get(), pos, blockState),
                        BlockRegistry.MASSIVE_FLUID_TANK.get())
        );
        CREATIVE_FLUID_TANK_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "creative_fluid_tank_block_entity",
                () -> new BlockEntityType<>((pos, blockState) ->
                                new FluidTankBlockEntity(CREATIVE_FLUID_TANK_BLOCK_ENTITY.get(), pos, blockState),
                        BlockRegistry.CREATIVE_FLUID_TANK.get())
        );
        HARVESTER_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "harvester_block_entity",
                () -> new BlockEntityType<>(
                        HarvesterBlockEntity::new,
                        BlockRegistry.HARVESTER.get())
        );
        BLOCK_BREAKER_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "block_breaker_block_entity",
                () -> new BlockEntityType<>(
                        BlockBreakerBlockEntity::new,
                        BlockRegistry.BLOCK_BREAKER.get())
        );
        BLOCK_PLACER_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "block_placer_block_entity",
                () -> new BlockEntityType<>(
                        BlockPlacerBlockEntity::new,
                        BlockRegistry.BLOCK_PLACER.get())
        );
        SOLAR_GENERATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "solar_generator_block_entity",
                () -> new BlockEntityType<>(
                        SolarGeneratorBlockEntity::new,
                        BlockRegistry.SOLAR_GENERATOR.get())
        );
        COMBUSTION_GENERATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "combustion_generator_block_entity",
                () -> new BlockEntityType<>(
                        CombustionGeneratorBlockEntity::new,
                        BlockRegistry.COMBUSTION_GENERATOR.get())
        );
        HEAT_GENERATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "heat_generator_block_entity",
                () -> new BlockEntityType<>(
                        HeatGeneratorBlockEntity::new,
                        BlockRegistry.HEAT_GENERATOR.get())
        );
        MINI_REACTOR_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "mini_reactor_block_entity",
                () -> new BlockEntityType<>(
                        MiniReactorBlockEntity::new,
                        BlockRegistry.MINI_REACTOR.get())
        );
        TEMPORAL_ACCELERATOR_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "temporal_accelerator_block_entity",
                () -> new BlockEntityType<>(
                        TemporalAcceleratorBlockEntity::new,
                        BlockRegistry.TEMPORAL_ACCELERATOR.get())
        );
        WIRELESS_ENERGY_TRANSMITTER_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "wireless_energy_transmitter_block_entity",
                () -> new BlockEntityType<>(
                        WirelessTransmitterBlockEntity::new,
                        BlockRegistry.WIRELESS_ENERGY_TRANSMITTER.get())
        );
        SPATIAL_CRATE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "spatial_crate_block_entity",
                () -> new BlockEntityType<>(
                        SpatialCrateBlockEntity::new,
                        BlockRegistry.SPATIAL_CRATE.get())
        );
        UPGRADE_TABLE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "upgrade_table_block_entity",
                () -> new BlockEntityType<>(
                        UpgradeTableBlockEntity::new,
                        BlockRegistry.UPGRADE_TABLE.get())
        );
        POWERED_FURNACE_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "powered_furnace_block_entity",
                () -> new BlockEntityType<>(
                        PoweredFurnaceBlockEntity::new,
                        BlockRegistry.POWERED_FURNACE.get())
        );
        CRUSHER_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "crusher_block_entity",
                () -> new BlockEntityType<>(
                        CrusherBlockEntity::new,
                        BlockRegistry.CRUSHER.get())
        );
        WIRELESS_PLAYER_CHARGER_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "wireless_player_charger_block_entity",
                () -> new BlockEntityType<>(
                        WirelessPlayerChargerBlockEntity::new,
                        BlockRegistry.WIRELESS_PLAYER_CHARGER.get())
        );
        FOOD_MASHER_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "food_masher_block_entity",
                () -> new BlockEntityType<>(
                        FoodMasherBlockEntity::new,
                        BlockRegistry.FOOD_MASHER.get())
        );
        CHUNK_LOADER_BLOCK_ENTITY = BLOCK_ENTITIES.register(
                "chunk_loader_block_entity",
                () -> new BlockEntityType<>(
                        ChunkLoaderBlockEntity::new,
                        BlockRegistry.CHUNK_LOADER.get())
        );
        BLOCK_ENTITIES.register(modEventBus);
    }
}
