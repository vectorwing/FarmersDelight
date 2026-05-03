package vectorwing.farmersdelight.common.item.component.consumable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.registry.ModConsumeEffectTypes;

import java.util.ArrayList;
import java.util.Iterator;

public record RemoveRandomStatusEffectsConsumeEffect(TagKey<MobEffect> excluded,
													 boolean harmfulOnly) implements ConsumeEffect
{
	public static final MapCodec<RemoveRandomStatusEffectsConsumeEffect> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
		TagKey.codec(Registries.MOB_EFFECT).fieldOf("excluded").forGetter(RemoveRandomStatusEffectsConsumeEffect::excluded),
		Codec.BOOL.optionalFieldOf("harmful_only", false).forGetter(RemoveRandomStatusEffectsConsumeEffect::harmfulOnly)
	).apply(inst, RemoveRandomStatusEffectsConsumeEffect::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, RemoveRandomStatusEffectsConsumeEffect> STREAM_CODEC = StreamCodec.composite(
		TagKey.streamCodec(Registries.MOB_EFFECT), RemoveRandomStatusEffectsConsumeEffect::excluded,
		ByteBufCodecs.BOOL, RemoveRandomStatusEffectsConsumeEffect::harmfulOnly,
		RemoveRandomStatusEffectsConsumeEffect::new
	);

	public RemoveRandomStatusEffectsConsumeEffect(TagKey<MobEffect> excluded) {
		this(excluded, false);
	}

	@Override
	public Type<? extends ConsumeEffect> getType() {
		return ModConsumeEffectTypes.REMOVE_RANDOM_EFFECTS.get();
	}

	@Override
	public boolean apply(Level level, ItemStack stack, LivingEntity entity) {
		Iterator<MobEffectInstance> itr = entity.getActiveEffects().iterator();
		ArrayList<Holder<MobEffect>> compatibleEffects = new ArrayList<>();

		while (itr.hasNext()) {
			MobEffectInstance effect = itr.next();
			if ((!harmfulOnly || effect.getEffect().value().getCategory().equals(MobEffectCategory.HARMFUL)) && !effect.getEffect().is(excluded)) {
				compatibleEffects.add(effect.getEffect());
			}
		}

		if (!compatibleEffects.isEmpty()) {
			MobEffectInstance selectedEffect = entity.getEffect(compatibleEffects.get(level.getRandom().nextInt(compatibleEffects.size())));
			if (selectedEffect != null) {
				entity.removeEffect(selectedEffect.getEffect());
			}
		}

		return true;
	}
}