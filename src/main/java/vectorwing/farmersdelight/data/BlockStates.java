package vectorwing.farmersdelight.data;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.Property;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.blocks.BasketBlock;
import vectorwing.farmersdelight.blocks.PantryBlock;
import vectorwing.farmersdelight.blocks.PieBlock;
import vectorwing.farmersdelight.blocks.StoveBlock;
import vectorwing.farmersdelight.registry.ModBlocks;

import java.util.function.Function;

public class BlockStates extends BlockStateProvider
{
	private static final int DEFAULT_ANGLE_OFFSET = 180;

	public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, FarmersDelight.MODID, exFileHelper);
	}

	private String blockName(Block block) {
		return block.getRegistryName().getPath();
	}

	public ResourceLocation resourceBlock(String path) {
		return new ResourceLocation(FarmersDelight.MODID, "block/" + path);
	}

	public ModelFile existingModel(Block block) {
		return new ModelFile.ExistingModelFile(resourceBlock(blockName(block)), models().existingFileHelper);
	}

	public ModelFile existingModel(String path) {
		return new ModelFile.ExistingModelFile(resourceBlock(path), models().existingFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		this.simpleBlock(ModBlocks.RICH_SOIL.get(), cubeRandomRotation(ModBlocks.RICH_SOIL.get(), ""));
		this.simpleBlock(ModBlocks.SAFETY_NET.get(), existingModel(ModBlocks.SAFETY_NET.get()));
		this.simpleBlock(ModBlocks.HALF_TATAMI_MAT.get(), existingModel("tatami_mat_half"));

		customDirectionalBlock(ModBlocks.BASKET.get(),
				$ -> existingModel(ModBlocks.BASKET.get()), BasketBlock.ENABLED, BasketBlock.WATERLOGGED);
		customDirectionalBlock(ModBlocks.RICE_BALE.get(),
				$ -> existingModel(ModBlocks.RICE_BALE.get()));
		customHorizontalBlock(ModBlocks.CUTTING_BOARD.get(),
				$ -> existingModel(ModBlocks.CUTTING_BOARD.get()), BasketBlock.WATERLOGGED);

		this.horizontalBlock(ModBlocks.STOVE.get(), state -> {
			String name = blockName(ModBlocks.STOVE.get());
			String suffix = state.get(StoveBlock.LIT) ? "_on" : "";

			return models().orientableWithBottom(name + suffix,
					resourceBlock(name + "_side"),
					resourceBlock(name + "_front" + suffix),
					resourceBlock(name + "_bottom"),
					resourceBlock(name + "_top" + suffix));
		});

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

		this.pieBlock(ModBlocks.APPLE_PIE.get());
		this.pieBlock(ModBlocks.CHOCOLATE_PIE.get());
		this.pieBlock(ModBlocks.SWEET_BERRY_CHEESECAKE.get());

		this.wildCropBlock(ModBlocks.WILD_BEETROOTS.get(), false);
		this.wildCropBlock(ModBlocks.WILD_CABBAGES.get(), false);
		this.wildCropBlock(ModBlocks.WILD_POTATOES.get(), false);
		this.wildCropBlock(ModBlocks.WILD_TOMATOES.get(), false);
		this.wildCropBlock(ModBlocks.WILD_CARROTS.get(), true);
		this.wildCropBlock(ModBlocks.WILD_ONIONS.get(), true);
		this.doublePlantBlock(ModBlocks.WILD_RICE.get());
	}

	public ConfiguredModel[] cubeRandomRotation(Block block, String suffix) {
		String formattedName = blockName(block) + (suffix.isEmpty() ? "" : "_" + suffix);
		return ConfiguredModel.allYRotations(models().cubeAll(formattedName, resourceBlock(formattedName)), 0, false);
	}

	public void customDirectionalBlock(Block block, Function<BlockState, ModelFile> modelFunc, Property<?>... ignored) {
		getVariantBuilder(block)
				.forAllStatesExcept(state -> {
							Direction dir = state.get(BlockStateProperties.FACING);
							return ConfiguredModel.builder()
									.modelFile(modelFunc.apply(state))
									.rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
									.rotationY(dir.getAxis().isVertical() ? 0 : ((int) dir.getHorizontalAngle() + DEFAULT_ANGLE_OFFSET) % 360)
									.build();
						},
						ignored);
	}

	public void customHorizontalBlock(Block block, Function<BlockState, ModelFile> modelFunc, Property<?>... ignored) {
		getVariantBuilder(block)
				.forAllStatesExcept(state -> ConfiguredModel.builder()
						.modelFile(modelFunc.apply(state))
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

	public void doublePlantBlock(Block block) {
		getVariantBuilder(block)
				.partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
				.modelForState().modelFile(models().cross(blockName(block) + "_bottom", resourceBlock(blockName(block) + "_bottom"))).addModel()
				.partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
				.modelForState().modelFile(models().cross(blockName(block) + "_top", resourceBlock(blockName(block) + "_top"))).addModel();
	}

	public void pieBlock(Block block) {
		getVariantBuilder(block)
				.forAllStates(state -> {
							int bites = state.get(PieBlock.BITES);
							String suffix = bites > 0 ? "_slice" + bites : "";
							return ConfiguredModel.builder()
									.modelFile(existingModel(blockName(block) + suffix))
									.build();
						}
				);
	}
}
