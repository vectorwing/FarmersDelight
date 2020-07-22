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
	public static final Tag<Item> FORGE_KNIVES = new ItemTags.Wrapper(new ResourceLocation("forge", "knives"));

	public static final Tag<Block> HEAT_SOURCES = new BlockTags.Wrapper(new ResourceLocation(FarmersDelight.MODID, "heat_sources"));
	public static final Tag<Block> LIT_HEAT_SOURCES = new BlockTags.Wrapper(new ResourceLocation(FarmersDelight.MODID, "lit_heat_sources"));

	public static final Tag<Item> KNIVES = new ItemTags.Wrapper(new ResourceLocation(FarmersDelight.MODID, "knives"));
}
