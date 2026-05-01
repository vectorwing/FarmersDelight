package vectorwing.farmersdelight.common.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.FuelValues;

import javax.annotation.Nullable;

public class FuelBlockItem extends BlockItem
{
	public final int burnTime;

	public FuelBlockItem(Block block, Properties properties) {
		super(block, properties);
		this.burnTime = 100;
	}

	public FuelBlockItem(Block block, Properties properties, int burnTime) {
		super(block, properties);
		this.burnTime = burnTime;
	}

	// TODO: Decide if this class is obsolete due to datamaps.
	@Override
	public int getBurnTime(ItemStack stack, @Nullable RecipeType<?> recipeType, FuelValues fuelValues) {
		return this.burnTime;
	}
}
