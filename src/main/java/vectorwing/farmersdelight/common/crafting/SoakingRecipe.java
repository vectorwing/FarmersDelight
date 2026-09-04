package vectorwing.farmersdelight.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import vectorwing.farmersdelight.common.crafting.input.SoakingRecipeInput;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class SoakingRecipe implements Recipe<SoakingRecipeInput>
{
	protected final Ingredient ingredient;
	protected final SizedFluidIngredient fluid;
	protected final boolean consumeFluid;
	protected final ItemStack result;
	private final int processingTime;

	public SoakingRecipe(Ingredient ingredient, SizedFluidIngredient fluid, boolean consumeFluid, ItemStack result, int processingTime) {
		this.ingredient = ingredient;
		this.fluid = fluid;
		this.consumeFluid = consumeFluid;
		this.result = result;
		this.processingTime = processingTime;
	}

	@Override
	public boolean matches(SoakingRecipeInput input, Level level) {
		return this.ingredient.test(input.getInput()) && this.fluid.test(input.getFluid());
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> nonnulllist = NonNullList.create();
		nonnulllist.add(this.ingredient);
		return nonnulllist;
	}

	public SizedFluidIngredient getFluid() {
		return this.fluid;
	}

	public int getProcessingTime() {
		return this.processingTime;
	}

	public boolean doesConsumeFluid() {
		return this.consumeFluid;
	}

	@Override
	public ItemStack assemble(SoakingRecipeInput input, HolderLookup.Provider registries) {
		return this.result.copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider registries) {
		return this.result;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.SOAKING.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ModRecipeTypes.SOAKING.get();
	}

	public static class Serializer implements RecipeSerializer<SoakingRecipe>
	{
		public static final StreamCodec<RegistryFriendlyByteBuf, SoakingRecipe> STREAM_CODEC =
			StreamCodec.of(SoakingRecipe.Serializer::toNetwork, SoakingRecipe.Serializer::fromNetwork);

		private static final MapCodec<SoakingRecipe> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
					Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
					SizedFluidIngredient.FLAT_CODEC.fieldOf("fluid").forGetter(recipe -> recipe.fluid),
					Codec.BOOL.optionalFieldOf("consume_fluid", true).forGetter(recipe -> recipe.consumeFluid),
					ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
					ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("processing_time", 0).forGetter(recipe -> recipe.processingTime)
				)
				.apply(instance, SoakingRecipe::new)
		);


		private static SoakingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
			Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
			SizedFluidIngredient fluid = SizedFluidIngredient.STREAM_CODEC.decode(buffer);
			boolean consumeIngredient = buffer.readBoolean();
			ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
			int processingTime = buffer.readVarInt();

			return new SoakingRecipe(ingredient, fluid, consumeIngredient, result, processingTime);
		}

		private static void toNetwork(RegistryFriendlyByteBuf buffer, SoakingRecipe recipe) {
			Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient);
			SizedFluidIngredient.STREAM_CODEC.encode(buffer, recipe.fluid);
			buffer.writeBoolean(recipe.consumeFluid);
			ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
			buffer.writeVarInt(recipe.processingTime);
		}

		@Override
		public MapCodec<SoakingRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, SoakingRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
