package vectorwing.farmersdelight.data.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.crafting.CuttingBoardRecipe;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CuttingBoardRecipeBuilder {
	private final Map<Item, Integer> results = new LinkedHashMap<Item, Integer>(4);
	private final Ingredient ingredient;
	private final Ingredient tool;
	private String soundEventID;

	public CuttingBoardRecipeBuilder(Ingredient ingredient, Ingredient tool, IItemProvider mainResult, int count) {
		this.results.put(mainResult.asItem(), count);
		this.ingredient = ingredient;
		this.tool = tool;
	}

	/**
	 * Creates a new builder for a cutting recipe.
	 */
	public static CuttingBoardRecipeBuilder cuttingRecipe(Ingredient ingredient, Ingredient tool, IItemProvider mainResult, int count) {
		return new CuttingBoardRecipeBuilder(ingredient, tool, mainResult, count);
	}

	/**
	 * Creates a new builder for a cutting recipe, returning 1 unit of the result.
	 */
	public static CuttingBoardRecipeBuilder cuttingRecipe(Ingredient ingredient, Ingredient tool, IItemProvider mainResult) {
		return new CuttingBoardRecipeBuilder(ingredient, tool, mainResult, 1);
	}

	public CuttingBoardRecipeBuilder addResult(IItemProvider result) {
		return this.addResult(result, 1);
	}

	public CuttingBoardRecipeBuilder addResult(IItemProvider result, int count) {
		this.results.put(result.asItem(), count);
		return this;
	}

	public CuttingBoardRecipeBuilder addSound(String soundEventID) {
		this.soundEventID = soundEventID;
		return this;
	}

	public void build(Consumer<IFinishedRecipe> consumerIn) {
		ResourceLocation location = Registry.ITEM.getKey(this.ingredient.getMatchingStacks()[0].getItem());
		this.build(consumerIn, FarmersDelight.MODID + ":cutting/cutting_" + location.getPath());
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
		ResourceLocation resourcelocation = Registry.ITEM.getKey(this.ingredient.getMatchingStacks()[0].getItem());
		if ((new ResourceLocation(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Shapeless Recipe " + save + " should remove its 'save' argument");
		} else {
			this.build(consumerIn, new ResourceLocation(save));
		}
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
		consumerIn.accept(new CuttingBoardRecipeBuilder.Result(id, this.ingredient, this.tool, this.results, this.soundEventID == null ? "" : this.soundEventID));
	}

	public static class Result implements IFinishedRecipe {
		private final ResourceLocation id;
		private final Ingredient ingredient;
		private final Ingredient tool;
		private final Map<Item, Integer> results;
		private String soundEventID;

		public Result(ResourceLocation idIn, Ingredient ingredientIn, Ingredient toolIn, Map<Item, Integer> resultsIn, String soundEventIDIn) {
			this.id = idIn;
			this.ingredient = ingredientIn;
			this.tool = toolIn;
			this.results = resultsIn;
			this.soundEventID = soundEventIDIn;
		}

		@Override
		public void serialize(JsonObject json) {
			// TODO: Consider adapting whether it's an array or object on your CuttingBoardRecipe later.
			JsonArray arrayIngredients = new JsonArray();
			arrayIngredients.add(this.ingredient.serialize());
			json.add("ingredients", arrayIngredients);

			json.add("tool", this.tool.serialize());

			JsonArray arrayResults = new JsonArray();
			for(Map.Entry<Item, Integer> result : this.results.entrySet()) {
				JsonObject jsonobject = new JsonObject();
				jsonobject.addProperty("item", Registry.ITEM.getKey(result.getKey()).toString());
				if (result.getValue() > 1) {
					jsonobject.addProperty("count", result.getValue());
				}
				arrayResults.add(jsonobject);
			}
			json.add("result", arrayResults);
			if (!this.soundEventID.isEmpty()) {
				json.addProperty("sound", this.soundEventID);
			}
		}

		@Override
		public ResourceLocation getID() {
			return this.id;
		}

		@Override
		public IRecipeSerializer<?> getSerializer() {
			return CuttingBoardRecipe.SERIALIZER;
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
