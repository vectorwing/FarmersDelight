package vectorwing.farmersdelight.core.event;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.core.Configuration;
import vectorwing.farmersdelight.core.registry.ModEffects;
import vectorwing.farmersdelight.core.tag.ModTags;

import java.util.List;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TooltipEvents
{
	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		ItemStack soupStack = event.getItemStack();
		if (Configuration.FOOD_EFFECT_TOOLTIP.get() && Configuration.COMFORT_FOOD_TAG_EFFECT.get() && ModTags.COMFORT_FOODS.contains(soupStack.getItem())) {
			List<Component> tooltip = event.getToolTip();
			MobEffectInstance comfort = new MobEffectInstance(ModEffects.COMFORT.get(), 2400);
			MutableComponent effectText = new TranslatableComponent(comfort.getDescriptionId());
			if (comfort.getDuration() > 20) {
				effectText = new TranslatableComponent("potion.withDuration", effectText, MobEffectUtil.formatDuration(comfort, 1));
			}
			tooltip.add(effectText.withStyle(comfort.getEffect().getCategory().getTooltipFormatting()));
		}
	}
}
