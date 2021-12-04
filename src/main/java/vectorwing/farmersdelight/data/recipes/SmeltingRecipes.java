package vectorwing.farmersdelight.data.recipes;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.registry.ModItems;

import java.util.function.Consumer;

public class SmeltingRecipes
{
	public static void register(Consumer<IFinishedRecipe> consumer) {
		foodSmeltingRecipes("fried_egg", Items.EGG, ModItems.FRIED_EGG.get(), 0.35F, consumer);
		foodSmeltingRecipes("beef_patty", ModItems.MINCED_BEEF.get(), ModItems.BEEF_PATTY.get(), 0.35F, consumer);
		foodSmeltingRecipes("cooked_chicken_cuts", ModItems.CHICKEN_CUTS.get(), ModItems.COOKED_CHICKEN_CUTS.get(), 0.35F, consumer);
		foodSmeltingRecipes("cooked_cod_slice", ModItems.COD_SLICE.get(), ModItems.COOKED_COD_SLICE.get(), 0.35F, consumer);
		foodSmeltingRecipes("cooked_salmon_slice", ModItems.SALMON_SLICE.get(), ModItems.COOKED_SALMON_SLICE.get(), 0.35F, consumer);
		foodSmeltingRecipes("cooked_bacon", ModItems.BACON.get(), ModItems.COOKED_BACON.get(), 0.35F, consumer);
		foodSmeltingRecipes("cooked_mutton_chops", ModItems.MUTTON_CHOPS.get(), ModItems.COOKED_MUTTON_CHOPS.get(), 0.35F, consumer);

		CookingRecipeBuilder.smelting(Ingredient.of(ModItems.WHEAT_DOUGH.get()),
				Items.BREAD, 0.35F, 200)
				.unlockedBy("has_dough", InventoryChangeTrigger.Instance.hasItems(ModItems.WHEAT_DOUGH.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "bread").toString() + "_from_smelting");
		CookingRecipeBuilder.cooking(Ingredient.of(ModItems.WHEAT_DOUGH.get()),
				Items.BREAD, 0.35F, 100, IRecipeSerializer.SMOKING_RECIPE)
				.unlockedBy("has_dough", InventoryChangeTrigger.Instance.hasItems(ModItems.WHEAT_DOUGH.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "bread").toString() + "_from_smoking");

		CookingRecipeBuilder.cooking(Ingredient.of(ModItems.HAM.get()),
				ModItems.SMOKED_HAM.get(), 0.35F, 200, IRecipeSerializer.SMOKING_RECIPE)
				.unlockedBy("has_ham", InventoryChangeTrigger.Instance.hasItems(ModItems.HAM.get()))
				.save(consumer);

		CookingRecipeBuilder.smelting(Ingredient.of(ModItems.IRON_KNIFE.get()),
				Items.IRON_NUGGET, 0.1F, 200)
				.unlockedBy("has_iron_knife", InventoryChangeTrigger.Instance.hasItems(ModItems.IRON_KNIFE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "iron_nugget_from_smelting_knife"));
		CookingRecipeBuilder.smelting(Ingredient.of(ModItems.GOLDEN_KNIFE.get()),
				Items.GOLD_NUGGET, 0.1F, 200)
				.unlockedBy("has_golden_knife", InventoryChangeTrigger.Instance.hasItems(ModItems.IRON_KNIFE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "gold_nugget_from_smelting_knife"));
		CookingRecipeBuilder.blasting(Ingredient.of(ModItems.IRON_KNIFE.get()),
				Items.IRON_NUGGET, 0.1F, 100)
				.unlockedBy("has_iron_knife", InventoryChangeTrigger.Instance.hasItems(ModItems.IRON_KNIFE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "iron_nugget_from_blasting_knife"));
		CookingRecipeBuilder.blasting(Ingredient.of(ModItems.GOLDEN_KNIFE.get()),
				Items.GOLD_NUGGET, 0.1F, 100)
				.unlockedBy("has_golden_knife", InventoryChangeTrigger.Instance.hasItems(ModItems.IRON_KNIFE.get()))
				.save(consumer, new ResourceLocation(FarmersDelight.MODID, "gold_nugget_from_blasting_knife"));
	}

	private static void foodSmeltingRecipes(String name, IItemProvider ingredient, IItemProvider result, float experience, Consumer<IFinishedRecipe> consumer) {
		String namePrefix = new ResourceLocation(FarmersDelight.MODID, name).toString();
		CookingRecipeBuilder.smelting(Ingredient.of(ingredient),
				result, experience, 200)
				.unlockedBy(name, InventoryChangeTrigger.Instance.hasItems(ingredient))
				.save(consumer);
		CookingRecipeBuilder.cooking(Ingredient.of(ingredient),
				result, experience, 600, IRecipeSerializer.CAMPFIRE_COOKING_RECIPE)
				.unlockedBy(name, InventoryChangeTrigger.Instance.hasItems(ingredient))
				.save(consumer, namePrefix + "_from_campfire_cooking");
		CookingRecipeBuilder.cooking(Ingredient.of(ingredient),
				result, experience, 100, IRecipeSerializer.SMOKING_RECIPE)
				.unlockedBy(name, InventoryChangeTrigger.Instance.hasItems(ingredient))
				.save(consumer, namePrefix + "_from_smoking");
	}
}
