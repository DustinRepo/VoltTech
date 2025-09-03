package me.bricked.volttech.register.worldgen;

import me.bricked.volttech.VoltTech;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class BiomeModifierRegistry {

    public static final ResourceKey<BiomeModifier> URANIUM_ORE_BIOME_MODIFIER = registerKey("ore_uranium_biome_mod");
    public static final ResourceKey<BiomeModifier> DALEKANIUM_ORE_BIOME_MODIFIER = registerKey("ore_dalekanium_biome_mod");
    public static final ResourceKey<BiomeModifier> HELLISH_ORE_BIOME_MODIFIER = registerKey("ore_hellish_biome_mod");
    public static final ResourceKey<BiomeModifier> VELTRIUM_ORE_BIOME_MODIFIER = registerKey("ore_veltrium_biome_mod");
    public static final ResourceKey<BiomeModifier> TREXALITE_ORE_BIOME_MODIFIER = registerKey("ore_trexalite_biome_mod");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        context.register(URANIUM_ORE_BIOME_MODIFIER, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(PlacedFeatureRegistry.URANIUM_ORE_PLACED_FEATURE)),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));

        context.register(DALEKANIUM_ORE_BIOME_MODIFIER, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER),
                HolderSet.direct(placedFeatures.getOrThrow(PlacedFeatureRegistry.DALEKANIUM_ORE_PLACED_FEATURE)),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));

        context.register(HELLISH_ORE_BIOME_MODIFIER, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER),
                HolderSet.direct(placedFeatures.getOrThrow(PlacedFeatureRegistry.HELLISH_ROCK_PLACED_FEATURE)),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));

        context.register(VELTRIUM_ORE_BIOME_MODIFIER, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_END),
                HolderSet.direct(placedFeatures.getOrThrow(PlacedFeatureRegistry.VELTRIUM_ORE_PLACED_FEATURE)),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));

        context.register(TREXALITE_ORE_BIOME_MODIFIER, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(PlacedFeatureRegistry.TREXALITE_ORE_PLACED_FEATURE)),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));
    }

    public static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, VoltTech.resourceLocation(name));
    }
}
