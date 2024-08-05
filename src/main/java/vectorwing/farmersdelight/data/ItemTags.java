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
import vectorwing.farmersdelight.common.tag.ForgeTags;
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
		copy(ModTags.WILD_CROPS, ModTags.WILD_CROPS_ITEM);
		copy(BlockTags.SMALL_FLOWERS, net.minecraft.tags.ItemTags.SMALL_FLOWERS);

		this.registerMinecraftTags();
		this.registerModTags();
		this.registerForgeTags();
		this.registerCompatibilityTags();
	}
	private void registerMinecraftTags() {
		tag(net.minecraft.tags.ItemTags.TALL_FLOWERS).add(ModItems.WILD_RICE.get());
		tag(net.minecraft.tags.ItemTags.PIGLIN_LOVED).add(ModItems.GOLDEN_KNIFE.get());
		tag(net.minecraft.tags.ItemTags.SIGNS).addTag(ModTags.CANVAS_SIGNS);
		tag(net.minecraft.tags.ItemTags.HANGING_SIGNS).addTag(ModTags.HANGING_CANVAS_SIGNS);
		tag(net.minecraft.tags.ItemTags.VILLAGER_PLANTABLE_SEEDS)
				.add(ModItems.CABBAGE_SEEDS.get())
				.add(ModItems.TOMATO_SEEDS.get())
				.add(ModItems.ONION.get());
	}

	private void registerModTags() {
		tag(ModTags.KNIVES).add(ModItems.FLINT_KNIFE.get(), ModItems.IRON_KNIFE.get(), ModItems.DIAMOND_KNIFE.get(), ModItems.GOLDEN_KNIFE.get(), ModItems.NETHERITE_KNIFE.get());
		tag(ModTags.STRAW_HARVESTERS).addTag(ModTags.KNIVES);
		tag(ModTags.WOLF_PREY).addTag(ForgeTags.RAW_CHICKEN).addTag(ForgeTags.RAW_MUTTON).add(Items.RABBIT);
		tag(ModTags.CABBAGE_ROLL_INGREDIENTS).addTag(ForgeTags.RAW_PORK).addTag(ForgeTags.RAW_FISHES).addTag(ForgeTags.RAW_CHICKEN).addTag(ForgeTags.RAW_BEEF).addTag(ForgeTags.RAW_MUTTON).addTag(ForgeTags.EGGS).addTag(Tags.Items.MUSHROOMS).add(Items.CARROT, Items.POTATO, Items.BEETROOT);
		tag(ModTags.CANVAS_SIGNS)
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
		tag(ModTags.HANGING_CANVAS_SIGNS)
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
		tag(ModTags.WOODEN_CABINETS)
				.add(ModItems.OAK_CABINET.get())
				.add(ModItems.SPRUCE_CABINET.get())
				.add(ModItems.BIRCH_CABINET.get())
				.add(ModItems.JUNGLE_CABINET.get())
				.add(ModItems.ACACIA_CABINET.get())
				.add(ModItems.DARK_OAK_CABINET.get())
				.add(ModItems.MANGROVE_CABINET.get())
				.add(ModItems.CHERRY_CABINET.get())
				.add(ModItems.BAMBOO_CABINET.get())
				.add(ModItems.CRIMSON_CABINET.get())
				.add(ModItems.WARPED_CABINET.get());
		tag(ModTags.CABINETS).addTag(ModTags.WOODEN_CABINETS);
		tag(ModTags.OFFHAND_EQUIPMENT).add(Items.SHIELD)
				.addOptional(new ResourceLocation("create:extendo_grip"));
		tag(ModTags.SERVING_CONTAINERS).add(Items.BOWL, Items.GLASS_BOTTLE, Items.BUCKET);
		tag(ModTags.FLAT_ON_CUTTING_BOARD).add(Items.TRIDENT, Items.SPYGLASS)
				.addOptional(new ResourceLocation("supplementaries:quiver"))
				.addOptional(new ResourceLocation("autumnity:turkey"))
				.addOptional(new ResourceLocation("autumnity:cooked_turkey"));
	}

	@SuppressWarnings("unchecked")
	private void registerForgeTags() {
		tag(ForgeTags.BERRIES).add(Items.SWEET_BERRIES, Items.GLOW_BERRIES);
		tag(ForgeTags.BREAD).addTag(ForgeTags.BREAD_WHEAT);
		tag(ForgeTags.BREAD_WHEAT).add(Items.BREAD);

		tag(ForgeTags.COOKED_BACON).add(ModItems.COOKED_BACON.get());

		tag(ForgeTags.COOKED_BEEF).add(Items.COOKED_BEEF, ModItems.BEEF_PATTY.get());
		tag(ForgeTags.COOKED_CHICKEN).add(Items.COOKED_CHICKEN, ModItems.COOKED_CHICKEN_CUTS.get());
		tag(ForgeTags.COOKED_PORK).add(Items.COOKED_PORKCHOP, ModItems.COOKED_BACON.get());
		tag(ForgeTags.COOKED_MUTTON).add(Items.COOKED_MUTTON, ModItems.COOKED_MUTTON_CHOPS.get());
		tag(ForgeTags.COOKED_EGGS).add(ModItems.FRIED_EGG.get());

		tag(ForgeTags.COOKED_FISHES).addTags(ForgeTags.COOKED_FISHES_COD, ForgeTags.COOKED_FISHES_SALMON);
		tag(ForgeTags.COOKED_FISHES_COD).add(Items.COOKED_COD, ModItems.COOKED_COD_SLICE.get());
		tag(ForgeTags.COOKED_FISHES_SALMON).add(Items.COOKED_SALMON, ModItems.COOKED_SALMON_SLICE.get());

		tag(ForgeTags.CROPS).addTags(ForgeTags.CROPS_CABBAGE, ForgeTags.CROPS_ONION, ForgeTags.CROPS_RICE, ForgeTags.CROPS_TOMATO);
		tag(ForgeTags.CROPS_CABBAGE).add(ModItems.CABBAGE.get(), ModItems.CABBAGE_LEAF.get());
		tag(ForgeTags.CROPS_ONION).add(ModItems.ONION.get());
		tag(ForgeTags.CROPS_RICE).add(ModItems.RICE.get());
		tag(ForgeTags.CROPS_TOMATO).add(ModItems.TOMATO.get());

		tag(ForgeTags.DOUGH).add(ModItems.WHEAT_DOUGH.get());
		tag(ForgeTags.DOUGH_WHEAT).add(ModItems.WHEAT_DOUGH.get());

		tag(ForgeTags.EGGS).add(Items.EGG);

		tag(ForgeTags.GRAIN).addTags(ForgeTags.GRAIN_WHEAT, ForgeTags.GRAIN_RICE);
		tag(ForgeTags.GRAIN_WHEAT).add(Items.WHEAT);
		tag(ForgeTags.GRAIN_RICE).add(ModItems.RICE.get());

		tag(ForgeTags.MILK).addTags(ForgeTags.MILK_BUCKET, ForgeTags.MILK_BOTTLE);
		tag(ForgeTags.MILK_BUCKET).add(Items.MILK_BUCKET);
		tag(ForgeTags.MILK_BOTTLE).add(ModItems.MILK_BOTTLE.get());

		tag(ForgeTags.PASTA).addTags(ForgeTags.PASTA_RAW_PASTA);
		tag(ForgeTags.PASTA_RAW_PASTA).add(ModItems.RAW_PASTA.get());

		tag(ForgeTags.RAW_BACON).add(ModItems.BACON.get());
		tag(ForgeTags.RAW_BEEF).add(Items.BEEF, ModItems.MINCED_BEEF.get());
		tag(ForgeTags.RAW_CHICKEN).add(Items.CHICKEN, ModItems.CHICKEN_CUTS.get());
		tag(ForgeTags.RAW_PORK).add(Items.PORKCHOP, ModItems.BACON.get());
		tag(ForgeTags.RAW_MUTTON).add(Items.MUTTON, ModItems.MUTTON_CHOPS.get());

		tag(ForgeTags.RAW_FISHES).addTags(ForgeTags.RAW_FISHES_COD, ForgeTags.RAW_FISHES_SALMON, ForgeTags.RAW_FISHES_TROPICAL);
		tag(ForgeTags.RAW_FISHES_COD).add(Items.COD, ModItems.COD_SLICE.get());
		tag(ForgeTags.RAW_FISHES_SALMON).add(Items.SALMON, ModItems.SALMON_SLICE.get());
		tag(ForgeTags.RAW_FISHES_TROPICAL).add(Items.TROPICAL_FISH);

		tag(ForgeTags.SALAD_INGREDIENTS).addTags(ForgeTags.SALAD_INGREDIENTS_CABBAGE);
		tag(ForgeTags.SALAD_INGREDIENTS_CABBAGE).add(ModItems.CABBAGE.get(), ModItems.CABBAGE_LEAF.get());

		tag(ForgeTags.SEEDS).addTags(ForgeTags.SEEDS_CABBAGE, ForgeTags.SEEDS_RICE, ForgeTags.SEEDS_TOMATO);
		tag(ForgeTags.SEEDS_CABBAGE).add(ModItems.CABBAGE_SEEDS.get());
		tag(ForgeTags.SEEDS_RICE).add(ModItems.RICE.get());
		tag(ForgeTags.SEEDS_TOMATO).add(ModItems.TOMATO_SEEDS.get());

		tag(ForgeTags.VEGETABLES).addTags(ForgeTags.VEGETABLES_BEETROOT, ForgeTags.VEGETABLES_CARROT, ForgeTags.VEGETABLES_ONION, ForgeTags.VEGETABLES_POTATO, ForgeTags.VEGETABLES_TOMATO);
		tag(ForgeTags.VEGETABLES_BEETROOT).add(Items.BEETROOT);
		tag(ForgeTags.VEGETABLES_CARROT).add(Items.CARROT);
		tag(ForgeTags.VEGETABLES_ONION).add(ModItems.ONION.get());
		tag(ForgeTags.VEGETABLES_POTATO).add(Items.POTATO);
		tag(ForgeTags.VEGETABLES_TOMATO).add(ModItems.TOMATO.get());

		tag(ForgeTags.TOOLS).addTags(ForgeTags.TOOLS_AXES, ForgeTags.TOOLS_KNIVES, ForgeTags.TOOLS_PICKAXES, ForgeTags.TOOLS_SHOVELS);
		tag(ForgeTags.TOOLS_AXES).add(Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.DIAMOND_AXE, Items.GOLDEN_AXE, Items.NETHERITE_AXE);
		tag(ForgeTags.TOOLS_KNIVES).add(ModItems.FLINT_KNIFE.get(), ModItems.IRON_KNIFE.get(), ModItems.DIAMOND_KNIFE.get(), ModItems.GOLDEN_KNIFE.get(), ModItems.NETHERITE_KNIFE.get());
		tag(ForgeTags.TOOLS_PICKAXES).add(Items.WOODEN_PICKAXE, Items.STONE_PICKAXE, Items.IRON_PICKAXE, Items.DIAMOND_PICKAXE, Items.GOLDEN_PICKAXE, Items.NETHERITE_PICKAXE);
		tag(ForgeTags.TOOLS_SHOVELS).add(Items.WOODEN_SHOVEL, Items.STONE_SHOVEL, Items.IRON_SHOVEL, Items.DIAMOND_SHOVEL, Items.GOLDEN_SHOVEL, Items.NETHERITE_SHOVEL);
	}

	public void registerCompatibilityTags() {
		tag(CompatibilityTags.CREATE_UPRIGHT_ON_BELT)
				.add(ModItems.MILK_BOTTLE.get())
				.add(ModItems.HOT_COCOA.get())
				.add(ModItems.APPLE_CIDER.get())
				.add(ModItems.MELON_JUICE.get())
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
