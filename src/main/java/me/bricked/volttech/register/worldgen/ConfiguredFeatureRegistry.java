package me.bricked.volttech.register.worldgen;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.register.BlockRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ConfiguredFeatureRegistry {

    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_URANIUM_ORE_CONFIGURED_FEATURE = registerKey("ore_uranium");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_TREXALITE_ORE_CONFIGURED_FEATURE = registerKey("ore_trexalite");
    public static final ResourceKey<ConfiguredFeature<?, ?>> NETHER_DALEKANIUM_ORE_CONFIGURED_FEATURE = registerKey("ore_dalekanium");
    public static final ResourceKey<ConfiguredFeature<?, ?>> NETHER_HELLISH_ROCK_CONFIGURED_FEATURE = registerKey("ore_hellish");
    public static final ResourceKey<ConfiguredFeature<?, ?>> END_VELTRIUM_ORE_CONFIGURED_FEATURE = registerKey("ore_veltrium");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest netherrackReplaceables = new BlockMatchTest(Blocks.NETHERRACK);
        RuleTest endstoneReplaceables = new BlockMatchTest(Blocks.END_STONE);

        List<OreConfiguration.TargetBlockState> overworldUraniumOres = List.of(
                OreConfiguration.target(stoneReplaceables, BlockRegistry.URANIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, BlockRegistry.DEEPSLATE_URANIUM_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> overworldTrexaliteOres = List.of(
                OreConfiguration.target(deepslateReplaceables, BlockRegistry.DEEPSLATE_TREXALITE_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> netherDalekaniumOres = List.of(
                OreConfiguration.target(netherrackReplaceables, BlockRegistry.DALEKANIUM_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> netherHellishRock = List.of(
                OreConfiguration.target(netherrackReplaceables, BlockRegistry.HELLISH_ROCK.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> endVeltriumOres = List.of(
                OreConfiguration.target(endstoneReplaceables, BlockRegistry.VELTRIUM_ORE.get().defaultBlockState())
        );

        register(context, OVERWORLD_URANIUM_ORE_CONFIGURED_FEATURE, Feature.ORE, new OreConfiguration(overworldUraniumOres, 6));
        register(context, NETHER_DALEKANIUM_ORE_CONFIGURED_FEATURE, Feature.ORE, new OreConfiguration(netherDalekaniumOres, 4));
        register(context, NETHER_HELLISH_ROCK_CONFIGURED_FEATURE, Feature.ORE, new OreConfiguration(netherHellishRock, 4));
        register(context, END_VELTRIUM_ORE_CONFIGURED_FEATURE, Feature.ORE, new OreConfiguration(endVeltriumOres, 4));
        register(context, OVERWORLD_TREXALITE_ORE_CONFIGURED_FEATURE, Feature.ORE, new OreConfiguration(overworldTrexaliteOres, 4));
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, VoltTech.resourceLocation(name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
        context.register(key, new ConfiguredFeature<>(feature, config));
    }

}
