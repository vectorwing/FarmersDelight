package vectorwing.farmersdelight.data.recipes;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;
import vectorwing.farmersdelight.registry.ModItems;
import vectorwing.farmersdelight.utils.tags.ForgeTags;
import vectorwing.farmersdelight.utils.tags.ModTags;

import java.util.function.Consumer;

public class CookingRecipes
{
	public static final int FAST_COOKING_TIME = 100;

	public static void register(Consumer<IFinishedRecipe> consumer) {
		cookMiscellaneous(consumer);
		cookMinecraftSoups(consumer);
		cookMeals(consumer);
	}
	private static void cookMiscellaneous(Consumer<IFinishedRecipe> consumer) {
		CookingPotRecipeBuilder.cookingRecipe(ModItems.HOT_COCOA.get(), 1)
				.addIngredient(ForgeTags.MILK)
				.addIngredient(Items.SUGAR)
				.addIngredient(Items.COCOA_BEANS)
				.addIngredient(Items.COCOA_BEANS)
				.build(consumer);
		CookingPotRecipeBuilder.cookingRecipe(ModItems.TOMATO_SAUCE.get(), 1, FAST_COOKING_TIME)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.build(consumer);
		CookingPotRecipeBuilder.cookingRecipe(ModItems.DOG_FOOD.get(), 1)
				.addIngredient(Items.ROTTEN_FLESH)
				.addIngredient(Items.BONE_MEAL)
				.addIngredient(ModTags.WOLF_PREY)
				.addIngredient(ForgeTags.CROPS_RICE)
				.build(consumer);
	}

	private static void cookMinecraftSoups(Consumer<IFinishedRecipe> consumer) {
		CookingPotRecipeBuilder.cookingRecipe(Items.MUSHROOM_STEW, 1, Items.BOWL)
				.addIngredient(Items.BROWN_MUSHROOM)
				.addIngredient(Items.RED_MUSHROOM)
				.build(consumer);
		CookingPotRecipeBuilder.cookingRecipe(Items.BEETROOT_SOUP, 1, Items.BOWL)
				.addIngredient(Items.BEETROOT)
				.addIngredient(Items.BEETROOT)
				.addIngredient(Items.BEETROOT)
				.build(consumer);
		CookingPotRecipeBuilder.cookingRecipe(Items.RABBIT_STEW, 1, Items.BOWL)
				.addIngredient(Items.BAKED_POTATO)
				.addIngredient(Ingredient.fromItems(Items.RABBIT, Items.COOKED_RABBIT))
				.addIngredient(Items.CARROT)
				.addIngredient(Ingredient.fromItems(Items.BROWN_MUSHROOM, Items.RED_MUSHROOM))
				.build(consumer);
	}

