package vectorwing.farmersdelight.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class DataMaps extends DataMapProvider
{
	protected DataMaps(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	protected void gather(HolderLookup.@NotNull Provider provider) {
		builder(NeoForgeDataMaps.FURNACE_FUELS)
			// 0.5 items
			.add(item(ModItems.HALF_TATAMI_MAT.get()), new FurnaceFuel(100), false)
			.add(item(ModItems.STRAW.get()), new FurnaceFuel(100), false)
			// 1 item
			.add(item(ModItems.CUTTING_BOARD.get()), new FurnaceFuel(200), false)
			.add(item(ModItems.ROPE.get()), new FurnaceFuel(200), false)
			.add(item(ModItems.SAFETY_NET.get()), new FurnaceFuel(200), false)
			.add(item(ModItems.FULL_TATAMI_MAT.get()), new FurnaceFuel(200), false)
			.add(item(ModItems.CANVAS_RUG.get()), new FurnaceFuel(200), false)
			.add(item(ModItems.ROPE_FENCE.get()), new FurnaceFuel(200), false)
			.add(item(ModItems.ROPE_FENCE_GATE.get()), new FurnaceFuel(200), false)
			.add(item(ModItems.TREE_BARK.get()), new FurnaceFuel(200), false)
			// 1.5 items
			.add(item(ModItems.WOODEN_BASKET.get()), new FurnaceFuel(300), false)
			.add(item(ModItems.BAMBOO_BASKET.get()), new FurnaceFuel(300), false)
			.add(ModTags.Items.CABINETS_WOODEN, new FurnaceFuel(300), false)
			// 2 items
			.add(item(ModItems.TATAMI.get()), new FurnaceFuel(400), false)
			.add(item(ModItems.CANVAS.get()), new FurnaceFuel(400), false)
			// 5 items
			.add(item(ModItems.STRAW_BALE.get()), new FurnaceFuel(1000), false)
			// Exclusions
			.remove(ModItems.CRIMSON_CABINET.get().builtInRegistryHolder())
			.remove(ModItems.WARPED_CABINET.get().builtInRegistryHolder());
		builder(NeoForgeDataMaps.COMPOSTABLES)
			// 30% chance
			.add(item(ModItems.TREE_BARK.get()), new Compostable(0.3F), false)
			.add(item(ModItems.STRAW.get()), new Compostable(0.3F), false)
			.add(item(ModItems.CABBAGE_SEEDS.get()), new Compostable(0.3F), false)
			.add(item(ModItems.TOMATO_SEEDS.get()), new Compostable(0.3F), false)
			.add(item(ModItems.RICE.get()), new Compostable(0.3F), false)
			.add(item(ModItems.RICE_PANICLE.get()), new Compostable(0.3F), false)
			.add(item(ModItems.SANDY_SHRUB.get()), new Compostable(0.3F), false)
			// 50% chance
			.add(item(ModItems.PUMPKIN_SLICE.get()), new Compostable(0.5F), false)
			.add(item(ModItems.CABBAGE_LEAF.get()), new Compostable(0.5F), false)
			.add(item(ModItems.KELP_ROLL_SLICE.get()), new Compostable(0.5F), false)
			// 65% chance
			.add(item(ModItems.CABBAGE.get()), new Compostable(0.65F), false)
			.add(item(ModItems.ONION.get()), new Compostable(0.65F), false)
			.add(item(ModItems.TOMATO.get()), new Compostable(0.65F), false)
			.add(item(ModItems.WILD_CABBAGES.get()), new Compostable(0.65F), false)
			.add(item(ModItems.WILD_ONIONS.get()), new Compostable(0.65F), false)
			.add(item(ModItems.WILD_TOMATOES.get()), new Compostable(0.65F), false)
			.add(item(ModItems.WILD_CARROTS.get()), new Compostable(0.65F), false)
			.add(item(ModItems.WILD_POTATOES.get()), new Compostable(0.65F), false)
			.add(item(ModItems.WILD_BEETROOTS.get()), new Compostable(0.65F), false)
			.add(item(ModItems.WILD_RICE.get()), new Compostable(0.65F), false)
			.add(item(ModItems.PIE_CRUST.get()), new Compostable(0.65F), false)
			// 85% chance
			.add(item(ModItems.RICE_BALE.get()), new Compostable(0.85F), false)
			.add(item(ModItems.SWEET_BERRY_COOKIE.get()), new Compostable(0.85F), false)
			.add(item(ModItems.HONEY_COOKIE.get()), new Compostable(0.85F), false)
			.add(item(ModItems.CAKE_SLICE.get()), new Compostable(0.85F), false)
			.add(item(ModItems.APPLE_PIE_SLICE.get()), new Compostable(0.85F), false)
			.add(item(ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get()), new Compostable(0.85F), false)
			.add(item(ModItems.CHOCOLATE_PIE_SLICE.get()), new Compostable(0.85F), false)
			.add(item(ModItems.RAW_PASTA.get()), new Compostable(0.85F), false)
			.add(item(ModItems.ROTTEN_TOMATO.get()), new Compostable(0.85F), false)
			.add(item(ModItems.KELP_ROLL.get()), new Compostable(0.85F), false)
			// 100% chance
			.add(item(ModItems.APPLE_PIE.get()), new Compostable(1.0F), false)
			.add(item(ModItems.SWEET_BERRY_CHEESECAKE.get()), new Compostable(1.0F), false)
			.add(item(ModItems.CHOCOLATE_PIE.get()), new Compostable(1.0F), false)
			.add(item(ModItems.DUMPLINGS.get()), new Compostable(1.0F), false)
			.add(item(ModItems.STUFFED_PUMPKIN_BLOCK.get()), new Compostable(1.0F), false)
			.add(item(ModItems.BROWN_MUSHROOM_COLONY.get()), new Compostable(1.0F), false)
			.add(item(ModItems.RED_MUSHROOM_COLONY.get()), new Compostable(1.0F), false);
	}

	private static ResourceKey<Item> item(Item item) {
		return BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow();
	}
}
