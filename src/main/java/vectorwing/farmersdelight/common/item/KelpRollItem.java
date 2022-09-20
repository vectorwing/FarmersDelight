package vectorwing.farmersdelight.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class KelpRollItem extends Item
{
	public KelpRollItem(Properties properties) {
		super(properties);
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 64;
	}
}
