//package vectorwing.farmersdelight.common.event;
//
//import com.google.common.collect.ImmutableMap;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.level.block.Block;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.Mod;
//import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
//import net.neoforged.neoforge.registries.RegisterEvent;
//import vectorwing.farmersdelight.FarmersDelight;
//import vectorwing.farmersdelight.common.registry.ModBlocks;
//import vectorwing.farmersdelight.common.registry.ModItems;
//
//import java.util.List;
//import java.util.Map;
//import java.util.function.Supplier;
//
//@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
//public class MappingEvents
//{
//	public static ResourceLocation mapping(String name) {
//		return new ResourceLocation(FarmersDelight.MODID, name);
//	}
//
//	@SubscribeEvent
//	public static void blockRemapping(FMLCommonSetupEvent event) {
//		event.enqueueWork(() -> {
//			Map<ResourceLocation, Supplier<Block>> blockRemapping = (new ImmutableMap.Builder<ResourceLocation, Supplier<Block>>())
//					.put(mapping("oak_pantry"), ModBlocks.OAK_CABINET)
//					.put(mapping("birch_pantry"), ModBlocks.BIRCH_CABINET)
//					.put(mapping("spruce_pantry"), ModBlocks.SPRUCE_CABINET)
//					.put(mapping("jungle_pantry"), ModBlocks.JUNGLE_CABINET)
//					.put(mapping("acacia_pantry"), ModBlocks.ACACIA_CABINET)
//					.put(mapping("dark_oak_pantry"), ModBlocks.DARK_OAK_CABINET)
//					.put(mapping("crimson_pantry"), ModBlocks.CRIMSON_CABINET)
//					.put(mapping("warped_pantry"), ModBlocks.WARPED_CABINET)
//					.put(mapping("rice_crop"), ModBlocks.RICE_CROP)
//					.put(mapping("rice_upper_crop"), ModBlocks.RICE_CROP_PANICLES)
//					.build();
//
//			for (Map.Entry<ResourceLocation, Supplier<Block>> mapping : blockRemapping.entrySet()) {
//				Supplier<Block> blockSupplier = blockRemapping.get(mapping.getKey());
//				if (blockSupplier != null) {
//					Block block = blockSupplier.get();
//					if (BuiltInRegistries.BLOCK.getKey(block) != null) {
//						ModBlocks.BLOCKS.addAlias(mapping.getKey(), BuiltInRegistries.BLOCK.getKey(block));
//						FarmersDelight.LOGGER.warn("Remapping block '{}' to '{}'...", mapping.getKey().toString(), BuiltInRegistries.BLOCK.getKey(block).toString());
//					}
//				}
//			}
//		});
//	}
//
//	@SubscribeEvent
//	public static void itemRemapping(FMLCommonSetupEvent event) {
//		event.enqueueWork(() -> {
//			Map<ResourceLocation, Supplier<Item>> itemRemapping = (new ImmutableMap.Builder<ResourceLocation, Supplier<Item>>())
//					.put(mapping("oak_pantry"), ModItems.OAK_CABINET)
//					.put(mapping("birch_pantry"), ModItems.BIRCH_CABINET)
//					.put(mapping("spruce_pantry"), ModItems.SPRUCE_CABINET)
//					.put(mapping("jungle_pantry"), ModItems.JUNGLE_CABINET)
//					.put(mapping("acacia_pantry"), ModItems.ACACIA_CABINET)
//					.put(mapping("dark_oak_pantry"), ModItems.DARK_OAK_CABINET)
//					.put(mapping("crimson_pantry"), ModItems.CRIMSON_CABINET)
//					.put(mapping("warped_pantry"), ModItems.WARPED_CABINET)
//					.build();
//
//			for (Map.Entry<ResourceLocation, Supplier<Item>> mapping : itemRemapping.entrySet()) {
//				Supplier<Item> itemSupplier = itemRemapping.get(mapping.getKey());
//
//				if (itemSupplier != null) {
//					Item item = itemSupplier.get();
//					if (item != null && BuiltInRegistries.ITEM.getKey(item) != null) {
//						ModItems.ITEMS.addAlias(mapping.getKey(), BuiltInRegistries.ITEM.getKey(item));
//						FarmersDelight.LOGGER.warn("Remapping item '{}' to '{}'...", mapping.getKey().toString(), BuiltInRegistries.ITEM.getKey(item).toString());
//					}
//				}
//			}
//		});
//	}
//}
