package vectorwing.farmersdelight.common.utility;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
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
import net.minecraft.world.item.component.ItemAttributeModifiers;
import vectorwing.farmersdelight.FarmersDelight;

import java.util.List;
import java.util.function.Consumer;

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
	public static void addFoodEffectTooltip(ItemStack stack, Consumer<Component> tooltipAdder, float durationFactor, float tickRate) {
		FoodProperties foodStats = stack.getFoodProperties(null);
		if (foodStats == null) {
			return;
		}

		List<FoodProperties.PossibleEffect> effectList = foodStats.effects();
		List<Pair<Holder<Attribute>, AttributeModifier>> attributeList = Lists.newArrayList();
		MutableComponent mutableComponent;

		if (!effectList.isEmpty()) {
			for (FoodProperties.PossibleEffect possibleEffect : effectList) {
				MobEffectInstance instance = possibleEffect.effect();
				mutableComponent = Component.translatable(instance.getDescriptionId());
				MobEffect effect = instance.getEffect().value();
				effect.createModifiers(instance.getAmplifier(), (attributeHolder, attributeModifier) -> {
					attributeList.add(new Pair<>(attributeHolder, attributeModifier));
				});

				if (instance.getAmplifier() > 0) {
					mutableComponent = Component.translatable("potion.withAmplifier", mutableComponent, Component.translatable("potion.potency." + instance.getAmplifier()));
				}

				if (instance.getDuration() > 20) {
					mutableComponent = Component.translatable("potion.withDuration", mutableComponent, MobEffectUtil.formatDuration(instance, durationFactor, tickRate));
				}

				tooltipAdder.accept(mutableComponent.withStyle(effect.getCategory().getTooltipFormatting()));
			}
		}

		if (!attributeList.isEmpty()) {
			tooltipAdder.accept(CommonComponents.EMPTY);
			tooltipAdder.accept(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));

			for (Pair<Holder<Attribute>, AttributeModifier> pair : attributeList) {
				AttributeModifier attributemodifier = pair.getSecond();
				double amount = attributemodifier.amount();
				double formattedAmount;
				if (attributemodifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_BASE && attributemodifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
					formattedAmount = attributemodifier.amount();
				} else {
					formattedAmount = attributemodifier.amount() * 100.0;
				}

				if (amount > 0.0) {
					tooltipAdder.accept(Component.translatable("attribute.modifier.plus." + attributemodifier.operation().id(), new Object[]{ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(formattedAmount), Component.translatable(((Attribute) ((Holder) pair.getFirst()).value()).getDescriptionId())}).withStyle(ChatFormatting.BLUE));
				} else if (amount < 0.0) {
					formattedAmount *= -1.0;
					tooltipAdder.accept(Component.translatable("attribute.modifier.take." + attributemodifier.operation().id(), new Object[]{ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(formattedAmount), Component.translatable(((Attribute) ((Holder) pair.getFirst()).value()).getDescriptionId())}).withStyle(ChatFormatting.RED));
				}
			}
		}
	}
}
