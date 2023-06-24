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
//	public static final TagKey<Block> CREATE_PASSIVE_BOILER_HEATERS = externalBlockTag("create", "passive_boiler_heaters");

	@Deprecated // Legacy compatibility with Create 0.4. Remove on 1.19 and beyond.
	public static final TagKey<Block> CREATE_FAN_HEATERS = externalBlockTag("create", "fan_heaters");
	public static final TagKey<Item> CREATE_UPRIGHT_ON_BELT = externalItemTag("create", "upright_on_belt");

	// Origins
	public static final TagKey<Item> ORIGINS_MEAT = externalItemTag("origins", "meat");

	// Serene Seasons

	private static TagKey<Item> externalItemTag(String modId, String path) {
		return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(modId, path));
	}

	private static TagKey<Block> externalBlockTag(String modId, String path) {
		return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(modId, path));
	}
}
