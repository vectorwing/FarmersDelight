package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.entity.*;

import java.util.function.Supplier;

public class ModBlockEntityTypes
{
	public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, FarmersDelight.MODID);

	public static final Supplier<BlockEntityType<StoveBlockEntity>> STOVE = TILES.register("stove",
			() -> BlockEntityType.Builder.of(StoveBlockEntity::new, ModBlocks.STOVE.get()).build(null));
	public static final Supplier<BlockEntityType<CookingPotBlockEntity>> COOKING_POT = TILES.register("cooking_pot",
			() -> BlockEntityType.Builder.of(CookingPotBlockEntity::new, ModBlocks.COOKING_POT.get()).build(null));
	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BasketBlockEntity>> BASKET = TILES.register("basket",
			() -> BlockEntityType.Builder.of(BasketBlockEntity::new, ModBlocks.BASKET.get()).build(null));
	public static final Supplier<BlockEntityType<CuttingBoardBlockEntity>> CUTTING_BOARD = TILES.register("cutting_board",
			() -> BlockEntityType.Builder.of(CuttingBoardBlockEntity::new, ModBlocks.CUTTING_BOARD.get()).build(null));
	public static final Supplier<BlockEntityType<SkilletBlockEntity>> SKILLET = TILES.register("skillet",
			() -> BlockEntityType.Builder.of(SkilletBlockEntity::new, ModBlocks.SKILLET.get()).build(null));
	public static final Supplier<BlockEntityType<CabinetBlockEntity>> CABINET = TILES.register("cabinet",
			() -> BlockEntityType.Builder.of(CabinetBlockEntity::new,
							ModBlocks.OAK_CABINET.get(),
							ModBlocks.BIRCH_CABINET.get(),
							ModBlocks.SPRUCE_CABINET.get(),
							ModBlocks.JUNGLE_CABINET.get(),
							ModBlocks.ACACIA_CABINET.get(),
							ModBlocks.DARK_OAK_CABINET.get(),
							ModBlocks.MANGROVE_CABINET.get(),
							ModBlocks.BAMBOO_CABINET.get(),
							ModBlocks.CHERRY_CABINET.get(),
							ModBlocks.CRIMSON_CABINET.get(),
							ModBlocks.WARPED_CABINET.get())
					.build(null));
	public static final Supplier<BlockEntityType<CanvasSignBlockEntity>> CANVAS_SIGN = TILES.register("canvas_sign",
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
	public static final Supplier<BlockEntityType<HangingCanvasSignBlockEntity>> HANGING_CANVAS_SIGN = TILES.register("hanging_canvas_sign",
			() -> BlockEntityType.Builder.of(HangingCanvasSignBlockEntity::new,
							ModBlocks.HANGING_CANVAS_SIGN.get(),
							ModBlocks.WHITE_HANGING_CANVAS_SIGN.get(),
							ModBlocks.ORANGE_HANGING_CANVAS_SIGN.get(),
							ModBlocks.MAGENTA_HANGING_CANVAS_SIGN.get(),
							ModBlocks.LIGHT_BLUE_HANGING_CANVAS_SIGN.get(),
							ModBlocks.YELLOW_HANGING_CANVAS_SIGN.get(),
							ModBlocks.LIME_HANGING_CANVAS_SIGN.get(),
							ModBlocks.PINK_HANGING_CANVAS_SIGN.get(),
							ModBlocks.GRAY_HANGING_CANVAS_SIGN.get(),
							ModBlocks.LIGHT_GRAY_HANGING_CANVAS_SIGN.get(),
							ModBlocks.CYAN_HANGING_CANVAS_SIGN.get(),
							ModBlocks.PURPLE_HANGING_CANVAS_SIGN.get(),
							ModBlocks.BLUE_HANGING_CANVAS_SIGN.get(),
							ModBlocks.BROWN_HANGING_CANVAS_SIGN.get(),
							ModBlocks.GREEN_HANGING_CANVAS_SIGN.get(),
							ModBlocks.RED_HANGING_CANVAS_SIGN.get(),
							ModBlocks.BLACK_HANGING_CANVAS_SIGN.get(),
							ModBlocks.HANGING_CANVAS_WALL_SIGN.get(),
							ModBlocks.WHITE_HANGING_CANVAS_WALL_SIGN.get(),
							ModBlocks.ORANGE_HANGING_CANVAS_WALL_SIGN.get(),
							ModBlocks.MAGENTA_HANGING_CANVAS_WALL_SIGN.get(),
							ModBlocks.LIGHT_BLUE_HANGING_CANVAS_WALL_SIGN.get(),
							ModBlocks.YELLOW_HANGING_CANVAS_WALL_SIGN.get(),
							ModBlocks.LIME_HANGING_CANVAS_WALL_SIGN.get(),
							ModBlocks.PINK_HANGING_CANVAS_WALL_SIGN.get(),
							ModBlocks.GRAY_HANGING_CANVAS_WALL_SIGN.get(),
							ModBlocks.LIGHT_GRAY_HANGING_CANVAS_WALL_SIGN.get(),
							ModBlocks.CYAN_HANGING_CANVAS_WALL_SIGN.get(),
							ModBlocks.PURPLE_HANGING_CANVAS_WALL_SIGN.get(),
							ModBlocks.BLUE_HANGING_CANVAS_WALL_SIGN.get(),
							ModBlocks.BROWN_HANGING_CANVAS_WALL_SIGN.get(),
							ModBlocks.GREEN_HANGING_CANVAS_WALL_SIGN.get(),
							ModBlocks.RED_HANGING_CANVAS_WALL_SIGN.get(),
							ModBlocks.BLACK_HANGING_CANVAS_WALL_SIGN.get())
					.build(null));
}
