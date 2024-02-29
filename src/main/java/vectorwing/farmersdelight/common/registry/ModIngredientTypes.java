package vectorwing.farmersdelight.common.registry;

import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.ingredient.ToolActionIngredient;

import java.util.function.Supplier;

public class ModIngredientTypes {
    public static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPES = DeferredRegister.create(NeoForgeRegistries.INGREDIENT_TYPES, FarmersDelight.MODID);

    public static final Supplier<IngredientType<?>> TOOL_ACTION_INGREDIENT = INGREDIENT_TYPES.register("tool_action", () -> new IngredientType<>(ToolActionIngredient.CODEC));
}
