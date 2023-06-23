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
