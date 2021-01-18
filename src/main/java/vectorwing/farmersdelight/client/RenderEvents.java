package vectorwing.farmersdelight.client;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.registry.ModItems;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class RenderEvents {

    @SubscribeEvent
    public static void playerRender(RenderPlayerEvent.Pre event) {
        PlayerEntity playerEntity = event.getPlayer();
        if (playerEntity.getHeldItem(Hand.MAIN_HAND).getItem() == ModItems.COOKING_POT.get() ||
                playerEntity.getHeldItem(Hand.OFF_HAND).getItem() == ModItems.COOKING_POT.get()) {
            PlayerModel<AbstractClientPlayerEntity> playerModel = event.getRenderer().getEntityModel();
            playerModel.leftArmPose = BipedModel.ArmPose.ITEM;
            playerModel.rightArmPose = BipedModel.ArmPose.ITEM;
        }
    }
}
