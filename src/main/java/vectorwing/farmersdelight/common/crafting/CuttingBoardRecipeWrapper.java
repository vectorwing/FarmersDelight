package vectorwing.farmersdelight.common.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class CuttingBoardRecipeWrapper extends RecipeWrapper {
	public CuttingBoardRecipeWrapper(IItemHandlerModifiable inv) {
		super(inv);
	}

	@Override
	public ItemStack getItem(int p_345528_) {
		if (p_345528_ != 0) {
			throw new IllegalArgumentException("No item for index " + p_345528_);
		} else {
			return this.inv.getStackInSlot(0);
		}
	}

	@Override
	public int getContainerSize() {
		return 2;	
	}
}
