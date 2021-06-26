package vectorwing.farmersdelight.items.drinks;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MelonJuiceItem extends DrinkItem
{
	public MelonJuiceItem(Properties properties) {
		super(properties, false, true);
	}

	@Override
	public void affectConsumer(ItemStack stack, World worldIn, LivingEntity consumer) {
		consumer.heal(2.0F);
	}
}
