package vectorwing.farmersdelight.common.loot.function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.registry.ModLootFunctions;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CopyMealFunction extends LootItemConditionalFunction
{
	private CopyMealFunction(LootItemCondition[] conditions) {
		super(conditions);
	}

	public static LootItemConditionalFunction.Builder<?> builder() {
		return simpleBuilder(CopyMealFunction::new);
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext context) {
		if (context.getParamOrNull(LootContextParams.BLOCK_ENTITY) instanceof CookingPotBlockEntity cookingPot) {
			CompoundTag tag = cookingPot.writeMeal(new CompoundTag());
			if (!tag.isEmpty()) {
				stack.addTagElement("BlockEntityTag", tag);
			}
		}
		return stack;
	}

	@Override
	public LootItemFunctionType getType() {
		return ModLootFunctions.COPY_MEAL.get();
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<CopyMealFunction>
	{
		@Override
		public CopyMealFunction deserialize(JsonObject json, JsonDeserializationContext context, LootItemCondition[] conditions) {
			return new CopyMealFunction(conditions);
		}
	}
}
