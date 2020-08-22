package vectorwing.farmersdelight.blocks;

import net.minecraft.item.ItemStack;
import vectorwing.farmersdelight.registry.ModItems;

public class ChocolatePieBlock extends PieBlock {
	public ChocolatePieBlock(Properties properties) {
		super(properties);
	}

	public ItemStack getPieSliceItem() {
		return new ItemStack(ModItems.CHOCOLATE_PIE_SLICE.get());
	}
}
