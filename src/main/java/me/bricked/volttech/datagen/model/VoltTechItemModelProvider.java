package me.bricked.volttech.datagen.model;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.block.ChunkLoaderBlock;
import me.bricked.volttech.item.PortalGunItem;
import me.bricked.volttech.item.property.ChargedItemProperty;
import me.bricked.volttech.register.BlockRegistry;
import me.bricked.volttech.register.ItemRegistry;
import me.bricked.volttech.render.special.ChunkLoaderSpecialRenderer;
import me.bricked.volttech.render.special.FluidTankSpecialRenderer;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.SpecialModelWrapper;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Block;

import java.util.Optional;
import java.util.Set;

public class VoltTechItemModelProvider {
    private ItemModelGenerators itemModels;
    public VoltTechItemModelProvider() {
    }

    public void registerModels(ItemModelGenerators itemModels) {
        this.itemModels = itemModels;
        basicItem(ItemRegistry.EDIBLE_SLOP.get());
        basicItem(ItemRegistry.TORCH_DISPENSER.get());
        handheldItem(ItemRegistry.UTILITY_WRENCH.get());
        basicItem(ItemRegistry.ENERGY_UPGRADE_CARD.get());
        basicItem(ItemRegistry.SPEED_UPGRADE_CARD.get());
        basicItem(ItemRegistry.NIGHT_VISION_UPGRADE_CARD.get());
        basicItem(ItemRegistry.JUMP_BOOST_UPGRADE_CARD.get());
        basicItem(ItemRegistry.JETPACK_UPGRADE_CARD.get());
        basicItem(ItemRegistry.FEATHER_FALLING_UPGRADE_CARD.get());
        basicItem(ItemRegistry.HEALTH_UPGRADE_CARD.get());
        basicItem(ItemRegistry.WATER_BREATHING_UPGRADE_CARD.get());
        basicItem(ItemRegistry.AUTO_FEED_UPGRADE_CARD.get());
        basicItem(ItemRegistry.FIRE_RESIST_UPGRADE_CARD.get());
        basicItem(ItemRegistry.WITHER_RESIST_UPGRADE_CARD.get());
        basicItem(ItemRegistry.POISON_RESIST_UPGRADE_CARD.get());
        basicItem(ItemRegistry.MAGNET_UPGRADE_CARD.get());
        basicItem(ItemRegistry.PHASE_UPGRADE_CARD.get());
        basicItem(ItemRegistry.REGENERATION_UPGRADE_CARD.get());
        basicItem(ItemRegistry.STEP_HEIGHT_UPGRADE_CARD.get());
        basicItem(ItemRegistry.REACH_UPGRADE_CARD.get());
        basicItem(ItemRegistry.MINING_SPEED_UPGRADE_CARD.get());
        basicItem(ItemRegistry.MINE_AREA_UPGRADE_CARD.get());
        basicItem(ItemRegistry.EVASION_UPGRADE_CARD.get());
        basicItem(ItemRegistry.SMELTER_UPGRADE_CARD.get());
        basicItem(ItemRegistry.DAMAGE_UPGRADE_CARD.get());
        basicItem(ItemRegistry.LOCATION_CARD_ITEM.get());
        basicItem(ItemRegistry.ELECTRIC_COMPONENT.get());
        basicItem(ItemRegistry.SOLAR_PANEL.get());
        basicItem(ItemRegistry.BOOSTER.get());
        basicItem(ItemRegistry.JETPACK.get());
        basicItem(ItemRegistry.RAW_URANIUM.get());
        basicItem(ItemRegistry.URANIUM_INGOT.get());
        basicItem(ItemRegistry.HELLFORGED_SHARD.get());
        basicItem(ItemRegistry.HELLFORGED_INGOT.get());
        basicItem(ItemRegistry.RAW_DALEKANIUM.get());
        basicItem(ItemRegistry.DALEKANIUM_INGOT.get());
        basicItem(ItemRegistry.CRUSHED_COPPER.get());
        basicItem(ItemRegistry.CRUSHED_GOLD.get());
        basicItem(ItemRegistry.CRUSHED_IRON.get());
        basicItem(ItemRegistry.CRUSHED_DIAMOND.get());
        basicItem(ItemRegistry.CRUSHED_EMERALD.get());
        basicItem(ItemRegistry.CRUSHED_REDSTONE.get());
        basicItem(ItemRegistry.CRUSHED_LAPIS.get());
        basicItem(ItemRegistry.CRUSHED_HELLFORGED.get());
        basicItem(ItemRegistry.CRUSHED_VELTRIUM.get());
        basicItem(ItemRegistry.CRUSHED_DALEKANIUM.get());
        basicItem(ItemRegistry.CRUSHED_URANIUM.get());
        basicItem(ItemRegistry.CRUSHED_TREXALITE.get());
        basicItem(ItemRegistry.RAW_TREXALITE.get());
        basicItem(ItemRegistry.TREXALITE.get());
        basicItem(ItemRegistry.RAW_VELTRIUM.get());
        basicItem(ItemRegistry.VELTRIUM.get());
        handheldItem(ItemRegistry.DALEKANIUM_PICKAXE.get());
        handheldItem(ItemRegistry.DALEKANIUM_AXE.get());
        handheldItem(ItemRegistry.DALEKANIUM_SHOVEL.get());
        handheldItem(ItemRegistry.DALEKANIUM_SWORD.get());
        handheldItem(ItemRegistry.DALEKANIUM_HOE.get());
        handheldItem(ItemRegistry.VELTRIUM_PICKAXE.get());
        handheldItem(ItemRegistry.VELTRIUM_AXE.get());
        handheldItem(ItemRegistry.VELTRIUM_SHOVEL.get());
        handheldItem(ItemRegistry.VELTRIUM_SWORD.get());
        handheldItem(ItemRegistry.VELTRIUM_HOE.get());
        handheldItem(ItemRegistry.TREXALITE_PICKAXE.get());
        handheldItem(ItemRegistry.TREXALITE_AXE.get());
        handheldItem(ItemRegistry.TREXALITE_SHOVEL.get());
        handheldItem(ItemRegistry.TREXALITE_SWORD.get());
        handheldItem(ItemRegistry.TREXALITE_HOE.get());
        createDemonicCrucible();
        basicItem(ItemRegistry.DALEKANIUM_HELMET.get());
        basicItem(ItemRegistry.DALEKANIUM_CHESTPLATE.get());
        basicItem(ItemRegistry.DALEKANIUM_LEGGINGS.get());
        basicItem(ItemRegistry.DALEKANIUM_BOOTS.get());
        basicItem(ItemRegistry.VELTRIUM_HELMET.get());
        basicItem(ItemRegistry.VELTRIUM_CHESTPLATE.get());
        basicItem(ItemRegistry.VELTRIUM_LEGGINGS.get());
        basicItem(ItemRegistry.VELTRIUM_BOOTS.get());
        basicItem(ItemRegistry.HELLFORGED_HELMET.get());
        basicItem(ItemRegistry.HELLFORGED_CHESTPLATE.get());
        basicItem(ItemRegistry.HELLFORGED_LEGGINGS.get());
        basicItem(ItemRegistry.HELLFORGED_BOOTS.get());
        basicItem(ItemRegistry.TREXALITE_HELMET.get());
        basicItem(ItemRegistry.TREXALITE_CHESTPLATE.get());
        basicItem(ItemRegistry.TREXALITE_LEGGINGS.get());
        basicItem(ItemRegistry.TREXALITE_BOOTS.get());
        basicItem(ItemRegistry.ARGENT_PLASMA_BUCKET.get());
        basicItem(ItemRegistry.MECHANIZED_ENDER_PEARL.get());
        basicItem(ItemRegistry.HOMING_DRIVE.get());
        basicItem(ItemRegistry.GRAVE_TRANSPORTER.get());
        basicItem(ItemRegistry.SMALL_BATTERY.get());
        basicItem(ItemRegistry.MEDIUM_BATTERY.get());
        basicItem(ItemRegistry.LARGE_BATTERY.get());
        basicItem(ItemRegistry.MASSIVE_BATTERY.get());
        basicItem(ItemRegistry.CREATIVE_BATTERY.get());
        basicItem(ItemRegistry.ATOMIC_BATTERY.get());
        createPortalGun();

        ResourceLocation infiniteModelLocation = new ModelTemplate(
                ModelTemplates.FLAT_ITEM.model,
                Optional.empty(),
                TextureSlot.LAYER0
        ).create(
                ItemRegistry.INFINITE_TORCH_DISPENSER.get(),
                new TextureMapping().
                        put(TextureSlot.LAYER0, VoltTech.resourceLocation("item/torch_dispenser")),
            itemModels.modelOutput
        );
        itemModels.itemModelOutput.accept(
                ItemRegistry.INFINITE_TORCH_DISPENSER.get(),
                ItemModelUtils.plainModel(infiniteModelLocation)
        );

        ResourceLocation chunkLoaderLocation = BuiltInRegistries.ITEM.getKey(ItemRegistry.CHUNK_LOADER_BLOCK_ITEM.get()).withSuffix("_frame");
        specialRendererItem(ItemRegistry.CHUNK_LOADER_BLOCK_ITEM.get(), chunkLoaderLocation, new ChunkLoaderSpecialRenderer.Unbaked());

        // Register special renderers for Fluid Tanks
        fluidTankItem(ItemRegistry.SMALL_FLUID_TANK_BLOCK_ITEM.get());
        fluidTankItem(ItemRegistry.MEDIUM_FLUID_TANK_BLOCK_ITEM.get());
        fluidTankItem(ItemRegistry.LARGE_FLUID_TANK_BLOCK_ITEM.get());
        fluidTankItem(ItemRegistry.MASSIVE_FLUID_TANK_BLOCK_ITEM.get());
        fluidTankItem(ItemRegistry.CREATIVE_FLUID_TANK_BLOCK_ITEM.get());
    }

