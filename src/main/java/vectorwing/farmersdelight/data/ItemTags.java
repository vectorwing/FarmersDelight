package vectorwing.farmersdelight.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.util.ResourceLocation;
import vectorwing.farmersdelight.registry.ModItems;
import vectorwing.farmersdelight.utils.tags.ForgeTags;
import vectorwing.farmersdelight.utils.tags.ModTags;
import net.minecraft.item.Items;

public class ItemTags extends ItemTagsProvider {
	public ItemTags(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void registerTags() {
		this.registerModTags();
		this.registerForgeTags();
	}


	private void registerModTags() {
		getBuilder(ModTags.KNIVES).add(ModItems.FLINT_KNIFE.get(), ModItems.IRON_KNIFE.get(), ModItems.DIAMOND_KNIFE.get(), ModItems.GOLDEN_KNIFE.get());
		getBuilder(ModTags.STRAW_HARVESTERS).add(ModTags.KNIVES);
		getBuilder(ModTags.COMFORT_FOODS).add(Items.MUSHROOM_STEW, Items.BEETROOT_SOUP, Items.RABBIT_STEW);
		getBuilder(ModTags.WOLF_PREY).add(ForgeTags.RAW_CHICKEN).add(Items.MUTTON, Items.RABBIT);
	}

	private void registerForgeTags() {
		getBuilder(ForgeTags.BREAD).add(ForgeTags.BREAD_WHEAT);
		getBuilder(ForgeTags.BREAD_WHEAT).add(Items.BREAD);

		getBuilder(ForgeTags.COOKED_BEEF).add(Items.COOKED_BEEF, ModItems.BEEF_PATTY.get());
		getBuilder(ForgeTags.COOKED_CHICKEN).add(Items.COOKED_CHICKEN, ModItems.COOKED_CHICKEN_CUTS.get());
		getBuilder(ForgeTags.COOKED_EGGS).add(ModItems.FRIED_EGG.get())
				.addOptional(net.minecraft.tags.ItemTags.getCollection(), new ResourceLocation("incubation", "fried_egg"));

		getBuilder(ForgeTags.COOKED_FISHES).add(ForgeTags.COOKED_FISHES_COD, ForgeTags.COOKED_FISHES_SALMON);
		getBuilder(ForgeTags.COOKED_FISHES_COD).add(Items.COOKED_COD, ModItems.COOKED_COD_SLICE.get());
		getBuilder(ForgeTags.COOKED_FISHES_SALMON).add(Items.COOKED_SALMON, ModItems.COOKED_SALMON_SLICE.get());

		getBuilder(ForgeTags.CROPS).add(ForgeTags.CROPS_CABBAGE, ForgeTags.CROPS_ONION, ForgeTags.CROPS_RICE, ForgeTags.CROPS_TOMATO);
		getBuilder(ForgeTags.CROPS_CABBAGE).add(ModItems.CABBAGE.get(), ModItems.CABBAGE_LEAF.get());
		getBuilder(ForgeTags.CROPS_ONION).add(ModItems.ONION.get());
		getBuilder(ForgeTags.CROPS_RICE).add(ModItems.RICE.get())
				.addOptional(net.minecraft.tags.ItemTags.getCollection(), new ResourceLocation("swampexpansion", "rice"));
		getBuilder(ForgeTags.CROPS_TOMATO).add(ModItems.TOMATO.get());

		getBuilder(ForgeTags.GRAIN).add(ForgeTags.GRAIN_WHEAT, ForgeTags.GRAIN_RICE);
		getBuilder(ForgeTags.GRAIN_WHEAT).add(Items.WHEAT);
		getBuilder(ForgeTags.GRAIN_RICE).add(ModItems.RICE.get());

		getBuilder(ForgeTags.MILK).add(ForgeTags.MILK_BUCKET, ForgeTags.MILK_BOTTLE);
		getBuilder(ForgeTags.MILK_BUCKET).add(Items.MILK_BUCKET);
		getBuilder(ForgeTags.MILK_BOTTLE).add(ModItems.MILK_BOTTLE.get());

		getBuilder(ForgeTags.PASTA).add(ForgeTags.PASTA_RAW_PASTA);
		getBuilder(ForgeTags.PASTA_RAW_PASTA).add(ModItems.RAW_PASTA.get());

		getBuilder(ForgeTags.RAW_BEEF).add(Items.BEEF, ModItems.MINCED_BEEF.get());
		getBuilder(ForgeTags.RAW_CHICKEN).add(Items.CHICKEN, ModItems.CHICKEN_CUTS.get());

		getBuilder(ForgeTags.RAW_FISHES).add(ForgeTags.RAW_FISHES_COD, ForgeTags.RAW_FISHES_SALMON);
		getBuilder(ForgeTags.RAW_FISHES_COD).add(Items.COD, ModItems.COD_SLICE.get());
		getBuilder(ForgeTags.RAW_FISHES_SALMON).add(Items.SALMON, ModItems.SALMON_SLICE.get());
		getBuilder(ForgeTags.RAW_FISHES_TROPICAL).add(Items.TROPICAL_FISH);

		getBuilder(ForgeTags.SALAD_INGREDIENTS).add(ForgeTags.SALAD_INGREDIENTS_CABBAGE);
		getBuilder(ForgeTags.SALAD_INGREDIENTS_CABBAGE).add(ModItems.CABBAGE.get(), ModItems.CABBAGE_LEAF.get());

		getBuilder(ForgeTags.SEEDS).add(ForgeTags.SEEDS_CABBAGE, ForgeTags.SEEDS_RICE, ForgeTags.SEEDS_TOMATO);
		getBuilder(ForgeTags.SEEDS_CABBAGE).add(ModItems.CABBAGE_SEEDS.get());
		getBuilder(ForgeTags.SEEDS_RICE).add(ModItems.TOMATO_SEEDS.get());
		getBuilder(ForgeTags.SEEDS_TOMATO).add(ModItems.RICE.get());

		getBuilder(ForgeTags.VEGETABLES).add(ForgeTags.VEGETABLES_BEETROOT, ForgeTags.VEGETABLES_CARROT, ForgeTags.VEGETABLES_ONION, ForgeTags.VEGETABLES_POTATO, ForgeTags.VEGETABLES_TOMATO);
		getBuilder(ForgeTags.VEGETABLES_BEETROOT).add(Items.BEETROOT);
		getBuilder(ForgeTags.VEGETABLES_CARROT).add(Items.CARROT);
		getBuilder(ForgeTags.VEGETABLES_ONION).add(ModItems.ONION.get());
		getBuilder(ForgeTags.VEGETABLES_POTATO).add(Items.POTATO);
		getBuilder(ForgeTags.VEGETABLES_TOMATO).add(ModItems.TOMATO.get());

		getBuilder(ForgeTags.TOOLS).add(ForgeTags.TOOLS_AXES, ForgeTags.TOOLS_KNIVES, ForgeTags.TOOLS_PICKAXES, ForgeTags.TOOLS_SHEARS, ForgeTags.TOOLS_SHOVELS);
		getBuilder(ForgeTags.TOOLS_AXES).add(Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.DIAMOND_AXE, Items.GOLDEN_AXE);
		getBuilder(ForgeTags.TOOLS_KNIVES).add(ModItems.FLINT_KNIFE.get(), ModItems.IRON_KNIFE.get(), ModItems.DIAMOND_KNIFE.get(), ModItems.GOLDEN_KNIFE.get());
		getBuilder(ForgeTags.TOOLS_PICKAXES).add(Items.WOODEN_PICKAXE, Items.STONE_PICKAXE, Items.IRON_PICKAXE, Items.DIAMOND_PICKAXE, Items.GOLDEN_PICKAXE);
		getBuilder(ForgeTags.TOOLS_SHEARS).add(Items.SHEARS);
		getBuilder(ForgeTags.TOOLS_SHOVELS).add(Items.WOODEN_SHOVEL, Items.STONE_SHOVEL, Items.IRON_SHOVEL, Items.DIAMOND_SHOVEL, Items.GOLDEN_SHOVEL);
	}
}
