package vectorwing.farmersdelight.registry;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.tile.*;

public class ModTileEntityTypes
{
	public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, FarmersDelight.MODID);

	public static final RegistryObject<TileEntityType<StoveTileEntity>> STOVE_TILE = TILES.register("stove",
			() -> TileEntityType.Builder.create(StoveTileEntity::new, ModBlocks.STOVE.get()).build(null));
	public static final RegistryObject<TileEntityType<CookingPotTileEntity>> COOKING_POT_TILE = TILES.register("cooking_pot",
			() -> TileEntityType.Builder.create(CookingPotTileEntity::new, ModBlocks.COOKING_POT.get()).build(null));
	public static final RegistryObject<TileEntityType<BasketTileEntity>> BASKET_TILE = TILES.register("basket",
			() -> TileEntityType.Builder.create(BasketTileEntity::new, ModBlocks.BASKET.get()).build(null));
	public static final RegistryObject<TileEntityType<CuttingBoardTileEntity>> CUTTING_BOARD_TILE = TILES.register("cutting_board",
			() -> TileEntityType.Builder.create(CuttingBoardTileEntity::new, ModBlocks.CUTTING_BOARD.get()).build(null));
	public static final RegistryObject<TileEntityType<SkilletTileEntity>> SKILLET_TILE = TILES.register("skillet",
			() -> TileEntityType.Builder.create(SkilletTileEntity::new, ModBlocks.SKILLET.get()).build(null));
	public static final RegistryObject<TileEntityType<PantryTileEntity>> PANTRY_TILE = TILES.register("pantry",
			() -> TileEntityType.Builder.create(PantryTileEntity::new,
					ModBlocks.OAK_PANTRY.get(), ModBlocks.BIRCH_PANTRY.get(), ModBlocks.SPRUCE_PANTRY.get(), ModBlocks.JUNGLE_PANTRY.get(), ModBlocks.ACACIA_PANTRY.get(), ModBlocks.DARK_OAK_PANTRY.get())
					.build(null));
	public static final RegistryObject<TileEntityType<CanvasSignTileEntity>> CANVAS_SIGN_TILE = TILES.register("canvas_sign",
			() -> TileEntityType.Builder.create(CanvasSignTileEntity::new,
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
					ModBlocks.BLACK_CANVAS_SIGN.get(),
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
					ModBlocks.BLACK_CANVAS_WALL_SIGN.get())
					.build(null));
}
