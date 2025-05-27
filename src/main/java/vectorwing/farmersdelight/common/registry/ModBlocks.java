package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.*;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, FarmersDelight.MODID);

	private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
		return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
	}

	// Workstations
	public static final Supplier<Block> STOVE = BLOCKS.register("stove",
			() -> new StoveBlock(Block.Properties.ofFullCopy(Blocks.BRICKS).lightLevel(litBlockEmission(13))));
	public static final Supplier<Block> COOKING_POT = BLOCKS.register("cooking_pot",
			() -> new CookingPotBlock(Block.Properties.of().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN)));
	public static final Supplier<Block> SKILLET = BLOCKS.register("skillet",
			() -> new SkilletBlock(Block.Properties.of().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN)));
	public static final Supplier<Block> BASKET = BLOCKS.register("basket",
			() -> new BasketBlock(Block.Properties.of().strength(1.5F).sound(SoundType.BAMBOO_WOOD)));
	public static final Supplier<Block> CUTTING_BOARD = BLOCKS.register("cutting_board",
			() -> new CuttingBoardBlock(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F).sound(SoundType.WOOD)));

	// Crop Storage
	public static final Supplier<Block> CARROT_CRATE = BLOCKS.register("carrot_crate",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final Supplier<Block> POTATO_CRATE = BLOCKS.register("potato_crate",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final Supplier<Block> BEETROOT_CRATE = BLOCKS.register("beetroot_crate",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final Supplier<Block> CABBAGE_CRATE = BLOCKS.register("cabbage_crate",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final Supplier<Block> TOMATO_CRATE = BLOCKS.register("tomato_crate",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final Supplier<Block> ONION_CRATE = BLOCKS.register("onion_crate",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final Supplier<Block> RICE_BALE = BLOCKS.register("rice_bale",
			() -> new RiceBaleBlock(Block.Properties.ofFullCopy(Blocks.HAY_BLOCK)));
	public static final Supplier<Block> RICE_BAG = BLOCKS.register("rice_bag",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.WHITE_WOOL)));
	public static final Supplier<Block> STRAW_BALE = BLOCKS.register("straw_bale",
			() -> new StrawBaleBlock(Block.Properties.ofFullCopy(Blocks.HAY_BLOCK)));

	// Building
	public static final Supplier<Block> ROPE = BLOCKS.register("rope",
			() -> new RopeBlock(Block.Properties.ofFullCopy(Blocks.BROWN_CARPET).noCollission().noOcclusion().strength(0.2F).sound(SoundType.WOOL)));
	public static final Supplier<Block> SAFETY_NET = BLOCKS.register("safety_net",
			() -> new SafetyNetBlock(Block.Properties.ofFullCopy(Blocks.BROWN_CARPET).strength(0.2F).sound(SoundType.WOOL)));
	public static final Supplier<Block> OAK_CABINET = BLOCKS.register("oak_cabinet",
			() -> new CabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL)));
	public static final Supplier<Block> SPRUCE_CABINET = BLOCKS.register("spruce_cabinet",
			() -> new CabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL)));
	public static final Supplier<Block> BIRCH_CABINET = BLOCKS.register("birch_cabinet",
			() -> new CabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL)));
	public static final Supplier<Block> JUNGLE_CABINET = BLOCKS.register("jungle_cabinet",
			() -> new CabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL)));
	public static final Supplier<Block> ACACIA_CABINET = BLOCKS.register("acacia_cabinet",
			() -> new CabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL)));
	public static final Supplier<Block> DARK_OAK_CABINET = BLOCKS.register("dark_oak_cabinet",
			() -> new CabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL)));
	public static final Supplier<Block> MANGROVE_CABINET = BLOCKS.register("mangrove_cabinet",
			() -> new CabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL)));
	public static final Supplier<Block> CHERRY_CABINET = BLOCKS.register("cherry_cabinet",
			() -> new CabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.CHERRY_WOOD)));
	public static final Supplier<Block> BAMBOO_CABINET = BLOCKS.register("bamboo_cabinet",
			() -> new CabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.BAMBOO_WOOD)));
	public static final Supplier<Block> CRIMSON_CABINET = BLOCKS.register("crimson_cabinet",
			() -> new CabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.NETHER_WOOD)));
	public static final Supplier<Block> WARPED_CABINET = BLOCKS.register("warped_cabinet",
			() -> new CabinetBlock(Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.NETHER_WOOD)));
	public static final Supplier<Block> CANVAS_RUG = BLOCKS.register("canvas_rug",
			() -> new CanvasRugBlock(Block.Properties.ofFullCopy(Blocks.WHITE_CARPET).sound(SoundType.GRASS).strength(0.2F)));
	public static final Supplier<Block> TATAMI = BLOCKS.register("tatami",
			() -> new TatamiBlock(Block.Properties.ofFullCopy(Blocks.WHITE_WOOL)));
	public static final Supplier<Block> FULL_TATAMI_MAT = BLOCKS.register("full_tatami_mat",
			() -> new TatamiMatBlock(Block.Properties.ofFullCopy(Blocks.WHITE_WOOL).strength(0.3F)));
	public static final Supplier<Block> HALF_TATAMI_MAT = BLOCKS.register("half_tatami_mat",
			() -> new TatamiHalfMatBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).strength(0.3F).pushReaction(PushReaction.DESTROY)));

	public static final Supplier<Block> CANVAS_SIGN = BLOCKS.register("canvas_sign",
			() -> new StandingCanvasSignBlock(null));
	public static final Supplier<Block> WHITE_CANVAS_SIGN = BLOCKS.register("white_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.WHITE));
	public static final Supplier<Block> ORANGE_CANVAS_SIGN = BLOCKS.register("orange_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.ORANGE));
	public static final Supplier<Block> MAGENTA_CANVAS_SIGN = BLOCKS.register("magenta_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.MAGENTA));
	public static final Supplier<Block> LIGHT_BLUE_CANVAS_SIGN = BLOCKS.register("light_blue_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.LIGHT_BLUE));
	public static final Supplier<Block> YELLOW_CANVAS_SIGN = BLOCKS.register("yellow_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.YELLOW));
	public static final Supplier<Block> LIME_CANVAS_SIGN = BLOCKS.register("lime_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.LIME));
	public static final Supplier<Block> PINK_CANVAS_SIGN = BLOCKS.register("pink_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.PINK));
	public static final Supplier<Block> GRAY_CANVAS_SIGN = BLOCKS.register("gray_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.GRAY));
	public static final Supplier<Block> LIGHT_GRAY_CANVAS_SIGN = BLOCKS.register("light_gray_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.LIGHT_GRAY));
	public static final Supplier<Block> CYAN_CANVAS_SIGN = BLOCKS.register("cyan_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.CYAN));
	public static final Supplier<Block> PURPLE_CANVAS_SIGN = BLOCKS.register("purple_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.PURPLE));
	public static final Supplier<Block> BLUE_CANVAS_SIGN = BLOCKS.register("blue_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.BLUE));
	public static final Supplier<Block> BROWN_CANVAS_SIGN = BLOCKS.register("brown_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.BROWN));
	public static final Supplier<Block> GREEN_CANVAS_SIGN = BLOCKS.register("green_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.GREEN));
	public static final Supplier<Block> RED_CANVAS_SIGN = BLOCKS.register("red_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.RED));
	public static final Supplier<Block> BLACK_CANVAS_SIGN = BLOCKS.register("black_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.BLACK));

	public static final Supplier<Block> CANVAS_WALL_SIGN = BLOCKS.register("canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).lootFrom(CANVAS_SIGN), null));
	public static final Supplier<Block> WHITE_CANVAS_WALL_SIGN = BLOCKS.register("white_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).lootFrom(WHITE_CANVAS_SIGN), DyeColor.WHITE));
	public static final Supplier<Block> ORANGE_CANVAS_WALL_SIGN = BLOCKS.register("orange_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).lootFrom(ORANGE_CANVAS_SIGN), DyeColor.ORANGE));
	public static final Supplier<Block> MAGENTA_CANVAS_WALL_SIGN = BLOCKS.register("magenta_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).lootFrom(MAGENTA_CANVAS_SIGN), DyeColor.MAGENTA));
	public static final Supplier<Block> LIGHT_BLUE_CANVAS_WALL_SIGN = BLOCKS.register("light_blue_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).lootFrom(LIGHT_BLUE_CANVAS_SIGN), DyeColor.LIGHT_BLUE));
	public static final Supplier<Block> YELLOW_CANVAS_WALL_SIGN = BLOCKS.register("yellow_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).lootFrom(YELLOW_CANVAS_SIGN), DyeColor.YELLOW));
	public static final Supplier<Block> LIME_CANVAS_WALL_SIGN = BLOCKS.register("lime_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).lootFrom(LIME_CANVAS_SIGN), DyeColor.LIME));
	public static final Supplier<Block> PINK_CANVAS_WALL_SIGN = BLOCKS.register("pink_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).lootFrom(PINK_CANVAS_SIGN), DyeColor.PINK));
	public static final Supplier<Block> GRAY_CANVAS_WALL_SIGN = BLOCKS.register("gray_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).lootFrom(GRAY_CANVAS_SIGN), DyeColor.GRAY));
	public static final Supplier<Block> LIGHT_GRAY_CANVAS_WALL_SIGN = BLOCKS.register("light_gray_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).lootFrom(LIGHT_GRAY_CANVAS_SIGN), DyeColor.LIGHT_GRAY));
	public static final Supplier<Block> CYAN_CANVAS_WALL_SIGN = BLOCKS.register("cyan_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).lootFrom(CYAN_CANVAS_SIGN), DyeColor.CYAN));
	public static final Supplier<Block> PURPLE_CANVAS_WALL_SIGN = BLOCKS.register("purple_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).lootFrom(PURPLE_CANVAS_SIGN), DyeColor.PURPLE));
	public static final Supplier<Block> BLUE_CANVAS_WALL_SIGN = BLOCKS.register("blue_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).lootFrom(BLUE_CANVAS_SIGN), DyeColor.BLUE));
	public static final Supplier<Block> BROWN_CANVAS_WALL_SIGN = BLOCKS.register("brown_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).lootFrom(BROWN_CANVAS_SIGN), DyeColor.BROWN));
	public static final Supplier<Block> GREEN_CANVAS_WALL_SIGN = BLOCKS.register("green_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).lootFrom(GREEN_CANVAS_SIGN), DyeColor.GREEN));
	public static final Supplier<Block> RED_CANVAS_WALL_SIGN = BLOCKS.register("red_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).lootFrom(RED_CANVAS_SIGN), DyeColor.RED));
	public static final Supplier<Block> BLACK_CANVAS_WALL_SIGN = BLOCKS.register("black_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN).lootFrom(BLACK_CANVAS_SIGN), DyeColor.BLACK));

	public static final Supplier<Block> HANGING_CANVAS_SIGN = BLOCKS.register("hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(null));
	public static final Supplier<Block> WHITE_HANGING_CANVAS_SIGN = BLOCKS.register("white_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.WHITE));
	public static final Supplier<Block> ORANGE_HANGING_CANVAS_SIGN = BLOCKS.register("orange_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.ORANGE));
	public static final Supplier<Block> MAGENTA_HANGING_CANVAS_SIGN = BLOCKS.register("magenta_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.MAGENTA));
	public static final Supplier<Block> LIGHT_BLUE_HANGING_CANVAS_SIGN = BLOCKS.register("light_blue_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.LIGHT_BLUE));
	public static final Supplier<Block> YELLOW_HANGING_CANVAS_SIGN = BLOCKS.register("yellow_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.YELLOW));
	public static final Supplier<Block> LIME_HANGING_CANVAS_SIGN = BLOCKS.register("lime_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.LIME));
	public static final Supplier<Block> PINK_HANGING_CANVAS_SIGN = BLOCKS.register("pink_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.PINK));
	public static final Supplier<Block> GRAY_HANGING_CANVAS_SIGN = BLOCKS.register("gray_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.GRAY));
	public static final Supplier<Block> LIGHT_GRAY_HANGING_CANVAS_SIGN = BLOCKS.register("light_gray_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.LIGHT_GRAY));
	public static final Supplier<Block> CYAN_HANGING_CANVAS_SIGN = BLOCKS.register("cyan_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.CYAN));
	public static final Supplier<Block> PURPLE_HANGING_CANVAS_SIGN = BLOCKS.register("purple_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.PURPLE));
	public static final Supplier<Block> BLUE_HANGING_CANVAS_SIGN = BLOCKS.register("blue_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.BLUE));
	public static final Supplier<Block> BROWN_HANGING_CANVAS_SIGN = BLOCKS.register("brown_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.BROWN));
	public static final Supplier<Block> GREEN_HANGING_CANVAS_SIGN = BLOCKS.register("green_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.GREEN));
	public static final Supplier<Block> RED_HANGING_CANVAS_SIGN = BLOCKS.register("red_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.RED));
	public static final Supplier<Block> BLACK_HANGING_CANVAS_SIGN = BLOCKS.register("black_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.BLACK));

	public static final Supplier<Block> HANGING_CANVAS_WALL_SIGN = BLOCKS.register("wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).lootFrom(HANGING_CANVAS_SIGN), null));
	public static final Supplier<Block> WHITE_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("white_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).lootFrom(WHITE_HANGING_CANVAS_SIGN), DyeColor.WHITE));
	public static final Supplier<Block> ORANGE_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("orange_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).lootFrom(ORANGE_HANGING_CANVAS_SIGN), DyeColor.ORANGE));
	public static final Supplier<Block> MAGENTA_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("magenta_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).lootFrom(MAGENTA_HANGING_CANVAS_SIGN), DyeColor.MAGENTA));
	public static final Supplier<Block> LIGHT_BLUE_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("light_blue_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).lootFrom(LIGHT_BLUE_HANGING_CANVAS_SIGN), DyeColor.LIGHT_BLUE));
	public static final Supplier<Block> YELLOW_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("yellow_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).lootFrom(YELLOW_HANGING_CANVAS_SIGN), DyeColor.YELLOW));
	public static final Supplier<Block> LIME_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("lime_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).lootFrom(LIME_HANGING_CANVAS_SIGN), DyeColor.LIME));
	public static final Supplier<Block> PINK_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("pink_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).lootFrom(PINK_HANGING_CANVAS_SIGN), DyeColor.PINK));
	public static final Supplier<Block> GRAY_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("gray_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).lootFrom(GRAY_HANGING_CANVAS_SIGN), DyeColor.GRAY));
	public static final Supplier<Block> LIGHT_GRAY_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("light_gray_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).lootFrom(LIGHT_GRAY_HANGING_CANVAS_SIGN), DyeColor.LIGHT_GRAY));
	public static final Supplier<Block> CYAN_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("cyan_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).lootFrom(CYAN_HANGING_CANVAS_SIGN), DyeColor.CYAN));
	public static final Supplier<Block> PURPLE_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("purple_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).lootFrom(PURPLE_HANGING_CANVAS_SIGN), DyeColor.PURPLE));
	public static final Supplier<Block> BLUE_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("blue_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).lootFrom(BLUE_HANGING_CANVAS_SIGN), DyeColor.BLUE));
	public static final Supplier<Block> BROWN_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("brown_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).lootFrom(BROWN_HANGING_CANVAS_SIGN), DyeColor.BROWN));
	public static final Supplier<Block> GREEN_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("green_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).lootFrom(GREEN_HANGING_CANVAS_SIGN), DyeColor.GREEN));
	public static final Supplier<Block> RED_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("red_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).lootFrom(RED_HANGING_CANVAS_SIGN), DyeColor.RED));
	public static final Supplier<Block> BLACK_HANGING_CANVAS_WALL_SIGN = BLOCKS.register("black_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN).lootFrom(BLACK_HANGING_CANVAS_SIGN), DyeColor.BLACK));

	// Composting
	public static final Supplier<Block> BROWN_MUSHROOM_COLONY = BLOCKS.register("brown_mushroom_colony",
			() -> new MushroomColonyBlock(Items.BROWN_MUSHROOM.builtInRegistryHolder(), Block.Properties.ofFullCopy(Blocks.BROWN_MUSHROOM)));
	public static final Supplier<Block> RED_MUSHROOM_COLONY = BLOCKS.register("red_mushroom_colony",
			() -> new MushroomColonyBlock(Items.RED_MUSHROOM.builtInRegistryHolder(), Block.Properties.ofFullCopy(Blocks.RED_MUSHROOM)));
	public static final Supplier<Block> ORGANIC_COMPOST = BLOCKS.register("organic_compost",
			() -> new OrganicCompostBlock(Block.Properties.ofFullCopy(Blocks.DIRT).strength(1.2F).sound(SoundType.CROP)));
	public static final Supplier<Block> RICH_SOIL = BLOCKS.register("rich_soil",
			() -> new RichSoilBlock(Block.Properties.ofFullCopy(Blocks.DIRT).randomTicks()));
	public static final Supplier<Block> RICH_SOIL_FARMLAND = BLOCKS.register("rich_soil_farmland",
			() -> new RichSoilFarmlandBlock(Block.Properties.ofFullCopy(Blocks.FARMLAND)));

	// Pastries
	public static final Supplier<Block> APPLE_PIE = BLOCKS.register("apple_pie",
			() -> new PieBlock(Block.Properties.ofFullCopy(Blocks.CAKE), ModItems.APPLE_PIE_SLICE));
	public static final Supplier<Block> SWEET_BERRY_CHEESECAKE = BLOCKS.register("sweet_berry_cheesecake",
			() -> new PieBlock(Block.Properties.ofFullCopy(Blocks.CAKE), ModItems.SWEET_BERRY_CHEESECAKE_SLICE));
	public static final Supplier<Block> CHOCOLATE_PIE = BLOCKS.register("chocolate_pie",
			() -> new PieBlock(Block.Properties.ofFullCopy(Blocks.CAKE), ModItems.CHOCOLATE_PIE_SLICE));

	// Wild Crops
	public static final Supplier<Block> SANDY_SHRUB = BLOCKS.register("sandy_shrub",
			() -> new SandyShrubBlock(Block.Properties.ofFullCopy(Blocks.TALL_GRASS)));

	public static final Supplier<Block> WILD_CABBAGES = BLOCKS.register("wild_cabbages",
			() -> new WildCropBlock(MobEffects.DAMAGE_BOOST, 6, Block.Properties.ofFullCopy(Blocks.TALL_GRASS)));
	public static final Supplier<Block> WILD_ONIONS = BLOCKS.register("wild_onions",
			() -> new WildCropBlock(MobEffects.FIRE_RESISTANCE, 6, Block.Properties.ofFullCopy(Blocks.TALL_GRASS)));
	public static final Supplier<Block> WILD_TOMATOES = BLOCKS.register("wild_tomatoes",
			() -> new WildCropBlock(MobEffects.POISON, 10, Block.Properties.ofFullCopy(Blocks.TALL_GRASS)));
	public static final Supplier<Block> WILD_CARROTS = BLOCKS.register("wild_carrots",
			() -> new WildCropBlock(MobEffects.DIG_SLOWDOWN, 6, Block.Properties.ofFullCopy(Blocks.TALL_GRASS)));
	public static final Supplier<Block> WILD_POTATOES = BLOCKS.register("wild_potatoes",
			() -> new WildCropBlock(MobEffects.CONFUSION, 8, Block.Properties.ofFullCopy(Blocks.TALL_GRASS)));
	public static final Supplier<Block> WILD_BEETROOTS = BLOCKS.register("wild_beetroots",
			() -> new WildCropBlock(MobEffects.WATER_BREATHING, 8, Block.Properties.ofFullCopy(Blocks.TALL_GRASS)));
	public static final Supplier<Block> WILD_RICE = BLOCKS.register("wild_rice",
			() -> new WildRiceBlock(Block.Properties.ofFullCopy(Blocks.TALL_GRASS)));

	// Crops
	public static final Supplier<Block> CABBAGE_CROP = BLOCKS.register("cabbages",
			() -> new CabbageBlock(Block.Properties.ofFullCopy(Blocks.WHEAT)));
	public static final Supplier<Block> ONION_CROP = BLOCKS.register("onions",
			() -> new OnionBlock(Block.Properties.ofFullCopy(Blocks.WHEAT)));
	public static final Supplier<Block> BUDDING_TOMATO_CROP = BLOCKS.register("budding_tomatoes",
			() -> new BuddingTomatoBlock(Block.Properties.ofFullCopy(Blocks.WHEAT)));
	public static final Supplier<Block> TOMATO_CROP = BLOCKS.register("tomatoes",
			() -> new TomatoVineBlock(Block.Properties.ofFullCopy(Blocks.WHEAT)));
	public static final Supplier<Block> RICE_CROP = BLOCKS.register("rice",
			() -> new RiceBlock(Block.Properties.ofFullCopy(Blocks.WHEAT).strength(0.2F)));
	public static final Supplier<Block> RICE_CROP_PANICLES = BLOCKS.register("rice_panicles",
			() -> new RicePaniclesBlock(Block.Properties.ofFullCopy(Blocks.WHEAT)));

	// Feasts
	public static final Supplier<Block> ROAST_CHICKEN_BLOCK = BLOCKS.register("roast_chicken_block",
			() -> new RoastChickenBlock(Block.Properties.ofFullCopy(Blocks.CAKE), ModItems.ROAST_CHICKEN, true));
	public static final Supplier<Block> STUFFED_PUMPKIN_BLOCK = BLOCKS.register("stuffed_pumpkin_block",
			() -> new FeastBlock(Block.Properties.ofFullCopy(Blocks.PUMPKIN), ModItems.STUFFED_PUMPKIN, false));
	public static final Supplier<Block> HONEY_GLAZED_HAM_BLOCK = BLOCKS.register("honey_glazed_ham_block",
			() -> new HoneyGlazedHamBlock(Block.Properties.ofFullCopy(Blocks.CAKE), ModItems.HONEY_GLAZED_HAM, true));
	public static final Supplier<Block> SHEPHERDS_PIE_BLOCK = BLOCKS.register("shepherds_pie_block",
			() -> new ShepherdsPieBlock(Block.Properties.ofFullCopy(Blocks.CAKE), ModItems.SHEPHERDS_PIE, true));
	public static final Supplier<Block> RICE_ROLL_MEDLEY_BLOCK = BLOCKS.register("rice_roll_medley_block",
			() -> new RiceRollMedleyBlock(Block.Properties.ofFullCopy(Blocks.CAKE)));
}
