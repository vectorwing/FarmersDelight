package vectorwing.farmersdelight.common.registry;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.function.Supplier;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.DoughRecipe;
import vectorwing.farmersdelight.common.crafting.FoodServingRecipe;

public class ModRecipeSerializers
{
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FarmersDelight.MODID);

	public static final Supplier<RecipeSerializer<?>> COOKING = RECIPE_SERIALIZERS.register("cooking", CookingPotRecipe.Serializer::new);
	public static final Supplier<RecipeSerializer<?>> CUTTING = RECIPE_SERIALIZERS.register("cutting", CuttingBoardRecipe.Serializer::new);

	public static final Supplier<SimpleCraftingRecipeSerializer<?>> FOOD_SERVING =
			RECIPE_SERIALIZERS.register("food_serving", () -> new SimpleCraftingRecipeSerializer<>(FoodServingRecipe::new));
	public static final Supplier<SimpleCraftingRecipeSerializer<?>> DOUGH =
			RECIPE_SERIALIZERS.register("dough", () -> new SimpleCraftingRecipeSerializer<>(DoughRecipe::new));
}
