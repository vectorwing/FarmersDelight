package vectorwing.farmersdelight.mixin.accessors;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

/**
 * Credits to Botania's dev team for the implementation!
 */
@Mixin(RecipeManager.class)
public interface RecipeManagerAccessor
{
	@Invoker("getRecipes")
	<C extends IInventory, T extends IRecipe<C>> Map<ResourceLocation, IRecipe<C>> getRecipeMap(IRecipeType<T> type);
}