	private static void cookMeals(Consumer<IFinishedRecipe> consumer) {
		CookingPotRecipeBuilder.cookingRecipe(ModItems.BAKED_COD_STEW.get(), 1)
				.addIngredient(ForgeTags.RAW_FISHES_COD)
				.addIngredient(Items.POTATO)
				.addIngredient(ForgeTags.EGGS)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.build(consumer);
		CookingPotRecipeBuilder.cookingRecipe(ModItems.BEEF_STEW.get(), 1)
				.addIngredient(ForgeTags.RAW_BEEF)
				.addIngredient(Items.CARROT)
				.addIngredient(Items.POTATO)
				.build(consumer);
		CookingPotRecipeBuilder.cookingRecipe(ModItems.CHICKEN_SOUP.get(), 1)
				.addIngredient(ForgeTags.RAW_CHICKEN)
				.addIngredient(Items.CARROT)
				.addIngredient(ForgeTags.SALAD_INGREDIENTS)
				.addIngredient(ForgeTags.VEGETABLES)
				.build(consumer);
		CookingPotRecipeBuilder.cookingRecipe(ModItems.COOKED_RICE.get(), 1, FAST_COOKING_TIME)
				.addIngredient(ForgeTags.CROPS_RICE)
				.build(consumer);
		// TODO: Figure out how to make an Ingredient out of both items and tags.
//		CookingPotRecipeBuilder.cookingRecipe(ModItems.DUMPLINGS.get(), 2)
//				.addIngredient(ForgeTags.PASTA)
//				.addIngredient(ForgeTags.CROPS_CABBAGE)
//				.addIngredient(ForgeTags.CROPS_ONION)
//				.addIngredient(Ingredient.fromItems(
//						Items.BROWN_MUSHROOM,
//						ForgeTags.RAW_CHICKEN.getAllElements().,
//						Items.PORKCHOP
//				))
//				.build(consumer);
		CookingPotRecipeBuilder.cookingRecipe(ModItems.FISH_STEW.get(), 1)
				.addIngredient(ForgeTags.RAW_FISHES)
				.addIngredient(ModItems.TOMATO_SAUCE.get())
				.addIngredient(ForgeTags.CROPS_ONION)
				.build(consumer);
		CookingPotRecipeBuilder.cookingRecipe(ModItems.FRIED_RICE.get(), 1)
				.addIngredient(ForgeTags.CROPS_RICE)
				.addIngredient(ForgeTags.EGGS)
				.addIngredient(Items.CARROT)
				.addIngredient(ForgeTags.CROPS_ONION)
				.build(consumer);
		CookingPotRecipeBuilder.cookingRecipe(ModItems.GRILLED_SALMON.get(), 1)
				.addIngredient(ForgeTags.COOKED_FISHES_SALMON)
				.addIngredient(Items.SWEET_BERRIES)
				.addIngredient(ForgeTags.CROPS_CABBAGE)
				.addIngredient(ForgeTags.CROPS_ONION)
				.build(consumer);
		CookingPotRecipeBuilder.cookingRecipe(ModItems.HONEY_GLAZED_HAM.get(), 1)
				.addIngredient(Items.PORKCHOP)
				.addIngredient(Items.HONEY_BOTTLE)
				.addIngredient(Items.SWEET_BERRIES)
				.addIngredient(ForgeTags.CROPS_RICE)
				.build(consumer);
		CookingPotRecipeBuilder.cookingRecipe(ModItems.PASTA_WITH_MEATBALLS.get(), 1)
				.addIngredient(ModItems.MINCED_BEEF.get())
				.addIngredient(ForgeTags.PASTA)
				.addIngredient(ModItems.TOMATO_SAUCE.get())
				.build(consumer);
		CookingPotRecipeBuilder.cookingRecipe(ModItems.PASTA_WITH_MUTTON_CHOP.get(), 1)
				.addIngredient(Items.MUTTON)
				.addIngredient(ForgeTags.PASTA)
				.addIngredient(ModItems.TOMATO_SAUCE.get())
				.build(consumer);
		CookingPotRecipeBuilder.cookingRecipe(ModItems.PUMPKIN_SOUP.get(), 1)
				.addIngredient(ModItems.PUMPKIN_SLICE.get())
				.addIngredient(ForgeTags.SALAD_INGREDIENTS)
				.addIngredient(Items.PORKCHOP)
				.addIngredient(ForgeTags.MILK)
				.build(consumer);
		CookingPotRecipeBuilder.cookingRecipe(ModItems.RATATOUILLE.get(), 1)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.addIngredient(ForgeTags.CROPS_ONION)
				.addIngredient(Items.BEETROOT)
				.addIngredient(ForgeTags.VEGETABLES)
				.build(consumer);
		CookingPotRecipeBuilder.cookingRecipe(ModItems.SHEPHERDS_PIE.get(), 1)
				.addIngredient(Items.MUTTON)
				.addIngredient(Items.BAKED_POTATO)
				.addIngredient(ForgeTags.CROPS_ONION)
				.addIngredient(ForgeTags.MILK)
				.build(consumer);
		CookingPotRecipeBuilder.cookingRecipe(ModItems.SQUID_INK_PASTA.get(), 1)
				.addIngredient(ForgeTags.RAW_FISHES)
				.addIngredient(ForgeTags.PASTA)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.addIngredient(Items.INK_SAC)
				.build(consumer);
		CookingPotRecipeBuilder.cookingRecipe(ModItems.STUFFED_PUMPKIN_BLOCK.get(), 1, Items.PUMPKIN)
				.addIngredient(ForgeTags.CROPS_RICE)
				.addIngredient(ForgeTags.VEGETABLES)
				.addIngredient(ModItems.TOMATO_SAUCE.get())
				.addIngredient(Items.BAKED_POTATO)
				.addIngredient(ModItems.BROWN_MUSHROOM_COLONY.get())
				.addIngredient(Items.SWEET_BERRIES)
				.build(consumer);
		CookingPotRecipeBuilder.cookingRecipe(ModItems.VEGETABLE_NOODLES.get(), 1)
				.addIngredient(Items.CARROT)
				.addIngredient(Items.BROWN_MUSHROOM)
				.addIngredient(ForgeTags.PASTA)
				.addIngredient(ForgeTags.SALAD_INGREDIENTS)
				.addIngredient(ForgeTags.VEGETABLES)
				.build(consumer);
		CookingPotRecipeBuilder.cookingRecipe(ModItems.VEGETABLE_SOUP.get(), 1)
				.addIngredient(Items.CARROT)
				.addIngredient(Items.POTATO)
				.addIngredient(Items.BEETROOT)
				.addIngredient(ForgeTags.SALAD_INGREDIENTS)
				.build(consumer);
	}
}
