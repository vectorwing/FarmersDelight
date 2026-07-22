package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.DoughRecipe;
import vectorwing.farmersdelight.common.crafting.FoodServingRecipe;

import java.util.function.Supplier;

public class ModRecipeSerializers
{
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, FarmersDelight.MODID);

	public static final Supplier<RecipeSerializer<CookingPotRecipe>> COOKING = RECIPE_SERIALIZERS.register("cooking",
			() -> new RecipeSerializer<>(CookingPotRecipe.Serializer.codec(), CookingPotRecipe.Serializer.streamCodec()));
	public static final Supplier<RecipeSerializer<CuttingBoardRecipe>> CUTTING = RECIPE_SERIALIZERS.register("cutting",
			() -> new RecipeSerializer<>(CuttingBoardRecipe.Serializer.codec(), CuttingBoardRecipe.Serializer.streamCodec()));

	public static final Supplier<RecipeSerializer<FoodServingRecipe>> FOOD_SERVING =
			RECIPE_SERIALIZERS.register("food_serving", () -> FoodServingRecipe.SERIALIZER);
	public static final Supplier<RecipeSerializer<DoughRecipe>> DOUGH =
			RECIPE_SERIALIZERS.register("dough", () -> DoughRecipe.SERIALIZER);
}
