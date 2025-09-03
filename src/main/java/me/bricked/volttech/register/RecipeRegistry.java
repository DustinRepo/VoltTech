package me.bricked.volttech.register;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.recipe.crusher.CrusherRecipe;
import me.bricked.volttech.recipe.food_masher.FoodMasherRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RecipeRegistry {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, VoltTech.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, VoltTech.MODID);
    public static Supplier<RecipeType<CrusherRecipe>> CRUSHER_RECIPE;
    public static Supplier<RecipeSerializer<CrusherRecipe>> CRUSHER_SERIALIZER;
    public static Supplier<RecipeType<FoodMasherRecipe>> FOOD_MASHER_RECIPE;
    public static Supplier<RecipeSerializer<FoodMasherRecipe>> FOOD_MASHER_SERIALIZER;

    public static void registerRecipeTypes(IEventBus modEventBus) {
        CRUSHER_RECIPE = RECIPE_TYPES.register(
                "crusher",
                () -> RecipeType.simple(VoltTech.resourceLocation("crusher"))
        );
        CRUSHER_SERIALIZER = RECIPE_SERIALIZERS.register(
                "crusher",
                CrusherRecipe.CrusherRecipeSerializer::new
        );
        FOOD_MASHER_RECIPE = RECIPE_TYPES.register(
                "food_masher",
                () -> RecipeType.simple(VoltTech.resourceLocation("food_masher"))
        );
        FOOD_MASHER_SERIALIZER = RECIPE_SERIALIZERS.register(
                "food_masher",
                FoodMasherRecipe.FoodMasherRecipeSerializer::new
        );
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
    }
}
