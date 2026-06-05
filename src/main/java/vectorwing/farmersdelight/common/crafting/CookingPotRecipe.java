package vectorwing.farmersdelight.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModRecipeCategories;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.Optional;

public class CookingPotRecipe implements Recipe<RecipeInput>
{
	public static final MapCodec<CookingPotRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
		Codec.STRING.optionalFieldOf("group", "").forGetter(CookingPotRecipe::group),
		CookingPotRecipeBookTab.CODEC.optionalFieldOf("recipe_book_tab", CookingPotRecipeBookTab.MISC).forGetter(CookingPotRecipe::getRecipeBookTab),
		Ingredient.CODEC.listOf(1, CookingPotRecipe.INPUT_SLOTS).fieldOf("ingredients").xmap(ingredients -> {
			NonNullList<Ingredient> nonNullList = NonNullList.create();
			nonNullList.addAll(ingredients);
			return nonNullList;
		}, ingredients -> ingredients).forGetter(CookingPotRecipe::getIngredients),
		ItemStack.CODEC.validate(ItemStack::validateStrict).fieldOf("result").forGetter(r -> r.output),
		ItemStack.CODEC.validate(ItemStack::validateStrict).optionalFieldOf("container", ItemStack.EMPTY).forGetter(CookingPotRecipe::getContainerOverride),
		Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(CookingPotRecipe::getExperience),
		Codec.INT.optionalFieldOf("cookingtime", 200).forGetter(CookingPotRecipe::getCookTime)
	).apply(inst, CookingPotRecipe::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, CookingPotRecipe> STREAM_CODEC =
		StreamCodec.of(CodecHelpers::toNetwork, CodecHelpers::fromNetwork);

	public static final int INPUT_SLOTS = 6;

	private final String group;
	private final CookingPotRecipeBookTab tab;
	private final NonNullList<Ingredient> inputItems;
	private final ItemStack output;
	private final ItemStack container;
	private final ItemStack containerOverride;
	private final float experience;
	private final int cookTime;

	private PlacementInfo placementInfo;

	public CookingPotRecipe(String group, @Nullable CookingPotRecipeBookTab tab, NonNullList<Ingredient> inputItems, ItemStack output, ItemStack container, float experience, int cookTime) {
		this.group = group;
		this.tab = tab;
		this.inputItems = inputItems;
		this.output = output;

		if (!container.isEmpty()) {
			this.container = container;
		} else if (output.getCraftingRemainder() != null && output.getCraftingRemainder().count() != 0) {
			this.container = output.getCraftingRemainder().create();
		} else {
			this.container = ItemStack.EMPTY;
		}

		this.containerOverride = container;
		this.experience = experience;
		this.cookTime = cookTime;

		Optional<Ingredient> emptyIngredient = Optional.of(Ingredient.of());
		LinkedList<Optional<Ingredient>> placementIngredients = new LinkedList<>();
		// Add the actual ingredients
		for (Ingredient ingredient : inputItems) {
			placementIngredients.add(Optional.of(ingredient));
		}
		// Fill the remaining empty slots
		for (int i = 0; i < INPUT_SLOTS - inputItems.size(); i++) {
			placementIngredients.add(emptyIngredient);
		}
		placementIngredients.add(Optional.empty()); // Meal display
		placementIngredients.add(this.container == ItemStack.EMPTY ? emptyIngredient : Optional.of(Ingredient.of(this.container.getItem()))); // Container
		placementIngredients.add(Optional.empty()); // Output

		this.placementInfo = PlacementInfo.createFromOptionals(placementIngredients);
	}

	@Nullable
	public CookingPotRecipeBookTab getRecipeBookTab() {
		return this.tab;
	}

	public NonNullList<Ingredient> getIngredients() {
		return this.inputItems;
	}

	public ItemStack getOutputContainer() {
		return this.container;
	}

	public ItemStack getContainerOverride() {
		return this.containerOverride;
	}

	public float getExperience() {
		return this.experience;
	}

	public int getCookTime() {
		return this.cookTime;
	}

	@Override
	public boolean matches(RecipeInput inv, Level level) {
		java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
		int i = 0;

		for (int j = 0; j < INPUT_SLOTS; ++j) {
			ItemStack itemstack = inv.getItem(j);
			if (!itemstack.isEmpty()) {
				++i;
				inputs.add(itemstack);
			}
		}
		return i == this.inputItems.size() && RecipeMatcher.findMatches(inputs, this.inputItems) != null;
	}

	@Override
	public ItemStack assemble(RecipeInput recipeInput) {
		return this.output.copy();
	}

	@Override
	public boolean showNotification() {
		return false;
	}

	@Override
	public String group() {
		return this.group;
	}

	@Override
	public RecipeSerializer<? extends Recipe<RecipeInput>> getSerializer() {
		return ModRecipeSerializers.COOKING.get();
	}

	@Override
	public RecipeType<? extends Recipe<RecipeInput>> getType() {
		return ModRecipeTypes.COOKING.get();
	}

	@Override
	public PlacementInfo placementInfo() {
		return placementInfo;
	}

	@Override
	public RecipeBookCategory recipeBookCategory() {
		return switch(tab) {
			case MEALS -> ModRecipeCategories.COOKING_MEALS.get();
			case DRINKS -> ModRecipeCategories.COOKING_DRINKS.get();
			case MISC -> ModRecipeCategories.COOKING_MISC.get();
		};
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CookingPotRecipe that = (CookingPotRecipe) o;

		if (Float.compare(that.getExperience(), getExperience()) != 0) return false;
		if (getCookTime() != that.getCookTime()) return false;
		if (!group().equals(that.group())) return false;
		if (tab != that.tab) return false;
		if (!inputItems.equals(that.inputItems)) return false;
		if (!output.equals(that.output)) return false;
		return container.equals(that.container);
	}

	@Override
	public int hashCode() {
		int result = group().hashCode();
		result = 31 * result + (getRecipeBookTab() != null ? getRecipeBookTab().hashCode() : 0);
		result = 31 * result + inputItems.hashCode();
		result = 31 * result + output.hashCode();
		result = 31 * result + container.hashCode();
		result = 31 * result + (getExperience() != 0.0f ? Float.floatToIntBits(getExperience()) : 0);
		result = 31 * result + getCookTime();
		return result;
	}

	private static class CodecHelpers
	{
		private static CookingPotRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
			String group = buffer.readUtf();
			CookingPotRecipeBookTab tab = CookingPotRecipeBookTab.findByName(buffer.readUtf());
			int i = buffer.readVarInt();
			NonNullList<Ingredient> inputItems = NonNullList.withSize(i, Ingredient.of());

			inputItems.replaceAll(ignored -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));

			ItemStack output = ItemStack.STREAM_CODEC.decode(buffer);
			ItemStack container = ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer);
			float experience = buffer.readFloat();
			int cookTime = buffer.readVarInt();
			return new CookingPotRecipe(group, tab, inputItems, output, container, experience, cookTime);
		}

		private static void toNetwork(RegistryFriendlyByteBuf buffer, CookingPotRecipe recipe) {
			buffer.writeUtf(recipe.group);
			buffer.writeUtf(recipe.tab != null ? recipe.tab.toString() : "");
			buffer.writeVarInt(recipe.inputItems.size());

			for (Ingredient ingredient : recipe.inputItems) {
				Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
			}

			ItemStack.STREAM_CODEC.encode(buffer, recipe.output);
			ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.container);
			buffer.writeFloat(recipe.experience);
			buffer.writeVarInt(recipe.cookTime);
		}
	}
}
