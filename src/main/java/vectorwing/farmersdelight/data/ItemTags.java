package vectorwing.farmersdelight.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import vectorwing.farmersdelight.registry.ModItems;
import vectorwing.farmersdelight.utils.tags.ForgeTags;
import vectorwing.farmersdelight.utils.tags.ModTags;

public class ItemTags extends ItemTagsProvider
{
	public ItemTags(DataGenerator generatorIn, BlockTagsProvider blockTagProvider,String modId, @javax.annotation.Nullable net.minecraftforge.common.data.ExistingFileHelper existingFileHelper) {
		super(generatorIn, blockTagProvider, modId, existingFileHelper);
	}

	@Override
	protected void registerTags() {
		copy(BlockTags.CARPETS, net.minecraft.tags.ItemTags.CARPETS);
		copy(BlockTags.SMALL_FLOWERS, net.minecraft.tags.ItemTags.SMALL_FLOWERS);

		this.registerModTags();
		this.registerForgeTags();
	}


	private void registerModTags() {
		getOrCreateBuilder(ModTags.KNIVES).add(ModItems.FLINT_KNIFE.get(), ModItems.IRON_KNIFE.get(), ModItems.DIAMOND_KNIFE.get(), ModItems.GOLDEN_KNIFE.get());
		getOrCreateBuilder(ModTags.STRAW_HARVESTERS).addTag(ModTags.KNIVES);
		getOrCreateBuilder(ModTags.COMFORT_FOODS).add(Items.MUSHROOM_STEW, Items.BEETROOT_SOUP, Items.RABBIT_STEW);
		getOrCreateBuilder(ModTags.WOLF_PREY).addTag(ForgeTags.RAW_CHICKEN).add(Items.MUTTON, Items.RABBIT);
	}

