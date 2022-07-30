package vectorwing.farmersdelight.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;

public class PopsicleItem extends ConsumableItem
{
	public PopsicleItem(Properties properties) {
		super(properties);
	}

	@Override
	public void affectConsumer(ItemStack stack, Level worldIn, LivingEntity consumer) {
		consumer.clearFire();
	}
}
