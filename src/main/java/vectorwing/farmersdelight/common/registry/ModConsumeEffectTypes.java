package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.item.component.consumable.ExtinguishConsumeEffect;
import vectorwing.farmersdelight.common.item.component.consumable.HealConsumeEffect;
import vectorwing.farmersdelight.common.item.component.consumable.RemoveRandomStatusEffectsConsumeEffect;

import java.util.function.Supplier;

public class ModConsumeEffectTypes
{
	public static final DeferredRegister<ConsumeEffect.Type<?>> CONSUME_EFFECTS = DeferredRegister.create(Registries.CONSUME_EFFECT_TYPE, FarmersDelight.MODID);

	public static final Supplier<ConsumeEffect.Type<RemoveRandomStatusEffectsConsumeEffect>> REMOVE_RANDOM_EFFECTS = CONSUME_EFFECTS.register("remove_random_effects",
		() -> new ConsumeEffect.Type<>(RemoveRandomStatusEffectsConsumeEffect.CODEC, RemoveRandomStatusEffectsConsumeEffect.STREAM_CODEC));
	public static final Supplier<ConsumeEffect.Type<HealConsumeEffect>> HEAL = CONSUME_EFFECTS.register("heal",
		() -> new ConsumeEffect.Type<>(HealConsumeEffect.CODEC, HealConsumeEffect.STREAM_CODEC));
	public static final Supplier<ConsumeEffect.Type<ExtinguishConsumeEffect>> EXTINGUISH = CONSUME_EFFECTS.register("extinguish",
		() -> new ConsumeEffect.Type<>(ExtinguishConsumeEffect.CODEC, ExtinguishConsumeEffect.STREAM_CODEC));
}