package me.bricked.volttech.recipe.crusher;

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

public record CrusherRecipe(Ingredient inputItem, ItemStack result, ItemStack chanceResult, int ticks, float chance) implements Recipe<SingleRecipeInput> {
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
        return RecipeRegistry.CRUSHER_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<SingleRecipeInput>> getType() {
        return RecipeRegistry.CRUSHER_RECIPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(inputItem);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    public static class CrusherRecipeSerializer implements RecipeSerializer<CrusherRecipe> {
        public static final MapCodec<CrusherRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(CrusherRecipe::inputItem),
                ItemStack.CODEC.fieldOf("result").forGetter(CrusherRecipe::result),
                ItemStack.CODEC.fieldOf("chanceResult").forGetter(CrusherRecipe::chanceResult),
                Codec.INT.fieldOf("ticks").forGetter(CrusherRecipe::ticks),
                Codec.FLOAT.fieldOf("chance").forGetter(CrusherRecipe::chance)
        ).apply(inst, CrusherRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, CrusherRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, CrusherRecipe::inputItem,
                        ItemStack.STREAM_CODEC, CrusherRecipe::result,
                        ItemStack.STREAM_CODEC, CrusherRecipe::chanceResult,
                        ByteBufCodecs.VAR_INT, CrusherRecipe::ticks,
                        ByteBufCodecs.FLOAT, CrusherRecipe::chance,
                        CrusherRecipe::new
                );

        @Override
        public MapCodec<CrusherRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CrusherRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
