package vectorwing.farmersdelight.blocks;

import net.minecraft.item.ItemStack;
import vectorwing.farmersdelight.registry.ModItems;

public class SweetBerryCheesecakeBlock extends PieBlock {
	public SweetBerryCheesecakeBlock(Properties properties) {
		super(properties);
	}

	public ItemStack getPieSliceItem() {
		return new ItemStack(ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get());
	}
}
