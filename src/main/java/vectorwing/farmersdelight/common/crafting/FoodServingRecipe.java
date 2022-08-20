package vectorwing.farmersdelight.common.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;

public class FoodServingRecipe extends CustomRecipe
{
	public FoodServingRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		ItemStack cookingPotStack = ItemStack.EMPTY;
		ItemStack containerStack = ItemStack.EMPTY;

		boolean hasFilledCookingPot = false;
		boolean hasContainer = false;

		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack selectedStack = container.getItem(i);
			if (!selectedStack.isEmpty() && selectedStack.is(ModItems.COOKING_POT.get())) {
				if (hasFilledCookingPot) {
					return false;
				}
				ItemStack mealStack = CookingPotBlockEntity.getMealFromItem(selectedStack);
				if (mealStack.isEmpty()) {
					return false;
				}
				cookingPotStack = selectedStack;
				containerStack = CookingPotBlockEntity.getContainerFromItem(selectedStack);
				hasFilledCookingPot = true;
			}
		}

		for (int j = 0; j < container.getContainerSize(); ++j) {
			ItemStack selectedStack = container.getItem(j);
			if (!selectedStack.isEmpty()) {
				if (selectedStack.is(containerStack.getItem()) && !hasContainer) {
					hasContainer = true;
				} else if (!selectedStack.equals(cookingPotStack)) {
					return false;
				}
			}
		}

		return hasFilledCookingPot && hasContainer;
	}

	@Override
	public ItemStack assemble(CraftingContainer container) {
		for (int i = 0; i < container.getContainerSize(); ++i) {
			ItemStack selectedStack = container.getItem(i);
			if (!selectedStack.isEmpty() && selectedStack.is(ModItems.COOKING_POT.get())) {
				ItemStack resultStack = CookingPotBlockEntity.getMealFromItem(selectedStack).copy();
				resultStack.setCount(1);
				return resultStack;
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		NonNullList<ItemStack> remainders = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);

		for (int i = 0; i < remainders.size(); ++i) {
			ItemStack selectedStack = container.getItem(i);
			if (selectedStack.hasContainerItem()) {
				remainders.set(i, selectedStack.getContainerItem());
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
