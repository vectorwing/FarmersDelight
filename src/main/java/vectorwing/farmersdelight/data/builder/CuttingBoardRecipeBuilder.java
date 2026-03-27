package vectorwing.farmersdelight.data.builder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CuttingBoardRecipeBuilder implements RecipeBuilder
{
	private final NonNullList<ChanceResult> results = NonNullList.createWithCapacity(4);
	private final Ingredient ingredient;
	private final Ingredient tool;
	private SoundEvent soundEvent;
	@Nullable
	private String namespace;
	private CuttingRecipeFolder folder;

	public CuttingBoardRecipeBuilder(Ingredient ingredient, Ingredient tool, ItemLike mainResult, int count, float chance) {
		this.results.add(new ChanceResult(new ItemStack(mainResult.asItem(), count), chance));
		this.ingredient = ingredient;
		this.tool = tool;
		this.folder = CuttingRecipeFolder.CUTTING;
	}

	/**
	 * Creates a new builder for a cutting recipe.
	 */
	public static CuttingBoardRecipeBuilder cuttingRecipe(Ingredient ingredient, Ingredient tool, ItemLike mainResult, int count) {
		return new CuttingBoardRecipeBuilder(ingredient, tool, mainResult, count, 1);
	}

	/**
	 * Creates a new builder for a cutting recipe, providing a chance for the main output to drop.
	 */
	public static CuttingBoardRecipeBuilder cuttingRecipe(Ingredient ingredient, Ingredient tool, ItemLike mainResult, int count, float chance) {
		return new CuttingBoardRecipeBuilder(ingredient, tool, mainResult, count, chance);
	}

	/**
	 * Creates a new builder for a cutting recipe, returning 1 unit of the result.
	 */
	public static CuttingBoardRecipeBuilder cuttingRecipe(Ingredient ingredient, Ingredient tool, ItemLike mainResult) {
		return new CuttingBoardRecipeBuilder(ingredient, tool, mainResult, 1, 1);
	}

	public CuttingBoardRecipeBuilder addResult(ItemLike result) {
		return this.addResult(result, 1);
	}

	public CuttingBoardRecipeBuilder addResult(ItemLike result, int count) {
		this.results.add(new ChanceResult(new ItemStack(result.asItem(), count), 1));
		return this;
	}

	public CuttingBoardRecipeBuilder addResultWithChance(ItemLike result, float chance) {
		return this.addResultWithChance(result, chance, 1);
	}

	public CuttingBoardRecipeBuilder addResultWithChance(ItemLike result, float chance, int count) {
		this.results.add(new ChanceResult(new ItemStack(result.asItem(), count), chance));
		return this;
	}

	public CuttingBoardRecipeBuilder addSound(SoundEvent soundEvent) {
		this.soundEvent = soundEvent;
		return this;
	}

	@Override
	public RecipeBuilder unlockedBy(String p_176496_, Criterion<?> p_301065_) {
		return this; // No-op - Cutting Board has no recipe book unlocks
	}

	/**
	 * Sets a custom namespace (mod ID) for the recipe. Use this only if the ingredient isn't registered to the mod ID you want.
	 */
	public CuttingBoardRecipeBuilder setNamespace(String namespace) {
		this.namespace = namespace;
		return this;
	}

	public CuttingBoardRecipeBuilder salvaging() {
		this.folder = CuttingRecipeFolder.SALVAGING;
		return this;
	}

	@Override
	public RecipeBuilder group(@Nullable String p_176495_) {
		return this;
	}

	@Override
	public Item getResult() {
		return this.ingredient.getItems()[0].getItem();
	}

	public static ResourceLocation getDefaultRecipeId(ItemLike itemLike) {
		return Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(itemLike.asItem()));
	}

	/**
	 * Shorthand for saving recipes in the FD namespace.
	 */
	public void saveToFD(RecipeOutput output) {
		this.setNamespace(FarmersDelight.MODID).save(output);
	}

	public void save(RecipeOutput output) {
		ResourceLocation defaultLocation = getDefaultRecipeId(getResult());
		save(output, ResourceLocation.fromNamespaceAndPath(this.namespace != null ? namespace : defaultLocation.getNamespace(), defaultLocation.getPath()).withPrefix(folder.getSerializedName() + "/"));
	}

	public void build(RecipeOutput outputIn, String save) {
		ResourceLocation resourcelocation = BuiltInRegistries.ITEM.getKey(getResult());
		if ((ResourceLocation.parse(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Cutting Recipe " + save + " should remove its 'save' argument");
		} else {
			this.build(outputIn, ResourceLocation.parse(save));
		}
	}

	public void build(RecipeOutput output, ResourceLocation id) {
		save(output, id);
	}

	@Override
	public void save(RecipeOutput output, ResourceLocation id) {
		CuttingBoardRecipe recipe = new CuttingBoardRecipe(
				"",
				this.ingredient,
				this.tool,
				this.results,
				this.soundEvent == null ? Optional.empty() : Optional.of(this.soundEvent)
		);
		output.accept(id, recipe, null);
	}
}
