package vectorwing.farmersdelight.common.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import vectorwing.farmersdelight.FarmersDelight;

public class RecipeUtils
{
	// Copyright (c) 2014-2015 mezz
	public static ItemStack getResultItem(Recipe<?> recipe) {
		return recipe.display().isEmpty() ? ItemStack.EMPTY : ItemStack.EMPTY;
	}

	public static Identifier FDLocation(String name) {
		return Identifier.fromNamespaceAndPath(FarmersDelight.MODID, name);
	}
}
