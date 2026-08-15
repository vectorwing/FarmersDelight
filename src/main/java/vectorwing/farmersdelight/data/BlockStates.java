package vectorwing.farmersdelight.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.AbstractStoveBlock;
import vectorwing.farmersdelight.common.block.BasketBlock;
import vectorwing.farmersdelight.common.block.BuddingBushBlock;
import vectorwing.farmersdelight.common.block.CabinetBlock;
import vectorwing.farmersdelight.common.block.CabbageBlock;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.FeastBlock;
import vectorwing.farmersdelight.common.block.MushroomColonyBlock;
import vectorwing.farmersdelight.common.block.OnionBlock;
import vectorwing.farmersdelight.common.block.OrganicCompostBlock;
import vectorwing.farmersdelight.common.block.PieBlock;
import vectorwing.farmersdelight.common.block.RichSoilFarmlandBlock;
import vectorwing.farmersdelight.common.block.RiceBaleBlock;
import vectorwing.farmersdelight.common.block.RiceBlock;
import vectorwing.farmersdelight.common.block.RicePaniclesBlock;
import vectorwing.farmersdelight.common.block.RopeBlock;
import vectorwing.farmersdelight.common.block.SkilletBlock;
import vectorwing.farmersdelight.common.block.TatamiBlock;
import vectorwing.farmersdelight.common.block.TatamiHalfMatBlock;
import vectorwing.farmersdelight.common.block.TatamiMatBlock;
import vectorwing.farmersdelight.common.block.TomatoBlock;
import vectorwing.farmersdelight.common.block.state.CookingPotSupport;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class BlockStates extends ModelProvider
{
	private static final TextureSlot HANDLE = TextureSlot.create("handle");
	private static final TextureSlot INNER = TextureSlot.create("inner");
	private static final TextureSlot ROPE_SIDE = TextureSlot.create("rope_side");
	private static final TextureSlot ROPE_TOP = TextureSlot.create("rope_top");

	public BlockStates(PackOutput output) {
		super(output, FarmersDelight.MODID);
	}

	@Override
	protected Stream<? extends Holder<Item>> getKnownItems() {
		return Stream.empty();
	}

	@Override
	protected Stream<? extends Holder<Block>> getKnownBlocks() {
		return Stream.concat(Stream.concat(standingSigns().stream().flatMap(pair -> Stream.of(pair.sign(), pair.wallSign())),
						hangingSigns().stream().flatMap(pair -> Stream.of(pair.sign(), pair.wallSign()))),
				generatedBlocks().stream())
				.map(Block::builtInRegistryHolder);
	}

	@Override
	protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
		registerSimpleBlocks(blockModels);
		standingSigns().forEach(pair -> registerStandingSign(blockModels, pair));
		hangingSigns().forEach(pair -> registerHangingSign(blockModels, pair));
	}

	@Override
	public String getName() {
		return "Block Model Definitions - " + FarmersDelight.MODID;
	}

	private static void registerSimpleBlocks(BlockModelGenerators blockModels) {
		registerSimpleExistingBlock(blockModels, ModBlocks.SAFETY_NET.get());
		registerSimpleExistingBlock(blockModels, ModBlocks.CANVAS_RUG.get());
		registerRiceBag(blockModels);
		registerBasket(blockModels, ModBlocks.WOODEN_BASKET.get());
		registerBasket(blockModels, ModBlocks.BAMBOO_BASKET.get());
		registerDirectionalBottomTop(blockModels, ModBlocks.RICE_BALE.get());
		registerRope(blockModels);
		registerRopeFence(blockModels);
		registerRopeFenceGate(blockModels);
		registerTatami(blockModels);
		registerFullTatamiMat(blockModels);
		registerCuttingBoard(blockModels);
		registerHalfTatami(blockModels);
		registerCrate(blockModels, ModBlocks.CARROT_CRATE.get(), "carrot");
		registerCrate(blockModels, ModBlocks.POTATO_CRATE.get(), "potato");
		registerCrate(blockModels, ModBlocks.BEETROOT_CRATE.get(), "beetroot");
		registerCrate(blockModels, ModBlocks.CABBAGE_CRATE.get(), "cabbage");
		registerCrate(blockModels, ModBlocks.TOMATO_CRATE.get(), "tomato");
		registerCrate(blockModels, ModBlocks.ONION_CRATE.get(), "onion");
		registerStrawBale(blockModels);
		registerRichSoil(blockModels);
		registerRichSoilFarmland(blockModels);
		registerOrganicCompost(blockModels);
		registerCabinet(blockModels, ModBlocks.OAK_CABINET.get(), "oak");
		registerCabinet(blockModels, ModBlocks.BIRCH_CABINET.get(), "birch");
		registerCabinet(blockModels, ModBlocks.SPRUCE_CABINET.get(), "spruce");
		registerCabinet(blockModels, ModBlocks.JUNGLE_CABINET.get(), "jungle");
		registerCabinet(blockModels, ModBlocks.ACACIA_CABINET.get(), "acacia");
		registerCabinet(blockModels, ModBlocks.DARK_OAK_CABINET.get(), "dark_oak");
		registerCabinet(blockModels, ModBlocks.MANGROVE_CABINET.get(), "mangrove");
		registerCabinet(blockModels, ModBlocks.CHERRY_CABINET.get(), "cherry");
		registerCabinet(blockModels, ModBlocks.BAMBOO_CABINET.get(), "bamboo");
		registerCabinet(blockModels, ModBlocks.CRIMSON_CABINET.get(), "crimson");
		registerCabinet(blockModels, ModBlocks.WARPED_CABINET.get(), "warped");
		registerCookingPot(blockModels);
		registerSkillet(blockModels);
		registerStove(blockModels);
		registerPie(blockModels, ModBlocks.APPLE_PIE.get());
		registerCustomPie(blockModels, ModBlocks.CHOCOLATE_PIE.get());
		registerPie(blockModels, ModBlocks.SWEET_BERRY_CHEESECAKE.get());
		registerPie(blockModels, ModBlocks.PUMPKIN_PIE.get());
		registerFeast(blockModels, (FeastBlock) ModBlocks.STUFFED_PUMPKIN_BLOCK.get());
		registerFeast(blockModels, (FeastBlock) ModBlocks.ROAST_CHICKEN_BLOCK.get());
		registerFeast(blockModels, (FeastBlock) ModBlocks.HONEY_GLAZED_HAM_BLOCK.get());
		registerFeast(blockModels, (FeastBlock) ModBlocks.SHEPHERDS_PIE_BLOCK.get());
		registerFeast(blockModels, (FeastBlock) ModBlocks.GLEAMING_SALAD_BLOCK.get());
		registerFeast(blockModels, (FeastBlock) ModBlocks.RICE_ROLL_MEDLEY_BLOCK.get());
		registerStageBlock(blockModels, ModBlocks.BROWN_MUSHROOM_COLONY.get(), MushroomColonyBlock.COLONY_AGE);
		registerStageBlock(blockModels, ModBlocks.RED_MUSHROOM_COLONY.get(), MushroomColonyBlock.COLONY_AGE);
		registerCustomStageBlock(blockModels, ModBlocks.CABBAGE_CROP.get(), cutoutTemplate(blockId("template_crop_cross"), TextureSlot.CROSS), TextureSlot.CROSS, CabbageBlock.AGE);
		registerCustomStageBlock(blockModels, ModBlocks.ONION_CROP.get(), cutoutTemplate(Identifier.withDefaultNamespace("block/crop"), TextureSlot.CROP), TextureSlot.CROP, OnionBlock.AGE, 0, 0, 1, 1, 2, 2, 2, 3);
		registerCustomStageBlock(blockModels, ModBlocks.BUDDING_TOMATO_CROP.get(), cutoutTemplate(blockId("template_crop_cross"), TextureSlot.CROSS), TextureSlot.CROSS, BuddingBushBlock.AGE, 0, 1, 2, 3, 3);
		registerTomato(blockModels);
		registerRopedTomato(blockModels);
		registerRiceRoot(blockModels);
		registerStageBlock(blockModels, ModBlocks.RICE_CROP_PANICLES.get(), RicePaniclesBlock.RICE_AGE);
		registerWildCrop(blockModels, ModBlocks.SANDY_SHRUB.get());
		registerWildCrop(blockModels, ModBlocks.WILD_BEETROOTS.get());
		registerWildCrop(blockModels, ModBlocks.WILD_CABBAGES.get());
		registerWildCrop(blockModels, ModBlocks.WILD_POTATOES.get());
		registerWildCrop(blockModels, ModBlocks.WILD_TOMATOES.get());
		registerWildCrop(blockModels, ModBlocks.WILD_CARROTS.get());
		registerWildCrop(blockModels, ModBlocks.WILD_ONIONS.get());
		registerDoublePlant(blockModels, ModBlocks.WILD_RICE.get());
	}

	private static void registerSimpleExistingBlock(BlockModelGenerators blockModels, Block block) {
		blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(block))));
	}

	private static void registerRiceBag(BlockModelGenerators blockModels) {
		Block block = ModBlocks.RICE_BAG.get();
		TextureMapping mapping = new TextureMapping()
				.put(TextureSlot.PARTICLE, blockMaterial("rice_bag_top"))
				.put(TextureSlot.DOWN, blockMaterial("rice_bag_bottom"))
				.put(TextureSlot.UP, blockMaterial("rice_bag_top"))
				.put(TextureSlot.NORTH, blockMaterial("rice_bag_side_tied"))
				.put(TextureSlot.SOUTH, blockMaterial("rice_bag_side_tied"))
				.put(TextureSlot.EAST, blockMaterial("rice_bag_side"))
				.put(TextureSlot.WEST, blockMaterial("rice_bag_side"));
		Identifier model = ModelTemplates.CUBE.create(block, mapping, blockModels.modelOutput);
		blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(model)));
	}

	private static void registerBasket(BlockModelGenerators blockModels, Block block) {
		String name = blockName(block);
		TextureMapping mapping = new TextureMapping()
				.put(TextureSlot.BOTTOM, blockMaterial(name + "_bottom"))
				.put(HANDLE, blockMaterial(name + "_handle"))
				.put(TextureSlot.SIDE, blockMaterial(name + "_side"))
				.put(TextureSlot.TOP, blockMaterial(name + "_top"));
		Identifier model = blockTemplate("template_basket", TextureSlot.BOTTOM, HANDLE, TextureSlot.SIDE, TextureSlot.TOP)
				.create(block, mapping, blockModels.modelOutput);
		MultiVariant variant = BlockModelGenerators.plainVariant(model);
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, variant)
				.with(PropertyDispatch.modify(BasketBlock.FACING)
						.select(Direction.DOWN, BlockModelGenerators.X_ROT_180)
						.select(Direction.UP, BlockModelGenerators.NOP)
						.select(Direction.NORTH, BlockModelGenerators.X_ROT_90)
						.select(Direction.SOUTH, BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_180))
						.select(Direction.WEST, BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_270))
						.select(Direction.EAST, BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_90))));
	}

	private static void registerDirectionalBottomTop(BlockModelGenerators blockModels, Block block) {
		Identifier model = ModelTemplates.CUBE_BOTTOM_TOP.create(block, cubeBottomTop(blockName(block)), blockModels.modelOutput);
		MultiVariant variant = BlockModelGenerators.plainVariant(model);
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, variant)
				.with(PropertyDispatch.modify(RiceBaleBlock.FACING)
						.select(Direction.DOWN, BlockModelGenerators.X_ROT_180)
						.select(Direction.UP, BlockModelGenerators.NOP)
						.select(Direction.NORTH, BlockModelGenerators.X_ROT_90)
						.select(Direction.SOUTH, BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_180))
						.select(Direction.WEST, BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_270))
						.select(Direction.EAST, BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_90))));
	}

	private static void registerRope(BlockModelGenerators blockModels) {
		Block block = ModBlocks.ROPE.get();
		blockModels.blockStateOutput.accept(MultiPartGenerator.multiPart(block)
				.with(BlockModelGenerators.plainVariant(blockId("rope_post")))
				.with(BlockModelGenerators.condition(RopeBlock.TIED_TO_BELL, true), BlockModelGenerators.plainVariant(blockId("rope_bell_tie")))
				.with(BlockModelGenerators.condition(CrossCollisionBlock.NORTH, true), BlockModelGenerators.plainVariant(blockId("rope_side")))
				.with(BlockModelGenerators.condition(CrossCollisionBlock.EAST, true), BlockModelGenerators.plainVariant(blockId("rope_side")).with(BlockModelGenerators.Y_ROT_90))
				.with(BlockModelGenerators.condition(CrossCollisionBlock.SOUTH, true), BlockModelGenerators.plainVariant(blockId("rope_side_alt")))
				.with(BlockModelGenerators.condition(CrossCollisionBlock.WEST, true), BlockModelGenerators.plainVariant(blockId("rope_side_alt")).with(BlockModelGenerators.Y_ROT_90)));
	}

	private static void registerRopeFence(BlockModelGenerators blockModels) {
		Block block = ModBlocks.ROPE_FENCE.get();
		blockModels.blockStateOutput.accept(MultiPartGenerator.multiPart(block)
				.with(BlockModelGenerators.plainVariant(blockId("rope_fence_post")))
				.with(BlockModelGenerators.condition(CrossCollisionBlock.NORTH, true), BlockModelGenerators.plainVariant(blockId("rope_fence_side")))
				.with(BlockModelGenerators.condition(CrossCollisionBlock.EAST, true), BlockModelGenerators.plainVariant(blockId("rope_fence_side")).with(BlockModelGenerators.Y_ROT_90))
				.with(BlockModelGenerators.condition(CrossCollisionBlock.SOUTH, true), BlockModelGenerators.plainVariant(blockId("rope_fence_side_alt")))
				.with(BlockModelGenerators.condition(CrossCollisionBlock.WEST, true), BlockModelGenerators.plainVariant(blockId("rope_fence_side_alt")).with(BlockModelGenerators.Y_ROT_90)));
	}

	private static void registerRopeFenceGate(BlockModelGenerators blockModels) {
		Block block = ModBlocks.ROPE_FENCE_GATE.get();
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(FenceGateBlock.FACING, FenceGateBlock.IN_WALL, FenceGateBlock.OPEN)
						.generate((facing, inWall, open) -> BlockModelGenerators.plainVariant(blockId("rope_fence_gate" + (inWall ? "_wall" : "") + (open ? "_open" : "")))
								.with(fenceGateRotation(facing)))));
	}

	private static void registerTatami(BlockModelGenerators blockModels) {
		Block block = ModBlocks.TATAMI.get();
		ModelTemplates.CUBE_ALL.create(blockId("tatami_half"), TextureMapping.cube(blockMaterial("tatami_mat_half")), blockModels.modelOutput);
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(TatamiBlock.FACING, TatamiBlock.PAIRED)
						.generate((facing, paired) -> BlockModelGenerators.plainVariant(blockId(tatamiModel(facing, paired))).with(directionRotation(facing)))));
	}

	private static String tatamiModel(Direction facing, boolean paired) {
		if (!paired) {
			return "tatami_half";
		}
		return facing.getAxisDirection() == Direction.AxisDirection.POSITIVE ? "tatami_odd" : "tatami_even";
	}

	private static void registerFullTatamiMat(BlockModelGenerators blockModels) {
		Block block = ModBlocks.FULL_TATAMI_MAT.get();
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(TatamiMatBlock.FACING, TatamiMatBlock.PART)
						.generate((facing, part) -> BlockModelGenerators.plainVariant(blockId(part == BedPart.HEAD ? "tatami_mat_head" : "tatami_mat_foot"))
								.with(fenceGateRotation(facing)))));
	}

	private static void registerCuttingBoard(BlockModelGenerators blockModels) {
		Block block = ModBlocks.CUTTING_BOARD.get();
		MultiVariant variant = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(block));
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, variant)
				.with(PropertyDispatch.modify(CuttingBoardBlock.FACING)
						.select(Direction.EAST, BlockModelGenerators.Y_ROT_90)
						.select(Direction.SOUTH, BlockModelGenerators.Y_ROT_180)
						.select(Direction.WEST, BlockModelGenerators.Y_ROT_270)
						.select(Direction.NORTH, BlockModelGenerators.NOP)));
	}

	private static void registerHalfTatami(BlockModelGenerators blockModels) {
		Block block = ModBlocks.HALF_TATAMI_MAT.get();
		MultiVariant variant = BlockModelGenerators.plainVariant(blockId("tatami_mat_half"));
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, variant)
				.with(PropertyDispatch.modify(TatamiHalfMatBlock.FACING)
						.select(Direction.EAST, BlockModelGenerators.Y_ROT_90)
						.select(Direction.SOUTH, BlockModelGenerators.Y_ROT_180)
						.select(Direction.WEST, BlockModelGenerators.Y_ROT_270)
						.select(Direction.NORTH, BlockModelGenerators.NOP)));
	}

	private static void registerCrate(BlockModelGenerators blockModels, Block block, String cropName) {
		TextureMapping mapping = new TextureMapping()
				.put(TextureSlot.BOTTOM, blockMaterial("crate_bottom"))
				.put(TextureSlot.SIDE, blockMaterial(cropName + "_crate_side"))
				.put(TextureSlot.TOP, blockMaterial(cropName + "_crate_top"));
		Identifier model = ModelTemplates.CUBE_BOTTOM_TOP.create(block, mapping, blockModels.modelOutput);
		blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(model)));
	}

	private static void registerStrawBale(BlockModelGenerators blockModels) {
		Block block = ModBlocks.STRAW_BALE.get();
		TextureMapping mapping = new TextureMapping()
				.put(TextureSlot.END, blockMaterial("straw_bale_end"))
				.put(TextureSlot.SIDE, blockMaterial("straw_bale_side"));
		MultiVariant model = BlockModelGenerators.plainVariant(ModelTemplates.CUBE_COLUMN.create(block, mapping, blockModels.modelOutput));
		MultiVariant horizontalModel = BlockModelGenerators.plainVariant(ModelTemplates.CUBE_COLUMN_HORIZONTAL.create(block, mapping, blockModels.modelOutput));
		blockModels.blockStateOutput.accept(BlockModelGenerators.createRotatedPillarWithHorizontalVariant(block, model, horizontalModel));
	}

	private static void registerRichSoil(BlockModelGenerators blockModels) {
		Block block = ModBlocks.RICH_SOIL.get();
		Identifier model = ModelTemplates.CUBE_ALL.create(block, TextureMapping.cube(blockMaterial(blockName(block))), blockModels.modelOutput);
		blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, randomRotatedVariant(model)));
	}

	private static void registerRichSoilFarmland(BlockModelGenerators blockModels) {
		Block block = ModBlocks.RICH_SOIL_FARMLAND.get();
		Identifier dryModel = farmlandModel(blockModels, false);
		Identifier moistModel = farmlandModel(blockModels, true);
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(RichSoilFarmlandBlock.MOISTURE)
						.generate(moisture -> BlockModelGenerators.plainVariant(moisture == 7 ? moistModel : dryModel))));
	}

	private static Identifier farmlandModel(BlockModelGenerators blockModels, boolean moist) {
		String name = blockName(ModBlocks.RICH_SOIL_FARMLAND.get());
		String suffix = moist ? "_moist" : "";
		TextureMapping mapping = new TextureMapping()
				.put(TextureSlot.BOTTOM, blockMaterial("rich_soil"))
				.put(TextureSlot.SIDE, blockMaterial(moist ? name + suffix + "_side" : "rich_soil"))
				.put(TextureSlot.TOP, blockMaterial(name + suffix));
		return blockTemplate("template_farmland_custom", TextureSlot.BOTTOM, TextureSlot.SIDE, TextureSlot.TOP)
				.create(blockId(name + suffix), mapping, blockModels.modelOutput);
	}

	private static void registerOrganicCompost(BlockModelGenerators blockModels) {
		Block block = ModBlocks.ORGANIC_COMPOST.get();
		Identifier[] models = new Identifier[4];
		for (int stage = 0; stage < models.length; stage++) {
			String stageName = blockName(block) + "_stage" + stage;
			models[stage] = ModelTemplates.CUBE_ALL.create(blockId(stageName), TextureMapping.cube(blockMaterial(stageName)), blockModels.modelOutput);
		}

		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(OrganicCompostBlock.COMPOSTING)
						.generate(composting -> randomRotatedVariant(models[composting / 2]))));
	}

	private static void registerCabinet(BlockModelGenerators blockModels, Block block, String woodType) {
		Identifier closedModel = cabinetModel(blockModels, block, woodType, false);
		Identifier openModel = cabinetModel(blockModels, block, woodType, true);
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(CabinetBlock.FACING, CabinetBlock.OPEN)
						.generate((facing, open) -> BlockModelGenerators.plainVariant(open ? openModel : closedModel).with(horizontalRotation(facing)))));
	}

	private static Identifier cabinetModel(BlockModelGenerators blockModels, Block block, String woodType, boolean open) {
		String suffix = open ? "_open" : "";
		TextureMapping mapping = new TextureMapping()
				.put(TextureSlot.FRONT, blockMaterial(woodType + "_cabinet_front" + suffix))
				.put(TextureSlot.SIDE, blockMaterial(woodType + "_cabinet_side"))
				.put(TextureSlot.TOP, blockMaterial(woodType + "_cabinet_top"));
		return ModelTemplates.CUBE_ORIENTABLE.create(blockId(blockName(block) + suffix), mapping, blockModels.modelOutput);
	}

	private static void registerCookingPot(BlockModelGenerators blockModels) {
		Block block = ModBlocks.COOKING_POT.get();
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(CookingPotBlock.FACING, CookingPotBlock.SUPPORT)
						.generate((facing, support) -> BlockModelGenerators.plainVariant(blockId(blockName(block) + cookingPotSupportSuffix(support))).with(horizontalRotation(facing)))));
	}

	private static String cookingPotSupportSuffix(CookingPotSupport support) {
		return switch (support) {
			case NONE -> "";
			case TRAY -> "_tray";
			case HANDLE -> "_handle";
		};
	}

	private static void registerSkillet(BlockModelGenerators blockModels) {
		Block block = ModBlocks.SKILLET.get();
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(SkilletBlock.FACING, SkilletBlock.SUPPORT)
						.generate((facing, support) -> BlockModelGenerators.plainVariant(blockId(blockName(block) + (support ? "_tray" : ""))).with(horizontalRotation(facing)))));
	}

	private static void registerStove(BlockModelGenerators blockModels) {
		Block block = ModBlocks.STOVE.get();
		Identifier offModel = stoveModel(blockModels, false);
		Identifier onModel = stoveModel(blockModels, true);
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(AbstractStoveBlock.FACING, AbstractStoveBlock.LIT)
						.generate((facing, lit) -> BlockModelGenerators.plainVariant(lit ? onModel : offModel).with(horizontalRotation(facing)))));
	}

	private static Identifier stoveModel(BlockModelGenerators blockModels, boolean lit) {
		String suffix = lit ? "_on" : "";
		TextureMapping mapping = new TextureMapping()
				.put(TextureSlot.BOTTOM, blockMaterial("stove_bottom"))
				.put(TextureSlot.FRONT, blockMaterial("stove_front" + suffix))
				.put(TextureSlot.SIDE, blockMaterial("stove_side"))
				.put(TextureSlot.TOP, blockMaterial("stove_top" + suffix));
		return ModelTemplates.CUBE_ORIENTABLE_TOP_BOTTOM.create(blockId("stove" + suffix), mapping, blockModels.modelOutput);
	}

	private static void registerPie(BlockModelGenerators blockModels, Block block) {
		Identifier wholeModel = pieModel(blockModels, block);
		Identifier slice1 = pieSliceModel(blockModels, block, 1);
		Identifier slice2 = pieSliceModel(blockModels, block, 2);
		Identifier slice3 = pieSliceModel(blockModels, block, 3);
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(PieBlock.BITES, PieBlock.FACING)
						.generate((bites, facing) -> BlockModelGenerators.plainVariant(switch (bites) {
							case 1 -> slice1;
							case 2 -> slice2;
							case 3 -> slice3;
							default -> wholeModel;
						}).with(horizontalRotation(facing)))));
	}

	private static void registerCustomPie(BlockModelGenerators blockModels, Block block) {
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(PieBlock.BITES, PieBlock.FACING)
						.generate((bites, facing) -> BlockModelGenerators.plainVariant(blockId(blockName(block) + (bites > 0 ? "_slice" + bites : "")))
								.with(horizontalRotation(facing)))));
	}

	private static Identifier pieModel(BlockModelGenerators blockModels, Block block) {
		String name = blockName(block);
		TextureMapping mapping = new TextureMapping()
				.put(TextureSlot.BOTTOM, blockMaterial("pie_bottom"))
				.put(TextureSlot.SIDE, blockMaterial("pie_side"))
				.put(TextureSlot.TOP, blockMaterial(name + "_top"));
		return blockTemplate("template_pie", TextureSlot.BOTTOM, TextureSlot.SIDE, TextureSlot.TOP)
				.create(block, mapping, blockModels.modelOutput);
	}

	private static Identifier pieSliceModel(BlockModelGenerators blockModels, Block block, int bites) {
		String name = blockName(block);
		TextureMapping mapping = new TextureMapping()
				.put(TextureSlot.BOTTOM, blockMaterial("pie_bottom"))
				.put(TextureSlot.SIDE, blockMaterial("pie_side"))
				.put(INNER, blockMaterial(name + "_inner"))
				.put(TextureSlot.TOP, blockMaterial(name + "_top"));
		return blockTemplate("template_pie_slice" + bites, TextureSlot.BOTTOM, INNER, TextureSlot.SIDE, TextureSlot.TOP)
				.create(blockId(name + "_slice" + bites), mapping, blockModels.modelOutput);
	}

	private static void registerFeast(BlockModelGenerators blockModels, FeastBlock block) {
		IntegerProperty servingsProperty = block.getServingsProperty();
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(FeastBlock.FACING, servingsProperty)
						.generate((facing, servings) -> BlockModelGenerators.plainVariant(blockId(blockName(block) + feastSuffix(block, servingsProperty, servings)))
								.with(horizontalRotation(facing)))));
	}

	private static String feastSuffix(FeastBlock block, IntegerProperty servingsProperty, int servings) {
		if (servings == 0) {
			return block.hasLeftovers ? "_leftovers" : "_stage" + (servingsProperty.getPossibleValues().size() - 2);
		}
		return "_stage" + (block.getMaxServings() - servings);
	}

	private static void registerStageBlock(BlockModelGenerators blockModels, Block block, IntegerProperty ageProperty) {
		registerCustomStageBlock(blockModels, block, cutoutTemplate(Identifier.withDefaultNamespace("block/cross"), TextureSlot.CROSS), TextureSlot.CROSS, ageProperty);
	}

	private static void registerCustomStageBlock(BlockModelGenerators blockModels, Block block, ModelTemplate template, TextureSlot textureSlot, IntegerProperty ageProperty, int... ageSuffixes) {
		Map<Integer, Identifier> models = new java.util.HashMap<>();
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(ageProperty)
						.generate(age -> BlockModelGenerators.plainVariant(models.computeIfAbsent(ageSuffix(age, ageSuffixes), stage -> stageModel(blockModels, block, template, textureSlot, stage))))));
	}

	private static int ageSuffix(int age, int... ageSuffixes) {
		return ageSuffixes.length > age ? ageSuffixes[age] : age;
	}

	private static Identifier stageModel(BlockModelGenerators blockModels, Block block, ModelTemplate template, TextureSlot textureSlot, int stage) {
		String stageName = blockName(block) + "_stage" + stage;
		return template.create(blockId(stageName), new TextureMapping().put(textureSlot, blockMaterial(stageName)), blockModels.modelOutput);
	}

	private static void registerTomato(BlockModelGenerators blockModels) {
		Block block = ModBlocks.TOMATO_CROP.get();
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(TomatoBlock.VINE_AGE, TomatoBlock.ROPELOGGED)
						.generate((age, ropelogged) -> BlockModelGenerators.plainVariant(ropelogged ? tomatoRopeModel(blockModels, age, true) : tomatoCropModel(blockModels, age)))));
	}

	private static Identifier tomatoCropModel(BlockModelGenerators blockModels, int age) {
		String stageName = blockName(ModBlocks.TOMATO_CROP.get()) + "_stage" + age;
		return cutoutTemplate(blockId("template_crop_cross"), TextureSlot.CROSS)
				.create(blockId(stageName), new TextureMapping().put(TextureSlot.CROSS, blockMaterial(stageName)), blockModels.modelOutput);
	}

	private static Identifier tomatoRopeModel(BlockModelGenerators blockModels, int age, boolean old) {
		String stageName = blockName(ModBlocks.TOMATO_CROP.get()) + (old ? "_old" : "_on_rope") + "_stage" + age;
		return cutoutTemplate(blockId("template_crop_with_rope"), TextureSlot.CROP, ROPE_SIDE, ROPE_TOP)
				.create(blockId(stageName), tomatoRopeMapping(stageName), blockModels.modelOutput);
	}

	private static TextureMapping tomatoRopeMapping(String stageName) {
		return new TextureMapping()
				.put(TextureSlot.CROP, blockMaterial(stageName))
				.put(ROPE_SIDE, blockMaterial("tomatoes_coiled_rope"))
				.put(ROPE_TOP, blockMaterial("rope_top"));
	}

	private static void registerRopedTomato(BlockModelGenerators blockModels) {
		Block block = ModBlocks.TOMATO_CROP_ON_ROPE.get();
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(TomatoBlock.VINE_AGE)
						.generate(age -> BlockModelGenerators.plainVariant(tomatoRopeModel(blockModels, age, false)))));
	}

	private static void registerRiceRoot(BlockModelGenerators blockModels) {
		Block block = ModBlocks.RICE_CROP.get();
		ModelTemplate template = cutoutTemplate(Identifier.withDefaultNamespace("block/cross"), TextureSlot.CROSS);
		Map<String, Identifier> models = new java.util.HashMap<>();
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(RiceBlock.AGE, RiceBlock.SUPPORTING)
						.generate((age, supporting) -> {
							String modelName = supporting && age == 3 ? blockName(block) + "_supporting" : blockName(block) + "_stage" + age;
							Identifier model = models.computeIfAbsent(modelName, name -> template.create(blockId(name), new TextureMapping().put(TextureSlot.CROSS, blockMaterial(name)), blockModels.modelOutput));
							return BlockModelGenerators.plainVariant(model);
						})));
	}

	private static void registerWildCrop(BlockModelGenerators blockModels, Block block) {
		Identifier model = cutoutTemplate(Identifier.withDefaultNamespace("block/cross"), TextureSlot.CROSS)
				.create(block, new TextureMapping().put(TextureSlot.CROSS, blockMaterial(blockName(block))), blockModels.modelOutput);
		blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.plainVariant(model)));
	}

	private static void registerDoublePlant(BlockModelGenerators blockModels, Block block) {
		ModelTemplate template = cutoutTemplate(Identifier.withDefaultNamespace("block/cross"), TextureSlot.CROSS);
		Identifier lowerModel = template.create(blockId(blockName(block) + "_bottom"), new TextureMapping().put(TextureSlot.CROSS, blockMaterial(blockName(block) + "_bottom")), blockModels.modelOutput);
		Identifier upperModel = template.create(blockId(blockName(block) + "_top"), new TextureMapping().put(TextureSlot.CROSS, blockMaterial(blockName(block) + "_top")), blockModels.modelOutput);
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
				.with(PropertyDispatch.initial(DoublePlantBlock.HALF)
						.generate(half -> BlockModelGenerators.plainVariant(half == DoubleBlockHalf.UPPER ? upperModel : lowerModel))));
	}

	private static void registerStandingSign(BlockModelGenerators blockModels, SignPair pair) {
		TextureMapping mapping = signMapping(standingSignTexture(pair.texture()), Identifier.withDefaultNamespace("block/spruce_planks"));
		MultiVariant rot0 = BlockModelGenerators.plainVariant(ModelTemplates.SIGN_ROT_0.create(ModelLocationUtils.getModelLocation(pair.sign(), "_rot_0"), mapping, blockModels.modelOutput));
		MultiVariant rot1 = BlockModelGenerators.plainVariant(ModelTemplates.SIGN_ROT_1.create(ModelLocationUtils.getModelLocation(pair.sign(), "_rot_1"), mapping, blockModels.modelOutput));
		MultiVariant rot2 = BlockModelGenerators.plainVariant(ModelTemplates.SIGN_ROT_2.create(ModelLocationUtils.getModelLocation(pair.sign(), "_rot_2"), mapping, blockModels.modelOutput));
		MultiVariant rot3 = BlockModelGenerators.plainVariant(ModelTemplates.SIGN_ROT_3.create(ModelLocationUtils.getModelLocation(pair.sign(), "_rot_3"), mapping, blockModels.modelOutput));

		blockModels.blockStateOutput.accept(BlockModelGenerators.createSign(pair.sign(), rot0, rot1, rot2, rot3));

		MultiVariant wallModel = BlockModelGenerators.plainVariant(ModelTemplates.WALL_SIGN.create(pair.wallSign(), mapping, blockModels.modelOutput));
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(pair.wallSign(), wallModel).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING_ALT));
	}

	private static void registerHangingSign(BlockModelGenerators blockModels, SignPair pair) {
		TextureMapping mapping = signMapping(hangingSignTexture(pair.texture()), Identifier.withDefaultNamespace("block/stripped_spruce_log"));
		MultiVariant rot0 = BlockModelGenerators.plainVariant(ModelTemplates.HANGING_SIGN_ROT_0.create(ModelLocationUtils.getModelLocation(pair.sign(), "_rot_0"), mapping, blockModels.modelOutput));
		MultiVariant rot1 = BlockModelGenerators.plainVariant(ModelTemplates.HANGING_SIGN_ROT_1.create(ModelLocationUtils.getModelLocation(pair.sign(), "_rot_1"), mapping, blockModels.modelOutput));
		MultiVariant rot2 = BlockModelGenerators.plainVariant(ModelTemplates.HANGING_SIGN_ROT_2.create(ModelLocationUtils.getModelLocation(pair.sign(), "_rot_2"), mapping, blockModels.modelOutput));
		MultiVariant rot3 = BlockModelGenerators.plainVariant(ModelTemplates.HANGING_SIGN_ROT_3.create(ModelLocationUtils.getModelLocation(pair.sign(), "_rot_3"), mapping, blockModels.modelOutput));
		MultiVariant attachedRot0 = BlockModelGenerators.plainVariant(ModelTemplates.ATTACHED_HANGING_SIGN_ROT_0.create(ModelLocationUtils.getModelLocation(pair.sign(), "_attached_rot_0"), mapping, blockModels.modelOutput));
		MultiVariant attachedRot1 = BlockModelGenerators.plainVariant(ModelTemplates.ATTACHED_HANGING_SIGN_ROT_1.create(ModelLocationUtils.getModelLocation(pair.sign(), "_attached_rot_1"), mapping, blockModels.modelOutput));
		MultiVariant attachedRot2 = BlockModelGenerators.plainVariant(ModelTemplates.ATTACHED_HANGING_SIGN_ROT_2.create(ModelLocationUtils.getModelLocation(pair.sign(), "_attached_rot_2"), mapping, blockModels.modelOutput));
		MultiVariant attachedRot3 = BlockModelGenerators.plainVariant(ModelTemplates.ATTACHED_HANGING_SIGN_ROT_3.create(ModelLocationUtils.getModelLocation(pair.sign(), "_attached_rot_3"), mapping, blockModels.modelOutput));

		blockModels.blockStateOutput.accept(BlockModelGenerators.createHangingSign(pair.sign(), rot0, rot1, rot2, rot3, attachedRot0, attachedRot1, attachedRot2, attachedRot3));

		MultiVariant wallModel = BlockModelGenerators.plainVariant(ModelTemplates.WALL_HANGING_SIGN.create(pair.wallSign(), mapping, blockModels.modelOutput));
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(pair.wallSign(), wallModel).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING_ALT));
	}

	private static TextureMapping signMapping(String texture, Identifier particle) {
		return new TextureMapping()
				.put(TextureSlot.ALL, new Material(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, texture)))
				.put(TextureSlot.PARTICLE, new Material(particle));
	}

	private static String standingSignTexture(String texture) {
		return texture.equals("canvas") ? "block/canvas_sign" : "block/" + texture.substring("canvas_".length()) + "_canvas_sign";
	}

	private static String hangingSignTexture(String texture) {
		return texture.equals("canvas") ? "block/hanging_canvas_sign" : "block/" + texture.substring("canvas_".length()) + "_hanging_canvas_sign";
	}

	private static TextureMapping cubeBottomTop(String baseName) {
		return new TextureMapping()
				.put(TextureSlot.BOTTOM, blockMaterial(baseName + "_bottom"))
				.put(TextureSlot.SIDE, blockMaterial(baseName + "_side"))
				.put(TextureSlot.TOP, blockMaterial(baseName + "_top"));
	}

	private static MultiVariant randomRotatedVariant(Identifier model) {
		return BlockModelGenerators.variants(
				BlockModelGenerators.plainModel(model),
				BlockModelGenerators.plainModel(model).with(BlockModelGenerators.Y_ROT_90),
				BlockModelGenerators.plainModel(model).with(BlockModelGenerators.Y_ROT_180),
				BlockModelGenerators.plainModel(model).with(BlockModelGenerators.Y_ROT_270));
	}

	private static VariantMutator horizontalRotation(Direction facing) {
		return switch (facing) {
			case EAST -> BlockModelGenerators.Y_ROT_90;
			case SOUTH -> BlockModelGenerators.Y_ROT_180;
			case WEST -> BlockModelGenerators.Y_ROT_270;
			default -> BlockModelGenerators.NOP;
		};
	}

	private static VariantMutator fenceGateRotation(Direction facing) {
		return switch (facing) {
			case WEST -> BlockModelGenerators.Y_ROT_90;
			case NORTH -> BlockModelGenerators.Y_ROT_180;
			case EAST -> BlockModelGenerators.Y_ROT_270;
			default -> BlockModelGenerators.NOP;
		};
	}

	private static VariantMutator directionRotation(Direction facing) {
		return switch (facing) {
			case DOWN -> BlockModelGenerators.X_ROT_180;
			case NORTH -> BlockModelGenerators.X_ROT_90;
			case SOUTH -> BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_180);
			case WEST -> BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_270);
			case EAST -> BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_90);
			default -> BlockModelGenerators.NOP;
		};
	}

	private static ModelTemplate blockTemplate(String parent, TextureSlot... slots) {
		return new ModelTemplate(Optional.of(blockId(parent)), Optional.empty(), slots);
	}

	private static ModelTemplate cutoutTemplate(Identifier parent, TextureSlot... slots) {
		return new RenderTypeModelTemplate(parent, "minecraft:cutout", slots);
	}

	private static Material blockMaterial(String path) {
		return new Material(blockId(path));
	}

	private static Identifier blockId(String path) {
		return Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "block/" + path);
	}

	private static String blockName(Block block) {
		return ModelLocationUtils.getModelLocation(block).getPath().replace("block/", "");
	}

	private static List<Block> generatedBlocks() {
		return List.of(
				ModBlocks.SAFETY_NET.get(),
				ModBlocks.CANVAS_RUG.get(),
				ModBlocks.RICE_BAG.get(),
				ModBlocks.WOODEN_BASKET.get(),
				ModBlocks.BAMBOO_BASKET.get(),
				ModBlocks.RICE_BALE.get(),
				ModBlocks.ROPE.get(),
				ModBlocks.ROPE_FENCE.get(),
				ModBlocks.ROPE_FENCE_GATE.get(),
				ModBlocks.TATAMI.get(),
				ModBlocks.FULL_TATAMI_MAT.get(),
				ModBlocks.CUTTING_BOARD.get(),
				ModBlocks.HALF_TATAMI_MAT.get(),
				ModBlocks.CARROT_CRATE.get(),
				ModBlocks.POTATO_CRATE.get(),
				ModBlocks.BEETROOT_CRATE.get(),
				ModBlocks.CABBAGE_CRATE.get(),
				ModBlocks.TOMATO_CRATE.get(),
				ModBlocks.ONION_CRATE.get(),
				ModBlocks.STRAW_BALE.get(),
				ModBlocks.RICH_SOIL.get(),
				ModBlocks.RICH_SOIL_FARMLAND.get(),
				ModBlocks.ORGANIC_COMPOST.get(),
				ModBlocks.OAK_CABINET.get(),
				ModBlocks.BIRCH_CABINET.get(),
				ModBlocks.SPRUCE_CABINET.get(),
				ModBlocks.JUNGLE_CABINET.get(),
				ModBlocks.ACACIA_CABINET.get(),
				ModBlocks.DARK_OAK_CABINET.get(),
				ModBlocks.MANGROVE_CABINET.get(),
				ModBlocks.CHERRY_CABINET.get(),
				ModBlocks.BAMBOO_CABINET.get(),
				ModBlocks.CRIMSON_CABINET.get(),
				ModBlocks.WARPED_CABINET.get(),
				ModBlocks.COOKING_POT.get(),
				ModBlocks.SKILLET.get(),
				ModBlocks.STOVE.get(),
				ModBlocks.APPLE_PIE.get(),
				ModBlocks.CHOCOLATE_PIE.get(),
				ModBlocks.SWEET_BERRY_CHEESECAKE.get(),
				ModBlocks.PUMPKIN_PIE.get(),
				ModBlocks.STUFFED_PUMPKIN_BLOCK.get(),
				ModBlocks.ROAST_CHICKEN_BLOCK.get(),
				ModBlocks.HONEY_GLAZED_HAM_BLOCK.get(),
				ModBlocks.SHEPHERDS_PIE_BLOCK.get(),
				ModBlocks.GLEAMING_SALAD_BLOCK.get(),
				ModBlocks.RICE_ROLL_MEDLEY_BLOCK.get(),
				ModBlocks.BROWN_MUSHROOM_COLONY.get(),
				ModBlocks.RED_MUSHROOM_COLONY.get(),
				ModBlocks.CABBAGE_CROP.get(),
				ModBlocks.ONION_CROP.get(),
				ModBlocks.BUDDING_TOMATO_CROP.get(),
				ModBlocks.TOMATO_CROP.get(),
				ModBlocks.TOMATO_CROP_ON_ROPE.get(),
				ModBlocks.RICE_CROP.get(),
				ModBlocks.RICE_CROP_PANICLES.get(),
				ModBlocks.SANDY_SHRUB.get(),
				ModBlocks.WILD_BEETROOTS.get(),
				ModBlocks.WILD_CABBAGES.get(),
				ModBlocks.WILD_POTATOES.get(),
				ModBlocks.WILD_TOMATOES.get(),
				ModBlocks.WILD_CARROTS.get(),
				ModBlocks.WILD_ONIONS.get(),
				ModBlocks.WILD_RICE.get()
		);
	}

	private static List<SignPair> standingSigns() {
		return List.of(
				new SignPair(ModBlocks.CANVAS_SIGN.get(), ModBlocks.CANVAS_WALL_SIGN.get(), "canvas"),
				new SignPair(ModBlocks.WHITE_CANVAS_SIGN.get(), ModBlocks.WHITE_CANVAS_WALL_SIGN.get(), "canvas_white"),
				new SignPair(ModBlocks.ORANGE_CANVAS_SIGN.get(), ModBlocks.ORANGE_CANVAS_WALL_SIGN.get(), "canvas_orange"),
				new SignPair(ModBlocks.MAGENTA_CANVAS_SIGN.get(), ModBlocks.MAGENTA_CANVAS_WALL_SIGN.get(), "canvas_magenta"),
				new SignPair(ModBlocks.LIGHT_BLUE_CANVAS_SIGN.get(), ModBlocks.LIGHT_BLUE_CANVAS_WALL_SIGN.get(), "canvas_light_blue"),
				new SignPair(ModBlocks.YELLOW_CANVAS_SIGN.get(), ModBlocks.YELLOW_CANVAS_WALL_SIGN.get(), "canvas_yellow"),
				new SignPair(ModBlocks.LIME_CANVAS_SIGN.get(), ModBlocks.LIME_CANVAS_WALL_SIGN.get(), "canvas_lime"),
				new SignPair(ModBlocks.PINK_CANVAS_SIGN.get(), ModBlocks.PINK_CANVAS_WALL_SIGN.get(), "canvas_pink"),
				new SignPair(ModBlocks.GRAY_CANVAS_SIGN.get(), ModBlocks.GRAY_CANVAS_WALL_SIGN.get(), "canvas_gray"),
				new SignPair(ModBlocks.LIGHT_GRAY_CANVAS_SIGN.get(), ModBlocks.LIGHT_GRAY_CANVAS_WALL_SIGN.get(), "canvas_light_gray"),
				new SignPair(ModBlocks.CYAN_CANVAS_SIGN.get(), ModBlocks.CYAN_CANVAS_WALL_SIGN.get(), "canvas_cyan"),
				new SignPair(ModBlocks.PURPLE_CANVAS_SIGN.get(), ModBlocks.PURPLE_CANVAS_WALL_SIGN.get(), "canvas_purple"),
				new SignPair(ModBlocks.BLUE_CANVAS_SIGN.get(), ModBlocks.BLUE_CANVAS_WALL_SIGN.get(), "canvas_blue"),
				new SignPair(ModBlocks.BROWN_CANVAS_SIGN.get(), ModBlocks.BROWN_CANVAS_WALL_SIGN.get(), "canvas_brown"),
				new SignPair(ModBlocks.GREEN_CANVAS_SIGN.get(), ModBlocks.GREEN_CANVAS_WALL_SIGN.get(), "canvas_green"),
				new SignPair(ModBlocks.RED_CANVAS_SIGN.get(), ModBlocks.RED_CANVAS_WALL_SIGN.get(), "canvas_red"),
				new SignPair(ModBlocks.BLACK_CANVAS_SIGN.get(), ModBlocks.BLACK_CANVAS_WALL_SIGN.get(), "canvas_black")
		);
	}

	private static List<SignPair> hangingSigns() {
		return List.of(
				new SignPair(ModBlocks.HANGING_CANVAS_SIGN.get(), ModBlocks.HANGING_CANVAS_WALL_SIGN.get(), "canvas"),
				new SignPair(ModBlocks.WHITE_HANGING_CANVAS_SIGN.get(), ModBlocks.WHITE_HANGING_CANVAS_WALL_SIGN.get(), "canvas_white"),
				new SignPair(ModBlocks.ORANGE_HANGING_CANVAS_SIGN.get(), ModBlocks.ORANGE_HANGING_CANVAS_WALL_SIGN.get(), "canvas_orange"),
				new SignPair(ModBlocks.MAGENTA_HANGING_CANVAS_SIGN.get(), ModBlocks.MAGENTA_HANGING_CANVAS_WALL_SIGN.get(), "canvas_magenta"),
				new SignPair(ModBlocks.LIGHT_BLUE_HANGING_CANVAS_SIGN.get(), ModBlocks.LIGHT_BLUE_HANGING_CANVAS_WALL_SIGN.get(), "canvas_light_blue"),
				new SignPair(ModBlocks.YELLOW_HANGING_CANVAS_SIGN.get(), ModBlocks.YELLOW_HANGING_CANVAS_WALL_SIGN.get(), "canvas_yellow"),
				new SignPair(ModBlocks.LIME_HANGING_CANVAS_SIGN.get(), ModBlocks.LIME_HANGING_CANVAS_WALL_SIGN.get(), "canvas_lime"),
				new SignPair(ModBlocks.PINK_HANGING_CANVAS_SIGN.get(), ModBlocks.PINK_HANGING_CANVAS_WALL_SIGN.get(), "canvas_pink"),
				new SignPair(ModBlocks.GRAY_HANGING_CANVAS_SIGN.get(), ModBlocks.GRAY_HANGING_CANVAS_WALL_SIGN.get(), "canvas_gray"),
				new SignPair(ModBlocks.LIGHT_GRAY_HANGING_CANVAS_SIGN.get(), ModBlocks.LIGHT_GRAY_HANGING_CANVAS_WALL_SIGN.get(), "canvas_light_gray"),
				new SignPair(ModBlocks.CYAN_HANGING_CANVAS_SIGN.get(), ModBlocks.CYAN_HANGING_CANVAS_WALL_SIGN.get(), "canvas_cyan"),
				new SignPair(ModBlocks.PURPLE_HANGING_CANVAS_SIGN.get(), ModBlocks.PURPLE_HANGING_CANVAS_WALL_SIGN.get(), "canvas_purple"),
				new SignPair(ModBlocks.BLUE_HANGING_CANVAS_SIGN.get(), ModBlocks.BLUE_HANGING_CANVAS_WALL_SIGN.get(), "canvas_blue"),
				new SignPair(ModBlocks.BROWN_HANGING_CANVAS_SIGN.get(), ModBlocks.BROWN_HANGING_CANVAS_WALL_SIGN.get(), "canvas_brown"),
				new SignPair(ModBlocks.GREEN_HANGING_CANVAS_SIGN.get(), ModBlocks.GREEN_HANGING_CANVAS_WALL_SIGN.get(), "canvas_green"),
				new SignPair(ModBlocks.RED_HANGING_CANVAS_SIGN.get(), ModBlocks.RED_HANGING_CANVAS_WALL_SIGN.get(), "canvas_red"),
				new SignPair(ModBlocks.BLACK_HANGING_CANVAS_SIGN.get(), ModBlocks.BLACK_HANGING_CANVAS_WALL_SIGN.get(), "canvas_black")
		);
	}

	private record SignPair(Block sign, Block wallSign, String texture) {
	}

	private static class RenderTypeModelTemplate extends ModelTemplate {
		private final String renderType;

		RenderTypeModelTemplate(Identifier parent, String renderType, TextureSlot... slots) {
			super(Optional.of(parent), Optional.empty(), slots);
			this.renderType = renderType;
		}

		@Override
		public JsonObject createBaseTemplate(Identifier target, Map<TextureSlot, Material> slots) {
			JsonObject result = new JsonObject();
			this.model.ifPresent(model -> result.addProperty("parent", model.toString()));
			result.addProperty("render_type", this.renderType);
			if (!slots.isEmpty()) {
				JsonObject textures = new JsonObject();
				slots.forEach((slot, material) -> {
					JsonElement value = Material.CODEC.encodeStart(JsonOps.INSTANCE, material).getOrThrow();
					textures.add(slot.getId(), value);
				});
				result.add("textures", textures);
			}
			return result;
		}
	}
}
