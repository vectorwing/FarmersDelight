package vectorwing.farmersdelight.integration.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.utility.TextUtils;
import vectorwing.farmersdelight.integration.jei.FDRecipeTypes;
import vectorwing.farmersdelight.integration.jei.resource.CuttingBoardDrawable;
import vectorwing.farmersdelight.integration.jei.resource.DecompositionDummy;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CuttingRecipeCategory implements IRecipeCategory<CuttingBoardRecipe>
{
	public static final ResourceLocation UID = new ResourceLocation(FarmersDelight.MODID, "cutting");
	public static final int OUTPUT_GRID_X = 76;
	public static final int OUTPUT_GRID_Y = 10;
	public static final int SLOT_SPRITE_SIZE = 18;
	private final IDrawable slot;
	private final IDrawable slotChance;
	private final Component title;
	private final IDrawable background;
	private final IDrawable icon;
	private final CuttingBoardDrawable cuttingBoard;

	public CuttingRecipeCategory(IGuiHelper helper) {
		title = TextUtils.getTranslation("jei.cutting");
		ResourceLocation backgroundImage = new ResourceLocation(FarmersDelight.MODID, "textures/gui/jei/cutting_board.png");
		slot = helper.createDrawable(backgroundImage, 0, 58, 18, 18);
		slotChance = helper.createDrawable(backgroundImage, 18, 58, 18, 18);
		background = helper.createDrawable(backgroundImage, 0, 0, 117, 57);
		icon = helper.createDrawableIngredient(new ItemStack(ModItems.CUTTING_BOARD.get()));
		cuttingBoard = new CuttingBoardDrawable(() -> new ItemStack(ModBlocks.CUTTING_BOARD.get()));
	}

	@Override
	public ResourceLocation getUid() {
		return this.getRecipeType().getUid();
	}

	@Override
	public Class<? extends CuttingBoardRecipe> getRecipeClass() {
		return this.getRecipeType().getRecipeClass();
	}

	@Override
	public RecipeType<CuttingBoardRecipe> getRecipeType() {
		return FDRecipeTypes.CUTTING;
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
	public void setIngredients(CuttingBoardRecipe cuttingBoardRecipe, IIngredients ingredients) {
		ingredients.setInputIngredients(cuttingBoardRecipe.getIngredientsAndTool());
		ingredients.setOutputs(VanillaTypes.ITEM, cuttingBoardRecipe.getResults());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, CuttingBoardRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
		NonNullList<ChanceResult> recipeOutputs = recipe.getRollableResults();

		// Draw required tool
		itemStacks.init(0, true, 15, 7);
		itemStacks.set(0, Arrays.asList(recipe.getTool().getItems()));

		// Draw input
		itemStacks.init(1, true, 15, 26);
		itemStacks.set(1, Arrays.asList(recipe.getIngredients().get(0).getItems()));

		// Draw outputs
		int size = recipeOutputs.size();
		int centerX = size > 1 ? 0 : 9;
		int centerY = size > 2 ? 0 : 9;

		for (int i = 0; i < size; i++) {
			int xOffset = centerX + (i % 2 == 0 ? 0 : 19);
			int yOffset = centerY + ((i / 2) * 19);

			itemStacks.init(i + 2, false, OUTPUT_GRID_X + xOffset, OUTPUT_GRID_Y + yOffset);
			itemStacks.set(i + 2, recipeOutputs.get(i).getStack());
		}

		itemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			if (input || slotIndex < 2) {
				return;
			}
			ChanceResult output = recipeOutputs.get(slotIndex - 2);
			float chance = output.getChance();
			if (chance != 1)
				tooltip.add(1, TextUtils.getTranslation("jei.chance", chance < 0.01 ? "<1" : (int) (chance * 100))
						.withStyle(ChatFormatting.GOLD));
		});
	}

	@Override
	public void draw(CuttingBoardRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
		//cuttingBoard.draw(matrixStack, 15, 19);
		NonNullList<ChanceResult> recipeOutputs = recipe.getRollableResults();

		int size = recipe.getResults().size();
		int centerX = size > 1 ? 0 : 9;
		int centerY = size > 2 ? 0 : 9;

		for (int i = 0; i < size; i++) {
			int xOffset = centerX + (i % 2 == 0 ? 0 : 19);
			int yOffset = centerY + ((i / 2) * 19);

			if (recipeOutputs.get(i).getChance() != 1) {
				slotChance.draw(matrixStack, OUTPUT_GRID_X + xOffset, OUTPUT_GRID_Y + yOffset);
			} else {
				slot.draw(matrixStack, OUTPUT_GRID_X + xOffset, OUTPUT_GRID_Y + yOffset);
			}
		}
	}
}
