package vectorwing.farmersdelight.data.builder;

import net.minecraft.advancements.Criterion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.FluidEmptyingRecipe;

import java.util.Objects;

public class FluidEmptyingRecipeBuilder implements RecipeBuilder
{
	protected final FluidStack fluid;
	protected final Ingredient ingredient;
	protected final ItemStack result;

	@Nullable
	private String namespace;

	public FluidEmptyingRecipeBuilder(FluidStack fluid, Ingredient ingredient, ItemLike result) {
		this.fluid = fluid;
		this.ingredient = ingredient;
		this.result = new ItemStack(result);
	}

	public static FluidEmptyingRecipeBuilder emptying(FluidStack fluid, Ingredient ingredient, ItemLike result) {
		return new FluidEmptyingRecipeBuilder(fluid, ingredient, result);
	}

	@Override
	public Item getResult() {
		return result.getItem();
	}

	/**
	 * Sets a custom namespace (mod ID) for the recipe. Use this only if the result isn't registered to the mod ID you want.
	 */
	public FluidEmptyingRecipeBuilder setNamespace(String namespace) {
		this.namespace = namespace;
		return this;
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

	public void saveToFD(RecipeOutput output, ItemLike outputName) {
		this.save(output, ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, getDefaultRecipeId(outputName).getPath()).withPrefix("fluid_emptying/"));
	}

	public void save(RecipeOutput output) {
		ResourceLocation defaultLocation = getDefaultRecipeId(ingredient.getItems()[0].getItem());
		save(output, ResourceLocation.fromNamespaceAndPath(this.namespace != null ? namespace : defaultLocation.getNamespace(), defaultLocation.getPath()).withPrefix("fluid_emptying/"));
	}

	@Override
	public void save(RecipeOutput output, ResourceLocation id) {
		FluidEmptyingRecipe recipe = new FluidEmptyingRecipe(
			this.fluid,
			this.ingredient,
			this.result
		);
		output.accept(id, recipe, null);
	}

	@Override
	public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
		return this; // No-op - No Recipe Book (yet)?
	}

	@Override
	public RecipeBuilder group(@Nullable String groupName) {
		return this; // No-op - No Recipe Book (yet)?
	}
}
