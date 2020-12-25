package vectorwing.farmersdelight.data;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.blocks.OrganicCompostBlock;
import vectorwing.farmersdelight.blocks.PantryBlock;
import vectorwing.farmersdelight.registry.ModBlocks;

import java.util.function.Function;

public class BlockStates extends BlockStateProvider
{
	public BlockStates(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
		super(gen, modid, exFileHelper);
	}

	private String blockName(Block block) {
		return block.getRegistryName().getPath();
	}

	private String itemName(Item item) {
		return item.getRegistryName().getPath();
	}

	public ResourceLocation rlBlockModel(String path) {
		return new ResourceLocation(FarmersDelight.MODID, "block/" + path);
	}

	@Override
	protected void registerStatesAndModels() {
		this.simpleBlock(ModBlocks.RICH_SOIL.get(), cubeRandomRotation(ModBlocks.RICH_SOIL.get(), ""));

		this.crateBlock(ModBlocks.CABBAGE_CRATE.get(), "cabbage");
		this.crateBlock(ModBlocks.TOMATO_CRATE.get(), "tomato");
		this.crateBlock(ModBlocks.ONION_CRATE.get(), "onion");

		this.pantryBlock(ModBlocks.OAK_PANTRY.get(), "oak");
		this.pantryBlock(ModBlocks.BIRCH_PANTRY.get(), "birch");
		this.pantryBlock(ModBlocks.SPRUCE_PANTRY.get(), "spruce");
		this.pantryBlock(ModBlocks.JUNGLE_PANTRY.get(), "jungle");
		this.pantryBlock(ModBlocks.ACACIA_PANTRY.get(), "acacia");
		this.pantryBlock(ModBlocks.DARK_OAK_PANTRY.get(), "dark_oak");
		this.pantryBlock(ModBlocks.CRIMSON_PANTRY.get(), "crimson");
		this.pantryBlock(ModBlocks.WARPED_PANTRY.get(), "warped");
	}

	public ConfiguredModel[] cubeRandomRotation(Block block, String suffix) {
		String formattedName = blockName(block) + (suffix.isEmpty() ? "" : "_" + suffix);
		return ConfiguredModel.allYRotations(models().cubeAll(formattedName, rlBlockModel(formattedName)), 0, false);
	}

	public void crateBlock(Block block, String cropName) {
		this.simpleBlock(block,
				models().cubeBottomTop(blockName(block), rlBlockModel(cropName + "_crate_side"), rlBlockModel("crate_bottom"), rlBlockModel(cropName + "_crate_top")));
	}

	public void pantryBlock(Block block, String woodType) {
		this.horizontalBlock(block, state -> {
			String suffix = state.get(PantryBlock.OPEN) ? "_open" : "";
			return models().orientable(blockName(block) + suffix,
				rlBlockModel(woodType + "_pantry_side"),
				rlBlockModel(woodType + "_pantry_front" + suffix),
				rlBlockModel(woodType + "_pantry_top"));
		});
	}
}
