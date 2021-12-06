package vectorwing.farmersdelight.common.loot.function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import vectorwing.farmersdelight.FarmersDelight;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SmokerCookFunction extends LootItemConditionalFunction
{
	public static final ResourceLocation ID = new ResourceLocation(FarmersDelight.MODID, "smoker_cook");

	protected SmokerCookFunction(LootItemCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext context) {
		if (stack.isEmpty()) {
			return stack;
		} else {
			Optional<SmokingRecipe> recipe = context.getLevel().getRecipeManager().getAllRecipesFor(RecipeType.SMOKING).stream()
					.filter(r -> r.getIngredients().get(0).test(stack)).findFirst();
			if (recipe.isPresent()) {
				ItemStack result = recipe.get().getResultItem().copy();
				result.setCount(result.getCount() * stack.getCount());
				return result;
			} else {
				return stack;
			}
		}
	}

	@Override
	@Nullable
	public LootItemFunctionType getType() {
		return null;
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<SmokerCookFunction>
	{
		@Override
		public SmokerCookFunction deserialize(JsonObject _object, JsonDeserializationContext _context, LootItemCondition[] conditionsIn) {
			return new SmokerCookFunction(conditionsIn);
		}
	}
}
