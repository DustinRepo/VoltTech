package me.bricked.volttech.recipe.food_masher;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.bricked.volttech.register.RecipeRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public record FoodMasherRecipe(Ingredient inputItem, ItemStack result, int ticks) implements Recipe<SingleRecipeInput> {

    @Override
    public boolean matches(SingleRecipeInput crusherRecipeInput, Level level) {
        return this.inputItem.test(crusherRecipeInput.getItem(0));
    }

    @Override
    public ItemStack assemble(SingleRecipeInput crusherRecipeInput, HolderLookup.Provider provider) {
        return this.result.copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<SingleRecipeInput>> getSerializer() {
        return RecipeRegistry.FOOD_MASHER_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<SingleRecipeInput>> getType() {
        return RecipeRegistry.FOOD_MASHER_RECIPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(this.inputItem);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    public static class FoodMasherRecipeSerializer implements RecipeSerializer<FoodMasherRecipe> {
        public static final MapCodec<FoodMasherRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(FoodMasherRecipe::inputItem),
                ItemStack.CODEC.fieldOf("result").forGetter(FoodMasherRecipe::result),
                Codec.INT.fieldOf("ticks").forGetter(FoodMasherRecipe::ticks)
        ).apply(inst, FoodMasherRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, FoodMasherRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, FoodMasherRecipe::inputItem,
                        ItemStack.STREAM_CODEC, FoodMasherRecipe::result,
                        ByteBufCodecs.VAR_INT, FoodMasherRecipe::ticks,
                        FoodMasherRecipe::new
                );

        @Override
        public MapCodec<FoodMasherRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FoodMasherRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
