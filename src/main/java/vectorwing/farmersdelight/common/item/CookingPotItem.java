package vectorwing.farmersdelight.common.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;

public class CookingPotItem extends BlockItem
{
	private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

	public CookingPotItem(Block block, Properties properties) {
		super(block, properties);
	}

	public boolean isBarVisible(ItemStack stack) {
		return getServingCount(stack) > 0;
	}

	public int getBarWidth(ItemStack stack) {
		return Math.min(1 + 12 * getServingCount(stack) / 64, 13);
	}

	public int getBarColor(ItemStack stack) {
		return BAR_COLOR;
	}

	private static int getServingCount(ItemStack stack) {
		CompoundTag nbt = stack.getTagElement("BlockEntityTag");
		if (nbt == null) {
			return 0;
		} else {
			ItemStack mealStack = CookingPotBlockEntity.getMealFromItem(stack);
			return mealStack.getCount();
		}
	}
}
