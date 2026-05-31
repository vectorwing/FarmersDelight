package vectorwing.farmersdelight.integration.jei;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.utility.RecipeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Feeds Farmer's Delight recipes to JEI.
 *
 * <p>NOTE (26.1 port): the client no longer holds full {@link Recipe} objects. {@code ClientLevel#recipeAccess()}
 * returns a {@link net.minecraft.world.item.crafting.RecipeAccess}, which only exposes ingredient property-sets and
 * stonecutter recipes; the client otherwise receives {@code RecipeDisplay}s for the recipe book, not the recipes
 * themselves. JEI 29.x likewise does not enumerate a mod's recipes for it, so Farmer's Delight has to source them.
 *
 * <p>The only place a full {@link RecipeManager} (and therefore every recipe of a given type) is available on the
 * client is the integrated server of a single-player session, reached via
 * {@code Minecraft#getSingleplayerServer()} -> {@code MinecraftServer#getRecipeManager()}. We read the recipes from
 * its {@link net.minecraft.world.item.crafting.RecipeMap} ({@code RecipeManager#recipeMap().byType(type)}).
 *
 * <p><b>Limitation:</b> on a dedicated server (multiplayer) {@code getSingleplayerServer()} is {@code null}, so no
 * integrated {@link RecipeManager} exists client-side and these lookups return empty. In that situation JEI shows the
 * Farmer's Delight categories/catalysts but no Cooking Pot / Cutting Board recipes. There is no client-side recipe
 * enumeration in 26.1 to work around this; it would require a dedicated network sync, which is out of scope here.
 */
public class FDRecipes
{
	private static final Logger LOGGER = LogUtils.getLogger();

	@Nullable
	private final RecipeManager recipeManager;

	public FDRecipes() {
		this.recipeManager = getServerRecipeManager();
		if (this.recipeManager == null) {
			LOGGER.info("Farmer's Delight JEI integration: no integrated server recipe manager available " +
					"(multiplayer or no world loaded); Cooking Pot / Cutting Board recipes will not be shown.");
		}
	}

	@Nullable
	private static RecipeManager getServerRecipeManager() {
		IntegratedServer server = Minecraft.getInstance().getSingleplayerServer();
		return server != null ? server.getRecipeManager() : null;
	}

	private <I extends net.minecraft.world.item.crafting.RecipeInput, T extends Recipe<I>> List<RecipeHolder<T>> getRecipesFor(RecipeType<T> type) {
		if (recipeManager == null) {
			return List.of();
		}
		return new ArrayList<>(recipeManager.recipeMap().byType(type));
	}

	public List<RecipeHolder<CookingPotRecipe>> getCookingPotRecipes() {
		return getRecipesFor(ModRecipeTypes.COOKING.get());
	}

	public List<RecipeHolder<CuttingBoardRecipe>> getCuttingBoardRecipes() {
		return getRecipesFor(ModRecipeTypes.CUTTING.get());
	}

	public List<RecipeHolder<CraftingRecipe>> getSpecialCraftingRecipes() {
		List<RecipeHolder<CraftingRecipe>> recipes = Lists.newArrayList();

		addValidatedSpecialRecipe(recipes, "wheat_dough_from_water", "fd_dough",
				List.of(
						Ingredient.of(Items.WHEAT),
						Ingredient.of(Items.WATER_BUCKET)
				),
				ModItems.WHEAT_DOUGH.get()
		);

		return recipes;
	}

	public void addValidatedSpecialRecipe(List<RecipeHolder<CraftingRecipe>> recipeList, String recipeId, String group, List<Ingredient> inputs, ItemLike output) {
		if (recipeManager == null) {
			return;
		}
		recipeManager.byKey(RecipeUtils.FDKey(recipeId)).ifPresent(found -> {
			Item resultItem = output.asItem();
			ShapelessRecipe shapeless = new ShapelessRecipe(
					new Recipe.CommonInfo(true),
					new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, group),
					new ItemStackTemplate(resultItem),
					inputs);
			recipeList.add(new RecipeHolder<CraftingRecipe>(found.id(), shapeless));
		});
	}
}
