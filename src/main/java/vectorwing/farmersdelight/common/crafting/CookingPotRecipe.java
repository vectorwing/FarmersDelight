package vectorwing.farmersdelight.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import vectorwing.farmersdelight.common.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeBookCategories;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class CookingPotRecipe implements Recipe<CookingPotRecipeInput>
{
	public static final int INPUT_SLOTS = 6;
	private static final Codec<List<Ingredient>> INGREDIENTS_CODEC = Codec.list(Ingredient.CODEC).validate(ingredients -> {
		if (ingredients.isEmpty()) {
			return DataResult.error(() -> "Cooking pot recipes must define at least one ingredient");
		}
		if (ingredients.size() > INPUT_SLOTS) {
			return DataResult.error(() -> "Cooking pot recipes support at most " + INPUT_SLOTS + " ingredients");
		}
		return DataResult.success(ingredients);
	});

	private final String group;
	private final CookingPotRecipeBookTab tab;
	private final NonNullList<Ingredient> inputItems;
	private final ItemStackTemplate output;
	private final Optional<ItemStackTemplate> containerOverride;
	private final float experience;
	private final int cookTime;

	public CookingPotRecipe(String group, @Nullable CookingPotRecipeBookTab tab, NonNullList<Ingredient> inputItems, ItemStack output, ItemStack container, float experience, int cookTime) {
		this(group, tab, inputItems, ItemStackTemplate.fromNonEmptyStack(output), stackToTemplate(container), experience, cookTime);
	}

	public CookingPotRecipe(String group, @Nullable CookingPotRecipeBookTab tab, NonNullList<Ingredient> inputItems, ItemStackTemplate output, Optional<ItemStackTemplate> containerOverride, float experience, int cookTime) {
		this.group = group;
		this.tab = tab;
		this.inputItems = inputItems;
		this.output = output;
		this.containerOverride = containerOverride;
		this.experience = experience;
		this.cookTime = cookTime;
	}

	private static Optional<ItemStackTemplate> stackToTemplate(ItemStack stack) {
		return stack.isEmpty() ? Optional.empty() : Optional.of(ItemStackTemplate.fromNonEmptyStack(stack));
	}

	public String getGroup() {
		return this.group;
	}

	@Override
	public String group() {
		return this.group;
	}

	@Nullable
	public CookingPotRecipeBookTab getRecipeBookTab() {
		return this.tab;
	}

	public NonNullList<Ingredient> getIngredients() {
		return this.inputItems;
	}

	public ItemStack getResultItem(HolderLookup.Provider provider) {
		return this.output.create();
	}

	public ItemStack getOutputContainer() {
		return this.containerOverride
				.map(ItemStackTemplate::create)
				.orElseGet(() -> vectorwing.farmersdelight.common.utility.ItemUtils.getCraftingRemainingItem(this.output.create()));
	}

	public ItemStack getContainerOverride() {
		return this.containerOverride.map(ItemStackTemplate::create).orElse(ItemStack.EMPTY);
	}

	@Override
	public ItemStack assemble(CookingPotRecipeInput inv) {
		return this.output.create();
	}

	public float getExperience() {
		return this.experience;
	}

	public int getCookTime() {
		return this.cookTime;
	}

	@Override
	public boolean matches(CookingPotRecipeInput inv, Level level) {
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

	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= this.inputItems.size();
	}

	@Override
	public RecipeSerializer<? extends Recipe<CookingPotRecipeInput>> getSerializer() {
		return ModRecipeSerializers.COOKING.get();
	}

	@Override
	public RecipeType<? extends Recipe<CookingPotRecipeInput>> getType() {
		return ModRecipeTypes.COOKING.get();
	}

	@Override
	public boolean showNotification() {
		return true;
	}

	@Override
	public PlacementInfo placementInfo() {
		return PlacementInfo.create(this.inputItems);
	}

	@Override
	public List<RecipeDisplay> display() {
		return List.of(new ShapelessCraftingRecipeDisplay(
				this.inputItems.stream().map(Ingredient::display).toList(),
				new SlotDisplay.ItemStackSlotDisplay(this.output),
				new SlotDisplay.ItemSlotDisplay(ModItems.COOKING_POT.get())
		));
	}

	@Override
	public RecipeBookCategory recipeBookCategory() {
		return switch (this.tab == null ? CookingPotRecipeBookTab.MISC : this.tab) {
			case MEALS -> ModRecipeBookCategories.COOKING_MEALS.get();
			case DRINKS -> ModRecipeBookCategories.COOKING_DRINKS.get();
			case MISC -> ModRecipeBookCategories.COOKING_MISC.get();
		};
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CookingPotRecipe that = (CookingPotRecipe) o;

		if (Float.compare(that.getExperience(), getExperience()) != 0) return false;
		if (getCookTime() != that.getCookTime()) return false;
		if (!getGroup().equals(that.getGroup())) return false;
		if (tab != that.tab) return false;
		if (!inputItems.equals(that.inputItems)) return false;
		if (!output.equals(that.output)) return false;
		return containerOverride.equals(that.containerOverride);
	}

	@Override
	public int hashCode() {
		int result = getGroup().hashCode();
		result = 31 * result + (getRecipeBookTab() != null ? getRecipeBookTab().hashCode() : 0);
		result = 31 * result + inputItems.hashCode();
		result = 31 * result + output.hashCode();
		result = 31 * result + containerOverride.hashCode();
		result = 31 * result + (getExperience() != 0.0f ? Float.floatToIntBits(getExperience()) : 0);
		result = 31 * result + getCookTime();
		return result;
	}

	public static class Serializer
	{
		public static final MapCodec<CookingPotRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
				Codec.STRING.optionalFieldOf("group", "").forGetter(CookingPotRecipe::getGroup),
				CookingPotRecipeBookTab.CODEC.optionalFieldOf("recipe_book_tab", CookingPotRecipeBookTab.MISC).forGetter(CookingPotRecipe::getRecipeBookTab),
				INGREDIENTS_CODEC.fieldOf("ingredients").xmap(ingredients -> {
					NonNullList<Ingredient> nonNullList = NonNullList.create();
					nonNullList.addAll(ingredients);
					return nonNullList;
				}, ingredients -> ingredients).forGetter(CookingPotRecipe::getIngredients),
				ItemStackTemplate.CODEC.fieldOf("result").forGetter(r -> r.output),
				ItemStackTemplate.CODEC.optionalFieldOf("container").forGetter(r -> r.containerOverride),
				Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(CookingPotRecipe::getExperience),
				Codec.INT.optionalFieldOf("cookingtime", 200).forGetter(CookingPotRecipe::getCookTime)
		).apply(inst, CookingPotRecipe::new));

		public static final StreamCodec<RegistryFriendlyByteBuf, CookingPotRecipe> STREAM_CODEC = StreamCodec.of(CookingPotRecipe.Serializer::toNetwork, CookingPotRecipe.Serializer::fromNetwork);

		public Serializer() {
		}

		public MapCodec<CookingPotRecipe> codec() {
			return CODEC;
		}

		public StreamCodec<RegistryFriendlyByteBuf, CookingPotRecipe> streamCodec() {
			return STREAM_CODEC;
		}

		private static CookingPotRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
			String group = buffer.readUtf();
			CookingPotRecipeBookTab tab = CookingPotRecipeBookTab.findByName(buffer.readUtf());
			int i = buffer.readVarInt();
			NonNullList<Ingredient> inputItems = NonNullList.create();
			for (int j = 0; j < i; ++j) {
				inputItems.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
			}

			ItemStackTemplate output = ItemStackTemplate.STREAM_CODEC.decode(buffer);
			Optional<ItemStackTemplate> container = buffer.readBoolean() ? Optional.of(ItemStackTemplate.STREAM_CODEC.decode(buffer)) : Optional.empty();
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

			ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.output);
			buffer.writeBoolean(recipe.containerOverride.isPresent());
			recipe.containerOverride.ifPresent(container -> ItemStackTemplate.STREAM_CODEC.encode(buffer, container));
			buffer.writeFloat(recipe.experience);
			buffer.writeVarInt(recipe.cookTime);
		}
	}
}
