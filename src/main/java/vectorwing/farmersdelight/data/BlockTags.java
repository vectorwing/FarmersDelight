package vectorwing.farmersdelight.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.utils.tags.ModTags;

import javax.annotation.Nullable;

public class BlockTags extends BlockTagsProvider
{
	public BlockTags(DataGenerator generatorIn, String modId, @Nullable ExistingFileHelper existingFileHelper) {
		super(generatorIn, modId, existingFileHelper);
	}

	@Override
	protected void addTags() {
		this.registerModTags();
		this.registerMinecraftTags();
		this.registerForgeTags();

		this.registerBlockMineables();
	}

	protected void registerBlockMineables() {
		tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_AXE).add(
				ModBlocks.BASKET.get(),
				ModBlocks.CUTTING_BOARD.get(),
				ModBlocks.CARROT_CRATE.get(),
				ModBlocks.POTATO_CRATE.get(),
				ModBlocks.BEETROOT_CRATE.get(),
				ModBlocks.CABBAGE_CRATE.get(),
				ModBlocks.TOMATO_CRATE.get(),
				ModBlocks.ONION_CRATE.get(),
				ModBlocks.OAK_PANTRY.get(),
				ModBlocks.BIRCH_PANTRY.get(),
				ModBlocks.SPRUCE_PANTRY.get(),
				ModBlocks.JUNGLE_PANTRY.get(),
				ModBlocks.ACACIA_PANTRY.get(),
				ModBlocks.DARK_OAK_PANTRY.get(),
				ModBlocks.CRIMSON_PANTRY.get(),
				ModBlocks.WARPED_PANTRY.get(),
				ModBlocks.STUFFED_PUMPKIN_BLOCK.get()
		);
		tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_HOE).add(
				ModBlocks.RICE_BALE.get(),
				ModBlocks.STRAW_BALE.get()
		);
		tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE).add(
				ModBlocks.STOVE.get(),
				ModBlocks.COOKING_POT.get(),
				ModBlocks.SKILLET.get()
		);
		tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_SHOVEL).add(
				ModBlocks.ORGANIC_COMPOST.get(),
				ModBlocks.RICH_SOIL.get(),
				ModBlocks.RICH_SOIL_FARMLAND.get()
		);
		tag(ModTags.MINEABLE_WITH_KNIFE).add(
				ModBlocks.RICE_BAG.get(),
				ModBlocks.ROPE.get(),
				ModBlocks.SAFETY_NET.get(),
				ModBlocks.CANVAS_RUG.get(),
				ModBlocks.TATAMI.get(),
				ModBlocks.FULL_TATAMI_MAT.get(),
				ModBlocks.HALF_TATAMI_MAT.get(),
				ModBlocks.APPLE_PIE.get(),
				ModBlocks.SWEET_BERRY_CHEESECAKE.get(),
				ModBlocks.CHOCOLATE_PIE.get(),
				ModBlocks.ROAST_CHICKEN_BLOCK.get(),
				ModBlocks.HONEY_GLAZED_HAM_BLOCK.get(),
				ModBlocks.SHEPHERDS_PIE_BLOCK.get()
		);
	}

	protected void registerMinecraftTags() {
		tag(net.minecraft.tags.BlockTags.BAMBOO_PLANTABLE_ON).add(
				ModBlocks.RICH_SOIL.get());
		tag(net.minecraft.tags.BlockTags.MUSHROOM_GROW_BLOCK).add(
				ModBlocks.ORGANIC_COMPOST.get(),
				ModBlocks.RICH_SOIL.get());
		tag(net.minecraft.tags.BlockTags.CARPETS).add(
				ModBlocks.FULL_TATAMI_MAT.get(),
				ModBlocks.HALF_TATAMI_MAT.get());
		tag(net.minecraft.tags.BlockTags.CROPS).add(
				ModBlocks.CABBAGE_CROP.get(),
				ModBlocks.ONION_CROP.get(),
				ModBlocks.RICE_UPPER_CROP.get(),
				ModBlocks.TOMATO_CROP.get());
		tag(net.minecraft.tags.BlockTags.STANDING_SIGNS).add(
				ModBlocks.CANVAS_SIGN.get(),
				ModBlocks.WHITE_CANVAS_SIGN.get(),
				ModBlocks.ORANGE_CANVAS_SIGN.get(),
				ModBlocks.MAGENTA_CANVAS_SIGN.get(),
				ModBlocks.LIGHT_BLUE_CANVAS_SIGN.get(),
				ModBlocks.YELLOW_CANVAS_SIGN.get(),
				ModBlocks.LIME_CANVAS_SIGN.get(),
				ModBlocks.PINK_CANVAS_SIGN.get(),
				ModBlocks.GRAY_CANVAS_SIGN.get(),
				ModBlocks.LIGHT_GRAY_CANVAS_SIGN.get(),
				ModBlocks.CYAN_CANVAS_SIGN.get(),
				ModBlocks.PURPLE_CANVAS_SIGN.get(),
				ModBlocks.BLUE_CANVAS_SIGN.get(),
				ModBlocks.BROWN_CANVAS_SIGN.get(),
				ModBlocks.GREEN_CANVAS_SIGN.get(),
				ModBlocks.RED_CANVAS_SIGN.get(),
				ModBlocks.BLACK_CANVAS_SIGN.get());
		tag(net.minecraft.tags.BlockTags.WALL_SIGNS).add(
				ModBlocks.CANVAS_WALL_SIGN.get(),
				ModBlocks.WHITE_CANVAS_WALL_SIGN.get(),
				ModBlocks.ORANGE_CANVAS_WALL_SIGN.get(),
				ModBlocks.MAGENTA_CANVAS_WALL_SIGN.get(),
				ModBlocks.LIGHT_BLUE_CANVAS_WALL_SIGN.get(),
				ModBlocks.YELLOW_CANVAS_WALL_SIGN.get(),
				ModBlocks.LIME_CANVAS_WALL_SIGN.get(),
				ModBlocks.PINK_CANVAS_WALL_SIGN.get(),
				ModBlocks.GRAY_CANVAS_WALL_SIGN.get(),
				ModBlocks.LIGHT_GRAY_CANVAS_WALL_SIGN.get(),
				ModBlocks.CYAN_CANVAS_WALL_SIGN.get(),
				ModBlocks.PURPLE_CANVAS_WALL_SIGN.get(),
				ModBlocks.BLUE_CANVAS_WALL_SIGN.get(),
				ModBlocks.BROWN_CANVAS_WALL_SIGN.get(),
				ModBlocks.GREEN_CANVAS_WALL_SIGN.get(),
				ModBlocks.RED_CANVAS_WALL_SIGN.get(),
				ModBlocks.BLACK_CANVAS_WALL_SIGN.get());
		tag(net.minecraft.tags.BlockTags.SMALL_FLOWERS).addTag(ModTags.WILD_CROPS);
	}

	protected void registerForgeTags() {
		tag(Tags.Blocks.DIRT).add(
				ModBlocks.RICH_SOIL.get());
	}

	protected void registerModTags() {
		tag(ModTags.WILD_CROPS).add(
				ModBlocks.WILD_CARROTS.get(),
				ModBlocks.WILD_POTATOES.get(),
				ModBlocks.WILD_BEETROOTS.get(),
				ModBlocks.WILD_CABBAGES.get(),
				ModBlocks.WILD_TOMATOES.get(),
				ModBlocks.WILD_ONIONS.get(),
				ModBlocks.WILD_RICE.get());
		tag(ModTags.TRAY_HEAT_SOURCES).add(
						Blocks.LAVA)
				.addTag(net.minecraft.tags.BlockTags.CAMPFIRES)
				.addTag(net.minecraft.tags.BlockTags.FIRE);
		tag(ModTags.HEAT_SOURCES).add(
						Blocks.MAGMA_BLOCK,
						ModBlocks.STOVE.get())
				.addTag(ModTags.TRAY_HEAT_SOURCES);
		tag(ModTags.HEAT_CONDUCTORS).add(
						Blocks.HOPPER)
				.addOptional(new ResourceLocation("create:chute"));
		tag(ModTags.COMPOST_ACTIVATORS).add(
				Blocks.BROWN_MUSHROOM,
				Blocks.RED_MUSHROOM,
				Blocks.PODZOL,
				Blocks.MYCELIUM,
				ModBlocks.ORGANIC_COMPOST.get(),
				ModBlocks.RICH_SOIL.get(),
				ModBlocks.RICH_SOIL_FARMLAND.get(),
				ModBlocks.BROWN_MUSHROOM_COLONY.get(),
				ModBlocks.RED_MUSHROOM_COLONY.get());
		tag(ModTags.UNAFFECTED_BY_RICH_SOIL).add(
						Blocks.GRASS,
						Blocks.TALL_GRASS,
						Blocks.FERN,
						Blocks.LARGE_FERN,
						Blocks.TWISTING_VINES,
						Blocks.TWISTING_VINES_PLANT,
						ModBlocks.BROWN_MUSHROOM_COLONY.get(),
						ModBlocks.RED_MUSHROOM_COLONY.get())
				.addTag(ModTags.WILD_CROPS);
		tag(ModTags.MUSHROOM_COLONY_GROWABLE_ON).add(ModBlocks.RICH_SOIL.get());
	}
}
