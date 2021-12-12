package vectorwing.farmersdelight.common.item;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class FuelBlockItem extends BlockItem
{
	public final int burnTime;

	public FuelBlockItem(Block blockIn, Properties properties) {
		super(blockIn, properties);
		this.burnTime = 100;
	}

	public FuelBlockItem(Block blockIn, Properties properties, int burnTime) {
		super(blockIn, properties);
		this.burnTime = burnTime;
	}

	@Override
	public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
		return this.burnTime;
	}
}
