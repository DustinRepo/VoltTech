package me.bricked.volttech.datagen.model;

import me.bricked.volttech.VoltTech;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class VoltTechModelProvider extends ModelProvider {
    private final VoltTechCustomModelProvider customModelProvider;
    private final VoltTechBlockstateProvider blockstateProvider;
    private final VoltTechItemModelProvider itemModelProvider;
    public VoltTechModelProvider(PackOutput output) {
        super(output, VoltTech.MODID);
        blockstateProvider = new VoltTechBlockstateProvider();
        itemModelProvider = new VoltTechItemModelProvider();
        customModelProvider = new VoltTechCustomModelProvider(output);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModelProvider.registerModels(itemModels);
        blockstateProvider.registerModels(blockModels);
        customModelProvider.registerModels(itemModels);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        CompletableFuture<?> first = super.run(output);
        return CompletableFuture.allOf(
                first,
                customModelProvider.run(output)
        );
    }
}