    private void basicItem(Item item) {
        itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
    }

    private void handheldItem(Item item) {
        itemModels.generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM);
    }

    private void fluidTankItem(Item item) {
        ResourceLocation chunkLoaderLocation = BuiltInRegistries.ITEM.getKey(item).withSuffix("_frame");
        specialRendererItem(item, chunkLoaderLocation, new FluidTankSpecialRenderer.Unbaked());
    }

    private void specialRendererItem(Item item, ResourceLocation resourceLocation, SpecialModelRenderer.Unbaked unbaked) {
        itemModels.itemModelOutput.accept(
                item,
                new SpecialModelWrapper.Unbaked(
                    resourceLocation.withPrefix("block/"),
                    unbaked
                )
        );
    }

    private void createPortalGun() {
        PortalGunItem item = ItemRegistry.PORTAL_GUN.get();
        TextureSlot MAIN_GUN = TextureSlot.create("main");
        TextureSlot HANDLE = TextureSlot.create("handle");
        ResourceLocation modelLocation = new ModelTemplate(
                Optional.empty(),
                Optional.empty(),
                MAIN_GUN,
                HANDLE
        ).extend().guiLight(UnbakedModel.GuiLight.FRONT).
                transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, transformVecBuilder -> transformVecBuilder.translation(0, 3.5f, -3)).
                transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND, transformVecBuilder -> transformVecBuilder.translation(0, 3.5f, -3)).
                transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, transformVecBuilder -> transformVecBuilder.translation(0, 2.5f, -4)).
                transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND, transformVecBuilder -> transformVecBuilder.translation(0, 2.5f, -4)).
                transform(ItemDisplayContext.FIXED, transformVecBuilder -> transformVecBuilder.rotation(0, 90, 0)).
                transform(ItemDisplayContext.GUI, transformVecBuilder -> transformVecBuilder.rotation(25, 135, 0).scale(1.25f).translation(0, 2.5f, 0)).
                element(elementBuilder -> {
                    // Main cube
                    elementBuilder.
                            from(5.5f, 5, 3).
                            to(10.5f, 8, 11).
                            textureAll(MAIN_GUN).
                            face(Direction.UP, f -> f.uvs(0, 0, 5, 8)).
                            face(Direction.DOWN, f -> f.uvs(5, 0, 10, 8)).
                            face(Direction.EAST, f -> f.uvs(10, 0, 13, 8)).
                            face(Direction.WEST, f -> f.uvs(10, 0, 13, 8)).
                            face(Direction.NORTH, f -> f.uvs(0, 8, 5, 11)).
                            face(Direction.SOUTH, f -> f.uvs(5, 8, 10, 11));
                }).element(elementBuilder -> {
                    // Fluid part
                    elementBuilder.
                            from(7.5f, 8, 5).
                            to(8.5f, 11, 6).
                            textureAll(MAIN_GUN).
                            face(Direction.UP, f -> f.uvs(0, 11, 1, 12)).
                            face(Direction.DOWN, f -> f.uvs(0, 11, 1, 12)).
                            face(Direction.EAST, f -> f.uvs(0, 11, 1, 14)).
                            face(Direction.WEST, f -> f.uvs(0, 11, 1, 14)).
                            face(Direction.SOUTH, f -> f.uvs(0, 11, 1, 14)).
                            face(Direction.NORTH, f -> f.uvs(0, 11, 1, 14));
                }).element(elementBuilder -> {
                    // Handle
                    elementBuilder.
                            from(7, 1.4f, 11.65f).
                            to(9, 7.4f, 13.65f).
                            rotation(rotationBuilder -> rotationBuilder.origin(7, 1, 11).axis(Direction.Axis.X).angle(-22.5f)).
                            textureAll(HANDLE).
                            face(Direction.UP, f -> f.uvs(0, 6, 2, 8)).
                            face(Direction.DOWN, f -> f.uvs(2, 6, 4, 8)).
                            face(Direction.EAST, f -> f.uvs(0, 0, 2, 6)).
                            face(Direction.WEST, f -> f.uvs(2, 0, 4, 6)).
                            face(Direction.SOUTH, f -> f.uvs(4, 0, 6, 6)).
                            face(Direction.NORTH, f -> f.uvs(6, 0, 8, 6));
                }).build().create(
                        item,
                        new TextureMapping().
                                put(MAIN_GUN, TextureMapping.getItemTexture(item)).
                                put(HANDLE, TextureMapping.getItemTexture(item, "_handle")),
                        itemModels.modelOutput
                );
        itemModels.itemModelOutput.accept(
                item,
                ItemModelUtils.plainModel(modelLocation)
        );
    }

    private void createDemonicCrucible() {
        Item item = ItemRegistry.DEMONIC_CRUCIBLE.get();
        ModelTemplate template = new ModelTemplate(
                ModelTemplates.FLAT_HANDHELD_ITEM.model,
                Optional.empty(),
                TextureSlot.LAYER0
        );
        ResourceLocation chargedModelLocation = template.create(
                ItemRegistry.DEMONIC_CRUCIBLE.get(),
                new TextureMapping().
                        put(TextureSlot.LAYER0, TextureMapping.getItemTexture(item)),
                itemModels.modelOutput
        );
        ResourceLocation unchargedModelLocation = template.extend().suffix("_uncharged").build().create(
                ItemRegistry.DEMONIC_CRUCIBLE.get(),
                new TextureMapping().
                        put(TextureSlot.LAYER0, TextureMapping.getItemTexture(item, "_uncharged")),
                itemModels.modelOutput
        );

        itemModels.itemModelOutput.accept(
                ItemRegistry.DEMONIC_CRUCIBLE.get(),
                ItemModelUtils.conditional(
                        new ChargedItemProperty(),
                        ItemModelUtils.plainModel(chargedModelLocation),
                        ItemModelUtils.plainModel(unchargedModelLocation)
                )
        );
    }
}
