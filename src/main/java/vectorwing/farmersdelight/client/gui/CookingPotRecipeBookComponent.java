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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import org.jspecify.annotations.NonNull;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.recipebook.RecipeCategories;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeBookCategories;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;
import java.util.Optional;

public class CookingPotRecipeBookComponent extends RecipeBookComponent<CookingPotMenu>
{
	protected static final WidgetSprites RECIPE_BOOK_BUTTONS = new WidgetSprites(
			Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_book/cooking_pot_enabled"),
			Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_book/cooking_pot_disabled"),
			Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_book/cooking_pot_enabled_highlighted"),
			Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_book/cooking_pot_disabled_highlighted"));

	private static final List<TabInfo> TABS = List.of(
			new TabInfo(new ItemStack(Items.COMPASS), Optional.empty(), RecipeCategories.COOKING_SEARCH),
			new TabInfo(ModItems.VEGETABLE_NOODLES.get(), ModRecipeBookCategories.COOKING_MEALS.get()),
			new TabInfo(ModItems.APPLE_CIDER.get(), ModRecipeBookCategories.COOKING_DRINKS.get()),
			new TabInfo(ModItems.DUMPLINGS.get(), ModItems.TOMATO_SAUCE.get(), ModRecipeBookCategories.COOKING_MISC.get()));

	public CookingPotRecipeBookComponent(CookingPotMenu menu) {
		super(menu, TABS);
	}

	@Override
	protected WidgetSprites getFilterButtonTextures() {
		return RECIPE_BOOK_BUTTONS;
	}

	@Override
	protected boolean isCraftingSlot(Slot slot) {
		return slot.index >= 0 && slot.index <= 5;
	}

	@Override
	protected void selectMatchingRecipes(RecipeCollection possibleRecipes, StackedItemContents stackedItemContents) {
		possibleRecipes.selectRecipes(stackedItemContents, recipeDisplay -> true);
	}

	@Override
	@NonNull
	protected Component getRecipeFilterName() {
		return TextUtils.container("recipe_book.cookable");
	}

	@Override
	protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipeDisplay, ContextMap contextMap) {
		// TODO 26.1: populate cooking-pot ghost slots once the custom recipe display is registered.
	}
}
