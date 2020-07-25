package vectorwing.farmersdelight.utils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;

public class Tags
{
	// Forge tags - For broad compatibilities
	public static final Tag<Item> FORGE_KNIVES = new ItemTags.Wrapper(new ResourceLocation("forge", "knives"));

	// Inner tags - For mod-related varieties
	public static final Tag<Block> HEAT_SOURCES = new BlockTags.Wrapper(new ResourceLocation(FarmersDelight.MODID, "heat_sources"));
	public static final Tag<Item> KNIVES = new ItemTags.Wrapper(new ResourceLocation(FarmersDelight.MODID, "knives"));
	public static final Tag<Item> WOLF_PREY = new ItemTags.Wrapper(new ResourceLocation(FarmersDelight.MODID, "wolf_prey"));
}
