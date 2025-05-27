package vectorwing.farmersdelight.common.item;

import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.utility.TextUtils;

public class RiceItem extends ItemNameBlockItem
{
	public RiceItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		InteractionResult result = this.place(new BlockPlaceContext(context));
		if (result.equals(InteractionResult.FAIL)) {
			Player player = context.getPlayer();
			BlockState targetState = context.getLevel().getBlockState(context.getClickedPos());
			if (player != null && context.getClickedFace().equals(Direction.UP) && (targetState.is(BlockTags.DIRT) || targetState.getBlock() instanceof FarmBlock)) {
				player.displayClientMessage(TextUtils.getTranslation("block.rice.invalid_placement"), true);
			}
		}
		return !result.consumesAction() ? this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult() : result;
	}
}
