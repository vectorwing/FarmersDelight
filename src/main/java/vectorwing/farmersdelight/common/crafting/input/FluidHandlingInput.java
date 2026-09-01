package vectorwing.farmersdelight.common.crafting.input;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

/**
 * Provides an input ItemStack and a fluid tank for fluid-handling recipes.
 */
public record FluidHandlingInput(ItemStack inputStack, FluidTank fluidTank) implements RecipeInput
{
	@Override
	public @NotNull ItemStack getItem(int index) {
		return getInput();
	}

	public ItemStack getInput() {
		return inputStack;
	}

	public FluidStack getFluid() {
		return fluidTank.getFluid();
	}

	public FluidTank getFluidTank() {
		return fluidTank;
	}

	public int getSpaceInTank() {
		return fluidTank().getSpace();
	}

	@Override
	public int size() {
		return 1;
	}
}
