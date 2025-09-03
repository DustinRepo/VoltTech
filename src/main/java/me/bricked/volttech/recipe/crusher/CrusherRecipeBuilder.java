package me.bricked.volttech.recipe.crusher;

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

public class CrusherRecipeBuilder extends SimpleRecipeBuilder {
    private final ItemStack chanceResult;
    private final Ingredient inputItem;
    private final int ticks;
    private final float chance;
    public CrusherRecipeBuilder(ItemStack result, ItemStack chanceResult, Ingredient inputItem, int ticks, float chance) {
        super(result);
        this.inputItem = inputItem;
        this.ticks = ticks;
        this.chanceResult = chanceResult;
        this.chance = chance;
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceKey<Recipe<?>> resourceKey) {
        // Build the advancement.
        Advancement.Builder advancement = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceKey))
                .rewards(AdvancementRewards.Builder.recipe(resourceKey))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement::addCriterion);
        CrusherRecipe recipe = new CrusherRecipe(this.inputItem, this.result, this.chanceResult, this.ticks, this.chance);
        recipeOutput.accept(resourceKey, recipe, advancement.build(resourceKey.location().withPrefix("recipes/")));
    }

    @Override
    public void save(RecipeOutput recipeOutput) {
        ResourceLocation outputId = BuiltInRegistries.ITEM.getKey(result.getItem());
        ResourceLocation inputId = BuiltInRegistries.ITEM.getKey(inputItem.getValues().get(0).value());
        String rlName = outputId.getPath() + "_from_crushing_" + inputId.getPath();
        save(recipeOutput, ResourceKey.create(Registries.RECIPE, VoltTech.resourceLocation(rlName)));
    }
}
