package vectorwing.farmersdelight.common.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;
import vectorwing.farmersdelight.common.tag.CommonTags;

public class DoughRecipe extends CustomRecipe
{
	public DoughRecipe(ResourceLocation id, CraftingBookCategory category) {
		super(id, category);
	}

	@Override
	public boolean matches(CraftingContainer container, Level level) {
		ItemStack wheatStack = ItemStack.EMPTY;
		ItemStack waterStack = ItemStack.EMPTY;

		for (int index = 0; index < container.getContainerSize(); ++index) {
			ItemStack selectedStack = container.getItem(index);
			if (!selectedStack.isEmpty()) {
				if (selectedStack.is(Items.WHEAT)) {
					if (!wheatStack.isEmpty()) return false;
					wheatStack = selectedStack;
				} else {
					if (!selectedStack.is(CommonTags.Items.BUCKETS_WATER)) {
						return false;
					}
					waterStack = selectedStack;
				}
			}
		}

		return !wheatStack.isEmpty() && !waterStack.isEmpty();
	}

	@Override
	public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
		return new ItemStack(ModItems.WHEAT_DOUGH.get());
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
		NonNullList<ItemStack> remainders = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);

		for (int index = 0; index < remainders.size(); ++index) {
			ItemStack selectedStack = container.getItem(index);
			if (selectedStack.is(CommonTags.Items.BUCKETS_WATER)) {
				remainders.set(index, selectedStack.copy());
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
		return ModRecipeSerializers.DOUGH.get();
	}
}
