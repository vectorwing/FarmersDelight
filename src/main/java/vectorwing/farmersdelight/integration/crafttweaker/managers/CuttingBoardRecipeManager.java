package vectorwing.farmersdelight.integration.crafttweaker.managers;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.CraftTweakerConstants;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.util.random.Percentaged;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.utility.ListUtils;
import vectorwing.farmersdelight.integration.crafttweaker.actions.ActionRemoveCuttingBoardRecipe;

/**
 * Farmer's Delight Cutting Board recipes.
 *
 * @docParam this <recipetype:farmersdelight:cutting>
 */
@Document("mods/FarmersDelight/CuttingBoard")
@ZenRegister
@ZenCodeType.Name("mods.farmersdelight.CuttingBoard")
public class CuttingBoardRecipeManager implements IRecipeManager
{
    /**
     * Add a cutting board recipe.
     * The tool ingredient can be a {@link net.neoforged.neoforge.common.ItemAbility}. It will get cast implicitly.
     * This allows you to work with any tool that provides that action to Forge.
     *
     * @param name    Name of the recipe to add
     * @param input   Input ingredient
     * @param results Output items
     * @param tool    Tool ingredient
     * @param sound   Sound event name
     *
     * @docParam name "cutting_board_test"
     * @docParam input <item:minecraft:gravel>
     * @docParam results [<item:minecraft:flint>]
     * @docParam tool <item:minecraft:string>
     * @docParam sound "minecraft:block.gravel.break"
     */
    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredient input,
                          Percentaged<IItemStack>[] results,
                          IIngredient tool,
                          @ZenCodeType.OptionalString String sound) {
        CraftTweakerAPI.apply(new ActionAddRecipe(this,
                new RecipeHolder(CraftTweakerConstants.rl(name),
                new CuttingBoardRecipe("",
                        input.asVanillaIngredient(),
                        tool.asVanillaIngredient(),
                        ListUtils.mapArrayIndexSet(results,
                                (stack) -> new ChanceResult(stack.getData().getInternal(), (float) stack.getPercentage()),
                                NonNullList.withSize(results.length, ChanceResult.EMPTY)),
                        BuiltInRegistries.SOUND_EVENT.getOptional(ResourceLocation.parse(sound)))),
                ""));
    }

    /**
     * Remove a cutting board recipe with multiple outputs.
     *
     * @param outputs Output items
     *
     * @docParam outputs [<item:farmersdelight:cooked_salmon_slice> * 2, <item:minecraft:bone_meal>]
     */
    @ZenCodeType.Method
    public void removeRecipe(IItemStack[] outputs) {
        CraftTweakerAPI.apply(new ActionRemoveCuttingBoardRecipe(this, outputs));
    }

    @Override
    public RecipeType<CuttingBoardRecipe> getRecipeType() {
        return ModRecipeTypes.CUTTING.get();
    }
}