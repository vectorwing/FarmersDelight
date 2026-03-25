//package vectorwing.farmersdelight.client.event;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.client.event.ClientTickEvent;
//import vectorwing.farmersdelight.FarmersDelight;
//
//@EventBusSubscriber(modid = FarmersDelight.MODID, value = Dist.CLIENT)
//public class KeybindEvents
//{
//	@SubscribeEvent
//	public static void preClientTick(ClientTickEvent event) { // Run this on pre so inputs don't get eaten up.
//		Minecraft mc = Minecraft.getInstance();
//		Player player = mc.player;
//		if (player != null && player.isUsingItem()) {
//			ItemStack useItem = player.getUseItem();
//			// TODO: Clean up for 1.21 Skillet PR
////            if (useItem.getItem() instanceof SkilletItem && useItem.getOrCreateTag().getLong("FlipTimeStamp") == 0L) {
////                while (mc.options.keyAttack.consumeClick()) {
////                    ModNetworking.INSTANCE.sendToServer(new ModNetworking.FlipSkilletMessage());
////                }
////            }
//		}
//	}
//}
