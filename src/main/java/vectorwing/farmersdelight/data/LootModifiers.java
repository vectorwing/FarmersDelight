package vectorwing.farmersdelight.data;

import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.RicePaniclesBlock;
import vectorwing.farmersdelight.common.loot.modifier.AddItemModifier;
import vectorwing.farmersdelight.common.loot.modifier.PastrySlicingModifier;
import vectorwing.farmersdelight.common.loot.modifier.ReplaceItemModifier;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModChestLootTables;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LootModifiers extends GlobalLootModifierProvider
{
	public LootModifiers(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries, FarmersDelight.MODID);
	}

	@Override
	protected void start() {
		// Chest Loot
		this.add("add_loot_abandoned_mineshaft", this.addNewLootPool(BuiltInLootTables.ABANDONED_MINESHAFT, ModChestLootTables.ABANDONED_MINESHAFT));
		this.add("add_loot_bastion_hoglin_stable", this.addNewLootPool(BuiltInLootTables.BASTION_HOGLIN_STABLE, ModChestLootTables.BASTION_HOGLIN_STABLE));
		this.add("add_loot_bastion_treasure", this.addNewLootPool(BuiltInLootTables.BASTION_TREASURE, ModChestLootTables.BASTION_TREASURE));
		this.add("add_loot_end_city_treasure", this.addNewLootPool(BuiltInLootTables.END_CITY_TREASURE, ModChestLootTables.END_CITY_TREASURE));
		this.add("add_loot_pillager_outpost", this.addNewLootPool(BuiltInLootTables.PILLAGER_OUTPOST, ModChestLootTables.PILLAGER_OUTPOST));
		this.add("add_loot_ruined_portal", this.addNewLootPool(BuiltInLootTables.RUINED_PORTAL, ModChestLootTables.RUINED_PORTAL));
		this.add("add_loot_shipwreck_supply", this.addNewLootPool(BuiltInLootTables.SHIPWRECK_SUPPLY, ModChestLootTables.SHIPWRECK_SUPPLY));
		this.add("add_loot_simple_dungeon", this.addNewLootPool(BuiltInLootTables.SIMPLE_DUNGEON, ModChestLootTables.SIMPLE_DUNGEON));
		this.add("add_loot_village_butcher", this.addNewLootPool(BuiltInLootTables.VILLAGE_BUTCHER, ModChestLootTables.VILLAGE_BUTCHER));
		this.add("add_loot_village_desert_house", this.addNewLootPool(BuiltInLootTables.VILLAGE_DESERT_HOUSE, ModChestLootTables.VILLAGE_DESERT_HOUSE));
		this.add("add_loot_village_plains_house", this.addNewLootPool(BuiltInLootTables.VILLAGE_PLAINS_HOUSE, ModChestLootTables.VILLAGE_PLAINS_HOUSE));
		this.add("add_loot_village_savanna_house", this.addNewLootPool(BuiltInLootTables.VILLAGE_SAVANNA_HOUSE, ModChestLootTables.VILLAGE_SAVANNA_HOUSE));
		this.add("add_loot_village_snowy_house", this.addNewLootPool(BuiltInLootTables.VILLAGE_SNOWY_HOUSE, ModChestLootTables.VILLAGE_SNOWY_HOUSE));
		this.add("add_loot_village_taiga_house", this.addNewLootPool(BuiltInLootTables.VILLAGE_TAIGA_HOUSE, ModChestLootTables.VILLAGE_TAIGA_HOUSE));

		// Entity Loot
		this.add("add_onion_to_illagers", this.addItemOnPlayerKill(ModItems.ONION.get(), 0.02F, EntityType.PILLAGER, EntityType.EVOKER, EntityType.VINDICATOR));
		this.add("add_onion_to_zombies", this.addItemOnPlayerKill(ModItems.ONION.get(), 0.02F, EntityType.ZOMBIE, EntityType.HUSK, EntityType.ZOMBIE_VILLAGER));

		// Knife Scavenging
		this.add("scavenging_feather", this.addItemOnKnifeKill(Items.FEATHER, EntityType.CHICKEN));
		this.add("scavenging_ham_from_hoglin", this.addItemOnKnifeKill(ModItems.HAM.get(), false, 1.0F, EntityType.HOGLIN));
		this.add("scavenging_ham_from_pig", this.addItemOnKnifeKill(ModItems.HAM.get(), false, 0.5F, EntityType.PIG));
		this.add("scavenging_leather", this.addItemOnKnifeKill(Items.LEATHER, EntityType.COW, EntityType.MOOSHROOM, EntityType.HORSE, EntityType.DONKEY, EntityType.MULE, EntityType.LLAMA, EntityType.TRADER_LLAMA));
		this.add("scavenging_rabbit_hide", this.addItemOnKnifeKill(Items.RABBIT_HIDE, EntityType.RABBIT));
		this.add("scavenging_shulker_shell", this.addItemOnKnifeKill(Items.SHULKER_SHELL, EntityType.SHULKER));
		this.add("scavenging_smoked_ham_from_hoglin", this.addItemOnKnifeKill(ModItems.SMOKED_HAM.get(), true, 1.0F, EntityType.HOGLIN));
		this.add("scavenging_smoked_ham_from_pig", this.addItemOnKnifeKill(ModItems.SMOKED_HAM.get(), true, 0.5F, EntityType.PIG));
		this.add("scavenging_string", this.addItemOnKnifeKill(Items.STRING, EntityType.SPIDER, EntityType.CAVE_SPIDER));
		this.add("scavenging_pumpkin", new ReplaceItemModifier(new LootItemCondition[]{
			LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.PUMPKIN).build(),
			MatchTool.toolMatches(ItemPredicate.Builder.item().of(ModTags.Items.KNIVES))
				.and(hasSilkTouch().invert()).build()
		},Items.PUMPKIN, ModItems.PUMPKIN_SLICE.get(), 4));

		// Pastry Slicing
		this.add("slicing_apple_pie", this.pastrySlicing(ModItems.APPLE_PIE_SLICE.get(), ModBlocks.APPLE_PIE.get()));
		this.add("slicing_cake", this.pastrySlicing(ModItems.CAKE_SLICE.get(), Blocks.CAKE));
		this.add("slicing_chocolate_pie", this.pastrySlicing(ModItems.CHOCOLATE_PIE_SLICE.get(), ModBlocks.CHOCOLATE_PIE.get()));
		this.add("slicing_pumpkin_pie", this.pastrySlicing(ModItems.PUMPKIN_PIE_SLICE.get(), ModBlocks.PUMPKIN_PIE.get()));
		this.add("slicing_sweet_berry_cheesecake", this.pastrySlicing(ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get(), ModBlocks.SWEET_BERRY_CHEESECAKE.get()));
		this.add("slicing_candle_cake", this.candleCakeSlicing());

		// Straw Harvesting
		this.add("straw_from_grass", this.strawHarvesting(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SHORT_GRASS), 0.2F));
		this.add("straw_from_mature_rice", this.strawHarvesting(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.RICE_CROP_PANICLES.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(RicePaniclesBlock.RICE_AGE, 3)), 1.0F));
		this.add("straw_from_mature_wheat", this.strawHarvesting(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.WHEAT).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, 7)), 1.0F));
		this.add("straw_from_sandy_shrub", this.strawHarvesting(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.SANDY_SHRUB.get()), 0.3F));
		this.add("straw_from_tall_grass", this.strawHarvesting(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.TALL_GRASS), 0.2F));
	}

	private AddTableLootModifier addNewLootPool(ResourceKey<LootTable> lootToAddTo, ResourceKey<LootTable> newPool) {
		return new AddTableLootModifier(new LootItemCondition[]{LootTableIdCondition.builder(lootToAddTo.location()).build()}, newPool);
	}

	private AddItemModifier addItemOnPlayerKill(Item item, float chance, EntityType<?>... entity) {
		LootItemCondition.Builder[] entityConditions = new LootItemCondition.Builder[entity.length];
		for (int i = 0; i < entity.length; i++) {
			entityConditions[i] = LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
				EntityPredicate.Builder.entity().of(entity[i]).build());
		}
		List<LootItemCondition> conditions = new ArrayList<>();
		conditions.add(entityConditions.length > 1 ? AnyOfCondition.anyOf(entityConditions).build() : entityConditions[0].build());
		conditions.add(LootItemKilledByPlayerCondition.killedByPlayer().build());

		if (chance < 1.0F) {
			conditions.add(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registries, chance, 0.01F).build());
		}

		return new AddItemModifier(conditions.toArray(LootItemCondition[]::new), item, 1);
	}

	private AddItemModifier addItemOnKnifeKill(Item item, EntityType<?>... entity) {
		return this.addItemOnKnifeKill(item, null, 1.0F, entity);
	}

	private AddItemModifier addItemOnKnifeKill(Item item, Boolean onFire, float chance, EntityType<?>... entity) {
		LootItemCondition.Builder[] entityConditions = new LootItemCondition.Builder[entity.length];
		for (int i = 0; i < entity.length; i++) {
			entityConditions[i] = LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
				EntityPredicate.Builder.entity().of(entity[i]).build());
		}

		List<LootItemCondition> conditions = new ArrayList<>();
		conditions.add(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().mainhand(ItemPredicate.Builder.item().of(ModTags.Items.KNIVES)).build()).build()).build());
		conditions.add(entityConditions.length > 1 ? AnyOfCondition.anyOf(entityConditions).build() : entityConditions[0].build());

		if (onFire != null) {
			conditions.add(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(onFire))).build());
		}

		if (chance < 1.0F) {
			conditions.add(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registries, chance, 0.1F).build());
		}

		return new AddItemModifier(conditions.toArray(LootItemCondition[]::new), item, 1);
	}

	private AddItemModifier candleCakeSlicing() {
		List<Block> cakes = List.of(Blocks.CANDLE_CAKE, Blocks.WHITE_CANDLE_CAKE, Blocks.ORANGE_CANDLE_CAKE, Blocks.MAGENTA_CANDLE_CAKE, Blocks.LIGHT_BLUE_CANDLE_CAKE, Blocks.YELLOW_CANDLE_CAKE, Blocks.LIME_CANDLE_CAKE, Blocks.PINK_CANDLE_CAKE, Blocks.GRAY_CANDLE_CAKE, Blocks.LIGHT_GRAY_CANDLE_CAKE, Blocks.CYAN_CANDLE_CAKE, Blocks.PURPLE_CANDLE_CAKE, Blocks.BLUE_CANDLE_CAKE, Blocks.BROWN_CANDLE_CAKE, Blocks.GREEN_CANDLE_CAKE, Blocks.RED_CANDLE_CAKE, Blocks.BLACK_CANDLE_CAKE);
		LootItemCondition.Builder[] conditions = new LootItemCondition.Builder[cakes.size()];
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

	protected LootItemCondition.Builder hasSilkTouch() {
		HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
		return MatchTool.toolMatches(
			ItemPredicate.Builder.item()
				.withSubPredicate(
					ItemSubPredicates.ENCHANTMENTS,
					ItemEnchantmentsPredicate.enchantments(
						List.of(new EnchantmentPredicate(registrylookup.getOrThrow(Enchantments.SILK_TOUCH), MinMaxBounds.Ints.atLeast(1)))
					)
				)
		);
	}
}
