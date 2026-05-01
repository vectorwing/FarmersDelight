package vectorwing.farmersdelight.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.function.Consumer;

@SuppressWarnings("deprecation")
public class PlaceableItem extends BlockItem
{
	public PlaceableItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
		builder.accept(TextUtils.PLACEABLE);
	}
}
