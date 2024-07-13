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
	private static final MutableComponent NO_EFFECTS = Component.translatable("effect.none").withStyle(ChatFormatting.GRAY);

	/**
	 * Syntactic sugar for custom translation keys. Always prefixed with the mod's ID in lang files (e.g. farmersdelight.your.key.here).
	 */
	public static MutableComponent getTranslation(String key, Object... args) {
		return Component.translatable(FarmersDelight.MODID + "." + key, args);
	}

	/**
	 * An alternate version of PotionUtils.addPotionTooltip, that obtains the item's food-property potion effects instead.
	 */
	public static void addFoodEffectTooltip(ItemStack stack, Consumer<Component> tooltipAdder, float durationFactor, float tickRate) {
		FoodProperties foodStats = stack.getFoodProperties(null);
		if (foodStats == null) {
			return;
		}

		List<FoodProperties.PossibleEffect> effectList = foodStats.effects();
		List<Pair<Holder<Attribute>, AttributeModifier>> attributeList = Lists.newArrayList();
		MutableComponent mutableComponent;

		if (effectList.isEmpty()) {
			tooltipAdder.accept(NO_EFFECTS);
		} else {
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
