package vectorwing.farmersdelight.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.registry.ModRecipeBookCategories;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class CuttingBoardRecipe implements Recipe<CuttingBoardRecipeInput>
{
	public static final int MAX_RESULTS = 4;

	private final String group;
	private final Ingredient input;
	private final Ingredient tool;
	private final NonNullList<ChanceResult> results;
	private final Optional<SoundEvent> soundEvent;

	public CuttingBoardRecipe(String group, Ingredient input, Ingredient tool, NonNullList<ChanceResult> results, Optional<SoundEvent> soundEvent) {
		this.group = group;
		this.input = input;
		this.tool = tool;
		this.results = results;
		this.soundEvent = soundEvent;
	}

	@Override
	public boolean matches(CuttingBoardRecipeInput input, Level level) {
		return this.input.test(input.item()) && this.tool.test(input.tool());
	}

	@Override
	public ItemStack assemble(CuttingBoardRecipeInput inv) {
		return this.results.getFirst().stack().create().copy();
	}

	@Override
	public boolean showNotification() {
		return false;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	public String getGroup() {
		return this.group;
	}

	@Override
	public String group() {
		return this.group;
	}

	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> nonnulllist = NonNullList.create();
		nonnulllist.add(this.input);
		return nonnulllist;
	}

	public Ingredient getTool() {
		return this.tool;
	}

	public ItemStack getResultItem(HolderLookup.Provider provider) {
		return this.results.getFirst().stack().create();
	}

	public List<ItemStack> getResults() {
		return getRollableResults().stream()
				.map(result -> result.stack().create())
				.collect(Collectors.toList());
	}

	public NonNullList<ChanceResult> getRollableResults() {
		return this.results;
	}

	public List<ItemStack> rollResults(RandomSource random, int fortuneLevel, RecipeWrapper inventory) {
		List<ItemStack> results = new ArrayList<>();
		NonNullList<ChanceResult> rollableResults = getRollableResults();
		for (ChanceResult output : rollableResults) {
			ItemStack stack = output.rollOutput(random, fortuneLevel);
			if (!stack.isEmpty())
				results.add(stack);
		}
		return results;
	}

	public Optional<SoundEvent> getSoundEvent() {
		return this.soundEvent;
	}

	protected int getMaxInputCount() {
		return 1;
	}

	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= this.getMaxInputCount();
	}

	@Override
	public RecipeSerializer<CuttingBoardRecipe> getSerializer() {
		return ModRecipeSerializers.CUTTING.get();
	}

	@Override
	public RecipeType<CuttingBoardRecipe> getType() {
		return ModRecipeTypes.CUTTING.get();
	}

	@Override
	public PlacementInfo placementInfo() {
		return PlacementInfo.NOT_PLACEABLE;
	}

	@Override
	public RecipeBookCategory recipeBookCategory() {
		return ModRecipeBookCategories.CUTTING.get();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CuttingBoardRecipe that = (CuttingBoardRecipe) o;

		if (!getGroup().equals(that.getGroup())) return false;
		if (!input.equals(that.input)) return false;
		if (!getTool().equals(that.getTool())) return false;
		if (!getResults().equals(that.getResults())) return false;
		return Objects.equals(soundEvent, that.soundEvent);
	}

	@Override
	public int hashCode() {
		int result = (getGroup() != null ? getGroup().hashCode() : 0);
		result = 31 * result + input.hashCode();
		result = 31 * result + getTool().hashCode();
		result = 31 * result + getResults().hashCode();
		result = 31 * result + (soundEvent.map(Object::hashCode).orElse(0));
		return result;
	}

	public static class Serializer
	{
		public static final StreamCodec<RegistryFriendlyByteBuf, CuttingBoardRecipe> STREAM_CODEC =
				StreamCodec.of(CuttingBoardRecipe.Serializer::toNetwork, CuttingBoardRecipe.Serializer::fromNetwork);

		private static final MapCodec<CuttingBoardRecipe> CODEC = RecordCodecBuilder.mapCodec(
				inst -> inst.group(Codec.STRING.optionalFieldOf("group", "").forGetter(CuttingBoardRecipe::getGroup),
								Ingredient.CODEC.listOf().fieldOf("ingredients").flatXmap(ingredients -> {
									if (ingredients.isEmpty()) {
										return DataResult.error(() -> "No ingredients for cutting recipe");
									}
									if (ingredients.size() > 1) {
										return DataResult.error(
												() -> "Too many ingredients for cutting recipe! Please define only one ingredient");
									}
									NonNullList<Ingredient> nonNullList = NonNullList.create();
									nonNullList.add(ingredients.get(0));
									return DataResult.success(ingredients.get(0));
								}, ingredient -> {
									NonNullList<Ingredient> nonNullList = NonNullList.create();
									nonNullList.add(ingredient);
									return DataResult.success(nonNullList);
								}).forGetter(cuttingBoardRecipe -> cuttingBoardRecipe.input),
								Ingredient.CODEC.fieldOf("tool").forGetter(CuttingBoardRecipe::getTool),
								Codec.list(ChanceResult.CODEC).fieldOf("result").flatXmap(chanceResults -> {
									if (chanceResults.size() > 4) {
										return DataResult.error(
												() -> "Too many results for cutting recipe! The maximum quantity of unique results is "
														+ MAX_RESULTS);
									}
									NonNullList<ChanceResult> nonNullList = NonNullList.create();
									nonNullList.addAll(chanceResults);
									return DataResult.success(nonNullList);
								}, DataResult::success).forGetter(CuttingBoardRecipe::getRollableResults),
								SoundEvent.DIRECT_CODEC.optionalFieldOf("sound").forGetter(CuttingBoardRecipe::getSoundEvent))
						.apply(inst, CuttingBoardRecipe::new));

		public Serializer() {
		}

		public static CuttingBoardRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
			String group = buffer.readUtf(32767);
			Ingredient inputItem = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
			Ingredient tool = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);

			List<ChanceResult> decodedResults = ChanceResult.STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer);
			NonNullList<ChanceResult> results = NonNullList.create();
			results.addAll(decodedResults);
			Optional<SoundEvent> soundEvent = Optional.empty();
			if (buffer.readBoolean()) {
				soundEvent = Optional.of(SoundEvent.DIRECT_STREAM_CODEC.decode(buffer));
			}

			return new CuttingBoardRecipe(group, inputItem, tool, results, soundEvent);
		}

		public static void toNetwork(RegistryFriendlyByteBuf buffer, CuttingBoardRecipe recipe) {
			buffer.writeUtf(recipe.group);
			Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.input);
			Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.tool);
			ChanceResult.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, recipe.results);
			if (recipe.getSoundEvent().isPresent()) {
				buffer.writeBoolean(true);
				SoundEvent.DIRECT_STREAM_CODEC.encode(buffer, recipe.getSoundEvent().get());
			} else {
				buffer.writeBoolean(false);
			}
		}

		public static MapCodec<CuttingBoardRecipe> codec() {
			return CODEC;
		}

		public static StreamCodec<RegistryFriendlyByteBuf, CuttingBoardRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
