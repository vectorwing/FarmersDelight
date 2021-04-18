package vectorwing.farmersdelight.loot.modifiers;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import vectorwing.farmersdelight.registry.ModItems;

import javax.annotation.Nonnull;
import java.util.List;

public class StrawHarvestingModifier extends LootModifier
{
	protected StrawHarvestingModifier(ILootCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Nonnull
	@Override
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
		generatedLoot.add(new ItemStack(ModItems.STRAW.get()));
		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<StrawHarvestingModifier>
	{
		@Override
		public StrawHarvestingModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
			return new StrawHarvestingModifier(ailootcondition);
		}

		@Override
		public JsonObject write(StrawHarvestingModifier instance) {
			return new JsonObject();
		}
	}
}
