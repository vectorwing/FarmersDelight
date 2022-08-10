package vectorwing.farmersdelight.data.recipe;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class CookingRecipes
{
	public static final int FAST_COOKING = 100;      // 5 seconds
	public static final int NORMAL_COOKING = 200;    // 10 seconds
	public static final int SLOW_COOKING = 400;      // 20 seconds

	public static final float SMALL_EXP = 0.35F;
	public static final float MEDIUM_EXP = 1.0F;
	public static final float LARGE_EXP = 2.0F;

	public static void register(Consumer<FinishedRecipe> consumer) {
		cookMiscellaneous(consumer);
		cookMinecraftSoups(consumer);
		cookMeals(consumer);
	}

	private static void cookMiscellaneous(Consumer<FinishedRecipe> consumer) {
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.HOT_COCOA.get(), 1, NORMAL_COOKING, MEDIUM_EXP)
				.addIngredient(ForgeTags.MILK)
				.addIngredient(Items.SUGAR)
				.addIngredient(Items.COCOA_BEANS)
				.addIngredient(Items.COCOA_BEANS)
				.unlockedByItems("has_cocoa_beans", Items.COCOA_BEANS)
				.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.APPLE_CIDER.get(), 1, NORMAL_COOKING, MEDIUM_EXP)
				.addIngredient(Items.APPLE)
				.addIngredient(Items.APPLE)
				.addIngredient(Items.SUGAR)
				.unlockedByItems("has_apple", Items.APPLE)
				.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.TOMATO_SAUCE.get(), 1, FAST_COOKING, SMALL_EXP)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.unlockedByItems("has_tomato", ModItems.TOMATO.get())
				.setRecipeBookTab(CookingPotRecipeBookTab.MISC)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.DOG_FOOD.get(), 1, NORMAL_COOKING, MEDIUM_EXP)
				.addIngredient(Items.ROTTEN_FLESH)
				.addIngredient(Items.BONE_MEAL)
				.addIngredient(ModTags.WOLF_PREY)
				.addIngredient(ForgeTags.CROPS_RICE)
				.unlockedByItems("has_rotten_flesh", Items.ROTTEN_FLESH)
				.setRecipeBookTab(CookingPotRecipeBookTab.MISC)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.GLOW_BERRY_CUSTARD.get(), 1, NORMAL_COOKING, MEDIUM_EXP)
				.addIngredient(Items.GLOW_BERRIES)
				.addIngredient(ForgeTags.MILK)
				.addIngredient(ForgeTags.EGGS)
				.addIngredient(Items.SUGAR)
				.build(consumer);
	}

	private static void cookMinecraftSoups(Consumer<FinishedRecipe> consumer) {
		CookingPotRecipeBuilder.cookingPotRecipe(Items.MUSHROOM_STEW, 1, NORMAL_COOKING, MEDIUM_EXP, Items.BOWL)
				.addIngredient(Items.BROWN_MUSHROOM)
				.addIngredient(Items.RED_MUSHROOM)
				.unlockedByItems("has_brown_mushroom", Blocks.BROWN_MUSHROOM)
				.unlockedByItems("has_red_mushroom", Blocks.RED_MUSHROOM)
				.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(Items.BEETROOT_SOUP, 1, NORMAL_COOKING, MEDIUM_EXP, Items.BOWL)
				.addIngredient(Items.BEETROOT)
				.addIngredient(Items.BEETROOT)
				.addIngredient(Items.BEETROOT)
				.unlockedByItems("has_beetroot", Items.BEETROOT)
				.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(Items.RABBIT_STEW, 1, NORMAL_COOKING, MEDIUM_EXP, Items.BOWL)
				.addIngredient(Items.BAKED_POTATO)
				.addIngredient(Ingredient.of(Items.RABBIT, Items.COOKED_RABBIT))
				.addIngredient(Items.CARROT)
				.addIngredient(Ingredient.of(Items.BROWN_MUSHROOM, Items.RED_MUSHROOM))
				.unlockedByItems("has_raw_rabbit", Items.RABBIT)
				.unlockedByItems("has_cooked_rabbit", Items.COOKED_RABBIT)
				.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
				.build(consumer);
	}

	private static void cookMeals(Consumer<FinishedRecipe> consumer) {
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.BAKED_COD_STEW.get(), 1, NORMAL_COOKING, MEDIUM_EXP)
				.addIngredient(ForgeTags.RAW_FISHES_COD)
				.addIngredient(Items.POTATO)
				.addIngredient(ForgeTags.EGGS)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.unlockedByItems("has_cod", Items.COD)
				.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.BEEF_STEW.get(), 1, NORMAL_COOKING, MEDIUM_EXP)
				.addIngredient(ForgeTags.RAW_BEEF)
				.addIngredient(Items.CARROT)
				.addIngredient(Items.POTATO)
				.unlockedByItems("has_beef", Items.BEEF)
				.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.BONE_BROTH.get(), 1, NORMAL_COOKING, SMALL_EXP)
				.addIngredient(Tags.Items.BONES)
				.addIngredient(Ingredient.fromValues(Stream.of(
						new Ingredient.ItemValue(new ItemStack(Items.GLOW_BERRIES)),
						new Ingredient.TagValue(Tags.Items.MUSHROOMS),
						new Ingredient.ItemValue(new ItemStack(Items.HANGING_ROOTS)),
						new Ingredient.ItemValue(new ItemStack(Items.GLOW_LICHEN))
				)))
				.unlockedByItems("has_bone", Items.BONE)
				.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.CABBAGE_ROLLS.get(), 1, FAST_COOKING, SMALL_EXP)
				.addIngredient(ForgeTags.CROPS_CABBAGE)
				.addIngredient(ModTags.CABBAGE_ROLL_INGREDIENTS)
				.unlockedByItems("has_cabbage", ModItems.CABBAGE.get())
				.setRecipeBookTab(CookingPotRecipeBookTab.MISC)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.CHICKEN_SOUP.get(), 1, NORMAL_COOKING, MEDIUM_EXP)
				.addIngredient(ForgeTags.RAW_CHICKEN)
				.addIngredient(Items.CARROT)
				.addIngredient(ForgeTags.SALAD_INGREDIENTS)
				.addIngredient(ForgeTags.VEGETABLES)
				.unlockedByItems("has_chicken", Items.CHICKEN)
				.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.COOKED_RICE.get(), 1, FAST_COOKING, SMALL_EXP)
				.addIngredient(ForgeTags.CROPS_RICE)
				.unlockedByItems("has_rice", ModItems.RICE.get())
				.setRecipeBookTab(CookingPotRecipeBookTab.MISC)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.DUMPLINGS.get(), 2, NORMAL_COOKING, MEDIUM_EXP)
				.addIngredient(ForgeTags.DOUGH)
				.addIngredient(ForgeTags.CROPS_CABBAGE)
				.addIngredient(ForgeTags.CROPS_ONION)
				.addIngredient(Ingredient.fromValues(Stream.of(
						new Ingredient.TagValue(ForgeTags.RAW_CHICKEN),
						new Ingredient.TagValue(ForgeTags.RAW_PORK),
						new Ingredient.TagValue(ForgeTags.RAW_BEEF),
						new Ingredient.ItemValue(new ItemStack(Items.BROWN_MUSHROOM))
				)))
				.unlockedByItems("has_wheat_dough", ModItems.WHEAT_DOUGH.get())
				.setRecipeBookTab(CookingPotRecipeBookTab.MISC)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.FISH_STEW.get(), 1, NORMAL_COOKING, MEDIUM_EXP)
				.addIngredient(ForgeTags.RAW_FISHES)
				.addIngredient(ModItems.TOMATO_SAUCE.get())
				.addIngredient(ForgeTags.CROPS_ONION)
				.unlockedByItems("has_tomato_sauce", ModItems.TOMATO_SAUCE.get())
				.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.FRIED_RICE.get(), 1, NORMAL_COOKING, MEDIUM_EXP)
				.addIngredient(ForgeTags.CROPS_RICE)
				.addIngredient(ForgeTags.EGGS)
				.addIngredient(Items.CARROT)
				.addIngredient(ForgeTags.CROPS_ONION)
				.unlockedByItems("has_rice", ModItems.RICE.get())
				.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.MUSHROOM_RICE.get(), 1, NORMAL_COOKING, MEDIUM_EXP)
				.addIngredient(Items.BROWN_MUSHROOM)
				.addIngredient(Items.RED_MUSHROOM)
				.addIngredient(ForgeTags.CROPS_RICE)
				.addIngredient(Ingredient.of(Items.CARROT, Items.POTATO))
				.unlockedByItems("has_rice", ModItems.RICE.get())
				.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.NOODLE_SOUP.get(), 1, NORMAL_COOKING, MEDIUM_EXP)
				.addIngredient(ForgeTags.PASTA)
				.addIngredient(ForgeTags.COOKED_EGGS)
				.addIngredient(Items.DRIED_KELP)
				.addIngredient(ForgeTags.RAW_PORK)
				.unlockedByItems("has_raw_pasta", ModItems.RAW_PASTA.get())
				.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.PASTA_WITH_MEATBALLS.get(), 1, NORMAL_COOKING, MEDIUM_EXP)
				.addIngredient(ModItems.MINCED_BEEF.get())
				.addIngredient(ForgeTags.PASTA)
				.addIngredient(ModItems.TOMATO_SAUCE.get())
				.unlockedByItems("has_raw_pasta", ModItems.RAW_PASTA.get())
				.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.PASTA_WITH_MUTTON_CHOP.get(), 1, NORMAL_COOKING, MEDIUM_EXP)
				.addIngredient(ForgeTags.RAW_MUTTON)
				.addIngredient(ForgeTags.PASTA)
				.addIngredient(ModItems.TOMATO_SAUCE.get())
				.unlockedByItems("has_raw_pasta", ModItems.RAW_PASTA.get())
				.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.PUMPKIN_SOUP.get(), 1, NORMAL_COOKING, MEDIUM_EXP)
				.addIngredient(ModItems.PUMPKIN_SLICE.get())
				.addIngredient(ForgeTags.SALAD_INGREDIENTS)
				.addIngredient(ForgeTags.RAW_PORK)
				.addIngredient(ForgeTags.MILK)
				.unlockedByItems("has_pumpkin", Items.PUMPKIN)
				.unlockedByItems("has_pumpkin_slice", ModItems.PUMPKIN_SLICE.get())
				.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.RATATOUILLE.get(), 1, NORMAL_COOKING, MEDIUM_EXP)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.addIngredient(ForgeTags.CROPS_ONION)
				.addIngredient(Items.BEETROOT)
				.addIngredient(ForgeTags.VEGETABLES)
				.unlockedByItems("has_tomato", ModItems.TOMATO.get())
				.unlockedByItems("has_onion", ModItems.ONION.get())
				.unlockedByItems("has_beetroot", Items.BEETROOT)
				.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.SQUID_INK_PASTA.get(), 1, NORMAL_COOKING, MEDIUM_EXP)
				.addIngredient(ForgeTags.RAW_FISHES)
				.addIngredient(ForgeTags.PASTA)
				.addIngredient(ForgeTags.CROPS_TOMATO)
				.addIngredient(Items.INK_SAC)
				.unlockedByItems("has_raw_pasta", ModItems.RAW_PASTA.get())
				.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.STUFFED_PUMPKIN_BLOCK.get(), 1, SLOW_COOKING, LARGE_EXP, Items.PUMPKIN)
				.addIngredient(ForgeTags.CROPS_RICE)
				.addIngredient(ForgeTags.VEGETABLES)
				.addIngredient(ModItems.TOMATO_SAUCE.get())
				.addIngredient(Items.BAKED_POTATO)
				.addIngredient(ModItems.BROWN_MUSHROOM_COLONY.get())
				.addIngredient(Items.SWEET_BERRIES)
				.unlockedByItems("has_pumpkin", Blocks.PUMPKIN)
				.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.VEGETABLE_NOODLES.get(), 1, NORMAL_COOKING, MEDIUM_EXP)
				.addIngredient(Items.CARROT)
				.addIngredient(Items.BROWN_MUSHROOM)
				.addIngredient(ForgeTags.PASTA)
				.addIngredient(ForgeTags.SALAD_INGREDIENTS)
				.addIngredient(ForgeTags.VEGETABLES)
				.unlockedByItems("has_raw_pasta", ModItems.RAW_PASTA.get())
				.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
				.build(consumer);
		CookingPotRecipeBuilder.cookingPotRecipe(ModItems.VEGETABLE_SOUP.get(), 1, NORMAL_COOKING, MEDIUM_EXP)
				.addIngredient(Items.CARROT)
				.addIngredient(Items.POTATO)
				.addIngredient(Items.BEETROOT)
				.addIngredient(ForgeTags.SALAD_INGREDIENTS)
				.unlockedByItems("has_carrot", Items.CARROT)
				.unlockedByItems("has_onion", ModItems.ONION.get())
				.unlockedByItems("has_beetroot", Items.BEETROOT)
				.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
				.build(consumer);
	}
}
