package vectorwing.farmersdelight.data;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.List;
import java.util.stream.Stream;

/**
 * TODO 26.2: Continue porting the remaining blockstate/model generation to this vanilla ModelProvider API.
 */
public class BlockStates extends ModelProvider
{
	public BlockStates(PackOutput output) {
		super(output, FarmersDelight.MODID);
	}

	@Override
	protected Stream<? extends Holder<Item>> getKnownItems() {
		return Stream.empty();
	}

	@Override
	protected Stream<? extends Holder<Block>> getKnownBlocks() {
		return Stream.concat(standingSigns().stream().flatMap(pair -> Stream.of(pair.sign(), pair.wallSign())),
				hangingSigns().stream().flatMap(pair -> Stream.of(pair.sign(), pair.wallSign())))
				.map(Block::builtInRegistryHolder);
	}

	@Override
	protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
		standingSigns().forEach(pair -> registerStandingSign(blockModels, pair));
		hangingSigns().forEach(pair -> registerHangingSign(blockModels, pair));
	}

	@Override
	public String getName() {
		return "Block Model Definitions - " + FarmersDelight.MODID;
	}

	private static void registerStandingSign(BlockModelGenerators blockModels, SignPair pair) {
		TextureMapping mapping = signMapping("entity/signs/" + pair.texture(), Identifier.withDefaultNamespace("block/spruce_planks"));
		MultiVariant rot0 = BlockModelGenerators.plainVariant(ModelTemplates.SIGN_ROT_0.create(ModelLocationUtils.getModelLocation(pair.sign(), "_rot_0"), mapping, blockModels.modelOutput));
		MultiVariant rot1 = BlockModelGenerators.plainVariant(ModelTemplates.SIGN_ROT_1.create(ModelLocationUtils.getModelLocation(pair.sign(), "_rot_1"), mapping, blockModels.modelOutput));
		MultiVariant rot2 = BlockModelGenerators.plainVariant(ModelTemplates.SIGN_ROT_2.create(ModelLocationUtils.getModelLocation(pair.sign(), "_rot_2"), mapping, blockModels.modelOutput));
		MultiVariant rot3 = BlockModelGenerators.plainVariant(ModelTemplates.SIGN_ROT_3.create(ModelLocationUtils.getModelLocation(pair.sign(), "_rot_3"), mapping, blockModels.modelOutput));

		blockModels.blockStateOutput.accept(BlockModelGenerators.createSign(pair.sign(), rot0, rot1, rot2, rot3));

		MultiVariant wallModel = BlockModelGenerators.plainVariant(ModelTemplates.WALL_SIGN.create(pair.wallSign(), mapping, blockModels.modelOutput));
		blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(pair.wallSign(), wallModel).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING_ALT));
	}

	private static void registerHangingSign(BlockModelGenerators blockModels, SignPair pair) {
		TextureMapping mapping = signMapping("entity/signs/hanging/" + pair.texture(), Identifier.withDefaultNamespace("block/stripped_spruce_log"));
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
}
