package vectorwing.farmersdelight;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import vectorwing.farmersdelight.init.BlockInit;
import vectorwing.farmersdelight.init.ItemInit;

public class CreativeTab extends ItemGroup
{
	public CreativeTab(String label)
	{
		super(label);
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(BlockInit.COOKING_POT.get());
	}

	@Override
	public void fill(NonNullList<ItemStack> items) {
		registerWorkstations(items);
		registerTools(items);
		registerCrops(items);
		registerIngredients(items);
		registerMeals(items);
	}

	private void registerWorkstations(NonNullList<ItemStack> items) {
		items.add(new ItemStack(BlockInit.STOVE.get()));
		items.add(new ItemStack(BlockInit.COOKING_POT.get()));
		items.add(new ItemStack(BlockInit.BASKET.get()));
	}
	private void registerCrops(NonNullList<ItemStack> items) {
		items.add(new ItemStack(ItemInit.CABBAGE.get()));
		items.add(new ItemStack(ItemInit.TOMATO.get()));
		items.add(new ItemStack(ItemInit.ONION.get()));
		items.add(new ItemStack(ItemInit.RICE.get()));
		items.add(new ItemStack(ItemInit.CABBAGE_SEEDS.get()));
		items.add(new ItemStack(ItemInit.TOMATO_SEEDS.get()));
	}
	private void registerIngredients(NonNullList<ItemStack> items) {
		items.add(new ItemStack(ItemInit.FRIED_EGG.get()));
		items.add(new ItemStack(ItemInit.MILK_BOTTLE.get()));
		items.add(new ItemStack(ItemInit.TOMATO_SAUCE.get()));
		items.add(new ItemStack(ItemInit.RAW_PASTA.get()));
		items.add(new ItemStack(ItemInit.CAKE_SLICE.get()));
	}
	private void registerTools(NonNullList<ItemStack> items) {
		items.add(new ItemStack(ItemInit.FLINT_KNIFE.get()));
		items.add(new ItemStack(ItemInit.IRON_KNIFE.get()));
		items.add(new ItemStack(ItemInit.DIAMOND_KNIFE.get()));
		items.add(new ItemStack(ItemInit.GOLDEN_KNIFE.get()));
	}
	private void registerMeals(NonNullList<ItemStack> items) {
		items.add(new ItemStack(ItemInit.MIXED_SALAD.get()));
		items.add(new ItemStack(ItemInit.BARBECUE_STICK.get()));
		items.add(new ItemStack(ItemInit.CHICKEN_SANDWICH.get()));
		items.add(new ItemStack(ItemInit.HAMBURGER.get()));
		items.add(new ItemStack(ItemInit.BEEF_STEW.get()));
		items.add(new ItemStack(ItemInit.CHICKEN_SOUP.get()));
		items.add(new ItemStack(ItemInit.FISH_STEW.get()));
		items.add(new ItemStack(ItemInit.FRIED_RICE.get()));
		items.add(new ItemStack(ItemInit.HONEY_GLAZED_HAM.get()));
		items.add(new ItemStack(ItemInit.PASTA_WITH_MEATBALLS.get()));
		items.add(new ItemStack(ItemInit.STEAK_AND_POTATOES.get()));
		items.add(new ItemStack(ItemInit.VEGETABLE_NOODLES.get()));
	}
}
