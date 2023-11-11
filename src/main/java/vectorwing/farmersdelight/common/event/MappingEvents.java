package vectorwing.farmersdelight.common.event;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.MissingMappingsEvent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID)
public class MappingEvents
{
	public static ResourceLocation mapping(String name) {
		return new ResourceLocation(FarmersDelight.MODID, name);
	}

	@SubscribeEvent
	public static void blockRemapping(MissingMappingsEvent event) {
		List<MissingMappingsEvent.Mapping<Block>> mappings = event.getMappings(ForgeRegistries.Keys.BLOCKS, FarmersDelight.MODID);
		Map<ResourceLocation, Supplier<Block>> blockRemapping = (new ImmutableMap.Builder<ResourceLocation, Supplier<Block>>())
				.put(mapping("oak_pantry"), ModBlocks.OAK_CABINET)
				.put(mapping("birch_pantry"), ModBlocks.BIRCH_CABINET)
				.put(mapping("spruce_pantry"), ModBlocks.SPRUCE_CABINET)
				.put(mapping("jungle_pantry"), ModBlocks.JUNGLE_CABINET)
				.put(mapping("acacia_pantry"), ModBlocks.ACACIA_CABINET)
				.put(mapping("dark_oak_pantry"), ModBlocks.DARK_OAK_CABINET)
				.put(mapping("crimson_pantry"), ModBlocks.CRIMSON_CABINET)
				.put(mapping("warped_pantry"), ModBlocks.WARPED_CABINET)
				.put(mapping("rice_crop"), ModBlocks.RICE_CROP)
				.put(mapping("rice_upper_crop"), ModBlocks.RICE_CROP_PANICLES)
				.build();

		for (MissingMappingsEvent.Mapping<Block> mapping : mappings) {
			Supplier<Block> blockSupplier = blockRemapping.get(mapping.getKey());
			if (blockSupplier != null) {
				Block block = blockSupplier.get();
				if (ForgeRegistries.BLOCKS.getKey(block) != null) {
					mapping.remap(block);
					FarmersDelight.LOGGER.warn("Remapping block '{}' to '{}'...", mapping.getKey().toString(), ForgeRegistries.BLOCKS.getKey(block).toString());
				}
			}
		}
	}

	@SubscribeEvent
	public static void itemRemapping(MissingMappingsEvent event) {
		List<MissingMappingsEvent.Mapping<Item>> mappings = event.getMappings(ForgeRegistries.Keys.ITEMS, FarmersDelight.MODID);
		Map<ResourceLocation, Supplier<Item>> itemRemapping = (new ImmutableMap.Builder<ResourceLocation, Supplier<Item>>())
				.put(mapping("oak_pantry"), ModItems.OAK_CABINET)
				.put(mapping("birch_pantry"), ModItems.BIRCH_CABINET)
				.put(mapping("spruce_pantry"), ModItems.SPRUCE_CABINET)
				.put(mapping("jungle_pantry"), ModItems.JUNGLE_CABINET)
				.put(mapping("acacia_pantry"), ModItems.ACACIA_CABINET)
				.put(mapping("dark_oak_pantry"), ModItems.DARK_OAK_CABINET)
				.put(mapping("crimson_pantry"), ModItems.CRIMSON_CABINET)
				.put(mapping("warped_pantry"), ModItems.WARPED_CABINET)
				.build();

		for (MissingMappingsEvent.Mapping<Item> mapping : mappings) {
			Supplier<Item> itemSupplier = itemRemapping.get(mapping.getKey());

			if (itemSupplier != null) {
				Item item = itemSupplier.get();
				if (item != null && ForgeRegistries.ITEMS.getKey(item) != null) {
					mapping.remap(item);
					FarmersDelight.LOGGER.warn("Remapping item '{}' to '{}'...", mapping.getKey().toString(), ForgeRegistries.ITEMS.getKey(item).toString());
				}
			}
		}
	}
}
