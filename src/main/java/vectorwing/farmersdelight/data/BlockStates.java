package vectorwing.farmersdelight.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.Property;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.blocks.BasketBlock;
import vectorwing.farmersdelight.blocks.PantryBlock;
import vectorwing.farmersdelight.registry.ModBlocks;

public class BlockStates extends BlockStateProvider
{
	private static final int DEFAULT_ANGLE_OFFSET = 180;

	public BlockStates(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
		super(gen, modid, exFileHelper);
	}

	private String blockName(Block block) {
		return block.getRegistryName().getPath();
	}

	public ResourceLocation resourceBlock(String path) {
		return new ResourceLocation(FarmersDelight.MODID, "block/" + path);
	}

	@Override
	protected void registerStatesAndModels() {
		this.simpleBlock(ModBlocks.RICH_SOIL.get(), cubeRandomRotation(ModBlocks.RICH_SOIL.get(), ""));

		customDirectionalBlock(ModBlocks.BASKET.get(),
				new ModelFile.ExistingModelFile(resourceBlock(blockName(ModBlocks.BASKET.get())), models().existingFileHelper), BasketBlock.ENABLED, BasketBlock.WATERLOGGED);
		customDirectionalBlock(ModBlocks.RICE_BALE.get(),
				new ModelFile.ExistingModelFile(resourceBlock(blockName(ModBlocks.RICE_BALE.get())), models().existingFileHelper));
		customHorizontalBlock(ModBlocks.CUTTING_BOARD.get(),
				new ModelFile.ExistingModelFile(resourceBlock(blockName(ModBlocks.CUTTING_BOARD.get())), models().existingFileHelper), BasketBlock.WATERLOGGED);

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

		this.wildCropBlock(ModBlocks.WILD_BEETROOTS.get(), false);
		this.wildCropBlock(ModBlocks.WILD_CABBAGES.get(), false);
		this.wildCropBlock(ModBlocks.WILD_POTATOES.get(), false);
		this.wildCropBlock(ModBlocks.WILD_TOMATOES.get(), false);
		this.wildCropBlock(ModBlocks.WILD_CARROTS.get(), true);
		this.wildCropBlock(ModBlocks.WILD_ONIONS.get(), true);
	}

	public ConfiguredModel[] cubeRandomRotation(Block block, String suffix) {
		String formattedName = blockName(block) + (suffix.isEmpty() ? "" : "_" + suffix);
		return ConfiguredModel.allYRotations(models().cubeAll(formattedName, resourceBlock(formattedName)), 0, false);
	}

	public void customDirectionalBlock(Block block, ModelFile model, Property<?>... ignored) {
		getVariantBuilder(block)
				.forAllStatesExcept(state -> {
							Direction dir = state.get(BlockStateProperties.FACING);
							return ConfiguredModel.builder()
									.modelFile(model)
									.rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
									.rotationY(dir.getAxis().isVertical() ? 0 : ((int) dir.getHorizontalAngle() + DEFAULT_ANGLE_OFFSET) % 360)
									.build();
						},
						ignored);
	}

	public void customHorizontalBlock(Block block, ModelFile model, Property<?>... ignored) {
		getVariantBuilder(block)
				.forAllStatesExcept(state -> ConfiguredModel.builder()
						.modelFile(model)
						.rotationY(((int) state.get(BlockStateProperties.HORIZONTAL_FACING).getHorizontalAngle() + DEFAULT_ANGLE_OFFSET) % 360)
						.build(), ignored);
	}

	public void wildCropBlock(Block block, boolean isBushCrop) {
		if (isBushCrop) {
			this.simpleBlock(block, models().singleTexture(blockName(block), resourceBlock("bush_crop"), "crop", resourceBlock(blockName(block))));
		} else {
			this.simpleBlock(block, models().cross(blockName(block), resourceBlock(blockName(block))));
		}
	}

	public void crateBlock(Block block, String cropName) {
		this.simpleBlock(block,
				models().cubeBottomTop(blockName(block), resourceBlock(cropName + "_crate_side"), resourceBlock("crate_bottom"), resourceBlock(cropName + "_crate_top")));
	}

	public void pantryBlock(Block block, String woodType) {
		this.horizontalBlock(block, state -> {
			String suffix = state.get(PantryBlock.OPEN) ? "_open" : "";
			return models().orientable(blockName(block) + suffix,
					resourceBlock(woodType + "_pantry_side"),
					resourceBlock(woodType + "_pantry_front" + suffix),
					resourceBlock(woodType + "_pantry_top"));
		});
	}
}
