package vectorwing.farmersdelight.data;

import net.minecraft.block.Blocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.utils.tags.ModTags;

import javax.annotation.Nullable;

public class BlockTags extends BlockTagsProvider
{
	public BlockTags(DataGenerator generatorIn, String modId, @Nullable ExistingFileHelper existingFileHelper) {
		super(generatorIn, modId, existingFileHelper);
	}

	@Override
	protected void registerTags() {
		this.registerModTags();
		this.registerMinecraftTags();
		this.registerForgeTags();
	}

	protected void registerMinecraftTags() {
		getOrCreateBuilder(net.minecraft.tags.BlockTags.BAMBOO_PLANTABLE_ON).add(
				ModBlocks.RICH_SOIL.get());
		getOrCreateBuilder(net.minecraft.tags.BlockTags.MUSHROOM_GROW_BLOCK).add(
				ModBlocks.ORGANIC_COMPOST.get(),
				ModBlocks.RICH_SOIL.get());
		getOrCreateBuilder(net.minecraft.tags.BlockTags.CARPETS).add(
				ModBlocks.FULL_TATAMI_MAT.get(),
				ModBlocks.HALF_TATAMI_MAT.get());
		getOrCreateBuilder(net.minecraft.tags.BlockTags.CROPS).add(
				ModBlocks.CABBAGE_CROP.get(),
				ModBlocks.ONION_CROP.get(),
				ModBlocks.RICE_UPPER_CROP.get(),
				ModBlocks.TOMATO_CROP.get());
		getOrCreateBuilder(net.minecraft.tags.BlockTags.STANDING_SIGNS).add(
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
		getOrCreateBuilder(net.minecraft.tags.BlockTags.WALL_SIGNS).add(
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
		getOrCreateBuilder(net.minecraft.tags.BlockTags.SMALL_FLOWERS).addTag(ModTags.WILD_CROPS);
	}

	protected void registerForgeTags() {
		getOrCreateBuilder(Tags.Blocks.DIRT).add(
				ModBlocks.RICH_SOIL.get());
	}

	protected void registerModTags() {
		getOrCreateBuilder(ModTags.WILD_CROPS).add(
				ModBlocks.WILD_CARROTS.get(),
				ModBlocks.WILD_POTATOES.get(),
				ModBlocks.WILD_BEETROOTS.get(),
				ModBlocks.WILD_CABBAGES.get(),
				ModBlocks.WILD_TOMATOES.get(),
				ModBlocks.WILD_ONIONS.get(),
				ModBlocks.WILD_RICE.get());
		getOrCreateBuilder(ModTags.TRAY_HEAT_SOURCES).add(
				Blocks.LAVA)
				.addTag(net.minecraft.tags.BlockTags.CAMPFIRES)
				.addTag(net.minecraft.tags.BlockTags.FIRE);
		getOrCreateBuilder(ModTags.HEAT_SOURCES).add(
				Blocks.MAGMA_BLOCK,
				ModBlocks.STOVE.get())
				.addTag(ModTags.TRAY_HEAT_SOURCES);
		getOrCreateBuilder(ModTags.HEAT_CONDUCTORS).add(
				Blocks.HOPPER)
				.addOptional(new ResourceLocation("create:chute"));
		getOrCreateBuilder(ModTags.COMPOST_ACTIVATORS).add(
				Blocks.BROWN_MUSHROOM,
				Blocks.RED_MUSHROOM,
				Blocks.PODZOL,
				Blocks.MYCELIUM,
				ModBlocks.ORGANIC_COMPOST.get(),
				ModBlocks.RICH_SOIL.get(),
				ModBlocks.RICH_SOIL_FARMLAND.get(),
				ModBlocks.BROWN_MUSHROOM_COLONY.get(),
				ModBlocks.RED_MUSHROOM_COLONY.get());
		getOrCreateBuilder(ModTags.UNAFFECTED_BY_RICH_SOIL).add(
				Blocks.GRASS,
				Blocks.TALL_GRASS,
				Blocks.FERN,
				Blocks.LARGE_FERN,
				Blocks.TWISTING_VINES,
				Blocks.TWISTING_VINES_PLANT,
				ModBlocks.BROWN_MUSHROOM_COLONY.get(),
				ModBlocks.RED_MUSHROOM_COLONY.get())
				.addTag(ModTags.WILD_CROPS);
		getOrCreateBuilder(ModTags.MUSHROOM_COLONY_GROWABLE_ON).add(ModBlocks.RICH_SOIL.get());
	}
}
