package vectorwing.farmersdelight.integration.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.FluidEmptyingRecipe;
import vectorwing.farmersdelight.common.crafting.SoakingRecipe;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModMenuTypes;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.utility.RecipeUtils;
import vectorwing.farmersdelight.integration.emi.handler.CookingPotEmiRecipeHandler;
import vectorwing.farmersdelight.integration.emi.recipe.*;

import java.util.Arrays;
import java.util.List;

@EmiEntrypoint
public class EMIPlugin implements EmiPlugin
{
	@Override
	public void register(EmiRegistry registry) {
		registry.addCategory(FDRecipeCategories.COOKING);
		registry.addCategory(FDRecipeCategories.CUTTING);
		registry.addCategory(FDRecipeCategories.DECOMPOSITION);
		registry.addCategory(FDRecipeCategories.SOAKING);
		registry.addCategory(FDRecipeCategories.FLUID_EMPTYING);
		registry.addCategory(FDRecipeCategories.FLUID_FILLING);

		registry.addWorkstation(FDRecipeCategories.COOKING, FDRecipeWorkstations.COOKING_POT);
		registry.addWorkstation(FDRecipeCategories.CUTTING, FDRecipeWorkstations.CUTTING_BOARD);
		registry.addWorkstation(FDRecipeCategories.SOAKING, FDRecipeWorkstations.JUG);
		registry.addWorkstation(FDRecipeCategories.SOAKING, FDRecipeWorkstations.GLASS_JUG);
		registry.addWorkstation(FDRecipeCategories.FLUID_EMPTYING, FDRecipeWorkstations.FLUID_EMPTYING);
		registry.addWorkstation(FDRecipeCategories.FLUID_FILLING, FDRecipeWorkstations.FLUID_FILLING);

		registry.addRecipeHandler(ModMenuTypes.COOKING_POT.get(), new CookingPotEmiRecipeHandler());

		RegistryAccess registryAccess = Minecraft.getInstance().level.registryAccess();

		for (RecipeHolder<CookingPotRecipe> recipeHolder : registry.getRecipeManager().getAllRecipesFor(ModRecipeTypes.COOKING.get())) {
			CookingPotRecipe recipe = recipeHolder.value();
			registry.addRecipe(new CookingPotEmiRecipe(recipeHolder.id(), recipe.getIngredients().stream().map(EmiIngredient::of).toList(),
				EmiStack.of(recipe.getResultItem(registryAccess)), EmiStack.of(recipe.getOutputContainer()), recipe.getCookTime(), recipe.getExperience()));
		}

		for (RecipeHolder<CuttingBoardRecipe> recipeHolder : registry.getRecipeManager().getAllRecipesFor(ModRecipeTypes.CUTTING.get())) {
			CuttingBoardRecipe recipe = recipeHolder.value();
			registry.addRecipe(new CuttingEmiRecipe(recipeHolder.id(), EmiIngredient.of(recipe.getTool()), EmiIngredient.of(recipe.getIngredients().getFirst()),
				recipe.getRollableResults().stream().map(chanceResult -> EmiStack.of(chanceResult.stack()).setChance(chanceResult.chance())).toList()));
		}

		for (RecipeHolder<SoakingRecipe> recipeHolder : registry.getRecipeManager().getAllRecipesFor(ModRecipeTypes.SOAKING.get())) {
			SoakingRecipe recipe = recipeHolder.value();
			registry.addRecipe(new SoakingEmiRecipe(recipeHolder.id(), getFluidIngredient(recipe.getFluid()), EmiIngredient.of(recipe.getIngredients().getFirst()), EmiStack.of(recipe.getResultItem(registryAccess)), recipe.doesConsumeFluid()));
		}

		registry.addRecipe(new DecompositionEmiRecipe());

		addFluidHandlingRecipes(registry, registryAccess);
		addSpecialRecipes(registry);
	}

	public void addFluidHandlingRecipes(EmiRegistry registry, RegistryAccess provider) {
		for (RecipeHolder<FluidEmptyingRecipe> recipeHolder : registry.getRecipeManager().getAllRecipesFor(ModRecipeTypes.FLUID_EMPTYING.get())) {
			FluidEmptyingRecipe recipe = recipeHolder.value();
			registry.addRecipe(new FluidEmptyingEmiRecipe(recipeHolder.id(), EmiIngredient.of(recipe.getIngredients().getFirst()), EmiStack.of(recipe.getFluid().getFluid(), recipe.getFluid().getAmount()), EmiStack.of(recipe.getResultItem(provider))));
		}
	}

	public void addSpecialRecipes(EmiRegistry registry) {
		ResourceLocation doughRecipeId = RecipeUtils.FDLocation("wheat_dough_from_water");
		if (registry.getRecipeManager().byKey(doughRecipeId).isPresent()) {
			ResourceLocation syntheticLocation = ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "/crafting/wheat_dough_from_water");
			registry.addRecipe(new EmiCraftingRecipe(List.of(EmiStack.of(Items.WHEAT), EmiStack.of(Items.WATER_BUCKET)), EmiStack.of(ModItems.WHEAT_DOUGH.get()), syntheticLocation, true));
		}
	}

	public static EmiIngredient getFluidIngredient(SizedFluidIngredient fluidIngredient) {
		return EmiIngredient.of(Arrays.stream(fluidIngredient.ingredient().getStacks())
			.map(stack -> EmiStack.of(stack.getFluid(), stack.getComponents() instanceof PatchedDataComponentMap patched ? patched.asPatch() : DataComponentPatch.EMPTY, fluidIngredient.amount()))
			.toList());
	}
}
