package vectorwing.farmersdelight.client.gui;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.gui.screens.recipebook.SearchRecipeBookCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jspecify.annotations.NonNull;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.recipe.CookingPotRecipeDisplay;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeCategories;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

public class CookingPotRecipeBookComponent extends RecipeBookComponent<CookingPotMenu>
{

	protected static final WidgetSprites RECIPE_BOOK_BUTTONS = new WidgetSprites(
		Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_book/cooking_pot_enabled"),
		Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_book/cooking_pot_disabled"),
		Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_book/cooking_pot_enabled_highlighted"),
		Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_book/cooking_pot_disabled_highlighted"));
	private static final List<TabInfo> TABS = ModRecipeCategories.createCookingPotTabInfo();

	public CookingPotRecipeBookComponent(CookingPotMenu menu) {
		super(menu, TABS);
	}

	@Override
	protected WidgetSprites getFilterButtonTextures() {
		return RECIPE_BOOK_BUTTONS;
	}

	@Override
	protected boolean isCraftingSlot(Slot slot) {
		return switch (slot.index) {
			case 0, 1, 2, 3, 4, 5 -> true;
			default -> false;
		};
	}

	@Override
	protected void selectMatchingRecipes(RecipeCollection possibleRecipes, StackedItemContents stackedItemContents) {
		possibleRecipes.selectRecipes(stackedItemContents, recipeDisplay -> recipeDisplay instanceof CookingPotRecipeDisplay);
	}

	@Override
	@NonNull
	protected Component getRecipeFilterName() {
		return TextUtils.container("recipe_book.cookable");
	}

	@Override
	protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipeDisplay, ContextMap contextMap) {
		ghostSlots.setResult(this.menu.getSlot(6), contextMap, recipeDisplay.result());
		if (recipeDisplay instanceof CookingPotRecipeDisplay cookingPotRecipeDisplay) {
			for (int i = 0; i < cookingPotRecipeDisplay.ingredients().size(); ++i) {
				SlotDisplay display = cookingPotRecipeDisplay.ingredients().get(i);
				ghostSlots.setInput(menu.getSlot(i), contextMap, display);
			}

			if (menu.getSlot(7).getItem().isEmpty() && cookingPotRecipeDisplay.container().isPresent()) {
				ghostSlots.setInput(menu.getSlot(7), contextMap, cookingPotRecipeDisplay.container().get());
			}
		}
	}
}
