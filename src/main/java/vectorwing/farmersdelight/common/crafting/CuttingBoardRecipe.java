package vectorwing.farmersdelight.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class CuttingBoardRecipe implements Recipe<CuttingBoardRecipeInput>
{
	public static final MapCodec<CuttingBoardRecipe> CODEC = RecordCodecBuilder.mapCodec(
		inst -> inst.group(Codec.STRING.optionalFieldOf("group", "").forGetter(CuttingBoardRecipe::group),
				Ingredient.CODEC.listOf(1, 1).fieldOf("ingredients").flatXmap(ingredients -> {
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
								+ CuttingBoardRecipe.MAX_RESULTS);
					}
					NonNullList<ChanceResult> nonNullList = NonNullList.create();
					nonNullList.addAll(chanceResults);
					return DataResult.success(nonNullList);
				}, DataResult::success).forGetter(CuttingBoardRecipe::getRollableResults),
				SoundEvent.CODEC.optionalFieldOf("sound").forGetter(CuttingBoardRecipe::getSoundEvent))
			.apply(inst, CuttingBoardRecipe::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, CuttingBoardRecipe> STREAM_CODEC =
		StreamCodec.of(CodecHelpers::toNetwork, CodecHelpers::fromNetwork);

	public static final RecipeSerializer<CuttingBoardRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

	public static final int MAX_RESULTS = 4;

	private final String group;
	private final Ingredient input;
	private final Ingredient tool;
	private final NonNullList<ChanceResult> results;
	private final Optional<Holder<SoundEvent>> soundEvent;

	public CuttingBoardRecipe(String group, Ingredient input, Ingredient tool, NonNullList<ChanceResult> results, Optional<Holder<SoundEvent>> soundEvent) {
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
	public ItemStack assemble(CuttingBoardRecipeInput cuttingBoardRecipeInput) {
		return this.results.getFirst().stack().create();
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public boolean showNotification() {
		return false;
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

	public List<ItemStack> getResults() {
		return getRollableResults().stream()
				.map(ChanceResult::stack)
				.map(ItemStackTemplate::create)
				.collect(Collectors.toList());
	}

	public NonNullList<ChanceResult> getRollableResults() {
		return this.results;
	}

	public List<ItemStack> rollResults(RandomSource random, int fortuneLevel, RecipeInput inventory) {
		List<ItemStack> results = new ArrayList<>();
		NonNullList<ChanceResult> rollableResults = getRollableResults();
		for (ChanceResult output : rollableResults) {
			ItemStack stack = output.rollOutput(random, fortuneLevel);
			if (!stack.isEmpty())
				results.add(stack);
		}
		return results;
	}

	public Optional<Holder<SoundEvent>> getSoundEvent() {
		return this.soundEvent;
	}

	@Override
	public RecipeSerializer<? extends Recipe<CuttingBoardRecipeInput>> getSerializer() {
		return ModRecipeSerializers.CUTTING.get();
	}

	@Override
	public RecipeType<? extends Recipe<CuttingBoardRecipeInput>> getType() {
		return ModRecipeTypes.CUTTING.get();
	}

	@Override
	public PlacementInfo placementInfo() {
		return PlacementInfo.NOT_PLACEABLE; // Never relevant for us?
	}

	@Override
	public RecipeBookCategory recipeBookCategory() {
		return RecipeBookCategories.CRAFTING_MISC; // Never read for our purposes, AFAIK?
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CuttingBoardRecipe that = (CuttingBoardRecipe) o;

		if (!group().equals(that.group())) return false;
		if (!input.equals(that.input)) return false;
		if (!getTool().equals(that.getTool())) return false;
		if (!getResults().equals(that.getResults())) return false;
		return Objects.equals(soundEvent, that.soundEvent);
	}

	@Override
	public int hashCode() {
		int result = (group() != null ? group().hashCode() : 0);
		result = 31 * result + input.hashCode();
		result = 31 * result + getTool().hashCode();
		result = 31 * result + getResults().hashCode();
		result = 31 * result + (soundEvent.map(Object::hashCode).orElse(0));
		return result;
	}

	private static class CodecHelpers
	{
		public static CuttingBoardRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
			String group = buffer.readUtf(32767);
			Ingredient inputItem = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
			Ingredient tool = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);

			List<ChanceResult> results = ChanceResult.STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer);
			Optional<Holder<SoundEvent>> soundEvent = SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional).decode(buffer);

			return new CuttingBoardRecipe(group, inputItem, tool, NonNullList.copyOf(results), soundEvent);
		}

		public static void toNetwork(RegistryFriendlyByteBuf buffer, CuttingBoardRecipe recipe) {
			buffer.writeUtf(recipe.group);
			Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.input);
			Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.tool);
			ChanceResult.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, recipe.results);
			SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional).encode(buffer, recipe.soundEvent);
		}
	}
}
