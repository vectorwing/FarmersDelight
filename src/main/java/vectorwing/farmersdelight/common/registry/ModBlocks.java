package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.BlockShapes;
import vectorwing.farmersdelight.common.block.*;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, FarmersDelight.MODID);

	private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
		return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
	}

	private static ToIntFunction<BlockState> glowingFeastBlockEmission() {
		return (state) -> state.getValue(FeastBlock.SERVINGS) * 3;
	}

	private static ResourceKey<Block> blockKey(String name) {
		return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(FarmersDelight.MODID, name));
	}

	private static BlockBehaviour.Properties cropProperties(String name) {
		return BlockBehaviour.Properties.of()
				.mapColor(MapColor.PLANT)
				.noCollision()
				.randomTicks()
				.instabreak()
				.sound(SoundType.CROP)
				.pushReaction(PushReaction.DESTROY)
				.setId(blockKey(name));
	}

	// Workstations
	public static final Supplier<Block> STOVE = BLOCKS.register("stove",
			() -> new StoveBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.BRICKS).lightLevel(litBlockEmission(13)).setId(blockKey("stove"))));
	public static final Supplier<Block> COOKING_POT = BLOCKS.register("cooking_pot",
			() -> new CookingPotBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN).setId(blockKey("cooking_pot"))));
	public static final Supplier<Block> SKILLET = BLOCKS.register("skillet",
			() -> new SkilletBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN).setId(blockKey("skillet"))));
	public static final Supplier<Block> WOODEN_BASKET = BLOCKS.register("wooden_basket",
			() -> new BasketBlock(BlockBehaviour.Properties.of().strength(1.5F).sound(SoundType.WOOD).setId(blockKey("wooden_basket"))));
	public static final Supplier<Block> BAMBOO_BASKET = BLOCKS.register("bamboo_basket",
			() -> new BasketBlock(BlockBehaviour.Properties.of().strength(1.5F).sound(SoundType.BAMBOO_WOOD).setId(blockKey("bamboo_basket"))));
	public static final Supplier<Block> CUTTING_BOARD = BLOCKS.register("cutting_board",
			() -> new CuttingBoardBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_PLANKS).strength(2.0F).sound(SoundType.WOOD).setId(blockKey("cutting_board"))));

	/**
	 * Deprecated reference added for backwards compatibility. Use BAMBOO_BASKET instead.
	 */
	@Deprecated(forRemoval = true)
	public static final Supplier<Block> BASKET = BAMBOO_BASKET;

	// Crop Storage
	public static final Supplier<Block> CARROT_CRATE = BLOCKS.register("carrot_crate",
			() -> new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD).setId(blockKey("carrot_crate"))));
	public static final Supplier<Block> POTATO_CRATE = BLOCKS.register("potato_crate",
			() -> new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD).setId(blockKey("potato_crate"))));
	public static final Supplier<Block> BEETROOT_CRATE = BLOCKS.register("beetroot_crate",
			() -> new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD).setId(blockKey("beetroot_crate"))));
	public static final Supplier<Block> CABBAGE_CRATE = BLOCKS.register("cabbage_crate",
			() -> new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD).setId(blockKey("cabbage_crate"))));
	public static final Supplier<Block> TOMATO_CRATE = BLOCKS.register("tomato_crate",
			() -> new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD).setId(blockKey("tomato_crate"))));
	public static final Supplier<Block> ONION_CRATE = BLOCKS.register("onion_crate",
			() -> new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD).setId(blockKey("onion_crate"))));
	public static final Supplier<Block> RICE_BALE = BLOCKS.register("rice_bale",
			() -> new RiceBaleBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.HAY_BLOCK).setId(blockKey("rice_bale"))));
	public static final Supplier<Block> RICE_BAG = BLOCKS.register("rice_bag",
			() -> new Block(BlockBehaviour.Properties.ofLegacyCopy(Blocks.WOOL.white()).setId(blockKey("rice_bag"))));
	public static final Supplier<Block> STRAW_BALE = BLOCKS.register("straw_bale",
			() -> new StrawBaleBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.HAY_BLOCK).setId(blockKey("straw_bale"))));

	// Building
	public static final Supplier<Block> ROPE = BLOCKS.register("rope",
			() -> new RopeBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.CARPET.brown()).noCollision().noOcclusion().strength(0.2F).sound(SoundType.WOOL).setId(blockKey("rope"))));
	public static final Supplier<Block> SAFETY_NET = BLOCKS.register("safety_net",
			() -> new SafetyNetBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.CARPET.brown()).strength(0.2F).sound(SoundType.WOOL).setId(blockKey("safety_net"))));
	public static final Supplier<Block> ROPE_FENCE = BLOCKS.register("rope_fence",
			() -> new RopeFenceBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_FENCE).strength(1.0F).setId(blockKey("rope_fence"))));
	public static final Supplier<Block> ROPE_FENCE_GATE = BLOCKS.register("rope_fence_gate",
			() -> new RopeFenceGateBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_FENCE).strength(1.0F).setId(blockKey("rope_fence_gate"))));
	public static final Supplier<Block> OAK_CABINET = BLOCKS.register("oak_cabinet",
			() -> new CabinetBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.BARREL).setId(blockKey("oak_cabinet"))));
	public static final Supplier<Block> SPRUCE_CABINET = BLOCKS.register("spruce_cabinet",
			() -> new CabinetBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.BARREL).setId(blockKey("spruce_cabinet"))));
	public static final Supplier<Block> BIRCH_CABINET = BLOCKS.register("birch_cabinet",
			() -> new CabinetBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.BARREL).setId(blockKey("birch_cabinet"))));
	public static final Supplier<Block> JUNGLE_CABINET = BLOCKS.register("jungle_cabinet",
			() -> new CabinetBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.BARREL).setId(blockKey("jungle_cabinet"))));
	public static final Supplier<Block> ACACIA_CABINET = BLOCKS.register("acacia_cabinet",
			() -> new CabinetBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.BARREL).setId(blockKey("acacia_cabinet"))));
	public static final Supplier<Block> DARK_OAK_CABINET = BLOCKS.register("dark_oak_cabinet",
			() -> new CabinetBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.BARREL).setId(blockKey("dark_oak_cabinet"))));
	public static final Supplier<Block> MANGROVE_CABINET = BLOCKS.register("mangrove_cabinet",
			() -> new CabinetBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.BARREL).setId(blockKey("mangrove_cabinet"))));
	public static final Supplier<Block> CHERRY_CABINET = BLOCKS.register("cherry_cabinet",
			() -> new CabinetBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.BARREL).sound(SoundType.CHERRY_WOOD).setId(blockKey("cherry_cabinet"))));
	public static final Supplier<Block> BAMBOO_CABINET = BLOCKS.register("bamboo_cabinet",
			() -> new CabinetBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.BARREL).sound(SoundType.BAMBOO_WOOD).setId(blockKey("bamboo_cabinet"))));
	public static final Supplier<Block> CRIMSON_CABINET = BLOCKS.register("crimson_cabinet",
			() -> new CabinetBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.BARREL).sound(SoundType.NETHER_WOOD).setId(blockKey("crimson_cabinet"))));
	public static final Supplier<Block> WARPED_CABINET = BLOCKS.register("warped_cabinet",
			() -> new CabinetBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.BARREL).sound(SoundType.NETHER_WOOD).setId(blockKey("warped_cabinet"))));
	public static final Supplier<Block> CANVAS_RUG = BLOCKS.register("canvas_rug",
			() -> new CanvasRugBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.CARPET.white()).sound(SoundType.GRASS).strength(0.2F).setId(blockKey("canvas_rug"))));
	public static final Supplier<Block> TATAMI = BLOCKS.register("tatami",
			() -> new TatamiBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.WOOL.white()).setId(blockKey("tatami"))));
	public static final Supplier<Block> FULL_TATAMI_MAT = BLOCKS.register("full_tatami_mat",
			() -> new TatamiMatBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.WOOL.white()).strength(0.3F).setId(blockKey("full_tatami_mat"))));
	public static final Supplier<Block> HALF_TATAMI_MAT = BLOCKS.register("half_tatami_mat",
			() -> new TatamiHalfMatBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.WOOL.white()).strength(0.3F).pushReaction(PushReaction.DESTROY).setId(blockKey("half_tatami_mat"))));

	public static final Supplier<Block> CANVAS_SIGN = BLOCKS.register("canvas_sign",
			() -> new StandingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).setId(blockKey("canvas_sign")), null));
	public static final Supplier<Block> WHITE_CANVAS_SIGN = BLOCKS.register("white_canvas_sign",
			() -> new StandingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).setId(blockKey("white_canvas_sign")), DyeColor.WHITE));
	public static final Supplier<Block> ORANGE_CANVAS_SIGN = BLOCKS.register("orange_canvas_sign",
			() -> new StandingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).setId(blockKey("orange_canvas_sign")), DyeColor.ORANGE));
	public static final Supplier<Block> MAGENTA_CANVAS_SIGN = BLOCKS.register("magenta_canvas_sign",
			() -> new StandingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).setId(blockKey("magenta_canvas_sign")), DyeColor.MAGENTA));
	public static final Supplier<Block> LIGHT_BLUE_CANVAS_SIGN = BLOCKS.register("light_blue_canvas_sign",
			() -> new StandingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).setId(blockKey("light_blue_canvas_sign")), DyeColor.LIGHT_BLUE));
	public static final Supplier<Block> YELLOW_CANVAS_SIGN = BLOCKS.register("yellow_canvas_sign",
			() -> new StandingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).setId(blockKey("yellow_canvas_sign")), DyeColor.YELLOW));
	public static final Supplier<Block> LIME_CANVAS_SIGN = BLOCKS.register("lime_canvas_sign",
			() -> new StandingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).setId(blockKey("lime_canvas_sign")), DyeColor.LIME));
	public static final Supplier<Block> PINK_CANVAS_SIGN = BLOCKS.register("pink_canvas_sign",
			() -> new StandingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).setId(blockKey("pink_canvas_sign")), DyeColor.PINK));
	public static final Supplier<Block> GRAY_CANVAS_SIGN = BLOCKS.register("gray_canvas_sign",
			() -> new StandingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).setId(blockKey("gray_canvas_sign")), DyeColor.GRAY));
	public static final Supplier<Block> LIGHT_GRAY_CANVAS_SIGN = BLOCKS.register("light_gray_canvas_sign",
			() -> new StandingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).setId(blockKey("light_gray_canvas_sign")), DyeColor.LIGHT_GRAY));
	public static final Supplier<Block> CYAN_CANVAS_SIGN = BLOCKS.register("cyan_canvas_sign",
			() -> new StandingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).setId(blockKey("cyan_canvas_sign")), DyeColor.CYAN));
	public static final Supplier<Block> PURPLE_CANVAS_SIGN = BLOCKS.register("purple_canvas_sign",
			() -> new StandingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).setId(blockKey("purple_canvas_sign")), DyeColor.PURPLE));
	public static final Supplier<Block> BLUE_CANVAS_SIGN = BLOCKS.register("blue_canvas_sign",
			() -> new StandingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).setId(blockKey("blue_canvas_sign")), DyeColor.BLUE));
	public static final Supplier<Block> BROWN_CANVAS_SIGN = BLOCKS.register("brown_canvas_sign",
			() -> new StandingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).setId(blockKey("brown_canvas_sign")), DyeColor.BROWN));
	public static final Supplier<Block> GREEN_CANVAS_SIGN = BLOCKS.register("green_canvas_sign",
			() -> new StandingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).setId(blockKey("green_canvas_sign")), DyeColor.GREEN));
	public static final Supplier<Block> RED_CANVAS_SIGN = BLOCKS.register("red_canvas_sign",
			() -> new StandingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).setId(blockKey("red_canvas_sign")), DyeColor.RED));
	public static final Supplier<Block> BLACK_CANVAS_SIGN = BLOCKS.register("black_canvas_sign",
			() -> new StandingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).setId(blockKey("black_canvas_sign")), DyeColor.BLACK));

	public static final Supplier<Block> CANVAS_WALL_SIGN = BLOCKS.register("canvas_wall_sign",
			() -> new WallCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_SIGN).setId(blockKey("canvas_wall_sign")), null));
	public static final Supplier<Block> WHITE_CANVAS_WALL_SIGN = BLOCKS.register("white_canvas_wall_sign",
			() -> new WallCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_SIGN).setId(blockKey("white_canvas_wall_sign")), DyeColor.WHITE));
	public static final Supplier<Block> ORANGE_CANVAS_WALL_SIGN = BLOCKS.register("orange_canvas_wall_sign",
			() -> new WallCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_SIGN).setId(blockKey("orange_canvas_wall_sign")), DyeColor.ORANGE));
	public static final Supplier<Block> MAGENTA_CANVAS_WALL_SIGN = BLOCKS.register("magenta_canvas_wall_sign",
			() -> new WallCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_SIGN).setId(blockKey("magenta_canvas_wall_sign")), DyeColor.MAGENTA));
	public static final Supplier<Block> LIGHT_BLUE_CANVAS_WALL_SIGN = BLOCKS.register("light_blue_canvas_wall_sign",
			() -> new WallCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_SIGN).setId(blockKey("light_blue_canvas_wall_sign")), DyeColor.LIGHT_BLUE));
	public static final Supplier<Block> YELLOW_CANVAS_WALL_SIGN = BLOCKS.register("yellow_canvas_wall_sign",
			() -> new WallCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_SIGN).setId(blockKey("yellow_canvas_wall_sign")), DyeColor.YELLOW));
	public static final Supplier<Block> LIME_CANVAS_WALL_SIGN = BLOCKS.register("lime_canvas_wall_sign",
			() -> new WallCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_SIGN).setId(blockKey("lime_canvas_wall_sign")), DyeColor.LIME));
	public static final Supplier<Block> PINK_CANVAS_WALL_SIGN = BLOCKS.register("pink_canvas_wall_sign",
			() -> new WallCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_SIGN).setId(blockKey("pink_canvas_wall_sign")), DyeColor.PINK));
	public static final Supplier<Block> GRAY_CANVAS_WALL_SIGN = BLOCKS.register("gray_canvas_wall_sign",
			() -> new WallCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_SIGN).setId(blockKey("gray_canvas_wall_sign")), DyeColor.GRAY));
	public static final Supplier<Block> LIGHT_GRAY_CANVAS_WALL_SIGN = BLOCKS.register("light_gray_canvas_wall_sign",
			() -> new WallCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_SIGN).setId(blockKey("light_gray_canvas_wall_sign")), DyeColor.LIGHT_GRAY));
	public static final Supplier<Block> CYAN_CANVAS_WALL_SIGN = BLOCKS.register("cyan_canvas_wall_sign",
			() -> new WallCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_SIGN).setId(blockKey("cyan_canvas_wall_sign")), DyeColor.CYAN));
	public static final Supplier<Block> PURPLE_CANVAS_WALL_SIGN = BLOCKS.register("purple_canvas_wall_sign",
			() -> new WallCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_SIGN).setId(blockKey("purple_canvas_wall_sign")), DyeColor.PURPLE));
	public static final Supplier<Block> BLUE_CANVAS_WALL_SIGN = BLOCKS.register("blue_canvas_wall_sign",
			() -> new WallCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_SIGN).setId(blockKey("blue_canvas_wall_sign")), DyeColor.BLUE));
	public static final Supplier<Block> BROWN_CANVAS_WALL_SIGN = BLOCKS.register("brown_canvas_wall_sign",
			() -> new WallCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_SIGN).setId(blockKey("brown_canvas_wall_sign")), DyeColor.BROWN));
	public static final Supplier<Block> GREEN_CANVAS_WALL_SIGN = BLOCKS.register("green_canvas_wall_sign",
			() -> new WallCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_SIGN).setId(blockKey("green_canvas_wall_sign")), DyeColor.GREEN));
	public static final Supplier<Block> RED_CANVAS_WALL_SIGN = BLOCKS.register("red_canvas_wall_sign",
			() -> new WallCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_SIGN).setId(blockKey("red_canvas_wall_sign")), DyeColor.RED));
	public static final Supplier<Block> BLACK_CANVAS_WALL_SIGN = BLOCKS.register("black_canvas_wall_sign",
			() -> new WallCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_SIGN).setId(blockKey("black_canvas_wall_sign")), DyeColor.BLACK));

	public static final Supplier<Block> HANGING_CANVAS_SIGN = BLOCKS.register("hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN).setId(blockKey("hanging_canvas_sign")), null));
	public static final Supplier<Block> WHITE_HANGING_CANVAS_SIGN = BLOCKS.register("white_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN).setId(blockKey("white_hanging_canvas_sign")), DyeColor.WHITE));
	public static final Supplier<Block> ORANGE_HANGING_CANVAS_SIGN = BLOCKS.register("orange_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN).setId(blockKey("orange_hanging_canvas_sign")), DyeColor.ORANGE));
	public static final Supplier<Block> MAGENTA_HANGING_CANVAS_SIGN = BLOCKS.register("magenta_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN).setId(blockKey("magenta_hanging_canvas_sign")), DyeColor.MAGENTA));
	public static final Supplier<Block> LIGHT_BLUE_HANGING_CANVAS_SIGN = BLOCKS.register("light_blue_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN).setId(blockKey("light_blue_hanging_canvas_sign")), DyeColor.LIGHT_BLUE));
	public static final Supplier<Block> YELLOW_HANGING_CANVAS_SIGN = BLOCKS.register("yellow_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN).setId(blockKey("yellow_hanging_canvas_sign")), DyeColor.YELLOW));
	public static final Supplier<Block> LIME_HANGING_CANVAS_SIGN = BLOCKS.register("lime_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN).setId(blockKey("lime_hanging_canvas_sign")), DyeColor.LIME));
	public static final Supplier<Block> PINK_HANGING_CANVAS_SIGN = BLOCKS.register("pink_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN).setId(blockKey("pink_hanging_canvas_sign")), DyeColor.PINK));
	public static final Supplier<Block> GRAY_HANGING_CANVAS_SIGN = BLOCKS.register("gray_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN).setId(blockKey("gray_hanging_canvas_sign")), DyeColor.GRAY));
	public static final Supplier<Block> LIGHT_GRAY_HANGING_CANVAS_SIGN = BLOCKS.register("light_gray_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN).setId(blockKey("light_gray_hanging_canvas_sign")), DyeColor.LIGHT_GRAY));
	public static final Supplier<Block> CYAN_HANGING_CANVAS_SIGN = BLOCKS.register("cyan_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN).setId(blockKey("cyan_hanging_canvas_sign")), DyeColor.CYAN));
	public static final Supplier<Block> PURPLE_HANGING_CANVAS_SIGN = BLOCKS.register("purple_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN).setId(blockKey("purple_hanging_canvas_sign")), DyeColor.PURPLE));
	public static final Supplier<Block> BLUE_HANGING_CANVAS_SIGN = BLOCKS.register("blue_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN).setId(blockKey("blue_hanging_canvas_sign")), DyeColor.BLUE));
	public static final Supplier<Block> BROWN_HANGING_CANVAS_SIGN = BLOCKS.register("brown_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN).setId(blockKey("brown_hanging_canvas_sign")), DyeColor.BROWN));
	public static final Supplier<Block> GREEN_HANGING_CANVAS_SIGN = BLOCKS.register("green_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN).setId(blockKey("green_hanging_canvas_sign")), DyeColor.GREEN));
	public static final Supplier<Block> RED_HANGING_CANVAS_SIGN = BLOCKS.register("red_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN).setId(blockKey("red_hanging_canvas_sign")), DyeColor.RED));
	public static final Supplier<Block> BLACK_HANGING_CANVAS_SIGN = BLOCKS.register("black_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN).setId(blockKey("black_hanging_canvas_sign")), DyeColor.BLACK));

	public static final Supplier<Block> HANGING_CANVAS_WALL_SIGN = BLOCKS.register("wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).setId(blockKey("wall_hanging_canvas_sign")), null));
	public static final Supplier<Block> WHITE_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("white_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).setId(blockKey("white_wall_hanging_canvas_sign")), DyeColor.WHITE));
	public static final Supplier<Block> ORANGE_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("orange_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).setId(blockKey("orange_wall_hanging_canvas_sign")), DyeColor.ORANGE));
	public static final Supplier<Block> MAGENTA_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("magenta_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).setId(blockKey("magenta_wall_hanging_canvas_sign")), DyeColor.MAGENTA));
	public static final Supplier<Block> LIGHT_BLUE_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("light_blue_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).setId(blockKey("light_blue_wall_hanging_canvas_sign")), DyeColor.LIGHT_BLUE));
	public static final Supplier<Block> YELLOW_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("yellow_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).setId(blockKey("yellow_wall_hanging_canvas_sign")), DyeColor.YELLOW));
	public static final Supplier<Block> LIME_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("lime_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).setId(blockKey("lime_wall_hanging_canvas_sign")), DyeColor.LIME));
	public static final Supplier<Block> PINK_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("pink_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).setId(blockKey("pink_wall_hanging_canvas_sign")), DyeColor.PINK));
	public static final Supplier<Block> GRAY_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("gray_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).setId(blockKey("gray_wall_hanging_canvas_sign")), DyeColor.GRAY));
	public static final Supplier<Block> LIGHT_GRAY_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("light_gray_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).setId(blockKey("light_gray_wall_hanging_canvas_sign")), DyeColor.LIGHT_GRAY));
	public static final Supplier<Block> CYAN_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("cyan_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).setId(blockKey("cyan_wall_hanging_canvas_sign")), DyeColor.CYAN));
	public static final Supplier<Block> PURPLE_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("purple_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).setId(blockKey("purple_wall_hanging_canvas_sign")), DyeColor.PURPLE));
	public static final Supplier<Block> BLUE_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("blue_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).setId(blockKey("blue_wall_hanging_canvas_sign")), DyeColor.BLUE));
	public static final Supplier<Block> BROWN_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("brown_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).setId(blockKey("brown_wall_hanging_canvas_sign")), DyeColor.BROWN));
	public static final Supplier<Block> GREEN_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("green_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).setId(blockKey("green_wall_hanging_canvas_sign")), DyeColor.GREEN));
	public static final Supplier<Block> RED_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("red_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).setId(blockKey("red_wall_hanging_canvas_sign")), DyeColor.RED));
	public static final Supplier<Block> BLACK_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("black_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).setId(blockKey("black_wall_hanging_canvas_sign")), DyeColor.BLACK));

	// Composting
	public static final Supplier<Block> BROWN_MUSHROOM_COLONY = BLOCKS.register("brown_mushroom_colony",
			() -> new MushroomColonyBlock(Items.BROWN_MUSHROOM.builtInRegistryHolder(), BlockBehaviour.Properties.ofLegacyCopy(Blocks.BROWN_MUSHROOM).setId(blockKey("brown_mushroom_colony"))));
	public static final Supplier<Block> RED_MUSHROOM_COLONY = BLOCKS.register("red_mushroom_colony",
			() -> new MushroomColonyBlock(Items.RED_MUSHROOM.builtInRegistryHolder(), BlockBehaviour.Properties.ofLegacyCopy(Blocks.RED_MUSHROOM).setId(blockKey("red_mushroom_colony"))));
	public static final Supplier<Block> ORGANIC_COMPOST = BLOCKS.register("organic_compost",
			() -> new OrganicCompostBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.DIRT).strength(1.2F).sound(SoundType.CROP).setId(blockKey("organic_compost"))));
	public static final Supplier<Block> RICH_SOIL = BLOCKS.register("rich_soil",
			() -> new RichSoilBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.DIRT).randomTicks().setId(blockKey("rich_soil"))));
	public static final Supplier<Block> RICH_SOIL_FARMLAND = BLOCKS.register("rich_soil_farmland",
			() -> new RichSoilFarmlandBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.FARMLAND).setId(blockKey("rich_soil_farmland"))));

	// Pastries
	public static final Supplier<Block> APPLE_PIE = BLOCKS.register("apple_pie",
			() -> new PieBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.CAKE).setId(blockKey("apple_pie")), ModItems.APPLE_PIE_SLICE));
	public static final Supplier<Block> SWEET_BERRY_CHEESECAKE = BLOCKS.register("sweet_berry_cheesecake",
			() -> new PieBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.CAKE).setId(blockKey("sweet_berry_cheesecake")), ModItems.SWEET_BERRY_CHEESECAKE_SLICE));
	public static final Supplier<Block> CHOCOLATE_PIE = BLOCKS.register("chocolate_pie",
			() -> new PieBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.CAKE).setId(blockKey("chocolate_pie")), ModItems.CHOCOLATE_PIE_SLICE));
	public static final Supplier<Block> PUMPKIN_PIE = BLOCKS.register("pumpkin_pie",
			() -> new PieBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.CAKE).setId(blockKey("pumpkin_pie")), ModItems.PUMPKIN_PIE_SLICE)
			{
				@Override
				@SuppressWarnings("deprecation")
				protected @NotNull ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
					return new ItemStack(Items.PUMPKIN_PIE);
				}
			});

	// Wild Crops
	public static final Supplier<Block> SANDY_SHRUB = BLOCKS.register("sandy_shrub",
			() -> new SandyShrubBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.TALL_GRASS).setId(blockKey("sandy_shrub"))));

	public static final Supplier<Block> WILD_CABBAGES = BLOCKS.register("wild_cabbages",
			() -> new WildCropBlock(MobEffects.STRENGTH, 6, BlockBehaviour.Properties.ofLegacyCopy(Blocks.TALL_GRASS).setId(blockKey("wild_cabbages"))));
	public static final Supplier<Block> WILD_ONIONS = BLOCKS.register("wild_onions",
			() -> new WildCropBlock(MobEffects.FIRE_RESISTANCE, 6, BlockBehaviour.Properties.ofLegacyCopy(Blocks.TALL_GRASS).setId(blockKey("wild_onions"))));
	public static final Supplier<Block> WILD_TOMATOES = BLOCKS.register("wild_tomatoes",
			() -> new WildCropBlock(MobEffects.POISON, 10, BlockBehaviour.Properties.ofLegacyCopy(Blocks.TALL_GRASS).setId(blockKey("wild_tomatoes"))));
	public static final Supplier<Block> WILD_CARROTS = BLOCKS.register("wild_carrots",
			() -> new WildCropBlock(MobEffects.MINING_FATIGUE, 6, BlockBehaviour.Properties.ofLegacyCopy(Blocks.TALL_GRASS).setId(blockKey("wild_carrots"))));
	public static final Supplier<Block> WILD_POTATOES = BLOCKS.register("wild_potatoes",
			() -> new WildCropBlock(MobEffects.NAUSEA, 8, BlockBehaviour.Properties.ofLegacyCopy(Blocks.TALL_GRASS).setId(blockKey("wild_potatoes"))));
	public static final Supplier<Block> WILD_BEETROOTS = BLOCKS.register("wild_beetroots",
			() -> new WildCropBlock(MobEffects.WATER_BREATHING, 8, BlockBehaviour.Properties.ofLegacyCopy(Blocks.TALL_GRASS).setId(blockKey("wild_beetroots"))));
	public static final Supplier<Block> WILD_RICE = BLOCKS.register("wild_rice",
			() -> new WildRiceBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.TALL_GRASS).setId(blockKey("wild_rice"))));

	// Crops
	public static final Supplier<Block> CABBAGE_CROP = BLOCKS.register("cabbages",
			() -> new CabbageBlock(cropProperties("cabbages")));
	public static final Supplier<Block> ONION_CROP = BLOCKS.register("onions",
			() -> new OnionBlock(cropProperties("onions")));
	public static final Supplier<Block> BUDDING_TOMATO_CROP = BLOCKS.register("budding_tomatoes",
			() -> new BuddingTomatoBlock(cropProperties("budding_tomatoes")));
	public static final DeferredHolder<Block, TomatoBlock> TOMATO_CROP = BLOCKS.register("tomatoes",
			() -> new TomatoBlock(BlockBehaviour.Properties.of().noCollision().randomTicks().instabreak().sound(SoundType.CROP).setId(blockKey("tomatoes"))));
	public static final DeferredHolder<Block, HangingTomatoBlock> TOMATO_CROP_ON_ROPE = BLOCKS.register("tomatoes_on_rope",
			() -> new HangingTomatoBlock(BlockBehaviour.Properties.ofLegacyCopy(ModBlocks.TOMATO_CROP.get()).pushReaction(PushReaction.NORMAL).setId(blockKey("tomatoes_on_rope"))));
	public static final Supplier<Block> RICE_CROP = BLOCKS.register("rice",
			() -> new RiceBlock(cropProperties("rice").strength(0.2F)));
	public static final Supplier<Block> RICE_CROP_PANICLES = BLOCKS.register("rice_panicles",
			() -> new RicePaniclesBlock(cropProperties("rice_panicles")));

	// Feasts
	public static final Supplier<Block> ROAST_CHICKEN_BLOCK = BLOCKS.register("roast_chicken_block",
			() -> new RotatedFeastBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.CAKE).setId(blockKey("roast_chicken_block")), ModItems.ROAST_CHICKEN, true, BlockShapes.ROAST_CHICKEN_SHAPES, BlockShapes.TRAY_SHAPE));
	public static final Supplier<Block> STUFFED_PUMPKIN_BLOCK = BLOCKS.register("stuffed_pumpkin_block",
			() -> new FeastBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.PUMPKIN).setId(blockKey("stuffed_pumpkin_block")), ModItems.STUFFED_PUMPKIN, false, true));
	public static final Supplier<Block> HONEY_GLAZED_HAM_BLOCK = BLOCKS.register("honey_glazed_ham_block",
			() -> new RotatedFeastBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.CAKE).setId(blockKey("honey_glazed_ham_block")), ModItems.HONEY_GLAZED_HAM, true, BlockShapes.HONEY_GLAZED_HAM_SHAPES, BlockShapes.TRAY_SHAPE));
	public static final Supplier<Block> SHEPHERDS_PIE_BLOCK = BLOCKS.register("shepherds_pie_block",
			() -> new RotatedFeastBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.CAKE).setId(blockKey("shepherds_pie_block")), ModItems.SHEPHERDS_PIE, true, BlockShapes.SHEPHERDS_PIE_SHAPES, BlockShapes.TRAY_SHAPE));
	public static final Supplier<Block> GLEAMING_SALAD_BLOCK = BLOCKS.register("gleaming_salad_block",
			() -> new GleamingSaladBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_PLANKS).lightLevel(glowingFeastBlockEmission()).setId(blockKey("gleaming_salad_block")), ModItems.GLEAMING_SALAD, true));
	public static final Supplier<Block> RICE_ROLL_MEDLEY_BLOCK = BLOCKS.register("rice_roll_medley_block",
			() -> new RiceRollMedleyBlock(BlockBehaviour.Properties.ofLegacyCopy(Blocks.CAKE).setId(blockKey("rice_roll_medley_block"))));
}
