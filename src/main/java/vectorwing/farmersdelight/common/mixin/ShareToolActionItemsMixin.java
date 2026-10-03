package vectorwing.farmersdelight.common.mixin;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vectorwing.farmersdelight.common.crafting.ingredient.ToolActionIngredient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(RecipeManager.class)
public abstract class ShareToolActionItemsMixin
{
	/**
	 * Lets the ToolActionIngredients parsed during a recipe reload share the items they find for each ToolAction.
	 * Otherwise, every one of them goes through the whole item registry, which is slow in large modpacks.
	 * This only saves time, so the injection is optional.
	 */
	@WrapOperation(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/RecipeManager;fromJson(Lnet/minecraft/resources/ResourceLocation;Lcom/google/gson/JsonObject;Lnet/minecraftforge/common/crafting/conditions/ICondition$IContext;)Lnet/minecraft/world/item/crafting/Recipe;", remap = false),
			require = 0)
	private Recipe<?> shareToolActionItems(ResourceLocation recipeId, JsonObject json, ICondition.IContext context, Operation<Recipe<?>> original,
			@Share("toolActionItems") LocalRef<Map<ToolAction, List<Item>>> sharedItems) {
		if (sharedItems.get() == null) {
			sharedItems.set(new HashMap<>());
		}

		Map<ToolAction, List<Item>> previousItems = ToolActionIngredient.setSharedItems(sharedItems.get());
		try {
			return original.call(recipeId, json, context);
		} finally {
			ToolActionIngredient.setSharedItems(previousItems);
		}
	}
}
