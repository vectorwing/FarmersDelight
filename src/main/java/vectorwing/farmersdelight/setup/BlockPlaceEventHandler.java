package vectorwing.farmersdelight.setup;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
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
			ActionResultType result = ModItems.DEBUG_PUMPKIN_PIE.get().useOn(context);
			event.setCanceled(true);
			event.setCancellationResult(result);
		}
	}
}
