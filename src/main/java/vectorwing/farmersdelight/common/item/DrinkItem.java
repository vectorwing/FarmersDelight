package vectorwing.farmersdelight.common.item;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

/**
 * Variant of FoodItem that sounds and animates like a drink.
 * It expects the drink to have food properties.
 */
public class DrinkItem extends FoodItem
{
	public DrinkItem(Properties properties) {
		super(properties);
	}

	/**
	 * This constructor accepts a custom Component to be added to the tooltip.
	 *
	 * @param properties    Item properties.
	 * @param customTooltip A text component to be displayed as-is. Provide any formatting to it before passing it.
	 */
	public DrinkItem(Properties properties, MutableComponent customTooltip) {
		super(properties);
		this.isEffectSecret = false;
		this.customTooltip = customTooltip;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}

	@Override
	public SoundEvent getEatingSound() {
		return SoundEvents.GENERIC_DRINK;
	}
}
