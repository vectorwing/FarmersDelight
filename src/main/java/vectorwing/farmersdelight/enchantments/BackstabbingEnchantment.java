package vectorwing.farmersdelight.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.registry.ModEnchantments;

import net.minecraft.enchantment.Enchantment.Rarity;

public class BackstabbingEnchantment extends Enchantment
{
	public BackstabbingEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
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
	public static boolean isLookingBehindTarget(LivingEntity target, Vector3d attackerLocation) {
		if (attackerLocation != null) {
			Vector3d lookingVector = target.getViewVector(1.0F);
			Vector3d attackAngleVector = attackerLocation.subtract(target.position()).normalize();
			attackAngleVector = new Vector3d(attackAngleVector.x, 0.0D, attackAngleVector.z);
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
			if (attacker instanceof PlayerEntity) {
				ItemStack weapon = ((PlayerEntity) attacker).getMainHandItem();
				int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.BACKSTABBING.get(), weapon);
				if (level > 0 && isLookingBehindTarget(event.getEntityLiving(), event.getSource().getSourcePosition())) {
					World world = event.getEntityLiving().getCommandSenderWorld();
					if (!world.isClientSide) {
						event.setAmount(getBackstabbingDamagePerLevel(event.getAmount(), level));
						world.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, SoundCategory.BLOCKS, 1.0F, 1.0F);
					}
				}
			}
		}
	}

}
