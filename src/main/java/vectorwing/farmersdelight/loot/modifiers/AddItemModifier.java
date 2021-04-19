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

	/**
	 * This loot modifier adds an item to the loot table, given the conditions specified.
	 */
	protected AddItemModifier(ILootCondition[] conditionsIn, Item addedItemIn) {
		super(conditionsIn);
		this.addedItem = addedItemIn;
	}

	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		generatedLoot.add(new ItemStack(addedItem));
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<AddItemModifier>
	{
		@Override
		public AddItemModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
			Item addedItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation((JSONUtils.getString(object, "item"))));
			return new AddItemModifier(ailootcondition, addedItem);
		}

		@Override
		public JsonObject write(AddItemModifier instance) {
			return new JsonObject();
		}
	}
}
