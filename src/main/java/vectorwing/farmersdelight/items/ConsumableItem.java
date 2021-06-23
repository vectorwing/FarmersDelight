package vectorwing.farmersdelight.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.world.World;

public class ConsumableItem extends Item
{
	/**
	 * Items that can be consumed by an entity.
	 * When consumed, they may affect the consumer somehow, and will give back containers if applicable, regardless of their stack size.
	 */
	public ConsumableItem(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity consumer) {
		if (!worldIn.isRemote) {
			this.affectConsumer(stack, worldIn, consumer);
		}

		ItemStack container = stack.getContainerItem();

		if (stack.isFood()) {
			super.onItemUseFinish(stack, worldIn, consumer);
		} else {
			PlayerEntity player = consumer instanceof PlayerEntity ? (PlayerEntity) consumer : null;
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

		if (stack.isEmpty()) {
			return container;
		} else {
			if (consumer instanceof PlayerEntity && !((PlayerEntity) consumer).abilities.isCreativeMode) {
				PlayerEntity player = (PlayerEntity) consumer;
				if (!player.inventory.addItemStackToInventory(container)) {
					player.dropItem(container, false);
				}
			}
			return stack;
		}
	}

	/**
	 * Override this to apply changes to the consumer (e.g. curing effects).
	 */
	public void affectConsumer(ItemStack stack, World worldIn, LivingEntity consumer) {
	}
}
