package vectorwing.farmersdelight.registry;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.blocks.*;
import vectorwing.farmersdelight.blocks.signs.StandingCanvasSignBlock;
import vectorwing.farmersdelight.blocks.signs.WallCanvasSignBlock;

import java.util.Set;
import java.util.function.ToIntFunction;

import com.google.common.collect.Sets;

public class ModBlocks
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FarmersDelight.MODID);

	private static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
		return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
	}

	// Workstations
	public static final RegistryObject<Block> STOVE = BLOCKS.register("stove", () -> new StoveBlock(AbstractBlock.Properties.copy(Blocks.BRICKS).lightLevel(getLightValueLit(13))));
	public static final RegistryObject<Block> COOKING_POT = BLOCKS.register("cooking_pot", CookingPotBlock::new);
	public static final RegistryObject<Block> SKILLET = BLOCKS.register("skillet", SkilletBlock::new);
	public static final RegistryObject<Block> BASKET = BLOCKS.register("basket", BasketBlock::new);
	public static final RegistryObject<Block> CUTTING_BOARD = BLOCKS.register("cutting_board", CuttingBoardBlock::new);

	// Crop Storage
	public static final RegistryObject<Block> CARROT_CRATE = BLOCKS.register("carrot_crate",
			() -> new Block(Block.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> POTATO_CRATE = BLOCKS.register("potato_crate",
			() -> new Block(Block.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> BEETROOT_CRATE = BLOCKS.register("beetroot_crate",
			() -> new Block(Block.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> CABBAGE_CRATE = BLOCKS.register("cabbage_crate",
			() -> new Block(Block.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> TOMATO_CRATE = BLOCKS.register("tomato_crate",
			() -> new Block(Block.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> ONION_CRATE = BLOCKS.register("onion_crate",
			() -> new Block(Block.Properties.of(Material.WOOD).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> RICE_BALE = BLOCKS.register("rice_bale",
			() -> new RiceBaleBlock(Block.Properties.copy(Blocks.HAY_BLOCK).harvestTool(ToolType.HOE)));
	public static final RegistryObject<Block> RICE_BAG = BLOCKS.register("rice_bag",
			() -> new Block(Block.Properties.copy(Blocks.WHITE_WOOL).harvestTool(ToolType.HOE)));
	public static final RegistryObject<Block> STRAW_BALE = BLOCKS.register("straw_bale",
			() -> new StrawBaleBlock(Block.Properties.copy(Blocks.HAY_BLOCK).harvestTool(ToolType.HOE)));

	// Building
	public static final Set<Block> PANTRIES = Sets.newHashSet();
	public static final RegistryObject<Block> ROPE = BLOCKS.register("rope", RopeBlock::new);
	public static final RegistryObject<Block> SAFETY_NET = BLOCKS.register("safety_net", SafetyNetBlock::new);
	public static final RegistryObject<Block> OAK_PANTRY = BLOCKS.register("oak_pantry",
			() -> new PantryBlock(Block.Properties.copy(Blocks.BARREL)));
	public static final RegistryObject<Block> BIRCH_PANTRY = BLOCKS.register("birch_pantry",
			() -> new PantryBlock(Block.Properties.copy(Blocks.BARREL)));
	public static final RegistryObject<Block> SPRUCE_PANTRY = BLOCKS.register("spruce_pantry",
			() -> new PantryBlock(Block.Properties.copy(Blocks.BARREL)));
	public static final RegistryObject<Block> JUNGLE_PANTRY = BLOCKS.register("jungle_pantry",
			() -> new PantryBlock(Block.Properties.copy(Blocks.BARREL)));
	public static final RegistryObject<Block> ACACIA_PANTRY = BLOCKS.register("acacia_pantry",
			() -> new PantryBlock(Block.Properties.copy(Blocks.BARREL)));
	public static final RegistryObject<Block> DARK_OAK_PANTRY = BLOCKS.register("dark_oak_pantry",
			() -> new PantryBlock(Block.Properties.copy(Blocks.BARREL)));
	public static final RegistryObject<Block> CRIMSON_PANTRY = BLOCKS.register("crimson_pantry",
			() -> new PantryBlock(Block.Properties.copy(Blocks.BARREL)));
	public static final RegistryObject<Block> WARPED_PANTRY = BLOCKS.register("warped_pantry",
			() -> new PantryBlock(Block.Properties.copy(Blocks.BARREL)));
	public static final RegistryObject<Block> CANVAS_RUG = BLOCKS.register("canvas_rug", CanvasRugBlock::new);
	public static final RegistryObject<Block> TATAMI = BLOCKS.register("tatami", TatamiBlock::new);
	public static final RegistryObject<Block> FULL_TATAMI_MAT = BLOCKS.register("full_tatami_mat", TatamiMatBlock::new);
	public static final RegistryObject<Block> HALF_TATAMI_MAT = BLOCKS.register("half_tatami_mat", TatamiHalfMatBlock::new);

	public static final RegistryObject<Block> CANVAS_SIGN = BLOCKS.register("canvas_sign",
			() -> new StandingCanvasSignBlock(null));
	public static final RegistryObject<Block> WHITE_CANVAS_SIGN = BLOCKS.register("white_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.WHITE));
	public static final RegistryObject<Block> ORANGE_CANVAS_SIGN = BLOCKS.register("orange_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.ORANGE));
	public static final RegistryObject<Block> MAGENTA_CANVAS_SIGN = BLOCKS.register("magenta_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.MAGENTA));
	public static final RegistryObject<Block> LIGHT_BLUE_CANVAS_SIGN = BLOCKS.register("light_blue_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.LIGHT_BLUE));
	public static final RegistryObject<Block> YELLOW_CANVAS_SIGN = BLOCKS.register("yellow_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.YELLOW));
	public static final RegistryObject<Block> LIME_CANVAS_SIGN = BLOCKS.register("lime_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.LIME));
	public static final RegistryObject<Block> PINK_CANVAS_SIGN = BLOCKS.register("pink_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.PINK));
	public static final RegistryObject<Block> GRAY_CANVAS_SIGN = BLOCKS.register("gray_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.GRAY));
	public static final RegistryObject<Block> LIGHT_GRAY_CANVAS_SIGN = BLOCKS.register("light_gray_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.LIGHT_GRAY));
	public static final RegistryObject<Block> CYAN_CANVAS_SIGN = BLOCKS.register("cyan_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.CYAN));
	public static final RegistryObject<Block> PURPLE_CANVAS_SIGN = BLOCKS.register("purple_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.PURPLE));
	public static final RegistryObject<Block> BLUE_CANVAS_SIGN = BLOCKS.register("blue_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.BLUE));
	public static final RegistryObject<Block> BROWN_CANVAS_SIGN = BLOCKS.register("brown_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.BROWN));
	public static final RegistryObject<Block> GREEN_CANVAS_SIGN = BLOCKS.register("green_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.GREEN));
	public static final RegistryObject<Block> RED_CANVAS_SIGN = BLOCKS.register("red_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.RED));
	public static final RegistryObject<Block> BLACK_CANVAS_SIGN = BLOCKS.register("black_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.BLACK));

	public static final RegistryObject<Block> CANVAS_WALL_SIGN = BLOCKS.register("canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.copy(Blocks.OAK_SIGN).lootFrom(CANVAS_SIGN), null));
	public static final RegistryObject<Block> WHITE_CANVAS_WALL_SIGN = BLOCKS.register("white_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.copy(Blocks.OAK_SIGN).lootFrom(WHITE_CANVAS_SIGN), DyeColor.WHITE));
	public static final RegistryObject<Block> ORANGE_CANVAS_WALL_SIGN = BLOCKS.register("orange_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.copy(Blocks.OAK_SIGN).lootFrom(ORANGE_CANVAS_SIGN), DyeColor.ORANGE));
	public static final RegistryObject<Block> MAGENTA_CANVAS_WALL_SIGN = BLOCKS.register("magenta_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.copy(Blocks.OAK_SIGN).lootFrom(MAGENTA_CANVAS_SIGN), DyeColor.MAGENTA));
	public static final RegistryObject<Block> LIGHT_BLUE_CANVAS_WALL_SIGN = BLOCKS.register("light_blue_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.copy(Blocks.OAK_SIGN).lootFrom(LIGHT_BLUE_CANVAS_SIGN), DyeColor.LIGHT_BLUE));
	public static final RegistryObject<Block> YELLOW_CANVAS_WALL_SIGN = BLOCKS.register("yellow_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.copy(Blocks.OAK_SIGN).lootFrom(YELLOW_CANVAS_SIGN), DyeColor.YELLOW));
	public static final RegistryObject<Block> LIME_CANVAS_WALL_SIGN = BLOCKS.register("lime_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.copy(Blocks.OAK_SIGN).lootFrom(LIME_CANVAS_SIGN), DyeColor.LIME));
	public static final RegistryObject<Block> PINK_CANVAS_WALL_SIGN = BLOCKS.register("pink_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.copy(Blocks.OAK_SIGN).lootFrom(PINK_CANVAS_SIGN), DyeColor.PINK));
	public static final RegistryObject<Block> GRAY_CANVAS_WALL_SIGN = BLOCKS.register("gray_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.copy(Blocks.OAK_SIGN).lootFrom(GRAY_CANVAS_SIGN), DyeColor.GRAY));
	public static final RegistryObject<Block> LIGHT_GRAY_CANVAS_WALL_SIGN = BLOCKS.register("light_gray_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.copy(Blocks.OAK_SIGN).lootFrom(LIGHT_GRAY_CANVAS_SIGN), DyeColor.LIGHT_GRAY));
	public static final RegistryObject<Block> CYAN_CANVAS_WALL_SIGN = BLOCKS.register("cyan_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.copy(Blocks.OAK_SIGN).lootFrom(CYAN_CANVAS_SIGN), DyeColor.CYAN));
	public static final RegistryObject<Block> PURPLE_CANVAS_WALL_SIGN = BLOCKS.register("purple_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.copy(Blocks.OAK_SIGN).lootFrom(PURPLE_CANVAS_SIGN), DyeColor.PURPLE));
	public static final RegistryObject<Block> BLUE_CANVAS_WALL_SIGN = BLOCKS.register("blue_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.copy(Blocks.OAK_SIGN).lootFrom(BLUE_CANVAS_SIGN), DyeColor.BLUE));
	public static final RegistryObject<Block> BROWN_CANVAS_WALL_SIGN = BLOCKS.register("brown_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.copy(Blocks.OAK_SIGN).lootFrom(BROWN_CANVAS_SIGN), DyeColor.BROWN));
	public static final RegistryObject<Block> GREEN_CANVAS_WALL_SIGN = BLOCKS.register("green_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.copy(Blocks.OAK_SIGN).lootFrom(GREEN_CANVAS_SIGN), DyeColor.GREEN));
	public static final RegistryObject<Block> RED_CANVAS_WALL_SIGN = BLOCKS.register("red_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.copy(Blocks.OAK_SIGN).lootFrom(RED_CANVAS_SIGN), DyeColor.RED));
	public static final RegistryObject<Block> BLACK_CANVAS_WALL_SIGN = BLOCKS.register("black_canvas_wall_sign",
			() -> new WallCanvasSignBlock(Block.Properties.copy(Blocks.OAK_SIGN).lootFrom(BLACK_CANVAS_SIGN), DyeColor.BLACK));

	// Composting
	public static final RegistryObject<Block> BROWN_MUSHROOM_COLONY = BLOCKS.register("brown_mushroom_colony",
			() -> new MushroomColonyBlock(Block.Properties.copy(Blocks.BROWN_MUSHROOM), Items.BROWN_MUSHROOM.delegate));
	public static final RegistryObject<Block> RED_MUSHROOM_COLONY = BLOCKS.register("red_mushroom_colony",
			() -> new MushroomColonyBlock(Block.Properties.copy(Blocks.RED_MUSHROOM), Items.RED_MUSHROOM.delegate));
	public static final RegistryObject<Block> ORGANIC_COMPOST = BLOCKS.register("organic_compost",
			() -> new OrganicCompostBlock(Block.Properties.copy(Blocks.DIRT).strength(1.2F).sound(SoundType.CROP).harvestTool(ToolType.SHOVEL)));
	public static final RegistryObject<Block> RICH_SOIL = BLOCKS.register("rich_soil",
			() -> new RichSoilBlock(Block.Properties.copy(Blocks.DIRT).harvestTool(ToolType.SHOVEL).randomTicks()));
	public static final RegistryObject<Block> RICH_SOIL_FARMLAND = BLOCKS.register("rich_soil_farmland",
			() -> new RichSoilFarmlandBlock(Block.Properties.copy(Blocks.FARMLAND).harvestTool(ToolType.SHOVEL)));

	// Pastries
	public static final RegistryObject<Block> APPLE_PIE = BLOCKS.register("apple_pie",
			() -> new PieBlock(Block.Properties.copy(Blocks.CAKE), ModItems.APPLE_PIE_SLICE));
	public static final RegistryObject<Block> SWEET_BERRY_CHEESECAKE = BLOCKS.register("sweet_berry_cheesecake",
			() -> new PieBlock(Block.Properties.copy(Blocks.CAKE), ModItems.SWEET_BERRY_CHEESECAKE_SLICE));
	public static final RegistryObject<Block> CHOCOLATE_PIE = BLOCKS.register("chocolate_pie",
			() -> new PieBlock(Block.Properties.copy(Blocks.CAKE), ModItems.CHOCOLATE_PIE_SLICE));

	// Wild Crops
	public static final RegistryObject<Block> WILD_CABBAGES = BLOCKS.register("wild_cabbages",
			() -> new WildPatchBlock(Block.Properties.copy(Blocks.TALL_GRASS)));
	public static final RegistryObject<Block> WILD_ONIONS = BLOCKS.register("wild_onions",
			() -> new WildCropsBlock(Block.Properties.copy(Blocks.TALL_GRASS)));
	public static final RegistryObject<Block> WILD_TOMATOES = BLOCKS.register("wild_tomatoes",
			() -> new WildPatchBlock(Block.Properties.copy(Blocks.TALL_GRASS)));
	public static final RegistryObject<Block> WILD_CARROTS = BLOCKS.register("wild_carrots",
			() -> new WildCropsBlock(Block.Properties.copy(Blocks.TALL_GRASS)));
	public static final RegistryObject<Block> WILD_POTATOES = BLOCKS.register("wild_potatoes",
			() -> new WildPatchBlock(Block.Properties.copy(Blocks.TALL_GRASS)));
	public static final RegistryObject<Block> WILD_BEETROOTS = BLOCKS.register("wild_beetroots",
			() -> new WildPatchBlock(Block.Properties.copy(Blocks.TALL_GRASS)));
	public static final RegistryObject<Block> WILD_RICE = BLOCKS.register("wild_rice",
			() -> new WildRiceBlock(Block.Properties.copy(Blocks.TALL_GRASS)));

	// Crops
	public static final RegistryObject<Block> CABBAGE_CROP = BLOCKS.register("cabbages",
			() -> new CabbagesBlock(Block.Properties.copy(Blocks.WHEAT)));
	public static final RegistryObject<Block> ONION_CROP = BLOCKS.register("onions",
			() -> new OnionsBlock(Block.Properties.copy(Blocks.WHEAT)));
	public static final RegistryObject<Block> TOMATO_CROP = BLOCKS.register("tomatoes",
			() -> new TomatoesBlock(Block.Properties.copy(Blocks.WHEAT)));
	public static final RegistryObject<Block> RICE_CROP = BLOCKS.register("rice_crop",
			() -> new RiceCropBlock(Block.Properties.copy(Blocks.WHEAT).strength(0.2F)));
	public static final RegistryObject<Block> RICE_UPPER_CROP = BLOCKS.register("rice_upper_crop",
			() -> new RiceUpperCropBlock(Block.Properties.copy(Blocks.WHEAT)));

	// Feasts
	public static final RegistryObject<Block> ROAST_CHICKEN_BLOCK = BLOCKS.register("roast_chicken_block",
			() -> new RoastChickenBlock(Block.Properties.copy(Blocks.WHITE_WOOL), ModItems.ROAST_CHICKEN, true));
	public static final RegistryObject<Block> STUFFED_PUMPKIN_BLOCK = BLOCKS.register("stuffed_pumpkin_block",
			() -> new FeastBlock(Block.Properties.copy(Blocks.PUMPKIN), ModItems.STUFFED_PUMPKIN, false));
	public static final RegistryObject<Block> HONEY_GLAZED_HAM_BLOCK = BLOCKS.register("honey_glazed_ham_block",
			() -> new HoneyGlazedHamBlock(Block.Properties.copy(Blocks.WHITE_WOOL), ModItems.HONEY_GLAZED_HAM, true));
	public static final RegistryObject<Block> SHEPHERDS_PIE_BLOCK = BLOCKS.register("shepherds_pie_block",
			() -> new ShepherdsPieBlock(Block.Properties.copy(Blocks.CAKE), ModItems.SHEPHERDS_PIE, true));
}
