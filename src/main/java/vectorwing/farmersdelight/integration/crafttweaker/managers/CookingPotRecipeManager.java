package vectorwing.farmersdelight.integration.crafttweaker.managers;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.CraftTweakerConstants;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.utility.ListUtils;
import vectorwing.farmersdelight.integration.crafttweaker.FarmersDelightCrTPlugin;

/**
 * Farmer's Delight Cooking Pot recipes.
 *
 * @docParam this <recipetype:farmersdelight:cooking>
 */
@Document("mods/FarmersDelight/CookingPot")
@ZenRegister
@ZenCodeType.Name("mods.farmersdelight.CookingPot")
public class CookingPotRecipeManager implements IRecipeManager
{
    /**
     * Add a cooking pot recipe.
     * The Cooking Tab is optional.
     *
     * @param name       Name of the recipe to add
     * @param output     Output item
     * @param inputs     Input ingredients
     * @param container  Container item
     * @param experience Experience granted
     * @param cookTime   Cooking time
     *
     * @docParam name "cooking_pot_test"
     * @docParam output <item:minecraft:enchanted_golden_apple>
     * @docParam inputs [<item:minecraft:gold_block>]
     * @docParam cookingPotRecipeBookTab <constant:farmersdelight:cooking_pot_recipe_book_tab:misc>
     * @docParam container <item:minecraft:apple>
     * @docParam experience 100
     * @docParam cookTime 400
     */
    @ZenCodeType.Method
    public void addRecipe(String name,
                          IItemStack output,
                          IIngredient[] inputs,
                          @ZenCodeType.Optional CookingPotRecipeBookTab cookingPotRecipeBookTab,
                          @ZenCodeType.Optional IItemStack container,
                          @ZenCodeType.OptionalFloat float experience,
                          @ZenCodeType.OptionalInt(200) int cookTime) {
        if (!validateInputs(inputs)) return;

        CraftTweakerAPI.apply(new ActionAddRecipe(this,
                new RecipeHolder<>(
                        CraftTweakerConstants.rl(name),
                        new CookingPotRecipe(
                                "",
                                cookingPotRecipeBookTab,
                                ListUtils.mapArrayIndexSet(inputs,
                                        IIngredient::asVanillaIngredient,
                                        NonNullList.withSize(inputs.length, Ingredient.EMPTY)),
                                output.getInternal(),
                                container == null ? ItemStack.EMPTY : container.getInternal(),
                                experience,
                                cookTime)
                ), ""));
    }

    private boolean validateInputs(IIngredient[] inputs) {
        if (inputs.length == 0) {
            FarmersDelightCrTPlugin.LOGGER_CT.error("No ingredients for cooking recipe");
            return false;
        } else if (inputs.length > CookingPotRecipe.INPUT_SLOTS) {
            FarmersDelightCrTPlugin.LOGGER_CT.error("Too many ingredients for cooking recipe! The max is %s", CookingPotRecipe.INPUT_SLOTS);
            return false;
        }
        return true;
    }

    @Override
    public RecipeType<CookingPotRecipe> getRecipeType() {
        return ModRecipeTypes.COOKING.get();
    }
}