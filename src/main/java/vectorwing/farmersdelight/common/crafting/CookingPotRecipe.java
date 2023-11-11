package vectorwing.farmersdelight.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipeCodecs;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import javax.annotation.Nullable;
import java.util.Optional;

@SuppressWarnings("ClassCanBeRecord")
public class CookingPotRecipe implements Recipe<RecipeWrapper>
{
	public static final int INPUT_SLOTS = 6;

	private final String group;
	private final CookingPotRecipeBookTab tab;
	private final NonNullList<Ingredient> inputItems;
	private final ItemStack output;
	private final ItemStack container;
	private final float experience;
	private final int cookTime;

	public CookingPotRecipe(String group, @Nullable CookingPotRecipeBookTab tab, NonNullList<Ingredient> inputItems, ItemStack output, ItemStack container, float experience, int cookTime) {
		this.group = group;
		this.tab = tab;
		this.inputItems = inputItems;
		this.output = output;

		if (!container.isEmpty()) {
			this.container = container;
		} else if (!output.getCraftingRemainingItem().isEmpty()) {
			this.container = output.getCraftingRemainingItem();
		} else {
			this.container = ItemStack.EMPTY;
		}

		this.experience = experience;
		this.cookTime = cookTime;
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	@Nullable
	public CookingPotRecipeBookTab getRecipeBookTab() {
		return this.tab;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		return this.inputItems;
	}

	@Override
	public ItemStack getResultItem(RegistryAccess access) {
		return this.output;
	}

	public ItemStack getOutputContainer() {
		return this.container;
	}

	@Override
	public ItemStack assemble(RecipeWrapper inv, RegistryAccess access) {
		return this.output.copy();
	}

	public float getExperience() {
		return this.experience;
	}

	public int getCookTime() {
		return this.cookTime;
	}

	@Override
	public boolean matches(RecipeWrapper inv, Level level) {
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
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= this.inputItems.size();
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.COOKING.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ModRecipeTypes.COOKING.get();
	}

	@Override
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
		return container.equals(that.container);
	}

	@Override
	public int hashCode() {
		int result = getGroup().hashCode();
		result = 31 * result + (getRecipeBookTab() != null ? getRecipeBookTab().hashCode() : 0);
		result = 31 * result + inputItems.hashCode();
		result = 31 * result + output.hashCode();
		result = 31 * result + container.hashCode();
		result = 31 * result + (getExperience() != +0.0f ? Float.floatToIntBits(getExperience()) : 0);
		result = 31 * result + getCookTime();
		return result;
	}

	public static class Serializer implements RecipeSerializer<CookingPotRecipe>
	{
		private static final Codec<CookingPotRecipe> CODEC = RecordCodecBuilder.create(inst -> inst.group(
				ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(CookingPotRecipe::getGroup),
				ExtraCodecs.strictOptionalField(CookingPotRecipeBookTab.CODEC, "recipe_book_tab").xmap(optional -> optional.orElse(null), Optional::of).forGetter(CookingPotRecipe::getRecipeBookTab),
				Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredients").xmap(ingredients -> {
					NonNullList<Ingredient> nonNullList = NonNullList.create();
					nonNullList.addAll(ingredients);
					return nonNullList;
				}, ingredients -> ingredients).forGetter(CookingPotRecipe::getIngredients),
				CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC.fieldOf("result").forGetter(r -> r.output),
				ExtraCodecs.strictOptionalField(CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC, "container", ItemStack.EMPTY).forGetter(CookingPotRecipe::getOutputContainer),
				ExtraCodecs.strictOptionalField(Codec.FLOAT, "experience", 0.0F).forGetter(CookingPotRecipe::getExperience),
				ExtraCodecs.strictOptionalField(Codec.INT, "cookingtime", 200).forGetter(CookingPotRecipe::getCookTime)
		).apply(inst, CookingPotRecipe::new));

		public Serializer() {
		}

		@Override
		public Codec<CookingPotRecipe> codec() {
			return CODEC;
		}


		@Override
		public @Nullable CookingPotRecipe fromNetwork(FriendlyByteBuf buffer) {
			String groupIn = buffer.readUtf();
			CookingPotRecipeBookTab tabIn = CookingPotRecipeBookTab.findByName(buffer.readUtf());
			int i = buffer.readVarInt();
			NonNullList<Ingredient> inputItemsIn = NonNullList.withSize(i, Ingredient.EMPTY);

			for (int j = 0; j < inputItemsIn.size(); ++j) {
				inputItemsIn.set(j, Ingredient.fromNetwork(buffer));
			}

			ItemStack outputIn = buffer.readItem();
			ItemStack container = buffer.readItem();
			float experienceIn = buffer.readFloat();
			int cookTimeIn = buffer.readVarInt();
			return new CookingPotRecipe(groupIn, tabIn, inputItemsIn, outputIn, container, experienceIn, cookTimeIn);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, CookingPotRecipe recipe) {
			buffer.writeUtf(recipe.group);
			buffer.writeUtf(recipe.tab != null ? recipe.tab.toString() : "");
			buffer.writeVarInt(recipe.inputItems.size());

			for (Ingredient ingredient : recipe.inputItems) {
				ingredient.toNetwork(buffer);
			}

			buffer.writeItem(recipe.output);
			buffer.writeItem(recipe.container);
			buffer.writeFloat(recipe.experience);
			buffer.writeVarInt(recipe.cookTime);
		}
	}
}
