package vectorwing.farmersdelight.utils.tags;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

/**
 * References to tags under the Forge namespace.
 * These tags are generic concepts, meant to be shared with other mods for compatibility.
 */
public class ForgeTags
{
	public static final Tag<Item> CROPS = forgeItemTag("crops");
	public static final Tag<Item> SEEDS = forgeItemTag("seeds");
	public static final Tag<Item> MILK = forgeItemTag("milk");
	public static final Tag<Item> BREAD = forgeItemTag("bread");
	public static final Tag<Item> VEGETABLES = forgeItemTag("vegetables");
	public static final Tag<Item> COOKED_BEEF = forgeItemTag("cooked_beef");
	public static final Tag<Item> COOKED_CHICKEN = forgeItemTag("cooked_chicken");
	public static final Tag<Item> COOKED_EGGS = forgeItemTag("cooked_eggs");
	public static final Tag<Item> SALAD_INGREDIENTS = forgeItemTag("salad_ingredients");
	public static final Tag<Item> CROPS_CABBAGE = forgeItemTag("crops/cabbage");
	public static final Tag<Item> CROPS_TOMATO = forgeItemTag("crops/tomato");
	public static final Tag<Item> CROPS_ONION = forgeItemTag("crops/onion");
	public static final Tag<Item> CROPS_RICE = forgeItemTag("crops/rice");

	public static final Tag<Item> PICKAXES = forgeItemTag("tools/pickaxes");
	public static final Tag<Item> AXES = forgeItemTag("tools/axes");
	public static final Tag<Item> SHEARS = forgeItemTag("tools/shears");
	public static final Tag<Item> KNIVES = forgeItemTag("tools/knives");

	private static Tag<Item> forgeItemTag(String path) {
		return new ItemTags.Wrapper(new ResourceLocation("forge", path));
	}

}
