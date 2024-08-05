package vectorwing.farmersdelight.common.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class DrinkableItem extends ConsumableItem
{
	public DrinkableItem(Properties properties) {
		super(properties);
	}

	public DrinkableItem(Properties properties, boolean hasFoodEffectTooltip) {
		super(properties, hasFoodEffectTooltip);
	}

	public DrinkableItem(Properties properties, boolean hasPotionEffectTooltip, boolean hasCustomTooltip) {
		super(properties, hasPotionEffectTooltip, hasCustomTooltip);
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack heldStack = player.getItemInHand(hand);
		if (heldStack.isEdible()) {
			if (player.canEat(heldStack.getFoodProperties(player).canAlwaysEat())) {
				player.startUsingItem(hand);
				return InteractionResultHolder.consume(heldStack);
			} else {
				return InteractionResultHolder.fail(heldStack);
			}
		}
		return ItemUtils.startUsingInstantly(level, player, hand);
	}
}
