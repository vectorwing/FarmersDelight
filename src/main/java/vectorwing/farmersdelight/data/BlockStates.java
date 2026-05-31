package vectorwing.farmersdelight.data;

import com.google.common.collect.Sets;
import com.mojang.math.Quadrant;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.ConditionBuilder;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.*;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.*;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static net.minecraft.client.data.models.BlockModelGenerators.plainModel;
import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;
import static net.minecraft.client.data.models.BlockModelGenerators.variants;

/**
 * Single {@link ModelProvider} responsible for ALL of Farmer's Delight's generated blockstates, block models
 * and item models. Replaces the old separate {@code BlockStates} (BlockStateProvider) and {@code ItemModels}
 * (ItemModelProvider) which relied on the removed NeoForge flat datagen API.
 * <p>
 * Credits to Vazkii and team for some references on mass-reading blocks to datagen!
 */
public class BlockStates extends ModelProvider
{
	// Custom texture slots used by Farmer's Delight templates (no vanilla equivalent).
	private static final TextureSlot SLOT_HANDLE = TextureSlot.create("handle");
	private static final TextureSlot SLOT_CROP = TextureSlot.CROP;
	private static final TextureSlot SLOT_CROSS = TextureSlot.CROSS;
	private static final TextureSlot SLOT_ROPE_SIDE = TextureSlot.create("rope_side");
	private static final TextureSlot SLOT_ROPE_TOP = TextureSlot.create("rope_top");
	private static final TextureSlot SLOT_INNER = TextureSlot.create("inner");

