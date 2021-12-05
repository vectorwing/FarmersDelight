package vectorwing.farmersdelight.setup;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.registry.ModEffects;
import vectorwing.farmersdelight.utils.tags.ModTags;

import java.util.List;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TooltipEventHandler
{
	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		ItemStack soupStack = event.getItemStack();
		if (Configuration.MEAL_EFFECT_TOOLTIP.get() && Configuration.COMFORT_FOOD_TAG_EFFECT.get() && soupStack.getItem().is(ModTags.COMFORT_FOODS)) {
			List<ITextComponent> tooltip = event.getToolTip();
			EffectInstance comfort = new EffectInstance(ModEffects.COMFORT.get(), 2400);
			IFormattableTextComponent effectText = new TranslationTextComponent(comfort.getDescriptionId());
			if (comfort.getDuration() > 20) {
				effectText = new TranslationTextComponent("potion.withDuration", effectText, EffectUtils.formatDuration(comfort, 1));
			}
			tooltip.add(effectText.withStyle(comfort.getEffect().getCategory().getTooltipFormatting()));
		}
	}
}
