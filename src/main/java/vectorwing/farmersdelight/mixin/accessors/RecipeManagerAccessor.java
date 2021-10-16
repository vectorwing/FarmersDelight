package vectorwing.farmersdelight.mixin.accessors;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

/**
 * Credits to Botania's dev team for the implementation!
 */
@Mixin(RecipeManager.class)
public interface RecipeManagerAccessor
{
	@Invoker("byType")
	<C extends Container, T extends Recipe<C>> Map<ResourceLocation, Recipe<C>> getRecipeMap(RecipeType<T> type);
}
