package vectorwing.farmersdelight.common.loot.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModLootFunctions;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SmokerCookFunction extends LootItemConditionalFunction
{
	public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "smoker_cook");
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
		} else {
			Optional<RecipeHolder<SmokingRecipe>> recipe = context.getLevel().getRecipeManager().getAllRecipesFor(RecipeType.SMOKING).stream()
					.filter(r -> r.value().getIngredients().get(0).test(stack)).findFirst();
			if (recipe.isPresent()) {
				ItemStack result = recipe.get().value().getResultItem(context.getLevel().registryAccess()).copy();
				result.setCount(result.getCount() * stack.getCount());
				return result;
			} else {
				return stack;
			}
		}
	}

	@Override
	public LootItemFunctionType<SmokerCookFunction> getType() {
		return ModLootFunctions.SMOKER_COOK.get();
	}
}
