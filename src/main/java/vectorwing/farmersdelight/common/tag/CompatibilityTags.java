package vectorwing.farmersdelight.common.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * References to tags belonging to other mods, which Farmer's Delight innately supports.
 * These tags are used by other mods for their own mechanics.
 */
public class CompatibilityTags
{
	// Create
	public static final String CREATE = "create";
	public static final TagKey<Block> CREATE_PASSIVE_BOILER_HEATERS = externalBlockTag(CREATE, "passive_boiler_heaters");
	public static final TagKey<Block> CREATE_BRITTLE = externalBlockTag(CREATE, "brittle");
	public static final TagKey<Item> CREATE_UPRIGHT_ON_BELT = externalItemTag(CREATE, "upright_on_belt");

	// Create Crafts and Additions
	public static final String CREATE_CA = "createaddition";
	public static final TagKey<Item> CREATE_CA_PLANT_FOODS = externalItemTag(CREATE_CA, "plant_foods");
	public static final TagKey<Item> CREATE_CA_PLANTS = externalItemTag(CREATE_CA, "plants");

	// Origins
	public static final String ORIGINS = "origins";
	public static final TagKey<Item> ORIGINS_MEAT = externalItemTag(ORIGINS, "meat");

	// Serene Seasons
	public static final String SERENE_SEASONS = "sereneseasons";
	public static final TagKey<Block> SERENE_SEASONS_AUTUMN_CROPS_BLOCK = externalBlockTag(SERENE_SEASONS, "autumn_crops");
	public static final TagKey<Block> SERENE_SEASONS_SPRING_CROPS_BLOCK = externalBlockTag(SERENE_SEASONS, "spring_crops");
	public static final TagKey<Block> SERENE_SEASONS_SUMMER_CROPS_BLOCK = externalBlockTag(SERENE_SEASONS, "summer_crops");
	public static final TagKey<Block> SERENE_SEASONS_WINTER_CROPS_BLOCK = externalBlockTag(SERENE_SEASONS, "winter_crops");
	public static final TagKey<Block> SERENE_SEASONS_UNBREAKABLE_FERTILE_CROPS = externalBlockTag(SERENE_SEASONS, "unbreakable_infertile_crops");
	public static final TagKey<Item> SERENE_SEASONS_AUTUMN_CROPS = externalItemTag(SERENE_SEASONS, "autumn_crops");
	public static final TagKey<Item> SERENE_SEASONS_SPRING_CROPS = externalItemTag(SERENE_SEASONS, "spring_crops");
	public static final TagKey<Item> SERENE_SEASONS_SUMMER_CROPS = externalItemTag(SERENE_SEASONS, "summer_crops");
	public static final TagKey<Item> SERENE_SEASONS_WINTER_CROPS = externalItemTag(SERENE_SEASONS, "winter_crops");

	// Tinker's Construct
	public static final String TINKERS_CONSTRUCT = "tconstruct";
	public static final TagKey<Item> TINKERS_CONSTRUCT_SEEDS = externalItemTag(TINKERS_CONSTRUCT, "seeds");

	private static TagKey<Item> externalItemTag(String modId, String path) {
		return ItemTags.create(ResourceLocation.fromNamespaceAndPath(modId, path));
	}

	private static TagKey<Block> externalBlockTag(String modId, String path) {
		return BlockTags.create(ResourceLocation.fromNamespaceAndPath(modId, path));
	}
}
