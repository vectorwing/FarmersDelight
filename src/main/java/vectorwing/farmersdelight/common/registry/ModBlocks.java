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

import java.util.function.Function;
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

	private static <T extends Block> Supplier<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> factory, BlockBehaviour.Properties properties) {
		properties.setId(ResourceKey.create(Registries.BLOCK, FarmersDelight.id(name)));
		return BLOCKS.register(name, () -> factory.apply(properties));
	}

	// Variant for blocks whose properties reference another (not-yet-bound) block holder; the properties are built lazily at registration time.
	private static <T extends Block> Supplier<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> factory, Supplier<BlockBehaviour.Properties> properties) {
		ResourceKey<Block> id = ResourceKey.create(Registries.BLOCK, FarmersDelight.id(name));
		return BLOCKS.register(name, () -> factory.apply(properties.get().setId(id)));
	}

	// Workstations
	public static final Supplier<Block> STOVE = registerBlock("stove",
		StoveBlock::new, Block.Properties.ofFullCopy(Blocks.BRICKS).lightLevel(litBlockEmission(13)));
	public static final Supplier<Block> COOKING_POT = registerBlock("cooking_pot",
		CookingPotBlock::new, Block.Properties.of().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN));
	public static final Supplier<Block> SKILLET = registerBlock("skillet",
		SkilletBlock::new, Block.Properties.of().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN));
	public static final Supplier<Block> WOODEN_BASKET = registerBlock("wooden_basket",
		BasketBlock::new, Block.Properties.of().strength(1.5F).sound(SoundType.WOOD));
	public static final Supplier<Block> BAMBOO_BASKET = registerBlock("bamboo_basket",
		BasketBlock::new, Block.Properties.of().strength(1.5F).sound(SoundType.BAMBOO_WOOD));
	public static final Supplier<Block> CUTTING_BOARD = registerBlock("cutting_board",
		CuttingBoardBlock::new, Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F).sound(SoundType.WOOD));

	// Crop Storage
	public static final Supplier<Block> CARROT_CRATE = registerBlock("carrot_crate",
		Block::new, Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD));
	public static final Supplier<Block> POTATO_CRATE = registerBlock("potato_crate",
		Block::new, Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD));
	public static final Supplier<Block> BEETROOT_CRATE = registerBlock("beetroot_crate",
		Block::new, Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD));
	public static final Supplier<Block> CABBAGE_CRATE = registerBlock("cabbage_crate",
		Block::new, Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD));
	public static final Supplier<Block> TOMATO_CRATE = registerBlock("tomato_crate",
		Block::new, Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD));
	public static final Supplier<Block> ONION_CRATE = registerBlock("onion_crate",
		Block::new, Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD));
	public static final Supplier<Block> RICE_BALE = registerBlock("rice_bale",
		RiceBaleBlock::new, Block.Properties.ofFullCopy(Blocks.HAY_BLOCK));
	public static final Supplier<Block> RICE_BAG = registerBlock("rice_bag",
		Block::new, Block.Properties.ofFullCopy(Blocks.WHITE_WOOL));
	public static final Supplier<Block> STRAW_BALE = registerBlock("straw_bale",
		StrawBaleBlock::new, Block.Properties.ofFullCopy(Blocks.HAY_BLOCK));

	// Building
	public static final Supplier<Block> ROPE = registerBlock("rope",
		RopeBlock::new, Block.Properties.ofFullCopy(Blocks.BROWN_CARPET).noCollision().noOcclusion().strength(0.2F).sound(SoundType.WOOL));
	public static final Supplier<Block> SAFETY_NET = registerBlock("safety_net",
		SafetyNetBlock::new, Block.Properties.ofFullCopy(Blocks.BROWN_CARPET).strength(0.2F).sound(SoundType.WOOL));
	public static final Supplier<Block> ROPE_FENCE = registerBlock("rope_fence",
		RopeFenceBlock::new, Block.Properties.ofFullCopy(Blocks.OAK_FENCE).strength(1.0F));
	public static final Supplier<Block> ROPE_FENCE_GATE = registerBlock("rope_fence_gate",
		RopeFenceGateBlock::new, Block.Properties.ofFullCopy(Blocks.OAK_FENCE).strength(1.0F));
	public static final Supplier<Block> OAK_CABINET = registerBlock("oak_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL));
	public static final Supplier<Block> SPRUCE_CABINET = registerBlock("spruce_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL));
	public static final Supplier<Block> BIRCH_CABINET = registerBlock("birch_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL));
	public static final Supplier<Block> JUNGLE_CABINET = registerBlock("jungle_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL));
	public static final Supplier<Block> ACACIA_CABINET = registerBlock("acacia_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL));
	public static final Supplier<Block> DARK_OAK_CABINET = registerBlock("dark_oak_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL));
	public static final Supplier<Block> MANGROVE_CABINET = registerBlock("mangrove_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL));
	public static final Supplier<Block> CHERRY_CABINET = registerBlock("cherry_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.CHERRY_WOOD));
	public static final Supplier<Block> BAMBOO_CABINET = registerBlock("bamboo_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.BAMBOO_WOOD));
	public static final Supplier<Block> CRIMSON_CABINET = registerBlock("crimson_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.NETHER_WOOD));
	public static final Supplier<Block> WARPED_CABINET = registerBlock("warped_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.NETHER_WOOD));
	public static final Supplier<Block> CANVAS_RUG = registerBlock("canvas_rug",
		CanvasRugBlock::new, Block.Properties.ofFullCopy(Blocks.WHITE_CARPET).sound(SoundType.GRASS).strength(0.2F));
	public static final Supplier<Block> TATAMI = registerBlock("tatami",
		TatamiBlock::new, Block.Properties.ofFullCopy(Blocks.WHITE_WOOL));
	public static final Supplier<Block> FULL_TATAMI_MAT = registerBlock("full_tatami_mat",
		TatamiMatBlock::new, Block.Properties.ofFullCopy(Blocks.WHITE_WOOL).strength(0.3F));
	public static final Supplier<Block> HALF_TATAMI_MAT = registerBlock("half_tatami_mat",
		TatamiHalfMatBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).strength(0.3F).pushReaction(PushReaction.DESTROY));

	public static final Supplier<Block> CANVAS_SIGN = registerBlock("canvas_sign",
		p -> new StandingCanvasSignBlock(p, null), Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN));
	public static final Supplier<Block> WHITE_CANVAS_SIGN = registerBlock("white_canvas_sign",
		p -> new StandingCanvasSignBlock(p, DyeColor.WHITE), Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN));
	public static final Supplier<Block> ORANGE_CANVAS_SIGN = registerBlock("orange_canvas_sign",
		p -> new StandingCanvasSignBlock(p, DyeColor.ORANGE), Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN));
	public static final Supplier<Block> MAGENTA_CANVAS_SIGN = registerBlock("magenta_canvas_sign",
		p -> new StandingCanvasSignBlock(p, DyeColor.MAGENTA), Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN));
	public static final Supplier<Block> LIGHT_BLUE_CANVAS_SIGN = registerBlock("light_blue_canvas_sign",
		p -> new StandingCanvasSignBlock(p, DyeColor.LIGHT_BLUE), Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN));
	public static final Supplier<Block> YELLOW_CANVAS_SIGN = registerBlock("yellow_canvas_sign",
		p -> new StandingCanvasSignBlock(p, DyeColor.YELLOW), Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN));
	public static final Supplier<Block> LIME_CANVAS_SIGN = registerBlock("lime_canvas_sign",
		p -> new StandingCanvasSignBlock(p, DyeColor.LIME), Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN));
	public static final Supplier<Block> PINK_CANVAS_SIGN = registerBlock("pink_canvas_sign",
		p -> new StandingCanvasSignBlock(p, DyeColor.PINK), Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN));
	public static final Supplier<Block> GRAY_CANVAS_SIGN = registerBlock("gray_canvas_sign",
		p -> new StandingCanvasSignBlock(p, DyeColor.GRAY), Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN));
	public static final Supplier<Block> LIGHT_GRAY_CANVAS_SIGN = registerBlock("light_gray_canvas_sign",
		p -> new StandingCanvasSignBlock(p, DyeColor.LIGHT_GRAY), Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN));
	public static final Supplier<Block> CYAN_CANVAS_SIGN = registerBlock("cyan_canvas_sign",
		p -> new StandingCanvasSignBlock(p, DyeColor.CYAN), Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN));
	public static final Supplier<Block> PURPLE_CANVAS_SIGN = registerBlock("purple_canvas_sign",
		p -> new StandingCanvasSignBlock(p, DyeColor.PURPLE), Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN));
	public static final Supplier<Block> BLUE_CANVAS_SIGN = registerBlock("blue_canvas_sign",
		p -> new StandingCanvasSignBlock(p, DyeColor.BLUE), Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN));
	public static final Supplier<Block> BROWN_CANVAS_SIGN = registerBlock("brown_canvas_sign",
		p -> new StandingCanvasSignBlock(p, DyeColor.BROWN), Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN));
	public static final Supplier<Block> GREEN_CANVAS_SIGN = registerBlock("green_canvas_sign",
		p -> new StandingCanvasSignBlock(p, DyeColor.GREEN), Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN));
	public static final Supplier<Block> RED_CANVAS_SIGN = registerBlock("red_canvas_sign",
		p -> new StandingCanvasSignBlock(p, DyeColor.RED), Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN));
	public static final Supplier<Block> BLACK_CANVAS_SIGN = registerBlock("black_canvas_sign",
		p -> new StandingCanvasSignBlock(p, DyeColor.BLACK), Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN));

	public static final Supplier<Block> CANVAS_WALL_SIGN = registerBlock("canvas_wall_sign",
		p -> new WallCanvasSignBlock(p, null), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> WHITE_CANVAS_WALL_SIGN = registerBlock("white_canvas_wall_sign",
		p -> new WallCanvasSignBlock(p, DyeColor.WHITE), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(WHITE_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> ORANGE_CANVAS_WALL_SIGN = registerBlock("orange_canvas_wall_sign",
		p -> new WallCanvasSignBlock(p, DyeColor.ORANGE), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(ORANGE_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> MAGENTA_CANVAS_WALL_SIGN = registerBlock("magenta_canvas_wall_sign",
		p -> new WallCanvasSignBlock(p, DyeColor.MAGENTA), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(MAGENTA_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> LIGHT_BLUE_CANVAS_WALL_SIGN = registerBlock("light_blue_canvas_wall_sign",
		p -> new WallCanvasSignBlock(p, DyeColor.LIGHT_BLUE), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(LIGHT_BLUE_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> YELLOW_CANVAS_WALL_SIGN = registerBlock("yellow_canvas_wall_sign",
		p -> new WallCanvasSignBlock(p, DyeColor.YELLOW), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(YELLOW_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> LIME_CANVAS_WALL_SIGN = registerBlock("lime_canvas_wall_sign",
		p -> new WallCanvasSignBlock(p, DyeColor.LIME), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(LIME_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> PINK_CANVAS_WALL_SIGN = registerBlock("pink_canvas_wall_sign",
		p -> new WallCanvasSignBlock(p, DyeColor.PINK), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(PINK_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> GRAY_CANVAS_WALL_SIGN = registerBlock("gray_canvas_wall_sign",
		p -> new WallCanvasSignBlock(p, DyeColor.GRAY), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(GRAY_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> LIGHT_GRAY_CANVAS_WALL_SIGN = registerBlock("light_gray_canvas_wall_sign",
		p -> new WallCanvasSignBlock(p, DyeColor.LIGHT_GRAY), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(LIGHT_GRAY_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> CYAN_CANVAS_WALL_SIGN = registerBlock("cyan_canvas_wall_sign",
		p -> new WallCanvasSignBlock(p, DyeColor.CYAN), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(CYAN_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> PURPLE_CANVAS_WALL_SIGN = registerBlock("purple_canvas_wall_sign",
		p -> new WallCanvasSignBlock(p, DyeColor.PURPLE), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(PURPLE_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> BLUE_CANVAS_WALL_SIGN = registerBlock("blue_canvas_wall_sign",
		p -> new WallCanvasSignBlock(p, DyeColor.BLUE), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(BLUE_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> BROWN_CANVAS_WALL_SIGN = registerBlock("brown_canvas_wall_sign",
		p -> new WallCanvasSignBlock(p, DyeColor.BROWN), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(BROWN_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> GREEN_CANVAS_WALL_SIGN = registerBlock("green_canvas_wall_sign",
		p -> new WallCanvasSignBlock(p, DyeColor.GREEN), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(GREEN_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> RED_CANVAS_WALL_SIGN = registerBlock("red_canvas_wall_sign",
		p -> new WallCanvasSignBlock(p, DyeColor.RED), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(RED_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> BLACK_CANVAS_WALL_SIGN = registerBlock("black_canvas_wall_sign",
		p -> new WallCanvasSignBlock(p, DyeColor.BLACK), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).overrideLootTable(BLACK_CANVAS_SIGN.get().getLootTable()));

	public static final Supplier<Block> HANGING_CANVAS_SIGN = registerBlock("hanging_canvas_sign",
		p -> new CeilingHangingCanvasSignBlock(p, null), Block.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN));
	public static final Supplier<Block> WHITE_HANGING_CANVAS_SIGN = registerBlock("white_hanging_canvas_sign",
		p -> new CeilingHangingCanvasSignBlock(p, DyeColor.WHITE), Block.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN));
	public static final Supplier<Block> ORANGE_HANGING_CANVAS_SIGN = registerBlock("orange_hanging_canvas_sign",
		p -> new CeilingHangingCanvasSignBlock(p, DyeColor.ORANGE), Block.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN));
	public static final Supplier<Block> MAGENTA_HANGING_CANVAS_SIGN = registerBlock("magenta_hanging_canvas_sign",
		p -> new CeilingHangingCanvasSignBlock(p, DyeColor.MAGENTA), Block.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN));
	public static final Supplier<Block> LIGHT_BLUE_HANGING_CANVAS_SIGN = registerBlock("light_blue_hanging_canvas_sign",
		p -> new CeilingHangingCanvasSignBlock(p, DyeColor.LIGHT_BLUE), Block.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN));
	public static final Supplier<Block> YELLOW_HANGING_CANVAS_SIGN = registerBlock("yellow_hanging_canvas_sign",
		p -> new CeilingHangingCanvasSignBlock(p, DyeColor.YELLOW), Block.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN));
	public static final Supplier<Block> LIME_HANGING_CANVAS_SIGN = registerBlock("lime_hanging_canvas_sign",
		p -> new CeilingHangingCanvasSignBlock(p, DyeColor.LIME), Block.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN));
	public static final Supplier<Block> PINK_HANGING_CANVAS_SIGN = registerBlock("pink_hanging_canvas_sign",
		p -> new CeilingHangingCanvasSignBlock(p, DyeColor.PINK), Block.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN));
	public static final Supplier<Block> GRAY_HANGING_CANVAS_SIGN = registerBlock("gray_hanging_canvas_sign",
		p -> new CeilingHangingCanvasSignBlock(p, DyeColor.GRAY), Block.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN));
	public static final Supplier<Block> LIGHT_GRAY_HANGING_CANVAS_SIGN = registerBlock("light_gray_hanging_canvas_sign",
		p -> new CeilingHangingCanvasSignBlock(p, DyeColor.LIGHT_GRAY), Block.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN));
	public static final Supplier<Block> CYAN_HANGING_CANVAS_SIGN = registerBlock("cyan_hanging_canvas_sign",
		p -> new CeilingHangingCanvasSignBlock(p, DyeColor.CYAN), Block.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN));
	public static final Supplier<Block> PURPLE_HANGING_CANVAS_SIGN = registerBlock("purple_hanging_canvas_sign",
		p -> new CeilingHangingCanvasSignBlock(p, DyeColor.PURPLE), Block.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN));
	public static final Supplier<Block> BLUE_HANGING_CANVAS_SIGN = registerBlock("blue_hanging_canvas_sign",
		p -> new CeilingHangingCanvasSignBlock(p, DyeColor.BLUE), Block.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN));
	public static final Supplier<Block> BROWN_HANGING_CANVAS_SIGN = registerBlock("brown_hanging_canvas_sign",
		p -> new CeilingHangingCanvasSignBlock(p, DyeColor.BROWN), Block.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN));
	public static final Supplier<Block> GREEN_HANGING_CANVAS_SIGN = registerBlock("green_hanging_canvas_sign",
		p -> new CeilingHangingCanvasSignBlock(p, DyeColor.GREEN), Block.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN));
	public static final Supplier<Block> RED_HANGING_CANVAS_SIGN = registerBlock("red_hanging_canvas_sign",
		p -> new CeilingHangingCanvasSignBlock(p, DyeColor.RED), Block.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN));
	public static final Supplier<Block> BLACK_HANGING_CANVAS_SIGN = registerBlock("black_hanging_canvas_sign",
		p -> new CeilingHangingCanvasSignBlock(p, DyeColor.BLACK), Block.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN));

	public static final Supplier<Block> HANGING_CANVAS_WALL_SIGN = registerBlock("wall_hanging_canvas_sign",
		p -> new WallHangingCanvasSignBlock(p, null), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(HANGING_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> WHITE_HANGING_CANVAS_WALL_SIGN = registerBlock("white_wall_hanging_canvas_sign",
		p -> new WallHangingCanvasSignBlock(p, DyeColor.WHITE), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(WHITE_HANGING_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> ORANGE_HANGING_CANVAS_WALL_SIGN = registerBlock("orange_wall_hanging_canvas_sign",
		p -> new WallHangingCanvasSignBlock(p, DyeColor.ORANGE), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(ORANGE_HANGING_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> MAGENTA_HANGING_CANVAS_WALL_SIGN = registerBlock("magenta_wall_hanging_canvas_sign",
		p -> new WallHangingCanvasSignBlock(p, DyeColor.MAGENTA), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(MAGENTA_HANGING_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> LIGHT_BLUE_HANGING_CANVAS_WALL_SIGN = registerBlock("light_blue_wall_hanging_canvas_sign",
		p -> new WallHangingCanvasSignBlock(p, DyeColor.LIGHT_BLUE), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(LIGHT_BLUE_HANGING_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> YELLOW_HANGING_CANVAS_WALL_SIGN = registerBlock("yellow_wall_hanging_canvas_sign",
		p -> new WallHangingCanvasSignBlock(p, DyeColor.YELLOW), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(YELLOW_HANGING_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> LIME_HANGING_CANVAS_WALL_SIGN = registerBlock("lime_wall_hanging_canvas_sign",
		p -> new WallHangingCanvasSignBlock(p, DyeColor.LIME), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(LIME_HANGING_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> PINK_HANGING_CANVAS_WALL_SIGN = registerBlock("pink_wall_hanging_canvas_sign",
		p -> new WallHangingCanvasSignBlock(p, DyeColor.PINK), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(PINK_HANGING_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> GRAY_HANGING_CANVAS_WALL_SIGN = registerBlock("gray_wall_hanging_canvas_sign",
		p -> new WallHangingCanvasSignBlock(p, DyeColor.GRAY), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(GRAY_HANGING_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> LIGHT_GRAY_HANGING_CANVAS_WALL_SIGN = registerBlock("light_gray_wall_hanging_canvas_sign",
		p -> new WallHangingCanvasSignBlock(p, DyeColor.LIGHT_GRAY), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(LIGHT_GRAY_HANGING_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> CYAN_HANGING_CANVAS_WALL_SIGN = registerBlock("cyan_wall_hanging_canvas_sign",
		p -> new WallHangingCanvasSignBlock(p, DyeColor.CYAN), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(CYAN_HANGING_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> PURPLE_HANGING_CANVAS_WALL_SIGN = registerBlock("purple_wall_hanging_canvas_sign",
		p -> new WallHangingCanvasSignBlock(p, DyeColor.PURPLE), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(PURPLE_HANGING_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> BLUE_HANGING_CANVAS_WALL_SIGN = registerBlock("blue_wall_hanging_canvas_sign",
		p -> new WallHangingCanvasSignBlock(p, DyeColor.BLUE), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(BLUE_HANGING_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> BROWN_HANGING_CANVAS_WALL_SIGN = registerBlock("brown_wall_hanging_canvas_sign",
		p -> new WallHangingCanvasSignBlock(p, DyeColor.BROWN), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(BROWN_HANGING_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> GREEN_HANGING_CANVAS_WALL_SIGN = registerBlock("green_wall_hanging_canvas_sign",
		p -> new WallHangingCanvasSignBlock(p, DyeColor.GREEN), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(GREEN_HANGING_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> RED_HANGING_CANVAS_WALL_SIGN = registerBlock("red_wall_hanging_canvas_sign",
		p -> new WallHangingCanvasSignBlock(p, DyeColor.RED), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(RED_HANGING_CANVAS_SIGN.get().getLootTable()));
	public static final Supplier<Block> BLACK_HANGING_CANVAS_WALL_SIGN = registerBlock("black_wall_hanging_canvas_sign",
		p -> new WallHangingCanvasSignBlock(p, DyeColor.BLACK), () -> Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).overrideLootTable(BLACK_HANGING_CANVAS_SIGN.get().getLootTable()));

	// Composting
	public static final Supplier<Block> BROWN_MUSHROOM_COLONY = registerBlock("brown_mushroom_colony",
		p -> new MushroomColonyBlock(Items.BROWN_MUSHROOM.builtInRegistryHolder(), p), Block.Properties.ofFullCopy(Blocks.BROWN_MUSHROOM));
	public static final Supplier<Block> RED_MUSHROOM_COLONY = registerBlock("red_mushroom_colony",
		p -> new MushroomColonyBlock(Items.RED_MUSHROOM.builtInRegistryHolder(), p), Block.Properties.ofFullCopy(Blocks.RED_MUSHROOM));
	public static final Supplier<Block> ORGANIC_COMPOST = registerBlock("organic_compost",
		OrganicCompostBlock::new, Block.Properties.ofFullCopy(Blocks.DIRT).strength(1.2F).sound(SoundType.CROP));
	public static final Supplier<Block> RICH_SOIL = registerBlock("rich_soil",
		RichSoilBlock::new, Block.Properties.ofFullCopy(Blocks.DIRT).randomTicks());
	public static final Supplier<Block> RICH_SOIL_FARMLAND = registerBlock("rich_soil_farmland",
		RichSoilFarmlandBlock::new, Block.Properties.ofFullCopy(Blocks.FARMLAND));

	// Pastries
	public static final Supplier<Block> APPLE_PIE = registerBlock("apple_pie",
		p -> new PieBlock(p, ModItems.APPLE_PIE_SLICE), Block.Properties.ofFullCopy(Blocks.CAKE));
	public static final Supplier<Block> SWEET_BERRY_CHEESECAKE = registerBlock("sweet_berry_cheesecake",
		p -> new PieBlock(p, ModItems.SWEET_BERRY_CHEESECAKE_SLICE), Block.Properties.ofFullCopy(Blocks.CAKE));
	public static final Supplier<Block> CHOCOLATE_PIE = registerBlock("chocolate_pie",
		p -> new PieBlock(p, ModItems.CHOCOLATE_PIE_SLICE), Block.Properties.ofFullCopy(Blocks.CAKE));
	public static final Supplier<Block> PUMPKIN_PIE = registerBlock("pumpkin_pie",
		p -> new PieBlock(p, ModItems.PUMPKIN_PIE_SLICE)
		{
			@Override
			public @NotNull ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
				return new ItemStack(Items.PUMPKIN_PIE);
			}
		}, Block.Properties.ofFullCopy(Blocks.CAKE));

	// Wild Crops
	public static final Supplier<Block> SANDY_SHRUB = registerBlock("sandy_shrub",
		SandyShrubBlock::new, Block.Properties.ofFullCopy(Blocks.TALL_GRASS));

	public static final Supplier<Block> WILD_CABBAGES = registerBlock("wild_cabbages",
		p -> new WildCropBlock(MobEffects.STRENGTH, 6, p), Block.Properties.ofFullCopy(Blocks.TALL_GRASS));
	public static final Supplier<Block> WILD_ONIONS = registerBlock("wild_onions",
		p -> new WildCropBlock(MobEffects.FIRE_RESISTANCE, 6, p), Block.Properties.ofFullCopy(Blocks.TALL_GRASS));
	public static final Supplier<Block> WILD_TOMATOES = registerBlock("wild_tomatoes",
		p -> new WildCropBlock(MobEffects.POISON, 10, p), Block.Properties.ofFullCopy(Blocks.TALL_GRASS));
	public static final Supplier<Block> WILD_CARROTS = registerBlock("wild_carrots",
		p -> new WildCropBlock(MobEffects.MINING_FATIGUE, 6, p), Block.Properties.ofFullCopy(Blocks.TALL_GRASS));
	public static final Supplier<Block> WILD_POTATOES = registerBlock("wild_potatoes",
		p -> new WildCropBlock(MobEffects.NAUSEA, 8, p), Block.Properties.ofFullCopy(Blocks.TALL_GRASS));
	public static final Supplier<Block> WILD_BEETROOTS = registerBlock("wild_beetroots",
		p -> new WildCropBlock(MobEffects.WATER_BREATHING, 8, p), Block.Properties.ofFullCopy(Blocks.TALL_GRASS));
	public static final Supplier<Block> WILD_RICE = registerBlock("wild_rice",
		WildRiceBlock::new, Block.Properties.ofFullCopy(Blocks.TALL_GRASS));

	// Crops
	public static final Supplier<Block> CABBAGE_CROP = registerBlock("cabbages",
		CabbageBlock::new, Block.Properties.ofFullCopy(Blocks.WHEAT).mapColor(MapColor.PLANT));
	public static final Supplier<Block> ONION_CROP = registerBlock("onions",
		OnionBlock::new, Block.Properties.ofFullCopy(Blocks.WHEAT).mapColor(MapColor.PLANT));
	public static final Supplier<Block> BUDDING_TOMATO_CROP = registerBlock("budding_tomatoes",
		BuddingTomatoBlock::new, Block.Properties.ofFullCopy(Blocks.WHEAT).mapColor(MapColor.PLANT));
	public static final DeferredHolder<Block, TomatoBlock> TOMATO_CROP = BLOCKS.register("tomatoes",
		() -> new TomatoBlock(Block.Properties.of().noCollision().randomTicks().instabreak().sound(SoundType.CROP).setId(ResourceKey.create(Registries.BLOCK, FarmersDelight.id("tomatoes")))));
	public static final DeferredHolder<Block, HangingTomatoBlock> TOMATO_CROP_ON_ROPE = BLOCKS.register("tomatoes_on_rope",
		() -> new HangingTomatoBlock(Block.Properties.ofFullCopy(ModBlocks.TOMATO_CROP.get()).pushReaction(PushReaction.NORMAL).setId(ResourceKey.create(Registries.BLOCK, FarmersDelight.id("tomatoes_on_rope")))));
	public static final Supplier<Block> RICE_CROP = registerBlock("rice",
		RiceBlock::new, Block.Properties.ofFullCopy(Blocks.WHEAT).mapColor(MapColor.PLANT).strength(0.2F));
	public static final Supplier<Block> RICE_CROP_PANICLES = registerBlock("rice_panicles",
		RicePaniclesBlock::new, Block.Properties.ofFullCopy(Blocks.WHEAT).mapColor(MapColor.PLANT));

	// Feasts
	public static final Supplier<Block> ROAST_CHICKEN_BLOCK = registerBlock("roast_chicken_block",
		p -> new RotatedFeastBlock(p, ModItems.ROAST_CHICKEN, true, BlockShapes.ROAST_CHICKEN_SHAPES, BlockShapes.TRAY_SHAPE), Block.Properties.ofFullCopy(Blocks.CAKE));
	public static final Supplier<Block> STUFFED_PUMPKIN_BLOCK = registerBlock("stuffed_pumpkin_block",
		p -> new FeastBlock(p, ModItems.STUFFED_PUMPKIN, false, true), Block.Properties.ofFullCopy(Blocks.PUMPKIN));
	public static final Supplier<Block> HONEY_GLAZED_HAM_BLOCK = registerBlock("honey_glazed_ham_block",
		p -> new RotatedFeastBlock(p, ModItems.HONEY_GLAZED_HAM, true, BlockShapes.HONEY_GLAZED_HAM_SHAPES, BlockShapes.TRAY_SHAPE), Block.Properties.ofFullCopy(Blocks.CAKE));
	public static final Supplier<Block> SHEPHERDS_PIE_BLOCK = registerBlock("shepherds_pie_block",
		p -> new RotatedFeastBlock(p, ModItems.SHEPHERDS_PIE, true, BlockShapes.SHEPHERDS_PIE_SHAPES, BlockShapes.TRAY_SHAPE), Block.Properties.ofFullCopy(Blocks.CAKE));
	public static final Supplier<Block> GLEAMING_SALAD_BLOCK = registerBlock("gleaming_salad_block",
		p -> new GleamingSaladBlock(p, ModItems.GLEAMING_SALAD, true), Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).lightLevel(glowingFeastBlockEmission()));
	public static final Supplier<Block> RICE_ROLL_MEDLEY_BLOCK = registerBlock("rice_roll_medley_block",
		RiceRollMedleyBlock::new, Block.Properties.ofFullCopy(Blocks.CAKE));
}
