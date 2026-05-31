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
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nonnull;
import java.util.List;

public class CookingPotRecipeBookComponent extends RecipeBookComponent<CookingPotMenu>
{
	protected static final WidgetSprites RECIPE_BOOK_BUTTONS = new WidgetSprites(
			Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_book/cooking_pot_enabled"),
			Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_book/cooking_pot_disabled"),
			Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_book/cooking_pot_enabled_highlighted"),
			Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_book/cooking_pot_disabled_highlighted"));

	private static final List<RecipeBookComponent.TabInfo> TABS = List.of(
			new RecipeBookComponent.TabInfo(ModItems.COOKING_POT.get(), RecipeBookCategories.CRAFTING_MISC));

	public CookingPotRecipeBookComponent(CookingPotMenu menu) {
		super(menu, TABS);
	}

	public void hide() {
		this.setVisible(false);
	}

	@Override
	protected WidgetSprites getFilterButtonTextures() {
		return RECIPE_BOOK_BUTTONS;
	}

	@Override
	@Nonnull
	protected Component getRecipeFilterName() {
		return TextUtils.container("recipe_book.cookable");
	}

	@Override
	protected boolean isCraftingSlot(Slot slot) {
		// Ingredient grid (0-5) plus the meal-display slot that previews the result (6).
		return slot.index >= 0 && slot.index <= CookingPotMenu.INDEX_MEAL;
	}

	@Override
	protected void selectMatchingRecipes(RecipeCollection collection, StackedItemContents stackedContents) {
		collection.selectRecipes(stackedContents, display -> true);
	}

	@Override
	protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipe, ContextMap context) {
		// Preview the result in the meal-display slot, mirroring the legacy ghost recipe.
		// NOTE (26.1): filling the ingredient grid requires a Farmer's Delight cooking-pot
		// RecipeDisplay type (server-side, common/crafting) to expose its ingredients/container.
		// Until that exists, only the result preview is shown.
		ghostSlots.setResult(this.menu.slots.get(CookingPotMenu.INDEX_MEAL), context, recipe.result());
	}
}
