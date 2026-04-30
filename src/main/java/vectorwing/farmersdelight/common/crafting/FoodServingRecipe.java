package vectorwing.farmersdelight.common.crafting;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.registry.ModItems;

public class FoodServingRecipe extends CustomRecipe
{
	public static final FoodServingRecipe INSTANCE = new FoodServingRecipe();
	public static final MapCodec<FoodServingRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
	public static final StreamCodec<RegistryFriendlyByteBuf, FoodServingRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
	public static final RecipeSerializer<FoodServingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

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
	public @NotNull ItemStack assemble(CraftingInput input) {
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
			ItemStackTemplate remainder = selectedStack.getCraftingRemainder();
			if (remainder != null) {
				remainders.set(i, remainder.create());
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

//	@Override
//	public boolean canCraftInDimensions(int width, int height) {
//		return width >= 2 && height >= 2;
//	}

	@Override
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return SERIALIZER;
	}
}
