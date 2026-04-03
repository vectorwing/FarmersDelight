package vectorwing.farmersdelight.common.utility;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.FarmersDelight;

import java.util.List;
import java.util.Map;

/**
 * Util for obtaining and formatting ITextComponents for use across the mod.
 */
public class TextUtils
{
	public static final MutableComponent PLACEABLE = tooltip("placeable").withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC);
	public static final MutableComponent PLACEABLE_SNEAKING = tooltip("placeable_sneaking").withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC);
	public static final MutableComponent DEBUG_ITEM = tooltip("debug_item").withStyle(ChatFormatting.RED);

	/**
	 * Syntactic sugar for custom translation keys. Always prefixed with the mod's ID in lang files (e.g. farmersdelight.your.key.here).
	 */
	public static MutableComponent getTranslation(String key, Object... args) {
		return Component.translatable(FarmersDelight.MODID + "." + key, args);
	}

	/**
	 * Gets text from a translation key, where "type" prefixes Farmer's Delight's mod ID.
	 * Example: "type.farmersdelight.key".
	 *
	 * @param translationType The type of lang being read, added as a prefix
	 * @param translationKey  The key itself, added as a suffix after the mod ID
	 * @param args            Additional values to be keyed into the text, through markers such as %s
	 */
	public static MutableComponent getTextWithType(String translationType, String translationKey, Object... args) {
		return Component.translatable(translationType + "." + FarmersDelight.MODID + "." + translationKey, args);
	}

	public static MutableComponent block(String key, Object... args) {
		return getTextWithType("block", key, args);
	}

	public static MutableComponent item(String key, Object... args) {
		return getTextWithType("item", key, args);
	}

	public static MutableComponent advancement(String key, Object... args) {
		return getTextWithType("advancements", key, args);
	}

	public static MutableComponent container(String key, Object... args) {
		return getTextWithType("container", key, args);
	}

	public static MutableComponent JEI(String key, Object... args) {
		return getTextWithType("jei", key, args);
	}

	public static MutableComponent tooltip(String key, Object... args) {
		return getTextWithType("tooltip", key, args);
	}

	public static String subtitleKey(String key, Object... args) {
		return getTextWithType("subtitles", key, args).getString();
	}

	/**
	 * An alternate version of PotionUtils.addPotionTooltip, that obtains the item's food property potion effects instead.
	 */
	public static void addFoodEffectTooltip(ItemStack stack, List<Component> lores, float durationFactor) {
		FoodProperties foodStats = stack.getItem().getFoodProperties(stack, null);
		if (foodStats == null) {
			return;
		}
		List<Pair<MobEffectInstance, Float>> effectList = foodStats.getEffects();
		List<Pair<Attribute, AttributeModifier>> attributeList = Lists.newArrayList();
		if (!effectList.isEmpty()) {
			for (Pair<MobEffectInstance, Float> effectPair : effectList) {
				MobEffectInstance instance = effectPair.getFirst();
				MutableComponent iformattabletextcomponent = Component.translatable(instance.getDescriptionId());
				MobEffect effect = instance.getEffect();
				Map<Attribute, AttributeModifier> attributeMap = effect.getAttributeModifiers();
				if (!attributeMap.isEmpty()) {
					for (Map.Entry<Attribute, AttributeModifier> entry : attributeMap.entrySet()) {
						AttributeModifier rawModifier = entry.getValue();
						AttributeModifier modifier = new AttributeModifier(rawModifier.getName(), effect.getAttributeModifierValue(instance.getAmplifier(), rawModifier), rawModifier.getOperation());
						attributeList.add(new Pair<>(entry.getKey(), modifier));
					}
				}

				if (instance.getAmplifier() > 0) {
					iformattabletextcomponent = Component.translatable("potion.withAmplifier", iformattabletextcomponent, Component.translatable("potion.potency." + instance.getAmplifier()));
				}

				if (instance.getDuration() > 20) {
					iformattabletextcomponent = Component.translatable("potion.withDuration", iformattabletextcomponent, MobEffectUtil.formatDuration(instance, durationFactor));
				}

				lores.add(iformattabletextcomponent.withStyle(effect.getCategory().getTooltipFormatting()));
			}
		}

		if (!attributeList.isEmpty()) {
			lores.add(CommonComponents.EMPTY);
			lores.add((Component.translatable("potion.whenDrank")).withStyle(ChatFormatting.DARK_PURPLE));

			for (Pair<Attribute, AttributeModifier> pair : attributeList) {
				AttributeModifier modifier = pair.getSecond();
				double amount = modifier.getAmount();
				double formattedAmount;
				if (modifier.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && modifier.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
					formattedAmount = modifier.getAmount();
				} else {
					formattedAmount = modifier.getAmount() * 100.0D;
				}

				if (amount > 0.0D) {
					lores.add((Component.translatable("attribute.modifier.plus." + modifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(formattedAmount), Component.translatable(pair.getFirst().getDescriptionId()))).withStyle(ChatFormatting.BLUE));
				} else if (amount < 0.0D) {
					formattedAmount = formattedAmount * -1.0D;
					lores.add((Component.translatable("attribute.modifier.take." + modifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(formattedAmount), Component.translatable(pair.getFirst().getDescriptionId()))).withStyle(ChatFormatting.RED));
				}
			}
		}

	}
}
