package me.bricked.volttech.register;

import me.bricked.volttech.VoltTech;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class TagRegistry {
    public static class Items {
        public static final TagKey<Item> EMPTY_TAG = TagKey.create(Registries.ITEM,
                VoltTech.resourceLocation("empty")
        );
        public static final TagKey<Item> URANIUM_INGOT_TAG = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath("c", "ingots/uranium")
        );
        public static final TagKey<Item> BRONZE_INGOT_TAG = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath("c", "ingots/bronze")
        );
        public static final TagKey<Item> HELLFORGED_INGOT_TAG = TagKey.create(Registries.ITEM,
                VoltTech.resourceLocation("ingots/hellforged")
        );
        public static final TagKey<Item> TREXALITE_GEM_TAG = TagKey.create(Registries.ITEM,
                VoltTech.resourceLocation("gems/trexalite")
        );
        public static final TagKey<Item> VELTRIUM_GEM_TAG = TagKey.create(Registries.ITEM,
                VoltTech.resourceLocation("gems/veltrium")
        );
        public static final TagKey<Item> BUCKETS_ARGENT_PLASMA_TAG = TagKey.create(Registries.ITEM,
                VoltTech.resourceLocation("buckets/argent_plasma")
        );
        public static final TagKey<Item> BATTERIES = TagKey.create(Registries.ITEM,
                VoltTech.resourceLocation("batteries")
        );
        public static final TagKey<Item> ARMORS_DALEKANIUM = TagKey.create(Registries.ITEM,
                VoltTech.resourceLocation("armors/dalekanium")
        );
        public static final TagKey<Item> ARMORS_HELLFORGED = TagKey.create(Registries.ITEM,
                VoltTech.resourceLocation("armors/hellforged")
        );
        public static final TagKey<Item> ARMORS_VELTRIUM = TagKey.create(Registries.ITEM,
                VoltTech.resourceLocation("armors/veltrium")
        );
        public static final TagKey<Item> ARMORS_TREXALITE = TagKey.create(Registries.ITEM,
                VoltTech.resourceLocation("armors/trexalite")
        );
        public static final TagKey<Item> TREXALITE_STORAGE_BLOCK = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/trexalite")
        );
        public static final TagKey<Item> RAW_TREXALITE_STORAGE_BLOCK = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/raw_trexalite")
        );
        public static final TagKey<Item> TREXALITE_RAW_MATERIAL = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath("c", "raw_materials/trexalite")
        );
        public static final TagKey<Item> URANIUM_STORAGE_BLOCK = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/uranium")
        );
        public static final TagKey<Item> RAW_URANIUM_STORAGE_BLOCK = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath("c", "storage_blocks/raw_uranium")
        );
        public static final TagKey<Item> URANIUM_RAW_MATERIAL = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath("c", "raw_materials/uranium")
        );
        public static final TagKey<Item> VELTRIUM_RAW_MATERIAL = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath("c", "raw_materials/uranium")
        );
        public static final TagKey<Item> UPGRADE_CARDS = TagKey.create(Registries.ITEM,
                VoltTech.resourceLocation("upgrade_cards")
        );
        public static final TagKey<Item> JETPACKS = TagKey.create(Registries.ITEM,
                VoltTech.resourceLocation("jetpacks")
        );
        public static final TagKey<Item> BLOCK_BREAKER_BANNED = TagKey.create(Registries.ITEM,
                VoltTech.resourceLocation("banned_block_breaker_tools")
        );
        public static final TagKey<Item> BLOCK_PLACER_BANNED = TagKey.create(Registries.ITEM,
                VoltTech.resourceLocation("banned_block_placer_blocks")
        );
    }
    public static class Blocks {
        public static final TagKey<Block> ORES_URANIUM = TagKey.create(Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath("c", "ores/uranium")
        );
        public static final TagKey<Block> ORES_BRONZE = TagKey.create(Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath("c", "ores/bronze")
        );
        public static final TagKey<Block> ORES_IN_GROUND_END_STONE = TagKey.create(Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath("c", "ores_in_ground/end_stone")
        );
        public static final TagKey<Block> ORES_HELLISH = TagKey.create(Registries.BLOCK,
                VoltTech.resourceLocation("ores/hellish")
        );
        public static final TagKey<Block> ORES_DALEKANIUM = TagKey.create(Registries.BLOCK,
                VoltTech.resourceLocation("ores/dalekanium")
        );
        public static final TagKey<Block> ORES_VELTRIUM = TagKey.create(Registries.BLOCK,
                VoltTech.resourceLocation("ores/veltrium")
        );
        public static final TagKey<Block> ORES_TREXALITE = TagKey.create(Registries.BLOCK,
                VoltTech.resourceLocation("ores/trexalite")
        );
        public static final TagKey<Block> PHASE_BANNED = TagKey.create(Registries.BLOCK,
                VoltTech.resourceLocation("banned_phase")
        );
        public static final TagKey<Block> BLOCK_BREAKER_BANNED = TagKey.create(Registries.BLOCK,
                VoltTech.resourceLocation("banned_block_breaker")
        );
        public static final TagKey<Block> TEMPORAL_ACCELERATOR_BANNED = TagKey.create(Registries.BLOCK,
                VoltTech.resourceLocation("banned_temporal_accelerator")
        );
        public static final TagKey<Block> CABLE_COVERS_BANNED = TagKey.create(Registries.BLOCK,
                VoltTech.resourceLocation("banned_cable_covers")
        );
    }

    public static class Fluids {
        public static final TagKey<Fluid> FLUID_TANK_BANNED = TagKey.create(Registries.FLUID,
                VoltTech.resourceLocation("banned_fluids_in_tank")
        );
    }
}
