package vectorwing.farmersdelight.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.FoodStats;
import net.minecraft.world.GameRules;

public class NourishedEffect extends Effect
{
	/**
	 * This effect makes the player immune to accumulating food exhaustion. It is useless to all other entities.
	 * Normal exhausting actions such as running, jumping and attacking will not drain hunger or saturation.
	 * If the player can spend saturation to heal damage, the effect halts to let them do so, until they can't any more.
	 * This means players can grow hungry by healing damage, but no further than 1.5 points, allowing them to eat more and keep healing.
	 * <p>
	 * This method employs an Access Transformer exclusively to read the exhaustion value; it calls the proper functions to change it.
	 */
	public NourishedEffect() {
		super(EffectType.BENEFICIAL, 0);
	}

	public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
		if (!entityLivingBaseIn.getEntityWorld().isRemote && entityLivingBaseIn instanceof PlayerEntity) {
			PlayerEntity player = ((PlayerEntity) entityLivingBaseIn);
			FoodStats foodStats = player.getFoodStats();
			boolean isPlayerHealingWithSaturation =
					player.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION)
							&& player.shouldHeal()
							&& foodStats.getSaturationLevel() > 0.0F
							&& foodStats.getFoodLevel() >= 20;
			if (!isPlayerHealingWithSaturation) {
				float exhaustion = player.getFoodStats().foodExhaustionLevel;
				if (exhaustion > 0.1F) {
					player.addExhaustion(-0.1F);
				}
			}
		}
	}

	public boolean isReady(int duration, int amplifier) {
		return true;
	}

}
