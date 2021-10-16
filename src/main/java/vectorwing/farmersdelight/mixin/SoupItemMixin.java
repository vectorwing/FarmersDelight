package vectorwing.farmersdelight.mixin;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.stats.Stats;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.setup.Configuration;
import vectorwing.farmersdelight.utils.tags.ModTags;

import net.minecraft.world.item.Item.Properties;

@Mixin(BowlFoodItem.class)
public abstract class SoupItemMixin extends Item {
	public SoupItemMixin(Properties properties) {
		super(properties);
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		if (Configuration.ENABLE_STACKABLE_SOUP_ITEMS.get()) {
			ResourceLocation stackable = stack.getItem().getRegistryName();
			String stackableKey = "";
			if (stackable != null) {
				stackableKey = stackable.toString();
			}
			if (Configuration.OVERRIDE_ALL_SOUP_ITEMS.get() && !Configuration.SOUP_ITEM_LIST.get().contains(stackableKey)
				|| !Configuration.OVERRIDE_ALL_SOUP_ITEMS.get() && Configuration.SOUP_ITEM_LIST.get().contains(stackableKey)) {
				return 16;
			}
		}
		return super.getItemStackLimit(stack);
	}

	/**
	 * Replication of ConsumableItem but in Mixin form, to allow SoupItems to stack
	 */
	@Inject(at = @At(value = "HEAD"), method = "finishUsingItem", cancellable = true)
	private void onItemUseFinish(ItemStack stack, Level worldIn, LivingEntity subject, CallbackInfoReturnable<ItemStack> cir) {
		if (Configuration.ENABLE_STACKABLE_SOUP_ITEMS.get()) {
			ItemStack container = stack.getContainerItem();
			if (container.isEmpty())
				container = new ItemStack(Items.BOWL);

			if (stack.isEdible()) {
				super.finishUsingItem(stack, worldIn, subject);
			} else {
				Player player = subject instanceof Player ? (Player) subject : null;
				if (player instanceof ServerPlayer) {
					CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
				}
				if (player != null) {
					player.awardStat(Stats.ITEM_USED.get(this));
					if (!player.abilities.instabuild) {
						stack.shrink(1);
					}
				}
			}

			if (stack.isEmpty()) {
				cir.setReturnValue(container);
			} else {
				if (subject instanceof Player && !((Player) subject).abilities.instabuild) {
					Player player = (Player) subject;
					if (!player.inventory.add(container)) {
						player.drop(container, false);
					}
				}
				cir.setReturnValue(stack);
			}
		}
	}
}
