package vectorwing.farmersdelight.common.item.enchantment;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import vectorwing.farmersdelight.FarmersDelight;

public class BackstabbingEnchantment
{
	/**
	 * Determines whether the attacker is facing a 90-100 degree cone behind the target's looking direction.
	 */
	public static boolean isLookingBehindTarget(LivingEntity target, Vec3 attackerLocation) {
		if (attackerLocation != null) {
			Vec3 lookingVector = target.getViewVector(1.0F);
			Vec3 attackAngleVector = attackerLocation.subtract(target.position()).normalize();
			attackAngleVector = new Vec3(attackAngleVector.x, 0.0D, attackAngleVector.z);
			return attackAngleVector.dot(lookingVector) < -0.5D;
		}
		return false;
	}

	public static float getBackstabbingDamagePerLevel(float amount, int level) {
		float multiplier = ((level * 0.2F) + 1.2F);
		return amount * multiplier;
	}

//	@EventBusSubscriber(modid = FarmersDelight.MODID, bus = EventBusSubscriber.Bus.GAME)
//	public static class BackstabbingEvent
//	{
//		@SubscribeEvent
//		@SuppressWarnings("unused")
//		public static void onKnifeBackstab(LivingDamageEvent.Pre event) {
//			Entity attacker = event.getSource().getEntity();
//			if (attacker instanceof Player) {
//				ItemStack weapon = ((Player) attacker).getMainHandItem();
//				int enchantmentLevel = EnchantmentHelper.getTagEnchantmentLevel(event.getEntity().level().holder(ResourceKey.BACKSTABBING, weapon);
//				if (enchantmentLevel > 0 && isLookingBehindTarget(event.getEntity(), event.getSource().getSourcePosition())) {
//					Level level = event.getEntity().getCommandSenderWorld();
//					if (!level.isClientSide) {
//						event.setAmount(getBackstabbingDamagePerLevel(event.getAmount(), enchantmentLevel));
//						level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.BLOCKS, 1.0F, 1.0F);
//					}
//				}
//			}
//		}
//	}

}
