package vectorwing.farmersdelight.data.recipe;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.function.Consumer;

public class SmeltingRecipes
{
	public static void register(Consumer<FinishedRecipe> consumer) {
		foodSmeltingRecipes("fried_egg", Items.EGG, ModItems.FRIED_EGG.get(), 0.35F, consumer);
		foodSmeltingRecipes("beef_patty", ModItems.MINCED_BEEF.get(), ModItems.BEEF_PATTY.get(), 0.35F, consumer);
		foodSmeltingRecipes("cooked_chicken_cuts", ModItems.CHICKEN_CUTS.get(), ModItems.COOKED_CHICKEN_CUTS.get(), 0.35F, consumer);
		foodSmeltingRecipes("cooked_cod_slice", ModItems.COD_SLICE.get(), ModItems.COOKED_COD_SLICE.get(), 0.35F, consumer);
		foodSmeltingRecipes("cooked_salmon_slice", ModItems.SALMON_SLICE.get(), ModItems.COOKED_SALMON_SLICE.get(), 0.35F, consumer);
		foodSmeltingRecipes("cooked_bacon", ModItems.BACON.get(), ModItems.COOKED_BACON.get(), 0.35F, consumer);
		foodSmeltingRecipes("cooked_mutton_chops", ModItems.MUTTON_CHOPS.get(), ModItems.COOKED_MUTTON_CHOPS.get(), 0.35F, consumer);

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.WHEAT_DOUGH.get()),
						Items.BREAD, 0.35F, 200)
				.unlockedBy("has_dough", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.WHEAT_DOUGH.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "bread").toString() + "_from_smelting");
		SimpleCookingRecipeBuilder.cooking(Ingredient.of(ModItems.WHEAT_DOUGH.get()),
						Items.BREAD, 0.35F, 100, RecipeSerializer.SMOKING_RECIPE)
				.unlockedBy("has_dough", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.WHEAT_DOUGH.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "bread").toString() + "_from_smoking");

		SimpleCookingRecipeBuilder.cooking(Ingredient.of(ModItems.HAM.get()),
						ModItems.SMOKED_HAM.get(), 0.35F, 200, RecipeSerializer.SMOKING_RECIPE)
				.unlockedBy("has_ham", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.HAM.get()))
				.save(consumer);

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.IRON_KNIFE.get()),
						Items.IRON_NUGGET, 0.1F, 200)
				.unlockedBy("has_iron_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.IRON_KNIFE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "iron_nugget_from_smelting_knife"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.GOLDEN_KNIFE.get()),
						Items.GOLD_NUGGET, 0.1F, 200)
				.unlockedBy("has_golden_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.IRON_KNIFE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "gold_nugget_from_smelting_knife"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(ModItems.IRON_KNIFE.get()),
						Items.IRON_NUGGET, 0.1F, 100)
				.unlockedBy("has_iron_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.IRON_KNIFE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "iron_nugget_from_blasting_knife"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(ModItems.GOLDEN_KNIFE.get()),
						Items.GOLD_NUGGET, 0.1F, 100)
				.unlockedBy("has_golden_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.IRON_KNIFE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "gold_nugget_from_blasting_knife"));
	}

	private static void foodSmeltingRecipes(String name, ItemLike ingredient, ItemLike result, float experience, Consumer<FinishedRecipe> consumer) {
		String namePrefix = new ResourceLocation(FarmersDelight.MODID, name).toString();
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ingredient),
						result, experience, 200)
				.unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(ingredient))
				.save(consumer);
		SimpleCookingRecipeBuilder.cooking(Ingredient.of(ingredient),
						result, experience, 600, RecipeSerializer.CAMPFIRE_COOKING_RECIPE)
				.unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(ingredient))
				.save(consumer, namePrefix + "_from_campfire_cooking");
		SimpleCookingRecipeBuilder.cooking(Ingredient.of(ingredient),
						result, experience, 100, RecipeSerializer.SMOKING_RECIPE)
				.unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(ingredient))
				.save(consumer, namePrefix + "_from_smoking");
	}
}
