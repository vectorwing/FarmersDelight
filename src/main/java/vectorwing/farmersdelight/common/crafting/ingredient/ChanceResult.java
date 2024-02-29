package vectorwing.farmersdelight.common.crafting.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.Configuration;

/**
 * Credits to the Create team for the implementation of results with chances!
 */
public class ChanceResult
{
	public static final ChanceResult EMPTY = new ChanceResult(ItemStack.EMPTY, 1);
	public static final Codec<ChanceResult> CODEC = RecordCodecBuilder.create(inst -> inst.group(
			((MapCodec.MapCodecCodec<ItemStack>) ItemStack.ITEM_WITH_COUNT_CODEC).codec().forGetter(ChanceResult::getStack),
			ExtraCodecs.strictOptionalField(Codec.FLOAT, "chance", 1.0f).forGetter(ChanceResult::getChance)
	).apply(inst, ChanceResult::new));

	private final ItemStack stack;
	private final float chance;

	public ChanceResult(ItemStack stack, float chance) {
		this.stack = stack;
		this.chance = chance;
	}

	public ItemStack getStack() {
		return stack;
	}

	public float getChance() {
		return chance;
	}

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

	public void write(FriendlyByteBuf buf) {
		buf.writeItem(getStack());
		buf.writeFloat(getChance());
	}

	public static ChanceResult read(FriendlyByteBuf buf) {
		return new ChanceResult(buf.readItem(), buf.readFloat());
	}
}
