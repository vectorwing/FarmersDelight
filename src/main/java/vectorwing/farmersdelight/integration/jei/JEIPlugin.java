package vectorwing.farmersdelight.integration.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import org.jspecify.annotations.NullMarked;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.gui.CookingPotScreen;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModMenuTypes;
import vectorwing.farmersdelight.common.utility.TextUtils;
import vectorwing.farmersdelight.integration.jei.category.CookingRecipeCategory;
import vectorwing.farmersdelight.integration.jei.category.CuttingRecipeCategory;
import vectorwing.farmersdelight.integration.jei.category.DecompositionRecipeCategory;
import vectorwing.farmersdelight.integration.jei.resource.DecompositionDummy;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@JeiPlugin
@ParametersAreNonnullByDefault
@NullMarked
@SuppressWarnings("unused")
public class JEIPlugin implements IModPlugin
{
	private static final Identifier ID = Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "jei_plugin");

    private IJeiRuntime runtime;
    private final Set<String> publishedCookingRecipes = new HashSet<>();
    private final Set<String> publishedCuttingRecipes = new HashSet<>();

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new CookingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new CuttingRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new DecompositionRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		FDRecipes modRecipes = new FDRecipes();

        // PORT34_STARTUP_RECIPE_REGISTRATION
        List<RecipeHolder<CookingPotRecipe>> startupCookingRecipes = modRecipes.getCookingPotRecipes();
        List<RecipeHolder<CuttingBoardRecipe>> startupCuttingRecipes = modRecipes.getCuttingBoardRecipes();

        if (!startupCookingRecipes.isEmpty()) {
            registration.addRecipes(FDRecipeTypes.COOKING, startupCookingRecipes);
            startupCookingRecipes.forEach(holder ->
                    publishedCookingRecipes.add(holder.id().identifier().toString()));
        }

        if (!startupCuttingRecipes.isEmpty()) {
            registration.addRecipes(FDRecipeTypes.CUTTING, startupCuttingRecipes);
            startupCuttingRecipes.forEach(holder ->
                    publishedCuttingRecipes.add(holder.id().identifier().toString()));
        }

		registration.addRecipes(FDRecipeTypes.DECOMPOSITION, ImmutableList.of(new DecompositionDummy()));

		registration.addRecipes(RecipeTypes.CRAFTING, modRecipes.getSpecialCraftingRecipes());

		registration.addIngredientInfo(new ItemStack(ModItems.WHEAT_DOUGH.get()), VanillaTypes.ITEM_STACK, TextUtils.JEI("info.dough"));
		registration.addIngredientInfo(new ItemStack(ModItems.STRAW.get()), VanillaTypes.ITEM_STACK, TextUtils.JEI("info.straw"));
		registration.addIngredientInfo(new ItemStack(ModItems.HAM.get()), VanillaTypes.ITEM_STACK, TextUtils.JEI("info.ham"));
		registration.addIngredientInfo(new ItemStack(ModItems.SMOKED_HAM.get()), VanillaTypes.ITEM_STACK, TextUtils.JEI("info.ham"));
		registration.addIngredientInfo(new ItemStack(ModItems.FLINT_KNIFE.get()), VanillaTypes.ITEM_STACK, TextUtils.JEI("info.knife"));
		registration.addIngredientInfo(new ItemStack(ModItems.IRON_KNIFE.get()), VanillaTypes.ITEM_STACK, TextUtils.JEI("info.knife"));
		registration.addIngredientInfo(new ItemStack(ModItems.DIAMOND_KNIFE.get()), VanillaTypes.ITEM_STACK, TextUtils.JEI("info.knife"));
		registration.addIngredientInfo(new ItemStack(ModItems.NETHERITE_KNIFE.get()), VanillaTypes.ITEM_STACK, TextUtils.JEI("info.knife"));
		registration.addIngredientInfo(new ItemStack(ModItems.GOLDEN_KNIFE.get()), VanillaTypes.ITEM_STACK, TextUtils.JEI("info.knife"));

		registration.addIngredientInfo(List.of(new ItemStack(ModItems.WILD_CABBAGES.get()), new ItemStack(ModItems.CABBAGE.get()), new ItemStack(ModItems.CABBAGE_LEAF.get())), VanillaTypes.ITEM_STACK, TextUtils.JEI("info.wild_cabbages"));
		registration.addIngredientInfo(List.of(new ItemStack(ModItems.WILD_BEETROOTS.get()), new ItemStack(Items.BEETROOT)), VanillaTypes.ITEM_STACK, TextUtils.JEI("info.wild_beetroots"));
		registration.addIngredientInfo(List.of(new ItemStack(ModItems.WILD_CARROTS.get()), new ItemStack(Items.CARROT)), VanillaTypes.ITEM_STACK, TextUtils.JEI("info.wild_carrots"));
		registration.addIngredientInfo(List.of(new ItemStack(ModItems.WILD_ONIONS.get()), new ItemStack(ModItems.ONION.get())), VanillaTypes.ITEM_STACK, TextUtils.JEI("info.wild_onions"));
		registration.addIngredientInfo(List.of(new ItemStack(ModItems.WILD_POTATOES.get()), new ItemStack(Items.POTATO)), VanillaTypes.ITEM_STACK, TextUtils.JEI("info.wild_potatoes"));
		registration.addIngredientInfo(List.of(new ItemStack(ModItems.WILD_TOMATOES.get()), new ItemStack(ModItems.TOMATO.get())), VanillaTypes.ITEM_STACK, TextUtils.JEI("info.wild_tomatoes"));
		registration.addIngredientInfo(List.of(new ItemStack(ModItems.WILD_RICE.get()), new ItemStack(ModItems.RICE.get()), new ItemStack(ModItems.RICE_PANICLE.get())), VanillaTypes.ITEM_STACK, TextUtils.JEI("info.wild_rice"));
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addCraftingStation(FDRecipeTypes.COOKING, new ItemStack(ModItems.COOKING_POT.get()));
		registration.addCraftingStation(FDRecipeTypes.CUTTING, new ItemStack(ModItems.CUTTING_BOARD.get()));
		registration.addCraftingStation(RecipeTypes.CAMPFIRE_COOKING, new ItemStack(ModItems.STOVE.get()));
		registration.addCraftingStation(RecipeTypes.CAMPFIRE_COOKING, new ItemStack(ModItems.SKILLET.get()));
		registration.addCraftingStation(FDRecipeTypes.DECOMPOSITION, new ItemStack(ModBlocks.ORGANIC_COMPOST.get()));
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
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        this.runtime = jeiRuntime;
        FDRecipes.setRecipeUpdateListener(this::publishSyncedRecipes);
        publishSyncedRecipes();
    }

    @Override
    public void onRuntimeUnavailable() {
        FDRecipes.clearRecipeUpdateListener();
        this.runtime = null;
        this.publishedCookingRecipes.clear();
        this.publishedCuttingRecipes.clear();
    }

    private void publishSyncedRecipes() {
        if (this.runtime == null) {
            return;
        }

        FDRecipes recipes = new FDRecipes();

        List<RecipeHolder<CookingPotRecipe>> cooking = recipes.getCookingPotRecipes().stream()
                .filter(holder -> publishedCookingRecipes.add(holder.id().identifier().toString()))
                .toList();

        List<RecipeHolder<CuttingBoardRecipe>> cutting = recipes.getCuttingBoardRecipes().stream()
                .filter(holder -> publishedCuttingRecipes.add(holder.id().identifier().toString()))
                .toList();

        if (!cooking.isEmpty()) {
            this.runtime.getRecipeManager().addRecipes(FDRecipeTypes.COOKING, cooking);
        }

        if (!cutting.isEmpty()) {
            this.runtime.getRecipeManager().addRecipes(FDRecipeTypes.CUTTING, cutting);
        }
    }

	@Override
	public Identifier getPluginUid() {
		return ID;
	}
}

