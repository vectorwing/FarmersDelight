package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.bus.api.IEventBus;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.BlockShapes;
import vectorwing.farmersdelight.common.block.*;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks
{
	private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
		return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
	}

	private static ToIntFunction<BlockState> glowingFeastBlockEmission() {
		return (state) -> state.getValue(FeastBlock.SERVINGS) * 3;
	}

	private static ResourceKey<Block> key(String path) {
		return ResourceKey.create(Registries.BLOCK, FarmersDelight.id(path));
	}

	private static ResourceKey<LootTable> lootTableKey(String path) {
		return ResourceKey.create(Registries.LOOT_TABLE, FarmersDelight.id("blocks/"+path));
	}

	private static Supplier<Block> regCanvasSign(@Nullable DyeColor color) {
		String path = (color != null ? (color + "_") : "") + "canvas_sign";
		return regBlock(path, properties -> new StandingCanvasSignBlock(properties, color), BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SIGN));
	}

	private static Supplier<Block> regWallCanvasSign(@Nullable DyeColor color) {
		String path = (color != null ? (color + "_") : "") + "canvas_wall_sign";
		String basePath = (color != null ? (color + "_") : "") + "canvas_sign";
		return regBlock(path, properties -> new WallCanvasSignBlock(properties, color), BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)
			.overrideLootTable(Optional.of(lootTableKey(basePath))));
	}

	private static Supplier<Block> regHangingCanvasSign(@Nullable DyeColor color) {
		String path = (color != null ? (color + "_") : "") + "hanging_canvas_sign";
		return regBlock(path, properties -> new CeilingHangingCanvasSignBlock(properties, color), BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN));
	}

	private static Supplier<Block> regWallHangingCanvasSign(@Nullable DyeColor color) {
		String path = (color != null ? (color + "_") : "") + "wall_hanging_canvas_sign";
		String basePath = (color != null ? (color + "_") : "") + "hanging_canvas_sign";
		return regBlock(path, properties -> new WallHangingCanvasSignBlock(properties, color), BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_HANGING_SIGN)
			.overrideLootTable(Optional.of(lootTableKey(basePath))));
	}

	private static Supplier<Block> regBlock(final String name, final Function<BlockBehaviour.Properties, Block> function, final BlockBehaviour.Properties properties) {
		properties.setId(key(name));
		return regBlock(name, () -> function.apply(properties));
	}

	private static Supplier<Block> regBlock(String name, Supplier<Block> o) {
		Block register = Registry.register(BuiltInRegistries.BLOCK, FarmersDelight.id(name), o.get());
		return ()-> register;
	}

	// Workstations
	public static final Supplier<Block> STOVE = regBlock("stove",
		StoveBlock::new, Block.Properties.ofFullCopy(Blocks.BRICKS).lightLevel(litBlockEmission(13)));
	public static final Supplier<Block> COOKING_POT = regBlock("cooking_pot",
		CookingPotBlock::new, Block.Properties.of().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN));
	public static final Supplier<Block> SKILLET = regBlock("skillet",
		SkilletBlock::new, Block.Properties.of().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN));
	public static final Supplier<Block> WOODEN_BASKET = regBlock("wooden_basket",
		BasketBlock::new, Block.Properties.of().strength(1.5F).sound(SoundType.WOOD));
	public static final Supplier<Block> BAMBOO_BASKET = regBlock("bamboo_basket",
		BasketBlock::new, Block.Properties.of().strength(1.5F).sound(SoundType.BAMBOO_WOOD));
	public static final Supplier<Block> CUTTING_BOARD = regBlock("cutting_board",
		CuttingBoardBlock::new, Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F).sound(SoundType.WOOD));

	/**
	 * Deprecated reference added for backwards compatibility. Use BAMBOO_BASKET instead.
	 */
	@Deprecated(forRemoval = true)
	public static final Supplier<Block> BASKET = BAMBOO_BASKET;

	// Crop Storage
	public static final Supplier<Block> CARROT_CRATE = regBlock("carrot_crate",
		Block::new, Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD));
	public static final Supplier<Block> POTATO_CRATE = regBlock("potato_crate",
		Block::new, Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD));
	public static final Supplier<Block> BEETROOT_CRATE = regBlock("beetroot_crate",
		Block::new, Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD));
	public static final Supplier<Block> CABBAGE_CRATE = regBlock("cabbage_crate",
		Block::new, Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD));
	public static final Supplier<Block> TOMATO_CRATE = regBlock("tomato_crate",
		Block::new, Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD));
	public static final Supplier<Block> ONION_CRATE = regBlock("onion_crate",
		Block::new, Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD));
	public static final Supplier<Block> RICE_BALE = regBlock("rice_bale",
		RiceBaleBlock::new, Block.Properties.ofFullCopy(Blocks.HAY_BLOCK));
	public static final Supplier<Block> RICE_BAG = regBlock("rice_bag",
		Block::new, Block.Properties.ofFullCopy(Blocks.WHITE_WOOL));
	public static final Supplier<Block> STRAW_BALE = regBlock("straw_bale",
		StrawBaleBlock::new, Block.Properties.ofFullCopy(Blocks.HAY_BLOCK));

	// Building
	public static final Supplier<Block> ROPE = regBlock("rope",
		RopeBlock::new, Block.Properties.ofFullCopy(Blocks.BROWN_CARPET).noCollision().noOcclusion().strength(0.2F).sound(SoundType.WOOL));
	public static final Supplier<Block> SAFETY_NET = regBlock("safety_net",
		SafetyNetBlock::new, Block.Properties.ofFullCopy(Blocks.BROWN_CARPET).strength(0.2F).sound(SoundType.WOOL));
	public static final Supplier<Block> ROPE_FENCE = regBlock("rope_fence",
		RopeFenceBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE).strength(1.0F));
	public static final Supplier<Block> ROPE_FENCE_GATE = regBlock("rope_fence_gate",
		RopeFenceGateBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE).strength(1.0F));
	public static final Supplier<Block> OAK_CABINET = regBlock("oak_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL));
	public static final Supplier<Block> SPRUCE_CABINET = regBlock("spruce_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL));
	public static final Supplier<Block> BIRCH_CABINET = regBlock("birch_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL));
	public static final Supplier<Block> JUNGLE_CABINET = regBlock("jungle_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL));
	public static final Supplier<Block> ACACIA_CABINET = regBlock("acacia_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL));
	public static final Supplier<Block> DARK_OAK_CABINET = regBlock("dark_oak_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL));
	public static final Supplier<Block> MANGROVE_CABINET = regBlock("mangrove_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL));
	public static final Supplier<Block> CHERRY_CABINET = regBlock("cherry_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.CHERRY_WOOD));
	public static final Supplier<Block> BAMBOO_CABINET = regBlock("bamboo_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.BAMBOO_WOOD));
	public static final Supplier<Block> PALE_OAK_CABINET = regBlock("pale_oak_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL));
	public static final Supplier<Block> CRIMSON_CABINET = regBlock("crimson_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.NETHER_WOOD));
	public static final Supplier<Block> WARPED_CABINET = regBlock("warped_cabinet",
		CabinetBlock::new, Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.NETHER_WOOD));
	public static final Supplier<Block> CANVAS_RUG = regBlock("canvas_rug",
		CanvasRugBlock::new, Block.Properties.ofFullCopy(Blocks.WHITE_CARPET).sound(SoundType.GRASS).strength(0.2F));
	public static final Supplier<Block> TATAMI = regBlock("tatami",
		TatamiBlock::new, Block.Properties.ofFullCopy(Blocks.WHITE_WOOL));
	public static final Supplier<Block> FULL_TATAMI_MAT = regBlock("full_tatami_mat",
		TatamiMatBlock::new, Block.Properties.ofFullCopy(Blocks.WHITE_WOOL).strength(0.3F));
	public static final Supplier<Block> HALF_TATAMI_MAT = regBlock("half_tatami_mat",
		TatamiHalfMatBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).strength(0.3F).pushReaction(PushReaction.DESTROY));

	public static final Supplier<Block> CANVAS_SIGN = regCanvasSign(null);
	public static final Supplier<Block> WHITE_CANVAS_SIGN = regCanvasSign(DyeColor.WHITE);
	public static final Supplier<Block> ORANGE_CANVAS_SIGN = regCanvasSign(DyeColor.ORANGE);
	public static final Supplier<Block> MAGENTA_CANVAS_SIGN = regCanvasSign(DyeColor.MAGENTA);
	public static final Supplier<Block> LIGHT_BLUE_CANVAS_SIGN = regCanvasSign(DyeColor.LIGHT_BLUE);
	public static final Supplier<Block> YELLOW_CANVAS_SIGN = regCanvasSign(DyeColor.YELLOW);
	public static final Supplier<Block> LIME_CANVAS_SIGN = regCanvasSign(DyeColor.LIME);
	public static final Supplier<Block> PINK_CANVAS_SIGN = regCanvasSign(DyeColor.PINK);
	public static final Supplier<Block> GRAY_CANVAS_SIGN = regCanvasSign(DyeColor.GRAY);
	public static final Supplier<Block> LIGHT_GRAY_CANVAS_SIGN = regCanvasSign(DyeColor.LIGHT_GRAY);
	public static final Supplier<Block> CYAN_CANVAS_SIGN = regCanvasSign(DyeColor.CYAN);
	public static final Supplier<Block> PURPLE_CANVAS_SIGN = regCanvasSign(DyeColor.PURPLE);
	public static final Supplier<Block> BLUE_CANVAS_SIGN = regCanvasSign(DyeColor.BLUE);
	public static final Supplier<Block> BROWN_CANVAS_SIGN = regCanvasSign(DyeColor.BROWN);
	public static final Supplier<Block> GREEN_CANVAS_SIGN = regCanvasSign(DyeColor.GREEN);
	public static final Supplier<Block> RED_CANVAS_SIGN = regCanvasSign(DyeColor.RED);
	public static final Supplier<Block> BLACK_CANVAS_SIGN = regCanvasSign(DyeColor.BLACK);

	public static final Supplier<Block> CANVAS_WALL_SIGN = regWallCanvasSign(null);
	public static final Supplier<Block> WHITE_CANVAS_WALL_SIGN = regWallCanvasSign(DyeColor.WHITE);
	public static final Supplier<Block> ORANGE_CANVAS_WALL_SIGN = regWallCanvasSign(DyeColor.ORANGE);
	public static final Supplier<Block> MAGENTA_CANVAS_WALL_SIGN = regWallCanvasSign(DyeColor.MAGENTA);
	public static final Supplier<Block> LIGHT_BLUE_CANVAS_WALL_SIGN = regWallCanvasSign(DyeColor.LIGHT_BLUE);
	public static final Supplier<Block> YELLOW_CANVAS_WALL_SIGN = regWallCanvasSign(DyeColor.YELLOW);
	public static final Supplier<Block> LIME_CANVAS_WALL_SIGN = regWallCanvasSign(DyeColor.LIME);
	public static final Supplier<Block> PINK_CANVAS_WALL_SIGN = regWallCanvasSign(DyeColor.PINK);
	public static final Supplier<Block> GRAY_CANVAS_WALL_SIGN = regWallCanvasSign(DyeColor.GRAY);
	public static final Supplier<Block> LIGHT_GRAY_CANVAS_WALL_SIGN = regWallCanvasSign(DyeColor.LIGHT_GRAY);
	public static final Supplier<Block> CYAN_CANVAS_WALL_SIGN = regWallCanvasSign(DyeColor.CYAN);
	public static final Supplier<Block> PURPLE_CANVAS_WALL_SIGN = regWallCanvasSign(DyeColor.PURPLE);
	public static final Supplier<Block> BLUE_CANVAS_WALL_SIGN = regWallCanvasSign(DyeColor.BLUE);
	public static final Supplier<Block> BROWN_CANVAS_WALL_SIGN = regWallCanvasSign(DyeColor.BROWN);
	public static final Supplier<Block> GREEN_CANVAS_WALL_SIGN = regWallCanvasSign(DyeColor.GREEN);
	public static final Supplier<Block> RED_CANVAS_WALL_SIGN = regWallCanvasSign(DyeColor.RED);
	public static final Supplier<Block> BLACK_CANVAS_WALL_SIGN = regWallCanvasSign(DyeColor.BLACK);

	public static final Supplier<Block> HANGING_CANVAS_SIGN = regHangingCanvasSign(null);
	public static final Supplier<Block> WHITE_HANGING_CANVAS_SIGN = regHangingCanvasSign(DyeColor.WHITE);
	public static final Supplier<Block> ORANGE_HANGING_CANVAS_SIGN = regHangingCanvasSign(DyeColor.ORANGE);
	public static final Supplier<Block> MAGENTA_HANGING_CANVAS_SIGN = regHangingCanvasSign(DyeColor.MAGENTA);
	public static final Supplier<Block> LIGHT_BLUE_HANGING_CANVAS_SIGN = regHangingCanvasSign(DyeColor.LIGHT_BLUE);
	public static final Supplier<Block> YELLOW_HANGING_CANVAS_SIGN = regHangingCanvasSign(DyeColor.YELLOW);
	public static final Supplier<Block> LIME_HANGING_CANVAS_SIGN = regHangingCanvasSign(DyeColor.LIME);
	public static final Supplier<Block> PINK_HANGING_CANVAS_SIGN = regHangingCanvasSign(DyeColor.PINK);
	public static final Supplier<Block> GRAY_HANGING_CANVAS_SIGN = regHangingCanvasSign(DyeColor.GRAY);
	public static final Supplier<Block> LIGHT_GRAY_HANGING_CANVAS_SIGN = regHangingCanvasSign(DyeColor.LIGHT_GRAY);
	public static final Supplier<Block> CYAN_HANGING_CANVAS_SIGN = regHangingCanvasSign(DyeColor.CYAN);
	public static final Supplier<Block> PURPLE_HANGING_CANVAS_SIGN = regHangingCanvasSign(DyeColor.PURPLE);
	public static final Supplier<Block> BLUE_HANGING_CANVAS_SIGN = regHangingCanvasSign(DyeColor.BLUE);
	public static final Supplier<Block> BROWN_HANGING_CANVAS_SIGN = regHangingCanvasSign(DyeColor.BROWN);
	public static final Supplier<Block> GREEN_HANGING_CANVAS_SIGN = regHangingCanvasSign(DyeColor.GREEN);
	public static final Supplier<Block> RED_HANGING_CANVAS_SIGN = regHangingCanvasSign(DyeColor.RED);
	public static final Supplier<Block> BLACK_HANGING_CANVAS_SIGN = regHangingCanvasSign(DyeColor.BLACK);

	public static final Supplier<Block> HANGING_CANVAS_WALL_SIGN = regWallHangingCanvasSign(null);
	public static final Supplier<Block> WHITE_HANGING_CANVAS_WALL_SIGN = regWallHangingCanvasSign(DyeColor.WHITE);
	public static final Supplier<Block> ORANGE_HANGING_CANVAS_WALL_SIGN = regWallHangingCanvasSign(DyeColor.ORANGE);
	public static final Supplier<Block> MAGENTA_HANGING_CANVAS_WALL_SIGN = regWallHangingCanvasSign(DyeColor.MAGENTA);
	public static final Supplier<Block> LIGHT_BLUE_HANGING_CANVAS_WALL_SIGN = regWallHangingCanvasSign(DyeColor.LIGHT_BLUE);
	public static final Supplier<Block> YELLOW_HANGING_CANVAS_WALL_SIGN = regWallHangingCanvasSign(DyeColor.YELLOW);
	public static final Supplier<Block> LIME_HANGING_CANVAS_WALL_SIGN = regWallHangingCanvasSign(DyeColor.LIME);
	public static final Supplier<Block> PINK_HANGING_CANVAS_WALL_SIGN = regWallHangingCanvasSign(DyeColor.PINK);
	public static final Supplier<Block> GRAY_HANGING_CANVAS_WALL_SIGN = regWallHangingCanvasSign(DyeColor.GRAY);
	public static final Supplier<Block> LIGHT_GRAY_HANGING_CANVAS_WALL_SIGN = regWallHangingCanvasSign(DyeColor.LIGHT_GRAY);
	public static final Supplier<Block> CYAN_HANGING_CANVAS_WALL_SIGN = regWallHangingCanvasSign(DyeColor.CYAN);
	public static final Supplier<Block> PURPLE_HANGING_CANVAS_WALL_SIGN = regWallHangingCanvasSign(DyeColor.PURPLE);
	public static final Supplier<Block> BLUE_HANGING_CANVAS_WALL_SIGN = regWallHangingCanvasSign(DyeColor.BLUE);
	public static final Supplier<Block> BROWN_HANGING_CANVAS_WALL_SIGN = regWallHangingCanvasSign(DyeColor.BROWN);
	public static final Supplier<Block> GREEN_HANGING_CANVAS_WALL_SIGN = regWallHangingCanvasSign(DyeColor.GREEN);
	public static final Supplier<Block> RED_HANGING_CANVAS_WALL_SIGN = regWallHangingCanvasSign(DyeColor.RED);
	public static final Supplier<Block> BLACK_HANGING_CANVAS_WALL_SIGN = regWallHangingCanvasSign(DyeColor.BLACK);

	// Composting
	public static final Supplier<Block> BROWN_MUSHROOM_COLONY = regBlock("brown_mushroom_colony",
		(properties) -> new MushroomColonyBlock(Items.BROWN_MUSHROOM.builtInRegistryHolder(), properties),
		Block.Properties.ofFullCopy(Blocks.BROWN_MUSHROOM));
	public static final Supplier<Block> RED_MUSHROOM_COLONY = regBlock("red_mushroom_colony",
		(properties) -> new MushroomColonyBlock(Items.RED_MUSHROOM.builtInRegistryHolder(), properties),
		Block.Properties.ofFullCopy(Blocks.RED_MUSHROOM));
	public static final Supplier<Block> ORGANIC_COMPOST = regBlock("organic_compost",
		OrganicCompostBlock::new, Block.Properties.ofFullCopy(Blocks.DIRT).strength(1.2F).sound(SoundType.CROP));
	public static final Supplier<Block> RICH_SOIL = regBlock("rich_soil",
		RichSoilBlock::new, Block.Properties.ofFullCopy(Blocks.DIRT).randomTicks());
	public static final Supplier<Block> RICH_SOIL_FARMLAND = regBlock("rich_soil_farmland",
		RichSoilFarmlandBlock::new, Block.Properties.ofFullCopy(Blocks.FARMLAND));

	// Pastries
	public static final Supplier<Block> APPLE_PIE = regBlock("apple_pie",
		(properties) -> new PieBlock(properties, () -> ModItems.APPLE_PIE_SLICE.get()), //dont kill double lambda
		Block.Properties.ofFullCopy(Blocks.CAKE));
	public static final Supplier<Block> SWEET_BERRY_CHEESECAKE = regBlock("sweet_berry_cheesecake",
		(properties) -> new PieBlock(properties, () -> ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get()),
		Block.Properties.ofFullCopy(Blocks.CAKE));
	public static final Supplier<Block> CHOCOLATE_PIE = regBlock("chocolate_pie",
		(properties) -> new PieBlock(properties, () -> ModItems.CHOCOLATE_PIE_SLICE.get()),
		Block.Properties.ofFullCopy(Blocks.CAKE));
	public static final Supplier<Block> PUMPKIN_PIE = regBlock("pumpkin_pie",
		(properties) -> new PieBlock(properties, () -> ModItems.PUMPKIN_PIE_SLICE.get()),
		Block.Properties.ofFullCopy(Blocks.CAKE));

	// Wild Crops
	public static final Supplier<Block> SANDY_SHRUB = regBlock("sandy_shrub",
		SandyShrubBlock::new, Block.Properties.ofFullCopy(Blocks.TALL_GRASS));

	public static final Supplier<Block> WILD_CABBAGES = regBlock("wild_cabbages",
		(properties) -> new WildCropBlock(MobEffects.STRENGTH, 6, properties),
		Block.Properties.ofFullCopy(Blocks.TALL_GRASS));
	public static final Supplier<Block> WILD_ONIONS = regBlock("wild_onions",
		(properties) -> new WildCropBlock(MobEffects.FIRE_RESISTANCE, 6, properties),
		Block.Properties.ofFullCopy(Blocks.TALL_GRASS));
	public static final Supplier<Block> WILD_TOMATOES = regBlock("wild_tomatoes",
		(properties) -> new WildCropBlock(MobEffects.POISON, 10, properties),
		Block.Properties.ofFullCopy(Blocks.TALL_GRASS));
	public static final Supplier<Block> WILD_CARROTS = regBlock("wild_carrots",
		(properties) -> new WildCropBlock(MobEffects.MINING_FATIGUE, 6, properties),
		Block.Properties.ofFullCopy(Blocks.TALL_GRASS));
	public static final Supplier<Block> WILD_POTATOES = regBlock("wild_potatoes",
		(properties) -> new WildCropBlock(MobEffects.NAUSEA, 8, properties),
		Block.Properties.ofFullCopy(Blocks.TALL_GRASS));
	public static final Supplier<Block> WILD_BEETROOTS = regBlock("wild_beetroots",
		(properties) -> new WildCropBlock(MobEffects.WATER_BREATHING, 8, properties),
		Block.Properties.ofFullCopy(Blocks.TALL_GRASS));
	public static final Supplier<Block> WILD_RICE = regBlock("wild_rice",
		WildRiceBlock::new, Block.Properties.ofFullCopy(Blocks.TALL_GRASS));

	// Crops
	public static final Supplier<Block> CABBAGE_CROP = regBlock("cabbages",
		CabbageBlock::new, Block.Properties.ofFullCopy(Blocks.WHEAT));
	public static final Supplier<Block> ONION_CROP = regBlock("onions",
		OnionBlock::new, Block.Properties.ofFullCopy(Blocks.WHEAT));
	public static final Supplier<Block> BUDDING_TOMATO_CROP = regBlock("budding_tomatoes",
		BuddingTomatoBlock::new, Block.Properties.ofFullCopy(Blocks.WHEAT).mapColor(MapColor.PLANT));
	public static final Supplier<Block> TOMATO_CROP = regBlock("tomatoes",
		TomatoBlock::new, Block.Properties.ofFullCopy(Blocks.WHEAT).mapColor(MapColor.PLANT));
	public static final Supplier<Block> TOMATO_CROP_ON_ROPE = regBlock("tomatoes_on_rope",
		HangingTomatoBlock::new, Block.Properties.ofFullCopy(TOMATO_CROP.get()).pushReaction(PushReaction.NORMAL));
	public static final Supplier<Block> RICE_CROP = regBlock("rice",
		RiceBlock::new, Block.Properties.ofFullCopy(Blocks.WHEAT).mapColor(MapColor.PLANT).strength(0.2F));
	public static final Supplier<Block> RICE_CROP_PANICLES = regBlock("rice_panicles",
		RicePaniclesBlock::new, Block.Properties.ofFullCopy(Blocks.WHEAT).mapColor(MapColor.PLANT));

	// Feasts
	public static final Supplier<Block> ROAST_CHICKEN_BLOCK = regBlock("roast_chicken_block",
		(properties) -> new RotatedFeastBlock(properties, () -> ModItems.ROAST_CHICKEN.get(), true, BlockShapes.ROAST_CHICKEN_SHAPES, BlockShapes.TRAY_SHAPE),
		Block.Properties.ofFullCopy(Blocks.CAKE));
	public static final Supplier<Block> STUFFED_PUMPKIN_BLOCK = regBlock("stuffed_pumpkin_block",
		(properties) -> new FeastBlock(properties, () -> ModItems.STUFFED_PUMPKIN.get(), false, true),
		Block.Properties.ofFullCopy(Blocks.PUMPKIN));
	public static final Supplier<Block> HONEY_GLAZED_HAM_BLOCK = regBlock("honey_glazed_ham_block",
		(properties) -> new RotatedFeastBlock(properties, () -> ModItems.HONEY_GLAZED_HAM.get(), true, BlockShapes.HONEY_GLAZED_HAM_SHAPES, BlockShapes.TRAY_SHAPE),
		Block.Properties.ofFullCopy(Blocks.CAKE));
	public static final Supplier<Block> SHEPHERDS_PIE_BLOCK = regBlock("shepherds_pie_block",
		(properties) -> new RotatedFeastBlock(properties, () -> ModItems.SHEPHERDS_PIE.get(), true, BlockShapes.SHEPHERDS_PIE_SHAPES, BlockShapes.TRAY_SHAPE),
		Block.Properties.ofFullCopy(Blocks.CAKE));
	public static final Supplier<Block> GLEAMING_SALAD_BLOCK = regBlock("gleaming_salad_block",
		(properties) -> new GleamingSaladBlock(properties, () -> ModItems.GLEAMING_SALAD.get(), true),
		Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).lightLevel(glowingFeastBlockEmission()));
	public static final Supplier<Block> RICE_ROLL_MEDLEY_BLOCK = regBlock("rice_roll_medley_block",
		RiceRollMedleyBlock::new, Block.Properties.ofFullCopy(Blocks.CAKE));

	public static void touch() {

	}

	public static void register(IEventBus modEventBus) {

	}
}