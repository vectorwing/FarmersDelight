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

import java.util.function.ToIntFunction;

public class ModBlocks
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FarmersDelight.MODID);

	private static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
		return (state) -> state.get(BlockStateProperties.LIT) ? lightValue : 0;
	}

	// Workstations
	public static final RegistryObject<Block> STOVE = BLOCKS.register("stove", () -> new StoveBlock(AbstractBlock.Properties.from(Blocks.BRICKS).setLightLevel(getLightValueLit(13))));
	public static final RegistryObject<Block> COOKING_POT = BLOCKS.register("cooking_pot", CookingPotBlock::new);
	public static final RegistryObject<Block> BASKET = BLOCKS.register("basket", BasketBlock::new);
	public static final RegistryObject<Block> CUTTING_BOARD = BLOCKS.register("cutting_board", CuttingBoardBlock::new);

	// Crop Storage
	public static final RegistryObject<Block> CARROT_CRATE = BLOCKS.register("carrot_crate",
			() -> new Block(Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> POTATO_CRATE = BLOCKS.register("potato_crate",
			() -> new Block(Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> BEETROOT_CRATE = BLOCKS.register("beetroot_crate",
			() -> new Block(Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> CABBAGE_CRATE = BLOCKS.register("cabbage_crate",
			() -> new Block(Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> TOMATO_CRATE = BLOCKS.register("tomato_crate",
			() -> new Block(Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> ONION_CRATE = BLOCKS.register("onion_crate",
			() -> new Block(Block.Properties.create(Material.WOOD).hardnessAndResistance(2.0F, 3.0F).sound(SoundType.WOOD)));
	public static final RegistryObject<Block> RICE_BALE = BLOCKS.register("rice_bale",
			() -> new RiceBaleBlock(Block.Properties.from(Blocks.HAY_BLOCK).harvestTool(ToolType.HOE)));
	public static final RegistryObject<Block> RICE_BAG = BLOCKS.register("rice_bag",
			() -> new Block(Block.Properties.from(Blocks.WHITE_WOOL).harvestTool(ToolType.HOE)));
	public static final RegistryObject<Block> STRAW_BALE = BLOCKS.register("straw_bale",
			() -> new StrawBaleBlock(Block.Properties.from(Blocks.HAY_BLOCK).harvestTool(ToolType.HOE)));

	// Building
	public static final RegistryObject<Block> ROPE = BLOCKS.register("rope", RopeBlock::new);
	public static final RegistryObject<Block> SAFETY_NET = BLOCKS.register("safety_net", SafetyNetBlock::new);
	public static final RegistryObject<Block> OAK_PANTRY = BLOCKS.register("oak_pantry",
			() -> new PantryBlock(Block.Properties.from(Blocks.BARREL)));
	public static final RegistryObject<Block> BIRCH_PANTRY = BLOCKS.register("birch_pantry",
			() -> new PantryBlock(Block.Properties.from(Blocks.BARREL)));
	public static final RegistryObject<Block> SPRUCE_PANTRY = BLOCKS.register("spruce_pantry",
			() -> new PantryBlock(Block.Properties.from(Blocks.BARREL)));
	public static final RegistryObject<Block> JUNGLE_PANTRY = BLOCKS.register("jungle_pantry",
			() -> new PantryBlock(Block.Properties.from(Blocks.BARREL)));
	public static final RegistryObject<Block> ACACIA_PANTRY = BLOCKS.register("acacia_pantry",
			() -> new PantryBlock(Block.Properties.from(Blocks.BARREL)));
	public static final RegistryObject<Block> DARK_OAK_PANTRY = BLOCKS.register("dark_oak_pantry",
			() -> new PantryBlock(Block.Properties.from(Blocks.BARREL)));
	public static final RegistryObject<Block> CRIMSON_PANTRY = BLOCKS.register("crimson_pantry",
			() -> new PantryBlock(Block.Properties.from(Blocks.BARREL)));
	public static final RegistryObject<Block> WARPED_PANTRY = BLOCKS.register("warped_pantry",
			() -> new PantryBlock(Block.Properties.from(Blocks.BARREL)));
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

	public static final RegistryObject<Block> CANVAS_WALL_SIGN = BLOCKS.register("canvas_wall_sign",
			() -> new WallCanvasSignBlock(null));
	public static final RegistryObject<Block> WHITE_CANVAS_WALL_SIGN = BLOCKS.register("white_canvas_wall_sign",
			() -> new WallCanvasSignBlock(DyeColor.WHITE));
	public static final RegistryObject<Block> ORANGE_CANVAS_WALL_SIGN = BLOCKS.register("orange_canvas_wall_sign",
			() -> new WallCanvasSignBlock(DyeColor.ORANGE));
	public static final RegistryObject<Block> MAGENTA_CANVAS_WALL_SIGN = BLOCKS.register("magenta_canvas_wall_sign",
			() -> new WallCanvasSignBlock(DyeColor.MAGENTA));

	// Composting
	public static final RegistryObject<Block> BROWN_MUSHROOM_COLONY = BLOCKS.register("brown_mushroom_colony",
			() -> new MushroomColonyBlock(Block.Properties.from(Blocks.BROWN_MUSHROOM), Items.BROWN_MUSHROOM.delegate));
	public static final RegistryObject<Block> RED_MUSHROOM_COLONY = BLOCKS.register("red_mushroom_colony",
			() -> new MushroomColonyBlock(Block.Properties.from(Blocks.RED_MUSHROOM), Items.RED_MUSHROOM.delegate));
	public static final RegistryObject<Block> ORGANIC_COMPOST = BLOCKS.register("organic_compost",
			() -> new OrganicCompostBlock(Block.Properties.from(Blocks.DIRT).hardnessAndResistance(1.2F).sound(SoundType.CROP).harvestTool(ToolType.SHOVEL)));
	public static final RegistryObject<Block> RICH_SOIL = BLOCKS.register("rich_soil",
			() -> new RichSoilBlock(Block.Properties.from(Blocks.DIRT).harvestTool(ToolType.SHOVEL).tickRandomly()));
	public static final RegistryObject<Block> RICH_SOIL_FARMLAND = BLOCKS.register("rich_soil_farmland",
			() -> new RichSoilFarmlandBlock(Block.Properties.from(Blocks.FARMLAND).harvestTool(ToolType.SHOVEL)));

	// Pastries
	public static final RegistryObject<Block> APPLE_PIE = BLOCKS.register("apple_pie",
			() -> new PieBlock(Block.Properties.from(Blocks.CAKE), ModItems.APPLE_PIE_SLICE));
	public static final RegistryObject<Block> SWEET_BERRY_CHEESECAKE = BLOCKS.register("sweet_berry_cheesecake",
			() -> new PieBlock(Block.Properties.from(Blocks.CAKE), ModItems.SWEET_BERRY_CHEESECAKE_SLICE));
	public static final RegistryObject<Block> CHOCOLATE_PIE = BLOCKS.register("chocolate_pie",
			() -> new PieBlock(Block.Properties.from(Blocks.CAKE), ModItems.CHOCOLATE_PIE_SLICE));

	// Wild Crops
	public static final RegistryObject<Block> WILD_CABBAGES = BLOCKS.register("wild_cabbages",
			() -> new WildPatchBlock(Block.Properties.from(Blocks.TALL_GRASS)));
	public static final RegistryObject<Block> WILD_ONIONS = BLOCKS.register("wild_onions",
			() -> new WildCropsBlock(Block.Properties.from(Blocks.TALL_GRASS)));
	public static final RegistryObject<Block> WILD_TOMATOES = BLOCKS.register("wild_tomatoes",
			() -> new WildPatchBlock(Block.Properties.from(Blocks.TALL_GRASS)));
	public static final RegistryObject<Block> WILD_CARROTS = BLOCKS.register("wild_carrots",
			() -> new WildCropsBlock(Block.Properties.from(Blocks.TALL_GRASS)));
	public static final RegistryObject<Block> WILD_POTATOES = BLOCKS.register("wild_potatoes",
			() -> new WildPatchBlock(Block.Properties.from(Blocks.TALL_GRASS)));
	public static final RegistryObject<Block> WILD_BEETROOTS = BLOCKS.register("wild_beetroots",
			() -> new WildPatchBlock(Block.Properties.from(Blocks.TALL_GRASS)));
	public static final RegistryObject<Block> WILD_RICE = BLOCKS.register("wild_rice",
			() -> new WildRiceBlock(Block.Properties.from(Blocks.TALL_GRASS)));

	// Crops
	public static final RegistryObject<Block> CABBAGE_CROP = BLOCKS.register("cabbages",
			() -> new CabbagesBlock(Block.Properties.from(Blocks.WHEAT)));
	public static final RegistryObject<Block> ONION_CROP = BLOCKS.register("onions",
			() -> new OnionsBlock(Block.Properties.from(Blocks.WHEAT)));
	public static final RegistryObject<Block> TOMATO_CROP = BLOCKS.register("tomatoes",
			() -> new TomatoesBlock(Block.Properties.from(Blocks.WHEAT)));
	public static final RegistryObject<Block> RICE_CROP = BLOCKS.register("rice_crop",
			() -> new RiceCropBlock(Block.Properties.from(Blocks.WHEAT)));
	public static final RegistryObject<Block> RICE_UPPER_CROP = BLOCKS.register("rice_upper_crop",
			() -> new RiceUpperCropBlock(Block.Properties.from(Blocks.WHEAT)));

	// Feasts
	public static final RegistryObject<Block> ROAST_CHICKEN_BLOCK = BLOCKS.register("roast_chicken_block",
			() -> new RoastChickenBlock(Block.Properties.from(Blocks.WHITE_WOOL), ModItems.ROAST_CHICKEN, true));
	public static final RegistryObject<Block> STUFFED_PUMPKIN_BLOCK = BLOCKS.register("stuffed_pumpkin_block",
			() -> new FeastBlock(Block.Properties.from(Blocks.PUMPKIN), ModItems.STUFFED_PUMPKIN, false));
	public static final RegistryObject<Block> HONEY_GLAZED_HAM_BLOCK = BLOCKS.register("honey_glazed_ham_block",
			() -> new HoneyGlazedHamBlock(Block.Properties.from(Blocks.WHITE_WOOL), ModItems.HONEY_GLAZED_HAM, true));
	public static final RegistryObject<Block> SHEPHERDS_PIE_BLOCK = BLOCKS.register("shepherds_pie_block",
			() -> new ShepherdsPieBlock(Block.Properties.from(Blocks.CAKE), ModItems.SHEPHERDS_PIE, true));

	@Deprecated
	public static final RegistryObject<Block> TALL_RICE_CROP = BLOCKS.register("tall_rice_crop",
			() -> new LegacyTallRiceCropBlock(Block.Properties.from(Blocks.WHEAT)));
}
