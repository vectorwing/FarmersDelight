package vectorwing.farmersdelight.common.tag;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * References to tags belonging to other mods, which Farmer's Delight innately supports.
 * These tags are data generated.
 */
public class CompatibilityTags
{
	// Create
	public static final String CREATE = "create";
	@Deprecated // Legacy compatibility with Create 0.4. Remove on 1.19 and beyond.
	public static final TagKey<Block> CREATE_FAN_HEATERS = externalBlockTag(CREATE, "fan_heaters");
	public static final TagKey<Item> CREATE_UPRIGHT_ON_BELT = externalItemTag(CREATE, "upright_on_belt");

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

	private static TagKey<Item> externalItemTag(String modId, String path) {
		return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(modId, path));
	}

	private static TagKey<Block> externalBlockTag(String modId, String path) {
		return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(modId, path));
	}
}
