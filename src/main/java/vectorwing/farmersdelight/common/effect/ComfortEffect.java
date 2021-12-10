package vectorwing.farmersdelight.common.effect;

import com.google.common.collect.Sets;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Set;

@SuppressWarnings("unused")
public class ComfortEffect extends MobEffect
{
	public static final Set<MobEffect> COMFORT_IMMUNITIES = Sets.newHashSet(MobEffects.MOVEMENT_SLOWDOWN, MobEffects.WEAKNESS, MobEffects.HUNGER);

	/**
	 * This effect makes the entity immune to negative effects related to cold and sickness.
	 * The entity cannot freeze inside powder snow, and cannot receive Slowness or Weakness.
	 */
	public ComfortEffect() {
		super(MobEffectCategory.BENEFICIAL, 0);
	}

	@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class ComfortEvent
	{
		@SubscribeEvent
		public static void onBadEffectApplicable(PotionEvent.PotionApplicableEvent event) {
			MobEffectInstance effect = event.getPotionEffect();
			LivingEntity entity = event.getEntityLiving();
			if (entity.getEffect(ModEffects.COMFORT.get()) != null && COMFORT_IMMUNITIES.contains(effect.getEffect())) {
				event.setResult(Event.Result.DENY);
			}
		}

		@SubscribeEvent
		public static void onBadEffectApplied(PotionEvent.PotionAddedEvent event) {
			MobEffectInstance addedEffect = event.getPotionEffect();
			LivingEntity entity = event.getEntityLiving();
			if (addedEffect.getEffect().equals(ModEffects.COMFORT.get())) {
				for (MobEffect effect : COMFORT_IMMUNITIES) {
					entity.removeEffect(effect);
				}
			}
		}
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		entity.setTicksFrozen(0);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
