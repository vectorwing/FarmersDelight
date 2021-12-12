//package vectorwing.farmersdelight.integration.crafttweaker;
//
//import com.blamejared.crafttweaker.CraftTweaker;
//import com.blamejared.crafttweaker.api.CraftTweakerAPI;
//import com.blamejared.crafttweaker.api.annotations.ZenRegister;
//import com.blamejared.crafttweaker.api.item.IIngredient;
//import com.blamejared.crafttweaker.api.item.IItemStack;
//import com.blamejared.crafttweaker.api.managers.IRecipeManager;
//import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
//import com.blamejared.crafttweaker_annotations.annotations.Document;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.RecipeType;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraft.core.NonNullList;
//import net.minecraft.resources.ResourceLocation;
//import org.openzen.zencode.java.ZenCodeType;
//import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
//import vectorwing.farmersdelight.core.utils.ListUtils;
//
///**
// * Farmer's Delight Cooking Pot recipes.
// *
// * @docParam this <recipetype:farmersdelight:cooking>
// */
//@Document("mods/farmersdelight/CookingPot")
//@ZenRegister
//@ZenCodeType.Name("mods.farmersdelight.CookingPot")
//public class CookingPotRecipeManager implements IRecipeManager
//{
//    /**
//     * Add a cooking pot recipe.
//     *
//     * @param name       Name of the recipe to add
//     * @param output     Output item
//     * @param inputs     Input ingredients
//     * @param container  Container item
//     * @param experience Experience granted
//     * @param cookTime   Cooking time
//     *
//     * @docParam name "cooking_pot_test"
//     * @docParam output <item:minecraft:enchanted_golden_apple>
//     * @docParam inputs [<item:minecraft:gold_block>]
//     * @docParam container <item:minecraft:apple>
//     * @docParam experience 100
//     * @docParam cookTime 400
//     */
//    @ZenCodeType.Method
//    public void addRecipe(String name,
//                          IItemStack output,
//                          IIngredient[] inputs,
//                          @ZenCodeType.Optional IItemStack container,
//                          @ZenCodeType.OptionalFloat float experience,
//                          @ZenCodeType.OptionalInt(200) int cookTime) {
//        if (!validateInputs(inputs)) return;
//
//        CraftTweakerAPI.apply(new ActionAddRecipe(this,
//                new CookingPotRecipe(new ResourceLocation(CraftTweaker.MODID, name),
//                        "",
//                        ListUtils.mapArrayIndexSet(inputs,
//                                IIngredient::asVanillaIngredient,
//                                NonNullList.withSize(inputs.length, Ingredient.EMPTY)),
//                        output.getInternal(),
//                        container == null ? ItemStack.EMPTY : container.getInternal(),
//                        experience,
//                        cookTime),
//                ""));
//    }
//
//    private boolean validateInputs(IIngredient[] inputs) {
//        if (inputs.length == 0) {
//            CraftTweakerAPI.logError("No ingredients for cooking recipe");
//            return false;
//        } else if (inputs.length > CookingPotRecipe.INPUT_SLOTS) {
//            CraftTweakerAPI.logError("Too many ingredients for cooking recipe! The max is %s", CookingPotRecipe.INPUT_SLOTS);
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public RecipeType<CookingPotRecipe> getRecipeType() {
//        return CookingPotRecipe.TYPE;
//    }
//}
