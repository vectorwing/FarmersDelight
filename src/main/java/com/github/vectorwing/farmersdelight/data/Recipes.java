package com.github.vectorwing.farmersdelight.data;

import com.github.vectorwing.farmersdelight.init.BlockInit;
import com.github.vectorwing.farmersdelight.init.ItemInit;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider
{
	public Recipes(DataGenerator generator)
	{
		super(generator);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		// SHAPED
		ShapedRecipeBuilder.shapedRecipe(BlockInit.STOVE.get())
				.patternLine("iii")
				.patternLine("B B")
				.patternLine("BCB")
				.key('i', Items.IRON_INGOT)
				.key('B', Blocks.BRICKS)
				.key('C', Blocks.CAMPFIRE)
				.setGroup("farmersdelight")
				.addCriterion("campfire", InventoryChangeTrigger.Instance.forItems(Blocks.CAMPFIRE))
				.build(consumer);

		// SHAPELESS
		ShapelessRecipeBuilder.shapelessRecipe(ItemInit.CABBAGE_SEEDS.get())
				.addIngredient(ItemInit.CABBAGE.get())
				.setGroup("farmersdelight")
				.addCriterion("cabbage", InventoryChangeTrigger.Instance.forItems(ItemInit.CABBAGE.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ItemInit.TOMATO_SEEDS.get())
				.addIngredient(ItemInit.TOMATO.get())
				.setGroup("farmersdelight")
				.addCriterion("cabbage", InventoryChangeTrigger.Instance.forItems(ItemInit.TOMATO.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ItemInit.ONION_SEEDS.get())
				.addIngredient(ItemInit.ONION.get())
				.setGroup("farmersdelight")
				.addCriterion("cabbage", InventoryChangeTrigger.Instance.forItems(ItemInit.ONION.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ItemInit.TOMATO_SAUCE.get())
				.addIngredient(ItemInit.TOMATO.get())
				.addIngredient(ItemInit.TOMATO.get())
				.addIngredient(Items.BOWL)
				.setGroup("farmersdelight")
				.addCriterion("tomato", InventoryChangeTrigger.Instance.forItems(ItemInit.TOMATO.get()))
				.build(consumer);
		ShapelessRecipeBuilder.shapelessRecipe(ItemInit.MILK_BOTTLE.get())
				.addIngredient(Items.MILK_BUCKET)
				.addIngredient(Items.GLASS_BOTTLE)
				.addIngredient(Items.GLASS_BOTTLE)
				.addIngredient(Items.GLASS_BOTTLE)
				.setGroup("farmersdelight")
				.addCriterion("milk_bucket", InventoryChangeTrigger.Instance.forItems(Items.MILK_BUCKET))
				.build(consumer);
	}
}
