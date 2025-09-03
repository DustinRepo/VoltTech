package me.bricked.volttech.datagen;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.register.worldgen.BiomeModifierRegistry;
import me.bricked.volttech.register.worldgen.ConfiguredFeatureRegistry;
import me.bricked.volttech.register.worldgen.PlacedFeatureRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class VoltTechDatapackProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder().
            add(Registries.CONFIGURED_FEATURE, ConfiguredFeatureRegistry::bootstrap).
            add(Registries.PLACED_FEATURE, PlacedFeatureRegistry::bootstrap).
            add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, BiomeModifierRegistry::bootstrap);

    public VoltTechDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(VoltTech.MODID));
    }

}
