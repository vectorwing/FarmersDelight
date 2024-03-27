package vectorwing.farmersdelight.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.item.SkilletItem;
import vectorwing.farmersdelight.common.networking.ModNetworking;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.List;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, value = Dist.CLIENT)
public class ClientEvents
{
	@SubscribeEvent
	public static void addTooltipToVanillaSoups(ItemTooltipEvent event) {
		if (!Configuration.VANILLA_SOUP_EXTRA_EFFECTS.get()) {
			return;
		}

		Item food = event.getItemStack().getItem();

		FoodProperties soupEffects = FoodValues.VANILLA_SOUP_EFFECTS.get(food);

		if (soupEffects != null) {
			List<Component> tooltip = event.getToolTip();
			for (Pair<MobEffectInstance, Float> pair : soupEffects.getEffects()) {
				MobEffectInstance effect = pair.getFirst();
				MutableComponent effectText = Component.translatable(effect.getDescriptionId());
				if (effect.getDuration() > 20) {
					effectText = Component.translatable("potion.withDuration", effectText, MobEffectUtil.formatDuration(effect, 1));
				}
				tooltip.add(effectText.withStyle(effect.getEffect().getCategory().getTooltipFormatting()));
			}
		}
	}

	@SubscribeEvent
	public static void onMouseClicked(InputEvent.MouseButton event){
		if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_1 && event.getAction() == InputConstants.PRESS) {
			var player = Minecraft.getInstance().player;
			if (player != null && player.isUsingItem()) {
				if (player.getUseItem().getItem() instanceof SkilletItem) {
					ModNetworking.CHANNEL.sendToServer(new ModNetworking.FlipSkilletMessage());
				}
			}
		}
	}


}
