package vectorwing.farmersdelight.integration.jei.cooking;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CookingRecipeCategory implements IRecipeCategory<CookingPotRecipe>
{
	public static final ResourceLocation UID = new ResourceLocation(FarmersDelight.MODID, "cooking");
	private final String title;
	private final IDrawable background;
	private final IDrawable icon;
	protected final IDrawable heatIndicator;
	protected final IDrawableAnimated arrow;

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
	public ResourceLocation getUid()
	{
		return UID;
	}

	@Override
	public Class<? extends CookingPotRecipe> getRecipeClass()
	{
		return CookingPotRecipe.class;
	}

	@Override
	public String getTitle()
	{
		return this.title;
	}

	@Override
	public IDrawable getBackground()
	{
		return this.background;
	}

	@Override
	public IDrawable getIcon()
	{
		return this.icon;
	}

	@Override
	public void setIngredients(CookingPotRecipe cookingPotRecipe, IIngredients ingredients)
	{
		List<Ingredient> inputAndContainer = new ArrayList<>(cookingPotRecipe.getIngredients());
		inputAndContainer.add(Ingredient.fromStacks(cookingPotRecipe.getOutputContainer()));

		ingredients.setInputIngredients(inputAndContainer);
		ingredients.setOutput(VanillaTypes.ITEM, cookingPotRecipe.getRecipeOutput());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, CookingPotRecipe recipe, IIngredients ingredients)
	{
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

		itemStacks.init(MEAL_DISPLAY, false, 94, 9);
		itemStacks.set(MEAL_DISPLAY, recipe.getRecipeOutput().getStack());

		if (!recipe.getOutputContainer().isEmpty()) {
			itemStacks.init(CONTAINER_INPUT, false, 62, 38);
			itemStacks.set(CONTAINER_INPUT, recipe.getOutputContainer());
		}

		itemStacks.init(OUTPUT, false, 94, 38);
		itemStacks.set(OUTPUT, recipe.getRecipeOutput().getStack());
	}

	@Override
	public void draw(CookingPotRecipe recipe, double mouseX, double mouseY) {
		arrow.draw(60, 9);
		heatIndicator.draw(18, 39);
	}
}
