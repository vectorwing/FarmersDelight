package vectorwing.farmersdelight.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;

public class NourishmentEffect extends MobEffect
{
	/**
	 * This effect prevents hunger loss by constantly setting the exhaustion level to zero.
	 * If the player can spend saturation to heal damage, the effect pauses to let them do so.
	 * Slow healing won't consume hunger, making it happen indefinitely. A mixin allows the player to always eat when under this effect to compensate.
	 */
	public NourishmentEffect() {
		super(MobEffectCategory.BENEFICIAL, 0xF3B300);
	}

	public void applyEffectTick(LivingEntity entity, int amplifier) {
		if (entity.getCommandSenderWorld().isClientSide) {
			return;
		}

		if (entity instanceof Player player) {
			FoodData foodData = player.getFoodData();
			boolean isPlayerHealingWithSaturation =
					player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)
					&& player.isHurt()
					&& foodData.getSaturationLevel() > 0.0;
			if (!isPlayerHealingWithSaturation) {
				foodData.setExhaustion(0);
			}
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
