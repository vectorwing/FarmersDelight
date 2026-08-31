package vectorwing.farmersdelight.common.utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import vectorwing.farmersdelight.FarmersDelight;

import java.util.Optional;

public class RecipeUtils
{
	// Copyright (c) 2014-2015 mezz
	public static ItemStack getResultItem(Recipe<?> recipe) {
		Minecraft minecraft = Minecraft.getInstance();
		ClientLevel level = minecraft.level;
		if (level == null) {
			throw new NullPointerException("level must not be null.");
		}
		RegistryAccess registryAccess = level.registryAccess();
		return recipe.getResultItem(registryAccess);
	}

	public static ResourceLocation FDLocation(String name) {
		return ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, name);
	}

	public static String blockName(Block block) {
		return BuiltInRegistries.BLOCK.getKey(block).getPath();
	}

	public static String itemName(ItemLike item) {
		return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
	}

	public static Optional<RecipeHolder<CampfireCookingRecipe>> getCampfireCookingRecipe(ItemStack stack, Level level) {
		if (stack.isEmpty()) {
			return Optional.empty();
		}
		return level.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SingleRecipeInput(stack), level);
	}
}
