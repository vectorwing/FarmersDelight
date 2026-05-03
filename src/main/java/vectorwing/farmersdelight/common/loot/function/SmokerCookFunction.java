package vectorwing.farmersdelight.common.loot.function;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SmokerCookFunction extends LootItemConditionalFunction
{
	public static final MapCodec<SmokerCookFunction> CODEC = RecordCodecBuilder.mapCodec(
		i -> commonFields(i).apply(i, SmokerCookFunction::new)
	);

	protected SmokerCookFunction(List<LootItemCondition> predicates) {
		super(predicates);
	}

	@Override
	public MapCodec<? extends LootItemConditionalFunction> codec() {
		return CODEC;
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext context) {
		if (stack.isEmpty()) {
			return stack;
		}
		SingleRecipeInput input = new SingleRecipeInput(stack);
		Optional<RecipeHolder<SmokingRecipe>> recipe = context.getLevel().recipeAccess().getRecipeFor(RecipeType.SMOKING, input, context.getLevel());
		if (recipe.isPresent()) {
			ItemStack resultStack = recipe.get().value().assemble(input).copy();
			if (!resultStack.isEmpty()) {
				int newCount = stack.count() * resultStack.getCount();
				return resultStack.copyWithCount(Math.min(newCount, resultStack.getMaxStackSize()));
			}
		}

		return stack;
	}
}
