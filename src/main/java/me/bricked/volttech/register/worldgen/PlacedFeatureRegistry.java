package me.bricked.volttech.register.worldgen;

import me.bricked.volttech.VoltTech;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class PlacedFeatureRegistry {

    public static final ResourceKey<PlacedFeature> URANIUM_ORE_PLACED_FEATURE = registerKey("ore_uranium_placed");
    public static final ResourceKey<PlacedFeature> DALEKANIUM_ORE_PLACED_FEATURE = registerKey("ore_dalekanium_placed");
    public static final ResourceKey<PlacedFeature> HELLISH_ROCK_PLACED_FEATURE = registerKey("ore_hellish_placed");
    public static final ResourceKey<PlacedFeature> VELTRIUM_ORE_PLACED_FEATURE = registerKey("ore_veltrium_placed");
    public static final ResourceKey<PlacedFeature> TREXALITE_ORE_PLACED_FEATURE = registerKey("ore_trexalite_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        register(
                context,
                URANIUM_ORE_PLACED_FEATURE,
                configuredFeatures.getOrThrow(ConfiguredFeatureRegistry.OVERWORLD_URANIUM_ORE_CONFIGURED_FEATURE),
                commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(50), VerticalAnchor.belowTop(0)))
        );
        register(
                context,
                DALEKANIUM_ORE_PLACED_FEATURE,
                configuredFeatures.getOrThrow(ConfiguredFeatureRegistry.NETHER_DALEKANIUM_ORE_CONFIGURED_FEATURE),
                commonOrePlacement(12, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.belowTop(0)))
        );
        register(
                context,
                HELLISH_ROCK_PLACED_FEATURE,
                configuredFeatures.getOrThrow(ConfiguredFeatureRegistry.NETHER_HELLISH_ROCK_CONFIGURED_FEATURE),
                commonOrePlacement(8, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(64)))
        );
        register(
                context,
                VELTRIUM_ORE_PLACED_FEATURE,
                configuredFeatures.getOrThrow(ConfiguredFeatureRegistry.END_VELTRIUM_ORE_CONFIGURED_FEATURE),
                commonOrePlacement(5, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(40), VerticalAnchor.aboveBottom(90)))
        );
        register(
                context,
                TREXALITE_ORE_PLACED_FEATURE,
                configuredFeatures.getOrThrow(ConfiguredFeatureRegistry.OVERWORLD_TREXALITE_ORE_CONFIGURED_FEATURE),
                commonOrePlacement(7, HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(0), VerticalAnchor.aboveBottom(50)))
        );
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, VoltTech.resourceLocation(name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> config, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(config, List.copyOf(modifiers)));
    }

    public static List<PlacementModifier> orePlacement(PlacementModifier pCountPlacement, PlacementModifier pHeightRange) {
        return List.of(pCountPlacement, InSquarePlacement.spread(), pHeightRange, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(int pCount, PlacementModifier pHeightRange) {
        return orePlacement(CountPlacement.of(pCount), pHeightRange);
    }

    public static List<PlacementModifier> rareOrePlacement(int pChance, PlacementModifier pHeightRange) {
        return orePlacement(RarityFilter.onAverageOnceEvery(pChance), pHeightRange);
    }
}
