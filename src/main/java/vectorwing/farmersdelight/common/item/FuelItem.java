package vectorwing.farmersdelight.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.FuelValues;

import javax.annotation.Nullable;

public class FuelItem extends Item
{
	public final int burnTime;

	public FuelItem(Properties properties) {
		super(properties);
		this.burnTime = 100;
	}

	public FuelItem(Properties properties, int burnTime) {
		super(properties);
		this.burnTime = burnTime;
	}

	// TODO: Decide if this class is obsolete due to datamaps.
	@Override
	public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType, FuelValues fuelValues) {
		return this.burnTime;
	}
}
