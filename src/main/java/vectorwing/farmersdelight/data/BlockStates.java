package vectorwing.farmersdelight.data;

import com.google.common.collect.Sets;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.*;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class BlockStates extends BlockStateProvider
{
	private static final int DEFAULT_ANGLE_OFFSET = 180;

	public BlockStates(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, FarmersDelight.MODID, existingFileHelper);
	}

	private String blockName(Block block) {
		return ForgeRegistries.BLOCKS.getKey(block).getPath();
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
		List<Integer> stages = Arrays.asList(0, 0, 1, 1, 2, 2, 3, 3);
		this.getVariantBuilder(ModBlocks.ORGANIC_COMPOST.get()).forAllStates(state -> {
			ModelFile currentFile = models().cubeAll("organic_compost_" + state.getValue(OrganicCompostBlock.COMPOSTING),
					new ResourceLocation(FarmersDelight.MODID, "block/organic_compost_stage" + stages.get(state.getValue(OrganicCompostBlock.COMPOSTING))));

			return ConfiguredModel.builder()
					.modelFile(currentFile).nextModel()
					.modelFile(currentFile).rotationY(90).nextModel()
					.modelFile(currentFile).rotationY(180).nextModel()
					.modelFile(currentFile).rotationY(270).build();
		});

		this.simpleBlock(ModBlocks.RICH_SOIL.get(), cubeRandomRotation(ModBlocks.RICH_SOIL.get(), ""));
		this.getVariantBuilder(ModBlocks.RICH_SOIL_FARMLAND.get()).forAllStates(state ->
			ConfiguredModel.builder().modelFile(existingModel(state.getValue(RichSoilFarmlandBlock.MOISTURE) == 7 ? "rich_soil_farmland_moist" : "rich_soil_farmland")).build());
		this.simpleBlock(ModBlocks.SAFETY_NET.get(), existingModel("safety_net"));
		this.simpleBlock(ModBlocks.CANVAS_RUG.get(), existingModel("canvas_rug"));

		this.getMultipartBuilder(ModBlocks.ROPE.get())
				.part().modelFile(existingModel("rope_post")).addModel().end()
				.part().modelFile(existingModel("rope_bell_tie")).addModel().condition(RopeBlock.TIED_TO_BELL, true).end()
				.part().modelFile(existingModel("rope_side")).addModel().condition(RopeBlock.NORTH, true).end()
				.part().modelFile(existingModel("rope_side")).rotationY(90).addModel().condition(RopeBlock.EAST, true).end()
				.part().modelFile(existingModel("rope_side_alt")).addModel().condition(RopeBlock.SOUTH, true).end()
				.part().modelFile(existingModel("rope_side_alt")).rotationY(90).addModel().condition(RopeBlock.WEST, true).end();

		Set<Block> canvasSigns = Sets.newHashSet(
				// Standard
				ModBlocks.CANVAS_SIGN.get(),
				ModBlocks.HANGING_CANVAS_SIGN.get(),
				ModBlocks.WHITE_CANVAS_SIGN.get(),
				ModBlocks.WHITE_HANGING_CANVAS_SIGN.get(),
				ModBlocks.ORANGE_CANVAS_SIGN.get(),
				ModBlocks.ORANGE_HANGING_CANVAS_SIGN.get(),
				ModBlocks.MAGENTA_CANVAS_SIGN.get(),
				ModBlocks.MAGENTA_HANGING_CANVAS_SIGN.get(),
				ModBlocks.LIGHT_BLUE_CANVAS_SIGN.get(),
				ModBlocks.LIGHT_BLUE_HANGING_CANVAS_SIGN.get(),
				ModBlocks.YELLOW_CANVAS_SIGN.get(),
				ModBlocks.YELLOW_HANGING_CANVAS_SIGN.get(),
				ModBlocks.LIME_CANVAS_SIGN.get(),
				ModBlocks.LIME_HANGING_CANVAS_SIGN.get(),
				ModBlocks.PINK_CANVAS_SIGN.get(),
				ModBlocks.PINK_HANGING_CANVAS_SIGN.get(),
				ModBlocks.GRAY_CANVAS_SIGN.get(),
				ModBlocks.GRAY_HANGING_CANVAS_SIGN.get(),
				ModBlocks.LIGHT_GRAY_CANVAS_SIGN.get(),
				ModBlocks.LIGHT_GRAY_HANGING_CANVAS_SIGN.get(),
				ModBlocks.CYAN_CANVAS_SIGN.get(),
				ModBlocks.CYAN_HANGING_CANVAS_SIGN.get(),
				ModBlocks.PURPLE_CANVAS_SIGN.get(),
				ModBlocks.PURPLE_HANGING_CANVAS_SIGN.get(),
				ModBlocks.BLUE_CANVAS_SIGN.get(),
				ModBlocks.BLUE_HANGING_CANVAS_SIGN.get(),
				ModBlocks.BROWN_CANVAS_SIGN.get(),
				ModBlocks.BROWN_HANGING_CANVAS_SIGN.get(),
				ModBlocks.GREEN_CANVAS_SIGN.get(),
				ModBlocks.GREEN_HANGING_CANVAS_SIGN.get(),
				ModBlocks.RED_CANVAS_SIGN.get(),
				ModBlocks.RED_HANGING_CANVAS_SIGN.get(),
				ModBlocks.BLACK_CANVAS_SIGN.get(),
				ModBlocks.BLACK_HANGING_CANVAS_SIGN.get(),
				// Wall
				ModBlocks.CANVAS_WALL_SIGN.get(),
				ModBlocks.HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.WHITE_CANVAS_WALL_SIGN.get(),
				ModBlocks.WHITE_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.ORANGE_CANVAS_WALL_SIGN.get(),
				ModBlocks.ORANGE_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.MAGENTA_CANVAS_WALL_SIGN.get(),
				ModBlocks.MAGENTA_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.LIGHT_BLUE_CANVAS_WALL_SIGN.get(),
				ModBlocks.LIGHT_BLUE_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.YELLOW_CANVAS_WALL_SIGN.get(),
				ModBlocks.YELLOW_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.LIME_CANVAS_WALL_SIGN.get(),
				ModBlocks.LIME_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.PINK_CANVAS_WALL_SIGN.get(),
				ModBlocks.PINK_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.GRAY_CANVAS_WALL_SIGN.get(),
				ModBlocks.GRAY_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.LIGHT_GRAY_CANVAS_WALL_SIGN.get(),
				ModBlocks.LIGHT_GRAY_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.CYAN_CANVAS_WALL_SIGN.get(),
				ModBlocks.CYAN_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.PURPLE_CANVAS_WALL_SIGN.get(),
				ModBlocks.PURPLE_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.BLUE_CANVAS_WALL_SIGN.get(),
				ModBlocks.BLUE_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.BROWN_CANVAS_WALL_SIGN.get(),
				ModBlocks.BROWN_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.GREEN_CANVAS_WALL_SIGN.get(),
				ModBlocks.GREEN_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.RED_CANVAS_WALL_SIGN.get(),
				ModBlocks.RED_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.BLACK_CANVAS_WALL_SIGN.get(),
				ModBlocks.BLACK_HANGING_CANVAS_WALL_SIGN.get());

		for (Block sign : canvasSigns) {
			this.simpleBlock(sign, existingModel(ModBlocks.CANVAS_SIGN.get()));
		}

		String riceBag = blockName(ModBlocks.RICE_BAG.get());
		this.simpleBlock(ModBlocks.RICE_BAG.get(), models().withExistingParent(riceBag, "cube")
				.texture("particle", resourceBlock(riceBag + "_top"))
				.texture("down", resourceBlock(riceBag + "_bottom"))
				.texture("up", resourceBlock(riceBag + "_top"))
				.texture("north", resourceBlock(riceBag + "_side_tied"))
				.texture("south", resourceBlock(riceBag + "_side_tied"))
				.texture("east", resourceBlock(riceBag + "_side"))
				.texture("west", resourceBlock(riceBag + "_side"))
		);

		customDirectionalBlock(ModBlocks.BASKET.get(),
				$ -> existingModel(ModBlocks.BASKET.get()), BasketBlock.ENABLED, BasketBlock.WATERLOGGED);
		customDirectionalBlock(ModBlocks.RICE_BALE.get(),
				$ -> existingModel(ModBlocks.RICE_BALE.get()));
		customHorizontalBlock(ModBlocks.CUTTING_BOARD.get(),
				$ -> existingModel(ModBlocks.CUTTING_BOARD.get()), BasketBlock.WATERLOGGED);

		this.horizontalBlock(ModBlocks.HALF_TATAMI_MAT.get(), existingModel("tatami_mat_half"));
		ModelFile head = existingModel("tatami_mat_head");
		ModelFile foot = existingModel("tatami_mat_foot");
		this.getVariantBuilder(ModBlocks.FULL_TATAMI_MAT.get()).forAllStates(state ->
				ConfiguredModel.builder().modelFile(state.getValue(TatamiMatBlock.PART) == BedPart.HEAD ? head : foot).rotationY((int) state.getValue(TatamiMatBlock.FACING).toYRot()).build());

		ModelFile odd = existingModel("tatami_odd");
		ModelFile even = existingModel("tatami_even");
		ModelFile notPaired = models().cubeAll(ModBlocks.TATAMI.getId().getPath() + "_half", new ResourceLocation(FarmersDelight.MODID, "block/tatami_mat_half"));
		this.getVariantBuilder(ModBlocks.TATAMI.get()).forAllStates(state -> {
			Direction dir = state.getValue(TatamiBlock.FACING);
			return ConfiguredModel.builder().modelFile(state.getValue(TatamiBlock.PAIRED) ? dir.get3DDataValue() % 2 == 0 ? even : odd : notPaired)
					.rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
					.rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360).build();
		});

		this.getVariantBuilder(ModBlocks.COOKING_POT.get()).forAllStates(state -> switch (state.getValue(CookingPotBlock.SUPPORT)) {
			case NONE -> ConfiguredModel.builder().modelFile(existingModel("cooking_pot")).rotationY((int) state.getValue(CookingPotBlock.FACING).toYRot()).build();
			case TRAY -> ConfiguredModel.builder().modelFile(existingModel("cooking_pot_tray")).rotationY((int) state.getValue(CookingPotBlock.FACING).toYRot()).build();
			case HANDLE -> ConfiguredModel.builder().modelFile(existingModel("cooking_pot_handle")).rotationY((int) state.getValue(CookingPotBlock.FACING).toYRot()).build();
		});

		this.getVariantBuilder(ModBlocks.SKILLET.get()).forAllStates(state ->
			ConfiguredModel.builder().modelFile(existingModel(state.getValue(SkilletBlock.SUPPORT) ? "skillet_tray" : "skillet")).rotationY((int) state.getValue(SkilletBlock.FACING).toYRot()).build());

		this.horizontalBlock(ModBlocks.STOVE.get(), state -> {
			String name = blockName(ModBlocks.STOVE.get());
			String suffix = state.getValue(StoveBlock.LIT) ? "_on" : "";

			return models().orientableWithBottom(name + suffix,
					resourceBlock(name + "_side"),
					resourceBlock(name + "_front" + suffix),
					resourceBlock(name + "_bottom"),
					resourceBlock(name + "_top" + suffix));
		});

		this.stageBlock(ModBlocks.BROWN_MUSHROOM_COLONY.get(), MushroomColonyBlock.COLONY_AGE);
		this.stageBlock(ModBlocks.RED_MUSHROOM_COLONY.get(), MushroomColonyBlock.COLONY_AGE);
		this.stageBlock(ModBlocks.RICE_CROP_PANICLES.get(), RicePaniclesBlock.RICE_AGE);
		this.customStageBlock(ModBlocks.CABBAGE_CROP.get(), resourceBlock("crop_cross"), "cross", CabbageBlock.AGE, new ArrayList<>());
		this.customStageBlock(ModBlocks.ONION_CROP.get(), mcLoc("crop"), "crop", OnionBlock.AGE, Arrays.asList(0, 0, 1, 1, 2, 2, 2, 3));
		this.customStageBlock(ModBlocks.BUDDING_TOMATO_CROP.get(), resourceBlock("crop_cross"), "cross", BuddingTomatoBlock.AGE, Arrays.asList(0, 1, 2, 3, 3));
		this.getVariantBuilder(ModBlocks.TOMATO_CROP.get()).forAllStates(state ->
				ConfiguredModel.builder().modelFile(existingModel("tomatoes_" + (state.getValue(TomatoVineBlock.ROPELOGGED) ? "vine_" : "") + "stage" + state.getValue(TomatoVineBlock.VINE_AGE))).build());
		this.getVariantBuilder(ModBlocks.RICE_CROP.get()).forAllStates(state ->
				ConfiguredModel.builder().modelFile(existingModel("rice_" + (state.getValue(RiceBlock.SUPPORTING) && state.getValue(RiceBlock.AGE) == 3 ? "supporting" : "stage" + state.getValue(RiceBlock.AGE)))).build());

		this.crateBlock(ModBlocks.CARROT_CRATE.get(), "carrot");
		this.crateBlock(ModBlocks.POTATO_CRATE.get(), "potato");
		this.crateBlock(ModBlocks.BEETROOT_CRATE.get(), "beetroot");
		this.crateBlock(ModBlocks.CABBAGE_CRATE.get(), "cabbage");
		this.crateBlock(ModBlocks.TOMATO_CRATE.get(), "tomato");
		this.crateBlock(ModBlocks.ONION_CRATE.get(), "onion");

		this.axisBlock((RotatedPillarBlock) ModBlocks.STRAW_BALE.get());

		this.cabinetBlock(ModBlocks.OAK_CABINET.get(), "oak");
		this.cabinetBlock(ModBlocks.BIRCH_CABINET.get(), "birch");
		this.cabinetBlock(ModBlocks.SPRUCE_CABINET.get(), "spruce");
		this.cabinetBlock(ModBlocks.JUNGLE_CABINET.get(), "jungle");
		this.cabinetBlock(ModBlocks.ACACIA_CABINET.get(), "acacia");
		this.cabinetBlock(ModBlocks.DARK_OAK_CABINET.get(), "dark_oak");
		this.cabinetBlock(ModBlocks.MANGROVE_CABINET.get(), "mangrove");
		this.cabinetBlock(ModBlocks.CHERRY_CABINET.get(), "cherry");
		this.cabinetBlock(ModBlocks.BAMBOO_CABINET.get(), "bamboo");
		this.cabinetBlock(ModBlocks.CRIMSON_CABINET.get(), "crimson");
		this.cabinetBlock(ModBlocks.WARPED_CABINET.get(), "warped");

		this.pieBlock(ModBlocks.APPLE_PIE.get());
		this.pieBlock(ModBlocks.CHOCOLATE_PIE.get());
		this.pieBlock(ModBlocks.SWEET_BERRY_CHEESECAKE.get());

		this.feastBlock((FeastBlock) ModBlocks.STUFFED_PUMPKIN_BLOCK.get());
		this.feastBlock((FeastBlock) ModBlocks.ROAST_CHICKEN_BLOCK.get());
		this.feastBlock((FeastBlock) ModBlocks.HONEY_GLAZED_HAM_BLOCK.get());
		this.feastBlock((FeastBlock) ModBlocks.SHEPHERDS_PIE_BLOCK.get());
		this.feastBlock((FeastBlock) ModBlocks.RICE_ROLL_MEDLEY_BLOCK.get());

		this.wildCropBlock(ModBlocks.SANDY_SHRUB.get());
		this.wildCropBlock(ModBlocks.WILD_BEETROOTS.get());
		this.wildCropBlock(ModBlocks.WILD_CABBAGES.get());
		this.wildCropBlock(ModBlocks.WILD_POTATOES.get());
		this.wildCropBlock(ModBlocks.WILD_TOMATOES.get());
		this.wildCropBlock(ModBlocks.WILD_CARROTS.get());
		this.wildCropBlock(ModBlocks.WILD_ONIONS.get());
		this.doublePlantBlock(ModBlocks.WILD_RICE.get());
	}

	public ConfiguredModel[] cubeRandomRotation(Block block, String suffix) {
		String formattedName = blockName(block) + (suffix.isEmpty() ? "" : "_" + suffix);
		return ConfiguredModel.allYRotations(models().cubeAll(formattedName, resourceBlock(formattedName)), 0, false);
	}

	public void customDirectionalBlock(Block block, Function<BlockState, ModelFile> modelFunc, Property<?>... ignored) {
		getVariantBuilder(block)
				.forAllStatesExcept(state -> {
					Direction dir = state.getValue(BlockStateProperties.FACING);
					return ConfiguredModel.builder()
							.modelFile(modelFunc.apply(state))
							.rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
							.rotationY(dir.getAxis().isVertical() ? 0 : ((int) dir.toYRot() + DEFAULT_ANGLE_OFFSET) % 360)
							.build();
				}, ignored);
	}

	public void customHorizontalBlock(Block block, Function<BlockState, ModelFile> modelFunc, Property<?>... ignored) {
		getVariantBuilder(block)
				.forAllStatesExcept(state -> ConfiguredModel.builder()
						.modelFile(modelFunc.apply(state))
						.rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + DEFAULT_ANGLE_OFFSET) % 360)
						.build(), ignored);
	}

	public void stageBlock(Block block, IntegerProperty ageProperty, Property<?>... ignored) {
		getVariantBuilder(block)
				.forAllStatesExcept(state -> {
					int ageSuffix = state.getValue(ageProperty);
					String stageName = blockName(block) + "_stage" + ageSuffix;
					return ConfiguredModel.builder()
							.modelFile(models().cross(stageName, resourceBlock(stageName)).renderType("cutout")).build();
				}, ignored);
	}

	// I am not proud of this method... But hey, it's runData. Only I shall have to deal with it.
	public void customStageBlock(Block block, @Nullable ResourceLocation parent, String textureKey, IntegerProperty ageProperty, List<Integer> suffixes, Property<?>... ignored) {
		getVariantBuilder(block)
				.forAllStatesExcept(state -> {
					int ageSuffix = state.getValue(ageProperty);
					String stageName = blockName(block) + "_stage";
					stageName += suffixes.isEmpty() ? ageSuffix : suffixes.get(Math.min(suffixes.size(), ageSuffix));
					if (parent == null) {
						return ConfiguredModel.builder()
								.modelFile(models().cross(stageName, resourceBlock(stageName)).renderType("cutout")).build();
					}
					return ConfiguredModel.builder()
							.modelFile(models().singleTexture(stageName, parent, textureKey, resourceBlock(stageName)).renderType("cutout")).build();
				}, ignored);
	}

	public void wildCropBlock(Block block) {
		this.wildCropBlock(block, false);
	}

	public void wildCropBlock(Block block, boolean isBushCrop) {
		if (isBushCrop) {
			this.simpleBlock(block, models().singleTexture(blockName(block), resourceBlock("bush_crop"), "crop", resourceBlock(blockName(block))).renderType("cutout"));
		} else {
			this.simpleBlock(block, models().cross(blockName(block), resourceBlock(blockName(block))).renderType("cutout"));
		}
	}

	public void crateBlock(Block block, String cropName) {
		this.simpleBlock(block,
				models().cubeBottomTop(blockName(block), resourceBlock(cropName + "_crate_side"), resourceBlock("crate_bottom"), resourceBlock(cropName + "_crate_top")));
	}

	public void cabinetBlock(Block block, String woodType) {
		this.horizontalBlock(block, state -> {
			String suffix = state.getValue(CabinetBlock.OPEN) ? "_open" : "";
			return models().orientable(blockName(block) + suffix,
					resourceBlock(woodType + "_cabinet_side"),
					resourceBlock(woodType + "_cabinet_front" + suffix),
					resourceBlock(woodType + "_cabinet_top"));
		});
	}

	public void feastBlock(FeastBlock block) {
		getVariantBuilder(block)
				.forAllStates(state -> {
					IntegerProperty servingsProperty = block.getServingsProperty();
					int servings = state.getValue(servingsProperty);

					String suffix = "_stage" + (block.getMaxServings() - servings);

					if (servings == 0) {
						suffix = block.hasLeftovers ? "_leftover" : "_stage" + (servingsProperty.getPossibleValues().toArray().length - 2);
					}

					return ConfiguredModel.builder()
							.modelFile(existingModel(blockName(block) + suffix))
							.rotationY(((int) state.getValue(FeastBlock.FACING).toYRot() + DEFAULT_ANGLE_OFFSET) % 360)
							.build();
				});
	}

	public void doublePlantBlock(Block block) {
		getVariantBuilder(block)
				.partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
				.modelForState().modelFile(models().cross(blockName(block) + "_bottom", resourceBlock(blockName(block) + "_bottom")).renderType("cutout")).addModel()
				.partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
				.modelForState().modelFile(models().cross(blockName(block) + "_top", resourceBlock(blockName(block) + "_top")).renderType("cutout")).addModel();
	}

	public void pieBlock(Block block) {
		getVariantBuilder(block)
				.forAllStates(state -> {
							int bites = state.getValue(PieBlock.BITES);
							String suffix = bites > 0 ? "_slice" + bites : "";
							return ConfiguredModel.builder()
									.modelFile(existingModel(blockName(block) + suffix))
									.rotationY(((int) state.getValue(PieBlock.FACING).toYRot() + DEFAULT_ANGLE_OFFSET) % 360)
									.build();
						}
				);
	}
}
