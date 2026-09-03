package vectorwing.farmersdelight.common.utility;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import vectorwing.farmersdelight.common.crafting.FluidEmptyingRecipe;
import vectorwing.farmersdelight.common.crafting.FluidFillingRecipe;
import vectorwing.farmersdelight.common.crafting.input.FluidHandlingInput;

import java.util.Optional;

public class FluidHandlingUtils
{
	public static final int MB_BOTTLE = 250;
	public static final int MB_BUCKET = 1000;

	public static int getBucketAmount(int millibuckets) {
		return millibuckets / MB_BUCKET;
	}

	public static int getBottleAmount(int millibuckets) {
		return millibuckets / 250;
	}

	/**
	 * Simulates emptying the input stack inside the fluid tank, returning the extracted fluid and remainder item.
	 * <br>No changes are applied to the inputs; these are handled by the method caller based on the return.
	 * <p>This method asks for a CachedCheck instance, expecting the caller to cache the recipe locally.
	 */
	public static Pair<FluidStack, ItemStack> testEmptyingInput(ItemStack inputStack, FluidTank fluidTank, Level level, RecipeManager.CachedCheck<FluidHandlingInput, FluidEmptyingRecipe> emptyingCache) {
		FluidStack resultFluid = FluidStack.EMPTY;
		ItemStack resultItem = ItemStack.EMPTY;

		ItemStack stackCopy = inputStack.copyWithCount(1);

		// Check emptying recipe
		FluidHandlingInput input = new FluidHandlingInput(inputStack, fluidTank);
		Optional<RecipeHolder<FluidEmptyingRecipe>> recipe = emptyingCache.getRecipeFor(input, level);
		if (recipe.isPresent()) {
			FluidEmptyingRecipe emptyingRecipe = recipe.get().value();
			resultItem = emptyingRecipe.assemble(input, level.registryAccess());
			resultFluid = emptyingRecipe.getFluid();
			return Pair.of(resultFluid, resultItem);
		}

		// Check fluid capability
		IFluidHandlerItem fluidHandler = stackCopy.getCapability(Capabilities.FluidHandler.ITEM);
		if (fluidHandler == null) return Pair.of(resultFluid, resultItem);
		FluidStack transfer = FluidUtil.tryFluidTransfer(fluidTank, fluidHandler, fluidTank.getSpace(), false);
		if (!transfer.isEmpty()) {
			fluidHandler.drain(transfer, IFluidHandler.FluidAction.EXECUTE);
			resultFluid = transfer;
			resultItem = fluidHandler.getContainer();
		}

		return Pair.of(resultFluid, resultItem);
	}

	/**
	 * Simulates filling the input stack with the fluid tank's contents, returning the extracted fluid and remainder item.
	 * <br>No changes are applied to the inputs; these are handled by the method caller based on the return.
	 * <p>This method asks for a CachedCheck instance, expecting the caller to cache the recipe locally.
	 */
	public static Pair<Integer, ItemStack> testFillingInput(ItemStack inputStack, FluidTank fluidTank, Level level, RecipeManager.CachedCheck<FluidHandlingInput, FluidFillingRecipe> fillingCache) {
		int resultFluid = 0;
		ItemStack resultItem = ItemStack.EMPTY;

		ItemStack stackCopy = inputStack.copyWithCount(1);

		// Check emptying recipe
		FluidHandlingInput input = new FluidHandlingInput(inputStack, fluidTank);
		Optional<RecipeHolder<FluidFillingRecipe>> recipe = fillingCache.getRecipeFor(input, level);
		if (recipe.isPresent()) {
			FluidFillingRecipe emptyingRecipe = recipe.get().value();
			resultItem = emptyingRecipe.assemble(input, level.registryAccess());
			resultFluid = emptyingRecipe.getFluid().amount();
			return Pair.of(resultFluid, resultItem);
		}

		// Check fluid capability
		IFluidHandlerItem fluidHandler = stackCopy.getCapability(Capabilities.FluidHandler.ITEM);
		if (fluidHandler == null) return Pair.of(resultFluid, resultItem);
		FluidStack transfer = FluidUtil.tryFluidTransfer(fluidHandler, fluidTank, fluidTank.getFluidAmount(), false);
		if (!transfer.isEmpty()) {
			fluidHandler.fill(transfer, IFluidHandler.FluidAction.EXECUTE);
			resultFluid = transfer.getAmount();
			resultItem = fluidHandler.getContainer();
		}

		return Pair.of(resultFluid, resultItem);
	}
}
