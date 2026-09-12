package vectorwing.farmersdelight.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nullable;
import java.util.List;

/**
 * <p>Simplified copy of ConsumableItem for items which always have food properties defined.</p>
 * <p>Instead of giving the craft remainder when eaten, this class expects you to define {@link FoodProperties#usingConvertsTo()} instead.
 * Still, defining a craft remainder is still good practice for modded recipe use.</p>
 */
public class FoodItem extends Item
{
	@Nullable
	private final MutableComponent customTooltip;
	private final boolean isEffectSecret;

	public FoodItem(Properties properties) {
		super(properties);
		this.isEffectSecret = false;
		this.customTooltip = null;
	}

	/**
	 * This constructor lets you hide the tooltip, for effects that should be secret.
	 */
	public FoodItem(Properties properties, boolean isEffectSecret) {
		super(properties);
		this.isEffectSecret = isEffectSecret;
		this.customTooltip = null;
	}

	/**
	 * This constructor accepts a custom Component to be added to the tooltip.
	 *
	 * @param properties    Item properties.
	 * @param customTooltip A text component to be displayed as-is. Provide any formatting to it before passing it.
	 */
	public FoodItem(Properties properties, MutableComponent customTooltip) {
		super(properties);
		this.isEffectSecret = false;
		this.customTooltip = customTooltip;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
		if (!level.isClientSide) {
			this.affectConsumer(stack, level, consumer);
		}
		return super.finishUsingItem(stack, level, consumer);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag isAdvanced) {
		if (!Configuration.ENABLE_FOOD_EFFECT_TOOLTIP.get() || isEffectSecret) {
			return;
		}
		if (customTooltip != null) {
			tooltip.add(customTooltip);
		} else {
			TextUtils.addFoodEffectTooltip(stack, tooltip::add, 1.0F, context.tickRate());
		}
	}

	/**
	 * Override this to apply changes to the consumer (e.g. curing effects).
	 */
	public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
	}
}
