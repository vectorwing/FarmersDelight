package vectorwing.farmersdelight.items.drinks;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class MelonJuiceItem extends DrinkItem
{
	public MelonJuiceItem(Properties properties) {
		super(properties, false, true);
	}

	@Override
	public void affectConsumer(ItemStack stack, Level worldIn, LivingEntity consumer) {
		consumer.heal(2.0F);
	}
}
