package vectorwing.farmersdelight.core.event;

import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.core.registry.ModBlocks;
import vectorwing.farmersdelight.core.registry.ModItems;

/**
 * Some items and blocks may suffer renames or changes during the Beta development period.
 * This handler is responsible for remapping them accordingly, so that old saves can be compatible with modern versions (most of the time).
 * <p>
 * For all intents and purposes, this is not a guaranteed solution. Many errors from other mods can get in the way.
 * New minor versions should be treated as breaking, and new worlds should be preferred over converting old ones.
 * Hopefully they work fine form the majority of cases. Always make backups!
 */
@Mod.EventBusSubscriber(modid = FarmersDelight.MODID)
public class RemappingEvents
{
	private static final Logger LOGGER = LogManager.getLogger();

	// OLD NAMES
	private static final ResourceLocation MULCH = new ResourceLocation(FarmersDelight.MODID, "mulch");
	private static final ResourceLocation MULCH_FARMLAND = new ResourceLocation(FarmersDelight.MODID, "mulch_farmland");

	@SubscribeEvent
	public static void onRemapBlocks(RegistryEvent.MissingMappings<Block> event) {
		ModContainer mod = ModList.get().getModContainerById(FarmersDelight.MODID).get();
		event.setModContainer(mod);

		ImmutableList<RegistryEvent.MissingMappings.Mapping<Block>> mappings = event.getMappings();

		for (RegistryEvent.MissingMappings.Mapping<Block> mapping : mappings) {
			if (mapping.key.equals(MULCH)) {
				mapping.remap(ModBlocks.RICH_SOIL.get());
				LOGGER.warn("Remapping block '{}' to '{}'...", MULCH, ModBlocks.RICH_SOIL.get().getRegistryName());
			}
			if (mapping.key.equals(MULCH_FARMLAND)) {
				mapping.remap(ModBlocks.RICH_SOIL_FARMLAND.get());
				LOGGER.warn("Remapping block '{}' to '{}'...", MULCH_FARMLAND, ModBlocks.RICH_SOIL_FARMLAND.get().getRegistryName());
			}
		}
	}

	@SubscribeEvent
	public static void onRemapItems(RegistryEvent.MissingMappings<Item> event) {
		ModContainer mod = ModList.get().getModContainerById(FarmersDelight.MODID).get();
		event.setModContainer(mod);

		ImmutableList<RegistryEvent.MissingMappings.Mapping<Item>> mappings = event.getMappings();

		for (RegistryEvent.MissingMappings.Mapping<Item> mapping : mappings) {
			if (mapping.key.equals(MULCH)) {
				mapping.remap(ModItems.RICH_SOIL.get());
				LOGGER.warn("Remapping item '{}' to '{}'...", MULCH, ModBlocks.RICH_SOIL.get().getRegistryName());
			}
			if (mapping.key.equals(MULCH_FARMLAND)) {
				mapping.remap(ModItems.RICH_SOIL_FARMLAND.get());
				LOGGER.warn("Remapping item '{}' to '{}'...", MULCH_FARMLAND, ModBlocks.RICH_SOIL_FARMLAND.get().getRegistryName());
			}
		}
	}
}
