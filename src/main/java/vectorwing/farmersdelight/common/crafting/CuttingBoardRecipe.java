package vectorwing.farmersdelight.common.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CuttingBoardRecipe implements Recipe<RecipeWrapper>
{
	public static RecipeType<CuttingBoardRecipe> TYPE = RecipeType.register(FarmersDelight.MODID + ":cutting");
	public static final CuttingBoardRecipe.Serializer SERIALIZER = new CuttingBoardRecipe.Serializer();
	public static final int MAX_RESULTS = 4;

	private final ResourceLocation id;
	private final String group;
	private final Ingredient input;
	private final Ingredient tool;
	private final NonNullList<ChanceResult> results;
	private final String soundEvent;

	public CuttingBoardRecipe(ResourceLocation id, String group, Ingredient input, Ingredient tool, NonNullList<ChanceResult> results, String soundEvent) {
		this.id = id;
		this.group = group;
		this.input = input;
		this.tool = tool;
		this.results = results;
		this.soundEvent = soundEvent;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> nonnulllist = NonNullList.create();
		nonnulllist.add(this.input);
		return nonnulllist;
	}

	public NonNullList<Ingredient> getIngredientsAndTool() {
		NonNullList<Ingredient> nonnulllist = NonNullList.create();
		nonnulllist.add(this.input);
		nonnulllist.add(this.tool);
		return nonnulllist;
	}

	public Ingredient getTool() {
		return this.tool;
	}

	@Override
	public ItemStack assemble(RecipeWrapper inv) {
		return this.results.get(0).getStack().copy();
	}

	@Override
	public ItemStack getResultItem() {
		return this.results.get(0).getStack();
	}

	public List<ItemStack> getResults() {
		return getRollableResults().stream()
				.map(ChanceResult::getStack)
				.collect(Collectors.toList());
	}

	public NonNullList<ChanceResult> getRollableResults() {
		return this.results;
	}

	public List<ItemStack> rollResults(Random rand, int fortuneLevel) {
		List<ItemStack> results = new ArrayList<>();
		NonNullList<ChanceResult> rollableResults = getRollableResults();
		for (ChanceResult output : rollableResults) {
			ItemStack stack = output.rollOutput(rand, fortuneLevel);
			if (!stack.isEmpty())
				results.add(stack);
		}
		return results;
	}

	public String getSoundEventID() {
		return this.soundEvent;
	}

	@Override
	public boolean matches(RecipeWrapper inv, Level worldIn) {
		if (inv.isEmpty())
			return false;
		return input.test(inv.getItem(0));
	}

	protected int getMaxInputCount() {
		return 1;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= this.getMaxInputCount();
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return CuttingBoardRecipe.SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return CuttingBoardRecipe.TYPE;
	}

	private static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<CuttingBoardRecipe>
	{
		Serializer() {
			this.setRegistryName(new ResourceLocation(FarmersDelight.MODID, "cutting"));
		}

		@Override
		public CuttingBoardRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			final String groupIn = GsonHelper.getAsString(json, "group", "");
			final NonNullList<Ingredient> inputItemsIn = readIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
			final JsonObject toolObject = GsonHelper.getAsJsonObject(json, "tool");
			final Ingredient toolIn = Ingredient.fromJson(toolObject);
			if (inputItemsIn.isEmpty()) {
				throw new JsonParseException("No ingredients for cutting recipe");
			} else if (toolIn.isEmpty()) {
				throw new JsonParseException("No tool for cutting recipe");
			} else if (inputItemsIn.size() > 1) {
				throw new JsonParseException("Too many ingredients for cutting recipe! Please define only one ingredient");
			} else {
				final NonNullList<ChanceResult> results = readResults(GsonHelper.getAsJsonArray(json, "result"));
				if (results.size() > 4) {
					throw new JsonParseException("Too many results for cutting recipe! The maximum quantity of unique results is " + MAX_RESULTS);
				} else {
					final String soundID = GsonHelper.getAsString(json, "sound", "");
					return new CuttingBoardRecipe(recipeId, groupIn, inputItemsIn.get(0), toolIn, results, soundID);
				}
			}
		}

		private static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
			NonNullList<Ingredient> nonnulllist = NonNullList.create();
			for (int i = 0; i < ingredientArray.size(); ++i) {
				Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i));
				if (!ingredient.isEmpty()) {
					nonnulllist.add(ingredient);
				}
			}
			return nonnulllist;
		}

		private static NonNullList<ChanceResult> readResults(JsonArray resultArray) {
			NonNullList<ChanceResult> results = NonNullList.create();
			for (JsonElement result : resultArray) {
				results.add(ChanceResult.deserialize(result));
			}
			return results;
		}

		@Nullable
		@Override
		public CuttingBoardRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			String groupIn = buffer.readUtf(32767);
			Ingredient inputItemIn = Ingredient.fromNetwork(buffer);
			Ingredient toolIn = Ingredient.fromNetwork(buffer);

			int i = buffer.readVarInt();
			NonNullList<ChanceResult> resultsIn = NonNullList.withSize(i, ChanceResult.EMPTY);
			for (int j = 0; j < resultsIn.size(); ++j) {
				resultsIn.set(j, ChanceResult.read(buffer));
			}
			String soundEventIn = buffer.readUtf();

			return new CuttingBoardRecipe(recipeId, groupIn, inputItemIn, toolIn, resultsIn, soundEventIn);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, CuttingBoardRecipe recipe) {
			buffer.writeUtf(recipe.group);
			recipe.input.toNetwork(buffer);
			recipe.tool.toNetwork(buffer);
			buffer.writeVarInt(recipe.results.size());
			for (ChanceResult result : recipe.results) {
				result.write(buffer);
			}
			buffer.writeUtf(recipe.soundEvent);
		}
	}
}
