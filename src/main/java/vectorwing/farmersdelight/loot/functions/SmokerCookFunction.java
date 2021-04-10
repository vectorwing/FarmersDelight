package vectorwing.farmersdelight.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import mezz.jei.api.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SmokingRecipe;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SmokerCookFunction extends LootFunction {
    public static final ResourceLocation ID = new ResourceLocation(FarmersDelight.MODID, "smoker_cook");

    protected SmokerCookFunction(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected ItemStack doApply(ItemStack stack, LootContext context) {
        if (stack.isEmpty()) {
            return stack;
        } else {
            Optional<SmokingRecipe> recipe = context.getWorld().getRecipeManager().getRecipesForType(IRecipeType.SMOKING).stream()
                    .filter(r -> r.getIngredients().get(0).test(stack)).findFirst();
            if (recipe.isPresent()) {
                ItemStack result = recipe.get().getRecipeOutput().copy();
                result.setCount(result.getCount() * stack.getCount());
                return result;
            } else {
                return stack;
            }
        }
    }

    @Override
    @Nullable
    public LootFunctionType getFunctionType() {
        return null;
    }

    public static class Serializer extends LootFunction.Serializer<SmokerCookFunction> {
        @Override
        public SmokerCookFunction deserialize(JsonObject _object, JsonDeserializationContext _context, ILootCondition[] conditionsIn) {
            return new SmokerCookFunction(conditionsIn);
        }
    }
}
