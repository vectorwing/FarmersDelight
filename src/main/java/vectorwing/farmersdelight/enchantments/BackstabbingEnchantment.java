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
	public int getMinEnchantability(int enchantmentLevel) {
		return 15 + (enchantmentLevel - 1) * 9;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return super.getMinEnchantability(enchantmentLevel) + 50;
	}

	/**
	 * Determines whether the attacker is facing a 90-100 degree cone behind the target's looking direction.
	 */
	public static boolean isLookingBehindTarget(LivingEntity target, Vector3d attackerLocation) {
		if (attackerLocation != null) {
			Vector3d vec3d = target.getLook(1.0F);
			Vector3d vec3d1 = attackerLocation.subtract(target.getPositionVec()).normalize();
			vec3d1 = new Vector3d(vec3d1.x, 0.0D, vec3d1.z);
			return vec3d1.dotProduct(vec3d) < -0.5D;
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
			Entity attacker = event.getSource().getTrueSource();
			if (attacker instanceof PlayerEntity) {
				ItemStack weapon = ((PlayerEntity) attacker).getHeldItemMainhand();
				int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.BACKSTABBING.get(), weapon);
				if (level > 0 && isLookingBehindTarget(event.getEntityLiving(), event.getSource().getDamageLocation())) {
					World world = event.getEntityLiving().getEntityWorld();
					if (!world.isRemote) {
						event.setAmount(getBackstabbingDamagePerLevel(event.getAmount(), level));
						world.playSound(null, attacker.getPosX(), attacker.getPosY(), attacker.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.BLOCKS, 1.0F, 1.0F);
					}
				}
			}
		}
	}

}
