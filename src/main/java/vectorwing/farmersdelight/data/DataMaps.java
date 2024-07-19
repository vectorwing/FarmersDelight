package vectorwing.farmersdelight.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.concurrent.CompletableFuture;

public class DataMaps extends DataMapProvider
{
	protected DataMaps(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	protected void gather() {
		builder(NeoForgeDataMaps.COMPOSTABLES)
				// 30% chance
				.add(ModItems.TREE_BARK.get().asItem().builtInRegistryHolder(), new Compostable(0.3F), false)
				.add(ModItems.STRAW.get().asItem().builtInRegistryHolder(), new Compostable(0.3F), false)
				.add(ModItems.CABBAGE_SEEDS.get().asItem().builtInRegistryHolder(), new Compostable(0.3F), false)
				.add(ModItems.TOMATO_SEEDS.get().asItem().builtInRegistryHolder(), new Compostable(0.3F), false)
				.add(ModItems.RICE.get().asItem().builtInRegistryHolder(), new Compostable(0.3F), false)
				.add(ModItems.RICE_PANICLE.get().asItem().builtInRegistryHolder(), new Compostable(0.3F), false)
				.add(ModItems.SANDY_SHRUB.get().asItem().builtInRegistryHolder(), new Compostable(0.3F), false)
				// 50% chance
				.add(ModItems.PUMPKIN_SLICE.get().asItem().builtInRegistryHolder(), new Compostable(0.5F), false)
				.add(ModItems.CABBAGE_LEAF.get().asItem().builtInRegistryHolder(), new Compostable(0.5F), false)
				.add(ModItems.KELP_ROLL_SLICE.get().asItem().builtInRegistryHolder(), new Compostable(0.5F), false)
				// 65% chance
				.add(ModItems.CABBAGE.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
				.add(ModItems.ONION.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
				.add(ModItems.TOMATO.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
				.add(ModItems.WILD_CABBAGES.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
				.add(ModItems.WILD_ONIONS.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
				.add(ModItems.WILD_TOMATOES.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
				.add(ModItems.WILD_CARROTS.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
				.add(ModItems.WILD_POTATOES.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
				.add(ModItems.WILD_BEETROOTS.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
				.add(ModItems.WILD_RICE.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
				.add(ModItems.PIE_CRUST.get().asItem().builtInRegistryHolder(), new Compostable(0.65F), false)
				// 85% chance
				.add(ModItems.RICE_BALE.get().asItem().builtInRegistryHolder(), new Compostable(0.85F), false)
				.add(ModItems.SWEET_BERRY_COOKIE.get().asItem().builtInRegistryHolder(), new Compostable(0.85F), false)
				.add(ModItems.HONEY_COOKIE.get().asItem().builtInRegistryHolder(), new Compostable(0.85F), false)
				.add(ModItems.CAKE_SLICE.get().asItem().builtInRegistryHolder(), new Compostable(0.85F), false)
				.add(ModItems.APPLE_PIE_SLICE.get().asItem().builtInRegistryHolder(), new Compostable(0.85F), false)
				.add(ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get().asItem().builtInRegistryHolder(), new Compostable(0.85F), false)
				.add(ModItems.CHOCOLATE_PIE_SLICE.get().asItem().builtInRegistryHolder(), new Compostable(0.85F), false)
				.add(ModItems.RAW_PASTA.get().asItem().builtInRegistryHolder(), new Compostable(0.85F), false)
				.add(ModItems.ROTTEN_TOMATO.get().asItem().builtInRegistryHolder(), new Compostable(0.85F), false)
				.add(ModItems.KELP_ROLL.get().asItem().builtInRegistryHolder(), new Compostable(0.85F), false)
				// 100% chance
				.add(ModItems.APPLE_PIE.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false)
				.add(ModItems.SWEET_BERRY_CHEESECAKE.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false)
				.add(ModItems.CHOCOLATE_PIE.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false)
				.add(ModItems.DUMPLINGS.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false)
				.add(ModItems.STUFFED_PUMPKIN_BLOCK.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false)
				.add(ModItems.BROWN_MUSHROOM_COLONY.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false)
				.add(ModItems.RED_MUSHROOM_COLONY.get().asItem().builtInRegistryHolder(), new Compostable(1.0F), false);
	}
}
