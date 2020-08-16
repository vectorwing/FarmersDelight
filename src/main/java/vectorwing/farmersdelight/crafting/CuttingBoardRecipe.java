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
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import vectorwing.farmersdelight.FarmersDelight;

import javax.annotation.Nullable;

public class CuttingBoardRecipe implements IRecipe<RecipeWrapper>
{
	public static IRecipeType<CuttingBoardRecipe> TYPE = IRecipeType.register(FarmersDelight.MODID + ":cutting");
	public static final CuttingBoardRecipe.Serializer SERIALIZER = new CuttingBoardRecipe.Serializer();

	private final ResourceLocation id;
	private final String group;
	private final Ingredient input;
	private final Ingredient tool;
	private final NonNullList<ItemStack> results;
	private final int effort;

	public CuttingBoardRecipe(ResourceLocation id, String group, Ingredient input, Ingredient tool, NonNullList<ItemStack> results, int effort)
	{
		this.id = id;
		this.group = group;
		this.input = input;
		this.tool = tool;
		this.results = results;
		this.effort = effort;
	}

	@Override
	public ResourceLocation getId()	{ return this.id; }

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

	public Ingredient getTool() {
		return this.tool;
	}

	@Override
	public ItemStack getCraftingResult(RecipeWrapper inv)
	{
		return this.results.get(0).copy();
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return this.results.get(0);
	}

	public NonNullList<ItemStack> getResults() {
		return this.results;
	}

	public int getEffort() {
		return this.effort;
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
	public boolean canFit(int width, int height)
	{
		return width * height >= this.getMaxInputCount();
	}

	@Override
	public IRecipeSerializer<?> getSerializer()
	{
		return CuttingBoardRecipe.SERIALIZER;
	}

	@Override
	public IRecipeType<?> getType()
	{
		return CuttingBoardRecipe.TYPE;
	}

	private static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CuttingBoardRecipe> {

		Serializer() {
			this.setRegistryName(new ResourceLocation(FarmersDelight.MODID, "cutting"));
		}

		@Override
		public CuttingBoardRecipe read(ResourceLocation recipeId, JsonObject json)
		{
			final String groupIn = JSONUtils.getString(json, "group", "");
			final NonNullList<Ingredient> inputItemsIn = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
			final Ingredient toolIn = Ingredient.deserialize(JSONUtils.getJsonObject(json, "tool"));
			if (inputItemsIn.isEmpty()) {
				throw new JsonParseException("No ingredients for cutting recipe");
			} else if (toolIn.hasNoMatchingItems()) {
				throw new JsonParseException("No tool for cutting recipe");
			} else if (inputItemsIn.size() > 1) {
				throw new JsonParseException("Too many ingredients for cutting recipe! Please define only one ingredient.");
			} else {
				final NonNullList<ItemStack> results = readResults(JSONUtils.getJsonArray(json, "result"));
				final int effortIn = JSONUtils.getInt(json, "effort", 1);
				return new CuttingBoardRecipe(recipeId, groupIn, inputItemsIn.get(0), toolIn, results, effortIn);
			}
		}

		private static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
			NonNullList<Ingredient> nonnulllist = NonNullList.create();
			for(int i = 0; i < ingredientArray.size(); ++i) {
				Ingredient ingredient = Ingredient.deserialize(ingredientArray.get(i));
				if (!ingredient.hasNoMatchingItems()) {
					nonnulllist.add(ingredient);
				}
			}
			return nonnulllist;
		}

		private static NonNullList<ItemStack> readResults(JsonArray resultArray) {
			NonNullList<ItemStack> results = NonNullList.create();
			for (JsonElement result : resultArray) {
				results.add(ShapedRecipe.deserializeItem(result.getAsJsonObject()));
			}
			return results;
		}

		@Nullable
		@Override
		public CuttingBoardRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
		{
			String groupIn = buffer.readString(32767);
			Ingredient inputItemIn = Ingredient.read(buffer);
			Ingredient toolIn = Ingredient.read(buffer);
			int i = buffer.readVarInt();
			NonNullList<ItemStack> resultsIn = NonNullList.withSize(i, ItemStack.EMPTY);
			for(int j = 0; j < resultsIn.size(); ++j) {
				resultsIn.set(j, buffer.readItemStack());
			}
			int effortIn = buffer.readVarInt();

			return new CuttingBoardRecipe(recipeId, groupIn, inputItemIn, toolIn, resultsIn, effortIn);
		}

		@Override
		public void write(PacketBuffer buffer, CuttingBoardRecipe recipe)
		{
			buffer.writeString(recipe.group);
			recipe.input.write(buffer);
			recipe.tool.write(buffer);
			buffer.writeVarInt(recipe.results.size());
			for(ItemStack result : recipe.results) {
				buffer.writeItemStack(result);
			}
			buffer.writeVarInt(recipe.effort);
		}
	}
}
