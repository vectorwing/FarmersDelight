package vectorwing.farmersdelight.client.event;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

@EventBusSubscriber(modid = FarmersDelight.MODID, value = Dist.CLIENT)
public class TooltipEvents
{
	@SubscribeEvent
	public static void addTooltipToVanillaSoups(ItemTooltipEvent event) {
		Item food = event.getItemStack().getItem();

		if (food.equals(Items.PUMPKIN_PIE)) {
			event.getToolTip().add(Configuration.ENABLE_PUMPKIN_PIE_SNEAK_TO_PLACE.get() ? TextUtils.PLACEABLE_SNEAKING : TextUtils.PLACEABLE);
		}

		if (!Configuration.ENABLE_VANILLA_SOUP_EXTRA_EFFECTS.get() || !Configuration.ENABLE_FOOD_EFFECT_TOOLTIP.get()) {
			return;
		}

		ApplyStatusEffectsConsumeEffect soupEffects = FoodValues.ConsumableValues.VANILLA_SOUP_EFFECTS.get(food);

		if (soupEffects != null) {
			List<Component> tooltip = event.getToolTip();
			for (MobEffectInstance effectInstance : soupEffects.effects()) {
				MutableComponent effectText = Component.translatable(effectInstance.getDescriptionId());
				Player player = event.getEntity();
				if (effectInstance.getDuration() > 20) {
					effectText = Component.translatable("potion.withDuration", effectText, MobEffectUtil.formatDuration(effectInstance, 1, player == null ? 20 : player.level().tickRateManager().tickrate()));
				}
				tooltip.add(effectText.withStyle(effectInstance.getEffect().value().getCategory().getTooltipFormatting()));
			}
		}
	}
}
