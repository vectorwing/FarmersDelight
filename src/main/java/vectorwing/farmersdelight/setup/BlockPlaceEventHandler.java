package vectorwing.farmersdelight.setup;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.registry.ModItems;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID)
public class BlockPlaceEventHandler
{

	@SubscribeEvent
	public static void onPumpkinPiePlace(PlayerInteractEvent.RightClickBlock event) {
		ItemStack placedStack = event.getItemStack();
		PlayerEntity player = event.getPlayer();

		if (placedStack.getItem().equals(Items.PUMPKIN_PIE) && player.isSecondaryUseActive()) {
			BlockItemUseContext context = new BlockItemUseContext(player, event.getHand(), placedStack, event.getHitVec());
			((BlockItem) ModItems.BLOCKITEM_PUMPKIN_PIE.get()).place(context);
			event.setCanceled(true);
		}
	}

//	public static ActionResultType place(BlockItemUseContext context, Block block) {
//		if (!context.canPlace()) {
//			return ActionResultType.FAIL;
//		} else {
//			BlockState state = getPlacementState(context, block);
//			if (state == null) {
//				return ActionResultType.FAIL;
//			} else if (!context.getLevel().setBlock(context.getClickedPos(), state, 11)) {
//				return ActionResultType.FAIL;
//			} else {
//
//			}
//		}
//	}
//
//	public static BlockState getPlacementState(BlockItemUseContext context, Block block) {
//		BlockState blockstate = block.getStateForPlacement(context);
//		return blockstate != null && canPlace(context, blockstate) ? blockstate : null;
//	}
//
//	public static boolean canPlace(BlockItemUseContext context, BlockState state) {
//		PlayerEntity player = context.getPlayer();
//		World world = context.getLevel();
//		BlockPos clickedPos = context.getClickedPos();
//
//		ISelectionContext selectionContext = player == null ? ISelectionContext.empty() : ISelectionContext.of(player);
//		return (state.canSurvive(world, clickedPos)) && world.isUnobstructed(state, clickedPos, selectionContext);
//	}
}
