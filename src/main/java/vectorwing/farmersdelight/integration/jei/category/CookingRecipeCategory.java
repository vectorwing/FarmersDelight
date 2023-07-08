package vectorwing.farmersdelight.integration.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.utility.ClientRenderUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;
import vectorwing.farmersdelight.integration.jei.FDRecipeTypes;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CookingRecipeCategory implements IRecipeCategory<CookingPotRecipe>
{
	public static final ResourceLocation UID = new ResourceLocation(FarmersDelight.MODID, "cooking");
	protected final IDrawable heatIndicator;
	protected final IDrawable timeIcon;
	protected final IDrawable expIcon;
	protected final IDrawableAnimated arrow;
	private final Component title;
	private final IDrawable background;
	private final IDrawable icon;

	public CookingRecipeCategory(IGuiHelper helper) {
		title = TextUtils.getTranslation("jei.cooking");
		ResourceLocation backgroundImage = new ResourceLocation(FarmersDelight.MODID, "textures/gui/cooking_pot.png");
		background = helper.createDrawable(backgroundImage, 29, 16, 116, 56);
		icon = helper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(ModItems.COOKING_POT.get()));
		heatIndicator = helper.createDrawable(backgroundImage, 176, 0, 17, 15);
		timeIcon = helper.createDrawable(backgroundImage, 176, 32, 8, 11);
		expIcon = helper.createDrawable(backgroundImage, 176, 43, 9, 9);
		arrow = helper.drawableBuilder(backgroundImage, 176, 15, 24, 17)
				.buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public ResourceLocation getUid() {
		return this.getRecipeType().getUid();
	}

	@Override
	public Class<? extends CookingPotRecipe> getRecipeClass() {
		return this.getRecipeType().getRecipeClass();
	}

	@Override
	public RecipeType<CookingPotRecipe> getRecipeType() {
		return FDRecipeTypes.COOKING;
	}

	@Override
	public Component getTitle() {
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
		List<Ingredient> inputAndContainer = new ArrayList<>(cookingPotRecipe.getIngredients());
		inputAndContainer.add(Ingredient.of(cookingPotRecipe.getOutputContainer()));

		ingredients.setInputIngredients(inputAndContainer);
		ingredients.setOutput(VanillaTypes.ITEM, cookingPotRecipe.getResultItem());
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
					itemStacks.set(inputIndex, Arrays.asList(recipeIngredients.get(inputIndex).getItems()));
				}
			}
		}

		itemStacks.init(MEAL_DISPLAY, false, 94, 9);
		itemStacks.set(MEAL_DISPLAY, recipe.getResultItem());

		if (!recipe.getOutputContainer().isEmpty()) {
			itemStacks.init(CONTAINER_INPUT, false, 62, 38);
			itemStacks.set(CONTAINER_INPUT, recipe.getOutputContainer());
		}

		itemStacks.init(OUTPUT, false, 94, 38);
		itemStacks.set(OUTPUT, recipe.getResultItem());
	}

	@Override
	public void draw(CookingPotRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
		arrow.draw(poseStack, 60, 9);
		heatIndicator.draw(poseStack, 18, 39);
		timeIcon.draw(poseStack, 64, 2);
		if (recipe.getExperience() > 0) {
			expIcon.draw(poseStack, 63, 21);
		}
	}

	@Override
	public List<Component> getTooltipStrings(CookingPotRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if (ClientRenderUtils.isCursorInsideBounds(61, 2, 22, 28, mouseX, mouseY)) {
			List<Component> tooltipStrings = new ArrayList<>();

			int cookTime = recipe.getCookTime();
			if (cookTime > 0) {
				int cookTimeSeconds = cookTime / 20;
				tooltipStrings.add(new TranslatableComponent("gui.jei.category.smelting.time.seconds", cookTimeSeconds));
			}
			float experience = recipe.getExperience();
			if (experience > 0) {
				tooltipStrings.add(new TranslatableComponent("gui.jei.category.smelting.experience", experience));
			}

			return tooltipStrings;
		}
		return Collections.emptyList();
	}
}
