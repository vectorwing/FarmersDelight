package vectorwing.farmersdelight.client.event;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.FoodValues;

import java.util.List;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, value = Dist.CLIENT)
public class TooltipEvents
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
				MutableComponent effectText = new TranslatableComponent(effect.getDescriptionId());
				if (effect.getDuration() > 20) {
					effectText = new TranslatableComponent("potion.withDuration", effectText, MobEffectUtil.formatDuration(effect, 1));
				}
				tooltip.add(effectText.withStyle(effect.getEffect().getCategory().getTooltipFormatting()));
			}
		}
	}
}
