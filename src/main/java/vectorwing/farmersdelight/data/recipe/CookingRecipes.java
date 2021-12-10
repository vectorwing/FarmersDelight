package vectorwing.farmersdelight.data.recipe;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class CookingRecipes
{
	public static final int FAST_COOKING = 100;
	public static final int NORMAL_COOKING = 200;
	public static final int SLOW_COOKING = 400;

	public static void register(Consumer<FinishedRecipe> consumer) {
		cookMiscellaneous(consumer);
		cookMinecraftSoups(consumer);
		cookMeals(consumer);
	}

	private static void cookMiscellaneous(Consumer<FinishedRecipe> consumer) {
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.HOT_COCOA.get(), 1, NORMAL_COOKING, 0.35F)
				.addIngredient(ForgeTags.MILK)
				.addIngredient(Items.SUGAR)
				.addIngredient(Items.COCOA_BEANS)
				.addIngredient(Items.COCOA_BEANS)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.APPLE_CIDER.get(), 1, NORMAL_COOKING, 0.35F)
				.addIngredient(Items.APPLE)
				.addIngredient(Items.APPLE)
				.addIngredient(Items.SUGAR)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.TOMATO_SAUCE.get(), 1, FAST_COOKING, 0.1f)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.DOG_FOOD.get(), 1, NORMAL_COOKING, 0.35F)
				.addIngredient(Items.ROTTEN_FLESH)
				.addIngredient(Items.BONE_MEAL)
				.addIngredient(ModTags.WOLF_PREY)
				.addIngredient(ForgeTags.CROPS_RICE)
				.build(consumer);
	}

	private static void cookMinecraftSoups(Consumer<FinishedRecipe> consumer) {
		CookingPotRecipeBuilder.cookingPotRecipe(Items.MUSHROOM_STEW, 1, NORMAL_COOKING, 0.2F, Items.BOWL)
				.addIngredient(Items.BROWN_MUSHROOM)
				.addIngredient(Items.RED_MUSHROOM)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(Items.BEETROOT_SOUP, 1, NORMAL_COOKING, 0.2F, Items.BOWL)
				.addIngredient(Items.BEETROOT)
				.addIngredient(Items.BEETROOT)
				.addIngredient(Items.BEETROOT)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(Items.RABBIT_STEW, 1, NORMAL_COOKING, 0.35F, Items.BOWL)
				.addIngredient(Items.BAKED_POTATO)
				.addIngredient(Ingredient.of(Items.RABBIT, Items.COOKED_RABBIT))
				.addIngredient(Items.CARROT)
				.addIngredient(Ingredient.of(Items.BROWN_MUSHROOM, Items.RED_MUSHROOM))
				.build(consumer);
	}

	private static void cookMeals(Consumer<FinishedRecipe> consumer) {
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.BAKED_COD_STEW.get(), 1, NORMAL_COOKING, 0.35F)
				.addIngredient(ForgeTags.RAW_FISHES_COD)
				.addIngredient(Items.POTATO)
				.addIngredient(ForgeTags.EGGS)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.BEEF_STEW.get(), 1, NORMAL_COOKING, 0.35F)
				.addIngredient(ForgeTags.RAW_BEEF)
				.addIngredient(Items.CARROT)
				.addIngredient(Items.POTATO)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.CABBAGE_ROLLS.get(), 1, FAST_COOKING, 0.1F)
				.addIngredient(ForgeTags.CROPS_CABBAGE)
				.addIngredient(ModTags.CABBAGE_ROLL_INGREDIENTS)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.CHICKEN_SOUP.get(), 1, NORMAL_COOKING, 0.35F)
				.addIngredient(ForgeTags.RAW_CHICKEN)
				.addIngredient(Items.CARROT)
				.addIngredient(ForgeTags.SALAD_INGREDIENTS)
				.addIngredient(ForgeTags.VEGETABLES)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.COOKED_RICE.get(), 1, FAST_COOKING, 0.1F)
				.addIngredient(ForgeTags.CROPS_RICE)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.DUMPLINGS.get(), 2, NORMAL_COOKING, 0.35F)
				.addIngredient(ForgeTags.PASTA)
				.addIngredient(ForgeTags.CROPS_CABBAGE)
				.addIngredient(ForgeTags.CROPS_ONION)
				.addIngredient(Ingredient.fromValues(Stream.of(
						new Ingredient.TagValue(ForgeTags.RAW_CHICKEN),
						new Ingredient.TagValue(ForgeTags.RAW_PORK),
						new Ingredient.TagValue(ForgeTags.RAW_BEEF),
						new Ingredient.ItemValue(new ItemStack(Items.BROWN_MUSHROOM))
				)))
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.FISH_STEW.get(), 1, NORMAL_COOKING, 0.35F)
				.addIngredient(ForgeTags.RAW_FISHES)
				.addIngredient(ModItems.TOMATO_SAUCE.get())
				.addIngredient(ForgeTags.CROPS_ONION)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.FRIED_RICE.get(), 1, NORMAL_COOKING, 0.35F)
				.addIngredient(ForgeTags.CROPS_RICE)
				.addIngredient(ForgeTags.EGGS)
				.addIngredient(Items.CARROT)
				.addIngredient(ForgeTags.CROPS_ONION)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.NOODLE_SOUP.get(), 1, NORMAL_COOKING, 0.35F)
				.addIngredient(ForgeTags.PASTA)
				.addIngredient(ForgeTags.COOKED_EGGS)
				.addIngredient(Items.DRIED_KELP)
				.addIngredient(ForgeTags.RAW_PORK)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.PASTA_WITH_MEATBALLS.get(), 1, NORMAL_COOKING, 0.35F)
				.addIngredient(ModItems.MINCED_BEEF.get())
				.addIngredient(ForgeTags.PASTA)
				.addIngredient(ModItems.TOMATO_SAUCE.get())
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.PASTA_WITH_MUTTON_CHOP.get(), 1, NORMAL_COOKING, 0.35F)
				.addIngredient(ForgeTags.RAW_MUTTON)
				.addIngredient(ForgeTags.PASTA)
				.addIngredient(ModItems.TOMATO_SAUCE.get())
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.PUMPKIN_SOUP.get(), 1, NORMAL_COOKING, 0.35F)
				.addIngredient(ModItems.PUMPKIN_SLICE.get())
				.addIngredient(ForgeTags.SALAD_INGREDIENTS)
				.addIngredient(ForgeTags.RAW_PORK)
				.addIngredient(ForgeTags.MILK)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.RATATOUILLE.get(), 1, NORMAL_COOKING, 0.35F)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.addIngredient(ForgeTags.CROPS_ONION)
				.addIngredient(Items.BEETROOT)
				.addIngredient(ForgeTags.VEGETABLES)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.SQUID_INK_PASTA.get(), 1, NORMAL_COOKING, 0.35F)
				.addIngredient(ForgeTags.RAW_FISHES)
				.addIngredient(ForgeTags.PASTA)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.addIngredient(Items.INK_SAC)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.STUFFED_PUMPKIN_BLOCK.get(), 1, SLOW_COOKING, 0.5F, Items.PUMPKIN)
				.addIngredient(ForgeTags.CROPS_RICE)
				.addIngredient(ForgeTags.VEGETABLES)
				.addIngredient(ModItems.TOMATO_SAUCE.get())
				.addIngredient(Items.BAKED_POTATO)
				.addIngredient(ModItems.BROWN_MUSHROOM_COLONY.get())
				.addIngredient(Items.SWEET_BERRIES)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.VEGETABLE_NOODLES.get(), 1, NORMAL_COOKING, 0.35F)
				.addIngredient(Items.CARROT)
				.addIngredient(Items.BROWN_MUSHROOM)
				.addIngredient(ForgeTags.PASTA)
				.addIngredient(ForgeTags.SALAD_INGREDIENTS)
				.addIngredient(ForgeTags.VEGETABLES)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.VEGETABLE_SOUP.get(), 1, NORMAL_COOKING, 0.35F)
				.addIngredient(Items.CARROT)
				.addIngredient(Items.POTATO)
				.addIngredient(Items.BEETROOT)
				.addIngredient(ForgeTags.SALAD_INGREDIENTS)
				.build(consumer);
	}
}
