package vectorwing.farmersdelight.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FuelItem extends Item
{
	public FuelItem(Properties properties)
	{
		super(properties);
	}

	@Override
	public int getBurnTime(ItemStack itemStack) {
		return 100;
	}
}
