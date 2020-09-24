package vectorwing.farmersdelight.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;

public class TatamiBlock extends Block {
	public static final DirectionProperty FACING = BlockStateProperties.FACING;

	public TatamiBlock() {
		super(Block.Properties.from(Blocks.HAY_BLOCK).sound(SoundType.CLOTH));
		this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.DOWN));
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		Direction direction = context.getFace().getOpposite();
		if (context.getPlayer() != null && context.getPlayer().isSecondaryUseActive()){
			direction = context.getFace();
		}
		return this.getDefaultState().with(FACING, direction);
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
