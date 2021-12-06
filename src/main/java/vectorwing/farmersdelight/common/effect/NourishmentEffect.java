package vectorwing.farmersdelight.common.effect;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;

public class NourishmentEffect extends MobEffect
{
	/**
	 * This effect prevents hunger loss by constantly decreasing the exhaustion level.
	 * If the player can spend saturation to heal damage, the effect halts to let them do so, until they can't any more.
	 * This means players can grow hungry by healing damage, but no further than 1.5 points, allowing them to eat more and keep healing.
	 */
	public NourishmentEffect() {
		super(MobEffectCategory.BENEFICIAL, 0);
	}

	public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
		if (!entityLivingBaseIn.getCommandSenderWorld().isClientSide && entityLivingBaseIn instanceof Player player) {
			FoodData foodData = player.getFoodData();
			boolean isPlayerHealingWithSaturation =
					player.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)
							&& player.isHurt()
							&& foodData.getSaturationLevel() > 0.0F
							&& foodData.getFoodLevel() >= 20;
			if (!isPlayerHealingWithSaturation) {
				float exhaustion = foodData.getExhaustionLevel();
				float reduction = Math.min(exhaustion, 0.1F);
				if (exhaustion > 0.0F) {
					player.causeFoodExhaustion(-reduction);
				}
			}
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
