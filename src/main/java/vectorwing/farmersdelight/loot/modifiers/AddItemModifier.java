package vectorwing.farmersdelight.loot.modifiers;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class AddItemModifier extends LootModifier
{
	private final Item addedItem;
	private final int count;

	/**
	 * This loot modifier adds an item to the loot table, given the conditions specified.
	 */
	protected AddItemModifier(ILootCondition[] conditionsIn, Item addedItemIn, int count) {
		super(conditionsIn);
		this.addedItem = addedItemIn;
		this.count = count;
	}

	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
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

	public static class Serializer extends GlobalLootModifierSerializer<AddItemModifier>
	{
		@Override
		public AddItemModifier read(ResourceLocation location, JsonObject object, ILootCondition[] conditions) {
			Item addedItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation((JSONUtils.getAsString(object, "item"))));
			int count = JSONUtils.getAsInt(object, "count", 1);
			return new AddItemModifier(conditions, addedItem, count);
		}

		@Override
		public JsonObject write(AddItemModifier instance) {
			return new JsonObject();
		}
	}
}
