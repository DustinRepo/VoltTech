package me.bricked.volttech.datagen.tag;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.register.BlockRegistry;
import me.bricked.volttech.register.TagRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class VoltTechBlockTagProvider extends BlockTagsProvider {
    public VoltTechBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, VoltTech.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(TagRegistry.Blocks.CABLE_COVERS_BANNED).
                add(
                        BlockRegistry.COPPER_ENERGY_CABLE.get(),
                        BlockRegistry.IRON_ENERGY_CABLE.get(),
                        BlockRegistry.GOLD_ENERGY_CABLE.get(),
                        BlockRegistry.DIAMOND_ENERGY_CABLE.get(),
                        BlockRegistry.EMERALD_ENERGY_CABLE.get(),
                        BlockRegistry.NETHERITE_ENERGY_CABLE.get(),
                        BlockRegistry.COPPER_FLUID_PIPE.get(),
                        BlockRegistry.IRON_FLUID_PIPE.get(),
                        BlockRegistry.GOLD_FLUID_PIPE.get(),
                        BlockRegistry.DIAMOND_FLUID_PIPE.get(),
                        BlockRegistry.EMERALD_FLUID_PIPE.get(),
                        BlockRegistry.NETHERITE_FLUID_PIPE.get(),
                        BlockRegistry.ITEM_PIPE.get(),
                        BlockRegistry.WIRELESS_ENERGY_TRANSMITTER.get(),
                        BlockRegistry.WIRELESS_PLAYER_CHARGER.get()
                );
        this.tag(TagRegistry.Blocks.TEMPORAL_ACCELERATOR_BANNED).
                add(
                        BlockRegistry.TEMPORAL_ACCELERATOR.get(),
                        BlockRegistry.HARVESTER.get()
                );
        this.tag(TagRegistry.Blocks.PHASE_BANNED).
                add(
                        BlockRegistry.VELTRIUM_BLOCK.get(),
                        BlockRegistry.RAW_VELTRIUM_BLOCK.get()
                );
        this.tag(TagRegistry.Blocks.BLOCK_BREAKER_BANNED).
                add(
                        Blocks.BEDROCK
                );
        this.tag(Tags.Blocks.ORES)
                .add(
                        BlockRegistry.URANIUM_ORE.get(),
                        BlockRegistry.DEEPSLATE_URANIUM_ORE.get(),
                        BlockRegistry.HELLISH_ROCK.get(),
                        BlockRegistry.DALEKANIUM_ORE.get(),
                        BlockRegistry.VELTRIUM_ORE.get(),
                        BlockRegistry.DEEPSLATE_TREXALITE_ORE.get()
                );
        this.tag(TagRegistry.Blocks.ORES_URANIUM)
                .add(
                        BlockRegistry.URANIUM_ORE.get(),
                        BlockRegistry.DEEPSLATE_URANIUM_ORE.get()
                );
        this.tag(TagRegistry.Blocks.ORES_VELTRIUM)
                .add(
                        BlockRegistry.VELTRIUM_ORE.get()
                );
        this.tag(TagRegistry.Blocks.ORES_TREXALITE)
                .add(
                        BlockRegistry.DEEPSLATE_TREXALITE_ORE.get()
                );
        this.tag(TagRegistry.Blocks.ORES_BRONZE)
                .add(
                        BlockRegistry.DALEKANIUM_ORE.get()
                );
        this.tag(TagRegistry.Blocks.ORES_DALEKANIUM)
                .add(
                        BlockRegistry.DALEKANIUM_ORE.get()
                );
        this.tag(TagRegistry.Blocks.ORES_HELLISH)
                .add(
                        BlockRegistry.HELLISH_ROCK.get()
                );
        this.tag(Tags.Blocks.ORES_IN_GROUND_NETHERRACK).
                add(
                        BlockRegistry.DALEKANIUM_ORE.get(),
                        BlockRegistry.HELLISH_ROCK.get()
                );
        this.tag(Tags.Blocks.ORES_IN_GROUND_STONE).
                add(
                        BlockRegistry.URANIUM_ORE.get()
                );
        this.tag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE).
                add(
                        BlockRegistry.DEEPSLATE_URANIUM_ORE.get(),
                        BlockRegistry.DEEPSLATE_TREXALITE_ORE.get()
                );
        this.tag(TagRegistry.Blocks.ORES_IN_GROUND_END_STONE).
                add(
                        BlockRegistry.VELTRIUM_ORE.get()
                );

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).
                add(
                        BlockRegistry.DALEKANIUM_ORE.get(),
                        BlockRegistry.DALEKANIUM_BLOCK.get(),
                        BlockRegistry.RAW_DALEKANIUM_BLOCK.get(),
                        BlockRegistry.TEMPORAL_ACCELERATOR.get(),
                        BlockRegistry.DEEPSLATE_TREXALITE_ORE.get(),
                        BlockRegistry.DEEPSLATE_URANIUM_ORE.get(),
                        BlockRegistry.HELLISH_ROCK.get(),
                        BlockRegistry.URANIUM_ORE.get(),
                        BlockRegistry.URANIUM_BLOCK.get(),
                        BlockRegistry.RAW_URANIUM_BLOCK.get(),
                        BlockRegistry.SPATIAL_CRATE.get(),
                        BlockRegistry.SOLAR_GENERATOR.get(),
                        BlockRegistry.MINI_REACTOR.get(),
                        BlockRegistry.VELTRIUM_ORE.get(),
                        BlockRegistry.VELTRIUM_BLOCK.get(),
                        BlockRegistry.RAW_VELTRIUM_BLOCK.get(),
                        BlockRegistry.SMALL_ENERGY_CUBE.get(),
                        BlockRegistry.MEDIUM_ENERGY_CUBE.get(),
                        BlockRegistry.LARGE_ENERGY_CUBE.get(),
                        BlockRegistry.MASSIVE_ENERGY_CUBE.get(),
                        BlockRegistry.WIRELESS_ENERGY_TRANSMITTER.get(),
                        BlockRegistry.CREATIVE_ENERGY_CUBE.get(),
                        BlockRegistry.COMBUSTION_GENERATOR.get(),
                        BlockRegistry.HEAT_GENERATOR.get(),
                        BlockRegistry.HARVESTER.get(),
                        BlockRegistry.POWERED_FURNACE.get(),
                        BlockRegistry.CRUSHER.get(),
                        BlockRegistry.FOOD_MASHER.get(),
                        BlockRegistry.BLOCK_BREAKER.get(),
                        BlockRegistry.BLOCK_PLACER.get(),
                        BlockRegistry.CHUNK_LOADER.get()
                );
        this.tag(BlockTags.NEEDS_STONE_TOOL).
                add(
                        BlockRegistry.SPATIAL_CRATE.get(),
                        BlockRegistry.SOLAR_GENERATOR.get(),
                        BlockRegistry.MINI_REACTOR.get(),
                        BlockRegistry.SMALL_ENERGY_CUBE.get(),
                        BlockRegistry.MEDIUM_ENERGY_CUBE.get(),
                        BlockRegistry.LARGE_ENERGY_CUBE.get(),
                        BlockRegistry.MASSIVE_ENERGY_CUBE.get(),
                        BlockRegistry.CREATIVE_ENERGY_CUBE.get(),
                        BlockRegistry.COMBUSTION_GENERATOR.get(),
                        BlockRegistry.HEAT_GENERATOR.get(),
                        BlockRegistry.UPGRADE_TABLE.get(),
                        BlockRegistry.WIRELESS_ENERGY_TRANSMITTER.get(),
                        BlockRegistry.HARVESTER.get(),
                        BlockRegistry.POWERED_FURNACE.get(),
                        BlockRegistry.CRUSHER.get(),
                        BlockRegistry.FOOD_MASHER.get(),
                        BlockRegistry.BLOCK_BREAKER.get(),
                        BlockRegistry.BLOCK_PLACER.get(),
                        BlockRegistry.CHUNK_LOADER.get()
                );
        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(
                        BlockRegistry.URANIUM_BLOCK.get(),
                        BlockRegistry.URANIUM_ORE.get(),
                        BlockRegistry.DEEPSLATE_URANIUM_ORE.get(),
                        BlockRegistry.RAW_URANIUM_BLOCK.get(),
                        BlockRegistry.HELLISH_ROCK.get()
                );
        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(
                        BlockRegistry.DALEKANIUM_BLOCK.get(),
                        BlockRegistry.DALEKANIUM_ORE.get(),
                        BlockRegistry.RAW_DALEKANIUM_BLOCK.get(),
                        BlockRegistry.VELTRIUM_ORE.get(),
                        BlockRegistry.VELTRIUM_BLOCK.get(),
                        BlockRegistry.RAW_VELTRIUM_BLOCK.get(),
                        BlockRegistry.DEEPSLATE_TREXALITE_ORE.get()
                );
        this.tag(Tags.Blocks.PLAYER_WORKSTATIONS_FURNACES).
                add(
                        BlockRegistry.POWERED_FURNACE.get()
                );
    }
}
