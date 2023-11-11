package vectorwing.farmersdelight.common.registry;

import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.ingredient.ToolActionIngredient;

public class ModIngredientTypes {
    public static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPES = DeferredRegister.create(ForgeRegistries.Keys.INGREDIENT_TYPES, FarmersDelight.MODID);

    public static final RegistryObject<IngredientType<?>> TOOL_ACTION_INGREDIENT = INGREDIENT_TYPES.register("tool_action", () -> new IngredientType<>(ToolActionIngredient.CODEC));
}
