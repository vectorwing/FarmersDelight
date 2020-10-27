package vectorwing.farmersdelight;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.registry.ModItems;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public class FDItemGroup extends ItemGroup {
	public FDItemGroup(String label) {
		super(label);
	}

	@Nonnull
	@Override
	public ItemStack createIcon() {
		return new ItemStack(ModBlocks.STOVE.get());
	}

//	@Override
//	public void fill(NonNullList<ItemStack> items)
//	{
//		registerWorkstations(items);
//		registerTools(items);
//		registerCrops(items);
//		registerMaterials(items);
//		registerIngredients(items);
//		registerMeals(items);
//		registerPetMeals(items);
//	}

	private void registerWorkstations(NonNullList<ItemStack> items) {
		items.add(new ItemStack(ModBlocks.STOVE.get()));
		items.add(new ItemStack(ModBlocks.COOKING_POT.get()));
		items.add(new ItemStack(ModBlocks.BASKET.get()));
		items.add(new ItemStack(ModBlocks.ROPE.get()));
		items.add(new ItemStack(ModBlocks.SAFETY_NET.get()));
	}

	private void registerCrops(NonNullList<ItemStack> items) {
		items.add(new ItemStack(ModItems.WILD_CABBAGES.get()));
		items.add(new ItemStack(ModItems.WILD_ONIONS.get()));
		items.add(new ItemStack(ModItems.WILD_TOMATOES.get()));
		items.add(new ItemStack(ModItems.WILD_CARROTS.get()));
		items.add(new ItemStack(ModItems.WILD_POTATOES.get()));
		items.add(new ItemStack(ModItems.WILD_BEETROOTS.get()));
		items.add(new ItemStack(ModItems.CABBAGE.get()));
		items.add(new ItemStack(ModItems.TOMATO.get()));
		items.add(new ItemStack(ModItems.ONION.get()));
		items.add(new ItemStack(ModItems.RICE.get()));
		items.add(new ItemStack(ModItems.CABBAGE_SEEDS.get()));
		items.add(new ItemStack(ModItems.TOMATO_SEEDS.get()));
	}

	private void registerIngredients(NonNullList<ItemStack> items) {
		items.add(new ItemStack(ModItems.FRIED_EGG.get()));
		items.add(new ItemStack(ModItems.MILK_BOTTLE.get()));
		items.add(new ItemStack(ModItems.TOMATO_SAUCE.get()));
		items.add(new ItemStack(ModItems.RAW_PASTA.get()));
		items.add(new ItemStack(ModItems.CAKE_SLICE.get()));
		items.add(new ItemStack(ModItems.SWEET_BERRY_COOKIE.get()));
		items.add(new ItemStack(ModItems.HONEY_COOKIE.get()));
	}

	private void registerTools(NonNullList<ItemStack> items) {
		items.add(new ItemStack(ModItems.FLINT_KNIFE.get()));
		items.add(new ItemStack(ModItems.IRON_KNIFE.get()));
		items.add(new ItemStack(ModItems.DIAMOND_KNIFE.get()));
		items.add(new ItemStack(ModItems.GOLDEN_KNIFE.get()));
	}

	private void registerMaterials(NonNullList<ItemStack> items) {
		items.add(new ItemStack(ModItems.STRAW.get()));
		items.add(new ItemStack(ModItems.CANVAS.get()));
	}

	private void registerMeals(NonNullList<ItemStack> items) {
		items.add(new ItemStack(ModItems.MIXED_SALAD.get()));
		items.add(new ItemStack(ModItems.BARBECUE_STICK.get()));
		items.add(new ItemStack(ModItems.CHICKEN_SANDWICH.get()));
		items.add(new ItemStack(ModItems.HAMBURGER.get()));
		items.add(new ItemStack(ModItems.BEEF_STEW.get()));
		items.add(new ItemStack(ModItems.CHICKEN_SOUP.get()));
		items.add(new ItemStack(ModItems.VEGETABLE_SOUP.get()));
		items.add(new ItemStack(ModItems.FISH_STEW.get()));
		items.add(new ItemStack(ModItems.FRIED_RICE.get()));
		items.add(new ItemStack(ModItems.HONEY_GLAZED_HAM.get()));
		items.add(new ItemStack(ModItems.PASTA_WITH_MEATBALLS.get()));
		items.add(new ItemStack(ModItems.PASTA_WITH_MUTTON_CHOP.get()));
		items.add(new ItemStack(ModItems.VEGETABLE_NOODLES.get()));
		items.add(new ItemStack(ModItems.STEAK_AND_POTATOES.get()));
		items.add(new ItemStack(ModItems.SHEPHERDS_PIE.get()));
	}

	private void registerPetMeals(NonNullList<ItemStack> items) {
		items.add(new ItemStack(ModItems.DOG_FOOD.get()));
	}
}
