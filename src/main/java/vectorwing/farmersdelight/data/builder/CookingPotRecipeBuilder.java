package vectorwing.farmersdelight.data.builder;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.triggers.RecipeUnlockedTrigger;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class CookingPotRecipeBuilder implements RecipeBuilder
{
	private CookingPotRecipeBookTab tab;
	private final NonNullList<Ingredient> ingredients = NonNullList.create();
	private final Item result;
	private final ItemStackTemplate resultStack;
	private final int cookingTime;
	private final float experience;
	private final Optional<ItemStackTemplate> container;
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
	@Nullable
	private String namespace;

	public CookingPotRecipeBuilder(ItemLike result, int count, int cookingTime, float experience, @Nullable ItemLike container) {
		this(result.asItem(), count, cookingTime, experience, container);
	}

	public CookingPotRecipeBuilder(ItemStack resultIn, int cookingTime, float experience, @Nullable ItemLike container) {
		this(resultIn.getItem(), resultIn.getCount(), cookingTime, experience, container);
	}

	public CookingPotRecipeBuilder(Item resultIn, int count, int cookingTime, float experience, @Nullable ItemLike container) {
		this.result = resultIn;
		this.resultStack = new ItemStackTemplate(resultIn, count);
		this.cookingTime = cookingTime;
		this.experience = experience;
		this.container = container != null ? Optional.of(new ItemStackTemplate(container.asItem())) : Optional.empty();
		this.tab = null;
	}

	public static CookingPotRecipeBuilder cookingPotRecipe(ItemLike mainResult, int count, int cookingTime, float experience) {
		return new CookingPotRecipeBuilder(mainResult, count, cookingTime, experience, null);
	}

	public static CookingPotRecipeBuilder cookingPotRecipe(ItemLike mainResult, int count, int cookingTime, float experience, ItemLike container) {
		return new CookingPotRecipeBuilder(mainResult, count, cookingTime, experience, container);
	}

	public CookingPotRecipeBuilder addIngredient(TagKey<Item> tagIn) {
		return addIngredient(Ingredient.of(HolderSet.emptyNamed(BuiltInRegistries.ITEM, tagIn)));
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
	public CookingPotRecipeBuilder unlockedBy(String criterionName, Criterion<?> criterionTrigger) {
		this.criteria.put(criterionName, criterionTrigger);
		return this;
	}

	public CookingPotRecipeBuilder unlockedByItems(String criterionName, ItemLike... items) {
		return unlockedBy(criterionName, InventoryChangeTrigger.TriggerInstance.hasItems(items));
	}

	public CookingPotRecipeBuilder unlockedByAnyIngredient(ItemLike... items) {
		this.criteria.put("has_any_ingredient", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, items).build()));
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
		save(output, ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(this.namespace != null ? namespace : defaultLocation.getNamespace(), defaultLocation.getPath()).withPrefix("cooking/")));
	}

	public void save(RecipeOutput output, Identifier id) {
		save(output, ResourceKey.create(Registries.RECIPE, id));
	}

//	public void build(RecipeOutput outputIn, String save) {
//		Identifier Identifier = BuiltInRegistries.ITEM.getKey(result);
//		if ((Identifier.parse(save)).equals(Identifier)) {
//			throw new IllegalStateException("Cooking Recipe " + save + " should remove its 'save' argument");
//		} else {
//			save(outputIn, Identifier.parse(save));
//		}
//	}

	@Override
	public ResourceKey<Recipe<?>> defaultId() {
		Identifier defaultLocation = getDefaultRecipeId(result);
		return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(this.namespace != null ? namespace : defaultLocation.getNamespace(), defaultLocation.getPath()).withPrefix("cooking/"));
	}

	@Override
	public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
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
				this.container,
				this.experience,
				this.cookingTime
		);
		output.accept(id, recipe, advancementBuilder.build(id.identifier().withPrefix("recipes/")));
	}
}
