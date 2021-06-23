package vectorwing.farmersdelight.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.Tags;
import vectorwing.farmersdelight.utils.TextUtils;

public class RiceCropItem extends BlockNamedItem
{
	public RiceCropItem(Block blockIn, Properties properties) {
		super(blockIn, properties);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		ActionResultType actionresulttype = this.tryPlace(new BlockItemUseContext(context));
		if (actionresulttype.equals(ActionResultType.FAIL)) {
			PlayerEntity player = context.getPlayer();
			BlockState targetBlock = context.getWorld().getBlockState(context.getPos());
			if (player != null && context.getFace().equals(Direction.UP) && (targetBlock.isIn(Tags.Blocks.DIRT) || targetBlock.getBlock() instanceof FarmlandBlock)) {
				player.sendStatusMessage(TextUtils.getTranslation("block.rice.invalid_placement"), true);
			}
		}
		return !actionresulttype.isSuccessOrConsume() && this.isFood() ? this.onItemRightClick(context.getWorld(), context.getPlayer(), context.getHand()).getType() : actionresulttype;
	}
}
