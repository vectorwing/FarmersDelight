package vectorwing.farmersdelight.common.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * References to common tags under the Forge namespace.
 * These tags are generally used for crafting recipes across different mods.
 * <p>
 * For add-ons: I recommend copying these as local tag references in your code, as these are not actual registries. The tags below are subject to changes which may break add-ons referencing them.
 */
public class CommonTags
{
	public static class Blocks
	{
		public static final TagKey<Block> MINEABLE_WITH_KNIFE = forgeBlockTag("mineable/knife");

		public static final TagKey<Block> STORAGE_BLOCKS_CARROT = forgeBlockTag("storage_blocks/carrot");
		public static final TagKey<Block> STORAGE_BLOCKS_POTATO = forgeBlockTag("storage_blocks/potato");
		public static final TagKey<Block> STORAGE_BLOCKS_BEETROOT = forgeBlockTag("storage_blocks/beetroot");
		public static final TagKey<Block> STORAGE_BLOCKS_CABBAGE = forgeBlockTag("storage_blocks/cabbage");
		public static final TagKey<Block> STORAGE_BLOCKS_TOMATO = forgeBlockTag("storage_blocks/tomato");
		public static final TagKey<Block> STORAGE_BLOCKS_ONION = forgeBlockTag("storage_blocks/onion");
		public static final TagKey<Block> STORAGE_BLOCKS_RICE = forgeBlockTag("storage_blocks/rice");
		public static final TagKey<Block> STORAGE_BLOCKS_RICE_PANICLE = forgeBlockTag("storage_blocks/rice_panicle");
		public static final TagKey<Block> STORAGE_BLOCKS_STRAW = forgeBlockTag("storage_blocks/straw");
	}

	public static class Items
	{
		public static final TagKey<Item> BERRIES = forgeItemTag("berries");

		public static final TagKey<Item> BREAD = forgeItemTag("bread");
		public static final TagKey<Item> BREAD_WHEAT = forgeItemTag("bread/wheat");

		public static final TagKey<Item> COOKED_BACON = forgeItemTag("cooked_bacon");
		public static final TagKey<Item> COOKED_BEEF = forgeItemTag("cooked_beef");
		public static final TagKey<Item> COOKED_CHICKEN = forgeItemTag("cooked_chicken");
		public static final TagKey<Item> COOKED_PORK = forgeItemTag("cooked_pork");
		public static final TagKey<Item> COOKED_MUTTON = forgeItemTag("cooked_mutton");
		public static final TagKey<Item> COOKED_EGGS = forgeItemTag("cooked_eggs");
		public static final TagKey<Item> COOKED_FISHES = forgeItemTag("cooked_fishes");
		public static final TagKey<Item> COOKED_FISHES_COD = forgeItemTag("cooked_fishes/cod");
		public static final TagKey<Item> COOKED_FISHES_SALMON = forgeItemTag("cooked_fishes/salmon");

		public static final TagKey<Item> CROPS = forgeItemTag("crops");
		public static final TagKey<Item> CROPS_CABBAGE = forgeItemTag("crops/cabbage");
		public static final TagKey<Item> CROPS_ONION = forgeItemTag("crops/onion");
		public static final TagKey<Item> CROPS_RICE = forgeItemTag("crops/rice");
		public static final TagKey<Item> CROPS_TOMATO = forgeItemTag("crops/tomato");

		public static final TagKey<Item> DOUGH = forgeItemTag("dough");
		public static final TagKey<Item> DOUGH_WHEAT = forgeItemTag("dough/wheat");

		public static final TagKey<Item> EGGS = forgeItemTag("eggs");

		public static final TagKey<Item> GRAIN = forgeItemTag("grain");
		public static final TagKey<Item> GRAIN_WHEAT = forgeItemTag("grain/wheat");
		public static final TagKey<Item> GRAIN_RICE = forgeItemTag("grain/rice");

		public static final TagKey<Item> MILK = forgeItemTag("milk");
		public static final TagKey<Item> MILK_BUCKET = forgeItemTag("milk/milk");
		public static final TagKey<Item> MILK_BOTTLE = forgeItemTag("milk/milk_bottle");

		public static final TagKey<Item> PASTA = forgeItemTag("pasta");
		public static final TagKey<Item> PASTA_RAW_PASTA = forgeItemTag("pasta/raw_pasta");

