package vectorwing.farmersdelight.data.builder;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mezz.jei.api.MethodsReturnNonnullByDefault;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.crafting.CookingPotRecipe;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CookingPotRecipeBuilder
{
	public static final int DEFAULT_COOKING_TIME = 200;

	private final Item result;
	private final int count;
	private final List<Ingredient> ingredients = Lists.newArrayList();
	private final Item container;
	private final int cookingTime;

	private CookingPotRecipeBuilder(IItemProvider resultIn, int count, @Nullable IItemProvider container, int cookingTime) {
		this.result = resultIn.asItem();
		this.count = count;
		this.container = container != null ? container.asItem() : null;
		this.cookingTime = cookingTime;
	}

	public static CookingPotRecipeBuilder cookingRecipe(IItemProvider mainResult, int count) {
		return new CookingPotRecipeBuilder(mainResult, count, null, DEFAULT_COOKING_TIME);
	}

	public static CookingPotRecipeBuilder cookingRecipe(IItemProvider mainResult, int count, IItemProvider container) {
		return new CookingPotRecipeBuilder(mainResult, count, container, DEFAULT_COOKING_TIME);
	}

	public static CookingPotRecipeBuilder cookingRecipe(IItemProvider mainResult, int count, int cookingTime) {
		return new CookingPotRecipeBuilder(mainResult, count, null, cookingTime);
	}

	public static CookingPotRecipeBuilder cookingRecipe(IItemProvider mainResult, int count, IItemProvider container, int cookingTime) {
		return new CookingPotRecipeBuilder(mainResult, count, container, cookingTime);
	}

	public CookingPotRecipeBuilder addIngredient(ITag<Item> tagIn) {
		return this.addIngredient(Ingredient.fromTag(tagIn));
	}

	public CookingPotRecipeBuilder addIngredient(IItemProvider itemIn) {
		return this.addIngredient(itemIn, 1);
	}

	public CookingPotRecipeBuilder addIngredient(IItemProvider itemIn, int quantity) {
		for (int i = 0; i < quantity; ++i) {
			this.addIngredient(Ingredient.fromItems(itemIn));
		}
		return this;
	}

	public CookingPotRecipeBuilder addIngredient(Ingredient ingredientIn) {
		return this.addIngredient(ingredientIn, 1);
	}

	public CookingPotRecipeBuilder addIngredient(Ingredient ingredientIn, int quantity) {
		for (int i = 0; i < quantity; ++i) {
			this.ingredients.add(ingredientIn);
		}
		return this;
	}

	public void build(Consumer<IFinishedRecipe> consumerIn) {
		ResourceLocation location = ForgeRegistries.ITEMS.getKey(this.result);
		this.build(consumerIn, FarmersDelight.MODID + ":cooking/" + location.getPath());
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
		ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
		if ((new ResourceLocation(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Cooking Recipe " + save + " should remove its 'save' argument");
		} else {
			this.build(consumerIn, new ResourceLocation(save));
		}
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
		consumerIn.accept(new CookingPotRecipeBuilder.Result(id, this.result, this.count, this.ingredients, this.container, this.cookingTime));
	}

	public static class Result implements IFinishedRecipe
	{
		private final ResourceLocation id;
		private final List<Ingredient> ingredients;
		private final Item result;
		private final int count;
		private final Item container;
		private final int cookingTime;

		public Result(ResourceLocation idIn, Item resultIn, int countIn, List<Ingredient> ingredientsIn, @Nullable Item containerIn, int cookingTimeIn) {
			this.id = idIn;
			this.result = resultIn;
			this.count = countIn;
			this.ingredients = ingredientsIn;
			this.container = containerIn;
			this.cookingTime = cookingTimeIn;
		}

		@Override
		public void serialize(JsonObject json) {
			JsonArray arrayIngredients = new JsonArray();

			for (Ingredient ingredient : this.ingredients) {
				arrayIngredients.add(ingredient.serialize());
			}
			json.add("ingredients", arrayIngredients);

			JsonObject objectResult = new JsonObject();
			objectResult.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result).toString());
			if (this.count > 1) {
				objectResult.addProperty("count", this.count);
			}
			json.add("result", objectResult);

			if (this.container != null) {
				JsonObject objectContainer = new JsonObject();
				objectContainer.addProperty("item", ForgeRegistries.ITEMS.getKey(this.container).toString());
				json.add("container", objectContainer);
			}
			json.addProperty("cookingtime", this.cookingTime);
		}

		@Override
		public ResourceLocation getID() {
			return this.id;
		}

		@Override
		public IRecipeSerializer<?> getSerializer() {
			return CookingPotRecipe.SERIALIZER;
		}

		@Nullable
		@Override
		public JsonObject getAdvancementJson() {
			return null;
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementID() {
			return null;
		}
	}
}
