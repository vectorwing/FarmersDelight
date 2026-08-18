package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import vectorwing.farmersdelight.common.utility.RecipeUtils;

public class RegistryAliases
{
	public static void addRegistryAliases() {
		addBlockAlias("basket", "bamboo_basket");
		addItemAlias("basket", "bamboo_basket");
	}

	public static void addBlockAlias(String oldName, String newName) {
		BuiltInRegistries.BLOCK.addAlias(RecipeUtils.FDLocation(oldName), RecipeUtils.FDLocation(newName));
	}

	public static void addItemAlias(String oldName, String newName) {
		BuiltInRegistries.ITEM.addAlias(RecipeUtils.FDLocation(oldName), RecipeUtils.FDLocation(newName));
	}
}
