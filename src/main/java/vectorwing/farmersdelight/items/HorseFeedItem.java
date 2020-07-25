package vectorwing.farmersdelight.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;

public class HorseFeedItem extends MealItem
{
	public HorseFeedItem(Properties builder)
	{
		super(builder);
	}

	public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
		if (target instanceof HorseEntity) {
			HorseEntity horse = (HorseEntity)target;
			if (horse.isAlive() && horse.isTame()) {
				horse.setJumping(true);
				target.addPotionEffect(new EffectInstance(Effects.REGENERATION, 600, 2));
				target.addPotionEffect(new EffectInstance(Effects.SPEED, 6000, 2));
				target.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 6000, 1));
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
