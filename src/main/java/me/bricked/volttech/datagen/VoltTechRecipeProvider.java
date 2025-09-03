package me.bricked.volttech.datagen;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.recipe.crusher.CrusherRecipeBuilder;
import me.bricked.volttech.recipe.food_masher.FoodMasherRecipeBuilder;
import me.bricked.volttech.register.ItemRegistry;
import me.bricked.volttech.register.TagRegistry;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.BlockTagIngredient;

import java.util.concurrent.CompletableFuture;

public class VoltTechRecipeProvider extends RecipeProvider {
    private final HolderGetter<Item> items;
    protected VoltTechRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
        this.items = registries.lookupOrThrow(Registries.ITEM);
    }

    @Override
    protected void buildRecipes() {
        createShapedRecipes(this.output);
        createShapelessRecipes(this.output);
        createSmeltRecipes(this.output);
        createCrusherRecipes(this.output);
        // Generate food masher recipes from item registry
        BuiltInRegistries.ITEM.forEach(item -> {
            if (item == ItemRegistry.EDIBLE_SLOP.get())
                return;
            if (!item.getDefaultInstance().has(DataComponents.FOOD))
                return;
            FoodProperties foodProperties = item.getDefaultInstance().get(DataComponents.FOOD);
            if (foodProperties != null) {
                ResourceLocation name = BuiltInRegistries.ITEM.getKey(item);
                String itemName = name.getPath();
                int count = foodProperties.nutrition();
                new FoodMasherRecipeBuilder(
                        new ItemStack(ItemRegistry.EDIBLE_SLOP.get(), count),
                        Ingredient.of(item),
                        50
                ).
                unlockedBy("has_" + itemName, has(item)).
                save(this.output);
            }
        });
        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(Items.BLAZE_POWDER),
                Ingredient.of(Items.IRON_INGOT),
                Ingredient.of(ItemRegistry.HELLFORGED_SHARD),
                RecipeCategory.MISC,
                ItemRegistry.HELLFORGED_INGOT.get())
                .unlocks("has_hellforged_shard", has(ItemRegistry.HELLFORGED_SHARD))
                .save(this.output, VoltTech.MODID + ":hellforged_ingot_from_smithing");
    }

    private void createCrusherRecipes(RecipeOutput recipeOutput) {
        createCrushers(recipeOutput,
                new CrusherOutputs(
                        100,
                        0.2f,
                        new ItemStack(ItemRegistry.CRUSHED_IRON.get(), 2),
                        new ItemStack(ItemRegistry.CRUSHED_IRON.get(), 1)
                ),
                Ingredient.of(Items.RAW_IRON),
                Ingredient.of(Items.IRON_ORE),
                Ingredient.of(Items.DEEPSLATE_IRON_ORE)
        );
        createCrushers(recipeOutput,
                new CrusherOutputs(
                        100,
                        0.2f,
                        new ItemStack(ItemRegistry.CRUSHED_GOLD.get(), 2),
                        new ItemStack(ItemRegistry.CRUSHED_GOLD.get(), 1)
                ),
                Ingredient.of(Items.RAW_GOLD),
                Ingredient.of(Items.GOLD_ORE),
                Ingredient.of(Items.DEEPSLATE_GOLD_ORE),
                Ingredient.of(Items.NETHER_GOLD_ORE)
        );
        createCrushers(recipeOutput,
                new CrusherOutputs(
                        100,
                        0.2f,
                        new ItemStack(ItemRegistry.CRUSHED_COPPER.get(), 2),
                        new ItemStack(ItemRegistry.CRUSHED_COPPER.get(), 1)
                ),
                Ingredient.of(Items.RAW_COPPER),
                Ingredient.of(Items.COPPER_ORE),
                Ingredient.of(Items.DEEPSLATE_COPPER_ORE)
        );
        createCrushers(recipeOutput,
                new CrusherOutputs(
                        100,
                        0.15f,
                        new ItemStack(ItemRegistry.CRUSHED_DIAMOND.get(), 2),
                        new ItemStack(ItemRegistry.CRUSHED_DIAMOND.get(), 1)
                ),
                Ingredient.of(Items.DIAMOND_ORE),
                Ingredient.of(Items.DEEPSLATE_DIAMOND_ORE)
        );
        createCrushers(recipeOutput,
                new CrusherOutputs(
                        100,
                        0.15f,
                        new ItemStack(ItemRegistry.CRUSHED_EMERALD.get(), 2),
                        new ItemStack(ItemRegistry.CRUSHED_EMERALD.get(), 1)
                ),
                Ingredient.of(Items.EMERALD_ORE),
                Ingredient.of(Items.DEEPSLATE_EMERALD_ORE)
        );
        createCrushers(recipeOutput,
                new CrusherOutputs(
                        100,
                        0.5f,
                        new ItemStack(ItemRegistry.CRUSHED_REDSTONE.get(), 2),
                        new ItemStack(ItemRegistry.CRUSHED_REDSTONE.get(), 1)
                ),
                Ingredient.of(Items.REDSTONE_ORE),
                Ingredient.of(Items.DEEPSLATE_REDSTONE_ORE)
        );
        createCrushers(recipeOutput,
                new CrusherOutputs(
                        100,
                        0.5f,
                        new ItemStack(ItemRegistry.CRUSHED_LAPIS.get(), 2),
                        new ItemStack(ItemRegistry.CRUSHED_LAPIS.get(), 1)
                ),
                Ingredient.of(Items.LAPIS_ORE),
                Ingredient.of(Items.DEEPSLATE_LAPIS_ORE)
        );
        createCrushers(recipeOutput,
                new CrusherOutputs(
                        100,
                        0.5f,
                        new ItemStack(ItemRegistry.CRUSHED_URANIUM.get(), 2),
                        new ItemStack(ItemRegistry.CRUSHED_URANIUM.get(), 1)
                ),
                Ingredient.of(ItemRegistry.RAW_URANIUM),
                Ingredient.of(ItemRegistry.URANIUM_ORE_BLOCK_ITEM),
                Ingredient.of(ItemRegistry.DEEPSLATE_URANIUM_ORE_BLOCK_ITEM)
        );
        createCrushers(recipeOutput,
                new CrusherOutputs(
                        100,
                        0.2f,
                        new ItemStack(ItemRegistry.CRUSHED_DALEKANIUM.get(), 2),
                        new ItemStack(ItemRegistry.CRUSHED_DALEKANIUM.get(), 1)
                ),
                Ingredient.of(ItemRegistry.RAW_DALEKANIUM),
                Ingredient.of(ItemRegistry.DALEKANIUM_ORE_BLOCK_ITEM)
        );
        createCrushers(recipeOutput,
                new CrusherOutputs(
                        100,
                        0.05f,
                        new ItemStack(ItemRegistry.CRUSHED_VELTRIUM.get(), 2),
                        new ItemStack(ItemRegistry.CRUSHED_VELTRIUM.get(), 1)
                ),
                Ingredient.of(ItemRegistry.RAW_VELTRIUM),
                Ingredient.of(ItemRegistry.VELTRIUM_ORE_BLOCK_ITEM)
        );
        createCrushers(recipeOutput,
                new CrusherOutputs(
                        100,
                        0.15f,
                        new ItemStack(ItemRegistry.CRUSHED_TREXALITE.get(), 2),
                        new ItemStack(ItemRegistry.CRUSHED_TREXALITE.get(), 1)
                ),
                Ingredient.of(ItemRegistry.RAW_TREXALITE),
                Ingredient.of(ItemRegistry.DEEPSLATE_TREXALITE_ORE_BLOCK_ITEM)
        );
        createCrushers(recipeOutput,
                new CrusherOutputs(
                        100,
                        0.15f,
                        new ItemStack(ItemRegistry.CRUSHED_HELLFORGED.get(), 2),
                        new ItemStack(ItemRegistry.CRUSHED_HELLFORGED.get(), 1)
                ),
                Ingredient.of(ItemRegistry.HELLISH_ROCK_BLOCK_ITEM)
        );
        createCrushers(recipeOutput,
                new CrusherOutputs(
                        50,
                        0.75f,
                        new ItemStack(Items.FLINT, 2),
                        new ItemStack(Items.FLINT, 1)
                ),
                Ingredient.of(Items.GRAVEL)
        );
    }

    private void createCrushers(RecipeOutput recipeOutput, CrusherOutputs outputs, Ingredient... ingredients) {
        for (Ingredient ingredient : ingredients) {
            Item ingredientItem = ingredient.getValues().get(0).value();
            ResourceLocation name = BuiltInRegistries.ITEM.getKey(ingredientItem);
            String itemName = name.getPath();
            new CrusherRecipeBuilder(
                    outputs.outputStack(),
                    outputs.chanceStack(),
                    ingredient,
                    outputs.ticks(),
                    outputs.chance()
            ).
            unlockedBy("has_" + itemName, has(ingredientItem)).
            save(recipeOutput);
        }
    }

    private void createShapedRecipes(RecipeOutput recipeOutput) {
        threeByThreeStorageBlock(recipeOutput, RecipeCategory.MISC, ItemRegistry.URANIUM_BLOCK_BLOCK_ITEM, ItemRegistry.URANIUM_INGOT);
        threeByThreeStorageBlock(recipeOutput, RecipeCategory.MISC, ItemRegistry.RAW_URANIUM_BLOCK_BLOCK_ITEM, ItemRegistry.RAW_URANIUM);
        threeByThreeStorageBlock(recipeOutput, RecipeCategory.MISC, ItemRegistry.VELTRIUM_BLOCK_BLOCK_ITEM, ItemRegistry.VELTRIUM);
        threeByThreeStorageBlock(recipeOutput, RecipeCategory.MISC, ItemRegistry.RAW_VELTRIUM_BLOCK_BLOCK_ITEM, ItemRegistry.RAW_VELTRIUM);
        threeByThreeStorageBlock(recipeOutput, RecipeCategory.MISC, ItemRegistry.DALEKANIUM_BLOCK_BLOCK_ITEM, ItemRegistry.DALEKANIUM_INGOT);
        threeByThreeStorageBlock(recipeOutput, RecipeCategory.MISC, ItemRegistry.RAW_DALEKANIUM_BLOCK_BLOCK_ITEM, ItemRegistry.RAW_DALEKANIUM);
        threeByThreeStorageBlock(recipeOutput, RecipeCategory.MISC, ItemRegistry.TREXALITE_BLOCK_BLOCK_ITEM, ItemRegistry.TREXALITE);
        threeByThreeStorageBlock(recipeOutput, RecipeCategory.MISC, ItemRegistry.RAW_TREXALITE_BLOCK_BLOCK_ITEM, ItemRegistry.RAW_TREXALITE);

        pickaxeRecipe(recipeOutput, ItemRegistry.DALEKANIUM_INGOT, ItemRegistry.DALEKANIUM_PICKAXE);
        axeRecipe(recipeOutput, ItemRegistry.DALEKANIUM_INGOT, ItemRegistry.DALEKANIUM_AXE);
        shovelRecipe(recipeOutput, ItemRegistry.DALEKANIUM_INGOT, ItemRegistry.DALEKANIUM_SHOVEL);
        hoeRecipe(recipeOutput, ItemRegistry.DALEKANIUM_INGOT, ItemRegistry.DALEKANIUM_HOE);
        swordRecipe(recipeOutput, ItemRegistry.DALEKANIUM_INGOT, ItemRegistry.DALEKANIUM_SWORD);
        helmetRecipe(recipeOutput, ItemRegistry.DALEKANIUM_INGOT, ItemRegistry.DALEKANIUM_HELMET);
        chestplateRecipe(recipeOutput, ItemRegistry.DALEKANIUM_INGOT, ItemRegistry.DALEKANIUM_CHESTPLATE);
        leggingsRecipe(recipeOutput, ItemRegistry.DALEKANIUM_INGOT, ItemRegistry.DALEKANIUM_LEGGINGS);
        bootsRecipe(recipeOutput, ItemRegistry.DALEKANIUM_INGOT, ItemRegistry.DALEKANIUM_BOOTS);

        pickaxeRecipe(recipeOutput, ItemRegistry.VELTRIUM, ItemRegistry.VELTRIUM_PICKAXE);
        axeRecipe(recipeOutput, ItemRegistry.VELTRIUM, ItemRegistry.VELTRIUM_AXE);
        shovelRecipe(recipeOutput, ItemRegistry.VELTRIUM, ItemRegistry.VELTRIUM_SHOVEL);
        hoeRecipe(recipeOutput, ItemRegistry.VELTRIUM, ItemRegistry.VELTRIUM_HOE);
        swordRecipe(recipeOutput, ItemRegistry.VELTRIUM, ItemRegistry.VELTRIUM_SWORD);
        helmetRecipe(recipeOutput, ItemRegistry.VELTRIUM, ItemRegistry.VELTRIUM_HELMET);
        chestplateRecipe(recipeOutput, ItemRegistry.VELTRIUM, ItemRegistry.VELTRIUM_CHESTPLATE);
        leggingsRecipe(recipeOutput, ItemRegistry.VELTRIUM, ItemRegistry.VELTRIUM_LEGGINGS);
        bootsRecipe(recipeOutput, ItemRegistry.VELTRIUM, ItemRegistry.VELTRIUM_BOOTS);

        helmetRecipe(recipeOutput, ItemRegistry.HELLFORGED_INGOT, ItemRegistry.HELLFORGED_HELMET);
        chestplateRecipe(recipeOutput, ItemRegistry.HELLFORGED_INGOT, ItemRegistry.HELLFORGED_CHESTPLATE);
        leggingsRecipe(recipeOutput, ItemRegistry.HELLFORGED_INGOT, ItemRegistry.HELLFORGED_LEGGINGS);
        bootsRecipe(recipeOutput, ItemRegistry.HELLFORGED_INGOT, ItemRegistry.HELLFORGED_BOOTS);

        pickaxeRecipe(recipeOutput, ItemRegistry.TREXALITE, ItemRegistry.TREXALITE_PICKAXE);
        axeRecipe(recipeOutput, ItemRegistry.TREXALITE, ItemRegistry.TREXALITE_AXE);
        shovelRecipe(recipeOutput, ItemRegistry.TREXALITE, ItemRegistry.TREXALITE_SHOVEL);
        hoeRecipe(recipeOutput, ItemRegistry.TREXALITE, ItemRegistry.TREXALITE_HOE);
        swordRecipe(recipeOutput, ItemRegistry.TREXALITE, ItemRegistry.TREXALITE_SWORD);
        helmetRecipe(recipeOutput, ItemRegistry.TREXALITE, ItemRegistry.TREXALITE_HELMET);
        chestplateRecipe(recipeOutput, ItemRegistry.TREXALITE, ItemRegistry.TREXALITE_CHESTPLATE);
        leggingsRecipe(recipeOutput, ItemRegistry.TREXALITE, ItemRegistry.TREXALITE_LEGGINGS);
        bootsRecipe(recipeOutput, ItemRegistry.TREXALITE, ItemRegistry.TREXALITE_BOOTS);

        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.REDSTONE, ItemRegistry.ITEM_PIPE_BLOCK_ITEM, 8).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', Tags.Items.INGOTS_IRON).
                define('#', Tags.Items.GLASS_BLOCKS).
                define('$', ItemRegistry.ELECTRIC_COMPONENT).
                unlockedBy("has_electric_component", has(ItemRegistry.ELECTRIC_COMPONENT)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.REDSTONE, ItemRegistry.COPPER_ENERGY_CABLE_BLOCK_ITEM, 8).
                pattern("XXX").
                pattern("X#X").
                pattern("XXX").
                define('X', Tags.Items.INGOTS_COPPER).
                define('#', Items.REDSTONE).
                unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.REDSTONE, ItemRegistry.IRON_ENERGY_CABLE_BLOCK_ITEM, 8).
                pattern("XXX").
                pattern("X#X").
                pattern("XXX").
                define('X', ItemRegistry.COPPER_ENERGY_CABLE_BLOCK_ITEM).
                define('#', Items.IRON_INGOT).
                unlockedBy("has_copper_energy_cable", has(ItemRegistry.COPPER_ENERGY_CABLE_BLOCK_ITEM)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.REDSTONE, ItemRegistry.GOLD_ENERGY_CABLE_BLOCK_ITEM, 8).
                pattern("XXX").
                pattern("X#X").
                pattern("XXX").
                define('X', ItemRegistry.IRON_ENERGY_CABLE_BLOCK_ITEM).
                define('#', Items.GOLD_INGOT).
                unlockedBy("has_iron_energy_cable", has(ItemRegistry.IRON_ENERGY_CABLE_BLOCK_ITEM)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.REDSTONE, ItemRegistry.DIAMOND_ENERGY_CABLE_BLOCK_ITEM, 8).
                pattern("XXX").
                pattern("X#X").
                pattern("XXX").
                define('X', ItemRegistry.GOLD_ENERGY_CABLE_BLOCK_ITEM).
                define('#', Items.DIAMOND).
                unlockedBy("has_diamond_energy_cable", has(ItemRegistry.GOLD_ENERGY_CABLE_BLOCK_ITEM)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.REDSTONE, ItemRegistry.EMERALD_ENERGY_CABLE_BLOCK_ITEM, 8).
                pattern("XXX").
                pattern("X#X").
                pattern("XXX").
                define('X', ItemRegistry.DIAMOND_ENERGY_CABLE_BLOCK_ITEM).
                define('#', Items.EMERALD).
                unlockedBy("has_diamond_energy_cable", has(ItemRegistry.DIAMOND_ENERGY_CABLE_BLOCK_ITEM)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.REDSTONE, ItemRegistry.NETHERITE_ENERGY_CABLE_BLOCK_ITEM, 8).
                pattern("XXX").
                pattern("X#X").
                pattern("XXX").
                define('X', ItemRegistry.EMERALD_ENERGY_CABLE_BLOCK_ITEM).
                define('#', Items.NETHERITE_INGOT).
                unlockedBy("has_emerald_energy_cable", has(ItemRegistry.EMERALD_ENERGY_CABLE_BLOCK_ITEM)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.REDSTONE, ItemRegistry.COPPER_FLUID_PIPE_BLOCK_ITEM, 8).
                pattern("XXX").
                pattern("X#X").
                pattern("XXX").
                define('X', Tags.Items.INGOTS_COPPER).
                define('#', Items.WATER_BUCKET).
                unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.REDSTONE, ItemRegistry.IRON_FLUID_PIPE_BLOCK_ITEM, 8).
                pattern("XXX").
                pattern("X#X").
                pattern("XXX").
                define('X', ItemRegistry.COPPER_FLUID_PIPE_BLOCK_ITEM).
                define('#', Items.IRON_INGOT).
                unlockedBy("has_copper_fluid_pipe", has(ItemRegistry.COPPER_FLUID_PIPE_BLOCK_ITEM)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.REDSTONE, ItemRegistry.GOLD_FLUID_PIPE_BLOCK_ITEM, 8).
                pattern("XXX").
                pattern("X#X").
                pattern("XXX").
                define('X', ItemRegistry.IRON_FLUID_PIPE_BLOCK_ITEM).
                define('#', Items.GOLD_INGOT).
                unlockedBy("has_iron_fluid_pipe", has(ItemRegistry.IRON_FLUID_PIPE_BLOCK_ITEM)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.REDSTONE, ItemRegistry.DIAMOND_FLUID_PIPE_BLOCK_ITEM, 8).
                pattern("XXX").
                pattern("X#X").
                pattern("XXX").
                define('X', ItemRegistry.GOLD_FLUID_PIPE_BLOCK_ITEM).
                define('#', Items.DIAMOND).
                unlockedBy("has_diamond_fluid_pipe", has(ItemRegistry.GOLD_FLUID_PIPE_BLOCK_ITEM)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.REDSTONE, ItemRegistry.EMERALD_FLUID_PIPE_BLOCK_ITEM, 8).
                pattern("XXX").
                pattern("X#X").
                pattern("XXX").
                define('X', ItemRegistry.DIAMOND_FLUID_PIPE_BLOCK_ITEM).
                define('#', Items.EMERALD).
                unlockedBy("has_diamond_fluid_pipe", has(ItemRegistry.DIAMOND_FLUID_PIPE_BLOCK_ITEM)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.REDSTONE, ItemRegistry.NETHERITE_FLUID_PIPE_BLOCK_ITEM, 8).
                pattern("XXX").
                pattern("X#X").
                pattern("XXX").
                define('X', ItemRegistry.EMERALD_FLUID_PIPE_BLOCK_ITEM).
                define('#', Items.NETHERITE_INGOT).
                unlockedBy("has_emerald_fluid_pipe", has(ItemRegistry.EMERALD_FLUID_PIPE_BLOCK_ITEM)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.REDSTONE, ItemRegistry.SMALL_BATTERY, 1).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', Tags.Items.INGOTS_IRON).
                define('#', Tags.Items.INGOTS_COPPER).
                define('$', ItemRegistry.ELECTRIC_COMPONENT).
                unlockedBy("has_electric_component", has(ItemRegistry.ELECTRIC_COMPONENT)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.REDSTONE, ItemRegistry.MEDIUM_BATTERY, 1).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', Tags.Items.INGOTS_IRON).
                define('#', Tags.Items.INGOTS_COPPER).
                define('$', ItemRegistry.SMALL_BATTERY).
                unlockedBy("has_small_battery", has(ItemRegistry.SMALL_BATTERY)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.REDSTONE, ItemRegistry.LARGE_BATTERY, 1).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', Tags.Items.INGOTS_GOLD).
                define('#', Tags.Items.INGOTS_COPPER).
                define('$', ItemRegistry.MEDIUM_BATTERY).
                unlockedBy("has_medium_battery", has(ItemRegistry.MEDIUM_BATTERY)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.REDSTONE, ItemRegistry.MASSIVE_BATTERY, 1).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', Tags.Items.INGOTS_GOLD).
                define('#', Tags.Items.STORAGE_BLOCKS_COPPER).
                define('$', ItemRegistry.LARGE_BATTERY).
                unlockedBy("has_large_battery", has(ItemRegistry.LARGE_BATTERY)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.COMBUSTION_GENERATOR_BLOCK_ITEM).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', Tags.Items.INGOTS_IRON).
                define('#', Tags.Items.PLAYER_WORKSTATIONS_FURNACES).
                define('$', ItemRegistry.ELECTRIC_COMPONENT).
                unlockedBy("has_electric_component", has(ItemRegistry.ELECTRIC_COMPONENT)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.CHUNK_LOADER_BLOCK_ITEM).
                pattern("X#X").
                pattern("@$@").
                pattern("X#X").
                define('X', Tags.Items.INGOTS_GOLD).
                define('#', Tags.Items.GEMS_DIAMOND).
                define('@', Items.ENDER_EYE).
                define('$', Items.ANVIL).
                unlockedBy("has_anvil", has(Items.ANVIL)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.HEAT_GENERATOR_BLOCK_ITEM).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', TagRegistry.Items.HELLFORGED_INGOT_TAG).
                define('#', Tags.Items.BUCKETS_LAVA).
                define('$', ItemRegistry.ELECTRIC_COMPONENT).
                unlockedBy("has_hellforged_shard", has(ItemRegistry.HELLFORGED_SHARD)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.SOLAR_GENERATOR_BLOCK_ITEM).
                pattern("XXX").
                pattern("$#$").
                pattern("$$$").
                define('X', ItemRegistry.SOLAR_PANEL).
                define('#', ItemRegistry.ELECTRIC_COMPONENT).
                define('$', Tags.Items.COBBLESTONES).
                unlockedBy("has_solar_panel", has(ItemRegistry.SOLAR_PANEL)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.SOLAR_PANEL, 3).
                pattern("XXX").
                pattern("###").
                pattern("XXX").
                define('X', Tags.Items.GEMS_LAPIS).
                define('#', Tags.Items.GLASS_BLOCKS).
                unlockedBy("has_lapis_lazuli", has(Tags.Items.GEMS_LAPIS)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.JETPACK).
                pattern("X X").
                pattern("@$@").
                pattern("# #").
                define('X', Tags.Items.STRINGS).
                define('@', Tags.Items.LEATHERS).
                define('#', ItemRegistry.BOOSTER).
                define('$', ItemRegistry.MEDIUM_BATTERY).
                unlockedBy("has_booster", has(ItemRegistry.BOOSTER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.TORCH_DISPENSER).
                pattern("X#X").
                pattern("X$X").
                pattern("XXX").
                define('X', Tags.Items.INGOTS_IRON).
                define('#', Items.TORCH).
                define('$', ItemRegistry.SMALL_BATTERY).
                unlockedBy("has_torch", has(Items.TORCH)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, ItemRegistry.UTILITY_WRENCH).
                pattern("  #").
                pattern(" X ").
                pattern("X  ").
                define('X', Tags.Items.INGOTS_COPPER).
                define('#', Tags.Items.INGOTS_IRON).
                unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TRANSPORTATION, ItemRegistry.HOMING_DRIVE).
                pattern("X#X").
                pattern("&$&").
                pattern("X@X").
                define('X', Tags.Items.GEMS_DIAMOND).
                define('#', Tags.Items.ENDER_PEARLS).
                define('@', Items.ENDER_EYE).
                define('&', Items.COMPASS).
                define('$', ItemRegistry.MEDIUM_BATTERY).
                unlockedBy("has_medium_battery", has(ItemRegistry.MEDIUM_BATTERY)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TRANSPORTATION, ItemRegistry.GRAVE_TRANSPORTER).
                pattern("X#X").
                pattern("&$&").
                pattern("X@X").
                define('X', Tags.Items.GEMS_DIAMOND).
                define('#', Tags.Items.ENDER_PEARLS).
                define('@', Items.TOTEM_OF_UNDYING).
                define('&', Items.COMPASS).
                define('$', ItemRegistry.MEDIUM_BATTERY).
                unlockedBy("has_medium_battery", has(ItemRegistry.MEDIUM_BATTERY)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TRANSPORTATION, ItemRegistry.PORTAL_GUN).
                pattern("X#X").
                pattern("&$&").
                pattern("X@X").
                define('X', Items.ENDER_EYE).
                define('#', ItemRegistry.GRAVE_TRANSPORTER).
                define('@', ItemRegistry.HOMING_DRIVE).
                define('&', Items.COMPASS).
                define('$', ItemRegistry.ELECTRIC_COMPONENT).
                unlockedBy("has_electric_component", has(ItemRegistry.ELECTRIC_COMPONENT)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.ENERGY_UPGRADE_CARD).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', Tags.Items.DUSTS_REDSTONE).
                define('#', Tags.Items.INGOTS_COPPER).
                define('$', Items.PAPER).
                unlockedBy("has_paper", has(Items.PAPER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.SPEED_UPGRADE_CARD).
                pattern("X#X").
                pattern("@$@").
                pattern("X#X").
                define('X', Items.SUGAR).
                define('#', Items.BLAZE_POWDER).
                define('@', Tags.Items.FEATHERS).
                define('$', Items.PAPER).
                unlockedBy("has_paper", has(Items.PAPER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.NIGHT_VISION_UPGRADE_CARD).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', Tags.Items.DUSTS_GLOWSTONE).
                define('#', Items.GOLDEN_CARROT).
                define('$', Items.PAPER).
                unlockedBy("has_paper", has(Items.PAPER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.JUMP_BOOST_UPGRADE_CARD).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', Items.RABBIT_FOOT).
                define('#', Tags.Items.SLIME_BALLS).
                define('$', Items.PAPER).
                unlockedBy("has_paper", has(Items.PAPER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.JETPACK_UPGRADE_CARD).
                pattern("X@X").
                pattern("#$#").
                pattern("X&X").
                define('X', Tags.Items.FEATHERS).
                define('#', Items.PHANTOM_MEMBRANE).
                define('@', ItemRegistry.JETPACK).
                define('&', ItemRegistry.ELECTRIC_COMPONENT).
                define('$', Items.PAPER).
                unlockedBy("has_paper", has(Items.PAPER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.FEATHER_FALLING_UPGRADE_CARD).
                pattern("XXX").
                pattern("X$X").
                pattern("XXX").
                define('X', Tags.Items.FEATHERS).
                define('$', Items.PAPER).
                unlockedBy("has_paper", has(Items.PAPER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.HEALTH_UPGRADE_CARD).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', Items.APPLE).
                define('#', Tags.Items.INGOTS_GOLD).
                define('$', Items.PAPER).
                unlockedBy("has_paper", has(Items.PAPER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.WATER_BREATHING_UPGRADE_CARD).
                pattern("X#X").
                pattern("@$@").
                pattern("X#X").
                define('X', Items.KELP).
                define('#', Items.PUFFERFISH).
                define('@', Items.PRISMARINE_SHARD).
                define('$', Items.PAPER).
                unlockedBy("has_paper", has(Items.PAPER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.AUTO_FEED_UPGRADE_CARD).
                pattern("X#X").
                pattern("@$@").
                pattern("X#X").
                define('X', Items.BREAD).
                define('#', Items.COOKED_BEEF).
                define('@', Items.COOKED_CHICKEN).
                define('$', Items.PAPER).
                unlockedBy("has_paper", has(Items.PAPER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.FIRE_RESIST_UPGRADE_CARD).
                pattern("X#X").
                pattern("@$@").
                pattern("X#X").
                define('X', Items.MAGMA_CREAM).
                define('#', Items.BLAZE_POWDER).
                define('@', Items.FIRE_CHARGE).
                define('$', Items.PAPER).
                unlockedBy("has_paper", has(Items.PAPER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.WITHER_RESIST_UPGRADE_CARD).
                pattern("X#X").
                pattern("@$@").
                pattern("X#X").
                define('X', Items.SOUL_SAND).
                define('#', Items.BLAZE_POWDER).
                define('@', Items.WITHER_ROSE).
                define('$', Items.PAPER).
                unlockedBy("has_paper", has(Items.PAPER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.POISON_RESIST_UPGRADE_CARD).
                pattern("X#X").
                pattern("@$@").
                pattern("X#X").
                define('X', Items.SPIDER_EYE).
                define('#', Items.FERMENTED_SPIDER_EYE).
                define('@', Tags.Items.MUSHROOMS).
                define('$', Items.PAPER).
                unlockedBy("has_paper", has(Items.PAPER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.MAGNET_UPGRADE_CARD).
                pattern("X#X").
                pattern("@$@").
                pattern("X#X").
                define('X', Items.IRON_INGOT).
                define('#', Tags.Items.ENDER_PEARLS).
                define('@', Tags.Items.DUSTS_REDSTONE).
                define('$', Items.PAPER).
                unlockedBy("has_paper", has(Items.PAPER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.REGENERATION_UPGRADE_CARD).
                pattern("X#X").
                pattern("@$@").
                pattern("X#X").
                define('X', Items.GHAST_TEAR).
                define('#', Items.GLISTERING_MELON_SLICE).
                define('@', Tags.Items.INGOTS_GOLD).
                define('$', Items.PAPER).
                unlockedBy("has_paper", has(Items.PAPER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.STEP_HEIGHT_UPGRADE_CARD).
                pattern("X#X").
                pattern("@$@").
                pattern("X#X").
                define('X', Items.PISTON).
                define('#', Tags.Items.SLIME_BALLS).
                define('@', Tags.Items.INGOTS_IRON).
                define('$', Items.PAPER).
                unlockedBy("has_paper", has(Items.PAPER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.REACH_UPGRADE_CARD).
                pattern("X#X").
                pattern("@$@").
                pattern("X#X").
                define('X', Tags.Items.ENDER_PEARLS).
                define('#', Items.SPYGLASS).
                define('@', Tags.Items.RODS_WOODEN).
                define('$', Items.PAPER).
                unlockedBy("has_paper", has(Items.PAPER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.PHASE_UPGRADE_CARD).
                pattern("X#X").
                pattern("@$@").
                pattern("X#X").
                define('X', Tags.Items.OBSIDIANS).
                define('#', Tags.Items.ENDER_PEARLS).
                define('@', Items.PHANTOM_MEMBRANE).
                define('$', Items.PAPER).
                unlockedBy("has_paper", has(Items.PAPER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.MINING_SPEED_UPGRADE_CARD).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', Items.SUGAR).
                define('#', ItemTags.PICKAXES).
                define('$', Items.PAPER).
                unlockedBy("has_paper", has(Items.PAPER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.MINE_AREA_UPGRADE_CARD).
                pattern("X#X").
                pattern("@$%").
                pattern("X&X").
                define('X', Tags.Items.OBSIDIANS).
                define('#', ItemTags.PICKAXES).
                define('@', ItemTags.SHOVELS).
                define('%', ItemTags.AXES).
                define('&', ItemTags.HOES).
                define('$', Items.PAPER).
                unlockedBy("has_paper", has(Items.PAPER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.EVASION_UPGRADE_CARD).
                pattern("X#X").
                pattern("@$@").
                pattern("X#X").
                define('X', Tags.Items.FEATHERS).
                define('#', Tags.Items.ENDER_PEARLS).
                define('@', Items.SUGAR).
                define('$', Items.PAPER).
                unlockedBy("has_paper", has(Items.PAPER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.SMELTER_UPGRADE_CARD).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', Tags.Items.PLAYER_WORKSTATIONS_FURNACES).
                define('#', Items.FLINT_AND_STEEL).
                define('$', Items.PAPER).
                unlockedBy("has_paper", has(Items.PAPER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.DAMAGE_UPGRADE_CARD).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', Items.APPLE).
                define('#', ItemTags.SWORDS).
                define('$', Items.PAPER).
                unlockedBy("has_paper", has(Items.PAPER)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.SMALL_ENERGY_CUBE_BLOCK_ITEM).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', Tags.Items.INGOTS_COPPER).
                define('#', Tags.Items.DUSTS_REDSTONE).
                define('$', ItemRegistry.ELECTRIC_COMPONENT).
                unlockedBy("has_electric_component", has(ItemRegistry.ELECTRIC_COMPONENT)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.MEDIUM_ENERGY_CUBE_BLOCK_ITEM).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', Tags.Items.INGOTS_IRON).
                define('#', Tags.Items.DUSTS_REDSTONE).
                define('$', ItemRegistry.SMALL_ENERGY_CUBE_BLOCK_ITEM).
                unlockedBy("has_small_cube", has(ItemRegistry.SMALL_ENERGY_CUBE_BLOCK_ITEM)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.LARGE_ENERGY_CUBE_BLOCK_ITEM).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', Tags.Items.INGOTS_GOLD).
                define('#', Tags.Items.DUSTS_REDSTONE).
                define('$', ItemRegistry.MEDIUM_ENERGY_CUBE_BLOCK_ITEM).
                unlockedBy("has_medium_cube", has(ItemRegistry.MEDIUM_ENERGY_CUBE_BLOCK_ITEM)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.MASSIVE_ENERGY_CUBE_BLOCK_ITEM).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', Tags.Items.INGOTS_GOLD).
                define('#', Tags.Items.STORAGE_BLOCKS_REDSTONE).
                define('$', ItemRegistry.LARGE_ENERGY_CUBE_BLOCK_ITEM).
                unlockedBy("has_large_cube", has(ItemRegistry.LARGE_ENERGY_CUBE_BLOCK_ITEM)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.SMALL_FLUID_TANK_BLOCK_ITEM).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', Tags.Items.INGOTS_COPPER).
                define('#', Tags.Items.GLASS_BLOCKS).
                define('$', Tags.Items.BUCKETS).
                unlockedBy("has_bucket", has(Tags.Items.BUCKETS)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.MEDIUM_FLUID_TANK_BLOCK_ITEM).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', Tags.Items.INGOTS_IRON).
                define('#', Tags.Items.GLASS_BLOCKS).
                define('$', ItemRegistry.SMALL_FLUID_TANK_BLOCK_ITEM).
                unlockedBy("has_small_tank", has(ItemRegistry.SMALL_FLUID_TANK_BLOCK_ITEM)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.LARGE_FLUID_TANK_BLOCK_ITEM).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', Tags.Items.INGOTS_GOLD).
                define('#', Tags.Items.GLASS_BLOCKS).
                define('$', ItemRegistry.MEDIUM_FLUID_TANK_BLOCK_ITEM).
                unlockedBy("has_medium_tank", has(ItemRegistry.MEDIUM_FLUID_TANK_BLOCK_ITEM)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.MASSIVE_FLUID_TANK_BLOCK_ITEM).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', Tags.Items.INGOTS_GOLD).
                define('#', Tags.Items.GLASS_BLOCKS).
                define('$', ItemRegistry.LARGE_FLUID_TANK_BLOCK_ITEM).
                unlockedBy("has_large_tank", has(ItemRegistry.LARGE_FLUID_TANK_BLOCK_ITEM)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.WIRELESS_ENERGY_TRANSMITTER_BLOCK_ITEM).
                pattern("X#X").
                pattern("@$@").
                pattern("X&X").
                define('X', Tags.Items.INGOTS_IRON).
                define('#', Tags.Items.ENDER_PEARLS).
                define('@', Tags.Items.STORAGE_BLOCKS_REDSTONE).
                define('&', Items.COMPARATOR).
                define('$', ItemRegistry.ELECTRIC_COMPONENT).
                unlockedBy("has_comparator", has(Items.COMPARATOR)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.UPGRADE_TABLE_BLOCK_ITEM).
                pattern("X#X").
                pattern("@$@").
                pattern("%&%").
                define('X', Tags.Items.GEMS_LAPIS).
                define('%', Tags.Items.INGOTS_IRON).
                define('#', Items.ANVIL).
                define('@', Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES).
                define('&', Items.PAPER).
                define('$', Items.SMITHING_TABLE).
                unlockedBy("has_smithing_table", has(Items.SMITHING_TABLE)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.MINI_REACTOR_BLOCK_ITEM).
                pattern("X#X").
                pattern("@$@").
                pattern("X&X").
                define('X', Tags.Items.INGOTS_IRON).
                define('#', Tags.Items.BUCKETS_LAVA).
                define('&', Tags.Items.BUCKETS_WATER).
                define('@', Tags.Items.STORAGE_BLOCKS_REDSTONE).
                define('$', ItemRegistry.ELECTRIC_COMPONENT).
                unlockedBy("has_electric_component", has(ItemRegistry.ELECTRIC_COMPONENT)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.TEMPORAL_ACCELERATOR_BLOCK_ITEM).
                pattern("X#X").
                pattern("@$@").
                pattern("%&%").
                define('X', Items.CLOCK).
                define('%', Tags.Items.GEMS_QUARTZ).
                define('#', Tags.Items.ENDER_PEARLS).
                define('&', Items.REPEATER).
                define('@', Tags.Items.STORAGE_BLOCKS_REDSTONE).
                define('$', ItemRegistry.ELECTRIC_COMPONENT).
                unlockedBy("has_clock", has(Items.CLOCK)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.SPATIAL_CRATE_BLOCK_ITEM).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('X', Tags.Items.INGOTS_IRON).
                define('#', Tags.Items.CHESTS).
                define('$', ItemRegistry.ELECTRIC_COMPONENT).
                unlockedBy("has_electric_component", has(ItemRegistry.ELECTRIC_COMPONENT)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.HARVESTER_BLOCK_ITEM).
                pattern("@#@").
                pattern("X$X").
                pattern("XXX").
                define('@', Tags.Items.INGOTS_IRON).
                define('#', ItemTags.HOES).
                define('X', Tags.Items.COBBLESTONES).
                define('$', ItemRegistry.ELECTRIC_COMPONENT).
                unlockedBy("has_electric_component", has(ItemRegistry.ELECTRIC_COMPONENT)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.BLOCK_BREAKER_BLOCK_ITEM).
                pattern("@#@").
                pattern("X$X").
                pattern("XXX").
                define('@', Tags.Items.INGOTS_IRON).
                define('#', ItemTags.PICKAXES).
                define('X', Tags.Items.COBBLESTONES).
                define('$', ItemRegistry.ELECTRIC_COMPONENT).
                unlockedBy("has_electric_component", has(ItemRegistry.ELECTRIC_COMPONENT)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.BLOCK_PLACER_BLOCK_ITEM).
                pattern("@#@").
                pattern("X$X").
                pattern("XXX").
                define('@', Tags.Items.INGOTS_IRON).
                define('#', Items.DISPENSER).
                define('X', Tags.Items.COBBLESTONES).
                define('$', ItemRegistry.ELECTRIC_COMPONENT).
                unlockedBy("has_electric_component", has(ItemRegistry.ELECTRIC_COMPONENT)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.POWERED_FURNACE_BLOCK_ITEM).
                pattern("X#X").
                pattern("#$#").
                pattern("X#X").
                define('#', Tags.Items.PLAYER_WORKSTATIONS_FURNACES).
                define('X', Tags.Items.INGOTS_IRON).
                define('$', ItemRegistry.ELECTRIC_COMPONENT).
                unlockedBy("has_electric_component", has(ItemRegistry.ELECTRIC_COMPONENT)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.CRUSHER_BLOCK_ITEM).
                pattern("X@X").
                pattern("#$#").
                pattern("X#X").
                define('#', Tags.Items.COBBLESTONES).
                define('@', new BlockTagIngredient(BlockTags.STONE_PRESSURE_PLATES).toVanilla()).
                define('X', Tags.Items.INGOTS_IRON).
                define('$', ItemRegistry.ELECTRIC_COMPONENT).
                unlockedBy("has_electric_component", has(ItemRegistry.ELECTRIC_COMPONENT)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.FOOD_MASHER_BLOCK_ITEM).
                pattern("X@X").
                pattern("#$#").
                pattern("###").
                define('#', Tags.Items.COBBLESTONES).
                define('@', Items.IRON_BARS).
                define('X', Items.FLINT).
                define('$', ItemRegistry.ELECTRIC_COMPONENT).
                unlockedBy("has_electric_component", has(ItemRegistry.ELECTRIC_COMPONENT)).
                save(recipeOutput);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, ItemRegistry.WIRELESS_PLAYER_CHARGER_BLOCK_ITEM).
                pattern(" @ ").
                pattern(" $ ").
                pattern("X#X").
                define('X', ItemTags.SLABS).
                define('#', ItemRegistry.SMALL_ENERGY_CUBE_BLOCK_ITEM).
                define('$', Items.COPPER_BLOCK).
                define('@', Items.LIGHTNING_ROD).
                unlockedBy("has_small_energy_cube", has(ItemRegistry.SMALL_ENERGY_CUBE_BLOCK_ITEM)).
                save(recipeOutput);
    }

    private void createShapelessRecipes(RecipeOutput recipeOutput) {
        ShapelessRecipeBuilder.shapeless(
                this.registries.lookupOrThrow(Registries.ITEM),
                RecipeCategory.MISC,
                ItemRegistry.LOCATION_CARD_ITEM,
                1
                ).requires(ItemRegistry.LOCATION_CARD_ITEM).
                unlockedBy("has_location_card", has(ItemRegistry.LOCATION_CARD_ITEM)).
                save(recipeOutput, VoltTech.MODID + ":location_card_clear");
        simpleShapeless(recipeOutput,
                RecipeCategory.MISC,
                ItemRegistry.LOCATION_CARD_ITEM,
                1,
                Items.PAPER,
                new ItemLike[]{Items.PAPER, Items.COMPASS},
                new TagKey[]{});
        simpleShapeless(recipeOutput,
                RecipeCategory.TRANSPORTATION,
                ItemRegistry.BOOSTER,
                1,
                Items.PAPER,
                new ItemLike[]{ItemRegistry.ELECTRIC_COMPONENT, Items.FLINT_AND_STEEL},
                new TagKey[]{Tags.Items.INGOTS_IRON});
        simpleShapeless(recipeOutput,
                RecipeCategory.MISC,
                ItemRegistry.ELECTRIC_COMPONENT,
                1,
                Items.REDSTONE,
                new ItemLike[]{},
                new TagKey[]{Tags.Items.DUSTS_REDSTONE, Tags.Items.INGOTS_COPPER});
        simpleShapeless(recipeOutput,
                RecipeCategory.MISC,
                ItemRegistry.INFINITE_TORCH_DISPENSER,
                1,
                ItemRegistry.TORCH_DISPENSER,
                new ItemLike[]{ItemRegistry.TORCH_DISPENSER},
                new TagKey[]{Tags.Items.NETHER_STARS});
        simpleShapeless(recipeOutput,
                RecipeCategory.MISC,
                ItemRegistry.MECHANIZED_ENDER_PEARL,
                1,
                ItemRegistry.ELECTRIC_COMPONENT,
                new ItemLike[]{ItemRegistry.SMALL_BATTERY},
                new TagKey[]{Tags.Items.ENDER_PEARLS, Tags.Items.INGOTS_COPPER});
    }

    private void createSmeltRecipes(RecipeOutput recipeOutput) {
        smeltAndBlast(
                recipeOutput,
                ItemRegistry.RAW_URANIUM,
                RecipeCategory.MISC,
                ItemRegistry.URANIUM_INGOT.get().getDefaultInstance(),
                0.7f,
                200);
        smeltAndBlast(
                recipeOutput,
                ItemRegistry.DEEPSLATE_URANIUM_ORE_BLOCK_ITEM,
                RecipeCategory.MISC,
                ItemRegistry.URANIUM_INGOT.get().getDefaultInstance(),
                0.7f,
                200);
        smeltAndBlast(
                recipeOutput,
                ItemRegistry.URANIUM_ORE_BLOCK_ITEM,
                RecipeCategory.MISC,
                ItemRegistry.URANIUM_INGOT.get().getDefaultInstance(),
                0.7f,
                200);
        smeltAndBlast(
                recipeOutput,
                ItemRegistry.RAW_DALEKANIUM,
                RecipeCategory.MISC,
                ItemRegistry.DALEKANIUM_INGOT.get().getDefaultInstance(),
                0.7f,
                200);
        smeltAndBlast(
                recipeOutput,
                ItemRegistry.DALEKANIUM_ORE_BLOCK_ITEM,
                RecipeCategory.MISC,
                ItemRegistry.DALEKANIUM_INGOT.get().getDefaultInstance(),
                0.7f,
                200);
        smeltAndBlast(
                recipeOutput,
                ItemRegistry.RAW_VELTRIUM,
                RecipeCategory.MISC,
                ItemRegistry.VELTRIUM.get().getDefaultInstance(),
                0.7f,
                200);
        smeltAndBlast(
                recipeOutput,
                ItemRegistry.VELTRIUM_ORE_BLOCK_ITEM,
                RecipeCategory.MISC,
                ItemRegistry.VELTRIUM.get().getDefaultInstance(),
                0.7f,
                200);
        smeltAndBlast(
                recipeOutput,
                ItemRegistry.RAW_TREXALITE,
                RecipeCategory.MISC,
                ItemRegistry.TREXALITE.get().getDefaultInstance(),
                0.7f,
                200);
        smeltAndBlast(
                recipeOutput,
                ItemRegistry.DEEPSLATE_TREXALITE_ORE_BLOCK_ITEM,
                RecipeCategory.MISC,
                ItemRegistry.TREXALITE.get().getDefaultInstance(),
                0.7f,
                200);
        smeltAndBlast(
                recipeOutput,
                ItemRegistry.CRUSHED_TREXALITE,
                RecipeCategory.MISC,
                ItemRegistry.TREXALITE.get().getDefaultInstance(),
                0.7f,
                200
        );
        smeltAndBlast(
                recipeOutput,
                ItemRegistry.CRUSHED_COPPER,
                RecipeCategory.MISC,
                Items.COPPER_INGOT.getDefaultInstance(),
                0.7f,
                200
        );
        smeltAndBlast(
                recipeOutput,
                ItemRegistry.CRUSHED_IRON,
                RecipeCategory.MISC,
                Items.IRON_INGOT.getDefaultInstance(),
                0.7f,
                200
        );
        smeltAndBlast(
                recipeOutput,
                ItemRegistry.CRUSHED_GOLD,
                RecipeCategory.MISC,
                Items.GOLD_INGOT.getDefaultInstance(),
                0.7f,
                200
        );
        smeltAndBlast(
                recipeOutput,
                ItemRegistry.CRUSHED_DIAMOND,
                RecipeCategory.MISC,
                Items.DIAMOND.getDefaultInstance(),
                0.7f,
                200
        );
        smeltAndBlast(
                recipeOutput,
                ItemRegistry.CRUSHED_EMERALD,
                RecipeCategory.MISC,
                Items.EMERALD.getDefaultInstance(),
                0.7f,
                200
        );
        smeltAndBlast(
                recipeOutput,
                ItemRegistry.CRUSHED_REDSTONE,
                RecipeCategory.MISC,
                Items.REDSTONE.getDefaultInstance(),
                0.7f,
                200
        );
        smeltAndBlast(
                recipeOutput,
                ItemRegistry.CRUSHED_LAPIS,
                RecipeCategory.MISC,
                Items.LAPIS_LAZULI.getDefaultInstance(),
                0.7f,
                200
        );
        smeltAndBlast(
                recipeOutput,
                ItemRegistry.CRUSHED_HELLFORGED,
                RecipeCategory.MISC,
                ItemRegistry.HELLFORGED_SHARD.get().getDefaultInstance(),
                0.7f,
                200
        );
        smeltAndBlast(
                recipeOutput,
                ItemRegistry.CRUSHED_URANIUM,
                RecipeCategory.MISC,
                ItemRegistry.URANIUM_INGOT.get().getDefaultInstance(),
                0.7f,
                200
        );
        smeltAndBlast(
                recipeOutput,
                ItemRegistry.CRUSHED_DALEKANIUM,
                RecipeCategory.MISC,
                ItemRegistry.DALEKANIUM_INGOT.get().getDefaultInstance(),
                0.7f,
                200
        );
        smeltAndBlast(
                recipeOutput,
                ItemRegistry.CRUSHED_VELTRIUM,
                RecipeCategory.MISC,
                ItemRegistry.VELTRIUM.get().getDefaultInstance(),
                0.7f,
                200
        );
    }

    private void smeltAndBlast(RecipeOutput recipeOutput, ItemLike ingredient, RecipeCategory recipeCategory, ItemStack output, float xpGained, int ticksToComplete) {
        ResourceLocation outputId = BuiltInRegistries.ITEM.getKey(output.getItem());
        ResourceLocation inputId = BuiltInRegistries.ITEM.getKey(ingredient.asItem());
        String smeltingName = outputId.getPath() + "_from_smelting_" + inputId.getPath();
        String blastingName = outputId.getPath() + "_from_blasting_" + inputId.getPath();
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ingredient),
                        recipeCategory,
                        output,
                        xpGained,
                        ticksToComplete).
                unlockedBy(getHasName(ingredient), has(ingredient)).
                save(recipeOutput, VoltTech.MODID + ":" + smeltingName);
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(ingredient),
                        recipeCategory,
                        output,
                        xpGained,
                        ticksToComplete / 2).
                unlockedBy(getHasName(ingredient), has(ingredient)).
                save(recipeOutput, VoltTech.MODID + ":" + blastingName);
    }

    private void simpleShapeless(RecipeOutput recipeOutput, RecipeCategory recipeCategory, ItemLike result, int resultCount, ItemLike required, ItemLike[] itemsRecipe, TagKey<Item>[] tagsRecipe) {
        String required_name = BuiltInRegistries.ITEM.getKey(required.asItem()).getPath();
        String result_name = BuiltInRegistries.ITEM.getKey(result.asItem()).getPath();
        ShapelessRecipeBuilder shapedRecipeBuilder  = ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM),
                        recipeCategory,
                        result,
                        resultCount
                );
        for (ItemLike itemLike : itemsRecipe) {
            shapedRecipeBuilder = shapedRecipeBuilder.requires(itemLike);
        }
        for (TagKey<Item> itemLike : tagsRecipe) {
            shapedRecipeBuilder = shapedRecipeBuilder.requires(itemLike);
        }
        shapedRecipeBuilder.
                unlockedBy("has_" + required_name, has(required)).save(output);
    }

    private void swordRecipe(RecipeOutput recipeOutput, ItemLike swordMaterial, ItemLike result) {
        ResourceLocation materialRL = BuiltInRegistries.ITEM.getKey(result.asItem());
        String materialName = materialRL.getPath();
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, result).
                pattern(" X ").
                pattern(" X ").
                pattern(" # ").
                define('X', swordMaterial).
                define('#', Tags.Items.RODS_WOODEN).
                unlockedBy("has_" + materialName, has(swordMaterial)).
                save(recipeOutput);
    }

    private void pickaxeRecipe(RecipeOutput recipeOutput, ItemLike pickaxeMaterial, ItemLike result) {
        ResourceLocation materialRL = BuiltInRegistries.ITEM.getKey(result.asItem());
        String materialName = materialRL.getPath();
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, result).
                pattern("XXX").
                pattern(" # ").
                pattern(" # ").
                define('X', pickaxeMaterial).
                define('#', Tags.Items.RODS_WOODEN).
                unlockedBy("has_" + materialName, has(pickaxeMaterial)).
                save(recipeOutput);
    }

    private void axeRecipe(RecipeOutput recipeOutput, ItemLike axeMaterial, ItemLike result) {
        ResourceLocation materialRL = BuiltInRegistries.ITEM.getKey(result.asItem());
        String materialName = materialRL.getPath();
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, result).
                pattern("XX ").
                pattern("X# ").
                pattern(" # ").
                define('X', axeMaterial).
                define('#', Tags.Items.RODS_WOODEN).
                unlockedBy("has_" + materialName, has(axeMaterial)).
                save(recipeOutput);
    }

    private void shovelRecipe(RecipeOutput recipeOutput, ItemLike shovelMaterial, ItemLike result) {
        ResourceLocation materialRL = BuiltInRegistries.ITEM.getKey(result.asItem());
        String materialName = materialRL.getPath();
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, result).
                pattern(" X ").
                pattern(" # ").
                pattern(" # ").
                define('X', shovelMaterial).
                define('#', Tags.Items.RODS_WOODEN).
                unlockedBy("has_" + materialName, has(shovelMaterial)).
                save(recipeOutput);
    }

    private void hoeRecipe(RecipeOutput recipeOutput, ItemLike hoeMaterial, ItemLike result) {
        ResourceLocation materialRL = BuiltInRegistries.ITEM.getKey(result.asItem());
        String materialName = materialRL.getPath();
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, result).
                pattern("XX ").
                pattern(" # ").
                pattern(" # ").
                define('X', hoeMaterial).
                define('#', Tags.Items.RODS_WOODEN).
                unlockedBy("has_" + materialName, has(hoeMaterial)).
                save(recipeOutput);
    }

    private void helmetRecipe(RecipeOutput recipeOutput, ItemLike armorMaterial, ItemLike result) {
        ResourceLocation materialRL = BuiltInRegistries.ITEM.getKey(result.asItem());
        String materialName = materialRL.getPath();
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.COMBAT, result).
                pattern("XXX").
                pattern("X X").
                define('X', armorMaterial).
                unlockedBy("has_" + materialName, has(armorMaterial)).
                save(recipeOutput);
    }

    private void chestplateRecipe(RecipeOutput recipeOutput, ItemLike armorMaterial, ItemLike result) {
        ResourceLocation materialRL = BuiltInRegistries.ITEM.getKey(result.asItem());
        String materialName = materialRL.getPath();
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, result).
                pattern("X X").
                pattern("XXX").
                pattern("XXX").
                define('X', armorMaterial).
                unlockedBy("has_" + materialName, has(armorMaterial)).
                save(recipeOutput);
    }

    private void leggingsRecipe(RecipeOutput recipeOutput, ItemLike armorMaterial, ItemLike result) {
        ResourceLocation materialRL = BuiltInRegistries.ITEM.getKey(result.asItem());
        String materialName = materialRL.getPath();
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, result).
                pattern("XXX").
                pattern("X X").
                pattern("X X").
                define('X', armorMaterial).
                unlockedBy("has_" + materialName, has(armorMaterial)).
                save(recipeOutput);
    }

    private void bootsRecipe(RecipeOutput recipeOutput, ItemLike armorMaterial, ItemLike result) {
        ResourceLocation materialRL = BuiltInRegistries.ITEM.getKey(result.asItem());
        String materialName = materialRL.getPath();
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TOOLS, result).
                pattern("X X").
                pattern("X X").
                define('X', armorMaterial).
                unlockedBy("has_" + materialName, has(armorMaterial)).
                save(recipeOutput);
    }
    
    // Create storage block recipe and unpack recipe
    private void threeByThreeStorageBlock(RecipeOutput recipeOutput, RecipeCategory recipeCategory, ItemLike packed, ItemLike unpacked) {
        threeByThreePacker(recipeCategory, packed, unpacked);
        threeByThreeUnpacker(recipeOutput, recipeCategory, packed, unpacked);
    }

    private void threeByThreeUnpacker(RecipeOutput recipeOutput, RecipeCategory recipeCategory, ItemLike packed, ItemLike unpacked) {
        ResourceLocation packedRL = BuiltInRegistries.ITEM.getKey(packed.asItem());
        String packedName = packedRL.getPath();
        ResourceLocation resultRL = BuiltInRegistries.ITEM.getKey(unpacked.asItem());
        String resultName = resultRL.getPath();
        ShapelessRecipeBuilder.shapeless(this.items,
                        recipeCategory,
                        unpacked,
                        9
                ).requires(packed).
                unlockedBy("has_" + packedName, has(ItemRegistry.URANIUM_BLOCK_BLOCK_ITEM)).
                save(recipeOutput, VoltTech.MODID + ":" + resultName + "_from_" + packedName);
    }

    private record CrusherOutputs(int ticks, float chance, ItemStack outputStack, ItemStack chanceStack) {}

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(packOutput, lookupProvider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
            return new VoltTechRecipeProvider(provider, recipeOutput);
        }

        @Override
        public String getName() {
            return "VoltTech Recipes";
        }
    }
}
