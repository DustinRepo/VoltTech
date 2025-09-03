package me.bricked.volttech.jei;

import me.bricked.volttech.VoltTech;
import me.bricked.volttech.recipe.food_masher.FoodMasherRecipe;
import me.bricked.volttech.register.ItemRegistry;
import me.bricked.volttech.util.RenderUtil;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fStack;
import org.joml.Vector4i;

public class FoodMasherRecipeCategory implements IRecipeCategory<FoodMasherRecipe> {
    private static ResourceLocation TYPE_RL = VoltTech.resourceLocation("food_masher");
    public static final IRecipeType<FoodMasherRecipe> TYPE = new IRecipeType<>() {
        @Override
        public ResourceLocation getUid() {
            return TYPE_RL;
        }

        @Override
        public Class<? extends FoodMasherRecipe> getRecipeClass() {
            return FoodMasherRecipe.class;
        }
    };
    private final Vector4i feBarPosition = new Vector4i(158, 15, 168, 78);
    private final IDrawable background;
    private final IDrawable background_bottom;
    private final IDrawable icon;
    public FoodMasherRecipeCategory(IGuiHelper helper) {
        ResourceLocation TEXTURE = VoltTech.resourceLocation("textures/gui/food_masher_gui.png");
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 80);
        this.background_bottom = helper.createDrawable(TEXTURE, 0, 160, 176, 6);
        this.icon = helper.createDrawableItemLike(ItemRegistry.FOOD_MASHER_BLOCK_ITEM.get());
    }

    @Override
    public IRecipeType<FoodMasherRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.volttech.food_masher");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return 176;
    }

    @Override
    public int getHeight() {
        return 86;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FoodMasherRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 54, 35).add(recipe.inputItem());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 107, 35).add(recipe.result());
    }

    @Override
    public void draw(FoodMasherRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
        Matrix3x2fStack poseStack = guiGraphics.pose();
        poseStack.pushMatrix();
        poseStack.translate(0, 80, poseStack);
        background_bottom.draw(guiGraphics);
        poseStack.translate(0, -80, poseStack);
        poseStack.popMatrix();

        int feBarWidth = feBarPosition.z - feBarPosition.x;
        int feBarHeight = feBarPosition.w - feBarPosition.y;
        RenderUtil.renderFluidInGui(guiGraphics, RenderUtil.FORGE_ENERGY_SPRITE, feBarPosition.x, feBarPosition.w - feBarHeight, feBarWidth, feBarHeight);
    }
}