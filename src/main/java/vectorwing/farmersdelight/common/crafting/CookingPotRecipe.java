package vectorwing.farmersdelight.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import javax.annotation.Nullable;
import java.util.Optional;

public class CookingPotRecipe implements Recipe<RecipeInput>
{
	public static final int INPUT_SLOTS = 6;

	public static final MapCodec<CookingPotRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
			Codec.STRING.optionalFieldOf("group", "").forGetter(CookingPotRecipe::group),
			CookingPotRecipeBookTab.CODEC.optionalFieldOf("recipe_book_tab", CookingPotRecipeBookTab.MISC).forGetter(CookingPotRecipe::getRecipeBookTab),
			Ingredient.CODEC.listOf(1, INPUT_SLOTS).fieldOf("ingredients").xmap(ingredients -> {
				NonNullList<Ingredient> nonNullList = NonNullList.create();
				nonNullList.addAll(ingredients);
				return nonNullList;
			}, ingredients -> ingredients).forGetter(CookingPotRecipe::getIngredients),
			ItemStackTemplate.CODEC.fieldOf("result").forGetter(r -> r.output),
			ItemStackTemplate.CODEC.optionalFieldOf("container").forGetter(r -> r.containerOverride),
			Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(CookingPotRecipe::getExperience),
			Codec.INT.optionalFieldOf("cookingtime", 200).forGetter(CookingPotRecipe::getCookTime)
	).apply(inst, CookingPotRecipe::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, CookingPotRecipe> STREAM_CODEC = StreamCodec.of(CookingPotRecipe::toNetwork, CookingPotRecipe::fromNetwork);

	public static final RecipeSerializer<CookingPotRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

	private final String group;
	private final CookingPotRecipeBookTab tab;
	private final NonNullList<Ingredient> inputItems;
	private final ItemStackTemplate output;
	private final Optional<ItemStackTemplate> containerOverride;
	private final float experience;
	private final int cookTime;
	@Nullable
	private PlacementInfo placementInfo;

	public CookingPotRecipe(String group, @Nullable CookingPotRecipeBookTab tab, NonNullList<Ingredient> inputItems, ItemStackTemplate output, Optional<ItemStackTemplate> containerOverride, float experience, int cookTime) {
		this.group = group;
		this.tab = tab;
		this.inputItems = inputItems;
		this.output = output;
		this.containerOverride = containerOverride;
		this.experience = experience;
		this.cookTime = cookTime;
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
		if (this.containerOverride.isPresent()) {
			return this.containerOverride.get().create();
		}
		ItemStackTemplate remainder = this.output.create().getCraftingRemainder();
		return remainder != null ? remainder.create() : ItemStack.EMPTY;
	}

	@Override
	public ItemStack assemble(RecipeInput inv) {
		return this.output.create();
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
	public boolean showNotification() {
		return false;
	}

	@Override
	public PlacementInfo placementInfo() {
		if (this.placementInfo == null) {
			this.placementInfo = PlacementInfo.create(this.inputItems);
		}
		return this.placementInfo;
	}

	@Override
	public RecipeBookCategory recipeBookCategory() {
		return RecipeBookCategories.CRAFTING_MISC;
	}

	@Override
	public RecipeSerializer<CookingPotRecipe> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public RecipeType<CookingPotRecipe> getType() {
		return ModRecipeTypes.COOKING.get();
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
		return containerOverride.equals(that.containerOverride);
	}

	@Override
	public int hashCode() {
		int result = group().hashCode();
		result = 31 * result + (getRecipeBookTab() != null ? getRecipeBookTab().hashCode() : 0);
		result = 31 * result + inputItems.hashCode();
		result = 31 * result + output.hashCode();
		result = 31 * result + containerOverride.hashCode();
		result = 31 * result + (getExperience() != 0.0f ? Float.floatToIntBits(getExperience()) : 0);
		result = 31 * result + getCookTime();
		return result;
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
		Optional<ItemStackTemplate> containerOverride = ByteBufCodecs.optional(ItemStackTemplate.STREAM_CODEC).decode(buffer);
		float experience = buffer.readFloat();
		int cookTime = buffer.readVarInt();
		return new CookingPotRecipe(group, tab, inputItems, output, containerOverride, experience, cookTime);
	}

	private static void toNetwork(RegistryFriendlyByteBuf buffer, CookingPotRecipe recipe) {
		buffer.writeUtf(recipe.group);
		buffer.writeUtf(recipe.tab != null ? recipe.tab.toString() : "");
		buffer.writeVarInt(recipe.inputItems.size());

		for (Ingredient ingredient : recipe.inputItems) {
			Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
		}

		ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.output);
		ByteBufCodecs.optional(ItemStackTemplate.STREAM_CODEC).encode(buffer, recipe.containerOverride);
		buffer.writeFloat(recipe.experience);
		buffer.writeVarInt(recipe.cookTime);
	}
}
