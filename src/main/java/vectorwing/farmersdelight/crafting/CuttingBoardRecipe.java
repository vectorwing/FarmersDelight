package vectorwing.farmersdelight.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.crafting.ingredients.ChanceResult;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CuttingBoardRecipe implements IRecipe<RecipeWrapper>
{
	public static IRecipeType<CuttingBoardRecipe> TYPE = IRecipeType.register(FarmersDelight.MODID + ":cutting");
	public static final CuttingBoardRecipe.Serializer SERIALIZER = new CuttingBoardRecipe.Serializer();

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
	public ItemStack getCraftingResult(RecipeWrapper inv) {
		return this.results.get(0).getStack().copy();
	}

	@Override
	public ItemStack getRecipeOutput() {
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

	public List<ItemStack> rollResults(Random rand) {
		List<ItemStack> results = new ArrayList<>();
		NonNullList<ChanceResult> rollableResults = getRollableResults();
		for (ChanceResult output : rollableResults) {
			ItemStack stack = output.rollOutput(rand);
			if (!stack.isEmpty())
				results.add(stack);
		}
		return results;
	}

	public String getSoundEventID() {
		return this.soundEvent;
	}

	@Override
	public boolean matches(RecipeWrapper inv, World worldIn) {
		if (inv.isEmpty())
			return false;
		return input.test(inv.getStackInSlot(0));
	}

	protected int getMaxInputCount() {
		return 1;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= this.getMaxInputCount();
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return CuttingBoardRecipe.SERIALIZER;
	}

	@Override
	public IRecipeType<?> getType() {
		return CuttingBoardRecipe.TYPE;
	}

	private static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CuttingBoardRecipe>
	{
		Serializer() {
			this.setRegistryName(new ResourceLocation(FarmersDelight.MODID, "cutting"));
		}

		@Override
		public CuttingBoardRecipe read(ResourceLocation recipeId, JsonObject json) {
			final String groupIn = JSONUtils.getString(json, "group", "");
			final NonNullList<Ingredient> inputItemsIn = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
			final JsonObject toolObject= JSONUtils.getJsonObject(json, "tool");
			final Ingredient toolIn = Ingredient.deserialize(toolObject);
			if (inputItemsIn.isEmpty()) {
				throw new JsonParseException("No ingredients for cutting recipe");
			} else if (toolIn.hasNoMatchingItems()) {
				throw new JsonParseException("No tool for cutting recipe");
			} else if (inputItemsIn.size() > 1) {
				throw new JsonParseException("Too many ingredients for cutting recipe! Please define only one ingredient.");
			} else {
				final NonNullList<ChanceResult> results = readResults(JSONUtils.getJsonArray(json, "result"));
				final String soundID = JSONUtils.getString(json, "sound", "");
				return new CuttingBoardRecipe(recipeId, groupIn, inputItemsIn.get(0), toolIn, results, soundID);
			}
		}

		private static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
			NonNullList<Ingredient> nonnulllist = NonNullList.create();
			for (int i = 0; i < ingredientArray.size(); ++i) {
				Ingredient ingredient = Ingredient.deserialize(ingredientArray.get(i));
				if (!ingredient.hasNoMatchingItems()) {
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
		public CuttingBoardRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			String groupIn = buffer.readString(32767);
			Ingredient inputItemIn = Ingredient.read(buffer);
			Ingredient toolIn = Ingredient.read(buffer);

			int i = buffer.readVarInt();
			NonNullList<ChanceResult> resultsIn = NonNullList.withSize(i, ChanceResult.EMPTY);
			for (int j = 0; j < resultsIn.size(); ++j) {
				resultsIn.set(j, ChanceResult.read(buffer));
			}
			String soundEventIn = buffer.readString();

			return new CuttingBoardRecipe(recipeId, groupIn, inputItemIn, toolIn, resultsIn, soundEventIn);
		}

		@Override
		public void write(PacketBuffer buffer, CuttingBoardRecipe recipe) {
			buffer.writeString(recipe.group);
			recipe.input.write(buffer);
			recipe.tool.write(buffer);
			buffer.writeVarInt(recipe.results.size());
			for (ChanceResult result : recipe.results) {
				result.write(buffer);
			}
			buffer.writeString(recipe.soundEvent);
		}
	}
}
