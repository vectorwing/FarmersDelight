//package vectorwing.farmersdelight.common.mixin.accessor;
//
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.Container;
//import net.minecraft.world.item.crafting.*;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.gen.Invoker;
//
//import java.util.Collection;
//import java.util.Map;
//
///**
// * Credits to Botania's dev team for the implementation!
// */
//@Mixin(RecipeManager.class)
//public interface RecipeManagerAccessor
//{
//	@Invoker("byType")
//	<I extends RecipeInput, T extends Recipe<I>> Collection<RecipeHolder<T>> getRecipeMap(RecipeType<T> type);
//}
