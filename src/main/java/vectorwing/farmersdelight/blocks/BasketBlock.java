package vectorwing.farmersdelight.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BasketBlock extends Block
{
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	public static final VoxelShape OUT_SHAPE = VoxelShapes.fullCube();
	public static final VoxelShape[] SHAPES_FACING = new VoxelShape[] {
		VoxelShapes.combineAndSimplify(OUT_SHAPE, Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D), IBooleanFunction.ONLY_FIRST), // down
		VoxelShapes.combineAndSimplify(OUT_SHAPE, Block.makeCuboidShape(2.0D, 2.0D, 2.0D, 14.0D, 16.0D, 14.0D), IBooleanFunction.ONLY_FIRST), // up
		VoxelShapes.combineAndSimplify(OUT_SHAPE, Block.makeCuboidShape(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 14.0D), IBooleanFunction.ONLY_FIRST), // north
		VoxelShapes.combineAndSimplify(OUT_SHAPE, Block.makeCuboidShape(2.0D, 2.0D, 2.0D, 14.0D, 14.0D, 16.0D), IBooleanFunction.ONLY_FIRST), // south
		VoxelShapes.combineAndSimplify(OUT_SHAPE, Block.makeCuboidShape(0.0D, 2.0D, 0.0D, 14.0D, 14.0D, 14.0D), IBooleanFunction.ONLY_FIRST), // west
		VoxelShapes.combineAndSimplify(OUT_SHAPE, Block.makeCuboidShape(2.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D), IBooleanFunction.ONLY_FIRST)  // east
	};

	public static final Map<Direction, VoxelShape> SHAPE_FACING = mapVoxelShapes();

	private static Map<Direction, VoxelShape> mapVoxelShapes() {
		Map<Direction, VoxelShape> result = new HashMap<>();
		result.put(Direction.DOWN, VoxelShapes.combineAndSimplify(OUT_SHAPE, Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D), IBooleanFunction.ONLY_FIRST));
		result.put(Direction.UP, VoxelShapes.combineAndSimplify(OUT_SHAPE, Block.makeCuboidShape(2.0D, 2.0D, 2.0D, 14.0D, 16.0D, 14.0D), IBooleanFunction.ONLY_FIRST));
		result.put(Direction.NORTH, VoxelShapes.combineAndSimplify(OUT_SHAPE, Block.makeCuboidShape(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 14.0D), IBooleanFunction.ONLY_FIRST));
		result.put(Direction.SOUTH, VoxelShapes.combineAndSimplify(OUT_SHAPE, Block.makeCuboidShape(2.0D, 2.0D, 2.0D, 14.0D, 14.0D, 16.0D), IBooleanFunction.ONLY_FIRST));
		result.put(Direction.WEST, VoxelShapes.combineAndSimplify(OUT_SHAPE, Block.makeCuboidShape(0.0D, 2.0D, 2.0D, 14.0D, 14.0D, 14.0D), IBooleanFunction.ONLY_FIRST));
		result.put(Direction.EAST, VoxelShapes.combineAndSimplify(OUT_SHAPE, Block.makeCuboidShape(2.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D), IBooleanFunction.ONLY_FIRST));
		return result;
	}

	public BasketBlock()
	{
		super(Properties.create(Material.WOOD).hardnessAndResistance(0.6F).sound(SoundType.WOOD));
	}

	public BasketBlock(Block.Properties properties) {
		super(properties);
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE_FACING.get(state.get(FACING));
	}

	public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return OUT_SHAPE;
	}

	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE_FACING.get(state.get(FACING));
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}
}
