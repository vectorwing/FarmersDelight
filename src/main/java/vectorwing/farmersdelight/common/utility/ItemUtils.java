package vectorwing.farmersdelight.common.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import vectorwing.farmersdelight.common.item.KnifeItem;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Optional;

/**
 * Util for handling ItemStacks and inventories containing them.
 */
public class ItemUtils
{
	/**
	 * Shorthand method for checking if the given stack either has a required ToolAction, or is otherwise part of a given tag.
	 * @param toolAction The ToolAction to check for
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
		for (int slot = 0; slot < inventory.size(); slot++) {
			ItemResource resource = inventory.getResource(slot);
			int amount = inventory.getAmountAsInt(slot);
			if (!resource.isEmpty() && amount > 0) {
				Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), resource.toStack(amount));
			}
		}
	}

	public static void clearItems(ResourceHandler<ItemResource> inventory) {
		try (Transaction transaction = Transaction.openRoot()) {
			for (int i = 0; i < inventory.size(); i++) {
				ItemResource resource = inventory.getResource(i);
				int amount = inventory.getAmountAsInt(i);
				if (!resource.isEmpty() && amount > 0) {
					inventory.extract(i, resource, amount, transaction);
				}
			}
			transaction.commit();
		}
	}

	public static boolean doesInventoryHaveItems(ResourceHandler<ItemResource> inventory) {
		for (int i = 0; i < inventory.size(); i++) {
			if (!inventory.getResource(i).isEmpty() && inventory.getAmountAsInt(i) > 0) {
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

	public static ItemStack getCraftingRemainingItem(ItemStack stack) {
		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		}
		return stack.getItem().getCraftingRemainder().create();
	}

	public static boolean hasCraftingRemainingItem(ItemStack stack) {
		return !getCraftingRemainingItem(stack).isEmpty();
	}

	/**
	 * Checks if the enchantment is registered, and if so, gets that enchantment's level on the passed stack. Defaults to 0 in all edge cases.
	 * @param enchantment The ResourceKey for the enchantment
	 * @param registries The provider for registry lookup
	 * @param stack The stack to be queried
	 * @return The enchantment's level, if the stack is enchanted with it. Returns 0 if not, or if the enchantment is disabled.
	 */
	public static int getValidatedEnchantmentLevel(ResourceKey<Enchantment> enchantment, HolderLookup.Provider registries, ItemStack stack) {
		Optional<Holder.Reference<Enchantment>> fortune = registries.holder(enchantment);
		return fortune.map(stack::getEnchantmentLevel).orElse(0);
	}
}
