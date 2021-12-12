package vectorwing.farmersdelight.data.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mezz.jei.api.MethodsReturnNonnullByDefault;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CuttingBoardRecipeBuilder
{
	private final List<ChanceResult> results = new ArrayList<>(4);
	private final Ingredient ingredient;
	private final Ingredient tool;
	private String soundEventID;

	private CuttingBoardRecipeBuilder(Ingredient ingredient, Ingredient tool, ItemLike mainResult, int count, float chance) {
		this.results.add(new ChanceResult(new ItemStack(mainResult.asItem(), count), chance));
		this.ingredient = ingredient;
		this.tool = tool;
	}

	/**
	 * Creates a new builder for a cutting recipe.
	 */
	public static CuttingBoardRecipeBuilder cuttingRecipe(Ingredient ingredient, Ingredient tool, ItemLike mainResult, int count) {
		return new CuttingBoardRecipeBuilder(ingredient, tool, mainResult, count, 1);
	}

	/**
	 * Creates a new builder for a cutting recipe, providing a chance for the main output to drop.
	 */
	public static CuttingBoardRecipeBuilder cuttingRecipe(Ingredient ingredient, Ingredient tool, ItemLike mainResult, int count, int chance) {
		return new CuttingBoardRecipeBuilder(ingredient, tool, mainResult, count, chance);
	}

	/**
	 * Creates a new builder for a cutting recipe, returning 1 unit of the result.
	 */
	public static CuttingBoardRecipeBuilder cuttingRecipe(Ingredient ingredient, Ingredient tool, ItemLike mainResult) {
		return new CuttingBoardRecipeBuilder(ingredient, tool, mainResult, 1, 1);
	}

	public CuttingBoardRecipeBuilder addResult(ItemLike result) {
		return this.addResult(result, 1);
	}

	public CuttingBoardRecipeBuilder addResult(ItemLike result, int count) {
		this.results.add(new ChanceResult(new ItemStack(result.asItem(), count), 1));
		return this;
	}

	public CuttingBoardRecipeBuilder addResultWithChance(ItemLike result, float chance) {
		return this.addResultWithChance(result, chance, 1);
	}

	public CuttingBoardRecipeBuilder addResultWithChance(ItemLike result, float chance, int count) {
		this.results.add(new ChanceResult(new ItemStack(result.asItem(), count), chance));
		return this;
	}

	public CuttingBoardRecipeBuilder addSound(String soundEventID) {
		this.soundEventID = soundEventID;
		return this;
	}

	public void build(Consumer<FinishedRecipe> consumerIn) {
		ResourceLocation location = ForgeRegistries.ITEMS.getKey(this.ingredient.getItems()[0].getItem());
		this.build(consumerIn, FarmersDelight.MODID + ":cutting/" + location.getPath());
	}

	public void build(Consumer<FinishedRecipe> consumerIn, String save) {
		ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.ingredient.getItems()[0].getItem());
		if ((new ResourceLocation(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Cutting Recipe " + save + " should remove its 'save' argument");
		} else {
			this.build(consumerIn, new ResourceLocation(save));
		}
	}

	public void build(Consumer<FinishedRecipe> consumerIn, ResourceLocation id) {
		consumerIn.accept(new CuttingBoardRecipeBuilder.Result(id, this.ingredient, this.tool, this.results, this.soundEventID == null ? "" : this.soundEventID));
	}

	public static class Result implements FinishedRecipe
	{
		private final ResourceLocation id;
		private final Ingredient ingredient;
		private final Ingredient tool;
		private final List<ChanceResult> results;
		private final String soundEventID;

		public Result(ResourceLocation idIn, Ingredient ingredientIn, Ingredient toolIn, List<ChanceResult> resultsIn, String soundEventIDIn) {
			this.id = idIn;
			this.ingredient = ingredientIn;
			this.tool = toolIn;
			this.results = resultsIn;
			this.soundEventID = soundEventIDIn;
		}

		@Override
		public void serializeRecipeData(JsonObject json) {
			JsonArray arrayIngredients = new JsonArray();
			arrayIngredients.add(this.ingredient.toJson());
			json.add("ingredients", arrayIngredients);

			json.add("tool", this.tool.toJson());

			JsonArray arrayResults = new JsonArray();
			for (ChanceResult result : this.results) {
				JsonObject jsonobject = new JsonObject();
				jsonobject.addProperty("item", ForgeRegistries.ITEMS.getKey(result.getStack().getItem()).toString());
				if (result.getStack().getCount() > 1) {
					jsonobject.addProperty("count", result.getStack().getCount());
				}
				if (result.getChance() < 1) {
					jsonobject.addProperty("chance", result.getChance());
				}
				arrayResults.add(jsonobject);
			}
			json.add("result", arrayResults);
			if (!this.soundEventID.isEmpty()) {
				json.addProperty("sound", this.soundEventID);
			}
		}

		@Override
		public ResourceLocation getId() {
			return this.id;
		}

		@Override
		public RecipeSerializer<?> getType() {
			return CuttingBoardRecipe.SERIALIZER;
		}

		@Nullable
		@Override
		public JsonObject serializeAdvancement() {
			return null;
		}

		@Nullable
		@Override
		public ResourceLocation getAdvancementId() {
			return null;
		}
	}
}
