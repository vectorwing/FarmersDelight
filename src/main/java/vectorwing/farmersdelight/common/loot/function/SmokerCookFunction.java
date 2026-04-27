package vectorwing.farmersdelight.common.loot.function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import vectorwing.farmersdelight.common.registry.ModLootFunctions;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SmokerCookFunction extends LootItemConditionalFunction
{
	protected SmokerCookFunction(LootItemCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext context) {
		if (stack.isEmpty()) {
			return stack;
		}

		Optional<SmokingRecipe> recipe = context.getLevel().getRecipeManager().getAllRecipesFor(RecipeType.SMOKING).stream()
				.filter(r -> r.getIngredients().get(0).test(stack)).findFirst();
		if (recipe.isPresent()) {
			ItemStack resultStack = recipe.get().getResultItem(context.getLevel().registryAccess()).copy();
			resultStack.setCount(resultStack.getCount() * stack.getCount());
			return resultStack;
		}

		return stack;
	}

	@Override
	public LootItemFunctionType getType() {
		return ModLootFunctions.SMOKER_COOK.get();
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<SmokerCookFunction>
	{
		@Override
		public SmokerCookFunction deserialize(JsonObject json, JsonDeserializationContext context, LootItemCondition[] conditions) {
			return new SmokerCookFunction(conditions);
		}
	}
}
