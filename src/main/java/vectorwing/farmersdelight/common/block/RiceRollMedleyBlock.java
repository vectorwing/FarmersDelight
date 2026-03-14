package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class RiceRollMedleyBlock extends FeastBlock
{
	public static final IntegerProperty ROLL_SERVINGS = IntegerProperty.create("servings", 0, 8);

	protected static final VoxelShape SHAPE_NORTH_SOUTH = Shapes.or(
			Block.box(2.0D, 1.0D, 1.0D, 14.0D, 3.0D, 15.0D),
			Block.box(2.0D, 0.0D, 3.0D, 14.0D, 2.0D, 5.0D),
			Block.box(2.0D, 0.0D, 11.0D, 14.0D, 2.0D, 13.0D));
	protected static final VoxelShape SHAPE_EAST_WEST = Shapes.or(
			Block.box(1.0D, 1.0D, 2.0D, 15.0D, 3.0D, 14.0D),
			Block.box(3.0D, 0.0D, 2.0D, 5.0D, 2.0D, 14.0D),
			Block.box(11.0D, 0.0D, 2.0D, 13.0D, 2.0D, 14.0D));

	public final List<Supplier<Item>> riceRollServings = Arrays.asList(
			ModItems.COD_ROLL,
			ModItems.COD_ROLL,
			ModItems.SALMON_ROLL,
			ModItems.SALMON_ROLL,
			ModItems.SALMON_ROLL,
			ModItems.KELP_ROLL_SLICE,
			ModItems.KELP_ROLL_SLICE,
			ModItems.KELP_ROLL_SLICE
	);

	public RiceRollMedleyBlock(Properties properties) {
		super(properties, ModItems.SALMON_ROLL, true, false);
	}

	@Override
	public IntegerProperty getServingsProperty() {
		return ROLL_SERVINGS;
	}

	@Override
	public int getMaxServings() {
		return 8;
	}

	@Override
	public ItemStack getServingItem(BlockState state) {
		return new ItemStack(riceRollServings.get(state.getValue(getServingsProperty()) - 1).get());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (state.getValue(FeastBlock.FACING).getAxis().equals(Direction.Axis.X)) {
			return SHAPE_NORTH_SOUTH;
		}
		return SHAPE_EAST_WEST;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, ROLL_SERVINGS);
	}
}
