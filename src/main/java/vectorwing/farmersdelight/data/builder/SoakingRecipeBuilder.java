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
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.SoakingRecipe;

import java.util.Objects;

public class SoakingRecipeBuilder implements RecipeBuilder
{
	public static final int DEFAULT_PROCESSING_TIME = 20;        // 1 second

	protected final Ingredient ingredient;
	protected final SizedFluidIngredient fluid;
	protected boolean consumeFluid;
	private final Item result;
	protected final ItemStack resultStack;
	private final int processingTime;

	@Nullable
	private String namespace;

	public SoakingRecipeBuilder(Ingredient ingredient, SizedFluidIngredient fluid, ItemLike result) {
		this(ingredient, fluid, result, 1, DEFAULT_PROCESSING_TIME, true);
	}

	public SoakingRecipeBuilder(Ingredient ingredient, SizedFluidIngredient fluid, ItemLike result, int count) {
		this(ingredient, fluid, result, count, DEFAULT_PROCESSING_TIME, true);
	}

	public SoakingRecipeBuilder(Ingredient ingredient, SizedFluidIngredient fluid, ItemLike result, int count, int processingTime) {
		this(ingredient, fluid, result, count, processingTime, true);
	}

	public SoakingRecipeBuilder(Ingredient ingredient, SizedFluidIngredient fluid, ItemLike result, int count, int processingTime, boolean consumeFluid) {
		this.ingredient = ingredient;
		this.fluid = fluid;
		this.consumeFluid = consumeFluid;
		this.resultStack = new ItemStack(result, count);
		this.result = resultStack.getItem();
		this.processingTime = processingTime;
	}

	public static SoakingRecipeBuilder soaking(Ingredient ingredient, SizedFluidIngredient fluid, ItemLike result) {
		return new SoakingRecipeBuilder(ingredient, fluid, result);
	}

	public static SoakingRecipeBuilder instant(Ingredient ingredient, SizedFluidIngredient fluid, ItemLike result, int count) {
		return new SoakingRecipeBuilder(ingredient, fluid, result, count, 0);
	}

	public static SoakingRecipeBuilder waterSoaking(Ingredient ingredient, ItemLike result) {
		return new SoakingRecipeBuilder(ingredient, SizedFluidIngredient.of(Tags.Fluids.WATER, 1), result).fluidNotConsumed();
	}

	public SoakingRecipeBuilder fluidNotConsumed() {
		this.consumeFluid = false;
		return this;
	}

	@Override
	public Item getResult() {
		return result;
	}

	/**
	 * Sets a custom namespace (mod ID) for the recipe. Use this only if the result isn't registered to the mod ID you want.
	 */
	public SoakingRecipeBuilder setNamespace(String namespace) {
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

	public void save(RecipeOutput output) {
		ResourceLocation defaultLocation = getDefaultRecipeId(result);
		save(output, ResourceLocation.fromNamespaceAndPath(this.namespace != null ? namespace : defaultLocation.getNamespace(), defaultLocation.getPath()).withPrefix("soaking/"));
	}

	@Override
	public void save(RecipeOutput output, ResourceLocation id) {
		SoakingRecipe recipe = new SoakingRecipe(
			this.ingredient,
			this.fluid,
			this.consumeFluid,
			this.resultStack,
			this.processingTime
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
