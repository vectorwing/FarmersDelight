package vectorwing.farmersdelight.data.recipes;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;
import vectorwing.farmersdelight.items.KnifeItem;
import vectorwing.farmersdelight.registry.ModItems;
import vectorwing.farmersdelight.utils.tags.ForgeTags;

import java.util.function.Consumer;

public class CuttingRecipes
{
	public static void register(Consumer<IFinishedRecipe> consumer) {
		// Knife
		chopMeats(consumer);
		chopPlants(consumer);
		chopPastries(consumer);

		// Pickaxe
		salvageBricks(consumer);

		// Axe
		stripWood(consumer);
		salvageWoodenFurniture(consumer);

		// Shears
		salvageUsingShears(consumer);
	}

	private static void chopMeats(Consumer<IFinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.BEEF), KnifeItem.KNIFE_TOOL, ModItems.MINCED_BEEF.get(), 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.CHICKEN), KnifeItem.KNIFE_TOOL, ModItems.CHICKEN_CUTS.get(), 2)
				.addResult(Items.BONE_MEAL)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.COOKED_CHICKEN), KnifeItem.KNIFE_TOOL, ModItems.COOKED_CHICKEN_CUTS.get(), 2)
				.addResult(Items.BONE_MEAL)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.COD), KnifeItem.KNIFE_TOOL, ModItems.COD_SLICE.get(), 2)
				.addResult(Items.BONE_MEAL)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.COOKED_COD), KnifeItem.KNIFE_TOOL, ModItems.COOKED_COD_SLICE.get(), 2)
				.addResult(Items.BONE_MEAL)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.SALMON), KnifeItem.KNIFE_TOOL, ModItems.SALMON_SLICE.get(), 2)
				.addResult(Items.BONE_MEAL)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.COOKED_SALMON), KnifeItem.KNIFE_TOOL, ModItems.COOKED_SALMON_SLICE.get(), 2)
				.addResult(Items.BONE_MEAL)
				.build(consumer);
	}

	private static void chopPlants(Consumer<IFinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(ModItems.CABBAGE.get()), KnifeItem.KNIFE_TOOL, ModItems.CABBAGE_LEAF.get(), 2)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(ModItems.RICE_PANICLE.get()), KnifeItem.KNIFE_TOOL, ModItems.RICE.get(), 1)
				.addResult(ModItems.STRAW.get())
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.MELON), KnifeItem.KNIFE_TOOL, Items.MELON_SLICE, 9)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.PUMPKIN), KnifeItem.KNIFE_TOOL, ModItems.PUMPKIN_SLICE.get(), 4)
				.build(consumer);
	}

	private static void chopPastries(Consumer<IFinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.CAKE), KnifeItem.KNIFE_TOOL, ModItems.CAKE_SLICE.get(), 7)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(ModItems.APPLE_PIE.get()), KnifeItem.KNIFE_TOOL, ModItems.APPLE_PIE_SLICE.get(), 4)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(ModItems.SWEET_BERRY_CHEESECAKE.get()), KnifeItem.KNIFE_TOOL, ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get(), 4)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(ModItems.CHOCOLATE_PIE.get()), KnifeItem.KNIFE_TOOL, ModItems.CHOCOLATE_PIE_SLICE.get(), 4)
				.build(consumer);
	}

	private static void salvageBricks(Consumer<IFinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.BRICKS), ToolType.PICKAXE, Items.BRICK, 4)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.NETHER_BRICKS), ToolType.PICKAXE, Items.NETHER_BRICK, 4)
				.build(consumer);
	}

	private static void stripWood(Consumer<IFinishedRecipe> consumer) {
		// Oak
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.OAK_LOG), ToolType.AXE, Items.STRIPPED_OAK_LOG)
				.addResult(ModItems.TREE_BARK.get())
				.addSound(SoundEvents.ITEM_AXE_STRIP.getRegistryName().toString()).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.OAK_WOOD), ToolType.AXE, Items.STRIPPED_OAK_WOOD)
				.addResult(ModItems.TREE_BARK.get())
				.addSound(SoundEvents.ITEM_AXE_STRIP.getRegistryName().toString()).build(consumer);

		// Birch
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.BIRCH_LOG), ToolType.AXE, Items.STRIPPED_BIRCH_LOG)
				.addResult(ModItems.TREE_BARK.get())
				.addSound(SoundEvents.ITEM_AXE_STRIP.getRegistryName().toString()).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.BIRCH_WOOD), ToolType.AXE, Items.STRIPPED_BIRCH_WOOD)
				.addResult(ModItems.TREE_BARK.get())
				.addSound(SoundEvents.ITEM_AXE_STRIP.getRegistryName().toString()).build(consumer);

		// Spruce
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.SPRUCE_LOG), ToolType.AXE, Items.STRIPPED_SPRUCE_LOG)
				.addResult(ModItems.TREE_BARK.get())
				.addSound(SoundEvents.ITEM_AXE_STRIP.getRegistryName().toString()).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.SPRUCE_WOOD), ToolType.AXE, Items.STRIPPED_SPRUCE_WOOD)
				.addResult(ModItems.TREE_BARK.get())
				.addSound(SoundEvents.ITEM_AXE_STRIP.getRegistryName().toString()).build(consumer);

		// Jungle
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.JUNGLE_LOG), ToolType.AXE, Items.STRIPPED_JUNGLE_LOG)
				.addResult(ModItems.TREE_BARK.get())
				.addSound(SoundEvents.ITEM_AXE_STRIP.getRegistryName().toString()).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.JUNGLE_WOOD), ToolType.AXE, Items.STRIPPED_JUNGLE_WOOD)
				.addResult(ModItems.TREE_BARK.get())
				.addSound(SoundEvents.ITEM_AXE_STRIP.getRegistryName().toString()).build(consumer);

		// Acacia
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.ACACIA_LOG), ToolType.AXE, Items.STRIPPED_ACACIA_LOG)
				.addResult(ModItems.TREE_BARK.get())
				.addSound(SoundEvents.ITEM_AXE_STRIP.getRegistryName().toString()).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.ACACIA_WOOD), ToolType.AXE, Items.STRIPPED_ACACIA_WOOD)
				.addResult(ModItems.TREE_BARK.get())
				.addSound(SoundEvents.ITEM_AXE_STRIP.getRegistryName().toString()).build(consumer);

		// Dark Oak
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.DARK_OAK_LOG), ToolType.AXE, Items.STRIPPED_DARK_OAK_LOG)
				.addResult(ModItems.TREE_BARK.get())
				.addSound(SoundEvents.ITEM_AXE_STRIP.getRegistryName().toString()).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.DARK_OAK_WOOD), ToolType.AXE, Items.STRIPPED_DARK_OAK_WOOD)
				.addResult(ModItems.TREE_BARK.get())
				.addSound(SoundEvents.ITEM_AXE_STRIP.getRegistryName().toString()).build(consumer);

		// Dark Oak
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.CRIMSON_STEM), ToolType.AXE, Items.STRIPPED_CRIMSON_STEM)
				.addResult(ModItems.TREE_BARK.get())
				.addSound(SoundEvents.ITEM_AXE_STRIP.getRegistryName().toString()).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.CRIMSON_HYPHAE), ToolType.AXE, Items.STRIPPED_CRIMSON_HYPHAE)
				.addResult(ModItems.TREE_BARK.get())
				.addSound(SoundEvents.ITEM_AXE_STRIP.getRegistryName().toString()).build(consumer);

		// Dark Oak
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.WARPED_STEM), ToolType.AXE, Items.STRIPPED_WARPED_STEM)
				.addResult(ModItems.TREE_BARK.get())
				.addSound(SoundEvents.ITEM_AXE_STRIP.getRegistryName().toString()).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.WARPED_HYPHAE), ToolType.AXE, Items.STRIPPED_WARPED_HYPHAE)
				.addResult(ModItems.TREE_BARK.get())
				.addSound(SoundEvents.ITEM_AXE_STRIP.getRegistryName().toString()).build(consumer);
	}

	private static void salvageWoodenFurniture(Consumer<IFinishedRecipe> consumer) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.OAK_DOOR), ToolType.AXE, Items.OAK_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.OAK_TRAPDOOR), ToolType.AXE, Items.OAK_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.OAK_SIGN), ToolType.AXE, Items.OAK_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.BIRCH_DOOR), ToolType.AXE, Items.BIRCH_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.BIRCH_TRAPDOOR), ToolType.AXE, Items.BIRCH_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.BIRCH_SIGN), ToolType.AXE, Items.BIRCH_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.SPRUCE_DOOR), ToolType.AXE, Items.SPRUCE_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.SPRUCE_TRAPDOOR), ToolType.AXE, Items.SPRUCE_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.SPRUCE_SIGN), ToolType.AXE, Items.SPRUCE_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.JUNGLE_DOOR), ToolType.AXE, Items.JUNGLE_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.JUNGLE_TRAPDOOR), ToolType.AXE, Items.JUNGLE_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.JUNGLE_SIGN), ToolType.AXE, Items.JUNGLE_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.ACACIA_DOOR), ToolType.AXE, Items.ACACIA_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.ACACIA_TRAPDOOR), ToolType.AXE, Items.ACACIA_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.ACACIA_SIGN), ToolType.AXE, Items.ACACIA_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.DARK_OAK_DOOR), ToolType.AXE, Items.DARK_OAK_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.DARK_OAK_TRAPDOOR), ToolType.AXE, Items.DARK_OAK_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.DARK_OAK_SIGN), ToolType.AXE, Items.DARK_OAK_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.CRIMSON_DOOR), ToolType.AXE, Items.CRIMSON_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.CRIMSON_TRAPDOOR), ToolType.AXE, Items.CRIMSON_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.CRIMSON_SIGN), ToolType.AXE, Items.CRIMSON_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.WARPED_DOOR), ToolType.AXE, Items.WARPED_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.WARPED_TRAPDOOR), ToolType.AXE, Items.WARPED_PLANKS).build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.WARPED_SIGN), ToolType.AXE, Items.WARPED_PLANKS).build(consumer);
	}

	private static void salvageUsingShears(Consumer<IFinishedRecipe> consumer) {
		// Saddle
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.SADDLE), Ingredient.fromTag(Tags.Items.SHEARS), Items.LEATHER, 2)
				.addResult(Items.IRON_NUGGET, 2)
				.build(consumer);
		// Book
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.LEATHER_HELMET), Ingredient.fromTag(Tags.Items.SHEARS), Items.LEATHER, 1)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.LEATHER_CHESTPLATE), Ingredient.fromTag(Tags.Items.SHEARS), Items.LEATHER, 1)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.LEATHER_LEGGINGS), Ingredient.fromTag(Tags.Items.SHEARS), Items.LEATHER, 1)
				.build(consumer);
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.fromItems(Items.LEATHER_BOOTS), Ingredient.fromTag(Tags.Items.SHEARS), Items.LEATHER, 1)
				.build(consumer);
	}
}
