package vectorwing.farmersdelight.common.utility;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import vectorwing.farmersdelight.FarmersDelight;

public class RecipeUtils
{
	public static Identifier FDLocation(String name) {
		return Identifier.fromNamespaceAndPath(FarmersDelight.MODID, name);
	}

	public static ResourceKey<Recipe<?>> FDKey(String path) {
		return ResourceKey.create(Registries.RECIPE, RecipeUtils.FDLocation(path));
	}
}
