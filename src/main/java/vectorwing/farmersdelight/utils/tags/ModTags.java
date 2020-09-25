package vectorwing.farmersdelight.utils.tags;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;

/**
 * References to tags under the Farmer's Delight namespace.
 * These tags are used for mod mechanics.
 */
public class ModTags
{
	// Blocks that can heat up a Cooking Pot.
	public static final Tag<Block> HEAT_SOURCES = modBlockTag("heat_sources");

	// Blocks to which a Cooking Pot will render a tray over. Included in HEAT_SOURCES.
	public static final Tag<Block> TRAY_HEAT_SOURCES = modBlockTag("tray_heat_sources");

	// Blocks that accelerate decomposition of Organic Compost if placed adjacent to it.
	public static final Tag<Block> COMPOST_ACTIVATORS = modBlockTag("compost_activators");

	// Items (ideally tools) that can obtain straw when harvesting grassy plants. Populated by all knives by default.
	public static final Tag<Item> STRAW_HARVESTERS = modItemTag("straw_harvesters");

	// Foods that should grant 5 minutes of Comfort when eaten. Does not include the main soups/stews of Farmer's Delight.
	public static final Tag<Item> COMFORT_FOODS = modItemTag("comfort_foods");

	// Foods that drop from mobs that wolves prey upon (sheep, rabbit and chicken).
	public static final Tag<Item> WOLF_PREY = modItemTag("wolf_prey");

	// Knife items for game logic.
	public static final Tag<Item> KNIVES = modItemTag("tools/knives");

	private static Tag<Item> modItemTag(String path) {
		return new ItemTags.Wrapper(new ResourceLocation(FarmersDelight.MODID, path));
	}
	private static Tag<Block> modBlockTag(String path) {
		return new BlockTags.Wrapper(new ResourceLocation(FarmersDelight.MODID, path));
	}
}
