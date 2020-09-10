package vectorwing.farmersdelight.utils.tags;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

/**
 * References to tags under the Forge namespace.
 * These tags are generic concepts, meant to be shared with other mods for compatibility.
 */
public class ForgeTags
{
	public static final ITag.INamedTag<Item> CROPS = forgeItemTag("crops");
	public static final ITag.INamedTag<Item> SEEDS = forgeItemTag("seeds");
	public static final ITag.INamedTag<Item> MILK = forgeItemTag("milk");
	public static final ITag.INamedTag<Item> BREAD = forgeItemTag("bread");
	public static final ITag.INamedTag<Item> VEGETABLES = forgeItemTag("vegetables");
	public static final ITag.INamedTag<Item> COOKED_BEEF = forgeItemTag("cooked_beef");
	public static final ITag.INamedTag<Item> COOKED_CHICKEN = forgeItemTag("cooked_chicken");
	public static final ITag.INamedTag<Item> COOKED_EGGS = forgeItemTag("cooked_eggs");
	public static final ITag.INamedTag<Item> SALAD_INGREDIENTS = forgeItemTag("salad_ingredients");
	public static final ITag.INamedTag<Item> CROPS_CABBAGE = forgeItemTag("crops/cabbage");
	public static final ITag.INamedTag<Item> CROPS_TOMATO = forgeItemTag("crops/tomato");
	public static final ITag.INamedTag<Item> CROPS_ONION = forgeItemTag("crops/onion");
	public static final ITag.INamedTag<Item> CROPS_RICE = forgeItemTag("crops/rice");

	public static final ITag.INamedTag<Item> PICKAXES = forgeItemTag("tools/pickaxe");
	public static final ITag.INamedTag<Item> AXES = forgeItemTag("tools/axe");
	public static final ITag.INamedTag<Item> SHEARS = forgeItemTag("tools/shears");
	public static final ITag.INamedTag<Item> KNIVES = forgeItemTag("tools/knife");

	private static ITag.INamedTag<Item> forgeItemTag(String path) {
		return ItemTags.makeWrapperTag(new ResourceLocation("forge", path).toString());
	}

}
