//package vectorwing.farmersdelight.common.mixin;
//
//import net.minecraft.advancements.CriteriaTriggers;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.stats.Stats;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.BowlFoodItem;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.Level;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//import vectorwing.farmersdelight.common.Configuration;
//
//@Mixin(BowlFoodItem.class)
//public abstract class SoupItemMixin extends Item
//{
//	public SoupItemMixin(Properties properties) {
//		super(properties);
//	}
//
//	@Inject(at = @At(value = "HEAD"), method = "finishUsingItem", cancellable = true)
//	private void onItemUseFinish(ItemStack stack, Level level, LivingEntity subject, CallbackInfoReturnable<ItemStack> cir) {
//		if (Configuration.ENABLE_STACKABLE_SOUP_ITEMS.get()) {
//			ItemStack container = stack.getCraftingRemainingItem();
//			if (container.isEmpty())
//				container = new ItemStack(Items.BOWL);
//
//			if (stack.isEdible()) {
//				super.finishUsingItem(stack, level, subject);
//			} else {
//				Player player = subject instanceof Player ? (Player) subject : null;
//				if (player instanceof ServerPlayer) {
//					CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
//				}
//				if (player != null) {
//					player.awardStat(Stats.ITEM_USED.get(this));
//					if (!player.getAbilities().instabuild) {
//						stack.shrink(1);
//					}
//				}
//			}
//
//			if (stack.isEmpty()) {
//				cir.setReturnValue(container);
//			} else {
//				if (subject instanceof Player && !((Player) subject).getAbilities().instabuild) {
//					Player player = (Player) subject;
//					if (!player.getInventory().add(container)) {
//						player.drop(container, false);
//					}
//				}
//				cir.setReturnValue(stack);
//			}
//		}
//	}
//}
