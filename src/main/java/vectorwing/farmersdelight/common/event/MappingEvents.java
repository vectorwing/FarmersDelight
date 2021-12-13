package vectorwing.farmersdelight.common.event;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID)
public class MappingEvents
{
	public static ResourceLocation mapping(String name) {
		return new ResourceLocation(FarmersDelight.MODID, name);
	}

	@SubscribeEvent
	public static void blockRemapping(RegistryEvent.MissingMappings<Block> event) {
		ImmutableList<RegistryEvent.MissingMappings.Mapping<Block>> mappings = event.getMappings(FarmersDelight.MODID);
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

		for (RegistryEvent.MissingMappings.Mapping<Block> mapping : mappings) {
			Supplier<Block> blockSupplier = blockRemapping.get(mapping.key);
			if (blockSupplier != null) {
				Block block = blockSupplier.get();
				if (block.getRegistryName() != null) {
					mapping.remap(block);
					FarmersDelight.LOGGER.warn("Remapping block '{}' to '{}'...", mapping.key.toString(), block.getRegistryName().toString());
				}
			}
		}
	}

	@SubscribeEvent
	public static void itemRemapping(RegistryEvent.MissingMappings<Item> event) {
		ImmutableList<RegistryEvent.MissingMappings.Mapping<Item>> mappings = event.getMappings(FarmersDelight.MODID);
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

		for (RegistryEvent.MissingMappings.Mapping<Item> mapping : mappings) {
			Supplier<Item> itemSupplier = itemRemapping.get(mapping.key);

			if (itemSupplier != null) {
				Item item = itemSupplier.get();
				if (item != null && item.getRegistryName() != null) {
					mapping.remap(item);
					FarmersDelight.LOGGER.warn("Remapping item '{}' to '{}'...", mapping.key.toString(), item.getRegistryName().toString());
				}
			}
		}
	}
}
