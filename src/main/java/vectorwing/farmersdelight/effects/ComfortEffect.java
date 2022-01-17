package vectorwing.farmersdelight.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;

@SuppressWarnings("unused")
public class ComfortEffect extends Effect
{
	/**
	 * This effect extends the player's natural regeneration, regardless of how hungry they are.
	 * Comfort does not care for amplifiers; it will always heal at the same slow pace.
	 * If the player has saturation to spend, or has the Regeneration effect, Comfort does nothing.
	 */
	public ComfortEffect() {
		super(EffectType.BENEFICIAL, 0);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		if (entity.hasEffect(Effects.REGENERATION)) {
			return;
		}
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;
			if (player.getFoodData().getSaturationLevel() > 0.0) {
				return;
			}
		}
		if (entity.getHealth() < entity.getMaxHealth()) {
			entity.heal(1.0F);
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration % 80 == 0;
	}
}
