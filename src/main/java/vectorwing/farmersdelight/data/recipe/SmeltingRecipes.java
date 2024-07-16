package vectorwing.farmersdelight.data.recipe;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModItems;

public class SmeltingRecipes
{
	public static void register(RecipeOutput output) {
		foodSmeltingRecipes("fried_egg", Items.EGG, ModItems.FRIED_EGG.get(), 0.35F, output);
		foodSmeltingRecipes("beef_patty", ModItems.MINCED_BEEF.get(), ModItems.BEEF_PATTY.get(), 0.35F, output);
		foodSmeltingRecipes("cooked_chicken_cuts", ModItems.CHICKEN_CUTS.get(), ModItems.COOKED_CHICKEN_CUTS.get(), 0.35F, output);
		foodSmeltingRecipes("cooked_cod_slice", ModItems.COD_SLICE.get(), ModItems.COOKED_COD_SLICE.get(), 0.35F, output);
		foodSmeltingRecipes("cooked_salmon_slice", ModItems.SALMON_SLICE.get(), ModItems.COOKED_SALMON_SLICE.get(), 0.35F, output);
		foodSmeltingRecipes("cooked_bacon", ModItems.BACON.get(), ModItems.COOKED_BACON.get(), 0.35F, output);
		foodSmeltingRecipes("cooked_mutton_chops", ModItems.MUTTON_CHOPS.get(), ModItems.COOKED_MUTTON_CHOPS.get(), 0.35F, output);

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.WHEAT_DOUGH.get()), RecipeCategory.FOOD,
						Items.BREAD, 0.35F, 200)
				.unlockedBy("has_dough", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.WHEAT_DOUGH.get()))
				.save(output, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "bread").toString() + "_from_smelting");
		SimpleCookingRecipeBuilder.smoking(Ingredient.of(ModItems.WHEAT_DOUGH.get()), RecipeCategory.FOOD,
						Items.BREAD, 0.35F, 100)
				.unlockedBy("has_dough", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.WHEAT_DOUGH.get()))
				.save(output, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "bread").toString() + "_from_smoking");

		SimpleCookingRecipeBuilder.smoking(Ingredient.of(ModItems.HAM.get()), RecipeCategory.FOOD,
						ModItems.SMOKED_HAM.get(), 0.35F, 200)
				.unlockedBy("has_ham", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.HAM.get()))
				.save(output);

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.IRON_KNIFE.get()), RecipeCategory.MISC,
						Items.IRON_NUGGET, 0.1F, 200)
				.unlockedBy("has_iron_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.IRON_KNIFE.get()))
				.save(output, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "iron_nugget_from_smelting_knife"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.GOLDEN_KNIFE.get()), RecipeCategory.MISC,
						Items.GOLD_NUGGET, 0.1F, 200)
				.unlockedBy("has_golden_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GOLDEN_KNIFE.get()))
				.save(output, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "gold_nugget_from_smelting_knife"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(ModItems.IRON_KNIFE.get()), RecipeCategory.MISC,
						Items.IRON_NUGGET, 0.1F, 100)
				.unlockedBy("has_iron_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.IRON_KNIFE.get()))
				.save(output, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "iron_nugget_from_blasting_knife"));
		SimpleCookingRecipeBuilder.blasting(Ingredient.of(ModItems.GOLDEN_KNIFE.get()), RecipeCategory.MISC,
						Items.GOLD_NUGGET, 0.1F, 100)
				.unlockedBy("has_golden_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GOLDEN_KNIFE.get()))
				.save(output, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "gold_nugget_from_blasting_knife"));
	}

	private static void foodSmeltingRecipes(String name, ItemLike ingredient, ItemLike result, float experience, RecipeOutput output) {
		String namePrefix = ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, name).toString();
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(ingredient), RecipeCategory.FOOD, result, experience, 200)
				.unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(ingredient))
				.save(output);
		SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(ingredient), RecipeCategory.FOOD, result, experience, 600)
				.unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(ingredient))
				.save(output, namePrefix + "_from_campfire_cooking");
		SimpleCookingRecipeBuilder.smoking(Ingredient.of(ingredient), RecipeCategory.FOOD, result, experience, 100)
				.unlockedBy(name, InventoryChangeTrigger.TriggerInstance.hasItems(ingredient))
				.save(output, namePrefix + "_from_smoking");
	}
}
