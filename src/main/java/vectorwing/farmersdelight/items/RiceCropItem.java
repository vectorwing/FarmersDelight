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

import net.minecraft.item.Item.Properties;

public class RiceCropItem extends BlockNamedItem
{
	public RiceCropItem(Block blockIn, Properties properties) {
		super(blockIn, properties);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		ActionResultType result = this.place(new BlockItemUseContext(context));
		if (result.equals(ActionResultType.FAIL)) {
			PlayerEntity player = context.getPlayer();
			BlockState targetState = context.getLevel().getBlockState(context.getClickedPos());
			if (player != null && context.getClickedFace().equals(Direction.UP) && (targetState.is(Tags.Blocks.DIRT) || targetState.getBlock() instanceof FarmlandBlock)) {
				player.displayClientMessage(TextUtils.getTranslation("block.rice.invalid_placement"), true);
			}
		}
		return !result.consumesAction() && this.isEdible() ? this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult() : result;
	}
}
