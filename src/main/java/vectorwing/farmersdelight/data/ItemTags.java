package vectorwing.farmersdelight.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CompatibilityTags;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ItemTags extends ItemTagsProvider
{
	public ItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, provider, blockTagProvider, FarmersDelight.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		copy(ModTags.Blocks.WILD_CROPS, ModTags.Items.WILD_CROPS);
		copy(BlockTags.SMALL_FLOWERS, net.minecraft.tags.ItemTags.SMALL_FLOWERS);

		this.registerMinecraftTags();
		this.registerModTags();
		this.registerForgeTags();
		this.registerCompatibilityTags();
	}

	private void registerMinecraftTags() {
		tag(net.minecraft.tags.ItemTags.TOOLS).addTag(ModTags.Items.KNIVES);
		tag(net.minecraft.tags.ItemTags.TALL_FLOWERS).add(ModItems.WILD_RICE.get());
		tag(net.minecraft.tags.ItemTags.PIGLIN_LOVED).add(ModItems.GOLDEN_KNIFE.get());
		tag(net.minecraft.tags.ItemTags.SIGNS).addTag(ModTags.Items.CANVAS_SIGNS);
		tag(net.minecraft.tags.ItemTags.HANGING_SIGNS).addTag(ModTags.Items.HANGING_CANVAS_SIGNS);
		tag(net.minecraft.tags.ItemTags.VILLAGER_PLANTABLE_SEEDS)
				.add(ModItems.CABBAGE_SEEDS.get())
				.add(ModItems.TOMATO_SEEDS.get())
				.add(ModItems.ONION.get());
	}

	private void registerModTags() {
		tag(ModTags.Items.MEALS).add(
				ModItems.MIXED_SALAD.get(),
				ModItems.COOKED_RICE.get(),
				ModItems.BONE_BROTH.get(),
				ModItems.BEEF_STEW.get(),
				ModItems.VEGETABLE_SOUP.get(),
				ModItems.FISH_STEW.get(),
				ModItems.CHICKEN_SOUP.get(),
				ModItems.FRIED_RICE.get(),
				ModItems.PUMPKIN_SOUP.get(),
				ModItems.BAKED_COD_STEW.get(),
				ModItems.NOODLE_SOUP.get(),
				ModItems.BACON_AND_EGGS.get(),
				ModItems.RATATOUILLE.get(),
				ModItems.STEAK_AND_POTATOES.get(),
				ModItems.PASTA_WITH_MEATBALLS.get(),
				ModItems.PASTA_WITH_MUTTON_CHOP.get(),
				ModItems.MUSHROOM_RICE.get(),
				ModItems.ROASTED_MUTTON_CHOPS.get(),
				ModItems.VEGETABLE_NOODLES.get(),
				ModItems.SQUID_INK_PASTA.get(),
				ModItems.GRILLED_SALMON.get(),
				ModItems.ROAST_CHICKEN.get(),
				ModItems.STUFFED_PUMPKIN.get(),
				ModItems.HONEY_GLAZED_HAM.get(),
				ModItems.SHEPHERDS_PIE.get()
		);
		tag(ModTags.Items.DRINKS).add(
				ModItems.MILK_BOTTLE.get(),
				ModItems.APPLE_CIDER.get(),
				ModItems.MELON_JUICE.get(),
				ModItems.HOT_COCOA.get()
		);
		tag(ModTags.Items.FEASTS).add(
				ModItems.ROAST_CHICKEN_BLOCK.get(),
				ModItems.STUFFED_PUMPKIN_BLOCK.get(),
				ModItems.SHEPHERDS_PIE_BLOCK.get(),
				ModItems.HONEY_GLAZED_HAM_BLOCK.get(),
				ModItems.RICE_ROLL_MEDLEY_BLOCK.get()
		);
		tag(ModTags.Items.KNIVES).add(ModItems.FLINT_KNIFE.get(), ModItems.IRON_KNIFE.get(), ModItems.DIAMOND_KNIFE.get(), ModItems.GOLDEN_KNIFE.get(), ModItems.NETHERITE_KNIFE.get());
		tag(ModTags.Items.STRAW_HARVESTERS).addTag(ModTags.Items.KNIVES);
		tag(ModTags.Items.CABBAGE_ROLL_INGREDIENTS).addTag(CommonTags.Items.RAW_PORK).addTag(CommonTags.Items.RAW_FISHES).addTag(CommonTags.Items.RAW_CHICKEN).addTag(CommonTags.Items.RAW_BEEF).addTag(CommonTags.Items.RAW_MUTTON).addTag(CommonTags.Items.EGGS).addTag(Tags.Items.MUSHROOMS).add(Items.CARROT, Items.POTATO, Items.BEETROOT);
		tag(ModTags.Items.CANVAS_SIGNS)
				.add(ModItems.CANVAS_SIGN.get())
				.add(ModItems.WHITE_CANVAS_SIGN.get())
				.add(ModItems.ORANGE_CANVAS_SIGN.get())
				.add(ModItems.MAGENTA_CANVAS_SIGN.get())
				.add(ModItems.LIGHT_BLUE_CANVAS_SIGN.get())
				.add(ModItems.YELLOW_CANVAS_SIGN.get())
				.add(ModItems.LIME_CANVAS_SIGN.get())
				.add(ModItems.PINK_CANVAS_SIGN.get())
				.add(ModItems.GRAY_CANVAS_SIGN.get())
				.add(ModItems.LIGHT_GRAY_CANVAS_SIGN.get())
				.add(ModItems.CYAN_CANVAS_SIGN.get())
				.add(ModItems.PURPLE_CANVAS_SIGN.get())
				.add(ModItems.BLUE_CANVAS_SIGN.get())
				.add(ModItems.BROWN_CANVAS_SIGN.get())
				.add(ModItems.GREEN_CANVAS_SIGN.get())
				.add(ModItems.RED_CANVAS_SIGN.get())
				.add(ModItems.BLACK_CANVAS_SIGN.get());
		tag(ModTags.Items.HANGING_CANVAS_SIGNS)
				.add(ModItems.HANGING_CANVAS_SIGN.get())
				.add(ModItems.WHITE_HANGING_CANVAS_SIGN.get())
				.add(ModItems.ORANGE_HANGING_CANVAS_SIGN.get())
				.add(ModItems.MAGENTA_HANGING_CANVAS_SIGN.get())
				.add(ModItems.LIGHT_BLUE_HANGING_CANVAS_SIGN.get())
				.add(ModItems.YELLOW_HANGING_CANVAS_SIGN.get())
				.add(ModItems.LIME_HANGING_CANVAS_SIGN.get())
				.add(ModItems.PINK_HANGING_CANVAS_SIGN.get())
				.add(ModItems.GRAY_HANGING_CANVAS_SIGN.get())
				.add(ModItems.LIGHT_GRAY_HANGING_CANVAS_SIGN.get())
				.add(ModItems.CYAN_HANGING_CANVAS_SIGN.get())
				.add(ModItems.PURPLE_HANGING_CANVAS_SIGN.get())
				.add(ModItems.BLUE_HANGING_CANVAS_SIGN.get())
				.add(ModItems.BROWN_HANGING_CANVAS_SIGN.get())
				.add(ModItems.GREEN_HANGING_CANVAS_SIGN.get())
				.add(ModItems.RED_HANGING_CANVAS_SIGN.get())
				.add(ModItems.BLACK_HANGING_CANVAS_SIGN.get());
		copy(ModTags.Blocks.CABINETS, ModTags.Items.CABINETS);
		copy(ModTags.Blocks.CABINETS_WOODEN, ModTags.Items.CABINETS_WOODEN);

		copy(ModTags.Blocks.MUSHROOM_COLONIES, ModTags.Items.MUSHROOM_COLONIES);

		tag(ModTags.Items.SERVING_CONTAINERS).add(Items.BOWL, Items.GLASS_BOTTLE, Items.BUCKET);
		tag(ModTags.Items.FLAT_ON_CUTTING_BOARD).add(Items.TRIDENT, Items.SPYGLASS)
				.addOptional(new ResourceLocation("supplementaries:quiver"))
				.addOptional(new ResourceLocation("autumnity:turkey"))
				.addOptional(new ResourceLocation("autumnity:cooked_turkey"));
	}

	@SuppressWarnings("unchecked")
	private void registerForgeTags() {
		tag(CommonTags.Items.BERRIES).add(Items.SWEET_BERRIES, Items.GLOW_BERRIES);
		tag(CommonTags.Items.BREAD).addTag(CommonTags.Items.BREAD_WHEAT);
		tag(CommonTags.Items.BREAD_WHEAT).add(Items.BREAD);

		tag(CommonTags.Items.COOKED_BACON).add(ModItems.COOKED_BACON.get());

		tag(CommonTags.Items.COOKED_BEEF).add(Items.COOKED_BEEF, ModItems.BEEF_PATTY.get());
		tag(CommonTags.Items.COOKED_CHICKEN).add(Items.COOKED_CHICKEN, ModItems.COOKED_CHICKEN_CUTS.get());
		tag(CommonTags.Items.COOKED_PORK).add(Items.COOKED_PORKCHOP, ModItems.COOKED_BACON.get());
		tag(CommonTags.Items.COOKED_MUTTON).add(Items.COOKED_MUTTON, ModItems.COOKED_MUTTON_CHOPS.get());
		tag(CommonTags.Items.COOKED_EGGS).add(ModItems.FRIED_EGG.get());

		tag(CommonTags.Items.COOKED_FISHES).addTags(CommonTags.Items.COOKED_FISHES_COD, CommonTags.Items.COOKED_FISHES_SALMON);
		tag(CommonTags.Items.COOKED_FISHES_COD).add(Items.COOKED_COD, ModItems.COOKED_COD_SLICE.get());
		tag(CommonTags.Items.COOKED_FISHES_SALMON).add(Items.COOKED_SALMON, ModItems.COOKED_SALMON_SLICE.get());

		tag(CommonTags.Items.CROPS).addTags(CommonTags.Items.CROPS_CABBAGE, CommonTags.Items.CROPS_ONION, CommonTags.Items.CROPS_RICE, CommonTags.Items.CROPS_TOMATO);
		tag(CommonTags.Items.CROPS_CABBAGE).add(ModItems.CABBAGE.get(), ModItems.CABBAGE_LEAF.get());
		tag(CommonTags.Items.CROPS_ONION).add(ModItems.ONION.get());
		tag(CommonTags.Items.CROPS_RICE).add(ModItems.RICE.get());
		tag(CommonTags.Items.CROPS_TOMATO).add(ModItems.TOMATO.get());

		tag(CommonTags.Items.DOUGH).add(ModItems.WHEAT_DOUGH.get());
		tag(CommonTags.Items.DOUGH_WHEAT).add(ModItems.WHEAT_DOUGH.get());

		tag(CommonTags.Items.EGGS).add(Items.EGG);

		tag(CommonTags.Items.GRAIN).addTags(CommonTags.Items.GRAIN_WHEAT, CommonTags.Items.GRAIN_RICE);
		tag(CommonTags.Items.GRAIN_WHEAT).add(Items.WHEAT);
		tag(CommonTags.Items.GRAIN_RICE).add(ModItems.RICE.get());

		tag(CommonTags.Items.MILK).addTags(CommonTags.Items.MILK_BUCKET, CommonTags.Items.MILK_BOTTLE);
		tag(CommonTags.Items.MILK_BUCKET).add(Items.MILK_BUCKET);
		tag(CommonTags.Items.MILK_BOTTLE).add(ModItems.MILK_BOTTLE.get());

		tag(CommonTags.Items.PASTA).addTags(CommonTags.Items.PASTA_RAW_PASTA);
		tag(CommonTags.Items.PASTA_RAW_PASTA).add(ModItems.RAW_PASTA.get());

		tag(CommonTags.Items.RAW_MEAT).add(Items.RABBIT).addTags(
				CommonTags.Items.RAW_BEEF,
				CommonTags.Items.RAW_CHICKEN,
				CommonTags.Items.RAW_PORK,
				CommonTags.Items.RAW_MUTTON
		);

		tag(CommonTags.Items.RAW_BACON).add(ModItems.BACON.get());
		tag(CommonTags.Items.RAW_BEEF).add(Items.BEEF, ModItems.MINCED_BEEF.get());
		tag(CommonTags.Items.RAW_CHICKEN).add(Items.CHICKEN, ModItems.CHICKEN_CUTS.get());
		tag(CommonTags.Items.RAW_PORK).add(Items.PORKCHOP, ModItems.BACON.get());
		tag(CommonTags.Items.RAW_MUTTON).add(Items.MUTTON, ModItems.MUTTON_CHOPS.get());

		tag(CommonTags.Items.RAW_FISHES).addTags(CommonTags.Items.RAW_FISHES_COD, CommonTags.Items.RAW_FISHES_SALMON, CommonTags.Items.RAW_FISHES_TROPICAL);
		tag(CommonTags.Items.RAW_FISHES_COD).add(Items.COD, ModItems.COD_SLICE.get());
		tag(CommonTags.Items.RAW_FISHES_SALMON).add(Items.SALMON, ModItems.SALMON_SLICE.get());
		tag(CommonTags.Items.RAW_FISHES_TROPICAL).add(Items.TROPICAL_FISH);

		tag(CommonTags.Items.SALAD_INGREDIENTS).addTags(CommonTags.Items.SALAD_INGREDIENTS_CABBAGE);
		tag(CommonTags.Items.SALAD_INGREDIENTS_CABBAGE).add(ModItems.CABBAGE.get(), ModItems.CABBAGE_LEAF.get());

		tag(CommonTags.Items.SEEDS).addTags(CommonTags.Items.SEEDS_CABBAGE, CommonTags.Items.SEEDS_RICE, CommonTags.Items.SEEDS_TOMATO);
		tag(CommonTags.Items.SEEDS_CABBAGE).add(ModItems.CABBAGE_SEEDS.get());
		tag(CommonTags.Items.SEEDS_RICE).add(ModItems.RICE.get());
		tag(CommonTags.Items.SEEDS_TOMATO).add(ModItems.TOMATO_SEEDS.get());

		tag(CommonTags.Items.STORAGE_BLOCKS_CARROT).add(ModItems.CARROT_CRATE.get());
		tag(CommonTags.Items.STORAGE_BLOCKS_POTATO).add(ModItems.POTATO_CRATE.get());
		tag(CommonTags.Items.STORAGE_BLOCKS_BEETROOT).add(ModItems.BEETROOT_CRATE.get());
		tag(CommonTags.Items.STORAGE_BLOCKS_CABBAGE).add(ModItems.CABBAGE_CRATE.get());
		tag(CommonTags.Items.STORAGE_BLOCKS_TOMATO).add(ModItems.TOMATO_CRATE.get());
		tag(CommonTags.Items.STORAGE_BLOCKS_ONION).add(ModItems.ONION_CRATE.get());
		tag(CommonTags.Items.STORAGE_BLOCKS_RICE).add(ModItems.RICE_BAG.get());
		tag(CommonTags.Items.STORAGE_BLOCKS_RICE_PANICLE).add(ModItems.RICE_BALE.get());
		tag(CommonTags.Items.STORAGE_BLOCKS_STRAW).add(ModItems.STRAW_BALE.get());

		tag(CommonTags.Items.VEGETABLES).addTags(CommonTags.Items.VEGETABLES_BEETROOT, CommonTags.Items.VEGETABLES_CARROT, CommonTags.Items.VEGETABLES_ONION, CommonTags.Items.VEGETABLES_POTATO, CommonTags.Items.VEGETABLES_TOMATO);
		tag(CommonTags.Items.VEGETABLES_BEETROOT).add(Items.BEETROOT);
		tag(CommonTags.Items.VEGETABLES_CARROT).add(Items.CARROT);
		tag(CommonTags.Items.VEGETABLES_ONION).add(ModItems.ONION.get());
		tag(CommonTags.Items.VEGETABLES_POTATO).add(Items.POTATO);
		tag(CommonTags.Items.VEGETABLES_TOMATO).add(ModItems.TOMATO.get());

		tag(CommonTags.Items.TOOLS).addTags(CommonTags.Items.TOOLS_AXES, CommonTags.Items.TOOLS_KNIVES, CommonTags.Items.TOOLS_PICKAXES, CommonTags.Items.TOOLS_SHOVELS);
		tag(CommonTags.Items.TOOLS_AXES).add(Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.DIAMOND_AXE, Items.GOLDEN_AXE, Items.NETHERITE_AXE);
		tag(CommonTags.Items.TOOLS_KNIVES).add(ModItems.FLINT_KNIFE.get(), ModItems.IRON_KNIFE.get(), ModItems.DIAMOND_KNIFE.get(), ModItems.GOLDEN_KNIFE.get(), ModItems.NETHERITE_KNIFE.get());
		tag(CommonTags.Items.TOOLS_PICKAXES).add(Items.WOODEN_PICKAXE, Items.STONE_PICKAXE, Items.IRON_PICKAXE, Items.DIAMOND_PICKAXE, Items.GOLDEN_PICKAXE, Items.NETHERITE_PICKAXE);
		tag(CommonTags.Items.TOOLS_SHOVELS).add(Items.WOODEN_SHOVEL, Items.STONE_SHOVEL, Items.IRON_SHOVEL, Items.DIAMOND_SHOVEL, Items.GOLDEN_SHOVEL, Items.NETHERITE_SHOVEL);

		tag(CommonTags.Items.BUCKETS_WATER).add(Items.WATER_BUCKET);
	}

	public void registerCompatibilityTags() {
		tag(CompatibilityTags.CREATE_UPRIGHT_ON_BELT)
				.addTag(ModTags.Items.MEALS)
				.addTag(ModTags.Items.DRINKS)
				.addTag(ModTags.Items.FEASTS)
				.add(ModItems.TOMATO_SAUCE.get())
				.add(ModItems.DOG_FOOD.get())
				.add(ModItems.FRUIT_SALAD.get())
				.add(ModItems.NETHER_SALAD.get())
				.add(ModItems.PIE_CRUST.get())
				.add(ModItems.APPLE_PIE.get())
				.add(ModItems.SWEET_BERRY_CHEESECAKE.get())
				.add(ModItems.CHOCOLATE_PIE.get());

		tag(CompatibilityTags.CREATE_CA_PLANT_FOODS)
				.add(ModItems.PUMPKIN_SLICE.get())
				.add(ModItems.ROTTEN_TOMATO.get())
				.add(ModItems.RICE_PANICLE.get());
		tag(CompatibilityTags.CREATE_CA_PLANTS)
				.add(ModItems.SANDY_SHRUB.get())
				.add(ModItems.BROWN_MUSHROOM_COLONY.get())
				.add(ModItems.RED_MUSHROOM_COLONY.get());

		tag(CompatibilityTags.ORIGINS_MEAT)
				.add(ModItems.FRIED_EGG.get())
				.add(ModItems.COD_SLICE.get())
				.add(ModItems.COOKED_COD_SLICE.get())
				.add(ModItems.SALMON_SLICE.get())
				.add(ModItems.COOKED_SALMON_SLICE.get())
				.add(ModItems.BACON_AND_EGGS.get());

		tag(CompatibilityTags.SERENE_SEASONS_AUTUMN_CROPS)
				.add(ModItems.CABBAGE_SEEDS.get())
				.add(ModItems.ONION.get())
				.add(ModItems.RICE.get());
		tag(CompatibilityTags.SERENE_SEASONS_SPRING_CROPS)
				.add(ModItems.ONION.get());
		tag(CompatibilityTags.SERENE_SEASONS_SUMMER_CROPS)
				.add(ModItems.TOMATO_SEEDS.get())
				.add(ModItems.RICE.get());
		tag(CompatibilityTags.SERENE_SEASONS_WINTER_CROPS)
				.add(ModItems.CABBAGE_SEEDS.get());

		tag(CompatibilityTags.TINKERS_CONSTRUCT_SEEDS).add(ModItems.ONION.get());
	}
}
