package vectorwing.farmersdelight.integration.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.gui.CookingPotScreen;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModMenuTypes;
import vectorwing.farmersdelight.common.utility.TextUtils;
import vectorwing.farmersdelight.integration.jei.category.CookingRecipeCategory;
import vectorwing.farmersdelight.integration.jei.category.CuttingRecipeCategory;
import vectorwing.farmersdelight.integration.jei.category.DecompositionRecipeCategory;
import vectorwing.farmersdelight.integration.jei.resource.DecompositionDummy;
import vectorwing.farmersdelight.integration.jei.resource.DoughRecipeMaker;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@JeiPlugin
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unused")
public class JEIPlugin implements IModPlugin
{
	private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "jei_plugin");

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new CookingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new CuttingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new DecompositionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		FDRecipes modRecipes = new FDRecipes();
		registration.addRecipes(FDRecipeTypes.COOKING, modRecipes.getCookingPotRecipes());
		registration.addRecipes(FDRecipeTypes.CUTTING, modRecipes.getCuttingBoardRecipes());
		registration.addRecipes(FDRecipeTypes.DECOMPOSITION, ImmutableList.of(new DecompositionDummy()));

		registration.addRecipes(RecipeTypes.CRAFTING, DoughRecipeMaker.createRecipe());

		registration.addIngredientInfo(new ItemStack(ModItems.WHEAT_DOUGH.get()), VanillaTypes.ITEM_STACK, TextUtils.getTranslation("jei.info.dough"));
		registration.addIngredientInfo(new ItemStack(ModItems.STRAW.get()), VanillaTypes.ITEM_STACK, TextUtils.getTranslation("jei.info.straw"));
		registration.addIngredientInfo(new ItemStack(ModItems.HAM.get()), VanillaTypes.ITEM_STACK, TextUtils.getTranslation("jei.info.ham"));
		registration.addIngredientInfo(new ItemStack(ModItems.SMOKED_HAM.get()), VanillaTypes.ITEM_STACK, TextUtils.getTranslation("jei.info.ham"));
		registration.addIngredientInfo(new ItemStack(ModItems.FLINT_KNIFE.get()), VanillaTypes.ITEM_STACK, TextUtils.getTranslation("jei.info.knife"));
		registration.addIngredientInfo(new ItemStack(ModItems.IRON_KNIFE.get()), VanillaTypes.ITEM_STACK, TextUtils.getTranslation("jei.info.knife"));
		registration.addIngredientInfo(new ItemStack(ModItems.DIAMOND_KNIFE.get()), VanillaTypes.ITEM_STACK, TextUtils.getTranslation("jei.info.knife"));
		registration.addIngredientInfo(new ItemStack(ModItems.NETHERITE_KNIFE.get()), VanillaTypes.ITEM_STACK, TextUtils.getTranslation("jei.info.knife"));
		registration.addIngredientInfo(new ItemStack(ModItems.GOLDEN_KNIFE.get()), VanillaTypes.ITEM_STACK, TextUtils.getTranslation("jei.info.knife"));

		registration.addIngredientInfo(List.of(new ItemStack(ModItems.WILD_CABBAGES.get()), new ItemStack(ModItems.CABBAGE.get()), new ItemStack(ModItems.CABBAGE_LEAF.get())), VanillaTypes.ITEM_STACK, TextUtils.getTranslation("jei.info.wild_cabbages"));
		registration.addIngredientInfo(List.of(new ItemStack(ModItems.WILD_BEETROOTS.get()), new ItemStack(Items.BEETROOT)), VanillaTypes.ITEM_STACK, TextUtils.getTranslation("jei.info.wild_beetroots"));
		registration.addIngredientInfo(List.of(new ItemStack(ModItems.WILD_CARROTS.get()), new ItemStack(Items.CARROT)), VanillaTypes.ITEM_STACK, TextUtils.getTranslation("jei.info.wild_carrots"));
		registration.addIngredientInfo(List.of(new ItemStack(ModItems.WILD_ONIONS.get()), new ItemStack(ModItems.ONION.get())), VanillaTypes.ITEM_STACK, TextUtils.getTranslation("jei.info.wild_onions"));
		registration.addIngredientInfo(List.of(new ItemStack(ModItems.WILD_POTATOES.get()), new ItemStack(Items.POTATO)), VanillaTypes.ITEM_STACK, TextUtils.getTranslation("jei.info.wild_potatoes"));
		registration.addIngredientInfo(List.of(new ItemStack(ModItems.WILD_TOMATOES.get()), new ItemStack(ModItems.TOMATO.get())), VanillaTypes.ITEM_STACK, TextUtils.getTranslation("jei.info.wild_tomatoes"));
		registration.addIngredientInfo(List.of(new ItemStack(ModItems.WILD_RICE.get()), new ItemStack(ModItems.RICE.get()), new ItemStack(ModItems.RICE_PANICLE.get())), VanillaTypes.ITEM_STACK, TextUtils.getTranslation("jei.info.wild_rice"));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ModItems.COOKING_POT.get()), FDRecipeTypes.COOKING);
		registration.addRecipeCatalyst(new ItemStack(ModItems.CUTTING_BOARD.get()), FDRecipeTypes.CUTTING);
		registration.addRecipeCatalyst(new ItemStack(ModItems.STOVE.get()), RecipeTypes.CAMPFIRE_COOKING);
		registration.addRecipeCatalyst(new ItemStack(ModItems.SKILLET.get()), RecipeTypes.CAMPFIRE_COOKING);
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.ORGANIC_COMPOST.get()), FDRecipeTypes.DECOMPOSITION);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(CookingPotScreen.class, 89, 25, 24, 17, FDRecipeTypes.COOKING);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(CookingPotMenu.class, ModMenuTypes.COOKING_POT.get(), FDRecipeTypes.COOKING, 0, 6, 9, 36);
	}

	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}
}
