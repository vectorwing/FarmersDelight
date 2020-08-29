package vectorwing.farmersdelight.utils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;

/**
 * References to tags under the Farmer's Delight namespace.
 * These tags are used for mod mechanics, which aren't always useful outside of it.
 */
public class ModTags
{
	public static final Tag<Block> HEAT_SOURCES = modBlockTag("heat_sources");
	public static final Tag<Block> COMPOST_ACTIVATORS = modBlockTag("compost_activators");

	public static final Tag<Item> WOLF_PREY = modItemTag("wolf_prey");
	public static final Tag<Item> KNIVES = modItemTag("tools/knife");

	private static Tag<Item> modItemTag(String path) {
		return new ItemTags.Wrapper(new ResourceLocation(FarmersDelight.MODID, path));
	}
	private static Tag<Block> modBlockTag(String path) {
		return new BlockTags.Wrapper(new ResourceLocation(FarmersDelight.MODID, path));
	}
}
