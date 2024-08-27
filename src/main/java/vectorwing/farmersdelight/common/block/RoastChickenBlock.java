package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class RoastChickenBlock extends FeastBlock
{
	protected static final VoxelShape PLATE_SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 2.0D, 15.0D);
	protected static final VoxelShape[] ROAST_SHAPES = new VoxelShape[] {
			Block.box(4.0D, 2.0D, 4.0D, 12.0D, 9.0D, 12.0D),
			Block.box(4, 2, 6, 12, 9, 12),
			Block.box(4, 2, 8, 12, 9, 12),
			Block.box(4, 2, 10, 12, 9, 12)
	};

	public RoastChickenBlock(Properties properties, Supplier<Item> servingItem, boolean hasLeftovers) {
		super(properties, servingItem, hasLeftovers);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return getPlatedServingShape(state, PLATE_SHAPE, ROAST_SHAPES);
	}
}
