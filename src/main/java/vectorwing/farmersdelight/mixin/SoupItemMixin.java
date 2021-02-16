package vectorwing.farmersdelight.mixin;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SoupItem;
import net.minecraft.stats.Stats;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.setup.Configuration;
import vectorwing.farmersdelight.utils.tags.ModTags;

@Mixin(SoupItem.class)
public abstract class SoupItemMixin extends Item {
	public SoupItemMixin(Properties properties) {
		super(properties);
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		Item item = stack.getItem();
		if (Configuration.STACKABLE_SOUP_ITEMS.get()) {
			if (Configuration.OVERRIDE_ALL_SOUP_ITEMS.get() && !item.isIn(ModTags.NON_STACKABLE_SOUP_ITEMS) || !Configuration.OVERRIDE_ALL_SOUP_ITEMS.get() && item.isIn(ModTags.STACKABLE_SOUP_ITEMS)) {
				return 16;
			}
		}
		return super.getItemStackLimit(stack);
	}

	/**
	 * Replication of ConsumableItem but in Mixin form, to allow SoupItems to stack
	 */
	@Inject(at = @At(value = "HEAD"), method = "onItemUseFinish", cancellable = true)
	private void onItemUseFinish(ItemStack stack, World worldIn, LivingEntity subject, CallbackInfoReturnable<ItemStack> cir) {
		if (Configuration.STACKABLE_SOUP_ITEMS.get()) {
			ItemStack container = stack.getContainerItem();
			if (container.isEmpty())
				container = new ItemStack(Items.BOWL);

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

			if (stack.isEmpty()) {
				cir.setReturnValue(container);
			} else {
				if (subject instanceof PlayerEntity && !((PlayerEntity) subject).abilities.isCreativeMode) {
					PlayerEntity player = (PlayerEntity) subject;
					if (!player.inventory.addItemStackToInventory(container)) {
						player.dropItem(container, false);
					}
				}
				cir.setReturnValue(stack);
			}
		}
	}
}
