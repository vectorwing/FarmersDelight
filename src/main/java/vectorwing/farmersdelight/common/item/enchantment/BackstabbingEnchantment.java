package vectorwing.farmersdelight.common.item.enchantment;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import org.apache.commons.lang3.mutable.MutableFloat;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModDataComponents;

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

	@EventBusSubscriber(modid = FarmersDelight.MODID, bus = EventBusSubscriber.Bus.GAME)
	public static class BackstabbingEvent
	{
		@SubscribeEvent
		@SuppressWarnings("unused")
		public static void onKnifeBackstab(LivingIncomingDamageEvent event) {
			Entity attacker = event.getSource().getEntity();
			if (attacker instanceof LivingEntity living && isLookingBehindTarget(event.getEntity(), event.getSource().getSourcePosition())) {
				Level level = attacker.level();
				if (level instanceof ServerLevel serverLevel) {
					ItemStack weapon = living.getWeaponItem();
					float preModifiedDamage = event.getAmount(); // since you play a sound on success, we record the original to do a change check later
					MutableFloat dmg = new MutableFloat(event.getAmount());
					EnchantmentHelper.runIterationOnItem(weapon, (enchantment, powerLevel) -> {
						enchantment.value().modifyDamageFilteredValue(ModDataComponents.BACKSTABBING.get(), serverLevel, powerLevel, weapon, attacker, event.getSource(), dmg);
					});

					if (preModifiedDamage != dmg.getValue()) {
						event.setAmount(dmg.getValue());
						serverLevel.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.BLOCKS, 1.0F, 1.0F);
					}
				}
			}
		}
	}
}
