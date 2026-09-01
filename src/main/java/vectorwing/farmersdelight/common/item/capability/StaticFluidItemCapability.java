package vectorwing.farmersdelight.common.item.capability;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import vectorwing.farmersdelight.common.crafting.FluidFillingRecipe;

/**
 * This capability allows an ItemStack to hold a speficic amount of a specific fluid, working similarly to a fluid-holding bucket.
 *
 * <p>When emptied, it swaps for an empty form. The empty form, however, won't be able to convert back into this
 * fluid without a capability of its own, or a {@link FluidFillingRecipe}.</p>
 */
public class StaticFluidItemCapability implements IFluidHandlerItem
{
	protected ItemStack container;
	protected Item emptyItem;
	protected FluidStack fluid;
	protected int capacity;

	public StaticFluidItemCapability(Fluid fluid, ItemStack container, Item emptyItem, int capacity) {
		this.container = container;
		this.emptyItem = emptyItem;
		this.fluid = new FluidStack(fluid, capacity);
		this.capacity = capacity;
	}

	@Override
	public ItemStack getContainer() {
		return container;
	}

	@Override
	public int getTanks() {
		return 1;
	}

	@Override
	public FluidStack getFluidInTank(int tank) {
		return fluid;
	}

	@Override
	public int getTankCapacity(int tank) {
		return capacity;
	}

	@Override
	public boolean isFluidValid(int tank, FluidStack stack) {
		return true;
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		return 0;
	}

	@Override
	public FluidStack drain(FluidStack resource, FluidAction action) {
		if (resource.getAmount() < capacity) {
			return FluidStack.EMPTY;
		}

		if (!fluid.isEmpty() && FluidStack.isSameFluidSameComponents(fluid, resource)) {
			if (action.execute()) {
				setFluid(FluidStack.EMPTY);
			}
			return fluid;
		}

		return FluidStack.EMPTY;
	}

	@Override
	public FluidStack drain(int maxDrain, FluidAction action) {
		if (maxDrain < capacity) {
			return FluidStack.EMPTY;
		}

		if (!fluid.isEmpty()) {
			if (action.execute()) {
				setFluid(FluidStack.EMPTY);
			}
			return fluid;
		}

		return FluidStack.EMPTY;
	}

	protected void setFluid(FluidStack fluidStack) {
		if (fluidStack.isEmpty()) {
			container = new ItemStack(emptyItem);
		}
	}
}
