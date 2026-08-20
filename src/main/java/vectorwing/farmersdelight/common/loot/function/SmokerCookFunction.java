package vectorwing.farmersdelight.common.loot.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jspecify.annotations.NullMarked;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModLootFunctions;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

@NullMarked
@ParametersAreNonnullByDefault
public class SmokerCookFunction extends LootItemConditionalFunction
{
	public static final Identifier ID = Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "smoker_cook");
	public static final MapCodec<SmokerCookFunction> CODEC = RecordCodecBuilder.mapCodec(
			p_298131_ -> commonFields(p_298131_).apply(p_298131_, SmokerCookFunction::new)
	);

	protected SmokerCookFunction(List<LootItemCondition> conditionsIn) {
		super(conditionsIn);
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext context) {
		if (stack.isEmpty()) {
			return stack;
		}

		SingleRecipeInput input = new SingleRecipeInput(stack);
		Optional<RecipeHolder<SmokingRecipe>> recipe = context.getLevel().recipeAccess().recipeMap()
				.getRecipesFor(RecipeType.SMOKING, input, context.getLevel()).findFirst();
		if (recipe.isPresent()) {
			ItemStack resultStack = recipe.get().value().assemble(input);
			resultStack.setCount(resultStack.getCount() * stack.getCount());
			return resultStack;
		}

		return stack;
	}

	@Override
	public MapCodec<? extends LootItemConditionalFunction> codec() {
		return CODEC;
	}
}
