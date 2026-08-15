package vectorwing.farmersdelight.common.registry;

import net.minecraft.resources.Identifier;
import vectorwing.farmersdelight.FarmersDelight;

public class RegistryAliases
{
	public static void addRegistryAliases() {
		addBlockAlias("basket", "bamboo_basket");
		addItemAlias("basket", "bamboo_basket");
	}

	public static void addBlockAlias(String oldName, String newName) {
		ModBlocks.BLOCKS.addAlias(fdLocation(oldName), fdLocation(newName));
	}

	public static void addItemAlias(String oldName, String newName) {
		ModItems.ITEMS.addAlias(fdLocation(oldName), fdLocation(newName));
	}

	private static Identifier fdLocation(String name) {
		return Identifier.fromNamespaceAndPath(FarmersDelight.MODID, name);
	}
}
