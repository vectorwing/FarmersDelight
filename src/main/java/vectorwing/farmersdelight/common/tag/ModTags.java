package vectorwing.farmersdelight.common.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import vectorwing.farmersdelight.FarmersDelight;

/**
 * References to tags under the Farmer's Delight namespace.
 * These tags are used for mod mechanics.
 * <p>
 * For add-ons: I recommend copying these as local tag references in your code, as these are not actual registries. The tags below are subject to changes which may break add-ons referencing them.
 */
public class ModTags
{
	public static class Blocks
	{
		// Blocks which cause Campfires to emit signal smoke when placed underneath them.
		public static final TagKey<Block> CAMPFIRE_SIGNAL_SMOKE = modBlockTag("campfire_signal_smoke");

		// Blocks which accelerate decomposition of Organic Compost if placed adjacent to it.
		public static final TagKey<Block> COMPOST_ACTIVATORS = modBlockTag("compost_activators");

		// Blocks which should drop a Cake Slice when sliced by a knife. Populated by all candle cakes by default.
		public static final TagKey<Block> DROPS_CAKE_SLICE = modBlockTag("drops_cake_slice");

		// Blocks which can transfer heat to cooking workstations, if directly above a heat source.
		public static final TagKey<Block> HEAT_CONDUCTORS = modBlockTag("heat_conductors");

		// Blocks which can heat up cooking workstations.
		public static final TagKey<Block> HEAT_SOURCES = modBlockTag("heat_sources");

		// Blocks which can heat up cooking workstations, while also rendering a tray to support such blocks. Included in HEAT_SOURCES.
		public static final TagKey<Block> TRAY_HEAT_SOURCES = modBlockTag("tray_heat_sources");

		// Blocks in which Mushroom Colonies can keep growing on. These blocks cannot form new colonies.
		public static final TagKey<Block> MUSHROOM_COLONY_GROWABLE_ON = modBlockTag("mushroom_colony_growable_on");

		// Blocks which are planted beneath a soil block. Used by Rich Soil to ensure such blocks can only be bonemealed if planted below it.
		public static final TagKey<Block> PLANTED_FROM_BELOW = modBlockTag("planted_from_below");

		// Blocks which represent a feast: a larger, placeable meal which can serve many portions.
		public static final TagKey<Block> FEASTS = modBlockTag("feasts");

		// Blocks which represent a pie: a placeable dessert which uses a Pie Crust, and usually provides 4 slices.
		public static final TagKey<Block> PIES = modBlockTag("pies");

		// Blocks made mostly of straw. Populates MINEABLE_WITH_KNIFE.
		public static final TagKey<Block> STRAW_BLOCKS = modBlockTag("straw_blocks");

		// Blocks commonly present in biome surfaces. Populated by "minecraft:dirt" and "minecraft:sand" by default.
		public static final TagKey<Block> TERRAIN = modBlockTag("terrain");

		// Blocks which should not have their growth boosted by Rich Soil, if planted on it.
		public static final TagKey<Block> UNAFFECTED_BY_RICH_SOIL = modBlockTag("unaffected_by_rich_soil");

		public static final TagKey<Block> CABINETS = modBlockTag("cabinets");

		public static final TagKey<Block> CABINETS_WOODEN = modBlockTag("cabinets/wooden");

		public static final TagKey<Block> MUSHROOM_COLONIES = modBlockTag("mushroom_colonies");

		public static final TagKey<Block> MINEABLE_WITH_KNIFE = modBlockTag("mineable/knife");

		public static final TagKey<Block> ROPES = modBlockTag("ropes");

		public static final TagKey<Block> WILD_CROPS = modBlockTag("wild_crops");
	}

	public static class Items
	{
		// Items which are compatible with the Backstabbing enchantment. Populated by #tools/knives.
		public static final TagKey<Item> KNIFE_ENCHANTABLE = modItemTag("enchantable/knife");

		// Items that represent a multi-ingredient food which isn't contained in a bowl or plate.
		public static final TagKey<Item> SNACKS = modItemTag("snacks");

