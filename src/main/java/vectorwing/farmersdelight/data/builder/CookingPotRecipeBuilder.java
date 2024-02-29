package vectorwing.farmersdelight.data.builder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedHashMap;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CookingPotRecipeBuilder implements RecipeBuilder
{
	private CookingPotRecipeBookTab tab;
	private final NonNullList<Ingredient> ingredients = NonNullList.create();
	private final Item result;
	private final ItemStack resultStack;
	private final int cookingTime;
	private final float experience;
	private final Item container;
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

//	private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

	public CookingPotRecipeBuilder(ItemLike result, int count, int cookingTime, float experience, @Nullable ItemLike container) {
		this(new ItemStack(result, count), cookingTime, experience, container);
	}

	public CookingPotRecipeBuilder(ItemStack resultIn, int cookingTime, float experience, @Nullable ItemLike container) {
		this.result = resultIn.getItem();
		this.resultStack = resultIn;
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

	@Override
	public RecipeBuilder group(@org.jetbrains.annotations.Nullable String p_176495_) {
		return this;
	}

	public CookingPotRecipeBuilder setRecipeBookTab(CookingPotRecipeBookTab tab) {
		this.tab = tab;
		return this;
	}

	@Override
	public Item getResult() {
		return this.result;
	}

	@Override
	public CookingPotRecipeBuilder unlockedBy(String criterionName, Criterion<?> criterionTrigger) {
		this.criteria.put(criterionName, criterionTrigger);
		return this;
	}

	public CookingPotRecipeBuilder unlockedByItems(String criterionName, ItemLike... items) {
		return unlockedBy(criterionName, InventoryChangeTrigger.TriggerInstance.hasItems(items));
	}

	public CookingPotRecipeBuilder unlockedByAnyIngredient(ItemLike... items) {
		this.criteria.put("has_any_ingredient", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items).build()));
		return this;
	}

	public void build(RecipeOutput output) {
		ResourceLocation location = BuiltInRegistries.ITEM.getKey(result);
		save(output, new ResourceLocation(FarmersDelight.MODID, location.getPath()));
	}

	public void build(RecipeOutput outputIn, String save) {
		ResourceLocation resourcelocation = BuiltInRegistries.ITEM.getKey(result);
		if ((new ResourceLocation(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Cooking Recipe " + save + " should remove its 'save' argument");
		} else {
			save(outputIn, new ResourceLocation(save));
		}
	}

//	public void build(RecipeOutput outputIn, ResourceLocation id) {
//		if (!advancement.criteria.buildOrThrow().isEmpty()) {
//			advancement.parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
//					.rewards(AdvancementRewards.Builder.recipe(id))
//					.requirements(AdvancementRequirements.Strategy.OR);
//			ResourceLocation advancementId = new ResourceLocation(id.getNamespace(), "recipes/" + id.getPath());
//			outputIn.accept(new CookingPotRecipeBuilder.Result(id, result, count, ingredients, cookingTime, experience, container, tab, advancement.build(advancementId)));
//		} else {
//			outputIn.accept(new CookingPotRecipeBuilder.Result(id, result, count, ingredients, cookingTime, experience, container, tab));
//		}
//	}

	@Override
	public void save(RecipeOutput output, ResourceLocation id) {
		Advancement.Builder advancementBuilder = output.advancement()
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
				.rewards(AdvancementRewards.Builder.recipe(id))
				.requirements(AdvancementRequirements.Strategy.OR);
		this.criteria.forEach(advancementBuilder::addCriterion);
		CookingPotRecipe recipe = new CookingPotRecipe(
				"",
				this.tab,
				this.ingredients,
				this.resultStack,
				new ItemStack(this.container),
				this.experience,
				this.cookingTime
		);
		output.accept(id.withPrefix("cooking/"), recipe, advancementBuilder.build(id.withPrefix("recipes/cooking/")));
	}

//	public static class Result implements FinishedRecipe
//	{
//		private final ResourceLocation id;
//		private final CookingPotRecipeBookTab tab;
//		private final List<Ingredient> ingredients;
//		private final Item result;
//		private final int count;
//		private final int cookingTime;
//		private final float experience;
//		private final Item container;
//		private final AdvancementHolder advancement;
//
//		public Result(ResourceLocation idIn, Item resultIn, int countIn, List<Ingredient> ingredientsIn, int cookingTimeIn, float experienceIn, @Nullable Item containerIn, @Nullable CookingPotRecipeBookTab tabIn, @Nullable AdvancementHolder advancement) {
//			this.id = idIn;
//			this.tab = tabIn;
//			this.ingredients = ingredientsIn;
//			this.result = resultIn;
//			this.count = countIn;
//			this.cookingTime = cookingTimeIn;
//			this.experience = experienceIn;
//			this.container = containerIn;
//			this.advancement = advancement;
//		}
//
//		public Result(ResourceLocation idIn, Item resultIn, int countIn, List<Ingredient> ingredientsIn, int cookingTimeIn, float experienceIn, @Nullable Item containerIn, @Nullable CookingPotRecipeBookTab tabIn) {
//			this(idIn, resultIn, countIn, ingredientsIn, cookingTimeIn, experienceIn, containerIn, tabIn, null);
//		}
//
//		@Override
//		public void serializeRecipeData(JsonObject json) {
//			if (tab != null) {
//				json.addProperty("recipe_book_tab", tab.toString());
//			}
//
//			JsonArray arrayIngredients = new JsonArray();
//
//			for (Ingredient ingredient : ingredients) {
//				arrayIngredients.add(ingredient.toJson(true));
//			}
//			json.add("ingredients", arrayIngredients);
//
//			JsonObject objectResult = new JsonObject();
//			objectResult.addProperty("item", BuiltInRegistries.ITEM.getKey(result).toString());
//			if (count > 1) {
//				objectResult.addProperty("count", count);
//			}
//			json.add("result", objectResult);
//
//			if (container != null) {
//				JsonObject objectContainer = new JsonObject();
//				objectContainer.addProperty("item", BuiltInRegistries.ITEM.getKey(container).toString());
//				json.add("container", objectContainer);
//			}
//			if (experience > 0) {
//				json.addProperty("experience", experience);
//			}
//			json.addProperty("cookingtime", cookingTime);
//		}
//
//		@Override
//		public JsonObject serializeRecipe() {
//			return FinishedRecipe.super.serializeRecipe();
//		}
//
//		@Override
//		public ResourceLocation id() {
//			return id;
//		}
//
//		@Override
//		public RecipeSerializer<?> type() {
//			return ModRecipeSerializers.COOKING.get();
//		}
//
//		@Nullable
//		@Override
//		public AdvancementHolder advancement() {
//			return advancement;
//		}
//	}
}
