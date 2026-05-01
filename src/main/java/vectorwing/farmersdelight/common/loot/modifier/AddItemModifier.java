package vectorwing.farmersdelight.common.loot.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class AddItemModifier extends LootModifier
{
	public static final MapCodec<AddItemModifier> CODEC =
		RecordCodecBuilder.mapCodec(instance -> codecStart(instance).and(
				instance.group(
					BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter((m) -> m.addedItem),
					Codec.INT.optionalFieldOf("count", 1).forGetter((m) -> m.count)
				)
			)
			.apply(instance, AddItemModifier::new));

	private final Item addedItem;
	private final int count;

	/**
	 * This loot modifier adds an item to the loot table, given the conditions specified.
	 */
	public AddItemModifier(LootItemCondition[] conditions, int priority, Item addedItem, int count) {
		super(conditions, priority);
		this.addedItem = addedItem;
		this.count = count;
	}

	@Nonnull
	@Override
	protected ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> generatedLoot, @NotNull LootContext context) {
		ItemStack addedStack = new ItemStack(addedItem, count);

		if (addedStack.getCount() < addedStack.getMaxStackSize()) {
			generatedLoot.add(addedStack);
		} else {
			int i = addedStack.getCount();

			while (i > 0) {
				ItemStack subStack = addedStack.copy();
				subStack.setCount(Math.min(addedStack.getMaxStackSize(), i));
				i -= subStack.getCount();
				generatedLoot.add(subStack);
			}
		}

		return generatedLoot;
	}

	@Override
	public @NotNull MapCodec<? extends IGlobalLootModifier> codec() {
		return CODEC;
	}
}
