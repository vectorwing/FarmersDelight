package vectorwing.farmersdelight.common.crafting.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import vectorwing.farmersdelight.common.registry.ModIngredientTypes;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.stream.Stream;

/**
 * Ingredient that checks if the given stack can perform a ItemAbility from Forge.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record ItemAbilityIngredient(ItemAbility itemAbility) implements ICustomIngredient
{
	public static final Codec<ItemAbilityIngredient> CODEC = RecordCodecBuilder.create(inst ->
			inst.group(ItemAbility.CODEC.fieldOf("action").forGetter(ItemAbilityIngredient::getItemAbility)
			).apply(inst, ItemAbilityIngredient::new));

	@Override
	public boolean test(@Nullable ItemStack stack) {
		return stack != null && stack.canPerformAction(itemAbility);
	}

	@Override
	public Stream<ItemStack> getItems() {
		return Stream.empty();
	}

	@Override
	public boolean isSimple() {
		return false;
	}

	public ItemAbility getItemAbility() {
		return itemAbility;
	}

	public IngredientType<?> getType() {
		return ModIngredientTypes.ITEM_ABILITY_INGREDIENT.get();
	}
}
