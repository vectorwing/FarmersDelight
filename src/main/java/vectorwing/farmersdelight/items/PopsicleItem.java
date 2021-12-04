package vectorwing.farmersdelight.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PopsicleItem extends ConsumableItem
{
	public PopsicleItem(Properties properties) {
		super(properties);
	}

	@Override
	public void affectConsumer(ItemStack stack, World worldIn, LivingEntity consumer) {
		consumer.clearFire();
	}
}
