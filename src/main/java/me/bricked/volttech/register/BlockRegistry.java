package me.bricked.volttech.register;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.block.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockRegistry {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(VoltTech.MODID);
    public static DeferredBlock<Block> URANIUM_ORE;
    public static DeferredBlock<Block> DEEPSLATE_URANIUM_ORE;
    public static DeferredBlock<Block> RAW_URANIUM_BLOCK;
    public static DeferredBlock<Block> URANIUM_BLOCK;
    public static DeferredBlock<Block> DALEKANIUM_ORE;
    public static DeferredBlock<Block> RAW_DALEKANIUM_BLOCK;
    public static DeferredBlock<Block> DALEKANIUM_BLOCK;
    public static DeferredBlock<Block> HELLISH_ROCK;
    public static DeferredBlock<Block> VELTRIUM_ORE;
    public static DeferredBlock<Block> VELTRIUM_BLOCK;
    public static DeferredBlock<Block> RAW_VELTRIUM_BLOCK;
    public static DeferredBlock<Block> DEEPSLATE_TREXALITE_ORE;
    public static DeferredBlock<Block> TREXALITE_BLOCK;
    public static DeferredBlock<Block> RAW_TREXALITE_BLOCK;

    public static DeferredBlock<LiquidBlock> ARGENT_PLASMA;

    public static DeferredBlock<EnergyCubeBlock> SMALL_ENERGY_CUBE;
    public static DeferredBlock<EnergyCubeBlock> MEDIUM_ENERGY_CUBE;
    public static DeferredBlock<EnergyCubeBlock> LARGE_ENERGY_CUBE;
    public static DeferredBlock<EnergyCubeBlock> MASSIVE_ENERGY_CUBE;
    public static DeferredBlock<EnergyCubeBlock> CREATIVE_ENERGY_CUBE;
    public static DeferredBlock<HarvesterBlock> HARVESTER;
    public static DeferredBlock<BlockBreakerBlock> BLOCK_BREAKER;
    public static DeferredBlock<BlockPlacerBlock> BLOCK_PLACER;
    public static DeferredBlock<SolarGeneratorBlock> SOLAR_GENERATOR;
    public static DeferredBlock<CombustionGeneratorBlock> COMBUSTION_GENERATOR;
    public static DeferredBlock<HeatGeneratorBlock> HEAT_GENERATOR;
    public static DeferredBlock<MiniReactorBlock> MINI_REACTOR;
    public static DeferredBlock<TemporalAcceleratorBlock> TEMPORAL_ACCELERATOR;
    public static DeferredBlock<WirelessEnergyTransmitterBlock> WIRELESS_ENERGY_TRANSMITTER;
    public static DeferredBlock<SpatialCrateBlock> SPATIAL_CRATE;
    public static DeferredBlock<UpgradeTableBlock> UPGRADE_TABLE;
    public static DeferredBlock<PoweredFurnaceBlock> POWERED_FURNACE;
    public static DeferredBlock<CrusherBlock> CRUSHER;
    public static DeferredBlock<WirelessPlayerChargerBlock> WIRELESS_PLAYER_CHARGER;
    public static DeferredBlock<FoodMasherBlock> FOOD_MASHER;
    public static DeferredBlock<ChunkLoaderBlock> CHUNK_LOADER;

    public static DeferredBlock<EnergyCableBlock> COPPER_ENERGY_CABLE;
    public static DeferredBlock<EnergyCableBlock> IRON_ENERGY_CABLE;
    public static DeferredBlock<EnergyCableBlock> GOLD_ENERGY_CABLE;
    public static DeferredBlock<EnergyCableBlock> DIAMOND_ENERGY_CABLE;
    public static DeferredBlock<EnergyCableBlock> EMERALD_ENERGY_CABLE;
    public static DeferredBlock<EnergyCableBlock> NETHERITE_ENERGY_CABLE;

    public static DeferredBlock<FluidPipeBlock> COPPER_FLUID_PIPE;
    public static DeferredBlock<FluidPipeBlock> IRON_FLUID_PIPE;
    public static DeferredBlock<FluidPipeBlock> GOLD_FLUID_PIPE;
    public static DeferredBlock<FluidPipeBlock> DIAMOND_FLUID_PIPE;
    public static DeferredBlock<FluidPipeBlock> EMERALD_FLUID_PIPE;
    public static DeferredBlock<FluidPipeBlock> NETHERITE_FLUID_PIPE;

    public static DeferredBlock<ItemPipeBlock> ITEM_PIPE;

    public static DeferredBlock<FluidTankBlock> SMALL_FLUID_TANK;
    public static DeferredBlock<FluidTankBlock> MEDIUM_FLUID_TANK;
    public static DeferredBlock<FluidTankBlock> LARGE_FLUID_TANK;
    public static DeferredBlock<FluidTankBlock> MASSIVE_FLUID_TANK;
    public static DeferredBlock<FluidTankBlock> CREATIVE_FLUID_TANK;

    public static void registerBlocks(IEventBus modEventBus) {
        URANIUM_ORE = BLOCKS.registerSimpleBlock(
                "uranium_ore",
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(2.0f, 6.0f));
        DEEPSLATE_URANIUM_ORE = BLOCKS.registerSimpleBlock(
                "deepslate_uranium_ore",
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(2.0f, 6.0f));
        URANIUM_BLOCK = BLOCKS.registerSimpleBlock(
                "uranium_block",
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(2.0f, 6.0f));
        RAW_URANIUM_BLOCK = BLOCKS.registerSimpleBlock(
                "raw_uranium_block",
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(2.0f, 6.0f));
        DALEKANIUM_ORE = BLOCKS.registerSimpleBlock(
                "dalekanium_ore",
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(2.5f, 6.0f));
        DALEKANIUM_BLOCK = BLOCKS.registerSimpleBlock(
                "dalekanium_block",
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(2.5f, 6.0f));
        RAW_DALEKANIUM_BLOCK = BLOCKS.registerSimpleBlock(
                "raw_dalekanium_block",
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(2.5f, 6.0f));
        HELLISH_ROCK = BLOCKS.registerSimpleBlock(
                "hellish_rock",
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(1.0f, 6.0f));
        VELTRIUM_ORE = BLOCKS.registerSimpleBlock(
                "veltrium_ore",
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(3.0f, 6.0f));
        VELTRIUM_BLOCK = BLOCKS.registerSimpleBlock(
                "veltrium_block",
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(3.0f, 6.0f));
        RAW_VELTRIUM_BLOCK = BLOCKS.registerSimpleBlock(
                "raw_veltrium_block",
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(3.0f, 6.0f));
        DEEPSLATE_TREXALITE_ORE = BLOCKS.registerSimpleBlock(
                "deepslate_trexalite_ore",
                BlockBehaviour.Properties.of().forceSolidOn().lightLevel(value -> 7).requiresCorrectToolForDrops().strength(3.0f, 6.0f));
        TREXALITE_BLOCK = BLOCKS.registerSimpleBlock(
                "trexalite_block",
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(3.0f, 6.0f));
        RAW_TREXALITE_BLOCK = BLOCKS.registerSimpleBlock(
                "raw_trexalite_block",
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(3.0f, 6.0f));
        COPPER_ENERGY_CABLE = BLOCKS.registerBlock(
                "copper_energy_cable",
                properties -> new EnergyCableBlock(properties, EnergyCableBlock.CableTier.COPPER),
                BlockBehaviour.Properties.of().forceSolidOn().instabreak());
        IRON_ENERGY_CABLE = BLOCKS.registerBlock(
                "iron_energy_cable",
                properties -> new EnergyCableBlock(properties, EnergyCableBlock.CableTier.IRON),
                BlockBehaviour.Properties.of().forceSolidOn().instabreak());
        GOLD_ENERGY_CABLE = BLOCKS.registerBlock(
                "gold_energy_cable",
                properties -> new EnergyCableBlock(properties, EnergyCableBlock.CableTier.GOLD),
                BlockBehaviour.Properties.of().forceSolidOn().instabreak());
        DIAMOND_ENERGY_CABLE = BLOCKS.registerBlock(
                "diamond_energy_cable",
                properties -> new EnergyCableBlock(properties, EnergyCableBlock.CableTier.DIAMOND),
                BlockBehaviour.Properties.of().forceSolidOn().instabreak());
        EMERALD_ENERGY_CABLE = BLOCKS.registerBlock(
                "emerald_energy_cable",
                properties -> new EnergyCableBlock(properties, EnergyCableBlock.CableTier.EMERALD),
                BlockBehaviour.Properties.of().forceSolidOn().instabreak());
        NETHERITE_ENERGY_CABLE = BLOCKS.registerBlock(
                "netherite_energy_cable",
                properties -> new EnergyCableBlock(properties, EnergyCableBlock.CableTier.NETHERITE),
                BlockBehaviour.Properties.of().forceSolidOn().instabreak());
        COPPER_FLUID_PIPE = BLOCKS.registerBlock(
                "copper_fluid_pipe",
                properties -> new FluidPipeBlock(properties, FluidPipeBlock.FluidPipeTier.COPPER),
                BlockBehaviour.Properties.of().forceSolidOn().instabreak());
        IRON_FLUID_PIPE = BLOCKS.registerBlock(
                "iron_fluid_pipe",
                properties -> new FluidPipeBlock(properties, FluidPipeBlock.FluidPipeTier.IRON),
                BlockBehaviour.Properties.of().forceSolidOn().instabreak());
        GOLD_FLUID_PIPE = BLOCKS.registerBlock(
                "gold_fluid_pipe",
                properties -> new FluidPipeBlock(properties, FluidPipeBlock.FluidPipeTier.GOLD),
                BlockBehaviour.Properties.of().forceSolidOn().instabreak());
        DIAMOND_FLUID_PIPE = BLOCKS.registerBlock(
                "diamond_fluid_pipe",
                properties -> new FluidPipeBlock(properties, FluidPipeBlock.FluidPipeTier.DIAMOND),
                BlockBehaviour.Properties.of().forceSolidOn().instabreak());
        EMERALD_FLUID_PIPE = BLOCKS.registerBlock(
                "emerald_fluid_pipe",
                properties -> new FluidPipeBlock(properties, FluidPipeBlock.FluidPipeTier.EMERALD),
                BlockBehaviour.Properties.of().forceSolidOn().instabreak());
        NETHERITE_FLUID_PIPE = BLOCKS.registerBlock(
                "netherite_fluid_pipe",
                properties -> new FluidPipeBlock(properties, FluidPipeBlock.FluidPipeTier.NETHERITE),
                BlockBehaviour.Properties.of().forceSolidOn().instabreak());
        ITEM_PIPE = BLOCKS.registerBlock(
                "item_pipe",
                ItemPipeBlock::new,
                BlockBehaviour.Properties.of().forceSolidOn().instabreak());
        SMALL_ENERGY_CUBE = BLOCKS.registerBlock(
                "small_energy_cube",
                properties -> new EnergyCubeBlock(properties, EnergyCubeBlock.EnergyCubeTier.SMALL),
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(1.5f, 6.0f)
        );
        MEDIUM_ENERGY_CUBE = BLOCKS.registerBlock(
                "medium_energy_cube",
                properties -> new EnergyCubeBlock(properties, EnergyCubeBlock.EnergyCubeTier.MEDIUM),
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(1.5f, 6.0f)
        );
        LARGE_ENERGY_CUBE = BLOCKS.registerBlock(
                "large_energy_cube",
                properties -> new EnergyCubeBlock(properties, EnergyCubeBlock.EnergyCubeTier.LARGE),
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(1.5f, 6.0f)
        );
        MASSIVE_ENERGY_CUBE = BLOCKS.registerBlock(
                "massive_energy_cube",
                properties -> new EnergyCubeBlock(properties, EnergyCubeBlock.EnergyCubeTier.MASSIVE),
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(1.5f, 6.0f)
        );
        CREATIVE_ENERGY_CUBE = BLOCKS.registerBlock(
                "creative_energy_cube",
                properties -> new EnergyCubeBlock(properties, EnergyCubeBlock.EnergyCubeTier.CREATIVE),
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(1.5f, 6.0f)
        );
        SMALL_FLUID_TANK = BLOCKS.registerBlock(
                "small_fluid_tank",
                properties -> new FluidTankBlock(FluidTankBlock.TankTier.SMALL, properties),
                BlockBehaviour.Properties.of().forceSolidOn().strength(1.0f, 6.0f));
        MEDIUM_FLUID_TANK = BLOCKS.registerBlock(
                "medium_fluid_tank",
                properties -> new FluidTankBlock(FluidTankBlock.TankTier.MEDIUM, properties),
                BlockBehaviour.Properties.of().forceSolidOn().strength(1.0f, 6.0f));
        LARGE_FLUID_TANK = BLOCKS.registerBlock(
                "large_fluid_tank",
                properties -> new FluidTankBlock(FluidTankBlock.TankTier.LARGE, properties),
                BlockBehaviour.Properties.of().forceSolidOn().strength(1.0f, 6.0f));
        MASSIVE_FLUID_TANK = BLOCKS.registerBlock(
                "massive_fluid_tank",
                properties -> new FluidTankBlock(FluidTankBlock.TankTier.MASSIVE, properties),
                BlockBehaviour.Properties.of().forceSolidOn().strength(1.0f, 6.0f));
        CREATIVE_FLUID_TANK = BLOCKS.registerBlock(
                "creative_fluid_tank",
                properties -> new FluidTankBlock(FluidTankBlock.TankTier.CREATIVE, properties),
                BlockBehaviour.Properties.of().forceSolidOn().strength(1.0f, 6.0f));
        HARVESTER = BLOCKS.registerBlock(
                "harvester",
                HarvesterBlock::new,
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(1.5f, 6.0f));
        BLOCK_BREAKER = BLOCKS.registerBlock(
                "block_breaker",
                BlockBreakerBlock::new,
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(1.5f, 6.0f));
        BLOCK_PLACER = BLOCKS.registerBlock(
                "block_placer",
                BlockPlacerBlock::new,
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(1.5f, 6.0f));
        SOLAR_GENERATOR = BLOCKS.registerBlock(
                "solar_generator",
                SolarGeneratorBlock::new,
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(1.5f, 6.0f));
        COMBUSTION_GENERATOR = BLOCKS.registerBlock(
                "combustion_generator",
                CombustionGeneratorBlock::new,
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(1.5f, 6.0f));
        HEAT_GENERATOR = BLOCKS.registerBlock(
                "heat_generator",
                HeatGeneratorBlock::new,
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(2.0f, 6.0f));
        MINI_REACTOR = BLOCKS.registerBlock(
                "mini_reactor",
                MiniReactorBlock::new,
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(1.5f, 6.0f));
        TEMPORAL_ACCELERATOR = BLOCKS.registerBlock(
                "temporal_accelerator",
                TemporalAcceleratorBlock::new,
                BlockBehaviour.Properties.of().forceSolidOn().requiresCorrectToolForDrops().strength(1.5f, 6.0f));
        WIRELESS_ENERGY_TRANSMITTER = BLOCKS.registerBlock(
                "wireless_energy_transmitter",
                WirelessEnergyTransmitterBlock::new,
                BlockBehaviour.Properties.of().forceSolidOn().strength(1.0f, 6.0f));
        SPATIAL_CRATE = BLOCKS.registerBlock(
                "spatial_crate",
                SpatialCrateBlock::new,
                BlockBehaviour.Properties.of().forceSolidOn().strength(1.0f, 6.0f));
        UPGRADE_TABLE = BLOCKS.registerBlock(
                "upgrade_table",
                UpgradeTableBlock::new,
                BlockBehaviour.Properties.of().forceSolidOn().strength(1.5f, 6.0f));
        POWERED_FURNACE = BLOCKS.registerBlock(
                "powered_furnace",
                PoweredFurnaceBlock::new,
                BlockBehaviour.Properties.of().forceSolidOn().strength(1.5f, 6.0f));
        CRUSHER = BLOCKS.registerBlock(
                "crusher",
                CrusherBlock::new,
                BlockBehaviour.Properties.of().forceSolidOn().strength(1.5f, 6.0f));
        WIRELESS_PLAYER_CHARGER = BLOCKS.registerBlock(
                "wireless_player_charger",
                WirelessPlayerChargerBlock::new,
                BlockBehaviour.Properties.of().forceSolidOn().strength(1.5f, 6.0f));
        FOOD_MASHER = BLOCKS.registerBlock(
                "food_masher",
                FoodMasherBlock::new,
                BlockBehaviour.Properties.of().forceSolidOn().strength(1.5f, 6.0f));
        CHUNK_LOADER = BLOCKS.registerBlock(
                "chunk_loader",
                ChunkLoaderBlock::new,
                BlockBehaviour.Properties.of().requiresCorrectToolForDrops().forceSolidOn().strength(1.5f, 6.0f));
        ARGENT_PLASMA = BLOCKS.registerBlock(
                "argent_plasma",
                properties -> new LiquidBlock(FluidRegistry.ARGENT_PLASMA.get(), properties),
                BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).replaceable().noCollission().strength(100.0F).pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY));
        BLOCKS.register(modEventBus);
    }
}
