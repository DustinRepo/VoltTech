package me.bricked.volttech.jei;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.VoltTechClient;
import me.bricked.volttech.recipe.crusher.CrusherRecipe;
import me.bricked.volttech.recipe.food_masher.FoodMasherRecipe;
import me.bricked.volttech.register.RecipeRegistry;
import me.bricked.volttech.screen.CrusherScreen;
import me.bricked.volttech.screen.FoodMasherScreen;
import me.bricked.volttech.screen.PoweredFurnaceScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;

import java.util.List;

@JeiPlugin
public class JEIVoltTechPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new CrusherRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new FoodMasherRecipeCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (VoltTechClient.recipeMap == null)
            return;
        List<CrusherRecipe> crusherRecipes = VoltTechClient.recipeMap.byType(RecipeRegistry.CRUSHER_RECIPE.get()).stream().map(RecipeHolder::value).toList();
        List<FoodMasherRecipe> foodMasherRecipes = VoltTechClient.recipeMap.byType(RecipeRegistry.FOOD_MASHER_RECIPE.get()).stream().map(RecipeHolder::value).toList();

        registration.addRecipes(CrusherRecipeCategory.TYPE, crusherRecipes);
        registration.addRecipes(FoodMasherRecipeCategory.TYPE, foodMasherRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CrusherScreen.class, 76, 37, 23, 12, CrusherRecipeCategory.TYPE);
        registration.addRecipeClickArea(FoodMasherScreen.class, 76, 37, 23, 12, FoodMasherRecipeCategory.TYPE);
        for (int i = 0; i < 4; i++)
            registration.addRecipeClickArea(PoweredFurnaceScreen.class, 28 + (36 * i), 35, 12, 23, RecipeTypes.SMELTING);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return VoltTech.resourceLocation("jei_plugin");
    }
}
