package vectorwing.farmersdelight.common.block.entity.inventory;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

/**
 * Wrapper for ItemStacksResourceHandler.
 */
public class RecipeWrapper implements RecipeInput {

	private final ItemStacksResourceHandler handler;

	public RecipeWrapper(ItemStacksResourceHandler handler) {
		this.handler = handler;
	}

	@Override
	public ItemStack getItem(int slot) {
		return handler.getResource(slot).toStack();
	}

	@Override
	public int size() {
		return handler.size();
	}
}