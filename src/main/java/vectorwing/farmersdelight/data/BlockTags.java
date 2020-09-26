package vectorwing.farmersdelight.data;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Items;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.registry.ModItems;
import vectorwing.farmersdelight.utils.tags.ForgeTags;
import vectorwing.farmersdelight.utils.tags.ModTags;

public class BlockTags extends BlockTagsProvider {
	public BlockTags(DataGenerator generatorIn) {
		super(generatorIn);
	}

	@Override
	protected void registerTags() {
		getBuilder(net.minecraft.tags.BlockTags.CARPETS).add(
				ModBlocks.FULL_TATAMI_MAT.get(),
				ModBlocks.HALF_TATAMI_MAT.get()
		);
		getBuilder(net.minecraft.tags.BlockTags.CROPS).add(
				ModBlocks.CABBAGE_CROP.get(),
				ModBlocks.ONION_CROP.get(),
				ModBlocks.RICE_CROP.get(),
				ModBlocks.TALL_RICE_CROP.get(),
				ModBlocks.TOMATO_CROP.get());
		getBuilder(net.minecraft.tags.BlockTags.SMALL_FLOWERS).add(
				ModBlocks.WILD_BEETROOTS.get(),
				ModBlocks.WILD_CABBAGES.get(),
				ModBlocks.WILD_CARROTS.get(),
				ModBlocks.WILD_ONIONS.get(),
				ModBlocks.WILD_POTATOES.get(),
				ModBlocks.WILD_TOMATOES.get());

		getBuilder(ModTags.TRAY_HEAT_SOURCES).add(Blocks.CAMPFIRE, Blocks.FIRE, Blocks.LAVA);
		getBuilder(ModTags.HEAT_SOURCES).add(Blocks.MAGMA_BLOCK, ModBlocks.STOVE.get()).add(ModTags.TRAY_HEAT_SOURCES);
		getBuilder(ModTags.COMPOST_ACTIVATORS).add(
				Blocks.BROWN_MUSHROOM,
				Blocks.RED_MUSHROOM,
				Blocks.PODZOL,
				Blocks.MYCELIUM,
				ModBlocks.ORGANIC_COMPOST.get(),
				ModBlocks.RICH_SOIL.get(),
				ModBlocks.RICH_SOIL_FARMLAND.get(),
				ModBlocks.BROWN_MUSHROOM_COLONY.get(),
				ModBlocks.RED_MUSHROOM_COLONY.get());
	}
}
