package vectorwing.farmersdelight.utils.tags;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import vectorwing.farmersdelight.FarmersDelight;

/**
 * References to tags under the Farmer's Delight namespace.
 * These tags are used for mod mechanics.
 */
public class ModTags
{
	// Blocks that represent the wild form of a farmable crop.
	public static final ITag.INamedTag<Block> WILD_CROPS = modBlockTag("wild_crops");

	// Blocks that can heat up a Cooking Pot.
	public static final ITag.INamedTag<Block> HEAT_SOURCES = modBlockTag("heat_sources");

	// Blocks to which a Cooking Pot will render a tray over. Included in HEAT_SOURCES.
	public static final ITag.INamedTag<Block> TRAY_HEAT_SOURCES = modBlockTag("tray_heat_sources");

	// Blocks that accelerate decomposition of Organic Compost if placed adjacent to it.
	public static final ITag.INamedTag<Block> COMPOST_ACTIVATORS = modBlockTag("compost_activators");

	// Blocks that should not have their growth boosted by Rich Soil, if planted on it.
	public static final ITag.INamedTag<Block> UNAFFECTED_BY_RICH_SOIL = modBlockTag("unaffected_by_rich_soil");

	// Blocks that represent the wild form of a farmable crop.
	public static final ITag.INamedTag<Item> WILD_CROPS_ITEM = modItemTag("wild_crops");

	// Items (ideally tools) that can obtain straw when harvesting grassy plants. Populated by all knives by default.
	public static final ITag.INamedTag<Item> STRAW_HARVESTERS = modItemTag("straw_harvesters");

	// Foods that should grant 5 minutes of Comfort when eaten. Does not include the main soups/stews of Farmer's Delight.
	public static final ITag.INamedTag<Item> COMFORT_FOODS = modItemTag("comfort_foods");

	// Foods that drop from mobs that wolves prey upon (sheep, rabbit and chicken).
	public static final ITag.INamedTag<Item> WOLF_PREY = modItemTag("wolf_prey");

	// Knife items for game logic.
	public static final ITag.INamedTag<Item> KNIVES = modItemTag("tools/knives");

	// Entities that should be able to eat Dog Food when tame. Defaults to tamed Wolves.
	public static final ITag.INamedTag<EntityType<?>> DOG_FOOD_USERS = modEntityTag("dog_food_users");

	// Entities that should be able to eat Horse Feed when tame. Defaults to most vanilla mounts, except Pigs and Striders.
	public static final ITag.INamedTag<EntityType<?>> HORSE_FEED_USERS = modEntityTag("horse_feed_users");

	private static ITag.INamedTag<Item> modItemTag(String path) {
		return ItemTags.makeWrapperTag(FarmersDelight.MODID + ":" + path);
	}

	private static ITag.INamedTag<Block> modBlockTag(String path) {
		return BlockTags.makeWrapperTag(FarmersDelight.MODID + ":" + path);
	}

	private static ITag.INamedTag<EntityType<?>> modEntityTag(String path) {
		return EntityTypeTags.getTagById(FarmersDelight.MODID + ":" + path);
	}
}
