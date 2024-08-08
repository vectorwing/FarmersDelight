package vectorwing.farmersdelight.data.loot;

import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModLootTables;

import java.util.function.BiConsumer;

public class FDChestLoot implements LootTableSubProvider {

	@Override
	public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
		consumer.accept(ModLootTables.ABANDONED_MINESHAFT, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(ModItems.COOKING_POT.get()))
						.add(LootItem.lootTableItem(ModItems.SKILLET.get())
								.apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.15F, 0.8F))))
						.add(EmptyLootItem.emptyItem().setWeight(6)))
				.withPool(LootPool.lootPool()
						.setRolls(UniformGenerator.between(1.0F, 4.0F))
						.add(LootItem.lootTableItem(ModItems.TOMATO_SEEDS.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
						.add(LootItem.lootTableItem(ModItems.CABBAGE_SEEDS.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
						.add(LootItem.lootTableItem(ModItems.RICE.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
						.add(EmptyLootItem.emptyItem().setWeight(2))
				).withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(3.0F))
						.add(LootItem.lootTableItem(ModItems.ROPE.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 12.0F))))
						.add(EmptyLootItem.emptyItem().setWeight(2))
				));

		consumer.accept(ModLootTables.BASTION_HOGLIN_STABLE, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(ModItems.DIAMOND_KNIFE.get())
								.apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.15F, 0.8F))))
						.add(LootItem.lootTableItem(ModItems.GOLDEN_KNIFE.get()).setWeight(2)
								.apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
						.add(EmptyLootItem.emptyItem().setWeight(2))
				).withPool(LootPool.lootPool()
						.setRolls(UniformGenerator.between(1.0F, 2.0F))
						.add(LootItem.lootTableItem(ModItems.HAM.get()).setWeight(4)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))))
						.add(LootItem.lootTableItem(ModItems.SMOKED_HAM.get()).setWeight(2)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))))
						.add(EmptyLootItem.emptyItem().setWeight(2))
				));

		consumer.accept(ModLootTables.BASTION_TREASURE, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(ModItems.DIAMOND_KNIFE.get())
								.apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.8F, 1.0F)))
								.apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
						.add(LootItem.lootTableItem(ModItems.DIAMOND_KNIFE.get()))
						.add(EmptyLootItem.emptyItem().setWeight(6))
				));

		consumer.accept(ModLootTables.END_CITY_TREASURE, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(ModItems.DIAMOND_KNIFE.get())
								.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(20.0F, 39.0F)).allowTreasure()))
						.add(LootItem.lootTableItem(ModItems.IRON_KNIFE.get())
								.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(20.0F, 39.0F)).allowTreasure()))
						.add(EmptyLootItem.emptyItem().setWeight(6))
				));

		consumer.accept(ModLootTables.PILLAGER_OUTPOST, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(UniformGenerator.between(1.0F, 2.0F))
						.add(LootItem.lootTableItem(ModItems.ONION.get()).setWeight(5)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 12.0F))))
						.add(LootItem.lootTableItem(ModItems.ONION_CRATE.get()).setWeight(2)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
						.add(EmptyLootItem.emptyItem().setWeight(2))
				));

		consumer.accept(ModLootTables.RUINED_PORTAL, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(ModItems.GOLDEN_KNIFE.get())
								.apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
						.add(EmptyLootItem.emptyItem().setWeight(3))
				));

		consumer.accept(ModLootTables.SHIPWRECK_SUPPLY, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(UniformGenerator.between(1.0F, 2.0F))
						.add(LootItem.lootTableItem(ModItems.TOMATO_SEEDS.get()).setWeight(6)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
						.add(LootItem.lootTableItem(ModItems.CABBAGE_SEEDS.get()).setWeight(6)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
						.add(LootItem.lootTableItem(ModItems.ONION.get()).setWeight(6)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
						.add(LootItem.lootTableItem(ModItems.RICE.get()).setWeight(6)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
				).withPool(LootPool.lootPool()
						.setRolls(UniformGenerator.between(1.0F, 3.0F))
						.add(LootItem.lootTableItem(ModItems.ROPE.get()).setWeight(2)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 12.0F))))
						.add(EmptyLootItem.emptyItem().setWeight(1))
				));

		consumer.accept(ModLootTables.SIMPLE_DUNGEON, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(UniformGenerator.between(1.0F, 4.0F))
						.add(LootItem.lootTableItem(ModItems.TOMATO_SEEDS.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
						.add(LootItem.lootTableItem(ModItems.CABBAGE_SEEDS.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))))
						.add(EmptyLootItem.emptyItem().setWeight(2))
				).withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(ModItems.ROPE.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 12.0F))))
						.add(EmptyLootItem.emptyItem().setWeight(2))
				));

		consumer.accept(ModLootTables.VILLAGE_BUTCHER, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1.0F))
						.add(LootItem.lootTableItem(ModItems.FLINT_KNIFE.get()))
						.add(LootItem.lootTableItem(ModItems.IRON_KNIFE.get()))
						.add(EmptyLootItem.emptyItem()))
				.withPool(LootPool.lootPool()
						.setRolls(UniformGenerator.between(1.0F, 2.0F))
						.add(LootItem.lootTableItem(ModItems.HAM.get()))
						.add(LootItem.lootTableItem(ModItems.MINCED_BEEF.get()).setWeight(3)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
						.add(LootItem.lootTableItem(ModItems.BACON.get()).setWeight(3)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
						.add(LootItem.lootTableItem(ModItems.MUTTON_CHOPS.get()).setWeight(3)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 6.0F))))
						.add(EmptyLootItem.emptyItem())));

		consumer.accept(ModLootTables.VILLAGE_DESERT_HOUSE, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(UniformGenerator.between(1.0F, 3.0F))
						.add(LootItem.lootTableItem(ModItems.TOMATO_SEEDS.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
						.add(LootItem.lootTableItem(ModItems.CABBAGE_SEEDS.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));

		consumer.accept(ModLootTables.VILLAGE_PLAINS_HOUSE, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(UniformGenerator.between(1.0F, 3.0F))
						.add(LootItem.lootTableItem(ModItems.ONION.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
						.add(LootItem.lootTableItem(ModItems.TOMATO_SEEDS.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));

		consumer.accept(ModLootTables.VILLAGE_SAVANNA_HOUSE, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(UniformGenerator.between(1.0F, 3.0F))
						.add(LootItem.lootTableItem(ModItems.TOMATO_SEEDS.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
						.add(LootItem.lootTableItem(ModItems.CABBAGE_SEEDS.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));

		consumer.accept(ModLootTables.VILLAGE_SNOWY_HOUSE, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(UniformGenerator.between(1.0F, 3.0F))
						.add(LootItem.lootTableItem(ModItems.ONION.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
						.add(LootItem.lootTableItem(ModItems.CABBAGE_SEEDS.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));

		consumer.accept(ModLootTables.VILLAGE_TAIGA_HOUSE, LootTable.lootTable()
				.withPool(LootPool.lootPool()
						.setRolls(UniformGenerator.between(1.0F, 3.0F))
						.add(LootItem.lootTableItem(ModItems.CABBAGE_SEEDS.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
						.add(LootItem.lootTableItem(ModItems.RICE.get())
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))));
	}
}
