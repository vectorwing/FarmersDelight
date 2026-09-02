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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import vectorwing.farmersdelight.common.crafting.input.FluidHandlingInput;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

/**
 * <p>This recipe defines a static fluid emptying process: an item provides a static amount of a fluid, and becomes another item.
 * <p>Example: 1 Milk Bottle -> 1 Glass Bottle + 250 mB of Milk.
 * <p>If possible, you should give the input a NeoForge fluid capability instead. This recipe is a failsafe for items without one.
 */
public class FluidEmptyingRecipe implements Recipe<FluidHandlingInput>
{
	protected final Ingredient filledInput;
	protected final ItemStack emptyResult;
	protected final FluidStack fluid;

	public FluidEmptyingRecipe(FluidStack fluid, Ingredient filledInput, ItemStack filledResult) {
		this.fluid = fluid;
		this.filledInput = filledInput;
		this.emptyResult = filledResult;
	}

	@Override
	public boolean matches(FluidHandlingInput input, Level level) {
		boolean canReceiveFluid = input.fluidTank().isEmpty() || FluidStack.isSameFluidSameComponents(fluid, input.getFluid());
		return filledInput.test(input.getInput()) && canReceiveFluid && fluid.getAmount() <= input.getSpaceInTank();
	}

	public FluidStack getFluid() {
		return this.fluid;
	}

	@Override
	public ItemStack assemble(FluidHandlingInput input, HolderLookup.Provider registries) {
		return emptyResult.copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider registries) {
		return emptyResult;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.FLUID_EMPTYING.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ModRecipeTypes.FLUID_EMPTYING.get();
	}

	public static class Serializer implements RecipeSerializer<FluidEmptyingRecipe>
	{
		public static final StreamCodec<RegistryFriendlyByteBuf, FluidEmptyingRecipe> STREAM_CODEC =
			StreamCodec.of(FluidEmptyingRecipe.Serializer::toNetwork, FluidEmptyingRecipe.Serializer::fromNetwork);

		private static final MapCodec<FluidEmptyingRecipe> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
					FluidStack.CODEC.fieldOf("fluid").forGetter(recipe -> recipe.fluid),
					Ingredient.CODEC_NONEMPTY.fieldOf("filled_input").forGetter(recipe -> recipe.filledInput),
					ItemStack.STRICT_CODEC.fieldOf("empty_result").forGetter(recipe -> recipe.emptyResult)
				)
				.apply(instance, FluidEmptyingRecipe::new)
		);

		private static FluidEmptyingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
			FluidStack fluid = FluidStack.STREAM_CODEC.decode(buffer);
			Ingredient filledInput = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
			ItemStack emptyResult = ItemStack.STREAM_CODEC.decode(buffer);

			return new FluidEmptyingRecipe(fluid, filledInput, emptyResult);
		}

		private static void toNetwork(RegistryFriendlyByteBuf buffer, FluidEmptyingRecipe recipe) {
			FluidStack.STREAM_CODEC.encode(buffer, recipe.fluid);
			Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.filledInput);
			ItemStack.STREAM_CODEC.encode(buffer, recipe.emptyResult);
		}

		@Override
		public MapCodec<FluidEmptyingRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, FluidEmptyingRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
