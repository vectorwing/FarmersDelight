package vectorwing.farmersdelight.data.builder;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CookingPotRecipeBuilder
{
	private CookingPotRecipeBookTab tab;
	private final List<Ingredient> ingredients = Lists.newArrayList();
	private final Item result;
	private final int count;
	private final int cookingTime;
	private final float experience;
	private final Item container;
	private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

	private CookingPotRecipeBuilder(ItemLike resultIn, int count, int cookingTime, float experience, @Nullable ItemLike container) {
		this.result = resultIn.asItem();
		this.count = count;
		this.cookingTime = cookingTime;
		this.experience = experience;
		this.container = container != null ? container.asItem() : null;
		this.tab = null;
	}

	public static CookingPotRecipeBuilder cookingPotRecipe(ItemLike mainResult, int count, int cookingTime, float experience) {
		return new CookingPotRecipeBuilder(mainResult, count, cookingTime, experience, null);
	}

	public static CookingPotRecipeBuilder cookingPotRecipe(ItemLike mainResult, int count, int cookingTime, float experience, ItemLike container) {
		return new CookingPotRecipeBuilder(mainResult, count, cookingTime, experience, container);
	}

	public CookingPotRecipeBuilder addIngredient(TagKey<Item> tagIn) {
		return addIngredient(Ingredient.of(tagIn));
	}

	public CookingPotRecipeBuilder addIngredient(ItemLike itemIn) {
		return addIngredient(itemIn, 1);
	}

	public CookingPotRecipeBuilder addIngredient(ItemLike itemIn, int quantity) {
		for (int i = 0; i < quantity; ++i) {
			addIngredient(Ingredient.of(itemIn));
		}
		return this;
	}

	public CookingPotRecipeBuilder addIngredient(Ingredient ingredientIn) {
		return addIngredient(ingredientIn, 1);
	}

	public CookingPotRecipeBuilder addIngredient(Ingredient ingredientIn, int quantity) {
		for (int i = 0; i < quantity; ++i) {
			ingredients.add(ingredientIn);
		}
		return this;
	}

	public CookingPotRecipeBuilder unlockedBy(String criterionName, Criterion<?> criterionTrigger) {
		advancement.addCriterion(criterionName, criterionTrigger);
		return this;
	}

	public CookingPotRecipeBuilder unlockedByItems(String criterionName, ItemLike... items) {
		return unlockedBy(criterionName, InventoryChangeTrigger.TriggerInstance.hasItems(items));
	}

	public CookingPotRecipeBuilder unlockedByAnyIngredient(ItemLike... items) {
		advancement.addCriterion("has_any_ingredient", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items).build()));
		return this;
	}

	public CookingPotRecipeBuilder setRecipeBookTab(CookingPotRecipeBookTab tab) {
		this.tab = tab;
		return this;
	}

	public void build(RecipeOutput outputIn) {
		ResourceLocation location = ForgeRegistries.ITEMS.getKey(result);
		build(outputIn, FarmersDelight.MODID + ":cooking/" + location.getPath());
	}

	public void build(RecipeOutput outputIn, String save) {
		ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(result);
		if ((new ResourceLocation(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Cooking Recipe " + save + " should remove its 'save' argument");
		} else {
			build(outputIn, new ResourceLocation(save));
		}
	}

	public void build(RecipeOutput outputIn, ResourceLocation id) {
		if (!advancement.criteria.buildOrThrow().isEmpty()) {
			advancement.parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
					.rewards(AdvancementRewards.Builder.recipe(id))
					.requirements(AdvancementRequirements.Strategy.OR);
			ResourceLocation advancementId = new ResourceLocation(id.getNamespace(), "recipes/" + id.getPath());
			outputIn.accept(new CookingPotRecipeBuilder.Result(id, result, count, ingredients, cookingTime, experience, container, tab, advancement.build(advancementId)));
		} else {
			outputIn.accept(new CookingPotRecipeBuilder.Result(id, result, count, ingredients, cookingTime, experience, container, tab));
		}
	}

	public static class Result implements FinishedRecipe
	{
		private final ResourceLocation id;
		private final CookingPotRecipeBookTab tab;
		private final List<Ingredient> ingredients;
		private final Item result;
		private final int count;
		private final int cookingTime;
		private final float experience;
		private final Item container;
		private final AdvancementHolder advancement;

		public Result(ResourceLocation idIn, Item resultIn, int countIn, List<Ingredient> ingredientsIn, int cookingTimeIn, float experienceIn, @Nullable Item containerIn, @Nullable CookingPotRecipeBookTab tabIn, @Nullable AdvancementHolder advancement) {
			this.id = idIn;
			this.tab = tabIn;
			this.ingredients = ingredientsIn;
			this.result = resultIn;
			this.count = countIn;
			this.cookingTime = cookingTimeIn;
			this.experience = experienceIn;
			this.container = containerIn;
			this.advancement = advancement;
		}

		public Result(ResourceLocation idIn, Item resultIn, int countIn, List<Ingredient> ingredientsIn, int cookingTimeIn, float experienceIn, @Nullable Item containerIn, @Nullable CookingPotRecipeBookTab tabIn) {
			this(idIn, resultIn, countIn, ingredientsIn, cookingTimeIn, experienceIn, containerIn, tabIn, null);
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			if (tab != null) {
				json.addProperty("recipe_book_tab", tab.toString());
			}

			JsonArray arrayIngredients = new JsonArray();

			for (Ingredient ingredient : ingredients) {
				arrayIngredients.add(ingredient.toJson(true));
			}
			json.add("ingredients", arrayIngredients);

			JsonObject objectResult = new JsonObject();
			objectResult.addProperty("item", ForgeRegistries.ITEMS.getKey(result).toString());
			if (count > 1) {
				objectResult.addProperty("count", count);
			}
			json.add("result", objectResult);

			if (container != null) {
				JsonObject objectContainer = new JsonObject();
				objectContainer.addProperty("item", ForgeRegistries.ITEMS.getKey(container).toString());
				json.add("container", objectContainer);
			}
			if (experience > 0) {
				json.addProperty("experience", experience);
			}
			json.addProperty("cookingtime", cookingTime);
		}

		@Override
		public JsonObject serializeRecipe() {
			return FinishedRecipe.super.serializeRecipe();
		}

		@Override
		public ResourceLocation id() {
			return id;
		}

		@Override
		public RecipeSerializer<?> type() {
			return ModRecipeSerializers.COOKING.get();
		}

		@Nullable
		@Override
		public AdvancementHolder advancement() {
			return advancement;
		}
	}
}
