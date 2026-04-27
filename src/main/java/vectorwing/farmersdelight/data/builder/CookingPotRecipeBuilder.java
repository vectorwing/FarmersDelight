package vectorwing.farmersdelight.data.builder;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CookingPotRecipeBuilder implements RecipeBuilder
{
	private CookingPotRecipeBookTab tab;
	private final List<Ingredient> ingredients = Lists.newArrayList();
	private final Item result;
	private final int count;
	private final int cookingTime;
	private final float experience;
	private final Item container;
	private final Advancement.Builder advancement = Advancement.Builder.advancement();
	@Nullable
	private String namespace;

	public CookingPotRecipeBuilder(ItemLike resultIn, int count, int cookingTime, float experience, @Nullable ItemLike container) {
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

	public CookingPotRecipeBuilder unlockedBy(String criterionName, CriterionTriggerInstance criterionTrigger) {
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

	/**
	 * Sets a custom namespace (mod ID) for the recipe. Use this only if the result isn't registered to the mod ID you want.
	 */
	public CookingPotRecipeBuilder setNamespace(String namespace) {
		this.namespace = namespace;
		return this;
	}

	@Override
	public CookingPotRecipeBuilder group(@Nullable String group) {
		return this; // no-op
	}

	@Override
	public Item getResult() {
		return this.result;
	}

	public static ResourceLocation getDefaultRecipeId(ItemLike itemLike) {
		return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemLike.asItem()));
	}

	/**
	 * Shorthand for saving recipes in the FD namespace.
	 */
	public void saveToFD(Consumer<FinishedRecipe> consumer) {
		this.setNamespace(FarmersDelight.MODID).save(consumer);
	}

	@Override
	public void save(Consumer<FinishedRecipe> consumer) {
		ResourceLocation defaultLocation = getDefaultRecipeId(result);
		save(consumer, new ResourceLocation(this.namespace != null ? namespace : defaultLocation.getNamespace(), defaultLocation.getPath())
				.withPrefix("cooking/"));
	}

	@Override
	public void save(Consumer<FinishedRecipe> consumerIn, ResourceLocation recipeId) {
		if (!advancement.getCriteria().isEmpty()) {
			advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
					.rewards(AdvancementRewards.Builder.recipe(recipeId))
					.requirements(RequirementsStrategy.OR);
			ResourceLocation advancementId = new ResourceLocation(recipeId.getNamespace(), "recipes/" + recipeId.getPath());
			consumerIn.accept(new CookingPotRecipeBuilder.Result(recipeId, result, count, ingredients, cookingTime, experience, container, tab, advancement, advancementId));
		} else {
			consumerIn.accept(new CookingPotRecipeBuilder.Result(recipeId, result, count, ingredients, cookingTime, experience, container, tab));
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
		private final Advancement.Builder advancement;
		private final ResourceLocation advancementId;

		public Result(ResourceLocation idIn, Item resultIn, int countIn, List<Ingredient> ingredientsIn, int cookingTimeIn, float experienceIn, @Nullable Item containerIn, @Nullable CookingPotRecipeBookTab tabIn, @Nullable Advancement.Builder advancement, @Nullable ResourceLocation advancementId) {
			this.id = idIn;
			this.tab = tabIn;
			this.ingredients = ingredientsIn;
			this.result = resultIn;
			this.count = countIn;
			this.cookingTime = cookingTimeIn;
			this.experience = experienceIn;
			this.container = containerIn;
			this.advancement = advancement;
			this.advancementId = advancementId;
		}

		public Result(ResourceLocation idIn, Item resultIn, int countIn, List<Ingredient> ingredientsIn, int cookingTimeIn, float experienceIn, @Nullable Item containerIn, @Nullable CookingPotRecipeBookTab tabIn) {
			this(idIn, resultIn, countIn, ingredientsIn, cookingTimeIn, experienceIn, containerIn, tabIn, null, null);
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			if (tab != null) {
				json.addProperty("recipe_book_tab", tab.toString());
			}

			JsonArray arrayIngredients = new JsonArray();

			for (Ingredient ingredient : ingredients) {
				arrayIngredients.add(ingredient.toJson());
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
		public ResourceLocation getId() {
			return id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return ModRecipeSerializers.COOKING.get();
		}

		@Nullable
		@Override
		public JsonObject serializeAdvancement() {
			return advancement != null ? advancement.serializeToJson() : null;
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementId() {
			return advancementId;
		}
	}

}
