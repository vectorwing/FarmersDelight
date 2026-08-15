package vectorwing.farmersdelight.common.crafting.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import vectorwing.farmersdelight.common.Configuration;

/**
 * Credits to the Create team for the implementation of results with chances!
 */
public record ChanceResult(ItemStackTemplate template, float chance)
{
	public static final Codec<ChanceResult> CODEC = RecordCodecBuilder.create(inst -> inst.group(
			ItemStackTemplate.CODEC.fieldOf("item").forGetter(ChanceResult::template),
			Codec.FLOAT.optionalFieldOf("chance", 1.0f).forGetter(ChanceResult::chance)
	).apply(inst, ChanceResult::new));

	public ChanceResult(ItemStack stack, float chance) {
		this(ItemStackTemplate.fromNonEmptyStack(stack), chance);
	}

	public ItemStack stack() {
		return template.create();
	}

	public ItemStack rollOutput(RandomSource random, int fortuneLevel) {
		ItemStack stack = stack();
		int outputAmount = stack.getCount();
		double fortuneBonus = Configuration.CUTTING_BOARD_FORTUNE_BONUS.get() * fortuneLevel;
		for (int roll = 0; roll < stack.getCount(); roll++)
			if (random.nextFloat() > chance + fortuneBonus)
				outputAmount--;
		if (outputAmount == 0)
			return ItemStack.EMPTY;
		ItemStack out = stack.copy();
		out.setCount(outputAmount);
		return out;
	}

	public void write(RegistryFriendlyByteBuf buffer) {
		ItemStackTemplate.STREAM_CODEC.encode(buffer, template());
		buffer.writeFloat(chance());
	}

	public static ChanceResult read(RegistryFriendlyByteBuf buffer) {
		return new ChanceResult(ItemStackTemplate.STREAM_CODEC.decode(buffer), buffer.readFloat());
	}
}
