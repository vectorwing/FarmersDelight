package vectorwing.farmersdelight.loot.modifiers;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import vectorwing.farmersdelight.setup.Configuration;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Credits to Commoble for this implementation!
 */
public class AddLootTableModifier extends LootModifier
{
	private final ResourceLocation lootTable;

	protected AddLootTableModifier(ILootCondition[] conditionsIn, ResourceLocation lootTable) {
		super(conditionsIn);
		this.lootTable = lootTable;
	}

	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		if (Configuration.GENERATE_FD_CHEST_LOOT.get()) {
			LootTable extraTable = context.getLootTable(this.lootTable);
			extraTable.getRandomItems(context, generatedLoot::add);
		}
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<AddLootTableModifier>
	{
		@Override
		public AddLootTableModifier read(ResourceLocation location, JsonObject object, ILootCondition[] conditions) {
			ResourceLocation lootTable = new ResourceLocation(JSONUtils.getAsString(object, "lootTable"));
			return new AddLootTableModifier(conditions, lootTable);
		}

		@Override
		public JsonObject write(AddLootTableModifier instance)
		{
			JsonObject object = this.makeConditions(instance.conditions);
			object.addProperty("lootTable", instance.lootTable.toString());
			return object;
		}
	}
}
