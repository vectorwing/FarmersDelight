package vectorwing.farmersdelight.data.loot;

import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.loot.CanItemPerformAbility;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.block.*;
import vectorwing.farmersdelight.common.loot.function.CopySkilletFunction;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModDataComponents;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Set;
import java.util.stream.Collectors;

public class FDBlockLoot extends BlockLootSubProvider
{
	public FDBlockLoot(HolderLookup.Provider holder) {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags(), holder);
	}

	@Override
	protected void generate() {
		HolderLookup.RegistryLookup<Enchantment> registryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

		dropSelf(ModBlocks.STOVE.get());
		dropNamedContainer(ModBlocks.WOODEN_BASKET.get());
		dropNamedContainer(ModBlocks.BAMBOO_BASKET.get());
		add(ModBlocks.COOKING_POT.get(), (block) -> LootTable.lootTable().withPool(this.applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block)
				.apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
						.include(DataComponents.CUSTOM_NAME)
						.include(ModDataComponents.MEAL.get())
						.include(ModDataComponents.CONTAINER.get())
				)))));
		add(ModBlocks.SKILLET.get(), block -> LootTable.lootTable().withPool(this.applyExplosionCondition(block, LootPool.lootPool()
				.add(LootItem.lootTableItem(block).apply(CopySkilletFunction.builder())))));
		dropSelf(ModBlocks.CUTTING_BOARD.get());

		dropOther(ModBlocks.BUDDING_TOMATO_CROP.get(), ModItems.TOMATO_SEEDS.get());
		add(ModBlocks.CABBAGE_CROP.get(), createCropDrops(ModBlocks.CABBAGE_CROP.get(), ModItems.CABBAGE.get(), ModItems.CABBAGE_SEEDS.get(), registryLookup));
		add(ModBlocks.ONION_CROP.get(), createSeedlessCropDrops(ModBlocks.ONION_CROP.get(), ModItems.ONION.get(), registryLookup));
		dropOther(ModBlocks.RICE_CROP.get(), ModItems.RICE.get());
		add(ModBlocks.RICE_CROP_PANICLES.get(), block -> LootTable.lootTable().withPool(this.applyExplosionDecay(block, LootPool.lootPool()
				.setRolls(ConstantValue.exactly(1.0F))
				.add(AlternativesEntry.alternatives(
						LootItem.lootTableItem(ModItems.RICE.get())
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(RicePaniclesBlock.RICE_AGE, 3)))
								.when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ModTags.Items.KNIVES))),
						LootItem.lootTableItem(ModItems.RICE_PANICLE.get())
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(RicePaniclesBlock.RICE_AGE, 3))))))));

		add(ModBlocks.TOMATO_CROP.get(), block -> this.applyExplosionDecay(block, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(ModItems.TOMATO.get()))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
								.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TomatoBlock.VINE_AGE, 3)))
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
						.apply(ApplyBonusCount.addUniformBonusCount(registryLookup.getOrThrow(Enchantments.FORTUNE), 1)))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(ModItems.TOMATO_SEEDS.get()))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
								.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TomatoBlock.ROPELOGGED, false))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(ModItems.ROTTEN_TOMATO.get()))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
								.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TomatoBlock.VINE_AGE, 3)))
						.when(LootItemRandomChanceCondition.randomChance(0.05F)))));

		add(ModBlocks.TOMATO_CROP_ON_ROPE.get(), block -> this.applyExplosionDecay(block, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(ModItems.TOMATO.get()))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
								.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TomatoBlock.VINE_AGE, 3)))
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
						.apply(ApplyBonusCount.addUniformBonusCount(registryLookup.getOrThrow(Enchantments.FORTUNE), 1)))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(ModItems.ROTTEN_TOMATO.get()))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
								.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TomatoBlock.VINE_AGE, 3)))
						.when(LootItemRandomChanceCondition.randomChance(0.05F)))));

		add(ModBlocks.SANDY_SHRUB.get(), block -> LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(AlternativesEntry.alternatives(
								LootItem.lootTableItem(block)
										.when(CanItemPerformAbility.canItemPerformAbility(ItemAbilities.SHEARS_HARVEST)),
								LootItem.lootTableItem(Items.BEETROOT_SEEDS)
										.when(LootItemRandomChanceCondition.randomChance(0.125F))
										.apply(ApplyExplosionDecay.explosionDecay())
										.apply(ApplyBonusCount.addUniformBonusCount(registryLookup.getOrThrow(Enchantments.FORTUNE), 2))))));

		add(ModBlocks.WILD_BEETROOTS.get(), block -> wildCrop(block, Items.BEETROOT, Items.BEETROOT_SEEDS, registryLookup));
		add(ModBlocks.WILD_CABBAGES.get(), block -> wildCrop(block, ModItems.CABBAGE.get(), ModItems.CABBAGE_SEEDS.get(), registryLookup));
		add(ModBlocks.WILD_CARROTS.get(), block -> wildCropNoSeeds(block, Items.CARROT, registryLookup));
		add(ModBlocks.WILD_ONIONS.get(), block -> wildCropNoSeeds(block, ModItems.ONION.get(), registryLookup)
				//onions drop alliums too
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.ALLIUM))
						.when(CanItemPerformAbility.canItemPerformAbility(ItemAbilities.SHEARS_HARVEST).invert())
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))));
		add(ModBlocks.WILD_POTATOES.get(), block -> wildCropNoSeeds(block, Items.POTATO, registryLookup));
		add(ModBlocks.WILD_TOMATOES.get(), block -> wildCrop(block, ModItems.TOMATO.get(), ModItems.TOMATO_SEEDS.get(), registryLookup));

		add(ModBlocks.WILD_RICE.get(), block -> LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(AlternativesEntry.alternatives(
								LootItem.lootTableItem(ModItems.WILD_RICE.get())
										.when(CanItemPerformAbility.canItemPerformAbility(ItemAbilities.SHEARS_HARVEST)),
								LootItem.lootTableItem(ModItems.RICE.get())
										.when(ExplosionCondition.survivesExplosion())))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
								.setProperties(StatePropertiesPredicate.Builder.properties()
										.hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)))
						.when(LocationCheck.checkLocation(LocationPredicate.Builder.location()
								.setBlock(BlockPredicate.Builder.block().of(block)
										.setProperties(StatePropertiesPredicate.Builder.properties()
												.hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER))), new BlockPos(0, 1, 0))))
				.withPool(LootPool.lootPool()
						.add(AlternativesEntry.alternatives(
								LootItem.lootTableItem(ModItems.WILD_RICE.get())
										.when(CanItemPerformAbility.canItemPerformAbility(ItemAbilities.SHEARS_HARVEST)),
								LootItem.lootTableItem(ModItems.RICE.get())
										.when(ExplosionCondition.survivesExplosion())))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
								.setProperties(StatePropertiesPredicate.Builder.properties()
										.hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)))
						.when(LocationCheck.checkLocation(LocationPredicate.Builder.location()
								.setBlock(BlockPredicate.Builder.block().of(block)
										.setProperties(StatePropertiesPredicate.Builder.properties()
												.hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER))), new BlockPos(0, -1, 0)))));

		dropSelf(ModBlocks.CARROT_CRATE.get());
		dropSelf(ModBlocks.POTATO_CRATE.get());
		dropSelf(ModBlocks.BEETROOT_CRATE.get());
		dropSelf(ModBlocks.CABBAGE_CRATE.get());
		dropSelf(ModBlocks.TOMATO_CRATE.get());
		dropSelf(ModBlocks.ONION_CRATE.get());
		dropSelf(ModBlocks.RICE_BALE.get());
		dropSelf(ModBlocks.RICE_BAG.get());
		dropSelf(ModBlocks.STRAW_BALE.get());

		dropSelf(ModBlocks.ROPE.get());
		dropSelf(ModBlocks.ROPE_FENCE.get());
		dropSelf(ModBlocks.ROPE_FENCE_GATE.get());
		dropSelf(ModBlocks.SAFETY_NET.get());

		dropSelf(ModBlocks.CANVAS_SIGN.get());
		dropSelf(ModBlocks.WHITE_CANVAS_SIGN.get());
		dropSelf(ModBlocks.ORANGE_CANVAS_SIGN.get());
		dropSelf(ModBlocks.MAGENTA_CANVAS_SIGN.get());
		dropSelf(ModBlocks.LIGHT_BLUE_CANVAS_SIGN.get());
		dropSelf(ModBlocks.YELLOW_CANVAS_SIGN.get());
		dropSelf(ModBlocks.LIME_CANVAS_SIGN.get());
		dropSelf(ModBlocks.PINK_CANVAS_SIGN.get());
		dropSelf(ModBlocks.GRAY_CANVAS_SIGN.get());
		dropSelf(ModBlocks.LIGHT_GRAY_CANVAS_SIGN.get());
		dropSelf(ModBlocks.CYAN_CANVAS_SIGN.get());
		dropSelf(ModBlocks.PURPLE_CANVAS_SIGN.get());
		dropSelf(ModBlocks.BLUE_CANVAS_SIGN.get());
		dropSelf(ModBlocks.BROWN_CANVAS_SIGN.get());
		dropSelf(ModBlocks.GREEN_CANVAS_SIGN.get());
		dropSelf(ModBlocks.RED_CANVAS_SIGN.get());
		dropSelf(ModBlocks.BLACK_CANVAS_SIGN.get());

		dropSelf(ModBlocks.HANGING_CANVAS_SIGN.get());
		dropSelf(ModBlocks.WHITE_HANGING_CANVAS_SIGN.get());
		dropSelf(ModBlocks.ORANGE_HANGING_CANVAS_SIGN.get());
		dropSelf(ModBlocks.MAGENTA_HANGING_CANVAS_SIGN.get());
		dropSelf(ModBlocks.LIGHT_BLUE_HANGING_CANVAS_SIGN.get());
		dropSelf(ModBlocks.YELLOW_HANGING_CANVAS_SIGN.get());
		dropSelf(ModBlocks.LIME_HANGING_CANVAS_SIGN.get());
		dropSelf(ModBlocks.PINK_HANGING_CANVAS_SIGN.get());
		dropSelf(ModBlocks.GRAY_HANGING_CANVAS_SIGN.get());
		dropSelf(ModBlocks.LIGHT_GRAY_HANGING_CANVAS_SIGN.get());
		dropSelf(ModBlocks.CYAN_HANGING_CANVAS_SIGN.get());
		dropSelf(ModBlocks.PURPLE_HANGING_CANVAS_SIGN.get());
		dropSelf(ModBlocks.BLUE_HANGING_CANVAS_SIGN.get());
		dropSelf(ModBlocks.BROWN_HANGING_CANVAS_SIGN.get());
		dropSelf(ModBlocks.GREEN_HANGING_CANVAS_SIGN.get());
		dropSelf(ModBlocks.RED_HANGING_CANVAS_SIGN.get());
		dropSelf(ModBlocks.BLACK_HANGING_CANVAS_SIGN.get());

		dropNamedContainer(ModBlocks.OAK_CABINET.get());
		dropNamedContainer(ModBlocks.SPRUCE_CABINET.get());
		dropNamedContainer(ModBlocks.BIRCH_CABINET.get());
		dropNamedContainer(ModBlocks.JUNGLE_CABINET.get());
		dropNamedContainer(ModBlocks.ACACIA_CABINET.get());
		dropNamedContainer(ModBlocks.DARK_OAK_CABINET.get());
		dropNamedContainer(ModBlocks.MANGROVE_CABINET.get());
		dropNamedContainer(ModBlocks.BAMBOO_CABINET.get());
		dropNamedContainer(ModBlocks.CHERRY_CABINET.get());
		dropNamedContainer(ModBlocks.CRIMSON_CABINET.get());
		dropNamedContainer(ModBlocks.WARPED_CABINET.get());

		dropSelf(ModBlocks.CANVAS_RUG.get());
		dropSelf(ModBlocks.TATAMI.get());
		add(ModBlocks.FULL_TATAMI_MAT.get(), block -> LootTable.lootTable().withPool(this.applyExplosionCondition(block, LootPool.lootPool()
				.setRolls(ConstantValue.exactly(1.0F))
				.add(LootItem.lootTableItem(block)
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
								.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TatamiMatBlock.PART, BedPart.HEAD)))))));
		dropSelf(ModBlocks.HALF_TATAMI_MAT.get());

		add(ModBlocks.BROWN_MUSHROOM_COLONY.get(), block -> this.mushroomColony(block, Items.BROWN_MUSHROOM));
		add(ModBlocks.RED_MUSHROOM_COLONY.get(), block -> this.mushroomColony(block, Items.RED_MUSHROOM));
		dropSelf(ModBlocks.ORGANIC_COMPOST.get());
		dropSelf(ModBlocks.RICH_SOIL.get());
		dropOther(ModBlocks.RICH_SOIL_FARMLAND.get(), ModBlocks.RICH_SOIL.get());

		add(ModBlocks.APPLE_PIE.get(), LootTable.lootTable());
		add(ModBlocks.CHOCOLATE_PIE.get(), LootTable.lootTable());
		add(ModBlocks.PUMPKIN_PIE.get(), LootTable.lootTable());
		add(ModBlocks.SWEET_BERRY_CHEESECAKE.get(), LootTable.lootTable());

		add(ModBlocks.HONEY_GLAZED_HAM_BLOCK.get(), block -> this.platedFoodExtraDrop(block, Items.BONE, 4));
		add(ModBlocks.RICE_ROLL_MEDLEY_BLOCK.get(), block -> this.platedFood(block, 8));
		add(ModBlocks.ROAST_CHICKEN_BLOCK.get(), block -> this.platedFoodExtraDrop(block, Items.BONE_MEAL, 4));
		add(ModBlocks.SHEPHERDS_PIE_BLOCK.get(), block -> this.platedFood(block, 4));
		add(ModBlocks.GLEAMING_SALAD_BLOCK.get(), block -> this.platedFood(block, 4));
		add(ModBlocks.STUFFED_PUMPKIN_BLOCK.get(), block -> LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(block))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
								.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(FeastBlock.SERVINGS, 4)))));
	}

	protected void dropNamedContainer(Block block) {
		add(block, this::createNameableBlockEntityTable);
	}

	@Override
	protected @NotNull Iterable<Block> getKnownBlocks() {
		return ModBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::value).collect(Collectors.toList());
	}

	protected LootTable.Builder mushroomColony(Block block, Item mushroom) {
		return this.applyExplosionDecay(block, LootTable.lootTable().withPool(LootPool.lootPool()
				.setRolls(ConstantValue.exactly(1.0F))
				.add(AlternativesEntry.alternatives(
						LootItem.lootTableItem(mushroom)
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(MushroomColonyBlock.COLONY_AGE, 0))),
						LootItem.lootTableItem(mushroom)
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0F)))
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(MushroomColonyBlock.COLONY_AGE, 1))),
						LootItem.lootTableItem(mushroom)
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F)))
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(MushroomColonyBlock.COLONY_AGE, 2))),
						//fully grown should only drop mushrooms if not harvested by shears
						LootItem.lootTableItem(mushroom)
								.apply(SetItemCountFunction.setCount(ConstantValue.exactly(5.0F)))
								.when(CanItemPerformAbility.canItemPerformAbility(ItemAbilities.SHEARS_HARVEST).invert())
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(MushroomColonyBlock.COLONY_AGE, 3))),
						//if broken with shears at max age, drop self
						LootItem.lootTableItem(block)
								.when(CanItemPerformAbility.canItemPerformAbility(ItemAbilities.SHEARS_HARVEST))
								.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
										.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(MushroomColonyBlock.COLONY_AGE, 3)))))));
	}

	protected LootTable.Builder createCropDrops(Block block, Item cropItem, Item seeds, HolderLookup.RegistryLookup<Enchantment> registryLookup) {
		LootItemCondition.Builder maxAgeCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, CropBlock.MAX_AGE));
		return this.applyExplosionDecay(block, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(cropItem)
								.when(maxAgeCondition)
								.otherwise(LootItem.lootTableItem(seeds))))
				.withPool(LootPool.lootPool()
						.when(maxAgeCondition)
						.add(LootItem.lootTableItem(seeds)
								.apply(ApplyBonusCount.addBonusBinomialDistributionCount(registryLookup.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3)))));
	}

	protected LootTable.Builder createSeedlessCropDrops(Block block, Item cropItem, HolderLookup.RegistryLookup<Enchantment> registryLookup) {
		LootItemCondition.Builder maxAgeCondition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CropBlock.AGE, CropBlock.MAX_AGE));
		return this.applyExplosionDecay(block, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(cropItem)))
				.withPool(LootPool.lootPool()
						.when(maxAgeCondition)
						.add(LootItem.lootTableItem(cropItem)
								.apply(ApplyBonusCount.addBonusBinomialDistributionCount(registryLookup.getOrThrow(Enchantments.FORTUNE), 0.5714286F, 3)))));


	}

	protected LootTable.Builder wildCrop(Block block, Item crop, Item seeds, HolderLookup.RegistryLookup<Enchantment> registryLookup) {
		return LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(crop))
						.when(LootItemRandomChanceCondition.randomChance(0.2F))
						.when(CanItemPerformAbility.canItemPerformAbility(ItemAbilities.SHEARS_HARVEST).invert()))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(AlternativesEntry.alternatives(
								LootItem.lootTableItem(block)
										.when(CanItemPerformAbility.canItemPerformAbility(ItemAbilities.SHEARS_HARVEST)),
								LootItem.lootTableItem(seeds)
										.apply(ApplyExplosionDecay.explosionDecay())
										.apply(ApplyBonusCount.addUniformBonusCount(registryLookup.getOrThrow(Enchantments.FORTUNE), 2)))));
	}

	protected LootTable.Builder wildCropNoSeeds(Block block, Item crop, HolderLookup.RegistryLookup<Enchantment> registryLookup) {
		return LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(AlternativesEntry.alternatives(
								LootItem.lootTableItem(block)
										.when(CanItemPerformAbility.canItemPerformAbility(ItemAbilities.SHEARS_HARVEST)),
								LootItem.lootTableItem(crop)
										.apply(ApplyExplosionDecay.explosionDecay())
										.apply(ApplyBonusCount.addUniformBonusCount(registryLookup.getOrThrow(Enchantments.FORTUNE), 2)))));
	}

	protected LootTable.Builder platedFood(Block block, int servings) {
		return LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(block))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
								.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(FeastBlock.SERVINGS, servings))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.BOWL))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
								.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(FeastBlock.SERVINGS, servings)).invert()));
	}

	protected LootTable.Builder platedFoodExtraDrop(Block block, Item extraDrop, int servings) {
		return LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(block))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
								.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(FeastBlock.SERVINGS, servings))))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(Items.BOWL))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
								.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(FeastBlock.SERVINGS, servings)).invert()))
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(extraDrop))
						.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
								.setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(FeastBlock.SERVINGS, servings)).invert()));
	}
}
