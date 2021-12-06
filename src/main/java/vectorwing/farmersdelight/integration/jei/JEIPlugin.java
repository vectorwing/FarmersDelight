package vectorwing.farmersdelight.integration.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.gui.CookingPotScreen;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotContainer;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.core.registry.ModBlocks;
import vectorwing.farmersdelight.core.registry.ModItems;
import vectorwing.farmersdelight.core.util.TextUtils;
import vectorwing.farmersdelight.integration.jei.category.CookingRecipeCategory;
import vectorwing.farmersdelight.integration.jei.category.CuttingRecipeCategory;
import vectorwing.farmersdelight.integration.jei.category.DecompositionRecipeCategory;
import vectorwing.farmersdelight.integration.jei.resource.DecompositionDummy;

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

	private static List<Recipe<?>> findRecipesByType(RecipeType<?> type) {
		return MC.level
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
		registration.addIngredientInfo(new ItemStack(ModItems.STRAW.get()), VanillaTypes.ITEM, TextUtils.getTranslation("jei.info.straw"));
		registration.addIngredientInfo(new ItemStack(ModItems.HAM.get()), VanillaTypes.ITEM, TextUtils.getTranslation("jei.info.ham"));
		registration.addIngredientInfo(new ItemStack(ModItems.SMOKED_HAM.get()), VanillaTypes.ITEM, TextUtils.getTranslation("jei.info.ham"));
		registration.addIngredientInfo(new ItemStack(ModItems.FLINT_KNIFE.get()), VanillaTypes.ITEM, TextUtils.getTranslation("jei.info.knife"));
		registration.addIngredientInfo(new ItemStack(ModItems.IRON_KNIFE.get()), VanillaTypes.ITEM, TextUtils.getTranslation("jei.info.knife"));
		registration.addIngredientInfo(new ItemStack(ModItems.DIAMOND_KNIFE.get()), VanillaTypes.ITEM, TextUtils.getTranslation("jei.info.knife"));
		registration.addIngredientInfo(new ItemStack(ModItems.NETHERITE_KNIFE.get()), VanillaTypes.ITEM, TextUtils.getTranslation("jei.info.knife"));
		registration.addIngredientInfo(new ItemStack(ModItems.GOLDEN_KNIFE.get()), VanillaTypes.ITEM, TextUtils.getTranslation("jei.info.knife"));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ModItems.COOKING_POT.get()), CookingRecipeCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(ModItems.CUTTING_BOARD.get()), CuttingRecipeCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(ModItems.STOVE.get()), VanillaRecipeCategoryUid.CAMPFIRE);
		registration.addRecipeCatalyst(new ItemStack(ModItems.SKILLET.get()), VanillaRecipeCategoryUid.CAMPFIRE);
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
