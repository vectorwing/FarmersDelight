package vectorwing.farmersdelight.items;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraftforge.common.Tags;
import vectorwing.farmersdelight.utils.TextUtils;

import net.minecraft.world.item.Item.Properties;

public class RiceCropItem extends ItemNameBlockItem
{
	public RiceCropItem(Block blockIn, Properties properties) {
		super(blockIn, properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		InteractionResult result = this.place(new BlockPlaceContext(context));
		if (result.equals(InteractionResult.FAIL)) {
			Player player = context.getPlayer();
			BlockState targetState = context.getLevel().getBlockState(context.getClickedPos());
			if (player != null && context.getClickedFace().equals(Direction.UP) && (targetState.is(Tags.Blocks.DIRT) || targetState.getBlock() instanceof FarmBlock)) {
				player.displayClientMessage(TextUtils.getTranslation("block.rice.invalid_placement"), true);
			}
		}
		return !result.consumesAction() && this.isEdible() ? this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult() : result;
	}
}
