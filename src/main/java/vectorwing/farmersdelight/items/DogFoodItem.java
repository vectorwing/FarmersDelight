package vectorwing.farmersdelight.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;

public class DogFoodItem extends MealItem
{
	public DogFoodItem(Properties builder)
	{
		super(builder);
	}

	public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
		if (target instanceof WolfEntity) {
			WolfEntity wolf = (WolfEntity)target;
			if (wolf.isAlive() && wolf.isTamed()) {
				wolf.setJumping(true);
				target.addPotionEffect(new EffectInstance(Effects.REGENERATION, 600, 2));
				target.addPotionEffect(new EffectInstance(Effects.STRENGTH, 6000, 1));
				target.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 6000, 1));
				if (stack.getContainerItem() != ItemStack.EMPTY) {
					playerIn.addItemStackToInventory(stack.getContainerItem());
				}
				stack.shrink(1);
				return true;
			}
			return false;
		}
		return false;
	}
}