		// Items which represent a meal: prepared food contained in a bowl or plate.
		public static final TagKey<Item> MEALS = modItemTag("meals");

		// Items which represent a drink: a bottled consumable that isn't a potion.
		public static final TagKey<Item> DRINKS = modItemTag("drinks");

		// Items which represent sweets: prepared foods made with sugar and/or sweet ingredients. Usually classified as desserts.
		public static final TagKey<Item> SWEETS = modItemTag("sweets");

		// Items which represent a feast: a larger, placeable meal which can serve many portions.
		// The feast servings exist in the MEALS tag.
		public static final TagKey<Item> FEASTS = modItemTag("feasts");

		// Items which represent a pie: a placeable dessert which uses a Pie Crust, and usually provides 4 slices.
		public static final TagKey<Item> PIES = modItemTag("pies");

		// Items which should render in 2D, laying down flat, when placed on the Cutting Board.
		public static final TagKey<Item> FLAT_ON_CUTTING_BOARD = modItemTag("flat_on_cutting_board");

		// Items commonly used to contain products. Used by the Cooking Pot for sneak-clicking actions.
		public static final TagKey<Item> SERVING_CONTAINERS = modItemTag("serving_containers");

		// Items (ideally tools) which can obtain straw when harvesting grassy plants. Populated by all knives by default.
		public static final TagKey<Item> STRAW_HARVESTERS = modItemTag("straw_harvesters");

		public static final TagKey<Item> CABINETS = modItemTag("cabinets");

		public static final TagKey<Item> CABINETS_WOODEN = modItemTag("cabinets/wooden");

		public static final TagKey<Item> CANVAS_SIGNS = modItemTag("canvas_signs");

		public static final TagKey<Item> HANGING_CANVAS_SIGNS = modItemTag("hanging_canvas_signs");

		public static final TagKey<Item> KNIVES = modItemTag("tools/knives");

		public static final TagKey<Item> MUSHROOM_COLONIES = modItemTag("mushroom_colonies");

		public static final TagKey<Item> WILD_CROPS = modItemTag("wild_crops");
	}

	public static class EntityTypes
	{
		// Entities which should be able to eat Dog Food when tame. Defaults to tamed Wolves.
		public static final TagKey<EntityType<?>> DOG_FOOD_USERS = modEntityTag("dog_food_users");

		// Entities which should be able to eat Horse Feed when tame. Defaults to most vanilla mounts, except Pigs and Striders.
		public static final TagKey<EntityType<?>> HORSE_FEED_USERS = modEntityTag("horse_feed_users");

		// Entities which should be given a TemptGoal for Horse Feed, allowing players to call them with it.
		public static final TagKey<EntityType<?>> HORSE_FEED_TEMPTED = modEntityTag("horse_feed_tempted");
	}

