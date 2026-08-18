package vectorwing.farmersdelight.common.crafting.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import vectorwing.farmersdelight.common.Configuration;

/**
 * Credits to the Create team for the implementation of results with chances!
 */
public record ChanceResult(ItemStackTemplate stack, float chance)
{
	public static final ChanceResult EMPTY = new ChanceResult(null, 1);
	public static final Codec<ChanceResult> CODEC = RecordCodecBuilder.create(inst -> inst.group(
		ItemStackTemplate.CODEC.fieldOf("item").forGetter(ChanceResult::stack),
		Codec.FLOAT.optionalFieldOf("chance", 1.0f).forGetter(ChanceResult::chance)
	).apply(inst, ChanceResult::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, ChanceResult> STREAM_CODEC = StreamCodec.composite(
		ItemStackTemplate.STREAM_CODEC, ChanceResult::stack,
		ByteBufCodecs.FLOAT, ChanceResult::chance,
		ChanceResult::new
	);


	public ItemStack rollOutput(RandomSource random, int fortuneLevel) {
		int outputAmount = stack.count();
		double fortuneBonus = Configuration.CUTTING_BOARD_FORTUNE_BONUS.get() * fortuneLevel;
		for (int roll = 0; roll < stack.count(); roll++)
			if (random.nextFloat() > chance + fortuneBonus)
				outputAmount--;
		if (outputAmount == 0)
			return ItemStack.EMPTY;
		ItemStack out = stack.create().copy();
		out.setCount(outputAmount);
		return out;
	}
}