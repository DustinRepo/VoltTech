package me.bricked.volttech.datagen.loot_table;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class VoltTechLootTableProvider extends LootTableProvider {
    public VoltTechLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(
                        new SubProviderEntry(VoltTechBlockLootSubProvider::new, LootContextParamSets.BLOCK),
                        new SubProviderEntry(VoltTechChestLootSubProvider::new, LootContextParamSets.CHEST)
                ),
                registries);
    }
}
