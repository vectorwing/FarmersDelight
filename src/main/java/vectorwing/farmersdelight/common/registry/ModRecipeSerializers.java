package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.*;

import java.util.function.Supplier;

public class ModRecipeSerializers
{
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, FarmersDelight.MODID);

	public static final Supplier<RecipeSerializer<?>> COOKING = RECIPE_SERIALIZERS.register("cooking", CookingPotRecipe.Serializer::new);
	public static final Supplier<RecipeSerializer<?>> CUTTING = RECIPE_SERIALIZERS.register("cutting", CuttingBoardRecipe.Serializer::new);
	public static final Supplier<RecipeSerializer<?>> SOAKING = RECIPE_SERIALIZERS.register("soaking", SoakingRecipe.Serializer::new);

	public static final Supplier<RecipeSerializer<?>> FLUID_FILLING = RECIPE_SERIALIZERS.register("fluid_filling", FluidFillingRecipe.Serializer::new);
	public static final Supplier<RecipeSerializer<?>> FLUID_EMPTYING = RECIPE_SERIALIZERS.register("fluid_emptying", FluidEmptyingRecipe.Serializer::new);

	public static final Supplier<SimpleCraftingRecipeSerializer<?>> FOOD_SERVING =
			RECIPE_SERIALIZERS.register("food_serving", () -> new SimpleCraftingRecipeSerializer<>(FoodServingRecipe::new));
	public static final Supplier<SimpleCraftingRecipeSerializer<?>> DOUGH =
			RECIPE_SERIALIZERS.register("dough", () -> new SimpleCraftingRecipeSerializer<>(DoughRecipe::new));
}
