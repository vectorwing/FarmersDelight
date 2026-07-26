package vectorwing.farmersdelight.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.tag.CompatibilityTags;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.concurrent.CompletableFuture;

public class BlockTags extends BlockTagsProvider
{
	public BlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, FarmersDelight.MODID);
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

	private static ResourceKey<Block> key(Block block) {
		return BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow();
	}

	protected void registerBlockMineables() {
		tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_AXE).add(
			key(ModBlocks.WOODEN_BASKET.get()),
			key(ModBlocks.BAMBOO_BASKET.get()),
			key(ModBlocks.ROPE_FENCE.get()),
			key(ModBlocks.ROPE_FENCE_GATE.get()),
			key(ModBlocks.CUTTING_BOARD.get()),
			key(ModBlocks.CARROT_CRATE.get()),
			key(ModBlocks.POTATO_CRATE.get()),
			key(ModBlocks.BEETROOT_CRATE.get()),
			key(ModBlocks.CABBAGE_CRATE.get()),
			key(ModBlocks.TOMATO_CRATE.get()),
			key(ModBlocks.ONION_CRATE.get()),
			key(ModBlocks.OAK_CABINET.get()),
			key(ModBlocks.BIRCH_CABINET.get()),
			key(ModBlocks.SPRUCE_CABINET.get()),
			key(ModBlocks.JUNGLE_CABINET.get()),
			key(ModBlocks.ACACIA_CABINET.get()),
			key(ModBlocks.DARK_OAK_CABINET.get()),
			key(ModBlocks.MANGROVE_CABINET.get()),
			key(ModBlocks.CHERRY_CABINET.get()),
			key(ModBlocks.BAMBOO_CABINET.get()),
			key(ModBlocks.CRIMSON_CABINET.get()),
			key(ModBlocks.WARPED_CABINET.get()),
			key(ModBlocks.SANDY_SHRUB.get()),
			key(ModBlocks.ROAST_CHICKEN_BLOCK.get()),
			key(ModBlocks.STUFFED_PUMPKIN_BLOCK.get()),
			key(ModBlocks.SHEPHERDS_PIE_BLOCK.get()),
			key(ModBlocks.HONEY_GLAZED_HAM_BLOCK.get()),
			key(ModBlocks.GLEAMING_SALAD_BLOCK.get()),
			key(ModBlocks.RICE_ROLL_MEDLEY_BLOCK.get())
		);
		tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_HOE).add(
			key(ModBlocks.RICE_BALE.get()),
			key(ModBlocks.STRAW_BALE.get())
		);
		tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE).add(
			key(ModBlocks.STOVE.get()),
			key(ModBlocks.COOKING_POT.get()),
			key(ModBlocks.SKILLET.get())
		);
		tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_SHOVEL).add(
			key(ModBlocks.ORGANIC_COMPOST.get()),
			key(ModBlocks.RICH_SOIL.get()),
			key(ModBlocks.RICH_SOIL_FARMLAND.get())
		);
		tag(ModTags.Blocks.MINEABLE_WITH_KNIFE).add(
				key(Blocks.CACTUS),
				key(Blocks.MELON),
				key(Blocks.PUMPKIN),
				key(Blocks.CARVED_PUMPKIN),
				key(Blocks.JACK_O_LANTERN),
				key(Blocks.COBWEB),
				key(Blocks.CAKE),
				key(ModBlocks.RICE_BAG.get()),
				key(ModBlocks.APPLE_PIE.get()),
				key(ModBlocks.SWEET_BERRY_CHEESECAKE.get()),
				key(ModBlocks.CHOCOLATE_PIE.get()),
				key(ModBlocks.PUMPKIN_PIE.get()))
			.addTag(net.minecraft.tags.BlockTags.WOOL_CARPETS)
			.addTag(net.minecraft.tags.BlockTags.WOOL)
			.addTag(net.minecraft.tags.BlockTags.CANDLE_CAKES)
			.addTag(ModTags.Blocks.STRAW_BLOCKS)
			.addTag(CommonTags.Blocks.MINEABLE_WITH_KNIFE);
		tag(CommonTags.Blocks.MINEABLE_WITH_KNIFE);

	}

	protected void registerMinecraftTags() {
		tag(net.minecraft.tags.BlockTags.CLIMBABLE).add(
			key(ModBlocks.ROPE.get()),
			key(ModBlocks.TOMATO_CROP_ON_ROPE.get()));
		tag(net.minecraft.tags.BlockTags.FENCES).add(key(ModBlocks.ROPE_FENCE.get()));
		tag(net.minecraft.tags.BlockTags.FENCE_GATES).add(key(ModBlocks.ROPE_FENCE_GATE.get()));
		tag(net.minecraft.tags.BlockTags.REPLACEABLE).add(
			key(ModBlocks.SANDY_SHRUB.get()));
		tag(net.minecraft.tags.BlockTags.REPLACEABLE_BY_TREES).add(
			key(ModBlocks.SANDY_SHRUB.get()));
		tag(net.minecraft.tags.BlockTags.SUPPORTS_BAMBOO).add(
			key(ModBlocks.RICH_SOIL.get()));
		tag(net.minecraft.tags.BlockTags.HUGE_BROWN_MUSHROOM_CAN_PLACE_ON).add(
			key(ModBlocks.ORGANIC_COMPOST.get()),
			key(ModBlocks.RICH_SOIL.get()));
		tag(net.minecraft.tags.BlockTags.CROPS).add(
			key(ModBlocks.CABBAGE_CROP.get()),
			key(ModBlocks.ONION_CROP.get()),
			key(ModBlocks.RICE_CROP_PANICLES.get()),
			key(ModBlocks.BUDDING_TOMATO_CROP.get()),
			key(ModBlocks.TOMATO_CROP.get()),
			key(ModBlocks.TOMATO_CROP_ON_ROPE.get()));
		tag(net.minecraft.tags.BlockTags.STANDING_SIGNS).add(
			key(ModBlocks.CANVAS_SIGN.get()),
			key(ModBlocks.WHITE_CANVAS_SIGN.get()),
			key(ModBlocks.ORANGE_CANVAS_SIGN.get()),
			key(ModBlocks.MAGENTA_CANVAS_SIGN.get()),
			key(ModBlocks.LIGHT_BLUE_CANVAS_SIGN.get()),
			key(ModBlocks.YELLOW_CANVAS_SIGN.get()),
			key(ModBlocks.LIME_CANVAS_SIGN.get()),
			key(ModBlocks.PINK_CANVAS_SIGN.get()),
			key(ModBlocks.GRAY_CANVAS_SIGN.get()),
			key(ModBlocks.LIGHT_GRAY_CANVAS_SIGN.get()),
			key(ModBlocks.CYAN_CANVAS_SIGN.get()),
			key(ModBlocks.PURPLE_CANVAS_SIGN.get()),
			key(ModBlocks.BLUE_CANVAS_SIGN.get()),
			key(ModBlocks.BROWN_CANVAS_SIGN.get()),
			key(ModBlocks.GREEN_CANVAS_SIGN.get()),
			key(ModBlocks.RED_CANVAS_SIGN.get()),
			key(ModBlocks.BLACK_CANVAS_SIGN.get()));
		tag(net.minecraft.tags.BlockTags.WALL_SIGNS).add(
			key(ModBlocks.CANVAS_WALL_SIGN.get()),
			key(ModBlocks.WHITE_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.ORANGE_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.MAGENTA_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.LIGHT_BLUE_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.YELLOW_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.LIME_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.PINK_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.GRAY_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.LIGHT_GRAY_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.CYAN_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.PURPLE_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.BLUE_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.BROWN_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.GREEN_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.RED_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.BLACK_CANVAS_WALL_SIGN.get()));
		tag(net.minecraft.tags.BlockTags.CEILING_HANGING_SIGNS).add(
			key(ModBlocks.HANGING_CANVAS_SIGN.get()),
			key(ModBlocks.WHITE_HANGING_CANVAS_SIGN.get()),
			key(ModBlocks.ORANGE_HANGING_CANVAS_SIGN.get()),
			key(ModBlocks.MAGENTA_HANGING_CANVAS_SIGN.get()),
			key(ModBlocks.LIGHT_BLUE_HANGING_CANVAS_SIGN.get()),
			key(ModBlocks.YELLOW_HANGING_CANVAS_SIGN.get()),
			key(ModBlocks.LIME_HANGING_CANVAS_SIGN.get()),
			key(ModBlocks.PINK_HANGING_CANVAS_SIGN.get()),
			key(ModBlocks.GRAY_HANGING_CANVAS_SIGN.get()),
			key(ModBlocks.LIGHT_GRAY_HANGING_CANVAS_SIGN.get()),
			key(ModBlocks.CYAN_HANGING_CANVAS_SIGN.get()),
			key(ModBlocks.PURPLE_HANGING_CANVAS_SIGN.get()),
			key(ModBlocks.BLUE_HANGING_CANVAS_SIGN.get()),
			key(ModBlocks.BROWN_HANGING_CANVAS_SIGN.get()),
			key(ModBlocks.GREEN_HANGING_CANVAS_SIGN.get()),
			key(ModBlocks.RED_HANGING_CANVAS_SIGN.get()),
			key(ModBlocks.BLACK_HANGING_CANVAS_SIGN.get()));
		tag(net.minecraft.tags.BlockTags.WALL_HANGING_SIGNS).add(
			key(ModBlocks.HANGING_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.WHITE_HANGING_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.ORANGE_HANGING_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.MAGENTA_HANGING_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.LIGHT_BLUE_HANGING_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.YELLOW_HANGING_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.LIME_HANGING_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.PINK_HANGING_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.GRAY_HANGING_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.LIGHT_GRAY_HANGING_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.CYAN_HANGING_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.PURPLE_HANGING_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.BLUE_HANGING_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.BROWN_HANGING_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.GREEN_HANGING_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.RED_HANGING_CANVAS_WALL_SIGN.get()),
			key(ModBlocks.BLACK_HANGING_CANVAS_WALL_SIGN.get()));
		tag(net.minecraft.tags.BlockTags.SMALL_FLOWERS).add(
			key(ModBlocks.WILD_CARROTS.get()),
			key(ModBlocks.WILD_POTATOES.get()),
			key(ModBlocks.WILD_BEETROOTS.get()),
			key(ModBlocks.WILD_CABBAGES.get()),
			key(ModBlocks.WILD_TOMATOES.get()),
			key(ModBlocks.WILD_ONIONS.get())
		);
		tag(net.minecraft.tags.BlockTags.FLOWERS).add(key(ModBlocks.WILD_RICE.get()));
		tag(net.minecraft.tags.BlockTags.DIRT).add(
			key(ModBlocks.RICH_SOIL.get()));
		tag(net.minecraft.tags.BlockTags.MAINTAINS_FARMLAND).add(
			key(ModBlocks.CABBAGE_CROP.get()),
			key(ModBlocks.BUDDING_TOMATO_CROP.get()),
			key(ModBlocks.TOMATO_CROP.get()),
			key(ModBlocks.ONION_CROP.get()),
			key(ModBlocks.RICE_CROP.get())
		);
		tag(net.minecraft.tags.BlockTags.COMBINATION_STEP_SOUND_BLOCKS).add(
			key(ModBlocks.CANVAS_RUG.get()),
			key(ModBlocks.FULL_TATAMI_MAT.get()),
			key(ModBlocks.HALF_TATAMI_MAT.get()),
			key(ModBlocks.CUTTING_BOARD.get())
		);
	}

	protected void registerNeoForgeTags() {
		tag(Tags.Blocks.ROPES).add(key(ModBlocks.ROPE.get()));
		tag(Tags.Blocks.VILLAGER_FARMLANDS).add(key(ModBlocks.RICH_SOIL_FARMLAND.get()));
		tag(Tags.Blocks.FENCES).add(key(ModBlocks.ROPE_FENCE.get()));
		tag(Tags.Blocks.FENCE_GATES).add(key(ModBlocks.ROPE_FENCE_GATE.get()));
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
		tag(CommonTags.Blocks.STORAGE_BLOCKS_CARROT).add(key(ModBlocks.CARROT_CRATE.get()));
		tag(CommonTags.Blocks.STORAGE_BLOCKS_POTATO).add(key(ModBlocks.POTATO_CRATE.get()));
		tag(CommonTags.Blocks.STORAGE_BLOCKS_BEETROOT).add(key(ModBlocks.BEETROOT_CRATE.get()));
		tag(CommonTags.Blocks.STORAGE_BLOCKS_CABBAGE).add(key(ModBlocks.CABBAGE_CRATE.get()));
		tag(CommonTags.Blocks.STORAGE_BLOCKS_TOMATO).add(key(ModBlocks.TOMATO_CRATE.get()));
		tag(CommonTags.Blocks.STORAGE_BLOCKS_ONION).add(key(ModBlocks.ONION_CRATE.get()));
		tag(CommonTags.Blocks.STORAGE_BLOCKS_RICE).add(key(ModBlocks.RICE_BAG.get()));
		tag(CommonTags.Blocks.STORAGE_BLOCKS_RICE_PANICLE).add(key(ModBlocks.RICE_BALE.get()));
		tag(CommonTags.Blocks.STORAGE_BLOCKS_STRAW).add(key(ModBlocks.STRAW_BALE.get()));
	}

	protected void registerModTags() {
		tag(ModTags.Blocks.FEASTS).add(
			key(ModBlocks.ROAST_CHICKEN_BLOCK.get()),
			key(ModBlocks.STUFFED_PUMPKIN_BLOCK.get()),
			key(ModBlocks.SHEPHERDS_PIE_BLOCK.get()),
			key(ModBlocks.HONEY_GLAZED_HAM_BLOCK.get()),
			key(ModBlocks.GLEAMING_SALAD_BLOCK.get()),
			key(ModBlocks.RICE_ROLL_MEDLEY_BLOCK.get())
		);
		tag(ModTags.Blocks.PIES).add(
			key(ModBlocks.APPLE_PIE.get()),
			key(ModBlocks.SWEET_BERRY_CHEESECAKE.get()),
			key(ModBlocks.CHOCOLATE_PIE.get()),
			key(ModBlocks.PUMPKIN_PIE.get())
		);
		tag(ModTags.Blocks.TERRAIN)
			.addTag(net.minecraft.tags.BlockTags.DIRT)
			.addTag(net.minecraft.tags.BlockTags.SAND);
		tag(ModTags.Blocks.STRAW_BLOCKS).add(
			key(ModBlocks.ROPE.get()),
			key(ModBlocks.SAFETY_NET.get()),
			key(ModBlocks.CANVAS_RUG.get()),
			key(ModBlocks.TATAMI.get()),
			key(ModBlocks.FULL_TATAMI_MAT.get()),
			key(ModBlocks.HALF_TATAMI_MAT.get())
		);
		tag(ModTags.Blocks.WILD_CROPS).add(
			key(ModBlocks.WILD_CARROTS.get()),
			key(ModBlocks.WILD_POTATOES.get()),
			key(ModBlocks.WILD_BEETROOTS.get()),
			key(ModBlocks.WILD_CABBAGES.get()),
			key(ModBlocks.WILD_TOMATOES.get()),
			key(ModBlocks.WILD_ONIONS.get()),
			key(ModBlocks.WILD_RICE.get()));
		tag(ModTags.Blocks.CABINETS_WOODEN)
			.add(key(ModBlocks.OAK_CABINET.get()))
			.add(key(ModBlocks.SPRUCE_CABINET.get()))
			.add(key(ModBlocks.BIRCH_CABINET.get()))
			.add(key(ModBlocks.JUNGLE_CABINET.get()))
			.add(key(ModBlocks.ACACIA_CABINET.get()))
			.add(key(ModBlocks.DARK_OAK_CABINET.get()))
			.add(key(ModBlocks.MANGROVE_CABINET.get()))
			.add(key(ModBlocks.CHERRY_CABINET.get()))
			.add(key(ModBlocks.BAMBOO_CABINET.get()))
			.add(key(ModBlocks.CRIMSON_CABINET.get()))
			.add(key(ModBlocks.WARPED_CABINET.get()));
		tag(ModTags.Blocks.CABINETS).addTag(ModTags.Blocks.CABINETS_WOODEN);
		tag(ModTags.Blocks.MUSHROOM_COLONIES)
			.add(key(ModBlocks.BROWN_MUSHROOM_COLONY.get()))
			.add(key(ModBlocks.RED_MUSHROOM_COLONY.get()));
		tag(ModTags.Blocks.ROPES).add(key(ModBlocks.ROPE.get()))
			.addOptional(ResourceKey.create(Registries.BLOCK, Identifier.parse("quark:rope")))
			.addOptional(ResourceKey.create(Registries.BLOCK, Identifier.parse("supplementaries:rope")));
		tag(ModTags.Blocks.TRAY_HEAT_SOURCES).add(
				key(Blocks.LAVA))
			.addTag(net.minecraft.tags.BlockTags.CAMPFIRES)
			.addTag(net.minecraft.tags.BlockTags.FIRE);
		tag(ModTags.Blocks.HEAT_SOURCES).add(
				key(Blocks.MAGMA_BLOCK),
				key(Blocks.LAVA_CAULDRON),
				key(ModBlocks.STOVE.get()))
			.addTag(ModTags.Blocks.TRAY_HEAT_SOURCES);
		tag(ModTags.Blocks.HEAT_CONDUCTORS).add(
				key(Blocks.HOPPER))
			.addOptional(ResourceKey.create(Registries.BLOCK, Identifier.parse("create:chute")));
		tag(ModTags.Blocks.COMPOST_ACTIVATORS).add(
				key(Blocks.BROWN_MUSHROOM),
				key(Blocks.RED_MUSHROOM),
				key(Blocks.PODZOL),
				key(Blocks.MYCELIUM),
				key(ModBlocks.ORGANIC_COMPOST.get()),
				key(ModBlocks.RICH_SOIL.get()),
				key(ModBlocks.RICH_SOIL_FARMLAND.get()))
			.addTag(ModTags.Blocks.MUSHROOM_COLONIES);
		tag(ModTags.Blocks.UNAFFECTED_BY_RICH_SOIL).add(
				key(Blocks.GRASS_BLOCK),
				key(Blocks.SHORT_GRASS),
				key(Blocks.MOSS_BLOCK),
				key(Blocks.CRIMSON_NYLIUM),
				key(Blocks.WARPED_NYLIUM),
				key(Blocks.FERN),
				key(Blocks.TWISTING_VINES),
				key(Blocks.TWISTING_VINES_PLANT),
				key(Blocks.BIG_DRIPLEAF),
				key(Blocks.BIG_DRIPLEAF_STEM),
				key(Blocks.PINK_PETALS),
				key(ModBlocks.SANDY_SHRUB.get()))
			.addTag(ModTags.Blocks.MUSHROOM_COLONIES)
			.addTag(ModTags.Blocks.WILD_CROPS)
			.addTag(net.minecraft.tags.BlockTags.FLOWERS);
		tag(ModTags.Blocks.MUSHROOM_COLONY_GROWABLE_ON).add(key(ModBlocks.RICH_SOIL.get()));
		tag(ModTags.Blocks.DROPS_CAKE_SLICE).addTag(net.minecraft.tags.BlockTags.CANDLE_CAKES);
		tag(ModTags.Blocks.CAMPFIRE_SIGNAL_SMOKE).add(key(ModBlocks.STRAW_BALE.get())).add(key(ModBlocks.RICE_BALE.get()));
		tag(ModTags.Blocks.PLANTED_FROM_BELOW).add(key(Blocks.CAVE_VINES), key(Blocks.CAVE_VINES_PLANT));
	}

	private void registerCompatibilityTags() {
		tag(CompatibilityTags.CREATE_FAN_TRANSPARENT).add(key(ModBlocks.SAFETY_NET.get()));
		tag(CompatibilityTags.CREATE_PASSIVE_BOILER_HEATERS).add(key(ModBlocks.STOVE.get()));
		tag(CompatibilityTags.CREATE_BRITTLE).add(
				key(ModBlocks.CUTTING_BOARD.get()),
				key(ModBlocks.FULL_TATAMI_MAT.get()),
				key(ModBlocks.HALF_TATAMI_MAT.get()))
			.addTag(ModTags.Blocks.FEASTS)
			.addTag(ModTags.Blocks.PIES);

		tag(CompatibilityTags.SABLE_SUPER_LIGHT)
			.add(key(ModBlocks.CUTTING_BOARD.get()))
			.add(key(ModBlocks.CANVAS_RUG.get()))
			.add(key(ModBlocks.FULL_TATAMI_MAT.get()))
			.add(key(ModBlocks.HALF_TATAMI_MAT.get()))
			.add(key(ModBlocks.SAFETY_NET.get()));
		tag(CompatibilityTags.SABLE_LIGHT)
			.addTag(ModTags.Blocks.CABINETS)
			.add(key(ModBlocks.WOODEN_BASKET.get()))
			.add(key(ModBlocks.BAMBOO_BASKET.get()));

		tag(CompatibilityTags.SERENE_SEASONS_AUTUMN_CROPS_BLOCK).add(
			key(ModBlocks.CABBAGE_CROP.get()),
			key(ModBlocks.ONION_CROP.get()),
			key(ModBlocks.RICE_CROP.get()),
			key(ModBlocks.RICE_CROP_PANICLES.get()));
		tag(CompatibilityTags.SERENE_SEASONS_SPRING_CROPS_BLOCK).add(
			key(ModBlocks.ONION_CROP.get()));
		tag(CompatibilityTags.SERENE_SEASONS_SUMMER_CROPS_BLOCK).add(
			key(ModBlocks.BUDDING_TOMATO_CROP.get()),
			key(ModBlocks.TOMATO_CROP.get()),
			key(ModBlocks.TOMATO_CROP_ON_ROPE.get()),
			key(ModBlocks.RICE_CROP.get()),
			key(ModBlocks.RICE_CROP_PANICLES.get()));
		tag(CompatibilityTags.SERENE_SEASONS_WINTER_CROPS_BLOCK).add(
			key(ModBlocks.CABBAGE_CROP.get()));
		tag(CompatibilityTags.SERENE_SEASONS_UNBREAKABLE_FERTILE_CROPS).add(
			key(ModBlocks.ONION_CROP.get()));
	}
}