	private static TagKey<Item> modItemTag(String path) {
		return ItemTags.create(ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, path));
	}

	private static TagKey<Block> modBlockTag(String path) {
		return BlockTags.create(ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, path));
	}

	private static TagKey<EntityType<?>> modEntityTag(String path) {
		return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, path));
	}

	/**
	 * The following tag references are deprecated, and should no longer be used by add-ons. Please use the sub-classed tag references moving forward, or create local tag references in your add-on.
	 * They were re-introduced to prevent crashes, but they will be removed in the next major/minor release.
	 */
	@Deprecated(forRemoval = true)
	public static final TagKey<Block> CAMPFIRE_SIGNAL_SMOKE = modBlockTag("campfire_signal_smoke");
	@Deprecated(forRemoval = true)
	public static final TagKey<Block> COMPOST_ACTIVATORS = modBlockTag("compost_activators");
	@Deprecated(forRemoval = true)
	public static final TagKey<Block> DROPS_CAKE_SLICE = modBlockTag("drops_cake_slice");
	@Deprecated(forRemoval = true)
	public static final TagKey<Block> HEAT_CONDUCTORS = modBlockTag("heat_conductors");
	@Deprecated(forRemoval = true)
	public static final TagKey<Block> HEAT_SOURCES = modBlockTag("heat_sources");
	@Deprecated(forRemoval = true)
	public static final TagKey<Block> TRAY_HEAT_SOURCES = modBlockTag("tray_heat_sources");
	@Deprecated(forRemoval = true)
	public static final TagKey<Block> MUSHROOM_COLONY_GROWABLE_ON = modBlockTag("mushroom_colony_growable_on");
	@Deprecated(forRemoval = true)
	public static final TagKey<Block> MINEABLE_WITH_KNIFE = modBlockTag("mineable/knife");
	@Deprecated(forRemoval = true)
	public static final TagKey<Block> TERRAIN = modBlockTag("terrain");
	@Deprecated(forRemoval = true)
	public static final TagKey<Block> STRAW_BLOCKS = modBlockTag("straw_blocks");
	@Deprecated(forRemoval = true)
	public static final TagKey<Block> WILD_CROPS = modBlockTag("wild_crops");
	@Deprecated(forRemoval = true)
	public static final TagKey<Block> CABINETS = modBlockTag("cabinets");
	@Deprecated(forRemoval = true)
	public static final TagKey<Block> WOODEN_CABINETS = modBlockTag("cabinets/wooden");
	@Deprecated(forRemoval = true)
	public static final TagKey<Block> MUSHROOM_COLONIES = modBlockTag("mushroom_colonies");
	@Deprecated(forRemoval = true)
	public static final TagKey<Block> ROPES = modBlockTag("ropes");
	@Deprecated(forRemoval = true)
	public static final TagKey<Block> UNAFFECTED_BY_RICH_SOIL = modBlockTag("unaffected_by_rich_soil");
	@Deprecated(forRemoval = true)
	public static final TagKey<Item> MEALS = modItemTag("meals");
	@Deprecated(forRemoval = true)
	public static final TagKey<Item> DRINKS = modItemTag("drinks");
	@Deprecated(forRemoval = true)
	public static final TagKey<Item> FEASTS = modItemTag("feasts");
	@Deprecated(forRemoval = true)
	public static final TagKey<Item> WILD_CROPS_ITEM = modItemTag("wild_crops");
	@Deprecated(forRemoval = true)
	public static final TagKey<Item> STRAW_HARVESTERS = modItemTag("straw_harvesters");
	@Deprecated(forRemoval = true)
	public static final TagKey<Item> KNIVES = modItemTag("tools/knives");
	@Deprecated(forRemoval = true)
	public static final TagKey<Item> CANVAS_SIGNS = modItemTag("canvas_signs");
	@Deprecated(forRemoval = true)
	public static final TagKey<Item> HANGING_CANVAS_SIGNS = modItemTag("hanging_canvas_signs");
	@Deprecated(forRemoval = true)
	public static final TagKey<Item> WOODEN_CABINET_ITEMS = modItemTag("cabinets/wooden");
	@Deprecated(forRemoval = true)
	public static final TagKey<Item> CABINET_ITEMS = modItemTag("cabinets");
	@Deprecated(forRemoval = true)
	public static final TagKey<Item> MUSHROOM_COLONY_ITEMS = modItemTag("mushroom_colonies");
	@Deprecated(forRemoval = true)
	public static final TagKey<Item> SERVING_CONTAINERS = modItemTag("serving_containers");
	@Deprecated(forRemoval = true)
	public static final TagKey<Item> FLAT_ON_CUTTING_BOARD = modItemTag("flat_on_cutting_board");
	@Deprecated(forRemoval = true)
	public static final TagKey<EntityType<?>> DOG_FOOD_USERS = modEntityTag("dog_food_users");
	@Deprecated(forRemoval = true)
	public static final TagKey<EntityType<?>> HORSE_FEED_USERS = modEntityTag("horse_feed_users");
	@Deprecated(forRemoval = true)
	public static final TagKey<EntityType<?>> HORSE_FEED_TEMPTED = modEntityTag("horse_feed_tempted");
}
