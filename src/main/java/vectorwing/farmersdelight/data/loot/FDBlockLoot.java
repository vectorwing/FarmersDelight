package vectorwing.farmersdelight.data.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModDataComponents;

import java.util.HashSet;
import java.util.Set;

public class FDBlockLoot extends BlockLootSubProvider
{
	private final Set<Block> generatedLootTables = new HashSet<>();

	public FDBlockLoot(HolderLookup.Provider holder) {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags(), holder);
	}

	@Override
	protected void generate() {
		dropSelf(ModBlocks.STOVE.get());
		dropNamedContainer(ModBlocks.BASKET.get());
		add(ModBlocks.COOKING_POT.get(), (block) -> LootTable.lootTable().withPool(this.applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block)
				.apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
						.include(DataComponents.CUSTOM_NAME)
						.include(ModDataComponents.MEAL.get())
						.include(ModDataComponents.CONTAINER.get())
				)))));
//		dropSelf(ModBlocks.SKILLET.get());			has special functions
		dropSelf(ModBlocks.CUTTING_BOARD.get());

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
		dropSelf(ModBlocks.SAFETY_NET.get());

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
//		dropSelf(ModBlocks.FULL_TATAMI_MAT.get());		Drops only from "head" side
		dropSelf(ModBlocks.HALF_TATAMI_MAT.get());

		// Canvas Signs only need it for the standard block; wall blocks inherit it.

		// Mushroom Colonies are rather complex...
		dropSelf(ModBlocks.ORGANIC_COMPOST.get());
		dropSelf(ModBlocks.RICH_SOIL.get());
		dropOther(ModBlocks.RICH_SOIL_FARMLAND.get(), ModBlocks.RICH_SOIL.get());


	}

	protected void dropNamedContainer(Block block) {
		add(block, this::createNameableBlockEntityTable);
	}

	@Override
	protected void add(Block block, LootTable.Builder builder) {
		this.generatedLootTables.add(block);
		this.map.put(block.getLootTable(), builder);
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return generatedLootTables;
	}
}
