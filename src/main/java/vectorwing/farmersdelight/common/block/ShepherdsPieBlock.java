package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class ShepherdsPieBlock extends FeastBlock
{
	protected static final VoxelShape PLATE_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D);
	protected static final VoxelShape[] PIE_SHAPES = new VoxelShape[] {
			Block.box(2, 2, 2, 14, 8, 14),
			Shapes.join(Block.box(8, 2, 2, 14, 8, 8), Block.box(2, 2, 8, 14, 8, 14), BooleanOp.OR),
			Block.box(2, 2, 8, 14, 8, 14),
			Block.box(2, 2, 8, 8, 8, 14)
	};

	public ShepherdsPieBlock(Properties properties, Supplier<Item> servingItem, boolean hasLeftovers) {
		super(properties, servingItem, hasLeftovers);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return getPlatedServingShape(state, PLATE_SHAPE, PIE_SHAPES);
	}
}
