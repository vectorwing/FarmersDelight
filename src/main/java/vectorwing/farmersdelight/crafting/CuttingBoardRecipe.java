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
import net.minecraftforge.common.ToolType;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;
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
	private final ToolType toolType;
	private final NonNullList<ItemStack> results;
	private final String soundEvent;

	public CuttingBoardRecipe(ResourceLocation id, String group, Ingredient input, Ingredient tool, @Nullable ToolType toolType, NonNullList<ItemStack> results, String soundEvent) {
		this.id = id;
		this.group = group;
		this.input = input;
		this.tool = tool;
		this.toolType = toolType;
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

	@Nullable
	public ToolType getToolType() {
		return this.toolType;
	}

	@Override
	public ItemStack getCraftingResult(RecipeWrapper inv) {
		return this.results.get(0).copy();
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.results.get(0);
	}

	public NonNullList<ItemStack> getResults() {
		return this.results;
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
			final Ingredient toolIn;
			final ToolType toolTypeIn;
			if (JSONUtils.hasField(toolObject, "type")) {
				// Create an Ingredient from all items that have the specified ToolType
				toolTypeIn = ToolType.get(JSONUtils.getString(toolObject, "type"));
				toolIn = Ingredient.fromStacks(ForgeRegistries.ITEMS.getValues().stream()
						.filter((item) -> item.getToolTypes(new ItemStack(item)).contains(toolTypeIn))
						.map(ItemStack::new));
			} else {
				toolTypeIn = null;
				toolIn = Ingredient.deserialize(toolObject);
			}
			if (inputItemsIn.isEmpty()) {
				throw new JsonParseException("No ingredients for cutting recipe");
			} else if (toolIn.hasNoMatchingItems()) {
				throw new JsonParseException("No tool for cutting recipe");
			} else if (inputItemsIn.size() > 1) {
				throw new JsonParseException("Too many ingredients for cutting recipe! Please define only one ingredient.");
			} else {
				final NonNullList<ItemStack> results = readResults(JSONUtils.getJsonArray(json, "result"));
				final String soundID = JSONUtils.getString(json, "sound", "");
				return new CuttingBoardRecipe(recipeId, groupIn, inputItemsIn.get(0), toolIn, toolTypeIn, results, soundID);
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

		private static NonNullList<ItemStack> readResults(JsonArray resultArray) {
			NonNullList<ItemStack> results = NonNullList.create();
			for (JsonElement result : resultArray) {
				results.add(ShapedRecipe.deserializeItem(result.getAsJsonObject()));
			}
			return results;
		}

		@Nullable
		@Override
		public CuttingBoardRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			String groupIn = buffer.readString(32767);
			Ingredient inputItemIn = Ingredient.read(buffer);
			Ingredient toolIn = Ingredient.read(buffer);
			String toolTypeName = buffer.readString();
			ToolType toolTypeIn = toolTypeName.isEmpty() ? null : ToolType.get(toolTypeName);

			int i = buffer.readVarInt();
			NonNullList<ItemStack> resultsIn = NonNullList.withSize(i, ItemStack.EMPTY);
			for (int j = 0; j < resultsIn.size(); ++j) {
				resultsIn.set(j, buffer.readItemStack());
			}
			String soundEventIn = buffer.readString();

			return new CuttingBoardRecipe(recipeId, groupIn, inputItemIn, toolIn, toolTypeIn, resultsIn, soundEventIn);
		}

		@Override
		public void write(PacketBuffer buffer, CuttingBoardRecipe recipe) {
			buffer.writeString(recipe.group);
			recipe.input.write(buffer);
			recipe.tool.write(buffer);
			ToolType toolTypeName = recipe.getToolType();
			buffer.writeString(toolTypeName == null ? "" : toolTypeName.getName());
			buffer.writeVarInt(recipe.results.size());
			for (ItemStack result : recipe.results) {
				buffer.writeItemStack(result);
			}
			buffer.writeString(recipe.soundEvent);
		}
	}
}
