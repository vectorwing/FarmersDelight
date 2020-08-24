package vectorwing.farmersdelight.blocks;

import net.minecraft.item.ItemStack;
import vectorwing.farmersdelight.registry.ModItems;

public class ApplePieBlock extends PieBlock {
	public ApplePieBlock(Properties properties) {
		super(properties);
	}

	public ItemStack getPieSliceItem() {
		return new ItemStack(ModItems.APPLE_PIE_SLICE.get());
	}
}
