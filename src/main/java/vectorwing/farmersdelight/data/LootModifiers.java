package vectorwing.farmersdelight.data;

import net.minecraft.advancements.critereon.*;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.RiceBlock;
import vectorwing.farmersdelight.common.loot.modifier.AddItemModifier;
import vectorwing.farmersdelight.common.loot.modifier.AddLootTableModifier;
import vectorwing.farmersdelight.common.loot.modifier.PastrySlicingModifier;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModLootTables;
import vectorwing.farmersdelight.common.tag.ModTags;

public class LootModifiers extends GlobalLootModifierProvider
{
	public LootModifiers(PackOutput output) {
		super(output, FarmersDelight.MODID);
	}

	@Override
	protected void start() {
		//chest loot
		this.add("add_loot_abandoned_mineshaft", createNewPool(BuiltInLootTables.ABANDONED_MINESHAFT, ModLootTables.ABANDONED_MINESHAFT));
		this.add("add_loot_bastion_hoglin_stable", createNewPool(BuiltInLootTables.BASTION_HOGLIN_STABLE, ModLootTables.BASTION_HOGLIN_STABLE));
		this.add("add_loot_bastion_treasure", createNewPool(BuiltInLootTables.BASTION_TREASURE, ModLootTables.BASTION_TREASURE));
		this.add("add_loot_end_city_treasure", createNewPool(BuiltInLootTables.END_CITY_TREASURE, ModLootTables.END_CITY_TREASURE));
		this.add("add_loot_pillager_outpost", createNewPool(BuiltInLootTables.PILLAGER_OUTPOST, ModLootTables.PILLAGER_OUTPOST));
		this.add("add_loot_ruined_portal", createNewPool(BuiltInLootTables.RUINED_PORTAL, ModLootTables.RUINED_PORTAL));
		this.add("add_loot_shipwreck_supply", createNewPool(BuiltInLootTables.SHIPWRECK_SUPPLY, ModLootTables.SHIPWRECK_SUPPLY));
		this.add("add_loot_simple_dungeon", createNewPool(BuiltInLootTables.SIMPLE_DUNGEON, ModLootTables.SIMPLE_DUNGEON));
		this.add("add_loot_village_butcher", createNewPool(BuiltInLootTables.VILLAGE_BUTCHER, ModLootTables.VILLAGE_BUTCHER));
		this.add("add_loot_village_desert_house", createNewPool(BuiltInLootTables.VILLAGE_DESERT_HOUSE, ModLootTables.VILLAGE_DESERT_HOUSE));
		this.add("add_loot_village_plains_house", createNewPool(BuiltInLootTables.VILLAGE_PLAINS_HOUSE, ModLootTables.VILLAGE_PLAINS_HOUSE));
		this.add("add_loot_village_savanna_house", createNewPool(BuiltInLootTables.VILLAGE_SAVANNA_HOUSE, ModLootTables.VILLAGE_SAVANNA_HOUSE));
		this.add("add_loot_village_snowy_house", createNewPool(BuiltInLootTables.VILLAGE_SNOWY_HOUSE, ModLootTables.VILLAGE_SNOWY_HOUSE));
		this.add("add_loot_village_taiga_house", createNewPool(BuiltInLootTables.VILLAGE_TAIGA_HOUSE, ModLootTables.VILLAGE_TAIGA_HOUSE));

		//entity drops
		this.add("scavenging_feather", addItemWithKnifeKill(Items.FEATHER, EntityType.CHICKEN));
		this.add("scavenging_ham_from_hoglin", addItemWithKnifeKill(ModItems.HAM.get(), false, EntityType.HOGLIN));
		this.add("scavenging_ham_from_pig", addItemWithKnifeKill(ModItems.HAM.get(), false, EntityType.PIG));
		this.add("scavenging_leather", addItemWithKnifeKill(Items.LEATHER, EntityType.COW, EntityType.MOOSHROOM, EntityType.HORSE, EntityType.DONKEY, EntityType.MULE, EntityType.LLAMA, EntityType.TRADER_LLAMA));
		this.add("scavenging_rabbit_hide", addItemWithKnifeKill(Items.RABBIT_HIDE, EntityType.RABBIT));
		this.add("scavenging_shulker_shell", addItemWithKnifeKill(Items.SHULKER_SHELL, EntityType.SHULKER));
		this.add("scavenging_smoked_ham_from_hoglin", addItemWithKnifeKill(ModItems.HAM.get(), true, EntityType.HOGLIN));
		this.add("scavenging_smoked_ham_from_pig", addItemWithKnifeKill(ModItems.HAM.get(), true, EntityType.PIG));
		this.add("scavenging_string", addItemWithKnifeKill(Items.STRING, EntityType.SPIDER, EntityType.CAVE_SPIDER));

		//pastry slicing
		this.add("slicing_apple_pie", pastrySlicing(ModItems.APPLE_PIE_SLICE.get(), ModBlocks.APPLE_PIE.get()));
		this.add("slicing_cake", pastrySlicing(ModItems.CAKE_SLICE.get(), Blocks.CAKE));
		this.add("slicing_candle_cake", pastrySlicing(ModItems.CAKE_SLICE.get(), Blocks.CANDLE_CAKE, Blocks.WHITE_CANDLE_CAKE, Blocks.ORANGE_CANDLE_CAKE, Blocks.MAGENTA_CANDLE_CAKE, Blocks.LIGHT_BLUE_CANDLE_CAKE, Blocks.YELLOW_CANDLE_CAKE, Blocks.LIME_CANDLE_CAKE, Blocks.PINK_CANDLE_CAKE, Blocks.GRAY_CANDLE_CAKE, Blocks.LIGHT_GRAY_CANDLE_CAKE, Blocks.CYAN_CANDLE_CAKE, Blocks.PURPLE_CANDLE_CAKE, Blocks.BLUE_CANDLE_CAKE, Blocks.BROWN_CANDLE_CAKE, Blocks.GREEN_CANDLE_CAKE, Blocks.RED_CANDLE_CAKE, Blocks.BLACK_CANDLE_CAKE));
		this.add("slicing_chocolate_pie", pastrySlicing(ModItems.CHOCOLATE_PIE_SLICE.get(), ModBlocks.CHOCOLATE_PIE.get()));
		this.add("slicing_sweet_berry_cheesecake", pastrySlicing(ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get(), ModBlocks.SWEET_BERRY_CHEESECAKE.get()));

		//straw
		this.add("straw_from_grass", strawHarvesting(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS), 0.2F));
		this.add("straw_from_mature_rice", strawHarvesting(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.RICE_CROP.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(RiceBlock.AGE, 3)), 1.0F));
		this.add("straw_from_mature_wheat", strawHarvesting(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.WHEAT).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 7)), 1.0F));
		this.add("straw_from_sandy_shrub", strawHarvesting(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.SANDY_SHRUB.get()), 0.3F));
		this.add("straw_from_tall_grass", strawHarvesting(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.TALL_GRASS), 0.2F));
	}

	private AddLootTableModifier createNewPool(ResourceLocation lootToAddTo, ResourceLocation newPool) {
		return new AddLootTableModifier(new LootItemCondition[]{LootTableIdCondition.builder(lootToAddTo).build()}, newPool);
	}

	private AddItemModifier addItemWithKnifeKill(Item item, EntityType<?>... entity) {
		//make an array to hold all possible entities that the modifier applies to
		LootItemCondition.Builder[] condition = new LootItemCondition.Builder[entity.length];
		//add every entity we list. The list can be as long as we want it to be.
		for (int i = 0; i < entity.length; i++) {
			condition[i] = LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
					EntityPredicate.Builder.entity().of(entity[i]).build());
		}

		return new AddItemModifier(new LootItemCondition[]{
				LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.KILLER,
						EntityPredicate.Builder.entity().equipment(
										EntityEquipmentPredicate.Builder.equipment().mainhand(
														ItemPredicate.Builder.item().of(ModTags.KNIVES).build())
												.build())
								.build())
						.build(),
				AnyOfCondition.anyOf(condition).build()
		}, item, 1);
	}

	private AddItemModifier addItemWithKnifeKill(Item item, boolean fire, EntityType<?> entity) {
		return new AddItemModifier(new LootItemCondition[]{
				LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.KILLER,
						EntityPredicate.Builder.entity().equipment(
										EntityEquipmentPredicate.Builder.equipment().mainhand(
														ItemPredicate.Builder.item().of(ModTags.KNIVES).build())
												.build())
								.build())
						.build(),
				LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
						EntityPredicate.Builder.entity().of(entity).build())
						.build(),
				LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
						EntityPredicate.Builder.entity().flags(
										EntityFlagsPredicate.Builder.flags().setOnFire(fire).build())
								.build())
						.build()
		}, item, 1);
	}

	private PastrySlicingModifier pastrySlicing(Item receivedItem, Block... slicedBlock) {
		//make an array to hold all possible entities that the modifier applies to
		LootItemCondition.Builder[] condition = new LootItemCondition.Builder[slicedBlock.length];
		//add every block we list. The list can be as long as we want it to be.
		for (int i = 0; i < slicedBlock.length; i++) {
			condition[i] = LootItemBlockStatePropertyCondition.hasBlockStateProperties(slicedBlock[i]);
		}
		return new PastrySlicingModifier(new LootItemCondition[]{
				MatchTool.toolMatches(ItemPredicate.Builder.item().of(ModTags.KNIVES)).build(),
				AnyOfCondition.anyOf(condition).build()
		}, receivedItem);
	}

	private AddItemModifier strawHarvesting(LootItemBlockStatePropertyCondition.Builder slicedBlock, float chance) {
		//make an array to hold all possible entities that the modifier applies to
		return new AddItemModifier(new LootItemCondition[]{
				MatchTool.toolMatches(ItemPredicate.Builder.item().of(ModTags.KNIVES)).build(),
				LootItemRandomChanceCondition.randomChance(chance).build(), //TODO ask if looting should also be accounted for
				slicedBlock.build()
		}, ModItems.STRAW.get(), 1);
	}
}
