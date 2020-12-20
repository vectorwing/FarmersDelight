package vectorwing.farmersdelight.integration.jei.cutting;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.registry.ModItems;

import java.util.Arrays;

public class CuttingRecipeCategory implements IRecipeCategory<CuttingBoardRecipe>
{
	public static final ResourceLocation UID = new ResourceLocation(FarmersDelight.MODID, "cutting");
	private final String title;
	private final IDrawable background;
	private final IDrawable icon;
	private final CuttingBoardModel cuttingBoard;

	public CuttingRecipeCategory(IGuiHelper helper) {
		title = I18n.format(FarmersDelight.MODID + ".jei.cutting");
		ResourceLocation backgroundImage = new ResourceLocation(FarmersDelight.MODID, "textures/gui/jei/cutting_board.png");
		background = helper.createDrawable(backgroundImage, 0, 0, 117, 57);
		icon = helper.createDrawableIngredient(new ItemStack(ModItems.CUTTING_BOARD.get()));
		cuttingBoard = new CuttingBoardModel(() -> new ItemStack(ModBlocks.CUTTING_BOARD.get()));
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends CuttingBoardRecipe> getRecipeClass() {
		return CuttingBoardRecipe.class;
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
	public void setIngredients(CuttingBoardRecipe cuttingBoardRecipe, IIngredients ingredients) {
		ingredients.setInputIngredients(cuttingBoardRecipe.getIngredientsAndTool());
		ingredients.setOutputs(VanillaTypes.ITEM, cuttingBoardRecipe.getResults());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, CuttingBoardRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
		NonNullList<ItemStack> recipeOutputs = recipe.getResults();

		// Draw required tool
		itemStacks.init(0, true, 15, 7);
		itemStacks.set(0, Arrays.asList(recipe.getTool().getMatchingStacks()));

		// Draw input
		itemStacks.init(1, true, 15, 26);
		itemStacks.set(1, Arrays.asList(recipe.getIngredients().get(0).getMatchingStacks()));

		// Draw outputs
		int OUTPUT_GRID_X = 77;
		int OUTPUT_GRID_Y = 11;

		int borderSlotSize = 18;
		for (int row = 0; row < 2; ++row) {
			for (int column = 0; column < 2; ++column) {
				int inputIndex = row * 2 + column;
				if (inputIndex < recipeOutputs.size()) {
					itemStacks.init(inputIndex + 2, true, (column * borderSlotSize) + OUTPUT_GRID_X, (row * borderSlotSize) + OUTPUT_GRID_Y);
					itemStacks.set(inputIndex + 2, recipeOutputs.get(inputIndex));
				}
			}
		}
	}

	@Override
	public void draw(CuttingBoardRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		cuttingBoard.draw(matrixStack, 15, 19);
	}
}
