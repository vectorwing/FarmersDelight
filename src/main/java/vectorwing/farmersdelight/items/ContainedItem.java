package vectorwing.farmersdelight.items;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ContainedItem extends Item
{
	/**
	 * Items that are contained by another item.
	 * When consumed, they deposit containers into the player's inventory, allowing them to set maximum stack sizes above 1.
	 */
	public ContainedItem(Item.Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity subject) {
		ItemStack container = stack.getContainerItem();

		if (stack.isFood()) {
			super.onItemUseFinish(stack, worldIn, subject);
		} else {
			PlayerEntity player = subject instanceof PlayerEntity ? (PlayerEntity) subject : null;
			if (player instanceof ServerPlayerEntity) {
				CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) player, stack);
			}
			if (player != null) {
				player.addStat(Stats.ITEM_USED.get(this));
				if (!player.abilities.isCreativeMode) {
					stack.shrink(1);
				}
			}
		}

		this.affectConsumer();
		return this.useItemAndGiveContainer(stack, container, subject);
	}

	/**
	 * Override this to apply changes to the consumer (e.g. curing effects).
	 */
	public void affectConsumer() {
	}

	/**
	 * If the player isn't in Creative Mode, uses the item and spawns a container,
	 * either in their inventory or as an ItemEntity if full.
	 */
	public ItemStack useItemAndGiveContainer(ItemStack stack, ItemStack container, LivingEntity subject) {
		if (stack.isEmpty()) {
			return container;
		} else {
			if (subject instanceof PlayerEntity && !((PlayerEntity) subject).abilities.isCreativeMode) {
				PlayerEntity player = (PlayerEntity) subject;
				if (!player.inventory.addItemStackToInventory(container)) {
					player.dropItem(container, false);
				}
			}

			return stack;
		}
	}
}
