package vectorwing.farmersdelight.common.item.enchantment;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModEnchantments;

public class BackstabbingEnchantment extends Enchantment
{
	public BackstabbingEnchantment(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots) {
		super(rarityIn, typeIn, slots);
	}

	@Override
	public int getMinLevel() {
		return 1;
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public int getMinCost(int enchantmentLevel) {
		return 15 + (enchantmentLevel - 1) * 9;
	}

	@Override
	public int getMaxCost(int enchantmentLevel) {
		return super.getMinCost(enchantmentLevel) + 50;
	}

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
		float multiplier = ((level * 0.4F) + 1.0F);
		return amount * multiplier;
	}

	@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class BackstabbingEvent
	{
		@SubscribeEvent
		@SuppressWarnings("unused")
		public static void onKnifeBackstab(LivingHurtEvent event) {
			Entity attacker = event.getSource().getEntity();
			if (attacker instanceof Player) {
				ItemStack weapon = ((Player) attacker).getMainHandItem();
				int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.BACKSTABBING.get(), weapon);
				if (level > 0 && isLookingBehindTarget(event.getEntityLiving(), event.getSource().getSourcePosition())) {
					Level world = event.getEntityLiving().getCommandSenderWorld();
					if (!world.isClientSide) {
						event.setAmount(getBackstabbingDamagePerLevel(event.getAmount(), level));
						world.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.BLOCKS, 1.0F, 1.0F);
					}
				}
			}
		}
	}

}
