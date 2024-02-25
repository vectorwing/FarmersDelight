package vectorwing.farmersdelight.integration.crafttweaker.handlers;

import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.item.MCItemStackMutable;
import com.blamejared.crafttweaker.api.recipe.component.BuiltinRecipeComponents;
import com.blamejared.crafttweaker.api.recipe.component.IDecomposedRecipe;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.util.StringUtil;
import com.blamejared.crafttweaker.impl.helper.AccessibleElementsProvider;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@IRecipeHandler.For(CookingPotRecipe.class)
public final class CookingPotRecipeHandler implements IRecipeHandler<CookingPotRecipe>
{
    @Override
    public String dumpToCommandString(IRecipeManager<? super CookingPotRecipe> manager, RegistryAccess registryAccess, RecipeHolder<CookingPotRecipe> recipe) {
        return String.format(
                "%s.addRecipe(%s, %s, %s, %s, %s, %s);",
                manager.getCommandString(),
                StringUtil.quoteAndEscape(recipe.id()),
                IItemStack.of(recipe.value().getResultItem(registryAccess)).getCommandString(),
                recipe.value().getIngredients().stream()
                        .map(IIngredient::fromIngredient)
                        .map(IIngredient::getCommandString)
                        .collect(Collectors.joining(", ", "[", "]")),
                new MCItemStackMutable(recipe.value().getOutputContainer()).getCommandString(),
                recipe.value().getExperience(),
                recipe.value().getCookTime()
        );
    }

    @Override
    public <U extends Recipe<?>> boolean doesConflict(IRecipeManager<? super CookingPotRecipe> manager, CookingPotRecipe firstRecipe, U secondRecipe) {
        return firstRecipe.equals(secondRecipe);
    }

    @Override
    public Optional<IDecomposedRecipe> decompose(IRecipeManager<? super CookingPotRecipe> manager, RegistryAccess registryAccess, CookingPotRecipe recipe) {
        final IDecomposedRecipe decomposedRecipe = IDecomposedRecipe.builder()
                .with(BuiltinRecipeComponents.Output.ITEMS, IItemStack.of(AccessibleElementsProvider.get().registryAccess(recipe::getResultItem)))
                .with(BuiltinRecipeComponents.Input.INGREDIENTS,  recipe.getIngredients().stream().map(IIngredient::fromIngredient).toList())
                .with(BuiltinRecipeComponents.Processing.TIME, recipe.getCookTime())
                .with(BuiltinRecipeComponents.Metadata.GROUP, recipe.getGroup())
                .with(RecipeHandlerUtils.CONTAINER_COMPONENT, new MCItemStackMutable(recipe.getOutputContainer()))
                .with(BuiltinRecipeComponents.Output.EXPERIENCE, recipe.getExperience())
                .build();
        if (recipe.getRecipeBookTab() != null) {
            decomposedRecipe.set(RecipeHandlerUtils.COOKING_TAB_COMPONENT, recipe.getRecipeBookTab().name());
        }
        return Optional.of(decomposedRecipe);
    }

    @Override
    public Optional<CookingPotRecipe> recompose(IRecipeManager<? super CookingPotRecipe> manager, RegistryAccess registryAccess, IDecomposedRecipe recipe) {
        final IItemStack output = recipe.getOrThrowSingle(BuiltinRecipeComponents.Output.ITEMS);
        final List<IIngredient> ingredients = recipe.getOrThrow(BuiltinRecipeComponents.Input.INGREDIENTS);
        final NonNullList<Ingredient> inputList = NonNullList.create();
        for (IIngredient ingredient : ingredients) {
            if (!ingredient.isEmpty()) {
                inputList.add(ingredient.asVanillaIngredient());
            }
        }
        final int time = recipe.getOrThrowSingle(BuiltinRecipeComponents.Processing.TIME);
        final String group = recipe.getOrThrowSingle(BuiltinRecipeComponents.Metadata.GROUP);
        final IItemStack container = recipe.getOrThrowSingle(RecipeHandlerUtils.CONTAINER_COMPONENT);
        final float exp = recipe.getOrThrowSingle(BuiltinRecipeComponents.Output.EXPERIENCE);
        //We're using get here to avoid exceptions.
        final List<String> cookingRecipeBookTabList = recipe.get(RecipeHandlerUtils.COOKING_TAB_COMPONENT);
        final CookingPotRecipeBookTab cookTab = cookingRecipeBookTabList == null ? null : CookingPotRecipeBookTab.valueOf(cookingRecipeBookTabList.get(0));
        return Optional.of(new CookingPotRecipe(group, cookTab, inputList, output.getInternal(), container.getInternal(), exp, time));
    }
}