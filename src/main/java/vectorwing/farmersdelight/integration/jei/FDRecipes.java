package vectorwing.farmersdelight.integration.jei;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.CraftingRecipe.CraftingBookInfo;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.utility.RecipeUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class FDRecipes
{
	private static final FileToIdConverter RECIPE_LISTER = FileToIdConverter.registry(Registries.RECIPE);
	private final ResourceManager resourceManager;
	private final Optional<HolderLookup.Provider> registries;

	public FDRecipes() {
		Minecraft minecraft = Minecraft.getInstance();
		ClientLevel level = minecraft.level;
		this.resourceManager = minecraft.getResourceManager();
		this.registries = level != null ? Optional.of(level.registryAccess()) : Optional.empty();
	}

	public List<RecipeHolder<CookingPotRecipe>> getCookingPotRecipes() {
		return getRecipes(ModRecipeTypes.COOKING.get(), CookingPotRecipe.class);
	}

	public List<RecipeHolder<CuttingBoardRecipe>> getCuttingBoardRecipes() {
		return getRecipes(ModRecipeTypes.CUTTING.get(), CuttingBoardRecipe.class);
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

	private <T extends Recipe<?>> List<RecipeHolder<T>> getRecipes(RecipeType<T> recipeType, Class<T> recipeClass) {
		if (this.registries.isEmpty()) {
			FarmersDelight.LOGGER.debug("Skipping JEI recipe population for {} before client registry access is available.", recipeType);
			return List.of();
		}

		List<RecipeHolder<T>> recipes = Lists.newArrayList();
		for (var entry : RECIPE_LISTER.listMatchingResourcesFromNamespace(this.resourceManager, FarmersDelight.MODID).entrySet()) {
			ResourceKey<Recipe<?>> id = ResourceKey.create(Registries.RECIPE, RECIPE_LISTER.fileToId(entry.getKey()));
			readRecipe(id, entry.getValue()).ifPresent(recipe -> {
				if (recipeClass.isInstance(recipe) && recipe.getType() == recipeType) {
					recipes.add(new RecipeHolder<>(id, recipeClass.cast(recipe)));
				}
			});
		}
		return recipes;
	}

	private Optional<Recipe<?>> readRecipe(ResourceKey<Recipe<?>> id, Resource resource) {
		try (var reader = resource.openAsReader()) {
			JsonElement json = JsonParser.parseReader(reader);
			return Recipe.CODEC.parse(this.registries.get().createSerializationContext(JsonOps.INSTANCE), json)
					.resultOrPartial(message -> FarmersDelight.LOGGER.debug("Skipping JEI recipe {}: {}", id.identifier(), message));
		} catch (IOException | RuntimeException ex) {
			FarmersDelight.LOGGER.debug("Skipping JEI recipe {} from {}.", id.identifier(), resource.sourcePackId(), ex);
			return Optional.empty();
		}
	}

	public void addValidatedSpecialRecipe(List<RecipeHolder<CraftingRecipe>> recipeList, String recipeId, String group, List<Ingredient> inputs, ItemLike output) {
		ResourceKey<Recipe<?>> id = ResourceKey.create(Registries.RECIPE, RecipeUtils.FDLocation(recipeId));
		if (hasRecipe(id)) {
			recipeList.add(new RecipeHolder<>(id, new ShapelessRecipe(
					new Recipe.CommonInfo(true),
					new CraftingBookInfo(CraftingBookCategory.MISC, group),
					ItemStackTemplate.fromNonEmptyStack(new ItemStack(output.asItem())),
					inputs)));
		}
	}

	private boolean hasRecipe(ResourceKey<Recipe<?>> id) {
		if (this.registries.isEmpty()) {
			return false;
		}
		Set<ResourceKey<Recipe<?>>> recipeIds = RECIPE_LISTER.listMatchingResourcesFromNamespace(this.resourceManager, FarmersDelight.MODID).keySet().stream()
				.map(RECIPE_LISTER::fileToId)
				.map(location -> ResourceKey.create(Registries.RECIPE, location))
				.collect(java.util.stream.Collectors.toSet());
		return recipeIds.contains(id);
	}
}
