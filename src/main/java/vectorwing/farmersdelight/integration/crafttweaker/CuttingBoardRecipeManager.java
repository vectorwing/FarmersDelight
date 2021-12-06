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
//import net.minecraft.item.ItemStack;
//import net.minecraft.world.item.crafting.RecipeType;
//import net.minecraft.core.NonNullList;
//import net.minecraft.resources.ResourceLocation;
//import org.openzen.zencode.java.ZenCodeType;
//import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
//import vectorwing.farmersdelight.common.crafting.ingredients.ChanceResult;
//import vectorwing.farmersdelight.integration.crafttweaker.actions.ActionRemoveCuttingBoardRecipe;
//import vectorwing.farmersdelight.core.utils.ListUtils;
//
///**
// * Farmer's Delight Cutting Board recipes.
// *
// * @docParam this <recipetype:farmersdelight:cutting>
// */
//@Document("mods/farmersdelight/CuttingBoard")
//@ZenRegister
//@ZenCodeType.Name("mods.farmersdelight.CuttingBoard")
//public class CuttingBoardRecipeManager implements IRecipeManager
//{
//    /**
//     * Add a cutting board recipe.
//     *
//     * @param name    Name of the recipe to add
//     * @param input   Input ingredient
//     * @param results Output items
//     * @param tool    Tool ingredient
//     * @param sound   Sound event name
//     *
//     * @docParam name "cutting_board_test"
//     * @docParam input <item:minecraft:gravel>
//     * @docParam results [<item:minecraft:flint>]
//     * @docParam tool <item:minecraft:string>
//     * @docParam sound "minecraft:block.gravel.break"
//     */
//    @ZenCodeType.Method
//    public void addRecipe(String name,
//                          IIngredient input,
//                          IItemStack[] results,
//                          IIngredient tool,
//                          @ZenCodeType.OptionalString String sound) {
//        CraftTweakerAPI.apply(new ActionAddRecipe(this,
//                new CuttingBoardRecipe(new ResourceLocation(CraftTweaker.MODID, name),
//                        "",
//                        input.asVanillaIngredient(),
//                        tool.asVanillaIngredient(),
//                        ListUtils.mapArrayIndexSet(results,
//                                (stack) -> new ChanceResult(stack.getInternal(), 1),
//                                NonNullList.withSize(results.length, ChanceResult.EMPTY)),
//                        sound),
//                ""));
//    }
//
//    @Override
//    public void removeRecipe(IItemStack output) {
//        removeRecipe(new IItemStack[]{output});
//    }
//
//    /**
//     * Remove a cutting board recipe with multiple outputs.
//     *
//     * @param outputs Output items
//     *
//     * @docParam outputs [<item:farmersdelight:cooked_salmon_slice> * 2, <item:minecraft:bone_meal>]
//     */
//    @ZenCodeType.Method
//    public void removeRecipe(IItemStack[] outputs) {
//        CraftTweakerAPI.apply(new ActionRemoveCuttingBoardRecipe(this, outputs));
//    }
//
//    @Override
//    public RecipeType<CuttingBoardRecipe> getRecipeType() {
//        return CuttingBoardRecipe.TYPE;
//    }
//}
