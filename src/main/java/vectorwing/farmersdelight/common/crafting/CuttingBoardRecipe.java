package vectorwing.farmersdelight.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import net.neoforged.neoforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.crafting.ingredient.ChanceResult;
import vectorwing.farmersdelight.common.registry.ModRecipeSerializers;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("ClassCanBeRecord")
public class CuttingBoardRecipe implements Recipe<RecipeWrapper>
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
	public boolean isSpecial() {
		return true;
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> nonnulllist = NonNullList.create();
		nonnulllist.add(this.input);
		return nonnulllist;
	}

	public Ingredient getTool() {
		return this.tool;
	}

	@Override
	public ItemStack assemble(RecipeWrapper inv, RegistryAccess access) {
		return this.results.get(0).getStack().copy();
	}

	@Override
	public ItemStack getResultItem(RegistryAccess access) {
		return this.results.get(0).getStack();
	}

	public List<ItemStack> getResults() {
		return getRollableResults().stream()
				.map(ChanceResult::getStack)
				.collect(Collectors.toList());
	}

	public NonNullList<ChanceResult> getRollableResults() {
		return this.results;
	}

	public List<ItemStack> rollResults(RandomSource rand, int fortuneLevel) {
		List<ItemStack> results = new ArrayList<>();
		NonNullList<ChanceResult> rollableResults = getRollableResults();
		for (ChanceResult output : rollableResults) {
			ItemStack stack = output.rollOutput(rand, fortuneLevel);
			if (!stack.isEmpty())
				results.add(stack);
		}
		return results;
	}

	public Optional<SoundEvent> getSoundEvent() {
		return this.soundEvent;
	}

	@Override
	public boolean matches(RecipeWrapper inv, Level level) {
		if (inv.isEmpty())
			return false;
		return input.test(inv.getItem(0));
	}

	protected int getMaxInputCount() {
		return 1;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= this.getMaxInputCount();
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.CUTTING.get();
	}

	@Override
	public RecipeType<?> getType() {
		return ModRecipeTypes.CUTTING.get();
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

	public static class Serializer implements RecipeSerializer<CuttingBoardRecipe>
	{
		private static final Codec<CuttingBoardRecipe> CODEC = RecordCodecBuilder.create(inst -> inst.group(
				ExtraCodecs.strictOptionalField(Codec.STRING, "group", "").forGetter(CuttingBoardRecipe::getGroup),
				Ingredient.LIST_CODEC_NONEMPTY.fieldOf("ingredients").flatXmap(ingredients -> {
					if (ingredients.isEmpty()) {
						return DataResult.error(() -> "No ingredients for cutting recipe");
					}
					if (ingredients.size() > 1) {
						return DataResult.error(() -> "Too many ingredients for cutting recipe! Please define only one ingredient");
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
						return DataResult.error(() -> "Too many results for cutting recipe! The maximum quantity of unique results is " + MAX_RESULTS);
					}
					NonNullList<ChanceResult> nonNullList = NonNullList.create();
					nonNullList.addAll(chanceResults);
					return DataResult.success(nonNullList);
				}, DataResult::success).forGetter(CuttingBoardRecipe::getRollableResults),
				ExtraCodecs.strictOptionalField(ForgeRegistries.SOUND_EVENTS.getCodec(), "sound").forGetter(CuttingBoardRecipe::getSoundEvent)
		).apply(inst, CuttingBoardRecipe::new));

		public Serializer() {
		}

		@Override
		public Codec<CuttingBoardRecipe> codec() {
			return CODEC;
		}

		@Nullable
		@Override
		public CuttingBoardRecipe fromNetwork(FriendlyByteBuf buffer) {
			String groupIn = buffer.readUtf(32767);
			Ingredient inputItemIn = Ingredient.fromNetwork(buffer);
			Ingredient toolIn = Ingredient.fromNetwork(buffer);

			int i = buffer.readVarInt();
			NonNullList<ChanceResult> resultsIn = NonNullList.withSize(i, ChanceResult.EMPTY);
			for (int j = 0; j < resultsIn.size(); ++j) {
				resultsIn.set(j, ChanceResult.read(buffer));
			}
			Optional<SoundEvent> soundEventIn = Optional.empty();
			if (buffer.readBoolean()) {
				Optional<Holder<SoundEvent>> holder = ForgeRegistries.SOUND_EVENTS.getHolder(buffer.readResourceKey(ForgeRegistries.Keys.SOUND_EVENTS));
				if (holder.isPresent() && holder.get().isBound()) {
					soundEventIn = Optional.of(holder.get().value());
				}
			}

			return new CuttingBoardRecipe(groupIn, inputItemIn, toolIn, resultsIn, soundEventIn);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, CuttingBoardRecipe recipe) {
			buffer.writeUtf(recipe.group);
			recipe.input.toNetwork(buffer);
			recipe.tool.toNetwork(buffer);
			buffer.writeVarInt(recipe.results.size());
			for (ChanceResult result : recipe.results) {
				result.write(buffer);
			}
			if (recipe.getSoundEvent().isPresent()) {
				Optional<ResourceKey<SoundEvent>> resourceKey = ForgeRegistries.SOUND_EVENTS.getResourceKey(recipe.getSoundEvent().get());
                resourceKey.ifPresentOrElse(rk -> {
					buffer.writeBoolean(true);
					buffer.writeResourceKey(rk);
				}, () -> buffer.writeBoolean(false));
			} else {
				buffer.writeBoolean(false);
			}
		}
	}
}
