package vectorwing.farmersdelight.data;

import net.minecraft.advancements.predicates.*;
import net.minecraft.advancements.predicates.entity.EntityEquipmentPredicate;
import net.minecraft.advancements.predicates.entity.EntityFlagsPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.entity.EntityTypePredicate;
import net.minecraft.advancements.triggers.*;
import net.minecraft.core.HolderSet;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypeIds;
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
		this.add("add_onion_to_illagers", this.addItemOnPlayerKill(ModItems.ONION.get(), 0.02F, EntityTypeIds.PILLAGER, EntityTypeIds.EVOKER, EntityTypeIds.VINDICATOR));
		this.add("add_onion_to_zombies", this.addItemOnPlayerKill(ModItems.ONION.get(), 0.02F, EntityTypeIds.ZOMBIE, EntityTypeIds.HUSK, EntityTypeIds.ZOMBIE_VILLAGER));

		// Knife Scavenging
		this.add("scavenging_feather", this.addItemOnKnifeKill(Items.FEATHER, EntityTypeIds.CHICKEN));
		this.add("scavenging_ham_from_hoglin", this.addItemOnKnifeKill(ModItems.HAM.get(), false, 1.0F, EntityTypeIds.HOGLIN));
		this.add("scavenging_ham_from_pig", this.addItemOnKnifeKill(ModItems.HAM.get(), false, 0.5F, EntityTypeIds.PIG));
		this.add("scavenging_leather", this.addItemOnKnifeKill(Items.LEATHER, EntityTypeIds.COW, EntityTypeIds.MOOSHROOM, EntityTypeIds.HORSE, EntityTypeIds.DONKEY, EntityTypeIds.MULE, EntityTypeIds.LLAMA, EntityTypeIds.TRADER_LLAMA));
		this.add("scavenging_rabbit_hide", this.addItemOnKnifeKill(Items.RABBIT_HIDE, EntityTypeIds.RABBIT));
		this.add("scavenging_shulker_shell", this.addItemOnKnifeKill(Items.SHULKER_SHELL, EntityTypeIds.SHULKER));
		this.add("scavenging_smoked_ham_from_hoglin", this.addItemOnKnifeKill(ModItems.SMOKED_HAM.get(), true, 1.0F, EntityTypeIds.HOGLIN));
		this.add("scavenging_smoked_ham_from_pig", this.addItemOnKnifeKill(ModItems.SMOKED_HAM.get(), true, 0.5F, EntityTypeIds.PIG));
		this.add("scavenging_string", this.addItemOnKnifeKill(Items.STRING, EntityTypeIds.SPIDER, EntityTypeIds.CAVE_SPIDER));
		this.add("scavenging_pumpkin", new ReplaceItemModifier(new LootItemCondition[]{
			LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.PUMPKIN).build(),
			MatchTool.toolMatches(ItemPredicate.Builder.item().of(registries.lookupOrThrow(Registries.ITEM), ModTags.Items.KNIVES))
				.and(hasSilkTouch().invert()).build()
		}, 0, Items.PUMPKIN, ModItems.PUMPKIN_SLICE.get(), 4));

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
		return new AddTableLootModifier(new LootItemCondition[]{LootTableIdCondition.builder(lootToAddTo.identifier()).build()}, 0, newPool);
	}

	private AddItemModifier addItemOnPlayerKill(Item item, float chance, ResourceKey<EntityType<?>>... entity) {
		LootItemCondition.Builder[] entityConditions = new LootItemCondition.Builder[entity.length];
		for (int i = 0; i < entity.length; i++) {
			entityConditions[i] = LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
				entityPredicate(entity[i]).build());
		}
		List<LootItemCondition> conditions = new ArrayList<>();
		conditions.add(entityConditions.length > 1 ? AnyOfCondition.anyOf(entityConditions).build() : entityConditions[0].build());
		conditions.add(LootItemKilledByPlayerCondition.killedByPlayer().build());

		if (chance < 1.0F) {
			conditions.add(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registries, chance, 0.01F).build());
		}

		return new AddItemModifier(conditions.toArray(LootItemCondition[]::new), 0, item, 1);
	}

	private AddItemModifier addItemOnKnifeKill(Item item, ResourceKey<EntityType<?>>... entity) {
		return this.addItemOnKnifeKill(item, null, 1.0F, entity);
	}

	private AddItemModifier addItemOnKnifeKill(Item item, Boolean onFire, float chance, ResourceKey<EntityType<?>>... entity) {
		LootItemCondition.Builder[] entityConditions = new LootItemCondition.Builder[entity.length];
		for (int i = 0; i < entity.length; i++) {
			entityConditions[i] = LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
				entityPredicate(entity[i]).build());
		}

		List<LootItemCondition> conditions = new ArrayList<>();
		conditions.add(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().mainhand(ItemPredicate.Builder.item().of(registries.lookupOrThrow(Registries.ITEM), ModTags.Items.KNIVES)).build()).build()).build());
		conditions.add(entityConditions.length > 1 ? AnyOfCondition.anyOf(entityConditions).build() : entityConditions[0].build());

		if (onFire != null) {
			conditions.add(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(onFire))).build());
		}

		if (chance < 1.0F) {
			conditions.add(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registries, chance, 0.1F).build());
		}

		return new AddItemModifier(conditions.toArray(LootItemCondition[]::new), 0, item, 1);
	}

	private AddItemModifier candleCakeSlicing() {
		List<Block> cakes = new ArrayList<>();
		cakes.add(Blocks.CANDLE_CAKE);
		cakes.addAll(Blocks.DYED_CANDLE_CAKE.asList());
		LootItemCondition.Builder[] conditions = new LootItemCondition.Builder[cakes.size()];
		for (int i = 0; i < cakes.size(); i++) {
			conditions[i] = LootItemBlockStatePropertyCondition.hasBlockStateProperties(cakes.get(i));
		}
		return new AddItemModifier(new LootItemCondition[]{
			MatchTool.toolMatches(ItemPredicate.Builder.item().of(registries.lookupOrThrow(Registries.ITEM), ModTags.Items.KNIVES)).build(),
			AnyOfCondition.anyOf(conditions).build()
		}, 0, ModItems.CAKE_SLICE.get(), 7);
	}

	private PastrySlicingModifier pastrySlicing(Item receivedItem, Block slicedBlock) {
		return new PastrySlicingModifier(new LootItemCondition[]{
			MatchTool.toolMatches(ItemPredicate.Builder.item().of(registries.lookupOrThrow(Registries.ITEM), ModTags.Items.KNIVES)).build(),
			LootItemBlockStatePropertyCondition.hasBlockStateProperties(slicedBlock).build()
		}, 0, receivedItem);
	}

	private AddItemModifier strawHarvesting(LootItemBlockStatePropertyCondition.Builder slicedBlock, float chance) {
		List<LootItemCondition> conditions = new ArrayList<>();

		conditions.add(MatchTool.toolMatches(ItemPredicate.Builder.item().of(registries.lookupOrThrow(Registries.ITEM), ModTags.Items.STRAW_HARVESTERS)).build());
		if (chance < 1.0F) {
			conditions.add(LootItemRandomChanceCondition.randomChance(chance).build());
		}
		conditions.add(slicedBlock.build());

		return new AddItemModifier(conditions.toArray(LootItemCondition[]::new), 0, ModItems.STRAW.get(), 1);
	}

	protected LootItemCondition.Builder hasSilkTouch() {
		HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
		return MatchTool.toolMatches(
			ItemPredicate.Builder.item()
				.withComponents(
					DataComponentMatchers.Builder.components()
						.partial(
							DataComponentPredicates.ENCHANTMENTS,
							EnchantmentsPredicate.enchantments(
								List.of(new EnchantmentPredicate(registrylookup.getOrThrow(Enchantments.SILK_TOUCH), MinMaxBounds.Ints.atLeast(1)))
							)
						).build()
				)
		);
	}

	private EntityPredicate.Builder entityPredicate(ResourceKey<EntityType<?>> entity) {
		HolderLookup.RegistryLookup<EntityType<?>> entityTypes = this.registries.lookupOrThrow(Registries.ENTITY_TYPE);
		return EntityPredicate.Builder.entity().entityType(new EntityTypePredicate(HolderSet.direct(entityTypes.getOrThrow(entity))));
	}
}
