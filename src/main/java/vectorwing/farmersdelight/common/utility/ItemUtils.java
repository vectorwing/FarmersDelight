package vectorwing.farmersdelight.common.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import vectorwing.farmersdelight.common.item.KnifeItem;
import vectorwing.farmersdelight.common.tag.ModTags;

/**
 * Util for handling ItemStacks and inventories containing them.
 */
public class ItemUtils
{
	/**
	 * Shorthand method for checking if the given stack either has a required ToolAction, or is otherwise part of a given tag.
	 *
	 * @param toolAction  The ToolAction to check for
	 * @param fallbackTag An item tag to check for, if the given ToolAction is absent
	 * @return true if either condition matches
	 */
	public static boolean isValidTool(ItemStack stack, ItemAbility toolAction, TagKey<Item> fallbackTag) {
		return stack.canPerformAction(toolAction) || stack.is(fallbackTag);
	}

	public static boolean isKnife(ItemStack stack) {
		return isValidTool(stack, KnifeItem.KNIFE_HARVEST, ModTags.Items.KNIVES);
	}

	public static void dropItems(Level level, BlockPos pos, ResourceHandler<ItemResource> inventory) {
		for (int slot = 0; slot < inventory.size(); slot++)
			Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), inventory.getResource(slot).toStack());
	}

	public static void clearItems(ItemStacksResourceHandler inventory) {
		for (int i = 0; i < inventory.size(); i++) {
			inventory.set(i, ItemResource.of(ItemStack.EMPTY), 0);
		}
	}

	public static boolean doesInventoryHaveItems(ResourceHandler<ItemResource> inventory) {
		for (int i = 0; i < inventory.size(); i++) {
			if (!inventory.getResource(i).isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public static void spawnItemEntity(Level level, ItemStack stack, double x, double y, double z, double xMotion, double yMotion, double zMotion) {
		ItemEntity entity = new ItemEntity(level, x, y, z, stack);
		entity.setDeltaMovement(xMotion, yMotion, zMotion);
		level.addFreshEntity(entity);
	}
}
