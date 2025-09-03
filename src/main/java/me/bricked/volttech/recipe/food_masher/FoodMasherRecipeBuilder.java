package me.bricked.volttech.recipe.food_masher;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.recipe.SimpleRecipeBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

public class FoodMasherRecipeBuilder extends SimpleRecipeBuilder {
    private final Ingredient inputItem;
    private final int ticks;
    public FoodMasherRecipeBuilder(ItemStack result, Ingredient inputItem, int ticks) {
        super(result);
        this.inputItem = inputItem;
        this.ticks = ticks;
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceKey<Recipe<?>> resourceKey) {
        // Build the advancement.
        Advancement.Builder advancement = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceKey))
                .rewards(AdvancementRewards.Builder.recipe(resourceKey))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement::addCriterion);
        FoodMasherRecipe recipe = new FoodMasherRecipe(this.inputItem, this.result, this.ticks);
        recipeOutput.accept(resourceKey, recipe, advancement.build(resourceKey.location().withPrefix("recipes/")));
    }

    @Override
    public void save(RecipeOutput recipeOutput) {
        ResourceLocation outputId = BuiltInRegistries.ITEM.getKey(result.getItem());
        ResourceLocation inputId = BuiltInRegistries.ITEM.getKey(inputItem.getValues().get(0).value());
        String rlName = outputId.getPath() + "_from_mashing_" + inputId.getPath();
        save(recipeOutput, ResourceKey.create(Registries.RECIPE, VoltTech.resourceLocation(rlName)));
    }
}
