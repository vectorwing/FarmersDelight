package vectorwing.farmersdelight.common.crafting.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.Configuration;

/**
 * Credits to the Create team for the implementation of results with chances!
 */
public record ChanceResult(ItemStack stack, float chance)
{
	public static final ChanceResult EMPTY = new ChanceResult(ItemStack.EMPTY, 1);
	public static final Codec<ChanceResult> CODEC = RecordCodecBuilder.create(inst -> inst.group(
			ItemStack.CODEC.fieldOf("item").forGetter(ChanceResult::stack),
			Codec.FLOAT.optionalFieldOf("chance", 1.0f).forGetter(ChanceResult::chance)
	).apply(inst, ChanceResult::new));


	public ItemStack rollOutput(RandomSource rand, int fortuneLevel) {
		int outputAmount = stack.getCount();
		double fortuneBonus = Configuration.CUTTING_BOARD_FORTUNE_BONUS.get() * fortuneLevel;
		for (int roll = 0; roll < stack.getCount(); roll++)
			if (rand.nextFloat() > chance + fortuneBonus)
				outputAmount--;
		if (outputAmount == 0)
			return ItemStack.EMPTY;
		ItemStack out = stack.copy();
		out.setCount(outputAmount);
		return out;
	}

	public void write(RegistryFriendlyByteBuf buffer) {
		ItemStack.STREAM_CODEC.encode(buffer, stack());
		buffer.writeFloat(chance());
	}

	public static ChanceResult read(RegistryFriendlyByteBuf buffer) {
		return new ChanceResult(ItemStack.STREAM_CODEC.decode(buffer), buffer.readFloat());
	}
}
