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
	 * This effect prevents hunger loss by constantly decreasing the exhaustion level.
	 * If the player can spend saturation to heal damage, the effect halts to let them do so, until they can't any more.
	 * This means players can grow hungry by healing damage, but no further than 1.5 points, allowing them to eat more and keep healing.
	 */
	public NourishmentEffect() {
		super(MobEffectCategory.BENEFICIAL, 15971072);
	}

	public boolean applyEffectTick(LivingEntity entity, int amplifier) {
		if (!entity.getCommandSenderWorld().isClientSide && entity instanceof Player player) {
			FoodData foodData = player.getFoodData();
			boolean isPlayerHealingWithHunger =
					player.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)
							&& player.isHurt()
							&& foodData.getFoodLevel() >= 18;
			if (!isPlayerHealingWithHunger) {
				float exhaustion = foodData.getExhaustionLevel();
				float reduction = Math.min(exhaustion, 4.0F);
				if (exhaustion > 0.0F) {
					player.causeFoodExhaustion(-reduction);
				}
			}
		}

		return true;
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}
}
