package vectorwing.farmersdelight.core;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.core.registry.ModBlocks;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public class FDCreativeModeTab extends CreativeModeTab
{
	public FDCreativeModeTab(String label) {
		super(label);
	}

	@Nonnull
	@Override
	public ItemStack makeIcon() {
		return new ItemStack(ModBlocks.STOVE.get());
	}
}
