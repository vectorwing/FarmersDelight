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
 * <p>
 * 26.1: the result is stored as an {@link ItemStackTemplate} (datagen-safe — building one does not require
 * the item's data components to be bound, unlike {@code new ItemStack(...)}). It is turned into a live
 * {@link ItemStack} via {@link ItemStackTemplate#create()} at runtime.
 */
public record ChanceResult(ItemStackTemplate stack, float chance)
{
	public static final Codec<ChanceResult> CODEC = RecordCodecBuilder.create(inst -> inst.group(
			ItemStackTemplate.CODEC.fieldOf("item").forGetter(ChanceResult::stack),
			Codec.FLOAT.optionalFieldOf("chance", 1.0f).forGetter(ChanceResult::chance)
	).apply(inst, ChanceResult::new));


	public ItemStack rollOutput(RandomSource random, int fortuneLevel) {
		int outputAmount = stack.count();
		double fortuneBonus = Configuration.CUTTING_BOARD_FORTUNE_BONUS.get() * fortuneLevel;
		for (int roll = 0; roll < stack.count(); roll++)
			if (random.nextFloat() > chance + fortuneBonus)
				outputAmount--;
		if (outputAmount == 0)
			return ItemStack.EMPTY;
		ItemStack out = stack.create();
		out.setCount(outputAmount);
		return out;
	}

	public void write(RegistryFriendlyByteBuf buffer) {
		ItemStackTemplate.STREAM_CODEC.encode(buffer, stack());
		buffer.writeFloat(chance());
	}

	public static ChanceResult read(RegistryFriendlyByteBuf buffer) {
		return new ChanceResult(ItemStackTemplate.STREAM_CODEC.decode(buffer), buffer.readFloat());
	}
}
