package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.*;

import java.util.function.Supplier;

public class ModRecipeTypes
{
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, FarmersDelight.MODID);

	// Crafting
	public static final Supplier<RecipeType<CookingPotRecipe>> COOKING = RECIPE_TYPES.register("cooking", () -> registerRecipeType("cooking"));
	public static final Supplier<RecipeType<CuttingBoardRecipe>> CUTTING = RECIPE_TYPES.register("cutting", () -> registerRecipeType("cutting"));
	public static final Supplier<RecipeType<SoakingRecipe>> SOAKING = RECIPE_TYPES.register("soaking", () -> registerRecipeType("soaking"));

	// Technical
	public static final Supplier<RecipeType<FluidFillingRecipe>> FLUID_FILLING = RECIPE_TYPES.register("fluid_filling", () -> registerRecipeType("fluid_filling"));
	public static final Supplier<RecipeType<FluidEmptyingRecipe>> FLUID_EMPTYING = RECIPE_TYPES.register("fluid_emptying", () -> registerRecipeType("fluid_emptying"));

	public static <T extends Recipe<?>> RecipeType<T> registerRecipeType(final String identifier) {
		return new RecipeType<>()
		{
			public String toString() {
				return FarmersDelight.MODID + ":" + identifier;
			}
		};
	}
}
