package vectorwing.farmersdelight.integration.jei.cooking;

import com.mojang.blaze3d.matrix.MatrixStack;
import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.registry.ModItems;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CookingRecipeCategory implements IRecipeCategory<CookingPotRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(FarmersDelight.MODID, "cooking");
    protected final IDrawable heatIndicator;
    protected final IDrawableAnimated arrow;
    private final String title;
    private final IDrawable background;
    private final IDrawable icon;

    public CookingRecipeCategory(IGuiHelper helper) {
        title = I18n.format(FarmersDelight.MODID + ".jei.cooking");
        ResourceLocation backgroundImage = new ResourceLocation(FarmersDelight.MODID, "textures/gui/cooking_pot.png");
        background = helper.createDrawable(backgroundImage, 29, 16, 117, 57);
        icon = helper.createDrawableIngredient(new ItemStack(ModItems.COOKING_POT.get()));
        heatIndicator = helper.createDrawable(backgroundImage, 176, 0, 17, 15);
        arrow = helper.drawableBuilder(backgroundImage, 176, 15, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends CookingPotRecipe> getRecipeClass() {
        return CookingPotRecipe.class;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(CookingPotRecipe cookingPotRecipe, IIngredients ingredients) {
        ingredients.setInputIngredients(cookingPotRecipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, cookingPotRecipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CookingPotRecipe recipe, IIngredients ingredients) {
        final int MEAL_DISPLAY = 6;
        final int CONTAINER_INPUT = 7;
        final int OUTPUT = 8;
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        NonNullList<Ingredient> recipeIngredients = recipe.getIngredients();

        int borderSlotSize = 18;
        for (int row = 0; row < 2; ++row) {
            for (int column = 0; column < 3; ++column) {
                int inputIndex = row * 3 + column;
                if (inputIndex < recipeIngredients.size()) {
                    itemStacks.init(inputIndex, true, column * borderSlotSize, row * borderSlotSize);
                    itemStacks.set(inputIndex, Arrays.asList(recipeIngredients.get(inputIndex).getMatchingStacks()));
                }
            }
        }

        itemStacks.init(MEAL_DISPLAY, true, 94, 9);
        itemStacks.set(MEAL_DISPLAY, recipe.getRecipeOutput().getStack());

        if (!recipe.getOutputContainer().isEmpty()) {
            itemStacks.init(CONTAINER_INPUT, true, 62, 38);
            itemStacks.set(CONTAINER_INPUT, recipe.getOutputContainer());
        }

        itemStacks.init(OUTPUT, true, 94, 38);
        itemStacks.set(OUTPUT, recipe.getRecipeOutput().getStack());
    }

    @Override
    public void draw(CookingPotRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        arrow.draw(matrixStack, 60, 9);
        heatIndicator.draw(matrixStack, 18, 39);
    }
}
