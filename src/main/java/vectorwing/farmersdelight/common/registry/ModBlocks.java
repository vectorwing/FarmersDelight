package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
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
import java.util.Objects;

public class ModBlocks
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, FarmersDelight.MODID);
	private static final ThreadLocal<ResourceKey<Block>> CURRENT_BLOCK_ID = new ThreadLocal<>();

	private static <B extends Block> DeferredHolder<Block, B> register(String name, Supplier<? extends B> supplier) {
		return BLOCKS.register(name, id -> {
			CURRENT_BLOCK_ID.set(ResourceKey.create(Registries.BLOCK, id));
			try {
				return supplier.get();
			} finally {
				CURRENT_BLOCK_ID.remove();
			}
		});
	}

	private static BlockBehaviour.Properties blockProperties(BlockBehaviour.Properties properties) {
		return properties.setId(Objects.requireNonNull(CURRENT_BLOCK_ID.get(), "Block registration id not set"));
	}

	private static BlockBehaviour.Properties cropProperties() {
		return blockProperties(Block.Properties.of()
				.mapColor(MapColor.PLANT)
				.noCollision()
				.randomTicks()
				.instabreak()
				.sound(SoundType.CROP)
				.pushReaction(PushReaction.DESTROY));
	}

	private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
		return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
	}

	private static ToIntFunction<BlockState> glowingFeastBlockEmission() {
		return (state) -> state.getValue(FeastBlock.SERVINGS) * 3;
	}

	// Workstations
	public static final Supplier<Block> STOVE = register("stove",
			() -> new StoveBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.BRICKS).lightLevel(litBlockEmission(13)))));
	public static final Supplier<Block> COOKING_POT = register("cooking_pot",
			() -> new CookingPotBlock(blockProperties(Block.Properties.of().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN))));
	public static final Supplier<Block> SKILLET = register("skillet",
			() -> new SkilletBlock(blockProperties(Block.Properties.of().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN))));
	public static final Supplier<Block> WOODEN_BASKET = register("wooden_basket",
			() -> new BasketBlock(blockProperties(Block.Properties.of().strength(1.5F).sound(SoundType.WOOD))));
	public static final Supplier<Block> BAMBOO_BASKET = register("bamboo_basket",
			() -> new BasketBlock(blockProperties(Block.Properties.of().strength(1.5F).sound(SoundType.BAMBOO_WOOD))));
	public static final Supplier<Block> CUTTING_BOARD = register("cutting_board",
			() -> new CuttingBoardBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F).sound(SoundType.WOOD))));

	/**
	 * Deprecated reference added for backwards compatibility. Use BAMBOO_BASKET instead.
	 */
	@Deprecated(forRemoval = true)
	public static final Supplier<Block> BASKET = BAMBOO_BASKET;

	// Crop Storage
	public static final Supplier<Block> CARROT_CRATE = register("carrot_crate",
			() -> new Block(blockProperties(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD))));
	public static final Supplier<Block> POTATO_CRATE = register("potato_crate",
			() -> new Block(blockProperties(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD))));
	public static final Supplier<Block> BEETROOT_CRATE = register("beetroot_crate",
			() -> new Block(blockProperties(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD))));
	public static final Supplier<Block> CABBAGE_CRATE = register("cabbage_crate",
			() -> new Block(blockProperties(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD))));
	public static final Supplier<Block> TOMATO_CRATE = register("tomato_crate",
			() -> new Block(blockProperties(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD))));
	public static final Supplier<Block> ONION_CRATE = register("onion_crate",
			() -> new Block(blockProperties(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD))));
	public static final Supplier<Block> RICE_BALE = register("rice_bale",
			() -> new RiceBaleBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.HAY_BLOCK))));
	public static final Supplier<Block> RICE_BAG = register("rice_bag",
			() -> new Block(blockProperties(Block.Properties.ofFullCopy(Blocks.WHITE_WOOL))));
	public static final Supplier<Block> STRAW_BALE = register("straw_bale",
			() -> new StrawBaleBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.HAY_BLOCK))));

	// Building
	public static final Supplier<Block> ROPE = register("rope",
			() -> new RopeBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.BROWN_CARPET).noCollision().noOcclusion().strength(0.2F).sound(SoundType.WOOL))));
	public static final Supplier<Block> SAFETY_NET = register("safety_net",
			() -> new SafetyNetBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.BROWN_CARPET).strength(0.2F).sound(SoundType.WOOL))));
	public static final Supplier<Block> ROPE_FENCE = register("rope_fence",
			() -> new RopeFenceBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.OAK_FENCE).strength(1.0F))));
	public static final Supplier<Block> ROPE_FENCE_GATE = register("rope_fence_gate",
			() -> new RopeFenceGateBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.OAK_FENCE).strength(1.0F))));
	public static final Supplier<Block> OAK_CABINET = register("oak_cabinet",
			() -> new CabinetBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.BARREL))));
	public static final Supplier<Block> SPRUCE_CABINET = register("spruce_cabinet",
			() -> new CabinetBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.BARREL))));
	public static final Supplier<Block> BIRCH_CABINET = register("birch_cabinet",
			() -> new CabinetBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.BARREL))));
	public static final Supplier<Block> JUNGLE_CABINET = register("jungle_cabinet",
			() -> new CabinetBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.BARREL))));
	public static final Supplier<Block> ACACIA_CABINET = register("acacia_cabinet",
			() -> new CabinetBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.BARREL))));
	public static final Supplier<Block> DARK_OAK_CABINET = register("dark_oak_cabinet",
			() -> new CabinetBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.BARREL))));
	public static final Supplier<Block> MANGROVE_CABINET = register("mangrove_cabinet",
			() -> new CabinetBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.BARREL))));
	public static final Supplier<Block> CHERRY_CABINET = register("cherry_cabinet",
			() -> new CabinetBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.CHERRY_WOOD))));
	public static final Supplier<Block> BAMBOO_CABINET = register("bamboo_cabinet",
			() -> new CabinetBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.BAMBOO_WOOD))));
	public static final Supplier<Block> CRIMSON_CABINET = register("crimson_cabinet",
			() -> new CabinetBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.NETHER_WOOD))));
	public static final Supplier<Block> WARPED_CABINET = register("warped_cabinet",
			() -> new CabinetBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.NETHER_WOOD))));
	public static final Supplier<Block> CANVAS_RUG = register("canvas_rug",
			() -> new CanvasRugBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.WHITE_CARPET).sound(SoundType.GRASS).strength(0.2F))));
	public static final Supplier<Block> TATAMI = register("tatami",
			() -> new TatamiBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.WHITE_WOOL))));
	public static final Supplier<Block> FULL_TATAMI_MAT = register("full_tatami_mat",
			() -> new TatamiMatBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.WHITE_WOOL).strength(0.3F))));
	public static final Supplier<Block> HALF_TATAMI_MAT = register("half_tatami_mat",
			() -> new TatamiHalfMatBlock(blockProperties(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).strength(0.3F).pushReaction(PushReaction.DESTROY))));

	public static final Supplier<Block> CANVAS_SIGN = register("canvas_sign",
			() -> new StandingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), null));
	public static final Supplier<Block> WHITE_CANVAS_SIGN = register("white_canvas_sign",
			() -> new StandingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.WHITE));
	public static final Supplier<Block> ORANGE_CANVAS_SIGN = register("orange_canvas_sign",
			() -> new StandingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.ORANGE));
	public static final Supplier<Block> MAGENTA_CANVAS_SIGN = register("magenta_canvas_sign",
			() -> new StandingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.MAGENTA));
	public static final Supplier<Block> LIGHT_BLUE_CANVAS_SIGN = register("light_blue_canvas_sign",
			() -> new StandingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.LIGHT_BLUE));
	public static final Supplier<Block> YELLOW_CANVAS_SIGN = register("yellow_canvas_sign",
			() -> new StandingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.YELLOW));
	public static final Supplier<Block> LIME_CANVAS_SIGN = register("lime_canvas_sign",
			() -> new StandingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.LIME));
	public static final Supplier<Block> PINK_CANVAS_SIGN = register("pink_canvas_sign",
			() -> new StandingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.PINK));
	public static final Supplier<Block> GRAY_CANVAS_SIGN = register("gray_canvas_sign",
			() -> new StandingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.GRAY));
	public static final Supplier<Block> LIGHT_GRAY_CANVAS_SIGN = register("light_gray_canvas_sign",
			() -> new StandingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.LIGHT_GRAY));
	public static final Supplier<Block> CYAN_CANVAS_SIGN = register("cyan_canvas_sign",
			() -> new StandingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.CYAN));
	public static final Supplier<Block> PURPLE_CANVAS_SIGN = register("purple_canvas_sign",
			() -> new StandingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.PURPLE));
	public static final Supplier<Block> BLUE_CANVAS_SIGN = register("blue_canvas_sign",
			() -> new StandingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.BLUE));
	public static final Supplier<Block> BROWN_CANVAS_SIGN = register("brown_canvas_sign",
			() -> new StandingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.BROWN));
	public static final Supplier<Block> GREEN_CANVAS_SIGN = register("green_canvas_sign",
			() -> new StandingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.GREEN));
	public static final Supplier<Block> RED_CANVAS_SIGN = register("red_canvas_sign",
			() -> new StandingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.RED));
	public static final Supplier<Block> BLACK_CANVAS_SIGN = register("black_canvas_sign",
			() -> new StandingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.BLACK));

	public static final Supplier<Block> CANVAS_WALL_SIGN = register("canvas_wall_sign",
			() -> new WallCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(CANVAS_SIGN.get().getLootTable())), null));
	public static final Supplier<Block> WHITE_CANVAS_WALL_SIGN = register("white_canvas_wall_sign",
			() -> new WallCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(WHITE_CANVAS_SIGN.get().getLootTable())), DyeColor.WHITE));
	public static final Supplier<Block> ORANGE_CANVAS_WALL_SIGN = register("orange_canvas_wall_sign",
			() -> new WallCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(ORANGE_CANVAS_SIGN.get().getLootTable())), DyeColor.ORANGE));
	public static final Supplier<Block> MAGENTA_CANVAS_WALL_SIGN = register("magenta_canvas_wall_sign",
			() -> new WallCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(MAGENTA_CANVAS_SIGN.get().getLootTable())), DyeColor.MAGENTA));
	public static final Supplier<Block> LIGHT_BLUE_CANVAS_WALL_SIGN = register("light_blue_canvas_wall_sign",
			() -> new WallCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(LIGHT_BLUE_CANVAS_SIGN.get().getLootTable())), DyeColor.LIGHT_BLUE));
	public static final Supplier<Block> YELLOW_CANVAS_WALL_SIGN = register("yellow_canvas_wall_sign",
			() -> new WallCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(YELLOW_CANVAS_SIGN.get().getLootTable())), DyeColor.YELLOW));
	public static final Supplier<Block> LIME_CANVAS_WALL_SIGN = register("lime_canvas_wall_sign",
			() -> new WallCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(LIME_CANVAS_SIGN.get().getLootTable())), DyeColor.LIME));
	public static final Supplier<Block> PINK_CANVAS_WALL_SIGN = register("pink_canvas_wall_sign",
			() -> new WallCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(PINK_CANVAS_SIGN.get().getLootTable())), DyeColor.PINK));
	public static final Supplier<Block> GRAY_CANVAS_WALL_SIGN = register("gray_canvas_wall_sign",
			() -> new WallCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(GRAY_CANVAS_SIGN.get().getLootTable())), DyeColor.GRAY));
	public static final Supplier<Block> LIGHT_GRAY_CANVAS_WALL_SIGN = register("light_gray_canvas_wall_sign",
			() -> new WallCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(LIGHT_GRAY_CANVAS_SIGN.get().getLootTable())), DyeColor.LIGHT_GRAY));
	public static final Supplier<Block> CYAN_CANVAS_WALL_SIGN = register("cyan_canvas_wall_sign",
			() -> new WallCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(CYAN_CANVAS_SIGN.get().getLootTable())), DyeColor.CYAN));
	public static final Supplier<Block> PURPLE_CANVAS_WALL_SIGN = register("purple_canvas_wall_sign",
			() -> new WallCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(PURPLE_CANVAS_SIGN.get().getLootTable())), DyeColor.PURPLE));
	public static final Supplier<Block> BLUE_CANVAS_WALL_SIGN = register("blue_canvas_wall_sign",
			() -> new WallCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(BLUE_CANVAS_SIGN.get().getLootTable())), DyeColor.BLUE));
	public static final Supplier<Block> BROWN_CANVAS_WALL_SIGN = register("brown_canvas_wall_sign",
			() -> new WallCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(BROWN_CANVAS_SIGN.get().getLootTable())), DyeColor.BROWN));
	public static final Supplier<Block> GREEN_CANVAS_WALL_SIGN = register("green_canvas_wall_sign",
			() -> new WallCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(GREEN_CANVAS_SIGN.get().getLootTable())), DyeColor.GREEN));
	public static final Supplier<Block> RED_CANVAS_WALL_SIGN = register("red_canvas_wall_sign",
			() -> new WallCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(RED_CANVAS_SIGN.get().getLootTable())), DyeColor.RED));
	public static final Supplier<Block> BLACK_CANVAS_WALL_SIGN = register("black_canvas_wall_sign",
			() -> new WallCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(BLACK_CANVAS_SIGN.get().getLootTable())), DyeColor.BLACK));

	public static final Supplier<Block> HANGING_CANVAS_SIGN = register("hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(blockProperties(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN)), null));
	public static final Supplier<Block> WHITE_HANGING_CANVAS_SIGN = register("white_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(blockProperties(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN)), DyeColor.WHITE));
	public static final Supplier<Block> ORANGE_HANGING_CANVAS_SIGN = register("orange_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(blockProperties(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN)), DyeColor.ORANGE));
	public static final Supplier<Block> MAGENTA_HANGING_CANVAS_SIGN = register("magenta_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(blockProperties(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN)), DyeColor.MAGENTA));
	public static final Supplier<Block> LIGHT_BLUE_HANGING_CANVAS_SIGN = register("light_blue_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(blockProperties(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN)), DyeColor.LIGHT_BLUE));
	public static final Supplier<Block> YELLOW_HANGING_CANVAS_SIGN = register("yellow_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(blockProperties(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN)), DyeColor.YELLOW));
	public static final Supplier<Block> LIME_HANGING_CANVAS_SIGN = register("lime_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(blockProperties(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN)), DyeColor.LIME));
	public static final Supplier<Block> PINK_HANGING_CANVAS_SIGN = register("pink_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(blockProperties(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN)), DyeColor.PINK));
	public static final Supplier<Block> GRAY_HANGING_CANVAS_SIGN = register("gray_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(blockProperties(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN)), DyeColor.GRAY));
	public static final Supplier<Block> LIGHT_GRAY_HANGING_CANVAS_SIGN = register("light_gray_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(blockProperties(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN)), DyeColor.LIGHT_GRAY));
	public static final Supplier<Block> CYAN_HANGING_CANVAS_SIGN = register("cyan_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(blockProperties(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN)), DyeColor.CYAN));
	public static final Supplier<Block> PURPLE_HANGING_CANVAS_SIGN = register("purple_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(blockProperties(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN)), DyeColor.PURPLE));
	public static final Supplier<Block> BLUE_HANGING_CANVAS_SIGN = register("blue_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(blockProperties(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN)), DyeColor.BLUE));
	public static final Supplier<Block> BROWN_HANGING_CANVAS_SIGN = register("brown_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(blockProperties(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN)), DyeColor.BROWN));
	public static final Supplier<Block> GREEN_HANGING_CANVAS_SIGN = register("green_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(blockProperties(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN)), DyeColor.GREEN));
	public static final Supplier<Block> RED_HANGING_CANVAS_SIGN = register("red_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(blockProperties(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN)), DyeColor.RED));
	public static final Supplier<Block> BLACK_HANGING_CANVAS_SIGN = register("black_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(blockProperties(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN)), DyeColor.BLACK));

	public static final Supplier<Block> HANGING_CANVAS_WALL_SIGN = register("wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(HANGING_CANVAS_SIGN.get().getLootTable())), null));
	public static final Supplier<Block> WHITE_HANGING_CANVAS_WALL_SIGN = register("white_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(WHITE_HANGING_CANVAS_SIGN.get().getLootTable())), DyeColor.WHITE));
	public static final Supplier<Block> ORANGE_HANGING_CANVAS_WALL_SIGN = register("orange_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(ORANGE_HANGING_CANVAS_SIGN.get().getLootTable())), DyeColor.ORANGE));
	public static final Supplier<Block> MAGENTA_HANGING_CANVAS_WALL_SIGN = register("magenta_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(MAGENTA_HANGING_CANVAS_SIGN.get().getLootTable())), DyeColor.MAGENTA));
	public static final Supplier<Block> LIGHT_BLUE_HANGING_CANVAS_WALL_SIGN = register("light_blue_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(LIGHT_BLUE_HANGING_CANVAS_SIGN.get().getLootTable())), DyeColor.LIGHT_BLUE));
	public static final Supplier<Block> YELLOW_HANGING_CANVAS_WALL_SIGN = register("yellow_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(YELLOW_HANGING_CANVAS_SIGN.get().getLootTable())), DyeColor.YELLOW));
	public static final Supplier<Block> LIME_HANGING_CANVAS_WALL_SIGN = register("lime_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(LIME_HANGING_CANVAS_SIGN.get().getLootTable())), DyeColor.LIME));
	public static final Supplier<Block> PINK_HANGING_CANVAS_WALL_SIGN = register("pink_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(PINK_HANGING_CANVAS_SIGN.get().getLootTable())), DyeColor.PINK));
	public static final Supplier<Block> GRAY_HANGING_CANVAS_WALL_SIGN = register("gray_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(GRAY_HANGING_CANVAS_SIGN.get().getLootTable())), DyeColor.GRAY));
	public static final Supplier<Block> LIGHT_GRAY_HANGING_CANVAS_WALL_SIGN = register("light_gray_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(LIGHT_GRAY_HANGING_CANVAS_SIGN.get().getLootTable())), DyeColor.LIGHT_GRAY));
	public static final Supplier<Block> CYAN_HANGING_CANVAS_WALL_SIGN = register("cyan_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(CYAN_HANGING_CANVAS_SIGN.get().getLootTable())), DyeColor.CYAN));
	public static final Supplier<Block> PURPLE_HANGING_CANVAS_WALL_SIGN = register("purple_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(PURPLE_HANGING_CANVAS_SIGN.get().getLootTable())), DyeColor.PURPLE));
	public static final Supplier<Block> BLUE_HANGING_CANVAS_WALL_SIGN = register("blue_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(BLUE_HANGING_CANVAS_SIGN.get().getLootTable())), DyeColor.BLUE));
	public static final Supplier<Block> BROWN_HANGING_CANVAS_WALL_SIGN = register("brown_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(BROWN_HANGING_CANVAS_SIGN.get().getLootTable())), DyeColor.BROWN));
	public static final Supplier<Block> GREEN_HANGING_CANVAS_WALL_SIGN = register("green_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(GREEN_HANGING_CANVAS_SIGN.get().getLootTable())), DyeColor.GREEN));
	public static final Supplier<Block> RED_HANGING_CANVAS_WALL_SIGN = register("red_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(RED_HANGING_CANVAS_SIGN.get().getLootTable())), DyeColor.RED));
	public static final Supplier<Block> BLACK_HANGING_CANVAS_WALL_SIGN = register("black_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(BLACK_HANGING_CANVAS_SIGN.get().getLootTable())), DyeColor.BLACK));

	// Composting
	public static final Supplier<Block> BROWN_MUSHROOM_COLONY = register("brown_mushroom_colony",
			() -> new MushroomColonyBlock(Items.BROWN_MUSHROOM.builtInRegistryHolder(), blockProperties(Block.Properties.ofFullCopy(Blocks.BROWN_MUSHROOM))));
	public static final Supplier<Block> RED_MUSHROOM_COLONY = register("red_mushroom_colony",
			() -> new MushroomColonyBlock(Items.RED_MUSHROOM.builtInRegistryHolder(), blockProperties(Block.Properties.ofFullCopy(Blocks.RED_MUSHROOM))));
	public static final Supplier<Block> ORGANIC_COMPOST = register("organic_compost",
			() -> new OrganicCompostBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.DIRT).strength(1.2F).sound(SoundType.CROP))));
	public static final Supplier<Block> RICH_SOIL = register("rich_soil",
			() -> new RichSoilBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.DIRT).randomTicks())));
	public static final Supplier<Block> RICH_SOIL_FARMLAND = register("rich_soil_farmland",
			() -> new RichSoilFarmlandBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.FARMLAND))));

	// Pastries
	public static final Supplier<Block> APPLE_PIE = register("apple_pie",
			() -> new PieBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.CAKE)), ModItems.APPLE_PIE_SLICE));
	public static final Supplier<Block> SWEET_BERRY_CHEESECAKE = register("sweet_berry_cheesecake",
			() -> new PieBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.CAKE)), ModItems.SWEET_BERRY_CHEESECAKE_SLICE));
	public static final Supplier<Block> CHOCOLATE_PIE = register("chocolate_pie",
			() -> new PieBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.CAKE)), ModItems.CHOCOLATE_PIE_SLICE));
	public static final Supplier<Block> PUMPKIN_PIE = register("pumpkin_pie",
			() -> new PieBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.CAKE)), ModItems.PUMPKIN_PIE_SLICE)
			{
				@Override
				@SuppressWarnings("deprecation")
				public @NotNull ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
					return new ItemStack(Items.PUMPKIN_PIE);
				}
			});

	// Wild Crops
	public static final Supplier<Block> SANDY_SHRUB = register("sandy_shrub",
			() -> new SandyShrubBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.TALL_GRASS))));

	public static final Supplier<Block> WILD_CABBAGES = register("wild_cabbages",
			() -> new WildCropBlock(MobEffects.STRENGTH, 6, blockProperties(Block.Properties.ofFullCopy(Blocks.TALL_GRASS))));
	public static final Supplier<Block> WILD_ONIONS = register("wild_onions",
			() -> new WildCropBlock(MobEffects.FIRE_RESISTANCE, 6, blockProperties(Block.Properties.ofFullCopy(Blocks.TALL_GRASS))));
	public static final Supplier<Block> WILD_TOMATOES = register("wild_tomatoes",
			() -> new WildCropBlock(MobEffects.POISON, 10, blockProperties(Block.Properties.ofFullCopy(Blocks.TALL_GRASS))));
	public static final Supplier<Block> WILD_CARROTS = register("wild_carrots",
			() -> new WildCropBlock(MobEffects.MINING_FATIGUE, 6, blockProperties(Block.Properties.ofFullCopy(Blocks.TALL_GRASS))));
	public static final Supplier<Block> WILD_POTATOES = register("wild_potatoes",
			() -> new WildCropBlock(MobEffects.NAUSEA, 8, blockProperties(Block.Properties.ofFullCopy(Blocks.TALL_GRASS))));
	public static final Supplier<Block> WILD_BEETROOTS = register("wild_beetroots",
			() -> new WildCropBlock(MobEffects.WATER_BREATHING, 8, blockProperties(Block.Properties.ofFullCopy(Blocks.TALL_GRASS))));
	public static final Supplier<Block> WILD_RICE = register("wild_rice",
			() -> new WildRiceBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.TALL_GRASS))));

	// Crops
	public static final Supplier<Block> CABBAGE_CROP = register("cabbages",
			() -> new CabbageBlock(cropProperties()));
	public static final Supplier<Block> ONION_CROP = register("onions",
			() -> new OnionBlock(cropProperties()));
	public static final Supplier<Block> BUDDING_TOMATO_CROP = register("budding_tomatoes",
			() -> new BuddingTomatoBlock(cropProperties()));
	public static final DeferredHolder<Block, TomatoBlock> TOMATO_CROP = register("tomatoes",
			() -> new TomatoBlock(blockProperties(Block.Properties.of().noCollision().randomTicks().instabreak().sound(SoundType.CROP))));
	public static final DeferredHolder<Block, HangingTomatoBlock> TOMATO_CROP_ON_ROPE = register("tomatoes_on_rope",
			() -> new HangingTomatoBlock(blockProperties(Block.Properties.ofFullCopy(ModBlocks.TOMATO_CROP.get()).pushReaction(PushReaction.NORMAL))));
	public static final Supplier<Block> RICE_CROP = register("rice",
			() -> new RiceBlock(cropProperties().strength(0.2F)));
	public static final Supplier<Block> RICE_CROP_PANICLES = register("rice_panicles",
			() -> new RicePaniclesBlock(cropProperties()));

	// Feasts
	public static final Supplier<Block> ROAST_CHICKEN_BLOCK = register("roast_chicken_block",
			() -> new RotatedFeastBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.CAKE)), ModItems.ROAST_CHICKEN, true, BlockShapes.ROAST_CHICKEN_SHAPES, BlockShapes.TRAY_SHAPE));
	public static final Supplier<Block> STUFFED_PUMPKIN_BLOCK = register("stuffed_pumpkin_block",
			() -> new FeastBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.PUMPKIN)), ModItems.STUFFED_PUMPKIN, false, true));
	public static final Supplier<Block> HONEY_GLAZED_HAM_BLOCK = register("honey_glazed_ham_block",
			() -> new RotatedFeastBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.CAKE)), ModItems.HONEY_GLAZED_HAM, true, BlockShapes.HONEY_GLAZED_HAM_SHAPES, BlockShapes.TRAY_SHAPE));
	public static final Supplier<Block> SHEPHERDS_PIE_BLOCK = register("shepherds_pie_block",
			() -> new RotatedFeastBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.CAKE)), ModItems.SHEPHERDS_PIE, true, BlockShapes.SHEPHERDS_PIE_SHAPES, BlockShapes.TRAY_SHAPE));
	public static final Supplier<Block> GLEAMING_SALAD_BLOCK = register("gleaming_salad_block",
			() -> new GleamingSaladBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).lightLevel(glowingFeastBlockEmission())), ModItems.GLEAMING_SALAD, true));
	public static final Supplier<Block> RICE_ROLL_MEDLEY_BLOCK = register("rice_roll_medley_block",
			() -> new RiceRollMedleyBlock(blockProperties(Block.Properties.ofFullCopy(Blocks.CAKE))));
}
