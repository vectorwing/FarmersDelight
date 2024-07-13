package vectorwing.farmersdelight.common.registry;

import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.ingredient.ItemAbilityIngredient;

import java.util.function.Supplier;

public class ModIngredientTypes {
    public static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPES = DeferredRegister.create(NeoForgeRegistries.INGREDIENT_TYPES, FarmersDelight.MODID);

    public static final Supplier<IngredientType<?>> ITEM_ABILITY_INGREDIENT = INGREDIENT_TYPES.register("item_ability", () -> new IngredientType<>(ItemAbilityIngredient.CODEC));
}
