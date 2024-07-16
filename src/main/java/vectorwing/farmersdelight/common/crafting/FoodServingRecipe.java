package vectorwing.farmersdelight.common.crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;

public class FoodServingRecipe extends CustomRecipe
{
	public FoodServingRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		ItemStack cookingPotStack = ItemStack.EMPTY;
		ItemStack containerStack = ItemStack.EMPTY;
		ItemStack secondStack = ItemStack.EMPTY;

		for (int index = 0; index < input.size(); ++index) {
			ItemStack selectedStack = input.getItem(index);
			if (!selectedStack.isEmpty()) {
				if (cookingPotStack.isEmpty()) {
					ItemStack mealStack = CookingPotBlockEntity.getMealFromItem(selectedStack);
					if (!mealStack.isEmpty()) {
						cookingPotStack = selectedStack;
						containerStack = CookingPotBlockEntity.getContainerFromItem(selectedStack);
						continue;
					}
				}
				if (secondStack.isEmpty()) {
					secondStack = selectedStack;
				} else {
					return false;
				}
			}
		}

		return !cookingPotStack.isEmpty() && !secondStack.isEmpty() && secondStack.is(containerStack.getItem());
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider access) {
		for (int i = 0; i < input.size(); ++i) {
			ItemStack selectedStack = input.getItem(i);
			if (!selectedStack.isEmpty() && selectedStack.is(ModItems.COOKING_POT.get())) {
				ItemStack resultStack = CookingPotBlockEntity.getMealFromItem(selectedStack).copy();
				resultStack.setCount(1);
				return resultStack;
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
		NonNullList<ItemStack> remainders = NonNullList.withSize(input.size(), ItemStack.EMPTY);

		for (int i = 0; i < remainders.size(); ++i) {
			ItemStack selectedStack = input.getItem(i);
			if (selectedStack.hasCraftingRemainingItem()) {
				remainders.set(i, selectedStack.getCraftingRemainingItem());
			} else if (selectedStack.is(ModItems.COOKING_POT.get())) {
				CookingPotBlockEntity.takeServingFromItem(selectedStack);
				ItemStack newCookingPotStack = selectedStack.copy();
				newCookingPotStack.setCount(1);
				remainders.set(i, newCookingPotStack);
				break;
			}
		}

		return remainders;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width >= 2 && height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.FOOD_SERVING.get();
	}
}
