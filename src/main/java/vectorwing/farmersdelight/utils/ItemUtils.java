package vectorwing.farmersdelight.utils;

import net.minecraft.inventory.InventoryHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

/**
 * Util for handling ItemStacks and inventories containing them.
 */
public class ItemUtils
{
	public static void dropItems(World world, BlockPos pos, IItemHandler inventory) {
		for (int slot = 0; slot < inventory.getSlots(); slot++)
			InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), inventory.getStackInSlot(slot));
	}

	public static boolean isInventoryEmpty(IItemHandler inventory) {
		for (int i = 0; i < inventory.getSlots(); i++) {
			if (!inventory.getStackInSlot(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}
}
