package vectorwing.farmersdelight.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.tag.CompatibilityTags;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class BlockTags extends BlockTagsProvider
{
	public BlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, FarmersDelight.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.@NotNull Provider provider) {
		this.registerModTags();
		this.registerMinecraftTags();
		this.registerNeoForgeTags();
		this.registerCommonTags();
		this.registerCompatibilityTags();

		this.registerBlockMineables();
	}

	protected void registerBlockMineables() {
		tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_AXE).add(
			ModBlocks.WOODEN_BASKET.get(),
			ModBlocks.BAMBOO_BASKET.get(),
			ModBlocks.ROPE_FENCE.get(),
			ModBlocks.ROPE_FENCE_GATE.get(),
			ModBlocks.CUTTING_BOARD.get(),
			ModBlocks.CARROT_CRATE.get(),
			ModBlocks.POTATO_CRATE.get(),
			ModBlocks.BEETROOT_CRATE.get(),
			ModBlocks.CABBAGE_CRATE.get(),
			ModBlocks.TOMATO_CRATE.get(),
			ModBlocks.ONION_CRATE.get(),
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
			ModBlocks.SANDY_SHRUB.get(),
			ModBlocks.ROAST_CHICKEN_BLOCK.get(),
			ModBlocks.STUFFED_PUMPKIN_BLOCK.get(),
			ModBlocks.SHEPHERDS_PIE_BLOCK.get(),
			ModBlocks.HONEY_GLAZED_HAM_BLOCK.get(),
			ModBlocks.GLEAMING_SALAD_BLOCK.get(),
			ModBlocks.RICE_ROLL_MEDLEY_BLOCK.get()
		);
		tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_HOE).add(
			ModBlocks.RICE_BALE.get(),
			ModBlocks.STRAW_BALE.get()
		);
		tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE).add(
			ModBlocks.STOVE.get(),
			ModBlocks.COOKING_POT.get(),
			ModBlocks.SKILLET.get()
		);
		tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_SHOVEL).add(
			ModBlocks.ORGANIC_COMPOST.get(),
			ModBlocks.RICH_SOIL.get(),
			ModBlocks.RICH_SOIL_FARMLAND.get()
		);
		tag(ModTags.Blocks.MINEABLE_WITH_KNIFE).add(
				Blocks.CACTUS,
				Blocks.MELON,
				Blocks.PUMPKIN,
				Blocks.CARVED_PUMPKIN,
				Blocks.JACK_O_LANTERN,
				Blocks.COBWEB,
				Blocks.CAKE,
				ModBlocks.RICE_BAG.get(),
				ModBlocks.APPLE_PIE.get(),
				ModBlocks.SWEET_BERRY_CHEESECAKE.get(),
				ModBlocks.CHOCOLATE_PIE.get(),
				ModBlocks.PUMPKIN_PIE.get())
			.addTag(net.minecraft.tags.BlockTags.WOOL_CARPETS)
			.addTag(net.minecraft.tags.BlockTags.WOOL)
			.addTag(net.minecraft.tags.BlockTags.CANDLE_CAKES)
			.addTag(ModTags.Blocks.STRAW_BLOCKS)
			.addTag(CommonTags.Blocks.MINEABLE_WITH_KNIFE);
		tag(CommonTags.Blocks.MINEABLE_WITH_KNIFE);

	}

	protected void registerMinecraftTags() {
		tag(net.minecraft.tags.BlockTags.CLIMBABLE).add(
			ModBlocks.ROPE.get(),
			ModBlocks.TOMATO_CROP_ON_ROPE.get());
		tag(net.minecraft.tags.BlockTags.FENCES).add(ModBlocks.ROPE_FENCE.get());
		tag(net.minecraft.tags.BlockTags.FENCE_GATES).add(ModBlocks.ROPE_FENCE_GATE.get());
		tag(net.minecraft.tags.BlockTags.REPLACEABLE).add(
			ModBlocks.SANDY_SHRUB.get());
		tag(net.minecraft.tags.BlockTags.REPLACEABLE_BY_TREES).add(
			ModBlocks.SANDY_SHRUB.get());
		tag(net.minecraft.tags.BlockTags.BAMBOO_PLANTABLE_ON).add(
			ModBlocks.RICH_SOIL.get());
		tag(net.minecraft.tags.BlockTags.MUSHROOM_GROW_BLOCK).add(
			ModBlocks.ORGANIC_COMPOST.get(),
			ModBlocks.RICH_SOIL.get());
		tag(net.minecraft.tags.BlockTags.CROPS).add(
			ModBlocks.CABBAGE_CROP.get(),
			ModBlocks.ONION_CROP.get(),
			ModBlocks.RICE_CROP_PANICLES.get(),
			ModBlocks.BUDDING_TOMATO_CROP.get(),
			ModBlocks.TOMATO_CROP.get(),
			ModBlocks.TOMATO_CROP_ON_ROPE.get());
		tag(net.minecraft.tags.BlockTags.STANDING_SIGNS).add(
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
		tag(net.minecraft.tags.BlockTags.WALL_SIGNS).add(
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
		tag(net.minecraft.tags.BlockTags.CEILING_HANGING_SIGNS).add(
			ModBlocks.HANGING_CANVAS_SIGN.get(),
			ModBlocks.WHITE_HANGING_CANVAS_SIGN.get(),
			ModBlocks.ORANGE_HANGING_CANVAS_SIGN.get(),
			ModBlocks.MAGENTA_HANGING_CANVAS_SIGN.get(),
			ModBlocks.LIGHT_BLUE_HANGING_CANVAS_SIGN.get(),
			ModBlocks.YELLOW_HANGING_CANVAS_SIGN.get(),
			ModBlocks.LIME_HANGING_CANVAS_SIGN.get(),
			ModBlocks.PINK_HANGING_CANVAS_SIGN.get(),
			ModBlocks.GRAY_HANGING_CANVAS_SIGN.get(),
			ModBlocks.LIGHT_GRAY_HANGING_CANVAS_SIGN.get(),
			ModBlocks.CYAN_HANGING_CANVAS_SIGN.get(),
			ModBlocks.PURPLE_HANGING_CANVAS_SIGN.get(),
			ModBlocks.BLUE_HANGING_CANVAS_SIGN.get(),
			ModBlocks.BROWN_HANGING_CANVAS_SIGN.get(),
			ModBlocks.GREEN_HANGING_CANVAS_SIGN.get(),
			ModBlocks.RED_HANGING_CANVAS_SIGN.get(),
			ModBlocks.BLACK_HANGING_CANVAS_SIGN.get());
		tag(net.minecraft.tags.BlockTags.WALL_HANGING_SIGNS).add(
			ModBlocks.HANGING_CANVAS_WALL_SIGN.get(),
			ModBlocks.WHITE_HANGING_CANVAS_WALL_SIGN.get(),
			ModBlocks.ORANGE_HANGING_CANVAS_WALL_SIGN.get(),
			ModBlocks.MAGENTA_HANGING_CANVAS_WALL_SIGN.get(),
			ModBlocks.LIGHT_BLUE_HANGING_CANVAS_WALL_SIGN.get(),
			ModBlocks.YELLOW_HANGING_CANVAS_WALL_SIGN.get(),
			ModBlocks.LIME_HANGING_CANVAS_WALL_SIGN.get(),
			ModBlocks.PINK_HANGING_CANVAS_WALL_SIGN.get(),
			ModBlocks.GRAY_HANGING_CANVAS_WALL_SIGN.get(),
			ModBlocks.LIGHT_GRAY_HANGING_CANVAS_WALL_SIGN.get(),
			ModBlocks.CYAN_HANGING_CANVAS_WALL_SIGN.get(),
			ModBlocks.PURPLE_HANGING_CANVAS_WALL_SIGN.get(),
			ModBlocks.BLUE_HANGING_CANVAS_WALL_SIGN.get(),
			ModBlocks.BROWN_HANGING_CANVAS_WALL_SIGN.get(),
			ModBlocks.GREEN_HANGING_CANVAS_WALL_SIGN.get(),
			ModBlocks.RED_HANGING_CANVAS_WALL_SIGN.get(),
			ModBlocks.BLACK_HANGING_CANVAS_WALL_SIGN.get());
		tag(net.minecraft.tags.BlockTags.SMALL_FLOWERS).add(
			ModBlocks.WILD_CARROTS.get(),
			ModBlocks.WILD_POTATOES.get(),
			ModBlocks.WILD_BEETROOTS.get(),
			ModBlocks.WILD_CABBAGES.get(),
			ModBlocks.WILD_TOMATOES.get(),
			ModBlocks.WILD_ONIONS.get()
		);
		tag(net.minecraft.tags.BlockTags.TALL_FLOWERS).add(ModBlocks.WILD_RICE.get());
		tag(net.minecraft.tags.BlockTags.DIRT).add(
			ModBlocks.RICH_SOIL.get());
		tag(net.minecraft.tags.BlockTags.MAINTAINS_FARMLAND).add(
			ModBlocks.CABBAGE_CROP.get(),
			ModBlocks.BUDDING_TOMATO_CROP.get(),
			ModBlocks.TOMATO_CROP.get(),
			ModBlocks.ONION_CROP.get(),
			ModBlocks.RICE_CROP.get()
		);
		tag(net.minecraft.tags.BlockTags.COMBINATION_STEP_SOUND_BLOCKS).add(
			ModBlocks.CANVAS_RUG.get(),
			ModBlocks.FULL_TATAMI_MAT.get(),
			ModBlocks.HALF_TATAMI_MAT.get(),
			ModBlocks.CUTTING_BOARD.get()
		);
	}

	protected void registerNeoForgeTags() {
		tag(Tags.Blocks.ROPES).add(ModBlocks.ROPE.get());
		tag(Tags.Blocks.VILLAGER_FARMLANDS).add(ModBlocks.RICH_SOIL_FARMLAND.get());
		tag(Tags.Blocks.FENCES).add(ModBlocks.ROPE_FENCE.get());
		tag(Tags.Blocks.FENCE_GATES).add(ModBlocks.ROPE_FENCE_GATE.get());
		tag(Tags.Blocks.STORAGE_BLOCKS).addTags(
			CommonTags.Blocks.STORAGE_BLOCKS_CARROT,
			CommonTags.Blocks.STORAGE_BLOCKS_POTATO,
			CommonTags.Blocks.STORAGE_BLOCKS_BEETROOT,
			CommonTags.Blocks.STORAGE_BLOCKS_CABBAGE,
			CommonTags.Blocks.STORAGE_BLOCKS_TOMATO,
			CommonTags.Blocks.STORAGE_BLOCKS_ONION,
			CommonTags.Blocks.STORAGE_BLOCKS_RICE,
			CommonTags.Blocks.STORAGE_BLOCKS_RICE_PANICLE,
			CommonTags.Blocks.STORAGE_BLOCKS_STRAW
		);
	}

	protected void registerCommonTags() {
		tag(CommonTags.Blocks.MINEABLE_WITH_KNIFE);
		tag(CommonTags.Blocks.STORAGE_BLOCKS_CARROT).add(ModBlocks.CARROT_CRATE.get());
		tag(CommonTags.Blocks.STORAGE_BLOCKS_POTATO).add(ModBlocks.POTATO_CRATE.get());
		tag(CommonTags.Blocks.STORAGE_BLOCKS_BEETROOT).add(ModBlocks.BEETROOT_CRATE.get());
		tag(CommonTags.Blocks.STORAGE_BLOCKS_CABBAGE).add(ModBlocks.CABBAGE_CRATE.get());
		tag(CommonTags.Blocks.STORAGE_BLOCKS_TOMATO).add(ModBlocks.TOMATO_CRATE.get());
		tag(CommonTags.Blocks.STORAGE_BLOCKS_ONION).add(ModBlocks.ONION_CRATE.get());
		tag(CommonTags.Blocks.STORAGE_BLOCKS_RICE).add(ModBlocks.RICE_BAG.get());
		tag(CommonTags.Blocks.STORAGE_BLOCKS_RICE_PANICLE).add(ModBlocks.RICE_BALE.get());
		tag(CommonTags.Blocks.STORAGE_BLOCKS_STRAW).add(ModBlocks.STRAW_BALE.get());
	}

	protected void registerModTags() {
		tag(ModTags.Blocks.FEASTS).add(
			ModBlocks.ROAST_CHICKEN_BLOCK.get(),
			ModBlocks.STUFFED_PUMPKIN_BLOCK.get(),
			ModBlocks.SHEPHERDS_PIE_BLOCK.get(),
			ModBlocks.HONEY_GLAZED_HAM_BLOCK.get(),
			ModBlocks.GLEAMING_SALAD_BLOCK.get(),
			ModBlocks.RICE_ROLL_MEDLEY_BLOCK.get()
		);
		tag(ModTags.Blocks.PIES).add(
			ModBlocks.APPLE_PIE.get(),
			ModBlocks.SWEET_BERRY_CHEESECAKE.get(),
			ModBlocks.CHOCOLATE_PIE.get(),
			ModBlocks.PUMPKIN_PIE.get()
		);
		tag(ModTags.Blocks.TERRAIN)
			.addTag(net.minecraft.tags.BlockTags.DIRT)
			.addTag(net.minecraft.tags.BlockTags.SAND);
		tag(ModTags.Blocks.STRAW_BLOCKS).add(
			ModBlocks.ROPE.get(),
			ModBlocks.SAFETY_NET.get(),
			ModBlocks.CANVAS_RUG.get(),
			ModBlocks.TATAMI.get(),
			ModBlocks.FULL_TATAMI_MAT.get(),
			ModBlocks.HALF_TATAMI_MAT.get()
		);
		tag(ModTags.Blocks.WILD_CROPS).add(
			ModBlocks.WILD_CARROTS.get(),
			ModBlocks.WILD_POTATOES.get(),
			ModBlocks.WILD_BEETROOTS.get(),
			ModBlocks.WILD_CABBAGES.get(),
			ModBlocks.WILD_TOMATOES.get(),
			ModBlocks.WILD_ONIONS.get(),
			ModBlocks.WILD_RICE.get());
		tag(ModTags.Blocks.CABINETS_WOODEN)
			.add(ModBlocks.OAK_CABINET.get())
			.add(ModBlocks.SPRUCE_CABINET.get())
			.add(ModBlocks.BIRCH_CABINET.get())
			.add(ModBlocks.JUNGLE_CABINET.get())
			.add(ModBlocks.ACACIA_CABINET.get())
			.add(ModBlocks.DARK_OAK_CABINET.get())
			.add(ModBlocks.MANGROVE_CABINET.get())
			.add(ModBlocks.CHERRY_CABINET.get())
			.add(ModBlocks.BAMBOO_CABINET.get())
			.add(ModBlocks.CRIMSON_CABINET.get())
			.add(ModBlocks.WARPED_CABINET.get());
		tag(ModTags.Blocks.CABINETS).addTag(ModTags.Blocks.CABINETS_WOODEN);
		tag(ModTags.Blocks.MUSHROOM_COLONIES)
			.add(ModBlocks.BROWN_MUSHROOM_COLONY.get())
			.add(ModBlocks.RED_MUSHROOM_COLONY.get());
		tag(ModTags.Blocks.ROPES).add(ModBlocks.ROPE.get())
			.addOptional(ResourceLocation.parse("quark:rope"))
			.addOptional(ResourceLocation.parse("supplementaries:rope"));
		tag(ModTags.Blocks.TRAY_HEAT_SOURCES).add(
				Blocks.LAVA)
			.addTag(net.minecraft.tags.BlockTags.CAMPFIRES)
			.addTag(net.minecraft.tags.BlockTags.FIRE);
		tag(ModTags.Blocks.HEAT_SOURCES).add(
				Blocks.MAGMA_BLOCK,
				Blocks.LAVA_CAULDRON,
				ModBlocks.STOVE.get())
			.addTag(ModTags.Blocks.TRAY_HEAT_SOURCES);
		tag(ModTags.Blocks.HEAT_CONDUCTORS).add(
				Blocks.HOPPER)
			.addOptional(ResourceLocation.parse("create:chute"));
		tag(ModTags.Blocks.COMPOST_ACTIVATORS).add(
				Blocks.BROWN_MUSHROOM,
				Blocks.RED_MUSHROOM,
				Blocks.PODZOL,
				Blocks.MYCELIUM,
				ModBlocks.ORGANIC_COMPOST.get(),
				ModBlocks.RICH_SOIL.get(),
				ModBlocks.RICH_SOIL_FARMLAND.get())
			.addTag(ModTags.Blocks.MUSHROOM_COLONIES);
		tag(ModTags.Blocks.UNAFFECTED_BY_RICH_SOIL).add(
				Blocks.GRASS_BLOCK,
				Blocks.SHORT_GRASS,
				Blocks.MOSS_BLOCK,
				Blocks.CRIMSON_NYLIUM,
				Blocks.WARPED_NYLIUM,
				Blocks.FERN,
				Blocks.TWISTING_VINES,
				Blocks.TWISTING_VINES_PLANT,
				Blocks.BIG_DRIPLEAF,
				Blocks.BIG_DRIPLEAF_STEM,
				Blocks.PINK_PETALS,
				ModBlocks.SANDY_SHRUB.get())
			.addTag(ModTags.Blocks.MUSHROOM_COLONIES)
			.addTag(ModTags.Blocks.WILD_CROPS)
			.addTag(net.minecraft.tags.BlockTags.TALL_FLOWERS);
		tag(ModTags.Blocks.MUSHROOM_COLONY_GROWABLE_ON).add(ModBlocks.RICH_SOIL.get());
		tag(ModTags.Blocks.DROPS_CAKE_SLICE).add(
			Blocks.CANDLE_CAKE,
			Blocks.WHITE_CANDLE_CAKE,
			Blocks.ORANGE_CANDLE_CAKE,
			Blocks.MAGENTA_CANDLE_CAKE,
			Blocks.LIGHT_BLUE_CANDLE_CAKE,
			Blocks.YELLOW_CANDLE_CAKE,
			Blocks.LIME_CANDLE_CAKE,
			Blocks.PINK_CANDLE_CAKE,
			Blocks.GRAY_CANDLE_CAKE,
			Blocks.LIGHT_GRAY_CANDLE_CAKE,
			Blocks.CYAN_CANDLE_CAKE,
			Blocks.PURPLE_CANDLE_CAKE,
			Blocks.BLUE_CANDLE_CAKE,
			Blocks.BROWN_CANDLE_CAKE,
			Blocks.GREEN_CANDLE_CAKE,
			Blocks.RED_CANDLE_CAKE,
			Blocks.BLACK_CANDLE_CAKE);
		tag(ModTags.Blocks.CAMPFIRE_SIGNAL_SMOKE).add(ModBlocks.STRAW_BALE.get()).add(ModBlocks.RICE_BALE.get());
		tag(ModTags.Blocks.PLANTED_FROM_BELOW).add(Blocks.CAVE_VINES, Blocks.CAVE_VINES_PLANT);
	}

	private void registerCompatibilityTags() {
		tag(CompatibilityTags.CREATE_FAN_TRANSPARENT).add(ModBlocks.SAFETY_NET.get());
		tag(CompatibilityTags.CREATE_PASSIVE_BOILER_HEATERS).add(ModBlocks.STOVE.get());
		tag(CompatibilityTags.CREATE_BRITTLE).add(
				ModBlocks.CUTTING_BOARD.get(),
				ModBlocks.FULL_TATAMI_MAT.get(),
				ModBlocks.HALF_TATAMI_MAT.get())
			.addTag(ModTags.Blocks.FEASTS)
			.addTag(ModTags.Blocks.PIES);

		tag(CompatibilityTags.SABLE_SUPER_LIGHT)
			.add(ModBlocks.CUTTING_BOARD.get())
			.add(ModBlocks.CANVAS_RUG.get())
			.add(ModBlocks.FULL_TATAMI_MAT.get())
			.add(ModBlocks.HALF_TATAMI_MAT.get())
			.add(ModBlocks.SAFETY_NET.get());
		tag(CompatibilityTags.SABLE_LIGHT)
			.addTag(ModTags.Blocks.CABINETS)
			.add(ModBlocks.WOODEN_BASKET.get())
			.add(ModBlocks.BAMBOO_BASKET.get());

		tag(CompatibilityTags.SERENE_SEASONS_AUTUMN_CROPS_BLOCK).add(
			ModBlocks.CABBAGE_CROP.get(),
			ModBlocks.ONION_CROP.get(),
			ModBlocks.RICE_CROP.get(),
			ModBlocks.RICE_CROP_PANICLES.get());
		tag(CompatibilityTags.SERENE_SEASONS_SPRING_CROPS_BLOCK).add(
			ModBlocks.ONION_CROP.get());
		tag(CompatibilityTags.SERENE_SEASONS_SUMMER_CROPS_BLOCK).add(
			ModBlocks.BUDDING_TOMATO_CROP.get(),
			ModBlocks.TOMATO_CROP.get(),
			ModBlocks.TOMATO_CROP_ON_ROPE.get(),
			ModBlocks.RICE_CROP.get(),
			ModBlocks.RICE_CROP_PANICLES.get());
		tag(CompatibilityTags.SERENE_SEASONS_WINTER_CROPS_BLOCK).add(
			ModBlocks.CABBAGE_CROP.get());
		tag(CompatibilityTags.SERENE_SEASONS_UNBREAKABLE_FERTILE_CROPS).add(
			ModBlocks.ONION_CROP.get());
	}
}
