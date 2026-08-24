package vectorwing.farmersdelight.common.crafting.input;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public record SoakingRecipeInput(ItemStack item, FluidStack fluid) implements RecipeInput
{
	@Override
	public @NotNull ItemStack getItem(int index) {
		return getInput();
	}

	public ItemStack getInput() {
		return item;
	}

	public FluidStack getFluid() {
		return fluid;
	}

	@Override
	public int size() {
		return 1;
	}
}
