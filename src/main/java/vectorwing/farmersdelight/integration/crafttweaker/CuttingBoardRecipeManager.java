package vectorwing.farmersdelight.integration.crafttweaker;

import com.blamejared.crafttweaker.CraftTweaker;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;
import vectorwing.farmersdelight.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.integration.crafttweaker.actions.ActionRemoveCuttingBoardRecipe;
import vectorwing.farmersdelight.utils.ListUtils;

@ZenRegister
@ZenCodeType.Name("mods.farmersdelight.CuttingBoard")
public class CuttingBoardRecipeManager implements IRecipeManager
{
    @ZenCodeType.Method
    public void addRecipe(String name,
                          IIngredient input,
                          IItemStack[] results,
                          IIngredient tool,
                          @ZenCodeType.OptionalString String sound) {
        CraftTweakerAPI.apply(new ActionAddRecipe(this,
                new CuttingBoardRecipe(new ResourceLocation(CraftTweaker.MODID, name),
                        "",
                        input.asVanillaIngredient(),
                        tool.asVanillaIngredient(),
                        ListUtils.mapArrayIndexSet(results,
                                IItemStack::getInternal,
                                NonNullList.withSize(results.length, ItemStack.EMPTY)),
                        sound),
                ""));
    }

    @Override
    public void removeRecipe(IItemStack output) {
        removeRecipe(new IItemStack[]{output});
    }

    @ZenCodeType.Method
    public void removeRecipe(IItemStack[] outputs) {
        CraftTweakerAPI.apply(new ActionRemoveCuttingBoardRecipe(this, outputs));
    }

    @Override
    public IRecipeType<CuttingBoardRecipe> getRecipeType() {
        return CuttingBoardRecipe.TYPE;
    }
}
