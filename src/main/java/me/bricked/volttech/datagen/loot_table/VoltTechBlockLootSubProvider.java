package me.bricked.volttech.datagen.loot_table;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.register.BlockRegistry;
import me.bricked.volttech.register.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class VoltTechBlockLootSubProvider extends BlockLootSubProvider {
    protected VoltTechBlockLootSubProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        this.dropSelf(BlockRegistry.SMALL_ENERGY_CUBE.get());
        this.dropSelf(BlockRegistry.MEDIUM_ENERGY_CUBE.get());
        this.dropSelf(BlockRegistry.LARGE_ENERGY_CUBE.get());
        this.dropSelf(BlockRegistry.MASSIVE_ENERGY_CUBE.get());
        this.dropSelf(BlockRegistry.CREATIVE_ENERGY_CUBE.get());
        this.dropSelf(BlockRegistry.MINI_REACTOR.get());
        this.dropSelf(BlockRegistry.SPATIAL_CRATE.get());
        this.dropSelf(BlockRegistry.TEMPORAL_ACCELERATOR.get());
        this.dropSelf(BlockRegistry.WIRELESS_ENERGY_TRANSMITTER.get());
        this.dropSelf(BlockRegistry.HARVESTER.get());
        this.dropSelf(BlockRegistry.SOLAR_GENERATOR.get());
        this.dropSelf(BlockRegistry.UPGRADE_TABLE.get());
        this.dropSelf(BlockRegistry.COMBUSTION_GENERATOR.get());
        this.dropSelf(BlockRegistry.HEAT_GENERATOR.get());
        this.dropSelf(BlockRegistry.POWERED_FURNACE.get());
        this.dropSelf(BlockRegistry.CRUSHER.get());
        this.dropSelf(BlockRegistry.FOOD_MASHER.get());
        this.dropSelf(BlockRegistry.BLOCK_BREAKER.get());
        this.dropSelf(BlockRegistry.BLOCK_PLACER.get());
        this.dropSelf(BlockRegistry.CHUNK_LOADER.get());

        this.dropSelf(BlockRegistry.DALEKANIUM_BLOCK.get());
        this.dropSelf(BlockRegistry.RAW_DALEKANIUM_BLOCK.get());
        this.dropSelf(BlockRegistry.URANIUM_BLOCK.get());
        this.dropSelf(BlockRegistry.RAW_URANIUM_BLOCK.get());
        this.dropSelf(BlockRegistry.VELTRIUM_BLOCK.get());
        this.dropSelf(BlockRegistry.RAW_VELTRIUM_BLOCK.get());
        this.dropSelf(BlockRegistry.TREXALITE_BLOCK.get());
        this.dropSelf(BlockRegistry.RAW_TREXALITE_BLOCK.get());
        this.add(BlockRegistry.URANIUM_ORE.get(), this.createOreDrop(BlockRegistry.URANIUM_ORE.get(), ItemRegistry.RAW_URANIUM.get()));
        this.add(BlockRegistry.DEEPSLATE_URANIUM_ORE.get(), this.createOreDrop(BlockRegistry.DEEPSLATE_URANIUM_ORE.get(), ItemRegistry.RAW_URANIUM.get()));
        this.add(BlockRegistry.DALEKANIUM_ORE.get(), this.createOreDrop(BlockRegistry.DALEKANIUM_ORE.get(), ItemRegistry.RAW_DALEKANIUM.get()));
        this.add(BlockRegistry.HELLISH_ROCK.get(), this.createOreDrop(BlockRegistry.HELLISH_ROCK.get(), ItemRegistry.HELLFORGED_SHARD.get()));
        this.add(BlockRegistry.VELTRIUM_ORE.get(), this.createOreDrop(BlockRegistry.VELTRIUM_ORE.get(), ItemRegistry.RAW_VELTRIUM.get()));
        this.add(BlockRegistry.DEEPSLATE_TREXALITE_ORE.get(), this.createOreDrop(BlockRegistry.DEEPSLATE_TREXALITE_ORE.get(), ItemRegistry.RAW_TREXALITE.get()));

        this.dropSelf(BlockRegistry.COPPER_ENERGY_CABLE.get());
        this.dropSelf(BlockRegistry.IRON_ENERGY_CABLE.get());
        this.dropSelf(BlockRegistry.GOLD_ENERGY_CABLE.get());
        this.dropSelf(BlockRegistry.DIAMOND_ENERGY_CABLE.get());
        this.dropSelf(BlockRegistry.EMERALD_ENERGY_CABLE.get());
        this.dropSelf(BlockRegistry.NETHERITE_ENERGY_CABLE.get());
        
        this.dropSelf(BlockRegistry.COPPER_FLUID_PIPE.get());
        this.dropSelf(BlockRegistry.IRON_FLUID_PIPE.get());
        this.dropSelf(BlockRegistry.GOLD_FLUID_PIPE.get());
        this.dropSelf(BlockRegistry.DIAMOND_FLUID_PIPE.get());
        this.dropSelf(BlockRegistry.EMERALD_FLUID_PIPE.get());
        this.dropSelf(BlockRegistry.NETHERITE_FLUID_PIPE.get());

        this.dropSelf(BlockRegistry.SMALL_FLUID_TANK.get());
        this.dropSelf(BlockRegistry.MEDIUM_FLUID_TANK.get());
        this.dropSelf(BlockRegistry.LARGE_FLUID_TANK.get());
        this.dropSelf(BlockRegistry.MASSIVE_FLUID_TANK.get());
        this.dropSelf(BlockRegistry.CREATIVE_FLUID_TANK.get());

        this.dropSelf(BlockRegistry.ITEM_PIPE.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.stream().
                filter(block ->
                        BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(VoltTech.MODID) &&
                        block != BlockRegistry.WIRELESS_PLAYER_CHARGER.get()).
                toList();
    }
}
