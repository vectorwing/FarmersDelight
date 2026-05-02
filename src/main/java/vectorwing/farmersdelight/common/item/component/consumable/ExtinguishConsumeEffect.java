package vectorwing.farmersdelight.common.item.component.consumable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.registry.ModConsumeEffectTypes;

public record ExtinguishConsumeEffect() implements ConsumeEffect {
	public static final MapCodec<ExtinguishConsumeEffect> CODEC = MapCodec.unit(new ExtinguishConsumeEffect());
	public static final StreamCodec<RegistryFriendlyByteBuf, ExtinguishConsumeEffect> STREAM_CODEC = StreamCodec.unit(new ExtinguishConsumeEffect());

	@Override
	public Type<ExtinguishConsumeEffect> getType() {
		return ModConsumeEffectTypes.EXTINGUISH.get();
	}

	@Override
	public boolean apply(Level level, ItemStack stack, LivingEntity entity) {
		entity.extinguishFire();
		return true;
	}
}