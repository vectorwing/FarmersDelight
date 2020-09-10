package vectorwing.farmersdelight.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vectorwing.farmersdelight.FarmersDelight;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CookingPotRecipe implements IRecipe<IInventory> {
	private static final Logger LOGGER = LogManager.getLogger();

	public static IRecipeType<CookingPotRecipe> TYPE = IRecipeType.register(FarmersDelight.MODID + ":cooking");
	public static final Serializer SERIALIZER = new Serializer();
	public static final int INPUT_SLOTS = 6;

	private final ResourceLocation id;
	private final String group;
	private final NonNullList<Ingredient> inputItems;
	private final ItemStack output;
	private final ItemStack container;
	private final float experience;
	private final int cookTime;

	public CookingPotRecipe(ResourceLocation id, String group, NonNullList<Ingredient> inputItems, ItemStack output, ItemStack container, float experience, int cookTime) {
		this.id = id;
		this.group = group;
		this.inputItems = inputItems;
		this.output = output;

		if (!container.isEmpty()) {
			this.container = container;
		}
		else if (!output.getContainerItem().isEmpty()) {
			this.container = output.getContainerItem();
		}
		else {
			this.container = ItemStack.EMPTY;
		}

		this.experience = experience;
		this.cookTime = cookTime;
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
		return this.inputItems;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.output;
	}

	public ItemStack getOutputContainer() {
		return this.container;
	}

	@Override
	public ItemStack getCraftingResult(IInventory inv) {
		return this.output.copy();
	}

	public float getExperience() {
		return this.experience;
	}

	public int getCookTime() {
		return this.cookTime;
	}

	@Override
	public boolean matches(IInventory inv, World worldIn) {
		RecipeItemHelper recipeitemhelper = new RecipeItemHelper();
		java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
		int i = 0;

		for (int j = 0; j < INPUT_SLOTS; ++j) {
			ItemStack itemstack = inv.getStackInSlot(j);
			if (!itemstack.isEmpty()) {
				++i;
				inputs.add(itemstack);
			}
		}
		return i == this.inputItems.size() && net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs, this.inputItems) != null;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= this.inputItems.size();
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return CookingPotRecipe.SERIALIZER;
	}

	@Override
	public IRecipeType<?> getType() {
		return CookingPotRecipe.TYPE;
	}

	private static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CookingPotRecipe> {

		Serializer() {
			this.setRegistryName(new ResourceLocation(FarmersDelight.MODID, "cooking"));
		}

		@Override
		public CookingPotRecipe read(ResourceLocation recipeId, JsonObject json) {
			final String groupIn = JSONUtils.getString(json, "group", "");
			final NonNullList<Ingredient> inputItemsIn = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
			if (inputItemsIn.isEmpty()) {
				throw new JsonParseException("No ingredients for cooking recipe");
			}
			else if (inputItemsIn.size() > CookingPotRecipe.INPUT_SLOTS) {
				throw new JsonParseException("Too many ingredients for cooking recipe! The max is " + CookingPotRecipe.INPUT_SLOTS);
			}
			else {
				final ItemStack outputIn = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
				ItemStack container = JSONUtils.hasField(json, "container") ? ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "container")) : ItemStack.EMPTY;
				final float experienceIn = JSONUtils.getFloat(json, "experience", 0.0F);
				final int cookTimeIn = JSONUtils.getInt(json, "cookingtime", 200);
				return new CookingPotRecipe(recipeId, groupIn, inputItemsIn, outputIn, container, experienceIn, cookTimeIn);
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

		@Nullable
		@Override
		public CookingPotRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			String groupIn = buffer.readString(32767);
			int i = buffer.readVarInt();
			NonNullList<Ingredient> inputItemsIn = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < inputItemsIn.size(); ++j) {
				inputItemsIn.set(j, Ingredient.read(buffer));
			}

			ItemStack outputIn = buffer.readItemStack();
			ItemStack container = buffer.readItemStack();
			float experienceIn = buffer.readFloat();
			int cookTimeIn = buffer.readVarInt();
			return new CookingPotRecipe(recipeId, groupIn, inputItemsIn, outputIn, container, experienceIn, cookTimeIn);
		}

		@Override
		public void write(PacketBuffer buffer, CookingPotRecipe recipe) {
			buffer.writeString(recipe.group);
			buffer.writeVarInt(recipe.inputItems.size());

			for (Ingredient ingredient : recipe.inputItems) {
				ingredient.write(buffer);
			}

			buffer.writeItemStack(recipe.output);
			buffer.writeItemStack(recipe.container);
			buffer.writeFloat(recipe.experience);
			buffer.writeVarInt(recipe.cookTime);
		}
	}
}
