package vectorwing.farmersdelight.utils.tags;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import vectorwing.farmersdelight.FarmersDelight;

/**
 * References to tags under the Farmer's Delight namespace.
 * These tags are used for mod mechanics, which aren't always useful outside of it.
 */
public class ModTags {
	// Blocks that can heat up a Cooking Pot.
	public static final ITag.INamedTag<Block> HEAT_SOURCES = modBlockTag("heat_sources");

	// Blocks to which a Cooking Pot will render a tray over. Use HEAT_SOURCES for heat logic.
	public static final ITag.INamedTag<Block> TRAY_HEAT_SOURCES = modBlockTag("tray_heat_sources");

	// Blocks that accelerate decomposition of Organic Compost if placed adjacent to it.
	public static final ITag.INamedTag<Block> COMPOST_ACTIVATORS = modBlockTag("compost_activators");

	// Foods that drop from mobs that wolves prey upon (currently, just sheep and chicken).
	public static final ITag.INamedTag<Item> WOLF_PREY = modItemTag("wolf_prey");

	// Knife items for game logic.
	public static final ITag.INamedTag<Item> KNIVES = modItemTag("tools/knife");

	private static ITag.INamedTag<Item> modItemTag(String path) {
		return ItemTags.makeWrapperTag(FarmersDelight.MODID + ":" + path);
	}

	private static ITag.INamedTag<Block> modBlockTag(String path) {
		return BlockTags.makeWrapperTag(FarmersDelight.MODID + ":" + path);
	}
}
