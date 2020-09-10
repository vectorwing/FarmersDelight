package vectorwing.farmersdelight.items;

import mezz.jei.api.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class DogFoodItem extends MealItem {
	public DogFoodItem(Properties builder) {
		super(builder);
	}

	public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
		if (target instanceof WolfEntity) {
			WolfEntity wolf = (WolfEntity) target;
			if (wolf.isAlive() && wolf.isTamed()) {
				target.addPotionEffect(new EffectInstance(Effects.REGENERATION, 600, 2));
				target.addPotionEffect(new EffectInstance(Effects.STRENGTH, 6000, 1));
				target.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 6000, 1));
				if (stack.getContainerItem() != ItemStack.EMPTY) {
					playerIn.addItemStackToInventory(stack.getContainerItem());
				}
				stack.shrink(1);
				return ActionResultType.SUCCESS;
			}
			return ActionResultType.PASS;
		}
		return ActionResultType.PASS;
	}
}
