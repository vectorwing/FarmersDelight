package vectorwing.farmersdelight.integration.jei;

import com.google.common.collect.ImmutableList;
import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.gui.CookingPotScreen;
import vectorwing.farmersdelight.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.integration.jei.cooking.CookingRecipeCategory;
import vectorwing.farmersdelight.integration.jei.cutting.CuttingRecipeCategory;
import vectorwing.farmersdelight.integration.jei.decomposition.DecompositionDummy;
import vectorwing.farmersdelight.integration.jei.decomposition.DecompositionRecipeCategory;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.registry.ModItems;
import vectorwing.farmersdelight.tile.container.CookingPotContainer;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unused")
public class JEIPlugin implements IModPlugin
{
	private static final ResourceLocation ID = new ResourceLocation(FarmersDelight.MODID, "jei_plugin");
	private static final Minecraft MC = Minecraft.getInstance();

	private static List<IRecipe<?>> findRecipesByType(IRecipeType<?> type) {
		return MC.world
				.getRecipeManager()
				.getRecipes()
				.stream()
				.filter(r -> r.getType() == type)
				.collect(Collectors.toList());
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new CookingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new CuttingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new DecompositionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addRecipes(findRecipesByType(CookingPotRecipe.TYPE), CookingRecipeCategory.UID);
		registration.addRecipes(findRecipesByType(CuttingBoardRecipe.TYPE), CuttingRecipeCategory.UID);
		registration.addRecipes(ImmutableList.of(new DecompositionDummy()), DecompositionRecipeCategory.UID);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ModItems.COOKING_POT.get()), CookingRecipeCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(ModItems.CUTTING_BOARD.get()), CuttingRecipeCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(ModItems.STOVE.get()), VanillaRecipeCategoryUid.CAMPFIRE);
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.ORGANIC_COMPOST.get()), DecompositionRecipeCategory.UID);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(CookingPotScreen.class, 89, 25, 24, 17, CookingRecipeCategory.UID);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(CookingPotContainer.class, CookingRecipeCategory.UID, 0, 6, 9, 36);
	}

	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}
}
