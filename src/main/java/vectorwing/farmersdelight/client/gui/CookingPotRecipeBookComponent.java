package vectorwing.farmersdelight.client.gui;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import vectorwing.farmersdelight.client.recipebook.RecipeCategories;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;

public class CookingPotRecipeBookComponent extends RecipeBookComponent<CookingPotMenu>
{
	private static final WidgetSprites FILTER_SPRITES = new WidgetSprites(
			Identifier.withDefaultNamespace("recipe_book/furnace_filter_enabled"),
			Identifier.withDefaultNamespace("recipe_book/furnace_filter_disabled"),
			Identifier.withDefaultNamespace("recipe_book/furnace_filter_enabled_highlighted"),
			Identifier.withDefaultNamespace("recipe_book/furnace_filter_disabled_highlighted")
	);
	private static final Component FILTER_NAME = Component.translatable("gui.recipebook.toggleRecipes.cookable");

	public CookingPotRecipeBookComponent(CookingPotMenu menu) {
		super(menu, RecipeCategories.cookingPotTabs());
	}

	@Override
	protected WidgetSprites getFilterButtonTextures() {
		return FILTER_SPRITES;
	}

	@Override
	protected boolean isCraftingSlot(Slot slot) {
		return slot.index >= 0 && slot.index < CookingPotMenu.INDEX_MEAL;
	}

	@Override
	protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipe, ContextMap context) {
		ghostSlots.setResult(this.menu.slots.get(CookingPotMenu.INDEX_OUTPUT), context, recipe.result());
		if (recipe instanceof ShapelessCraftingRecipeDisplay cookingRecipe) {
			for (int i = 0; i < cookingRecipe.ingredients().size() && i < CookingPotMenu.INDEX_MEAL; i++) {
				ghostSlots.setInput(this.menu.slots.get(i), context, cookingRecipe.ingredients().get(i));
			}
		}
	}

	@Override
	protected Component getRecipeFilterName() {
		return FILTER_NAME;
	}

	@Override
	protected void selectMatchingRecipes(RecipeCollection collection, StackedItemContents stackedContents) {
		collection.selectRecipes(stackedContents, display -> display instanceof ShapelessCraftingRecipeDisplay);
	}
}
