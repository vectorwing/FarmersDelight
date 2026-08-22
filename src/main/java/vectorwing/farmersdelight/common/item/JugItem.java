package vectorwing.farmersdelight.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import vectorwing.farmersdelight.common.block.entity.JugBlockEntity;
import vectorwing.farmersdelight.common.registry.ModDataComponents;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.List;

public class JugItem extends BlockItem
{
	private static final int DEFAULT_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

	public JugItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return getFluid(stack).getAmount() > 0;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		SimpleFluidContent fluid = getFluid(stack);
		return Math.min(1 + 12 * fluid.getAmount() / JugBlockEntity.JUG_CAPACITY, 13);
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return DEFAULT_COLOR;
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
		SimpleFluidContent fluid = getFluid(stack);
		if (!fluid.isEmpty()) {
			tooltipComponents.add(TextUtils.tooltip("jug.contains").withStyle(ChatFormatting.GRAY));
			tooltipComponents.add(TextUtils.tooltip("jug.fluid", fluid.getFluidType().getDescription(), fluid.getAmount()).withStyle(ChatFormatting.GRAY));
		} else {
			tooltipComponents.add(TextUtils.tooltip("jug.empty").withStyle(ChatFormatting.GRAY));
		}
	}

	private static SimpleFluidContent getFluid(ItemStack stack) {
		return stack.getOrDefault(ModDataComponents.FLUID_TANK.get(), SimpleFluidContent.EMPTY);
	}
}
