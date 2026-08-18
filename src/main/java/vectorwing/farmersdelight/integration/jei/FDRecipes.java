package vectorwing.farmersdelight.integration.jei;

import com.google.common.collect.Lists;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.utility.RecipeUtils;

import java.util.List;
import java.util.Optional;

@EventBusSubscriber
public class FDRecipes
{
	private @Nullable RecipeMap recipeMap;

	public FDRecipes() {
		if (recipeMap == null) {
			throw new NullPointerException("Minecraft level must not be null.");
		}
	}

	@SubscribeEvent
	public void receiveRecipes(RecipesReceivedEvent event) {
		this.recipeMap = event.getRecipeMap();
	}

	public List<RecipeHolder<CookingPotRecipe>> getCookingPotRecipes() {
		return recipeMap.byType(ModRecipeTypes.COOKING.get()).stream().toList();
	}

	public List<RecipeHolder<CuttingBoardRecipe>> getCuttingBoardRecipes() {
		return recipeMap.byType(ModRecipeTypes.CUTTING.get()).stream().toList();
	}

	public List<RecipeHolder<CraftingRecipe>> getSpecialCraftingRecipes() {
		List<RecipeHolder<CraftingRecipe>> recipes = Lists.newArrayList();

		addValidatedSpecialRecipe(recipes, "wheat_dough_from_water", "fd_dough",
				NonNullList.of(
						Ingredient.of(),
						Ingredient.of(Items.WHEAT),
						Ingredient.of(Items.WATER_BUCKET)
				),
				ModItems.WHEAT_DOUGH.get()
		);

		return recipes;
	}

	public void addValidatedSpecialRecipe(List<RecipeHolder<CraftingRecipe>> recipeList, String recipeId, String group, NonNullList<Ingredient> inputs, ItemLike output) {
		Optional<RecipeHolder<?>> specialRecipe = Optional.ofNullable(recipeMap.byKey(ResourceKey.create(Registries.RECIPE, RecipeUtils.FDLocation(recipeId))));

		specialRecipe.ifPresent((recipe) -> {
			ShapelessRecipe shapeless = new ShapelessRecipe(
				new Recipe.CommonInfo(false),
				new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, group),
				ItemStackTemplate.fromNonEmptyStack(new ItemStack(output)),
				inputs
			);
			recipeList.add(new RecipeHolder<>(specialRecipe.get().id(), shapeless));
		});
	}
}
