package vectorwing.farmersdelight.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.FoodStats;
import net.minecraft.world.GameRules;

public class NourishmentEffect extends Effect
{
	/**
	 * This effect makes the player immune to accumulating food exhaustion. It is useless to all other entities.
	 * Normal exhausting actions such as running, jumping and attacking will not drain hunger or saturation.
	 * If the player can spend saturation to heal damage, the effect halts to let them do so, until they can't any more.
	 * This means players can grow hungry by healing damage, but no further than 1.5 points, allowing them to eat more and keep healing.
	 * <p>
	 * This method employs an Access Transformer exclusively to read the exhaustion value; it calls the proper functions to change it.
	 */
	public NourishmentEffect() {
		super(EffectType.BENEFICIAL, 0);
	}

	public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
		if (!entityLivingBaseIn.getCommandSenderWorld().isClientSide && entityLivingBaseIn instanceof PlayerEntity) {
			PlayerEntity player = ((PlayerEntity) entityLivingBaseIn);
			FoodStats foodStats = player.getFoodData();
			boolean isPlayerHealingWithSaturation =
					player.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)
							&& player.isHurt()
							&& foodStats.getSaturationLevel() > 0.0F
							&& foodStats.getFoodLevel() >= 20;
			if (!isPlayerHealingWithSaturation) {
				float exhaustion = player.getFoodData().exhaustionLevel;
				if (exhaustion > 0.1F) {
					player.causeFoodExhaustion(-0.1F);
				}
			}
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
