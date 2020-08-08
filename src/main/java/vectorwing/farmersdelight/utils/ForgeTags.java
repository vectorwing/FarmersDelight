package vectorwing.farmersdelight.utils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;

public class ForgeTags
{
	// GENERAL GROUPS
	public static final Tag<Item> CROPS = forgeTag("crops");
	public static final Tag<Item> SEEDS = forgeTag("seeds");
	public static final Tag<Item> MILK = forgeTag("milk");
	public static final Tag<Item> BREAD = forgeTag("bread");
	public static final Tag<Item> VEGETABLES = forgeTag("vegetables");
	public static final Tag<Item> SALAD_INGREDIENTS = forgeTag("salad_ingredients");

	// TYPES OF ITEMS
	public static final Tag<Item> CROPS_CABBAGE = forgeTag("crops/cabbage");
	public static final Tag<Item> CROPS_TOMATO = forgeTag("crops/tomato");
	public static final Tag<Item> CROPS_ONION = forgeTag("crops/onion");
	public static final Tag<Item> CROPS_RICE = forgeTag("crops/rice");

	public static final Tag<Item> KNIVES = forgeTag("knives");

	public static final Tag<Block> HEAT_SOURCES = new BlockTags.Wrapper(new ResourceLocation(FarmersDelight.MODID, "heat_sources"));
	public static final Tag<Item> WOLF_PREY = new ItemTags.Wrapper(new ResourceLocation(FarmersDelight.MODID, "wolf_prey"));

	private static Tag<Item> forgeTag(String path) {
		return new ItemTags.Wrapper(new ResourceLocation("forge", path));
	}
}
