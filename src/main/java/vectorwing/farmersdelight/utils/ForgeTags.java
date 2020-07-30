package vectorwing.farmersdelight.utils;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class ForgeTags
{
	// GENERAL GROUPS
	public static final Tag<Item> CROPS = tag("crops");
	public static final Tag<Item> SEEDS = tag("seeds");
	public static final Tag<Item> MILK = tag("milk");
	public static final Tag<Item> BREAD = tag("bread");
	public static final Tag<Item> VEGETABLES = tag("vegetables");
	public static final Tag<Item> SALAD_INGREDIENTS = tag("salad_ingredients");

	// TYPES OF ITEMS
	public static final Tag<Item> CROPS_CABBAGE = tag("crops/cabbage");
	public static final Tag<Item> CROPS_TOMATO = tag("crops/tomato");
	public static final Tag<Item> CROPS_ONION = tag("crops/onion");
	public static final Tag<Item> CROPS_RICE = tag("crops/rice");

	private static Tag<Item> tag(String path) {
		return new ItemTags.Wrapper(new ResourceLocation("forge", path));
	}
}
