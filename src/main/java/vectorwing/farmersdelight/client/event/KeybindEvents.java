package vectorwing.farmersdelight.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.item.SkilletItem;
import vectorwing.farmersdelight.common.network.ModNetworking;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class KeybindEvents {
    @SubscribeEvent
    public static void preClientTick(TickEvent.ClientTickEvent event) { // Run this on pre so inputs don't get eaten up.
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player != null && player.isUsingItem()) {
            ItemStack useItem = player.getUseItem();
            if (useItem.getItem() instanceof SkilletItem && useItem.getOrCreateTag().getLong("FlipTimeStamp") == 0L) {
                while (mc.options.keyAttack.consumeClick()) {
                    ModNetworking.INSTANCE.sendToServer(new ModNetworking.FlipSkilletMessage());
                }
            }
        }
    }
}
