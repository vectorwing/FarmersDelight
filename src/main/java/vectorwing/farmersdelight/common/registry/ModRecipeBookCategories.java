package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;

import java.util.function.Supplier;

public class ModRecipeBookCategories
{
    public static final DeferredRegister<RecipeBookCategory> RECIPE_BOOK_CATEGORIES =
            DeferredRegister.create(Registries.RECIPE_BOOK_CATEGORY, FarmersDelight.MODID);

    public static final Supplier<RecipeBookCategory> COOKING_MEALS =
            RECIPE_BOOK_CATEGORIES.register("cooking_meals", RecipeBookCategory::new);

    public static final Supplier<RecipeBookCategory> COOKING_DRINKS =
            RECIPE_BOOK_CATEGORIES.register("cooking_drinks", RecipeBookCategory::new);

    public static final Supplier<RecipeBookCategory> COOKING_MISC =
            RECIPE_BOOK_CATEGORIES.register("cooking_misc", RecipeBookCategory::new);

    public static final Supplier<RecipeBookCategory> CUTTING =
            RECIPE_BOOK_CATEGORIES.register("cutting", RecipeBookCategory::new);
}
