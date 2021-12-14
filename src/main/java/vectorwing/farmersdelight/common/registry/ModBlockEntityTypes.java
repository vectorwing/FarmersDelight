package vectorwing.farmersdelight.common.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.entity.*;

public class ModBlockEntityTypes
{
	public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, FarmersDelight.MODID);

	public static final RegistryObject<BlockEntityType<StoveBlockEntity>> STOVE = TILES.register("stove",
			() -> BlockEntityType.Builder.of(StoveBlockEntity::new, ModBlocks.STOVE.get()).build(null));
	public static final RegistryObject<BlockEntityType<CookingPotBlockEntity>> COOKING_POT = TILES.register("cooking_pot",
			() -> BlockEntityType.Builder.of(CookingPotBlockEntity::new, ModBlocks.COOKING_POT.get()).build(null));
	public static final RegistryObject<BlockEntityType<BasketBlockEntity>> BASKET = TILES.register("basket",
			() -> BlockEntityType.Builder.of(BasketBlockEntity::new, ModBlocks.BASKET.get()).build(null));
	public static final RegistryObject<BlockEntityType<CuttingBoardBlockEntity>> CUTTING_BOARD = TILES.register("cutting_board",
			() -> BlockEntityType.Builder.of(CuttingBoardBlockEntity::new, ModBlocks.CUTTING_BOARD.get()).build(null));
	public static final RegistryObject<BlockEntityType<SkilletBlockEntity>> SKILLET = TILES.register("skillet",
			() -> BlockEntityType.Builder.of(SkilletBlockEntity::new, ModBlocks.SKILLET.get()).build(null));
	public static final RegistryObject<BlockEntityType<CabinetBlockEntity>> PANTRY = TILES.register("pantry",
			() -> BlockEntityType.Builder.of(CabinetBlockEntity::new,
							ModBlocks.OAK_CABINET.get(), ModBlocks.BIRCH_CABINET.get(), ModBlocks.SPRUCE_CABINET.get(), ModBlocks.JUNGLE_CABINET.get(), ModBlocks.ACACIA_CABINET.get(), ModBlocks.DARK_OAK_CABINET.get())
					.build(null));
	public static final RegistryObject<BlockEntityType<CabinetBlockEntity>> CABINET = TILES.register("cabinet",
			() -> BlockEntityType.Builder.of(CabinetBlockEntity::new,
							ModBlocks.OAK_CABINET.get(), ModBlocks.BIRCH_CABINET.get(), ModBlocks.SPRUCE_CABINET.get(), ModBlocks.JUNGLE_CABINET.get(), ModBlocks.ACACIA_CABINET.get(), ModBlocks.DARK_OAK_CABINET.get())
					.build(null));
	public static final RegistryObject<BlockEntityType<CanvasSignBlockEntity>> CANVAS_SIGN = TILES.register("canvas_sign",
			() -> BlockEntityType.Builder.of(CanvasSignBlockEntity::new,
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
