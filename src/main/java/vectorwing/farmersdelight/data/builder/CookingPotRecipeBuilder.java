package vectorwing.farmersdelight.data.builder;

import com.mojang.logging.annotations.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@MethodsReturnNonnullByDefault
public class CookingPotRecipeBuilder implements RecipeBuilder
{
	private final HolderGetter<Item> items;
	private CookingPotRecipeBookTab category;
	private final NonNullList<Ingredient> ingredients = NonNullList.create();
	private final Item result;
	private final ItemStackTemplate resultStack;
	private final int cookingTime;
	private final float experience;
	private final ItemStackTemplate container;
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
	@Nullable
	private String namespace;

	public CookingPotRecipeBuilder(HolderGetter<Item> items, ItemLike result, int count, int cookingTime, float experience, @Nullable ItemLike container) {
		this(items, new ItemStackTemplate(result.asItem(), count), cookingTime, experience, container);
	}

	public CookingPotRecipeBuilder(HolderGetter<Item> items, ItemStackTemplate result, int cookingTime, float experience, @Nullable ItemLike container) {
		this.items = items;
		this.result = result.item().value();
		this.resultStack = result;
		this.cookingTime = cookingTime;
		this.experience = experience;
		this.container = container != null ? new ItemStackTemplate(container.asItem()) : null;
		this.category = null;
	}

	public static CookingPotRecipeBuilder cookingPotRecipe(HolderGetter<Item> items, ItemLike mainResult, int count, int cookingTime, float experience) {
		return new CookingPotRecipeBuilder(items, mainResult, count, cookingTime, experience, null);
	}

	public static CookingPotRecipeBuilder cookingPotRecipe(HolderGetter<Item> items, ItemLike mainResult, int count, int cookingTime, float experience, ItemLike container) {
		return new CookingPotRecipeBuilder(items, mainResult, count, cookingTime, experience, container);
	}

	public CookingPotRecipeBuilder addIngredient(TagKey<Item> tag) {
		return addIngredient(Ingredient.of(items.getOrThrow(tag)));
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
	public RecipeBuilder group(@Nullable String p_176495_) {
		return this;
	}

	@Override
	public ResourceKey<Recipe<?>> defaultId() {
		return RecipeBuilder.getDefaultRecipeId(resultStack);
	}

	public CookingPotRecipeBuilder setRecipeBookCategory(CookingPotRecipeBookTab category) {
		this.category = category;
		return this;
	}

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

	public CookingPotRecipeBuilder unlockedByAnyIngredient(ItemLike... itemCollection) {
		this.criteria.put("has_any_ingredient", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(items, itemCollection).build()));
		return this;
	}

	/**
	 * Sets a custom namespace (mod ID) for the recipe. Use this only if the result isn't registered to the mod ID you want.
	 */
	public CookingPotRecipeBuilder setNamespace(String namespace) {
		this.namespace = namespace;
		return this;
	}

	public static Identifier getDefaultRecipeId(ItemLike itemLike) {
		return Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(itemLike.asItem()));
	}

	/**
	 * Shorthand for saving recipes in the FD namespace.
	 */
	public void saveToFD(RecipeOutput output) {
		this.setNamespace(FarmersDelight.MODID).save(output);
	}

	public void save(RecipeOutput output) {
		Identifier defaultLocation = getDefaultRecipeId(result);
		save(output, Identifier.fromNamespaceAndPath(this.namespace != null ? namespace : defaultLocation.getNamespace(), defaultLocation.getPath()).withPrefix("cooking/"));
	}

	public void build(RecipeOutput outputIn, String save) {
		Identifier resourcelocation = BuiltInRegistries.ITEM.getKey(result);
		if ((Identifier.parse(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Cooking Recipe " + save + " should remove its 'save' argument");
		} else {
			save(outputIn, Identifier.parse(save));
		}
	}

	public void build(RecipeOutput output, Identifier id) {
		save(output, ResourceKey.create(Registries.RECIPE, id));
	}


	public void save(RecipeOutput output, Identifier id) {
		build(output, id);
	}

	@Override
	public void save(RecipeOutput output, ResourceKey<Recipe<?>> resourceKey) {
		Advancement.Builder advancementBuilder = output.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceKey))
			.rewards(AdvancementRewards.Builder.recipe(resourceKey))
			.requirements(AdvancementRequirements.Strategy.OR);
		this.criteria.forEach(advancementBuilder::addCriterion);
		CookingPotRecipe recipe = new CookingPotRecipe(
			"",
			this.category,
			this.ingredients,
			this.resultStack,
			Optional.ofNullable(this.container),
			this.experience,
			this.cookingTime
		);
		output.accept(resourceKey, recipe, advancementBuilder.build(resourceKey.identifier().withPrefix("recipes/")));
	}
}