		public static final TagKey<Item> RAW_MEAT = forgeItemTag("raw_meat");

		public static final TagKey<Item> RAW_BACON = forgeItemTag("raw_bacon");
		public static final TagKey<Item> RAW_BEEF = forgeItemTag("raw_beef");
		public static final TagKey<Item> RAW_CHICKEN = forgeItemTag("raw_chicken");
		public static final TagKey<Item> RAW_PORK = forgeItemTag("raw_pork");
		public static final TagKey<Item> RAW_MUTTON = forgeItemTag("raw_mutton");
		public static final TagKey<Item> RAW_FISHES = forgeItemTag("raw_fishes");
		public static final TagKey<Item> RAW_FISHES_COD = forgeItemTag("raw_fishes/cod");
		public static final TagKey<Item> RAW_FISHES_SALMON = forgeItemTag("raw_fishes/salmon");
		public static final TagKey<Item> RAW_FISHES_TROPICAL = forgeItemTag("raw_fishes/tropical_fish");

		public static final TagKey<Item> SALAD_INGREDIENTS = forgeItemTag("salad_ingredients");
		public static final TagKey<Item> SALAD_INGREDIENTS_CABBAGE = forgeItemTag("salad_ingredients/cabbage");

		public static final TagKey<Item> SEEDS = forgeItemTag("seeds");
		public static final TagKey<Item> SEEDS_CABBAGE = forgeItemTag("seeds/cabbage");
		public static final TagKey<Item> SEEDS_RICE = forgeItemTag("seeds/rice");
		public static final TagKey<Item> SEEDS_TOMATO = forgeItemTag("seeds/tomato");

		public static final TagKey<Item> VEGETABLES = forgeItemTag("vegetables");
		public static final TagKey<Item> VEGETABLES_BEETROOT = forgeItemTag("vegetables/beetroot");
		public static final TagKey<Item> VEGETABLES_CARROT = forgeItemTag("vegetables/carrot");
		public static final TagKey<Item> VEGETABLES_ONION = forgeItemTag("vegetables/onion");
		public static final TagKey<Item> VEGETABLES_POTATO = forgeItemTag("vegetables/potato");
		public static final TagKey<Item> VEGETABLES_TOMATO = forgeItemTag("vegetables/tomato");

		public static final TagKey<Item> TOOLS = forgeItemTag("tools");
		public static final TagKey<Item> TOOLS_AXES = forgeItemTag("tools/axes");
		public static final TagKey<Item> TOOLS_KNIVES = forgeItemTag("tools/knives");
		public static final TagKey<Item> TOOLS_PICKAXES = forgeItemTag("tools/pickaxes");
		public static final TagKey<Item> TOOLS_SHOVELS = forgeItemTag("tools/shovels");

		public static final TagKey<Item> BUCKETS_WATER = forgeItemTag("buckets/water");

		public static final TagKey<Item> STORAGE_BLOCKS_CARROT = forgeItemTag("storage_blocks/carrot");
		public static final TagKey<Item> STORAGE_BLOCKS_POTATO = forgeItemTag("storage_blocks/potato");
		public static final TagKey<Item> STORAGE_BLOCKS_BEETROOT = forgeItemTag("storage_blocks/beetroot");
		public static final TagKey<Item> STORAGE_BLOCKS_CABBAGE = forgeItemTag("storage_blocks/cabbage");
		public static final TagKey<Item> STORAGE_BLOCKS_TOMATO = forgeItemTag("storage_blocks/tomato");
		public static final TagKey<Item> STORAGE_BLOCKS_ONION = forgeItemTag("storage_blocks/onion");
		public static final TagKey<Item> STORAGE_BLOCKS_RICE = forgeItemTag("storage_blocks/rice");
		public static final TagKey<Item> STORAGE_BLOCKS_RICE_PANICLE = forgeItemTag("storage_blocks/rice_panicle");
		public static final TagKey<Item> STORAGE_BLOCKS_STRAW = forgeItemTag("storage_blocks/straw");
	}

	private static TagKey<Block> forgeBlockTag(String path) {
		return BlockTags.create(new ResourceLocation("forge", path));
	}

	private static TagKey<Item> forgeItemTag(String path) {
		return ItemTags.create(new ResourceLocation("forge", path));
	}
}
