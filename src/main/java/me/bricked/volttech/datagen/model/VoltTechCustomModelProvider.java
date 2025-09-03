package me.bricked.volttech.datagen.model;

import com.google.gson.JsonObject;
import me.bricked.volttech.VoltTech;
import me.bricked.volttech.register.BlockRegistry;
import me.bricked.volttech.register.ItemRegistry;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.Direction;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class VoltTechCustomModelProvider {
    private final PackOutput.PathProvider itemInfoPathProvider;
    private ItemModelGenerators itemModels;
    public VoltTechCustomModelProvider(PackOutput packOutput) {
        this.itemInfoPathProvider = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");
    }
    private ResourceLocation CHUNK_LOADER_CORE_MODEL_LOCATION;
    private ResourceLocation MULTIVERSE_PORTAL_MODEL_LOCATION;

    public void registerModels(ItemModelGenerators itemModelGenerators) {
        // Creates /assets/volttech/models/item/<model>.json
        this.itemModels = itemModelGenerators;
        CHUNK_LOADER_CORE_MODEL_LOCATION = chunkLoaderCoreModel();
        MULTIVERSE_PORTAL_MODEL_LOCATION = multiversePortalModel();
    }

    public CompletableFuture<?> run(CachedOutput output) {
        // Creates /assets/volttech/items/<model>.json
        return CompletableFuture.allOf(
                itemInfoPointsToModel(VoltTech.resourceLocation("chunk_loader_core"), CHUNK_LOADER_CORE_MODEL_LOCATION, output),
                itemInfoPointsToModel(VoltTech.resourceLocation("multiverse_portal"), MULTIVERSE_PORTAL_MODEL_LOCATION, output)
        );
    }

    private CompletableFuture<?> itemInfoPointsToModel(ResourceLocation itemJsonName, ResourceLocation modelLocation, CachedOutput output) {
        Path target = this.itemInfoPathProvider.json(itemJsonName);
        JsonObject json = new JsonObject();
        JsonObject model = new JsonObject();
        model.addProperty("type", "minecraft:model");
        model.addProperty("model", modelLocation.toString());
        json.add("model", model);
        return DataProvider.saveStable(output, json, target);
    }

    private ResourceLocation multiversePortalModel() {
        ResourceLocation textureLocation = VoltTech.resourceLocation("item/multiverse_portal");
        return new ModelTemplate(
                Optional.empty(),
                Optional.empty(),
                TextureSlot.SIDE,
                TextureSlot.PARTICLE
        ).extend().renderType("cutout").element(elementBuilder -> {
            elementBuilder.
                    from(0, 0, 0).
                    to(16, 32, 0).
                    face(Direction.NORTH, f -> f.texture(TextureSlot.SIDE).uvs(0, 0, 16, 16)).
                    face(Direction.SOUTH, f -> f.texture(TextureSlot.SIDE).uvs(0, 0, 16, 16));
        }).build().create(
                textureLocation,
                new TextureMapping().
                        put(TextureSlot.SIDE, textureLocation).
                        put(TextureSlot.PARTICLE, textureLocation),
                itemModels.modelOutput
        );
    }

    private ResourceLocation chunkLoaderCoreModel() {
        TextureSlot MAIN = TextureSlot.create("main");
        TextureSlot DESIGN = TextureSlot.create("design");
        Block block = BlockRegistry.CHUNK_LOADER.get();
        return new ModelTemplate(
                Optional.of(
                        ResourceLocation.parse("block/cube")
                ),
                Optional.of("_core"),
                MAIN,
                DESIGN,
                TextureSlot.PARTICLE
        ).extend().renderType("translucent").element(elementBuilder -> {
                    elementBuilder.
                            from(2, 2, 2).
                            to(14, 14, 14).
                            allFaces((direction, f) -> f.texture(MAIN));
                }).element(elementBuilder -> {
                    // Top extension
                    elementBuilder
                            .from(4, 14, 4)
                            .to(12, 16, 12)
                            .allFacesExcept((direction, f) -> f.texture(DESIGN), Set.of(Direction.DOWN));
                })
                .element(elementBuilder -> {
                    // Bottom extension
                    elementBuilder
                            .from(4, 0, 4)
                            .to(12, 2, 12)
                            .allFacesExcept((direction, f) -> f.texture(DESIGN), Set.of(Direction.UP));
                })
                .element(elementBuilder -> {
                    // North extension
                    elementBuilder
                            .from(4, 4, 0)
                            .to(12, 12, 2)
                            .allFacesExcept((direction, f) -> f.texture(DESIGN), Set.of(Direction.SOUTH));
                })
                .element(elementBuilder -> {
                    // South extension
                    elementBuilder
                            .from(4, 4, 14)
                            .to(12, 12, 16)
                            .allFacesExcept((direction, f) -> f.texture(DESIGN), Set.of(Direction.NORTH));
                })
                .element(elementBuilder -> {
                    // West extension
                    elementBuilder
                            .from(0, 4, 4)
                            .to(2, 12, 12)
                            .allFacesExcept((direction, f) -> f.texture(DESIGN), Set.of(Direction.EAST));
                })
                .element(elementBuilder -> {
                    // East extension
                    elementBuilder
                            .from(14, 4, 4)
                            .to(16, 12, 12)
                            .allFacesExcept((direction, f) -> f.texture(DESIGN), Set.of(Direction.WEST));
                })
                .build().create(
                        ItemRegistry.CHUNK_LOADER_BLOCK_ITEM.get(),
                        new TextureMapping().
                                put(MAIN, TextureMapping.getBlockTexture(block, "_core")).
                                put(DESIGN, TextureMapping.getBlockTexture(block, "_core_design")).
                                put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_core")),
                        itemModels.modelOutput
                );
    }
}
