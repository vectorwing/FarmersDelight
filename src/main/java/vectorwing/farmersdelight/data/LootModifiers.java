package vectorwing.farmersdelight.data;

import net.minecraft.advancements.critereon.*;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.RicePaniclesBlock;
import vectorwing.farmersdelight.common.loot.modifier.AddItemModifier;
import vectorwing.farmersdelight.common.loot.modifier.AddLootTableModifier;
import vectorwing.farmersdelight.common.loot.modifier.PastrySlicingModifier;
import vectorwing.farmersdelight.common.loot.modifier.ReplaceItemModifier;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModChestLootTables;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.ArrayList;
import java.util.List;

public class LootModifiers extends GlobalLootModifierProvider {
	public LootModifiers(PackOutput output) {
		super(output, FarmersDelight.MODID);
	}

	@Override
	protected void start() {
		//chest loot
		this.add("add_loot_abandoned_mineshaft", this.createNewPool(BuiltInLootTables.ABANDONED_MINESHAFT, ModChestLootTables.ABANDONED_MINESHAFT));
		this.add("add_loot_bastion_hoglin_stable", this.createNewPool(BuiltInLootTables.BASTION_HOGLIN_STABLE, ModChestLootTables.BASTION_HOGLIN_STABLE));
		this.add("add_loot_bastion_treasure", this.createNewPool(BuiltInLootTables.BASTION_TREASURE, ModChestLootTables.BASTION_TREASURE));
		this.add("add_loot_end_city_treasure", this.createNewPool(BuiltInLootTables.END_CITY_TREASURE, ModChestLootTables.END_CITY_TREASURE));
		this.add("add_loot_pillager_outpost", this.createNewPool(BuiltInLootTables.PILLAGER_OUTPOST, ModChestLootTables.PILLAGER_OUTPOST));
		this.add("add_loot_ruined_portal", this.createNewPool(BuiltInLootTables.RUINED_PORTAL, ModChestLootTables.RUINED_PORTAL));
		this.add("add_loot_shipwreck_supply", this.createNewPool(BuiltInLootTables.SHIPWRECK_SUPPLY, ModChestLootTables.SHIPWRECK_SUPPLY));
		this.add("add_loot_simple_dungeon", this.createNewPool(BuiltInLootTables.SIMPLE_DUNGEON, ModChestLootTables.SIMPLE_DUNGEON));
		this.add("add_loot_village_butcher", this.createNewPool(BuiltInLootTables.VILLAGE_BUTCHER, ModChestLootTables.VILLAGE_BUTCHER));
		this.add("add_loot_village_desert_house", this.createNewPool(BuiltInLootTables.VILLAGE_DESERT_HOUSE, ModChestLootTables.VILLAGE_DESERT_HOUSE));
		this.add("add_loot_village_plains_house", this.createNewPool(BuiltInLootTables.VILLAGE_PLAINS_HOUSE, ModChestLootTables.VILLAGE_PLAINS_HOUSE));
		this.add("add_loot_village_savanna_house", this.createNewPool(BuiltInLootTables.VILLAGE_SAVANNA_HOUSE, ModChestLootTables.VILLAGE_SAVANNA_HOUSE));
		this.add("add_loot_village_snowy_house", this.createNewPool(BuiltInLootTables.VILLAGE_SNOWY_HOUSE, ModChestLootTables.VILLAGE_SNOWY_HOUSE));
		this.add("add_loot_village_taiga_house", this.createNewPool(BuiltInLootTables.VILLAGE_TAIGA_HOUSE, ModChestLootTables.VILLAGE_TAIGA_HOUSE));

		//entity drops
		this.add("scavenging_feather", this.addItemWithKnifeKill(Items.FEATHER, EntityType.CHICKEN));
		this.add("scavenging_ham_from_hoglin", this.addItemWithKnifeKill(ModItems.HAM.get(), false, 1.0F, EntityType.HOGLIN));
		this.add("scavenging_ham_from_pig", this.addItemWithKnifeKill(ModItems.HAM.get(), false, 0.5F, EntityType.PIG));
		this.add("scavenging_leather", this.addItemWithKnifeKill(Items.LEATHER, EntityType.COW, EntityType.MOOSHROOM, EntityType.HORSE, EntityType.DONKEY, EntityType.MULE, EntityType.LLAMA, EntityType.TRADER_LLAMA));
		this.add("scavenging_rabbit_hide", this.addItemWithKnifeKill(Items.RABBIT_HIDE, EntityType.RABBIT));
		this.add("scavenging_shulker_shell", this.addItemWithKnifeKill(Items.SHULKER_SHELL, EntityType.SHULKER));
		this.add("scavenging_smoked_ham_from_hoglin", this.addItemWithKnifeKill(ModItems.SMOKED_HAM.get(), true, 1.0F, EntityType.HOGLIN));
		this.add("scavenging_smoked_ham_from_pig", this.addItemWithKnifeKill(ModItems.SMOKED_HAM.get(), true, 0.5F, EntityType.PIG));
		this.add("scavenging_string", this.addItemWithKnifeKill(Items.STRING, EntityType.SPIDER, EntityType.CAVE_SPIDER));

		//block drops
		this.add("scavenging_pumpkin", new ReplaceItemModifier(new LootItemCondition[]{
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.PUMPKIN).build(),
				MatchTool.toolMatches(ItemPredicate.Builder.item().of(ModTags.Items.KNIVES))
						.and(MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.ANY))).invert()).build()
		}, Items.PUMPKIN, ModItems.PUMPKIN_SLICE.get(), 4));

		//pastry slicing
		this.add("slicing_apple_pie", this.pastrySlicing(ModItems.APPLE_PIE_SLICE.get(), ModBlocks.APPLE_PIE.get()));
		this.add("slicing_cake", this.pastrySlicing(ModItems.CAKE_SLICE.get(), Blocks.CAKE));
		this.add("slicing_chocolate_pie", this.pastrySlicing(ModItems.CHOCOLATE_PIE_SLICE.get(), ModBlocks.CHOCOLATE_PIE.get()));
		this.add("slicing_pumpkin_pie", this.pastrySlicing(ModItems.PUMPKIN_PIE_SLICE.get(), ModBlocks.PUMPKIN_PIE.get()));
		this.add("slicing_sweet_berry_cheesecake", this.pastrySlicing(ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get(), ModBlocks.SWEET_BERRY_CHEESECAKE.get()));
		this.add("slicing_candle_cake", this.candleCakeSlicing());

		//straw
		this.add("straw_from_grass", this.strawHarvesting(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS), 0.2F));
		this.add("straw_from_mature_rice", this.strawHarvesting(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.RICE_CROP_PANICLES.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(RicePaniclesBlock.RICE_AGE, 3)), 1.0F));
		this.add("straw_from_mature_wheat", this.strawHarvesting(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.WHEAT).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 7)), 1.0F));
		this.add("straw_from_sandy_shrub", this.strawHarvesting(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.SANDY_SHRUB.get()), 0.3F));
		this.add("straw_from_tall_grass", this.strawHarvesting(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.TALL_GRASS), 0.2F));
	}

	private AddLootTableModifier createNewPool(ResourceLocation lootToAddTo, ResourceLocation newPool) {
		return new AddLootTableModifier(new LootItemCondition[]{LootTableIdCondition.builder(lootToAddTo).build()}, newPool);
	}

	private AddItemModifier addItemWithKnifeKill(Item item, EntityType<?>... entity) {
		return this.addItemWithKnifeKill(item, null, 1.0F, entity);
	}

	private AddItemModifier addItemWithKnifeKill(Item item, Boolean onFire, float chance, EntityType<?>... entity) {
		//make an array to hold all possible entities that the modifier applies to
		LootItemCondition.Builder[] entityConditions = new LootItemCondition.Builder[entity.length];
		//add every entity we list. The list can be as long as we want it to be.
		for (int i = 0; i < entity.length; i++) {
			entityConditions[i] = LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
					EntityPredicate.Builder.entity().of(entity[i]).build());
		}
		List<LootItemCondition> conditions = new ArrayList<>();

		//check for knife kill
		conditions.add(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.KILLER, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().mainhand(ItemPredicate.Builder.item().of(ModTags.Items.KNIVES).build()).build()).build()).build());
		conditions.add(entityConditions.length > 1 ? AnyOfCondition.anyOf(entityConditions).build() : entityConditions[0].build());

		if (onFire != null) {
			conditions.add(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(onFire).build())).build());
		}

		if (chance < 1.0F) {
			conditions.add(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(chance, 0.1F).build());
		}

		return new AddItemModifier(conditions.toArray(LootItemCondition[]::new), item, 1);
	}

	private AddItemModifier candleCakeSlicing() {
		List<Block> cakes = List.of(Blocks.CANDLE_CAKE, Blocks.WHITE_CANDLE_CAKE, Blocks.ORANGE_CANDLE_CAKE, Blocks.MAGENTA_CANDLE_CAKE, Blocks.LIGHT_BLUE_CANDLE_CAKE, Blocks.YELLOW_CANDLE_CAKE, Blocks.LIME_CANDLE_CAKE, Blocks.PINK_CANDLE_CAKE, Blocks.GRAY_CANDLE_CAKE, Blocks.LIGHT_GRAY_CANDLE_CAKE, Blocks.CYAN_CANDLE_CAKE, Blocks.PURPLE_CANDLE_CAKE, Blocks.BLUE_CANDLE_CAKE, Blocks.BROWN_CANDLE_CAKE, Blocks.GREEN_CANDLE_CAKE, Blocks.RED_CANDLE_CAKE, Blocks.BLACK_CANDLE_CAKE);
		LootItemCondition.Builder[] conditions = new LootItemCondition.Builder[cakes.size()];
		//add every block we list. The list can be as long as we want it to be.
		for (int i = 0; i < cakes.size(); i++) {
			conditions[i] = LootItemBlockStatePropertyCondition.hasBlockStateProperties(cakes.get(i));
		}
		return new AddItemModifier(new LootItemCondition[]{
				MatchTool.toolMatches(ItemPredicate.Builder.item().of(ModTags.Items.KNIVES)).build(),
				AnyOfCondition.anyOf(conditions).build()
		}, ModItems.CAKE_SLICE.get(), 7);
	}

	private PastrySlicingModifier pastrySlicing(Item receivedItem, Block slicedBlock) {
		return new PastrySlicingModifier(new LootItemCondition[]{
				MatchTool.toolMatches(ItemPredicate.Builder.item().of(ModTags.Items.KNIVES)).build(),
				LootItemBlockStatePropertyCondition.hasBlockStateProperties(slicedBlock).build()
		}, receivedItem);
	}

	private AddItemModifier strawHarvesting(LootItemBlockStatePropertyCondition.Builder slicedBlock, float chance) {
		List<LootItemCondition> conditions = new ArrayList<>();

		conditions.add(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ModTags.Items.STRAW_HARVESTERS)).build());
		if (chance < 1.0F) {
			conditions.add(LootItemRandomChanceCondition.randomChance(chance).build());
		}
		conditions.add(slicedBlock.build());

		return new AddItemModifier(conditions.toArray(LootItemCondition[]::new), ModItems.STRAW.get(), 1);
	}
}
