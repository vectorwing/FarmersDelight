package vectorwing.farmersdelight.items;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vectorwing.farmersdelight.utils.TextUtils;

import javax.annotation.Nullable;
import java.util.List;

public class DebugBlockItem extends BlockNamedItem
{
	private final boolean isFoil;

	/**
	 * Items that serve a low-level purpose, and are not meant to be obtained by players.
	 */
	public DebugBlockItem(Block pBlock, Properties pProperties) {
		super(pBlock, pProperties);
		this.isFoil = true;
	}

	public DebugBlockItem(Block pBlock, Properties pProperties, boolean isFoil) {
		super(pBlock, pProperties);
		this.isFoil = isFoil;
	}

	public boolean isFoil(ItemStack pStack) {
		return isFoil;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		IFormattableTextComponent textDescription = TextUtils.getTranslation("tooltip.debug_item");
		tooltip.add(textDescription.withStyle(TextFormatting.RED));
	}
}
