package vectorwing.farmersdelight.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nullable;
import java.util.List;

public class ConsumableItem extends Item
{
	private final boolean hasFoodEffectTooltip;
	private final boolean hasCustomTooltip;

	/**
	 * Items that can be consumed by an entity.
	 * When consumed, they may affect the consumer somehow, and will give back containers if applicable, regardless of their stack size.
	 */
	public ConsumableItem(Properties properties) {
		super(properties);
		this.hasFoodEffectTooltip = false;
		this.hasCustomTooltip = false;
	}

	public ConsumableItem(Properties properties, boolean hasFoodEffectTooltip) {
		super(properties);
		this.hasFoodEffectTooltip = hasFoodEffectTooltip;
		this.hasCustomTooltip = false;
	}

	public ConsumableItem(Properties properties, boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
		super(properties);
		this.hasFoodEffectTooltip = hasFoodEffectTooltip;
		this.hasCustomTooltip = hasCustomTooltip;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
		if (!level.isClientSide) {
			this.affectConsumer(stack, level, consumer);
		}

		ItemStack containerStack = stack.getCraftingRemainingItem();

		if (stack.getFoodProperties(consumer) != null) {
			super.finishUsingItem(stack, level, consumer);
		} else {
			Player player = consumer instanceof Player ? (Player) consumer : null;
			if (player instanceof ServerPlayer) {
				CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
			}
			if (player != null) {
				player.awardStat(Stats.ITEM_USED.get(this));
				if (!player.getAbilities().instabuild) {
					stack.shrink(1);
				}
			}
		}

		if (stack.isEmpty()) {
			return containerStack;
		} else {
			if (consumer instanceof Player player && !((Player) consumer).getAbilities().instabuild) {
				if (!player.getInventory().add(containerStack)) {
					player.drop(containerStack, false);
				}
			}
			return stack;
		}
	}

	/**
	 * Override this to apply changes to the consumer (e.g. curing effects).
	 */
	public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag isAdvanced) {
		if (Configuration.FOOD_EFFECT_TOOLTIP.get()) {
			if (this.hasCustomTooltip) {
				MutableComponent textEmpty = TextUtils.getTranslation("tooltip." + BuiltInRegistries.ITEM.getKey(this).getPath());
				tooltip.add(textEmpty.withStyle(ChatFormatting.BLUE));
			}
			if (this.hasFoodEffectTooltip) {
				TextUtils.addFoodEffectTooltip(stack, tooltip::add, 1.0F, context.tickRate());
			}
		}
	}
}
