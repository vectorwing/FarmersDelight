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

		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModItems.IRON_KNIFE.get()),
				Items.IRON_NUGGET, 0.1F, 200)
				.addCriterion("has_iron_knife", InventoryChangeTrigger.Instance.forItems(ModItems.IRON_KNIFE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "iron_nugget_from_smelting_knife"));
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ModItems.GOLDEN_KNIFE.get()),
				Items.GOLD_NUGGET, 0.1F, 200)
				.addCriterion("has_golden_knife", InventoryChangeTrigger.Instance.forItems(ModItems.IRON_KNIFE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "gold_nugget_from_smelting_knife"));
		CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(ModItems.IRON_KNIFE.get()),
				Items.IRON_NUGGET, 0.1F, 100)
				.addCriterion("has_iron_knife", InventoryChangeTrigger.Instance.forItems(ModItems.IRON_KNIFE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "iron_nugget_from_blasting_knife"));
		CookingRecipeBuilder.blastingRecipe(Ingredient.fromItems(ModItems.GOLDEN_KNIFE.get()),
				Items.GOLD_NUGGET, 0.1F, 100)
				.addCriterion("has_golden_knife", InventoryChangeTrigger.Instance.forItems(ModItems.IRON_KNIFE.get()))
				.build(consumer, new ResourceLocation(FarmersDelight.MODID, "gold_nugget_from_blasting_knife"));
	}

	private static void foodSmeltingRecipes(String name, IItemProvider ingredient, IItemProvider result, float experience, Consumer<IFinishedRecipe> consumer) {
		String namePrefix = new ResourceLocation(FarmersDelight.MODID, name).toString();
		CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ingredient),
				result, experience, 200)
				.addCriterion(name, InventoryChangeTrigger.Instance.forItems(ingredient))
				.build(consumer);
		CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(ingredient),
				result, experience, 600, IRecipeSerializer.CAMPFIRE_COOKING)
				.addCriterion(name, InventoryChangeTrigger.Instance.forItems(ingredient))
				.build(consumer, namePrefix + "_from_campfire_cooking");
		CookingRecipeBuilder.cookingRecipe(Ingredient.fromItems(ingredient),
				result, experience, 100, IRecipeSerializer.SMOKING)
				.addCriterion(name, InventoryChangeTrigger.Instance.forItems(ingredient))
				.build(consumer, namePrefix + "_from_smoking");
	}
}