	@SuppressWarnings("unchecked")
	private void registerForgeTags() {
		getOrCreateBuilder(ForgeTags.BREAD).addTag(ForgeTags.BREAD_WHEAT);
		getOrCreateBuilder(ForgeTags.BREAD_WHEAT).add(Items.BREAD);

		getOrCreateBuilder(ForgeTags.COOKED_BEEF).add(Items.COOKED_BEEF, ModItems.BEEF_PATTY.get());
		getOrCreateBuilder(ForgeTags.COOKED_CHICKEN).add(Items.COOKED_CHICKEN, ModItems.COOKED_CHICKEN_CUTS.get());
		getOrCreateBuilder(ForgeTags.COOKED_EGGS).add(ModItems.FRIED_EGG.get());

		getOrCreateBuilder(ForgeTags.COOKED_FISHES).addTags(ForgeTags.COOKED_FISHES_COD, ForgeTags.COOKED_FISHES_SALMON);
		getOrCreateBuilder(ForgeTags.COOKED_FISHES_COD).add(Items.COOKED_COD, ModItems.COOKED_COD_SLICE.get());
		getOrCreateBuilder(ForgeTags.COOKED_FISHES_SALMON).add(Items.COOKED_SALMON, ModItems.COOKED_SALMON_SLICE.get());

		getOrCreateBuilder(ForgeTags.CROPS).addTags(ForgeTags.CROPS_CABBAGE, ForgeTags.CROPS_ONION, ForgeTags.CROPS_RICE, ForgeTags.CROPS_TOMATO);
		getOrCreateBuilder(ForgeTags.CROPS_CABBAGE).add(ModItems.CABBAGE.get(), ModItems.CABBAGE_LEAF.get());
		getOrCreateBuilder(ForgeTags.CROPS_ONION).add(ModItems.ONION.get());
		getOrCreateBuilder(ForgeTags.CROPS_RICE).add(ModItems.RICE.get());
		getOrCreateBuilder(ForgeTags.CROPS_TOMATO).add(ModItems.TOMATO.get());

		getOrCreateBuilder(ForgeTags.GRAIN).addTags(ForgeTags.GRAIN_WHEAT, ForgeTags.GRAIN_RICE);
		getOrCreateBuilder(ForgeTags.GRAIN_WHEAT).add(Items.WHEAT);
		getOrCreateBuilder(ForgeTags.GRAIN_RICE).add(ModItems.RICE.get());

		getOrCreateBuilder(ForgeTags.MILK).addTags(ForgeTags.MILK_BUCKET, ForgeTags.MILK_BOTTLE);
		getOrCreateBuilder(ForgeTags.MILK_BUCKET).add(Items.MILK_BUCKET);
		getOrCreateBuilder(ForgeTags.MILK_BOTTLE).add(ModItems.MILK_BOTTLE.get());

		getOrCreateBuilder(ForgeTags.PASTA).addTags(ForgeTags.PASTA_RAW_PASTA);
		getOrCreateBuilder(ForgeTags.PASTA_RAW_PASTA).add(ModItems.RAW_PASTA.get());

		getOrCreateBuilder(ForgeTags.RAW_BEEF).add(Items.BEEF, ModItems.MINCED_BEEF.get());
		getOrCreateBuilder(ForgeTags.RAW_CHICKEN).add(Items.CHICKEN, ModItems.CHICKEN_CUTS.get());

		getOrCreateBuilder(ForgeTags.RAW_FISHES).addTags(ForgeTags.RAW_FISHES_COD, ForgeTags.RAW_FISHES_SALMON, ForgeTags.RAW_FISHES_TROPICAL);
		getOrCreateBuilder(ForgeTags.RAW_FISHES_COD).add(Items.COD, ModItems.COD_SLICE.get());
		getOrCreateBuilder(ForgeTags.RAW_FISHES_SALMON).add(Items.SALMON, ModItems.SALMON_SLICE.get());
		getOrCreateBuilder(ForgeTags.RAW_FISHES_TROPICAL).add(Items.TROPICAL_FISH);

		getOrCreateBuilder(ForgeTags.SALAD_INGREDIENTS).addTags(ForgeTags.SALAD_INGREDIENTS_CABBAGE);
		getOrCreateBuilder(ForgeTags.SALAD_INGREDIENTS_CABBAGE).add(ModItems.CABBAGE.get(), ModItems.CABBAGE_LEAF.get());

		getOrCreateBuilder(ForgeTags.SEEDS).addTags(ForgeTags.SEEDS_CABBAGE, ForgeTags.SEEDS_RICE, ForgeTags.SEEDS_TOMATO);
		getOrCreateBuilder(ForgeTags.SEEDS_CABBAGE).add(ModItems.CABBAGE_SEEDS.get());
		getOrCreateBuilder(ForgeTags.SEEDS_RICE).add(ModItems.RICE.get());
		getOrCreateBuilder(ForgeTags.SEEDS_TOMATO).add(ModItems.TOMATO_SEEDS.get());

		getOrCreateBuilder(ForgeTags.VEGETABLES).addTags(ForgeTags.VEGETABLES_BEETROOT, ForgeTags.VEGETABLES_CARROT, ForgeTags.VEGETABLES_ONION, ForgeTags.VEGETABLES_POTATO, ForgeTags.VEGETABLES_TOMATO);
		getOrCreateBuilder(ForgeTags.VEGETABLES_BEETROOT).add(Items.BEETROOT);
		getOrCreateBuilder(ForgeTags.VEGETABLES_CARROT).add(Items.CARROT);
		getOrCreateBuilder(ForgeTags.VEGETABLES_ONION).add(ModItems.ONION.get());
		getOrCreateBuilder(ForgeTags.VEGETABLES_POTATO).add(Items.POTATO);
		getOrCreateBuilder(ForgeTags.VEGETABLES_TOMATO).add(ModItems.TOMATO.get());

		getOrCreateBuilder(ForgeTags.TOOLS).addTags(ForgeTags.TOOLS_AXES, ForgeTags.TOOLS_KNIVES, ForgeTags.TOOLS_PICKAXES, ForgeTags.TOOLS_SHOVELS);
		getOrCreateBuilder(ForgeTags.TOOLS_AXES).add(Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.DIAMOND_AXE, Items.GOLDEN_AXE);
		getOrCreateBuilder(ForgeTags.TOOLS_KNIVES).add(ModItems.FLINT_KNIFE.get(), ModItems.IRON_KNIFE.get(), ModItems.DIAMOND_KNIFE.get(), ModItems.GOLDEN_KNIFE.get());
		getOrCreateBuilder(ForgeTags.TOOLS_PICKAXES).add(Items.WOODEN_PICKAXE, Items.STONE_PICKAXE, Items.IRON_PICKAXE, Items.DIAMOND_PICKAXE, Items.GOLDEN_PICKAXE);
		getOrCreateBuilder(ForgeTags.TOOLS_SHOVELS).add(Items.WOODEN_SHOVEL, Items.STONE_SHOVEL, Items.IRON_SHOVEL, Items.DIAMOND_SHOVEL, Items.GOLDEN_SHOVEL);
	}
}
