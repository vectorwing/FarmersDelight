package vectorwing.farmersdelight.data;

import com.google.common.collect.Sets;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
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
		return BuiltInRegistries.BLOCK.getKey(block).getPath();
	}

	public ResourceLocation resourceMCBlock(String path) {
		return ResourceLocation.withDefaultNamespace(ModelProvider.BLOCK_FOLDER + "/" + path);
	}

	public ResourceLocation resourceFDBlock(String path) {
		return ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, ModelProvider.BLOCK_FOLDER + "/" + path);
	}

	public ModelFile existingModel(Block block) {
		return new ModelFile.ExistingModelFile(resourceFDBlock(blockName(block)), models().existingFileHelper);
	}

	public ModelFile existingModel(String path) {
		return new ModelFile.ExistingModelFile(resourceFDBlock(path), models().existingFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlock(ModBlocks.SAFETY_NET.get(), existingModel(ModBlocks.SAFETY_NET.get()));
		simpleBlock(ModBlocks.CANVAS_RUG.get(), existingModel(ModBlocks.CANVAS_RUG.get()));

		String riceBag = blockName(ModBlocks.RICE_BAG.get());
		this.simpleBlock(ModBlocks.RICE_BAG.get(), models().withExistingParent(riceBag, "cube")
				.texture("particle", resourceFDBlock(riceBag + "_top"))
				.texture("down", resourceFDBlock(riceBag + "_bottom"))
				.texture("up", resourceFDBlock(riceBag + "_top"))
				.texture("north", resourceFDBlock(riceBag + "_side_tied"))
				.texture("south", resourceFDBlock(riceBag + "_side_tied"))
				.texture("east", resourceFDBlock(riceBag + "_side"))
				.texture("west", resourceFDBlock(riceBag + "_side"))
		);
		customDirectionalBlock(ModBlocks.WOODEN_BASKET.get(),
				$ -> modelBasket(blockName(ModBlocks.WOODEN_BASKET.get())), BasketBlock.ENABLED, BasketBlock.WATERLOGGED);
		customDirectionalBlock(ModBlocks.BAMBOO_BASKET.get(),
				$ -> modelBasket(blockName(ModBlocks.BAMBOO_BASKET.get())), BasketBlock.ENABLED, BasketBlock.WATERLOGGED);
		customDirectionalBlock(ModBlocks.RICE_BALE.get(),
				$ -> modelCubeBottomTop(blockName(ModBlocks.RICE_BALE.get())));
		customHorizontalBlock(ModBlocks.CUTTING_BOARD.get(),
				$ -> existingModel(ModBlocks.CUTTING_BOARD.get()), BasketBlock.WATERLOGGED);

		horizontalBlock(ModBlocks.HALF_TATAMI_MAT.get(), existingModel("tatami_mat_half"));

		stageBlock(ModBlocks.BROWN_MUSHROOM_COLONY.get(), MushroomColonyBlock.COLONY_AGE);
		stageBlock(ModBlocks.RED_MUSHROOM_COLONY.get(), MushroomColonyBlock.COLONY_AGE);

		customStageBlock(ModBlocks.CABBAGE_CROP.get(), resourceFDBlock("template_crop_cross"), "cross", CabbageBlock.AGE, new ArrayList<>());
		customStageBlock(ModBlocks.ONION_CROP.get(), mcLoc("crop"), "crop", OnionBlock.AGE, Arrays.asList(0, 0, 1, 1, 2, 2, 2, 3));
		customStageBlock(ModBlocks.BUDDING_TOMATO_CROP.get(), resourceFDBlock("template_crop_cross"), "cross", BuddingTomatoBlock.AGE, Arrays.asList(0, 1, 2, 3, 3));
		tomatoBlock(ModBlocks.TOMATO_CROP.get(), TomatoBlock.VINE_AGE, TomatoBlock.ROPELOGGED);
		ropedTomatoBlock(ModBlocks.TOMATO_CROP_ON_ROPE.get(), TomatoBlock.VINE_AGE);
		riceRootBlock(ModBlocks.RICE_CROP.get());
		stageBlock(ModBlocks.RICE_CROP_PANICLES.get(), RicePaniclesBlock.RICE_AGE);

		crateBlock(ModBlocks.CARROT_CRATE.get(), "carrot");
		crateBlock(ModBlocks.POTATO_CRATE.get(), "potato");
		crateBlock(ModBlocks.BEETROOT_CRATE.get(), "beetroot");
		crateBlock(ModBlocks.CABBAGE_CRATE.get(), "cabbage");
		crateBlock(ModBlocks.TOMATO_CRATE.get(), "tomato");
		crateBlock(ModBlocks.ONION_CRATE.get(), "onion");

		axisBlock((RotatedPillarBlock) ModBlocks.STRAW_BALE.get());

		organicCompostBlock(ModBlocks.ORGANIC_COMPOST.get());
		simpleBlock(ModBlocks.RICH_SOIL.get(), cubeRandomRotation(ModBlocks.RICH_SOIL.get(), ""));
		farmlandBlock(ModBlocks.RICH_SOIL_FARMLAND.get(), ModBlocks.RICH_SOIL.get());

		this.getMultipartBuilder(ModBlocks.ROPE.get())
				.part().modelFile(existingModel("rope_post")).addModel().end()
				.part().modelFile(existingModel("rope_bell_tie")).addModel().condition(RopeBlock.TIED_TO_BELL, true).end()
				.part().modelFile(existingModel("rope_side")).addModel().condition(RopeBlock.NORTH, true).end()
				.part().modelFile(existingModel("rope_side")).rotationY(90).addModel().condition(RopeBlock.EAST, true).end()
				.part().modelFile(existingModel("rope_side_alt")).addModel().condition(RopeBlock.SOUTH, true).end()
				.part().modelFile(existingModel("rope_side_alt")).rotationY(90).addModel().condition(RopeBlock.WEST, true).end();

		this.getMultipartBuilder(ModBlocks.ROPE_FENCE.get())
				.part().modelFile(existingModel("rope_fence_post")).addModel().end()
				.part().modelFile(existingModel("rope_fence_side")).addModel().condition(FenceBlock.NORTH, true).end()
				.part().modelFile(existingModel("rope_fence_side")).rotationY(90).addModel().condition(FenceBlock.EAST, true).end()
				.part().modelFile(existingModel("rope_fence_side_alt")).addModel().condition(FenceBlock.SOUTH, true).end()
				.part().modelFile(existingModel("rope_fence_side_alt")).rotationY(90).addModel().condition(FenceBlock.WEST, true).end();

		ropeFenceGateBlock(ModBlocks.ROPE_FENCE_GATE.get());

		ModelFile head = existingModel("tatami_mat_head");
		ModelFile foot = existingModel("tatami_mat_foot");
		this.getVariantBuilder(ModBlocks.FULL_TATAMI_MAT.get()).forAllStates(state ->
				ConfiguredModel.builder().modelFile(state.getValue(TatamiMatBlock.PART) == BedPart.HEAD ? head : foot).rotationY((int) state.getValue(TatamiMatBlock.FACING).toYRot()).build());

		ModelFile odd = existingModel("tatami_odd");
		ModelFile even = existingModel("tatami_even");
		ModelFile notPaired = models().cubeAll(blockName(ModBlocks.TATAMI.get()) + "_half", ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "block/tatami_mat_half"));
		this.getVariantBuilder(ModBlocks.TATAMI.get()).forAllStates(state -> {
			Direction dir = state.getValue(TatamiBlock.FACING);
			return ConfiguredModel.builder().modelFile(state.getValue(TatamiBlock.PAIRED) ? dir.get3DDataValue() % 2 == 0 ? even : odd : notPaired)
					.rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
					.rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360).build();
		});

		cabinetBlock(ModBlocks.OAK_CABINET.get(), "oak");
		cabinetBlock(ModBlocks.BIRCH_CABINET.get(), "birch");
		cabinetBlock(ModBlocks.SPRUCE_CABINET.get(), "spruce");
		cabinetBlock(ModBlocks.JUNGLE_CABINET.get(), "jungle");
		cabinetBlock(ModBlocks.ACACIA_CABINET.get(), "acacia");
		cabinetBlock(ModBlocks.DARK_OAK_CABINET.get(), "dark_oak");
		cabinetBlock(ModBlocks.MANGROVE_CABINET.get(), "mangrove");
		cabinetBlock(ModBlocks.CHERRY_CABINET.get(), "cherry");
		cabinetBlock(ModBlocks.BAMBOO_CABINET.get(), "bamboo");
		cabinetBlock(ModBlocks.CRIMSON_CABINET.get(), "crimson");
		cabinetBlock(ModBlocks.WARPED_CABINET.get(), "warped");

		pieBlock(ModBlocks.APPLE_PIE.get());
		customPieBlock(ModBlocks.CHOCOLATE_PIE.get());
		pieBlock(ModBlocks.SWEET_BERRY_CHEESECAKE.get());
		pieBlock(ModBlocks.PUMPKIN_PIE.get());

		feastBlock((FeastBlock) ModBlocks.STUFFED_PUMPKIN_BLOCK.get());
		feastBlock((FeastBlock) ModBlocks.ROAST_CHICKEN_BLOCK.get());
		feastBlock((FeastBlock) ModBlocks.HONEY_GLAZED_HAM_BLOCK.get());
		feastBlock((FeastBlock) ModBlocks.SHEPHERDS_PIE_BLOCK.get());
		feastBlock((FeastBlock) ModBlocks.GLEAMING_SALAD_BLOCK.get());
		feastBlock((FeastBlock) ModBlocks.RICE_ROLL_MEDLEY_BLOCK.get());

		wildCropBlock(ModBlocks.SANDY_SHRUB.get());
		wildCropBlock(ModBlocks.WILD_BEETROOTS.get());
		wildCropBlock(ModBlocks.WILD_CABBAGES.get());
		wildCropBlock(ModBlocks.WILD_POTATOES.get());
		wildCropBlock(ModBlocks.WILD_TOMATOES.get());
		wildCropBlock(ModBlocks.WILD_CARROTS.get());
		wildCropBlock(ModBlocks.WILD_ONIONS.get());
		doublePlantBlock(ModBlocks.WILD_RICE.get());

		cookingPotBlock(ModBlocks.COOKING_POT.get());
		skilletBlock(ModBlocks.SKILLET.get());
		horizontalBlock(ModBlocks.STOVE.get(), state -> {
			String name = blockName(ModBlocks.STOVE.get());
			String suffix = state.getValue(StoveBlock.LIT) ? "_on" : "";

			return models().orientableWithBottom(name + suffix,
					resourceFDBlock(name + "_side"),
					resourceFDBlock(name + "_front" + suffix),
					resourceFDBlock(name + "_bottom"),
					resourceFDBlock(name + "_top" + suffix));
		});

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
			simpleBlock(sign, existingModel(ModBlocks.CANVAS_SIGN.get()));
		}
	}

	public void cookingPotBlock(Block block) {
		getVariantBuilder(block).forAllStatesExcept(state -> {
			String supportSuffix = switch (state.getValue(CookingPotBlock.SUPPORT)) {
				case NONE -> "";
				case TRAY -> "_tray";
				case HANDLE -> "_handle";
			};
			return ConfiguredModel.builder()
					.modelFile(existingModel(blockName(block) + supportSuffix))
					.rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + DEFAULT_ANGLE_OFFSET) % 360)
					.build();
		}, CookingPotBlock.WATERLOGGED);
	}

	public void skilletBlock(Block block) {
		getVariantBuilder(block).forAllStatesExcept(state -> {
			String supportSuffix = state.getValue(SkilletBlock.SUPPORT) ? "_tray" : "";
			return ConfiguredModel.builder()
					.modelFile(existingModel(blockName(block) + supportSuffix))
					.rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + DEFAULT_ANGLE_OFFSET) % 360)
					.build();
		}, SkilletBlock.WATERLOGGED);
	}

	public void ropeFenceGateBlock(Block block) {
		getVariantBuilder(block).forAllStatesExcept(state -> {
			String wallInfix = state.getValue(FenceGateBlock.IN_WALL) ? "_wall" : "";
			ModelFile modelClosed = existingModel(blockName(block) + wallInfix);
			ModelFile modelOpen = existingModel(blockName(block) + wallInfix + "_open");
			return ConfiguredModel.builder()
					.modelFile(state.getValue(FenceGateBlock.OPEN) ? modelOpen : modelClosed)
					.rotationY((int) state.getValue(FenceGateBlock.FACING).toYRot())
					.build();
		}, FenceGateBlock.POWERED);
	}

	public void organicCompostBlock(Block block) {
		getVariantBuilder(block).forAllStates(state -> {
			int composting = state.getValue(OrganicCompostBlock.COMPOSTING);
			String textureName = blockName(block) + "_stage" + composting / 2;
			return ConfiguredModel.allYRotations(models().cubeAll(textureName, resourceFDBlock(textureName)), 0, false);
		});
	}

	public void farmlandBlock(Block farmlandBlock, Block dirtBlock) {
		getVariantBuilder(farmlandBlock).forAllStates(state -> {
			int moisture = state.getValue(RichSoilFarmlandBlock.MOISTURE);
			return ConfiguredModel.builder()
					.modelFile(modelFarmland(blockName(farmlandBlock), blockName(dirtBlock), moisture == 7))
					.build();
		});
	}

	public void customDirectionalBlock(Block block, Function<BlockState, ModelFile> modelFunc, Property<?>... ignored) {
		getVariantBuilder(block).forAllStatesExcept(state -> {
			Direction dir = state.getValue(BlockStateProperties.FACING);
			return ConfiguredModel.builder()
					.modelFile(modelFunc.apply(state))
					.rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
					.rotationY(dir.getAxis().isVertical() ? 0 : ((int) dir.toYRot() + DEFAULT_ANGLE_OFFSET) % 360)
					.build();
		}, ignored);
	}

	public void customHorizontalBlock(Block block, Function<BlockState, ModelFile> modelFunc, Property<?>... ignored) {
		getVariantBuilder(block).forAllStatesExcept(state -> ConfiguredModel.builder()
				.modelFile(modelFunc.apply(state))
				.rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + DEFAULT_ANGLE_OFFSET) % 360)
				.build(), ignored);
	}

	public void stageBlock(Block block, IntegerProperty ageProperty, Property<?>... ignored) {
		getVariantBuilder(block).forAllStatesExcept(state -> {
			int age = state.getValue(ageProperty);
			String stageName = blockName(block) + "_stage" + age;
			return ConfiguredModel.builder()
					.modelFile(models().cross(stageName, resourceFDBlock(stageName)).renderType("cutout")).build();
		}, ignored);
	}

	public void customStageBlock(Block block, @Nullable ResourceLocation parent, String textureKey, IntegerProperty ageProperty, List<Integer> suffixes, Property<?>... ignored) {
		getVariantBuilder(block).forAllStatesExcept(state -> {
			int age = state.getValue(ageProperty);
			String stageName = blockName(block) + "_stage";
			stageName += suffixes.isEmpty() ? age : suffixes.get(Math.min(suffixes.size(), age));
			if (parent == null) {
				return ConfiguredModel.builder()
						.modelFile(models().cross(stageName, resourceFDBlock(stageName)).renderType("cutout")).build();
			}
			return ConfiguredModel.builder()
					.modelFile(models().singleTexture(stageName, parent, textureKey, resourceFDBlock(stageName)).renderType("cutout")).build();
		}, ignored);
	}

	public void riceRootBlock(Block block) {
		getVariantBuilder(block).forAllStatesExcept(state -> {
			int age = state.getValue(RiceBlock.AGE);
			boolean isSupporting = state.getValue(RiceBlock.SUPPORTING) && age == 3;
			String stageName = isSupporting
					? blockName(block) + "_supporting"
					: blockName(block) + "_stage" + age;
			return ConfiguredModel.builder().modelFile(models().cross(stageName, resourceFDBlock(stageName))
					.renderType("cutout")).build();
		});
	}

	public void tomatoBlock(Block block, IntegerProperty ageProperty, BooleanProperty ropeloggedProperty, Property<?>... ignored) {
		getVariantBuilder(block).forAllStatesExcept(state -> {
			int age = state.getValue(ageProperty);
			boolean ropelogged = state.getValue(ropeloggedProperty);
			String stageName = blockName(block) + "_stage" + age;
			String ropeloggedStageName = blockName(block) + "_old_stage" + age; // TODO: Make this a customStageBlock once we remove the ropelogged state for good.
			return ConfiguredModel.builder()
					.modelFile(ropelogged
							? modelCropWithRope(ropeloggedStageName, "tomatoes_coiled_rope")
							: models().singleTexture(stageName, resourceFDBlock("template_crop_cross"), "cross", resourceFDBlock(stageName)).renderType("cutout")).build();
		}, ignored);
	}

	public void ropedTomatoBlock(Block block, IntegerProperty ageProperty, Property<?>... ignored) {
		getVariantBuilder(block).forAllStatesExcept(state -> {
			int age = state.getValue(ageProperty);
			String stageName = blockName(block) + "_stage" + age;
			return ConfiguredModel.builder()
					.modelFile(modelCropWithRope(stageName, "tomatoes_coiled_rope")).build();
		}, ignored);
	}

	public void wildCropBlock(Block block) {
		this.wildCropBlock(block, false);
	}

	public void wildCropBlock(Block block, boolean isBushCrop) {
		if (isBushCrop) {
			this.simpleBlock(block, models().singleTexture(blockName(block), resourceFDBlock("template_bush_crop"), "crop", resourceFDBlock(blockName(block))).renderType("cutout"));
		} else {
			this.simpleBlock(block, models().cross(blockName(block), resourceFDBlock(blockName(block))).renderType("cutout"));
		}
	}

	public void crateBlock(Block block, String cropName) {
		this.simpleBlock(block,
				models().cubeBottomTop(blockName(block), resourceFDBlock(cropName + "_crate_side"), resourceFDBlock("crate_bottom"), resourceFDBlock(cropName + "_crate_top")));
	}

	public void cabinetBlock(Block block, String woodType) {
		this.horizontalBlock(block, state -> {
			String suffix = state.getValue(CabinetBlock.OPEN) ? "_open" : "";
			return models().orientable(blockName(block) + suffix,
					resourceFDBlock(woodType + "_cabinet_side"),
					resourceFDBlock(woodType + "_cabinet_front" + suffix),
					resourceFDBlock(woodType + "_cabinet_top"));
		});
	}

	public void feastBlock(FeastBlock block) {
		getVariantBuilder(block).forAllStates(state -> {
			IntegerProperty servingsProperty = block.getServingsProperty();
			int servings = state.getValue(servingsProperty);

			String suffix = "_stage" + (block.getMaxServings() - servings);

			if (servings == 0) {
				suffix = block.hasLeftovers ? "_leftovers" : "_stage" + (servingsProperty.getPossibleValues().toArray().length - 2);
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
				.modelForState().modelFile(models().cross(blockName(block) + "_bottom", resourceFDBlock(blockName(block) + "_bottom")).renderType("cutout")).addModel()
				.partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
				.modelForState().modelFile(models().cross(blockName(block) + "_top", resourceFDBlock(blockName(block) + "_top")).renderType("cutout")).addModel();
	}

	/**
	 * Creates blockstates for a pie whose model is based on the pie template.
	 */
	public void pieBlock(Block block) {
		getVariantBuilder(block).forAllStates(state -> {
			int bites = state.getValue(PieBlock.BITES);
			return ConfiguredModel.builder()
					.modelFile(bites > 0 ? modelPieSlice(blockName(block), bites) : modelPie(blockName(block)))
					.rotationY(((int) state.getValue(PieBlock.FACING).toYRot() + DEFAULT_ANGLE_OFFSET) % 360)
					.build();
		});
	}

	/**
	 * Creates blockstates for a pie whose model is custom, in an existing file.
	 */
	public void customPieBlock(Block block) {
		getVariantBuilder(block).forAllStates(state -> {
			int bites = state.getValue(PieBlock.BITES);
			String suffix = bites > 0 ? "_slice" + bites : "";
			return ConfiguredModel.builder()
					.modelFile(existingModel(blockName(block) + suffix))
					.rotationY(((int) state.getValue(PieBlock.FACING).toYRot() + DEFAULT_ANGLE_OFFSET) % 360)
					.build();
		});
	}

	// Model Functions --------------------------

	public ConfiguredModel[] cubeRandomRotation(Block block, String suffix) {
		String formattedName = blockName(block) + (suffix.isEmpty() ? "" : "_" + suffix);
		return ConfiguredModel.allYRotations(models().cubeAll(formattedName, resourceFDBlock(formattedName)), 0, false);
	}

	private ModelFile modelCubeBottomTop(String baseName) {
		return models().withExistingParent(baseName, resourceMCBlock("cube_bottom_top"))
				.texture("bottom", resourceFDBlock(baseName + "_bottom"))
				.texture("side", resourceFDBlock(baseName + "_side"))
				.texture("top", resourceFDBlock(baseName + "_top"));
	}

	private ModelFile modelBasket(String baseName) {
		return models().withExistingParent(baseName, resourceFDBlock("template_basket"))
				.texture("bottom", resourceFDBlock(baseName + "_bottom"))
				.texture("side", resourceFDBlock(baseName + "_side"))
				.texture("top", resourceFDBlock(baseName + "_top"))
				.texture("handle", resourceFDBlock(baseName + "_handle"));
	}

	private ModelFile modelCropCross(String baseName) {
		return models().withExistingParent(baseName, resourceFDBlock("template_crop_cross"))
				.texture("cross", resourceFDBlock(baseName))
				.renderType("cutout");
	}

	private ModelFile modelCropWithRope(String baseName, String ropeSideTextureName) {
		return models().withExistingParent(baseName, resourceFDBlock("template_crop_with_rope"))
				.texture("crop", resourceFDBlock(baseName))
				.texture("rope_side", resourceFDBlock(ropeSideTextureName))
				.texture("rope_top", resourceFDBlock("rope_top"))
				.renderType("cutout");
	}

	private ModelFile modelPie(String baseName) {
		return models().withExistingParent(baseName, resourceFDBlock("template_pie"))
				.texture("bottom", resourceFDBlock("pie_bottom"))
				.texture("side", resourceFDBlock("pie_side"))
				.texture("top", resourceFDBlock(baseName + "_top"));
	}

	private ModelFile modelPieSlice(String baseName, int bites) {
		return models().withExistingParent(baseName + "_slice" + bites, resourceFDBlock("template_pie_slice" + bites))
				.texture("bottom", resourceFDBlock("pie_bottom"))
				.texture("side", resourceFDBlock("pie_side"))
				.texture("inner", resourceFDBlock(baseName + "_inner"))
				.texture("top", resourceFDBlock(baseName + "_top"));
	}

	private ModelFile modelFarmland(String farmlandName, String dirtName, boolean moist) {
		String moistSuffix = moist ? "_moist" : "";
		return models().withExistingParent(farmlandName + moistSuffix, resourceFDBlock("template_farmland_custom"))
				.texture("bottom", resourceFDBlock(dirtName))
				.texture("side", resourceFDBlock(moist ? farmlandName + moistSuffix + "_side" : dirtName))
				.texture("top", resourceFDBlock(farmlandName + moistSuffix));
	}
}
