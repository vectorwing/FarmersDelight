package vectorwing.farmersdelight.utils;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vectorwing.farmersdelight.FarmersDelight;

import java.util.List;
import java.util.Map;

/**
 * Util for obtaining and formatting ITextComponents for use across the mod.
 */

public class TextUtils
{
	private static final IFormattableTextComponent NO_EFFECTS = (new TranslationTextComponent("effect.none")).withStyle(TextFormatting.GRAY);

	/**
	 * Syntactic sugar for custom translation keys. Always prefixed with the mod's ID in lang files (e.g. farmersdelight.your.key.here).
	 */
	public static IFormattableTextComponent getTranslation(String key, Object... args) {
		return new TranslationTextComponent(FarmersDelight.MODID + "." + key, args);
	}

	/**
	 * An alternate version of PotionUtils.addPotionTooltip, that obtains the item's food-property potion effects instead.
	 */
	@OnlyIn(Dist.CLIENT)
	public static void addFoodEffectTooltip(ItemStack itemIn, List<ITextComponent> lores, float durationFactor) {
		Food stackFood = itemIn.getItem().getFoodProperties();
		if (stackFood == null) {
			return;
		}
		List<Pair<EffectInstance, Float>> list = stackFood.getEffects();
		List<Pair<Attribute, AttributeModifier>> list1 = Lists.newArrayList();
		if (list.isEmpty()) {
			lores.add(NO_EFFECTS);
		} else {
			for (Pair<EffectInstance, Float> effectPair : list) {
				EffectInstance instance = effectPair.getFirst();
				IFormattableTextComponent iformattabletextcomponent = new TranslationTextComponent(instance.getDescriptionId());
				Effect effect = instance.getEffect();
				Map<Attribute, AttributeModifier> map = effect.getAttributeModifiers();
				if (!map.isEmpty()) {
					for(Map.Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
						AttributeModifier attributemodifier = entry.getValue();
						AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), effect.getAttributeModifierValue(instance.getAmplifier(), attributemodifier), attributemodifier.getOperation());
						list1.add(new Pair<>(entry.getKey(), attributemodifier1));
					}
				}

				if (instance.getAmplifier() > 0) {
					iformattabletextcomponent = new TranslationTextComponent("potion.withAmplifier", iformattabletextcomponent, new TranslationTextComponent("potion.potency." + instance.getAmplifier()));
				}

				if (instance.getDuration() > 20) {
					iformattabletextcomponent = new TranslationTextComponent("potion.withDuration", iformattabletextcomponent, EffectUtils.formatDuration(instance, durationFactor));
				}

				lores.add(iformattabletextcomponent.withStyle(effect.getCategory().getTooltipFormatting()));
			}
		}

		if (!list1.isEmpty()) {
			lores.add(StringTextComponent.EMPTY);
			lores.add((new TranslationTextComponent("potion.whenDrank")).withStyle(TextFormatting.DARK_PURPLE));

			for(Pair<Attribute, AttributeModifier> pair : list1) {
				AttributeModifier attributemodifier2 = pair.getSecond();
				double d0 = attributemodifier2.getAmount();
				double d1;
				if (attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && attributemodifier2.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
					d1 = attributemodifier2.getAmount();
				} else {
					d1 = attributemodifier2.getAmount() * 100.0D;
				}

				if (d0 > 0.0D) {
					lores.add((new TranslationTextComponent("attribute.modifier.plus." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), new TranslationTextComponent(pair.getFirst().getDescriptionId()))).withStyle(TextFormatting.BLUE));
				} else if (d0 < 0.0D) {
					d1 = d1 * -1.0D;
					lores.add((new TranslationTextComponent("attribute.modifier.take." + attributemodifier2.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(d1), new TranslationTextComponent(pair.getFirst().getDescriptionId()))).withStyle(TextFormatting.RED));
				}
			}
		}

	}
}
