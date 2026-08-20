package vectorwing.farmersdelight.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.registry.ModRecipeBookCategories;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.List;

import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import vectorwing.farmersdelight.client.recipe.CookingPotRecipeDisplay;
public class CookingPotRecipe implements Recipe<RecipeWrapper>
{
	public static final int INPUT_SLOTS = 6;

	private final String group;
	private final CookingPotRecipeBookTab tab;
	private final List<Ingredient> inputItems;
	private final ItemStackTemplate output;
	@Nullable
	private final ItemStackTemplate container;
	private final float experience;
	private final int cookTime;

	public CookingPotRecipe(String group, @Nullable CookingPotRecipeBookTab tab, List<Ingredient> inputItems, ItemStackTemplate output, Optional<ItemStackTemplate> container, float experience, int cookTime) {
		this.group = group;
		this.tab = tab;
		this.inputItems = inputItems;
		this.output = output;

		this.container = container.orElse(null);
		this.experience = experience;
		this.cookTime = cookTime;
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
		NonNullList<Ingredient> ingredients = NonNullList.create();
		ingredients.addAll(this.inputItems);
		return ingredients;
	}

	public ItemStack getResultItem(HolderLookup.Provider provider) {
		return this.output.create();
	}

	/**
	 * Port 38: materialize the stored result template only for JEI display.
	 */
	public ItemStack getJeiResultStack() {
		return this.output.create();
	}

	public ItemStack getOutputContainer() {
		if (this.container != null) return this.container.create();
		ItemStack result = this.output.create();
		return result.getCraftingRemainder() == null ? ItemStack.EMPTY : result.getCraftingRemainder().create();
	}

	public ItemStack getContainerOverride() {
		return this.container == null ? ItemStack.EMPTY : this.container.create();
	}

	@Override
	public ItemStack assemble(RecipeWrapper inv) {
		return this.output.create();
	}

	@Override
	public boolean showNotification() {
		return false;
	}

	public float getExperience() {
		return this.experience;
	}

	public int getCookTime() {
		return this.cookTime;
	}

	@Override
	public boolean matches(RecipeWrapper inv, Level level) {
		return inv.ingredientAmount() == this.inputItems.size() && inv.stackedContents().canCraft(this, null);
	}

	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= this.inputItems.size();
	}


	@Override
	public List<RecipeDisplay> display() {
		ItemStack resultStack = this.getJeiResultStack();
		ItemStack containerStack = this.getOutputContainer();

		if (resultStack.isEmpty()) {
			return List.of();
		}

		ItemStackTemplate resultTemplate = ItemStackTemplate.fromNonEmptyStack(resultStack);
		java.util.Optional<SlotDisplay> containerDisplay = containerStack.isEmpty()
				? java.util.Optional.empty()
				: java.util.Optional.of(new SlotDisplay.ItemStackSlotDisplay(
						ItemStackTemplate.fromNonEmptyStack(containerStack)
				));

		return List.of(new CookingPotRecipeDisplay(
				this.getIngredients().stream().map(Ingredient::display).toList(),
				containerDisplay,
				new SlotDisplay.ItemStackSlotDisplay(resultTemplate),
				new SlotDisplay.ItemSlotDisplay(ModItems.COOKING_POT.get()),
				this.getCookTime(),
				this.getExperience()
		));
	}

	@Override
	public RecipeSerializer<CookingPotRecipe> getSerializer() {
		return ModRecipeSerializers.COOKING.get();
	}

	@Override
	public RecipeType<CookingPotRecipe> getType() {
		return ModRecipeTypes.COOKING.get();
	}

	@Override
	public PlacementInfo placementInfo() {
		return PlacementInfo.create(this.inputItems);
	}

	@Override
	public RecipeBookCategory recipeBookCategory() {
		if (this.tab == null) return ModRecipeBookCategories.COOKING_MISC.get();
		return switch (this.tab) {
			case MEALS -> ModRecipeBookCategories.COOKING_MEALS.get();
			case DRINKS -> ModRecipeBookCategories.COOKING_DRINKS.get();
			case MISC -> ModRecipeBookCategories.COOKING_MISC.get();
		};
	}

	public ItemStack getToastSymbol() {
		return new ItemStack(ModItems.COOKING_POT.get());
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
		return java.util.Objects.equals(container, that.container);
	}

	@Override
	public int hashCode() {
		int result = getGroup().hashCode();
		result = 31 * result + (getRecipeBookTab() != null ? getRecipeBookTab().hashCode() : 0);
		result = 31 * result + inputItems.hashCode();
		result = 31 * result + output.hashCode();
		result = 31 * result + java.util.Objects.hashCode(container);
		result = 31 * result + (getExperience() != 0.0f ? Float.floatToIntBits(getExperience()) : 0);
		result = 31 * result + getCookTime();
		return result;
	}

	public static class Serializer
	{
		private static final MapCodec<CookingPotRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
				Codec.STRING.optionalFieldOf("group", "").forGetter(CookingPotRecipe::getGroup),
				CookingPotRecipeBookTab.CODEC.optionalFieldOf("recipe_book_tab", CookingPotRecipeBookTab.MISC).forGetter(CookingPotRecipe::getRecipeBookTab),
				Ingredient.CODEC.listOf(1, INPUT_SLOTS).fieldOf("ingredients").forGetter(r -> r.inputItems),
				ItemStackTemplate.CODEC.fieldOf("result").forGetter(r -> r.output),
				ItemStackTemplate.CODEC.optionalFieldOf("container").forGetter(r -> Optional.ofNullable(r.container)),
				Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(CookingPotRecipe::getExperience),
				Codec.INT.optionalFieldOf("cookingtime", 200).forGetter(CookingPotRecipe::getCookTime)
		).apply(inst, CookingPotRecipe::new));

		public static final StreamCodec<RegistryFriendlyByteBuf, CookingPotRecipe> STREAM_CODEC = StreamCodec.of(CookingPotRecipe.Serializer::toNetwork, CookingPotRecipe.Serializer::fromNetwork);

		public Serializer() {
		}

		public static MapCodec<CookingPotRecipe> codec() {
			return CODEC;
		}

		public static StreamCodec<RegistryFriendlyByteBuf, CookingPotRecipe> streamCodec() {
			return STREAM_CODEC;
		}

		private static CookingPotRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
			String group = buffer.readUtf();
			CookingPotRecipeBookTab tab = CookingPotRecipeBookTab.STREAM_CODEC.decode(buffer);
			List<Ingredient> inputItems = Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer);
			ItemStackTemplate output = ItemStackTemplate.STREAM_CODEC.decode(buffer);
			Optional<ItemStackTemplate> container = ByteBufCodecs.optional(ItemStackTemplate.STREAM_CODEC).decode(buffer);
			float experience = buffer.readFloat();
			int cookTime = buffer.readVarInt();
			return new CookingPotRecipe(group, tab, inputItems, output, container, experience, cookTime);
		}

		private static void toNetwork(RegistryFriendlyByteBuf buffer, CookingPotRecipe recipe) {
			buffer.writeUtf(recipe.group);
			CookingPotRecipeBookTab.STREAM_CODEC.encode(buffer, recipe.tab == null ? CookingPotRecipeBookTab.MISC : recipe.tab);
			Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, recipe.inputItems);
			ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.output);
			ByteBufCodecs.optional(ItemStackTemplate.STREAM_CODEC).encode(buffer, Optional.ofNullable(recipe.container));
			buffer.writeFloat(recipe.experience);
			buffer.writeVarInt(recipe.cookTime);
		}
	}
}