	// Parent for mug-held drink items, authored by hand under src/main/resources.
	private static final Identifier MUG = Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "item/mug");

	private BlockModelGenerators blockModels;
	private ItemModelGenerators itemModels;
	private BiConsumer<Identifier, ModelInstance> modelOutput;

	public BlockStates(PackOutput output) {
		super(output, FarmersDelight.MODID);
	}

	@Override
	protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
		this.blockModels = blockModels;
		this.itemModels = itemModels;
		// Many crop/pie/compost stages share a single model across multiple blockstate variants. The vanilla
		// SimpleModelCollector throws on duplicate model ids, while the old NeoForge builder silently de-duplicated
		// by name. Wrap the output to keep that lenient, datagen-only behaviour (the JSON for a given id is identical).
		Set<Identifier> emittedModels = new java.util.HashSet<>();
		this.modelOutput = (id, model) -> {
			if (emittedModels.add(id)) {
				blockModels.modelOutput.accept(id, model);
			}
		};
		registerBlockModels();
		registerItemModels();
	}

	// Identifier helpers --------------------------

	private String blockName(Block block) {
		return BuiltInRegistries.BLOCK.getKey(block).getPath();
	}

	private String itemName(Item item) {
		return BuiltInRegistries.ITEM.getKey(item).getPath();
	}

	private Identifier resourceMCBlock(String path) {
		return Identifier.withDefaultNamespace("block/" + path);
	}

	private Identifier resourceFDBlock(String path) {
		return Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "block/" + path);
	}

	private Identifier resourceItem(String path) {
		return Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "item/" + path);
	}

	private Material fdBlockTexture(String path) {
		return new Material(resourceFDBlock(path));
	}

	/**
	 * Reference to a hand-authored block model living in src/main/resources (the old {@code existingModel(block)}).
	 */
	private MultiVariant existingModel(Block block) {
		return plainVariant(resourceFDBlock(blockName(block)));
	}

	private MultiVariant existingModel(String path) {
		return plainVariant(resourceFDBlock(path));
	}

	private Variant existingVariant(String path) {
		return plainModel(resourceFDBlock(path));
	}

	// Rotation helpers ----------------------------

	private static Quadrant quadrant(int degrees) {
		return switch (((degrees % 360) + 360) % 360) {
			case 90 -> Quadrant.R90;
			case 180 -> Quadrant.R180;
			case 270 -> Quadrant.R270;
			default -> Quadrant.R0;
		};
	}

	private static VariantMutator yRot(int degrees) {
		return VariantMutator.Y_ROT.withValue(quadrant(degrees));
	}

	private static VariantMutator xRot(int degrees) {
		return VariantMutator.X_ROT.withValue(quadrant(degrees));
	}

	private static int yRotFromFacing(Direction facing) {
		return ((int) facing.toYRot() + DEFAULT_ANGLE_OFFSET) % 360;
	}

	private static final int DEFAULT_ANGLE_OFFSET = 180;

	// Templates -----------------------------------

	private static ModelTemplate template(Identifier parent, TextureSlot... slots) {
		return new ModelTemplate(java.util.Optional.of(parent), java.util.Optional.empty(), slots);
	}

	// =========================================================================
	// BLOCK MODELS & BLOCKSTATES
	// =========================================================================

	private void registerBlockModels() {
		simpleBlock(ModBlocks.SAFETY_NET.get(), existingModel(ModBlocks.SAFETY_NET.get()));
		simpleBlock(ModBlocks.CANVAS_RUG.get(), existingModel(ModBlocks.CANVAS_RUG.get()));

		riceBagBlock(ModBlocks.RICE_BAG.get());

		customDirectionalBlock(ModBlocks.WOODEN_BASKET.get(),
				modelBasket(blockName(ModBlocks.WOODEN_BASKET.get())), BasketBlock.ENABLED, BasketBlock.WATERLOGGED);
		customDirectionalBlock(ModBlocks.BAMBOO_BASKET.get(),
				modelBasket(blockName(ModBlocks.BAMBOO_BASKET.get())), BasketBlock.ENABLED, BasketBlock.WATERLOGGED);
		customDirectionalBlock(ModBlocks.RICE_BALE.get(),
				modelCubeBottomTop(blockName(ModBlocks.RICE_BALE.get())));
		customHorizontalBlock(ModBlocks.CUTTING_BOARD.get(),
				existingVariant(blockName(ModBlocks.CUTTING_BOARD.get())), BasketBlock.WATERLOGGED);

		horizontalBlock(ModBlocks.HALF_TATAMI_MAT.get(), existingModel("tatami_mat_half"));

		stageBlock(ModBlocks.BROWN_MUSHROOM_COLONY.get(), MushroomColonyBlock.COLONY_AGE);
		stageBlock(ModBlocks.RED_MUSHROOM_COLONY.get(), MushroomColonyBlock.COLONY_AGE);

		customStageBlock(ModBlocks.CABBAGE_CROP.get(), resourceFDBlock("template_crop_cross"), SLOT_CROSS, CabbageBlock.AGE, Collections.emptyList());
		customStageBlock(ModBlocks.ONION_CROP.get(), resourceMCBlock("crop"), SLOT_CROP, OnionBlock.AGE, Arrays.asList(0, 0, 1, 1, 2, 2, 2, 3));
		customStageBlock(ModBlocks.BUDDING_TOMATO_CROP.get(), resourceFDBlock("template_crop_cross"), SLOT_CROSS, BuddingTomatoBlock.AGE, Arrays.asList(0, 1, 2, 3, 3));
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
		simpleBlock(ModBlocks.RICH_SOIL.get(), cubeRandomRotation(ModBlocks.RICH_SOIL.get()));
		farmlandBlock(ModBlocks.RICH_SOIL_FARMLAND.get(), ModBlocks.RICH_SOIL.get());

		ropeBlock(ModBlocks.ROPE.get());
		ropeFenceBlock(ModBlocks.ROPE_FENCE.get());
		ropeFenceGateBlock(ModBlocks.ROPE_FENCE_GATE.get());

		fullTatamiMatBlock(ModBlocks.FULL_TATAMI_MAT.get());
		tatamiBlock(ModBlocks.TATAMI.get());

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
		stoveBlock(ModBlocks.STOVE.get());

		for (Block sign : canvasSigns()) {
			simpleBlock(sign, existingModel(ModBlocks.CANVAS_SIGN.get()));
		}
	}

	// Blockstate primitives -----------------------

	private void simpleBlock(Block block, MultiVariant model) {
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, model));
	}

	private void horizontalBlock(Block block, MultiVariant model) {
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, model)
				.with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
	}

	private void axisBlock(RotatedPillarBlock block) {
		String name = blockName(block);
		TextureMapping mapping = new TextureMapping()
				.put(TextureSlot.END, fdBlockTexture(name + "_end"))
				.put(TextureSlot.SIDE, fdBlockTexture(name + "_side"));
		MultiVariant model = plainVariant(ModelTemplates.CUBE_COLUMN.create(block, mapping, modelOutput));
		blockModels.createAxisAlignedPillarBlockCustomModel(block, model);
	}

	// Individual block builders -------------------

	private void riceBagBlock(Block block) {
		String riceBag = blockName(block);
		ModelTemplate cube = template(resourceMCBlock("cube"),
				TextureSlot.PARTICLE, TextureSlot.DOWN, TextureSlot.UP,
				TextureSlot.NORTH, TextureSlot.SOUTH, TextureSlot.EAST, TextureSlot.WEST);
		TextureMapping mapping = new TextureMapping()
				.put(TextureSlot.PARTICLE, fdBlockTexture(riceBag + "_top"))
				.put(TextureSlot.DOWN, fdBlockTexture(riceBag + "_bottom"))
				.put(TextureSlot.UP, fdBlockTexture(riceBag + "_top"))
				.put(TextureSlot.NORTH, fdBlockTexture(riceBag + "_side_tied"))
				.put(TextureSlot.SOUTH, fdBlockTexture(riceBag + "_side_tied"))
				.put(TextureSlot.EAST, fdBlockTexture(riceBag + "_side"))
				.put(TextureSlot.WEST, fdBlockTexture(riceBag + "_side"));
		MultiVariant model = plainVariant(cube.create(block, mapping, modelOutput));
		simpleBlock(block, model);
	}

	private void cookingPotBlock(Block block) {
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(CookingPotBlock.SUPPORT, BlockStateProperties.HORIZONTAL_FACING)
						.generate((support, facing) -> {
							String supportSuffix = switch (support) {
								case NONE -> "";
								case TRAY -> "_tray";
								case HANDLE -> "_handle";
							};
							return existingModel(blockName(block) + supportSuffix)
									.with(yRot(yRotFromFacing(facing)));
						})));
	}

	private void skilletBlock(Block block) {
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(SkilletBlock.SUPPORT, BlockStateProperties.HORIZONTAL_FACING)
						.generate((support, facing) -> {
							String supportSuffix = support ? "_tray" : "";
							return existingModel(blockName(block) + supportSuffix)
									.with(yRot(yRotFromFacing(facing)));
						})));
	}

	private void stoveBlock(Block block) {
		String name = blockName(block);
		ModelTemplate orientableWithBottom = ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM;
		Identifier offModel = orientableWithBottom.createWithSuffix(block, "", new TextureMapping()
				.put(TextureSlot.SIDE, fdBlockTexture(name + "_side"))
				.put(TextureSlot.FRONT, fdBlockTexture(name + "_front"))
				.put(TextureSlot.BOTTOM, fdBlockTexture(name + "_bottom"))
				.put(TextureSlot.TOP, fdBlockTexture(name + "_top")), modelOutput);
		Identifier onModel = orientableWithBottom.createWithSuffix(block, "_on", new TextureMapping()
				.put(TextureSlot.SIDE, fdBlockTexture(name + "_side"))
				.put(TextureSlot.FRONT, fdBlockTexture(name + "_front_on"))
				.put(TextureSlot.BOTTOM, fdBlockTexture(name + "_bottom"))
				.put(TextureSlot.TOP, fdBlockTexture(name + "_top_on")), modelOutput);
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(StoveBlock.LIT)
						.select(false, plainVariant(offModel))
						.select(true, plainVariant(onModel)))
				.with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
	}

	private void ropeBlock(Block block) {
		blockModels.blockStateOutput.accept(MultiPartGenerator.multiPart(block)
				.with(existingModel("rope_post"))
				.with(condition(RopeBlock.TIED_TO_BELL, true), existingModel("rope_bell_tie"))
				.with(condition(RopeBlock.NORTH, true), existingModel("rope_side"))
				.with(condition(RopeBlock.EAST, true), existingModel("rope_side").with(yRot(90)))
				.with(condition(RopeBlock.SOUTH, true), existingModel("rope_side_alt"))
				.with(condition(RopeBlock.WEST, true), existingModel("rope_side_alt").with(yRot(90))));
	}

	private void ropeFenceBlock(Block block) {
		blockModels.blockStateOutput.accept(MultiPartGenerator.multiPart(block)
				.with(existingModel("rope_fence_post"))
				.with(condition(FenceBlock.NORTH, true), existingModel("rope_fence_side"))
				.with(condition(FenceBlock.EAST, true), existingModel("rope_fence_side").with(yRot(90)))
				.with(condition(FenceBlock.SOUTH, true), existingModel("rope_fence_side_alt"))
				.with(condition(FenceBlock.WEST, true), existingModel("rope_fence_side_alt").with(yRot(90))));
	}

	private void ropeFenceGateBlock(Block block) {
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(FenceGateBlock.IN_WALL, FenceGateBlock.OPEN, FenceGateBlock.FACING)
						.generate((inWall, open, facing) -> {
							String wallInfix = inWall ? "_wall" : "";
							String openSuffix = open ? "_open" : "";
							return existingModel(blockName(block) + wallInfix + openSuffix)
									.with(yRot((int) facing.toYRot()));
						})));
	}

	private void fullTatamiMatBlock(Block block) {
		Variant head = existingVariant("tatami_mat_head");
		Variant foot = existingVariant("tatami_mat_foot");
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(TatamiMatBlock.PART, TatamiMatBlock.FACING)
						.generate((part, facing) -> {
							Variant base = part == BedPart.HEAD ? head : foot;
							return variants(base.with(yRot((int) facing.toYRot())));
						})));
	}

	private void tatamiBlock(Block block) {
		Variant odd = existingVariant("tatami_odd");
		Variant even = existingVariant("tatami_even");
		Identifier notPairedModel = ModelTemplates.CUBE_ALL.createWithSuffix(block, "_half",
				TextureMapping.cube(fdBlockTexture("tatami_mat_half")), modelOutput);
		Variant notPaired = plainModel(notPairedModel);
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(TatamiBlock.PAIRED, TatamiBlock.FACING)
						.generate((paired, dir) -> {
							Variant base = paired ? (dir.get3DDataValue() % 2 == 0 ? even : odd) : notPaired;
							int xRotation = dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0;
							int yRotation = dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360;
							return variants(base.with(xRot(xRotation)).with(yRot(yRotation)));
						})));
	}

	private void cabinetBlock(Block block, String woodType) {
		ModelTemplate orientable = ModelTemplates.CUBE_ORIENTABLE;
		Identifier closed = orientable.createWithSuffix(block, "", new TextureMapping()
				.put(TextureSlot.SIDE, fdBlockTexture(woodType + "_cabinet_side"))
				.put(TextureSlot.FRONT, fdBlockTexture(woodType + "_cabinet_front"))
				.put(TextureSlot.TOP, fdBlockTexture(woodType + "_cabinet_top")), modelOutput);
		Identifier open = orientable.createWithSuffix(block, "_open", new TextureMapping()
				.put(TextureSlot.SIDE, fdBlockTexture(woodType + "_cabinet_side"))
				.put(TextureSlot.FRONT, fdBlockTexture(woodType + "_cabinet_front_open"))
				.put(TextureSlot.TOP, fdBlockTexture(woodType + "_cabinet_top")), modelOutput);
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(CabinetBlock.OPEN)
						.select(false, plainVariant(closed))
						.select(true, plainVariant(open)))
				.with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
	}

	private void organicCompostBlock(Block block) {
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(OrganicCompostBlock.COMPOSTING)
						.generate(composting -> {
							String textureName = blockName(block) + "_stage" + composting / 2;
							Identifier model = ModelTemplates.CUBE_ALL.createWithSuffix(block, "_stage" + composting / 2,
									TextureMapping.cube(fdBlockTexture(textureName)), modelOutput);
							return BlockModelGenerators.createRotatedVariants(plainModel(model));
						})));
	}

	private void farmlandBlock(Block farmlandBlock, Block dirtBlock) {
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(farmlandBlock)
				.with(PropertyDispatch.initial(RichSoilFarmlandBlock.MOISTURE)
						.generate(moisture -> plainVariant(
								modelFarmland(blockName(farmlandBlock), blockName(dirtBlock), moisture == 7)))));
	}

	private void customDirectionalBlock(Block block, MultiVariant model, Property<?>... ignored) {
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, model)
				.with(PropertyDispatch.modify(BlockStateProperties.FACING)
						.select(Direction.DOWN, xRot(180))
						.select(Direction.UP, VariantMutator.X_ROT.withValue(Quadrant.R0))
						.select(Direction.NORTH, xRot(90).then(yRot(yRotFromFacing(Direction.NORTH))))
						.select(Direction.SOUTH, xRot(90).then(yRot(yRotFromFacing(Direction.SOUTH))))
						.select(Direction.WEST, xRot(90).then(yRot(yRotFromFacing(Direction.WEST))))
						.select(Direction.EAST, xRot(90).then(yRot(yRotFromFacing(Direction.EAST))))));
	}

	private void customHorizontalBlock(Block block, Variant model, Property<?>... ignored) {
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, variants(model))
				.with(PropertyDispatch.modify(BlockStateProperties.HORIZONTAL_FACING)
						.select(Direction.NORTH, yRot(yRotFromFacing(Direction.NORTH)))
						.select(Direction.SOUTH, yRot(yRotFromFacing(Direction.SOUTH)))
						.select(Direction.WEST, yRot(yRotFromFacing(Direction.WEST)))
						.select(Direction.EAST, yRot(yRotFromFacing(Direction.EAST)))));
	}

	private void stageBlock(Block block, IntegerProperty ageProperty) {
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(ageProperty)
						.generate(age -> {
							String stageName = blockName(block) + "_stage" + age;
							return plainVariant(modelCross(stageName));
						})));
	}

	private void customStageBlock(Block block, @Nullable Identifier parent, TextureSlot textureKey, IntegerProperty ageProperty, List<Integer> suffixes) {
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(ageProperty)
						.generate(age -> {
							String stageName = blockName(block) + "_stage";
							stageName += suffixes.isEmpty() ? age : suffixes.get(Math.min(suffixes.size(), age));
							if (parent == null) {
								return plainVariant(modelCross(stageName));
							}
							ModelTemplate tpl = template(parent, textureKey);
							Identifier model = tpl.create(resourceFDBlock(stageName),
									new TextureMapping().put(textureKey, fdBlockTexture(stageName)), modelOutput);
							return plainVariant(model);
						})));
	}

	private void riceRootBlock(Block block) {
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(RiceBlock.AGE, RiceBlock.SUPPORTING)
						.generate((age, supporting) -> {
							boolean isSupporting = supporting && age == 3;
							String stageName = isSupporting
									? blockName(block) + "_supporting"
									: blockName(block) + "_stage" + age;
							return plainVariant(modelCross(stageName));
						})));
	}

	private void tomatoBlock(Block block, IntegerProperty ageProperty, BooleanProperty ropeloggedProperty) {
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(ageProperty, ropeloggedProperty)
						.generate((age, ropelogged) -> {
							String stageName = blockName(block) + "_stage" + age;
							String ropeloggedStageName = blockName(block) + "_old_stage" + age;
							if (ropelogged) {
								return plainVariant(modelCropWithRope(ropeloggedStageName, "tomatoes_coiled_rope"));
							}
							ModelTemplate tpl = template(resourceFDBlock("template_crop_cross"), SLOT_CROSS);
							Identifier model = tpl.create(resourceFDBlock(stageName),
									new TextureMapping().put(SLOT_CROSS, fdBlockTexture(stageName)), modelOutput);
							return plainVariant(model);
						})));
	}

	private void ropedTomatoBlock(Block block, IntegerProperty ageProperty) {
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(ageProperty)
						.generate(age -> {
							String stageName = blockName(block) + "_stage" + age;
							return plainVariant(modelCropWithRope(stageName, "tomatoes_coiled_rope"));
						})));
	}

	private void wildCropBlock(Block block) {
		simpleBlock(block, plainVariant(modelCross(blockName(block))));
	}

	private void crateBlock(Block block, String cropName) {
		TextureMapping mapping = new TextureMapping()
				.put(TextureSlot.SIDE, fdBlockTexture(cropName + "_crate_side"))
				.put(TextureSlot.BOTTOM, fdBlockTexture("crate_bottom"))
				.put(TextureSlot.TOP, fdBlockTexture(cropName + "_crate_top"));
		simpleBlock(block, plainVariant(ModelTemplates.CUBE_BOTTOM_TOP.create(block, mapping, modelOutput)));
	}

	private void feastBlock(FeastBlock block) {
		IntegerProperty servingsProperty = block.getServingsProperty();
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(servingsProperty, FeastBlock.FACING)
						.generate((servings, facing) -> {
							String suffix = "_stage" + (block.getMaxServings() - servings);
							if (servings == 0) {
								suffix = block.hasLeftovers ? "_leftovers" : "_stage" + (servingsProperty.getPossibleValues().toArray().length - 2);
							}
							return existingModel(blockName(block) + suffix).with(yRot(yRotFromFacing(facing)));
						})));
	}

	private void doublePlantBlock(Block block) {
		MultiVariant bottom = plainVariant(modelCross(blockName(block) + "_bottom"));
		MultiVariant top = plainVariant(modelCross(blockName(block) + "_top"));
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(DoublePlantBlock.HALF)
						.select(DoubleBlockHalf.LOWER, bottom)
						.select(DoubleBlockHalf.UPPER, top)));
	}

	/**
	 * Creates blockstates for a pie whose model is based on the pie template.
	 */
	private void pieBlock(Block block) {
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(PieBlock.BITES, PieBlock.FACING)
						.generate((bites, facing) -> {
							MultiVariant model = bites > 0 ? plainVariant(modelPieSlice(blockName(block), bites)) : plainVariant(modelPie(blockName(block)));
							return model.with(yRot(yRotFromFacing(facing)));
						})));
	}

	/**
	 * Creates blockstates for a pie whose model is custom, in an existing file.
	 */
	private void customPieBlock(Block block) {
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(PieBlock.BITES, PieBlock.FACING)
						.generate((bites, facing) -> {
							String suffix = bites > 0 ? "_slice" + bites : "";
							return existingModel(blockName(block) + suffix).with(yRot(yRotFromFacing(facing)));
						})));
	}

	// Model file generators -----------------------

	private MultiVariant cubeRandomRotation(Block block) {
		Identifier model = ModelTemplates.CUBE_ALL.create(block, TextureMapping.cube(block), modelOutput);
		return BlockModelGenerators.createRotatedVariants(plainModel(model));
	}

	private Identifier modelCross(String baseName) {
		return ModelTemplates.CROSS.create(resourceFDBlock(baseName),
				TextureMapping.cross(fdBlockTexture(baseName)), modelOutput);
	}

	private MultiVariant modelCubeBottomTop(String baseName) {
		TextureMapping mapping = new TextureMapping()
				.put(TextureSlot.BOTTOM, fdBlockTexture(baseName + "_bottom"))
				.put(TextureSlot.SIDE, fdBlockTexture(baseName + "_side"))
				.put(TextureSlot.TOP, fdBlockTexture(baseName + "_top"));
		ModelTemplate tpl = template(resourceMCBlock("cube_bottom_top"), TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
		return plainVariant(tpl.create(resourceFDBlock(baseName), mapping, modelOutput));
	}

	private MultiVariant modelBasket(String baseName) {
		TextureMapping mapping = new TextureMapping()
				.put(TextureSlot.BOTTOM, fdBlockTexture(baseName + "_bottom"))
				.put(TextureSlot.SIDE, fdBlockTexture(baseName + "_side"))
				.put(TextureSlot.TOP, fdBlockTexture(baseName + "_top"))
				.put(SLOT_HANDLE, fdBlockTexture(baseName + "_handle"));
		ModelTemplate tpl = template(resourceFDBlock("template_basket"),
				TextureSlot.BOTTOM, TextureSlot.SIDE, TextureSlot.TOP, SLOT_HANDLE);
		return plainVariant(tpl.create(resourceFDBlock(baseName), mapping, modelOutput));
	}

	private Identifier modelCropWithRope(String baseName, String ropeSideTextureName) {
		TextureMapping mapping = new TextureMapping()
				.put(SLOT_CROP, fdBlockTexture(baseName))
				.put(SLOT_ROPE_SIDE, fdBlockTexture(ropeSideTextureName))
				.put(SLOT_ROPE_TOP, fdBlockTexture("rope_top"));
		ModelTemplate tpl = template(resourceFDBlock("template_crop_with_rope"), SLOT_CROP, SLOT_ROPE_SIDE, SLOT_ROPE_TOP);
		return tpl.create(resourceFDBlock(baseName), mapping, modelOutput);
	}

	private Identifier modelPie(String baseName) {
		TextureMapping mapping = new TextureMapping()
				.put(TextureSlot.BOTTOM, fdBlockTexture("pie_bottom"))
				.put(TextureSlot.SIDE, fdBlockTexture("pie_side"))
				.put(TextureSlot.TOP, fdBlockTexture(baseName + "_top"));
		ModelTemplate tpl = template(resourceFDBlock("template_pie"), TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
		return tpl.create(resourceFDBlock(baseName), mapping, modelOutput);
	}

	private Identifier modelPieSlice(String baseName, int bites) {
		TextureMapping mapping = new TextureMapping()
				.put(TextureSlot.BOTTOM, fdBlockTexture("pie_bottom"))
				.put(TextureSlot.SIDE, fdBlockTexture("pie_side"))
				.put(SLOT_INNER, fdBlockTexture(baseName + "_inner"))
				.put(TextureSlot.TOP, fdBlockTexture(baseName + "_top"));
		ModelTemplate tpl = template(resourceFDBlock("template_pie_slice" + bites), TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE, SLOT_INNER);
		return tpl.create(resourceFDBlock(baseName + "_slice" + bites), mapping, modelOutput);
	}

	private Identifier modelFarmland(String farmlandName, String dirtName, boolean moist) {
		String moistSuffix = moist ? "_moist" : "";
		TextureMapping mapping = new TextureMapping()
				.put(TextureSlot.BOTTOM, fdBlockTexture(dirtName))
				.put(TextureSlot.SIDE, fdBlockTexture(moist ? farmlandName + moistSuffix + "_side" : dirtName))
				.put(TextureSlot.TOP, fdBlockTexture(farmlandName + moistSuffix));
		ModelTemplate tpl = template(resourceFDBlock("template_farmland_custom"), TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
		return tpl.create(resourceFDBlock(farmlandName + moistSuffix), mapping, modelOutput);
	}

	// Conditions ----------------------------------

	private static ConditionBuilder condition(BooleanProperty property, boolean value) {
		return BlockModelGenerators.condition().term(property, value);
	}

	// =========================================================================
	// ITEM MODELS
	// =========================================================================

	private void registerItemModels() {
		Set<Item> items = BuiltInRegistries.ITEM.stream()
				.filter(i -> FarmersDelight.MODID.equals(BuiltInRegistries.ITEM.getKey(i).getNamespace()))
				.collect(Collectors.toSet());

		// Specific cases
		// Skillet: previously used the deprecated ItemProperties "cooking" override (removed in 26.1).
		// We emit a plain item model pointing at the hand-authored block/skillet model.
		// TODO(follow-up): re-implement the "cooking" conditional item model via a custom ConditionalItemModelProperty.
		registerSimpleItemModel(ModItems.SKILLET.get(), resourceFDBlock("skillet"));
		items.remove(ModItems.SKILLET.get());

		itemGeneratedModel(ModItems.WILD_RICE.get(), resourceFDBlock(itemName(ModItems.WILD_RICE.get()) + "_top"));
		items.remove(ModItems.WILD_RICE.get());

		itemGeneratedModel(ModItems.BROWN_MUSHROOM_COLONY.get(), resourceFDBlock(itemName(ModItems.BROWN_MUSHROOM_COLONY.get()) + "_stage3"));
		items.remove(ModItems.BROWN_MUSHROOM_COLONY.get());

		itemGeneratedModel(ModItems.DEBUG_PUMPKIN_PIE.get(), resourceItem("debug_pumpkin_pie"));
		items.remove(ModItems.DEBUG_PUMPKIN_PIE.get());

		itemGeneratedModel(ModItems.RED_MUSHROOM_COLONY.get(), resourceFDBlock(itemName(ModItems.RED_MUSHROOM_COLONY.get()) + "_stage3"));
		items.remove(ModItems.RED_MUSHROOM_COLONY.get());

		blockBasedModel(ModItems.TATAMI.get(), "_half");
		items.remove(ModItems.TATAMI.get());

		blockBasedModel(ModItems.ORGANIC_COMPOST.get(), "_stage0");
		items.remove(ModItems.ORGANIC_COMPOST.get());

		blockBasedModel(ModItems.ROPE_FENCE.get(), "_inventory");
		items.remove(ModItems.ROPE_FENCE.get());

		// Items that should be held like a mug
		Set<Item> mugItems = Sets.newHashSet(
				ModItems.HOT_COCOA.get(),
				ModItems.APPLE_CIDER.get(),
				ModItems.MELON_JUICE.get());
		ItemModels.takeAll(items, mugItems.toArray(new Item[0])).forEach(item -> itemMugModel(item, resourceItem(itemName(item))));

		// Blocks with special item sprites
		Set<Item> spriteBlockItems = spriteBlockItems();
		ItemModels.takeAll(items, spriteBlockItems.toArray(new Item[0])).forEach(item -> itemGeneratedModel(item, resourceItem(itemName(item))));

		// Blocks with flat block textures for their items
		Set<Item> flatBlockItems = Sets.newHashSet(
				ModItems.SAFETY_NET.get(),
				ModItems.SANDY_SHRUB.get(),
				ModItems.WILD_BEETROOTS.get(),
				ModItems.WILD_CABBAGES.get(),
				ModItems.WILD_CARROTS.get(),
				ModItems.WILD_ONIONS.get(),
				ModItems.WILD_POTATOES.get(),
				ModItems.WILD_TOMATOES.get()
		);
		ItemModels.takeAll(items, flatBlockItems.toArray(new Item[0])).forEach(item -> itemGeneratedModel(item, resourceFDBlock(itemName(item))));

		// Blocks whose items look alike (use their block model as the item model)
		ItemModels.takeAll(items, i -> i instanceof BlockItem).forEach(item -> blockBasedModel(item, ""));

		// Handheld items
		Set<Item> handheldItems = Sets.newHashSet(
				ModItems.BARBECUE_STICK.get(),
				ModItems.HAM.get(),
				ModItems.SMOKED_HAM.get(),
				ModItems.FLINT_KNIFE.get(),
				ModItems.IRON_KNIFE.get(),
				ModItems.DIAMOND_KNIFE.get(),
				ModItems.GOLDEN_KNIFE.get(),
				ModItems.NETHERITE_KNIFE.get()
		);
		ItemModels.takeAll(items, handheldItems.toArray(new Item[0])).forEach(item -> itemHandheldModel(item, resourceItem(itemName(item))));

		// Generated items
		items.forEach(item -> itemGeneratedModel(item, resourceItem(itemName(item))));
	}

	private void registerSimpleItemModel(Item item, Identifier model) {
		itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(model));
	}

	/**
	 * Item whose model simply parents the given block model (optionally with a suffix).
	 */
	private void blockBasedModel(Item item, String suffix) {
		Identifier blockModel = resourceFDBlock(itemName(item) + suffix);
		itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(blockModel));
	}

	private void itemGeneratedModel(Item item, Identifier texture) {
		Identifier model = ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item),
				TextureMapping.layer0(new Material(texture)), modelOutput);
		itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(model));
	}

	private void itemHandheldModel(Item item, Identifier texture) {
		Identifier model = ModelTemplates.FLAT_HANDHELD_ITEM.create(ModelLocationUtils.getModelLocation(item),
				TextureMapping.layer0(new Material(texture)), modelOutput);
		itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(model));
	}

	private void itemMugModel(Item item, Identifier texture) {
		ModelTemplate mugTemplate = template(MUG, TextureSlot.LAYER0);
		Identifier model = mugTemplate.create(ModelLocationUtils.getModelLocation(item),
				TextureMapping.layer0(new Material(texture)), modelOutput);
		itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(model));
	}

	// Bulk block sets -----------------------------

	private Set<Block> canvasSigns() {
		return Sets.newHashSet(
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
	}

	private Set<Item> spriteBlockItems() {
		return Sets.newHashSet(
				ModItems.FULL_TATAMI_MAT.get(),
				ModItems.HALF_TATAMI_MAT.get(),
				ModItems.ROPE.get(),
				ModItems.CANVAS_SIGN.get(),
				ModItems.HANGING_CANVAS_SIGN.get(),
				ModItems.WHITE_CANVAS_SIGN.get(),
				ModItems.WHITE_HANGING_CANVAS_SIGN.get(),
				ModItems.ORANGE_CANVAS_SIGN.get(),
				ModItems.ORANGE_HANGING_CANVAS_SIGN.get(),
				ModItems.MAGENTA_CANVAS_SIGN.get(),
				ModItems.MAGENTA_HANGING_CANVAS_SIGN.get(),
				ModItems.LIGHT_BLUE_CANVAS_SIGN.get(),
				ModItems.LIGHT_BLUE_HANGING_CANVAS_SIGN.get(),
				ModItems.YELLOW_CANVAS_SIGN.get(),
				ModItems.YELLOW_HANGING_CANVAS_SIGN.get(),
				ModItems.LIME_CANVAS_SIGN.get(),
				ModItems.LIME_HANGING_CANVAS_SIGN.get(),
				ModItems.PINK_CANVAS_SIGN.get(),
				ModItems.PINK_HANGING_CANVAS_SIGN.get(),
				ModItems.GRAY_CANVAS_SIGN.get(),
				ModItems.GRAY_HANGING_CANVAS_SIGN.get(),
				ModItems.LIGHT_GRAY_CANVAS_SIGN.get(),
				ModItems.LIGHT_GRAY_HANGING_CANVAS_SIGN.get(),
				ModItems.CYAN_CANVAS_SIGN.get(),
				ModItems.CYAN_HANGING_CANVAS_SIGN.get(),
				ModItems.PURPLE_CANVAS_SIGN.get(),
				ModItems.PURPLE_HANGING_CANVAS_SIGN.get(),
				ModItems.BLUE_CANVAS_SIGN.get(),
				ModItems.BLUE_HANGING_CANVAS_SIGN.get(),
				ModItems.BROWN_CANVAS_SIGN.get(),
				ModItems.BROWN_HANGING_CANVAS_SIGN.get(),
				ModItems.GREEN_CANVAS_SIGN.get(),
				ModItems.GREEN_HANGING_CANVAS_SIGN.get(),
				ModItems.RED_CANVAS_SIGN.get(),
				ModItems.RED_HANGING_CANVAS_SIGN.get(),
				ModItems.BLACK_CANVAS_SIGN.get(),
				ModItems.BLACK_HANGING_CANVAS_SIGN.get(),
				ModItems.APPLE_PIE.get(),
				ModItems.SWEET_BERRY_CHEESECAKE.get(),
				ModItems.CHOCOLATE_PIE.get(),
				ModItems.CABBAGE_SEEDS.get(),
				ModItems.TOMATO_SEEDS.get(),
				ModItems.ONION.get(),
				ModItems.RICE.get(),
				ModItems.ROAST_CHICKEN_BLOCK.get(),
				ModItems.STUFFED_PUMPKIN_BLOCK.get(),
				ModItems.HONEY_GLAZED_HAM_BLOCK.get(),
				ModItems.SHEPHERDS_PIE_BLOCK.get(),
				ModItems.GLEAMING_SALAD_BLOCK.get(),
				ModItems.RICE_ROLL_MEDLEY_BLOCK.get()
		);
	}
}
