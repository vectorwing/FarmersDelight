package vectorwing.farmersdelight.client.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;
import java.util.Optional;

public record CookingPotRecipeDisplay(List<SlotDisplay> ingredients, Optional<SlotDisplay> container, SlotDisplay result, SlotDisplay craftingStation, int duration, float experience) implements RecipeDisplay {
	public static final MapCodec<CookingPotRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		SlotDisplay.CODEC.listOf().fieldOf("ingredients").forGetter(CookingPotRecipeDisplay::ingredients),
		SlotDisplay.CODEC.optionalFieldOf("container").forGetter(CookingPotRecipeDisplay::container),
		SlotDisplay.CODEC.fieldOf("result").forGetter(CookingPotRecipeDisplay::result),
		SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(CookingPotRecipeDisplay::craftingStation),
		Codec.INT.fieldOf("duration").forGetter(CookingPotRecipeDisplay::duration),
		Codec.FLOAT.fieldOf("experience").forGetter(CookingPotRecipeDisplay::experience)
	).apply(instance, CookingPotRecipeDisplay::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, CookingPotRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
		SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()), CookingPotRecipeDisplay::ingredients,
		ByteBufCodecs.optional(SlotDisplay.STREAM_CODEC), CookingPotRecipeDisplay::container,
		SlotDisplay.STREAM_CODEC, CookingPotRecipeDisplay::result,
		SlotDisplay.STREAM_CODEC, CookingPotRecipeDisplay::craftingStation,
		ByteBufCodecs.INT, CookingPotRecipeDisplay::duration,
		ByteBufCodecs.FLOAT, CookingPotRecipeDisplay::experience,
		CookingPotRecipeDisplay::new
	);
	public static final Type<CookingPotRecipeDisplay> TYPE = new Type<>(MAP_CODEC, STREAM_CODEC);

	@Override
	public Type<CookingPotRecipeDisplay> type() {
		return TYPE;
	}

	@Override
	public boolean isEnabled(FeatureFlagSet enabledFeatures) {
		return this.ingredients.stream().allMatch(slotDisplay -> slotDisplay.isEnabled(enabledFeatures)) && RecipeDisplay.super.isEnabled(enabledFeatures);
	}
}