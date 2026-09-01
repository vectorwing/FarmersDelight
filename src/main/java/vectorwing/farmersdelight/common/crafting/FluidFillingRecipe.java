package vectorwing.farmersdelight.common.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import vectorwing.farmersdelight.common.crafting.input.FluidHandlingInput;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

/**
 * <p>This recipe defines a static fluid filling process: an item is filled with a specific amount of fluid to become another item.
 * <p>Example: 1 Glass Bottle + 250 mB of Milk -> 1 Milk Bottle.
 * <p>If possible, you should give the input a NeoForge fluid capability instead. This recipe is a failsafe for items without one.
 */
public class FluidFillingRecipe implements Recipe<FluidHandlingInput>
{
	protected final SizedFluidIngredient fluid;
	protected final Ingredient emptyInput;
	protected final ItemStack filledResult;

	public FluidFillingRecipe(SizedFluidIngredient fluid, Ingredient emptyInput, ItemStack filledResult) {
		this.fluid = fluid;
		this.emptyInput = emptyInput;
		this.filledResult = filledResult;
	}

	@Override
	public boolean matches(FluidHandlingInput input, Level level) {
		return emptyInput.test(input.getInput()) && fluid.test(input.getFluidTank().getFluid());
	}

	public SizedFluidIngredient getFluid() {
		return this.fluid;
	}

	@Override
	public ItemStack assemble(FluidHandlingInput input, HolderLookup.Provider registries) {
		return filledResult.copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider registries) {
		return filledResult;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.FLUID_FILLING.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ModRecipeTypes.FLUID_FILLING.get();
	}

	public static class Serializer implements RecipeSerializer<FluidFillingRecipe>
	{
		public static final StreamCodec<RegistryFriendlyByteBuf, FluidFillingRecipe> STREAM_CODEC =
			StreamCodec.of(FluidFillingRecipe.Serializer::toNetwork, FluidFillingRecipe.Serializer::fromNetwork);

		private static final MapCodec<FluidFillingRecipe> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
					SizedFluidIngredient.FLAT_CODEC.fieldOf("fluid").forGetter(recipe -> recipe.fluid),
					Ingredient.CODEC_NONEMPTY.fieldOf("empty_input").forGetter(recipe -> recipe.emptyInput),
					ItemStack.STRICT_CODEC.fieldOf("filled_result").forGetter(recipe -> recipe.filledResult)
				)
				.apply(instance, FluidFillingRecipe::new)
		);

		private static FluidFillingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
			Ingredient emptyInput = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
			SizedFluidIngredient fluid = SizedFluidIngredient.STREAM_CODEC.decode(buffer);
			ItemStack filledResult = ItemStack.STREAM_CODEC.decode(buffer);

			return new FluidFillingRecipe(fluid, emptyInput, filledResult);
		}

		private static void toNetwork(RegistryFriendlyByteBuf buffer, FluidFillingRecipe recipe) {
			Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.emptyInput);
			SizedFluidIngredient.STREAM_CODEC.encode(buffer, recipe.fluid);
			ItemStack.STREAM_CODEC.encode(buffer, recipe.filledResult);
		}

		@Override
		public MapCodec<FluidFillingRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, FluidFillingRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
