package vectorwing.farmersdelight.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.item.SkilletItem;
import vectorwing.farmersdelight.common.networking.ModNetworking;
import vectorwing.farmersdelight.common.registry.ModDataComponents;
import vectorwing.farmersdelight.common.registry.ModItems;

@EventBusSubscriber(modid = FarmersDelight.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class KeybindEvents {
    @SubscribeEvent
    public static void preClientTick(ClientTickEvent.Pre event) { // Run this on pre so inputs don't get eaten up.
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player != null && player.isUsingItem()) {
            ItemStack useItem = player.getUseItem();
            if (useItem.getItem() instanceof SkilletItem && !useItem.has(ModDataComponents.SKILLET_FLIP_TIMESTAMP.get())) {
                while (mc.options.keyAttack.consumeClick()) {
                    PacketDistributor.sendToServer(ModNetworking.FlipSkilletMessage.INSTANCE);
                }
            }
        }
    }
}
