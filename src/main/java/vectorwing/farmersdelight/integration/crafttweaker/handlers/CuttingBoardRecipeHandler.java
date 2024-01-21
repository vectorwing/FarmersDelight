package vectorwing.farmersdelight.integration.crafttweaker.handlers;

import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.item.MCItemStack;
import com.blamejared.crafttweaker.api.item.MCItemStackMutable;
import com.blamejared.crafttweaker.api.recipe.component.BuiltinRecipeComponents;
import com.blamejared.crafttweaker.api.recipe.component.IDecomposedRecipe;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.util.StringUtil;
import com.blamejared.crafttweaker.api.util.random.Percentaged;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@IRecipeHandler.For(CuttingBoardRecipe.class)
public class CuttingBoardRecipeHandler implements IRecipeHandler<CuttingBoardRecipe>
{
    @Override
    public String dumpToCommandString(IRecipeManager manager, CuttingBoardRecipe recipe) {
        return String.format(
                "%s.addRecipe(%s, %s, %s, %s, %s);",
                manager.getCommandString(),
                StringUtil.quoteAndEscape(recipe.getId()),
                IIngredient.fromIngredient(recipe.getIngredients().get(0)).getCommandString(),
                recipe.getResults().stream()
                        .map(MCItemStackMutable::new)
                        .map(MCItemStackMutable::getCommandString)
                        .collect(Collectors.joining(", ", "[", "]")),
                IIngredient.fromIngredient(recipe.getTool()).getCommandString(),
                recipe.getSoundEventID()
        );
    }

    @Override
    public <U extends Recipe<?>> boolean doesConflict(IRecipeManager<? super CuttingBoardRecipe> manager, CuttingBoardRecipe firstRecipe, U secondRecipe) {
        return firstRecipe.equals(secondRecipe);
    }

    @Override
    public Optional<IDecomposedRecipe> decompose(IRecipeManager<? super CuttingBoardRecipe> manager, CuttingBoardRecipe recipe) {

        final IDecomposedRecipe decomposedRecipe = IDecomposedRecipe
                .builder()
                .with(BuiltinRecipeComponents.Input.INGREDIENTS, recipe.getIngredients().stream().map(IIngredient::fromIngredient).toList())
                .with(RecipeHandlerUtils.TOOL_COMPONENT, IIngredient.fromIngredient(recipe.getTool()))
                .with(BuiltinRecipeComponents.Metadata.GROUP, recipe.getGroup())
                .with(BuiltinRecipeComponents.Output.CHANCED_ITEMS,
                        recipe.getRollableResults().stream()
                        .map(chanceResult -> new MCItemStack(chanceResult.getStack()).percent(
                         chanceResult.getChance())).toList())
                .build();
        if (!recipe.getSoundEventID().equals("")){
            decomposedRecipe.set(RecipeHandlerUtils.SOUND_COMPONENT, recipe.getSoundEventID());
        }
        return Optional.of(decomposedRecipe);
    }

    @Override
    public Optional<CuttingBoardRecipe> recompose(IRecipeManager<? super CuttingBoardRecipe> manager, ResourceLocation name, IDecomposedRecipe recipe) {
        final String group = recipe.getOrThrowSingle(BuiltinRecipeComponents.Metadata.GROUP);
        final List<IIngredient> ingredients = recipe.getOrThrow(BuiltinRecipeComponents.Input.INGREDIENTS);
        final IIngredient tool = recipe.getOrThrowSingle(RecipeHandlerUtils.TOOL_COMPONENT);
        final IIngredient[] ingredientArray = ingredients.toArray(IIngredient[]::new);
        final List<Percentaged<IItemStack>> results = recipe.getOrThrow(BuiltinRecipeComponents.Output.CHANCED_ITEMS);
        final NonNullList<ChanceResult> stackedResults = NonNullList.create();
        stackedResults.addAll(results.stream().map(iItemStackPercentaged -> new ChanceResult(iItemStackPercentaged.getData().getInternal(), (float) iItemStackPercentaged.getPercentage())).toList());
        final List<String> soundList = recipe.get(RecipeHandlerUtils.SOUND_COMPONENT);
        final String sound = soundList == null ? "" : soundList.get(0);
        final Ingredient input = ingredientArray[0].asVanillaIngredient();
        return Optional.of(new CuttingBoardRecipe(name, group, input, tool.asVanillaIngredient(), stackedResults, sound));
    }
}
