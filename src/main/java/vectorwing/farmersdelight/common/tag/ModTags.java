package vectorwing.farmersdelight.common.tag;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import vectorwing.farmersdelight.FarmersDelight;

/**
 * References to tags under the Farmer's Delight namespace.
 * These tags are used for mod mechanics.
 */
public class ModTags
{
	// Blocks that are efficiently mined with a Knife.
	public static final Tag.Named<Block> MINEABLE_WITH_KNIFE = modBlockTag("mineable/knife");

	// Blocks that represent the wild form of a farmable crop.
	public static final Tag.Named<Block> WILD_CROPS = modBlockTag("wild_crops");

	// Blocks that can heat up cooking workstations.
	public static final Tag.Named<Block> HEAT_SOURCES = modBlockTag("heat_sources");

	// Blocks that can transfer heat to cooking workstations, if directly above a heat source.
	public static final Tag.Named<Block> HEAT_CONDUCTORS = modBlockTag("heat_conductors");

	// Blocks to which a Cooking Pot/Skillet will render a tray over. Included in HEAT_SOURCES.
	public static final Tag.Named<Block> TRAY_HEAT_SOURCES = modBlockTag("tray_heat_sources");

	// Blocks that accelerate decomposition of Organic Compost if placed adjacent to it.
	public static final Tag.Named<Block> COMPOST_ACTIVATORS = modBlockTag("compost_activators");

	// Blocks in which Mushroom Colonies can keep growing on, if it's dark enough. These blocks cannot form new colonies.
	public static final Tag.Named<Block> MUSHROOM_COLONY_GROWABLE_ON = modBlockTag("mushroom_colony_growable_on");

	// Blocks that should not have their growth boosted by Rich Soil, if planted on it.
	public static final Tag.Named<Block> UNAFFECTED_BY_RICH_SOIL = modBlockTag("unaffected_by_rich_soil");

	// Items that represent the wild form of a farmable crop.
	public static final Tag.Named<Item> WILD_CROPS_ITEM = modItemTag("wild_crops");

	// Items (ideally tools) that can obtain straw when harvesting grassy plants. Populated by all knives by default.
	public static final Tag.Named<Item> STRAW_HARVESTERS = modItemTag("straw_harvesters");

	// Foods that drop from mobs that wolves prey upon (sheep, rabbit and chicken).
	public static final Tag.Named<Item> WOLF_PREY = modItemTag("wolf_prey");

	// Foods and items that serve as filling for Cabbage Rolls
	public static final Tag.Named<Item> CABBAGE_ROLL_INGREDIENTS = modItemTag("cabbage_roll_ingredients");

	// Items commonly held in the off-hand. Cutting Boards won't let them be placed from the off-hand, for convenience.
	public static final Tag.Named<Item> OFFHAND_EQUIPMENT = modItemTag("offhand_equipment");

	// Knife items for game logic.
	public static final Tag.Named<Item> KNIVES = modItemTag("tools/knives");

	// Canvas Signs items for crafting.
	public static final Tag.Named<Item> CANVAS_SIGNS = modItemTag("canvas_signs");

	// Entities that should be able to eat Dog Food when tame. Defaults to tamed Wolves.
	public static final Tag.Named<EntityType<?>> DOG_FOOD_USERS = modEntityTag("dog_food_users");

	// Entities that should be able to eat Horse Feed when tame. Defaults to most vanilla mounts, except Pigs and Striders.
	public static final Tag.Named<EntityType<?>> HORSE_FEED_USERS = modEntityTag("horse_feed_users");

	private static Tag.Named<Item> modItemTag(String path) {
		return ItemTags.bind(FarmersDelight.MODID + ":" + path);
	}

	private static Tag.Named<Block> modBlockTag(String path) {
		return BlockTags.bind(FarmersDelight.MODID + ":" + path);
	}

	private static Tag.Named<EntityType<?>> modEntityTag(String path) {
		return EntityTypeTags.bind(FarmersDelight.MODID + ":" + path);
	}
}
