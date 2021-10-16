package vectorwing.farmersdelight.effects;

import com.google.common.collect.Sets;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.registry.ModEffects;

import java.util.Set;

@SuppressWarnings("unused")
public class ComfortEffect extends Effect
{
	public static final Set<Effect> COMFORT_IMMUNITIES = Sets.newHashSet(Effects.MOVEMENT_SLOWDOWN, Effects.WEAKNESS, Effects.HUNGER);

	/**
	 * This effect makes the player immune to negative effects related to cold and sickness.
	 * It also instantly heals the equivalent effects when first applied.
	 * The effect runs entirely on events, which I assumed to be more efficient than constantly ticking over the entity's effect list.
	 * Current targets: Slowness, Weakness and Hunger.
	 */
	public ComfortEffect() {
		super(EffectType.BENEFICIAL, 0);
	}

	@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class ComfortEvent
	{
		@SubscribeEvent
		public static void onComfortDuration(PotionEvent.PotionApplicableEvent event) {
			EffectInstance effect = event.getPotionEffect();
			LivingEntity entity = event.getEntityLiving();
			if (entity.getEffect(ModEffects.COMFORT.get()) != null && COMFORT_IMMUNITIES.contains(effect.getEffect())) {
				event.setResult(Event.Result.DENY);
			}
		}

		@SubscribeEvent
		public static void onComfortApplied(PotionEvent.PotionAddedEvent event) {
			EffectInstance addedEffect = event.getPotionEffect();
			LivingEntity entity = event.getEntityLiving();
			if (addedEffect.getEffect().equals(ModEffects.COMFORT.get())) {
				for (Effect effect : COMFORT_IMMUNITIES) {
					entity.removeEffect(effect);
				}
			}
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
