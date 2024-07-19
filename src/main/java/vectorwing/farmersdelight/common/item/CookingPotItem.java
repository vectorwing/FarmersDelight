package vectorwing.farmersdelight.common.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Block;
import vectorwing.farmersdelight.client.gui.CookingPotTooltip;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;

import java.util.Optional;

public class CookingPotItem extends BlockItem
{
	private static final int BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

	public CookingPotItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return getServingCount(stack) > 0;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return Math.min(1 + 12 * getServingCount(stack) / 64, 13);
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return BAR_COLOR;
	}

	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		ItemStack mealStack = CookingPotBlockEntity.getMealFromItem(stack);
		return Optional.of(new CookingPotTooltip.CookingPotTooltipComponent(mealStack));
	}

	private static int getServingCount(ItemStack stack) {
		ItemStack mealStack = CookingPotBlockEntity.getMealFromItem(stack);
		return mealStack.getCount();
	}
}
