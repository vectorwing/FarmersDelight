package vectorwing.farmersdelight.setup;

import com.mojang.datafixers.util.Pair;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
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
import vectorwing.farmersdelight.items.Foods;

import java.util.List;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, value = Dist.CLIENT)
public class TooltipEventHandler
{
	@SubscribeEvent
	public static void addTooltipToVanillaSoups(ItemTooltipEvent event) {
		if (!Configuration.FOOD_EFFECT_TOOLTIP.get() || !Configuration.COMFORT_FOOD_TAG_EFFECT.get()) {
			return;
		}

		Item food = event.getItemStack().getItem();
		Food soupEffects = Foods.VANILLA_SOUP_EFFECTS.get(food);

		if (soupEffects != null) {
			List<ITextComponent> tooltip = event.getToolTip();
			for (Pair<EffectInstance, Float> pair : soupEffects.getEffects()) {
				EffectInstance effect = pair.getFirst();
				IFormattableTextComponent effectText = new TranslationTextComponent(effect.getDescriptionId());
				if (effect.getDuration() > 20) {
					effectText = new TranslationTextComponent("potion.withDuration", effectText, EffectUtils.formatDuration(effect, 1));
				}
				tooltip.add(effectText.withStyle(effect.getEffect().getCategory().getTooltipFormatting()));
			}
		}
	}
}
