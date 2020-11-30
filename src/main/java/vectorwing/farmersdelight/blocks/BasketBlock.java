package vectorwing.farmersdelight.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import vectorwing.farmersdelight.tile.BasketTileEntity;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class BasketBlock extends ContainerBlock implements IWaterLoggable {
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final VoxelShape OUT_SHAPE = VoxelShapes.fullCube();
	@SuppressWarnings("UnstableApiUsage")
	public static final ImmutableMap<Direction, VoxelShape> SHAPE_FACING =
			Maps.immutableEnumMap(ImmutableMap.<Direction, VoxelShape>builder()
					.put(Direction.DOWN, cutout(
							makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D),
							makeCuboidShape(6.0D, 3.0D, 14.0D, 10.0D, 5.0D, 16.0D),
							makeCuboidShape(14.0D, 3.0D, 6.0D, 16.0D, 5.0D, 10.0D),
							makeCuboidShape(6.0D, 3.0D, 0.0D, 10.0D, 5.0D, 2.0D),
							makeCuboidShape(0.0D, 3.0D, 6.0D, 2.0D, 5.0D, 10.0D)
					))
					.put(Direction.UP, cutout(
							makeCuboidShape(2.0D, 2.0D, 2.0D, 14.0D, 16.0D, 14.0D),
							makeCuboidShape(6.0D, 11.0D, 0.0D, 10.0D, 13.0D, 2.0D),
							makeCuboidShape(14.0D, 11.0D, 6.0D, 16.0D, 13.0D, 10.0D),
							makeCuboidShape(6.0D, 11.0D, 14.0D, 10.0D, 13.0D, 16.0D),
							makeCuboidShape(0.0D, 11.0D, 6.0D, 2.0D, 13.0D, 10.0D)
					))
					.put(Direction.NORTH, cutout(
							makeCuboidShape(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 14.0D),
							makeCuboidShape(6.0D, 0.0D, 3.0D, 10.0D, 2.0D, 5.0D),
							makeCuboidShape(14.0D, 6.0D, 3.0D, 16.0D, 10.0D, 5.0D),
							makeCuboidShape(6.0D, 14.0D, 3.0D, 10.0D, 16.0D, 5.0D),
							makeCuboidShape(0.0D, 6.0D, 3.0D, 2.0D, 10.0D, 5.0D)
					))
					.put(Direction.SOUTH, cutout(
							makeCuboidShape(2.0D, 2.0D, 2.0D, 14.0D, 14.0D, 16.0D),
							makeCuboidShape(6.0D, 14.0D, 11.0D, 10.0D, 16.0D, 13.0D),
							makeCuboidShape(14.0D, 6.0D, 11.0D, 16.0D, 10.0D, 13.0D),
							makeCuboidShape(6.0D, 0.0D, 11.0D, 10.0D, 2.0D, 13.0D),
							makeCuboidShape(0.0D, 6.0D, 11.0D, 2.0D, 10.0D, 13.0D)
					))
					.put(Direction.WEST, cutout(
							makeCuboidShape(0.0D, 2.0D, 2.0D, 14.0D, 14.0D, 14.0D),
							makeCuboidShape(3.0D, 14.0D, 6.0D, 5.0D, 16.0D, 10.0D),
							makeCuboidShape(3.0D, 6.0D, 14.0D, 5.0D, 10.0D, 16.0D),
							makeCuboidShape(3.0D, 0.0D, 6.0D, 5.0D, 2.0D, 10.0D),
							makeCuboidShape(3.0D, 6.0D, 0.0D, 5.0D, 10.0D, 2.0D)
					))
					.put(Direction.EAST, cutout(
							makeCuboidShape(2.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D),
							makeCuboidShape(11.0D, 14.0D, 6.0D, 13.0D, 16.0D, 10.0D),
							makeCuboidShape(11.0D, 6.0D, 0.0D, 13.0D, 10.0D, 2.0D),
							makeCuboidShape(11.0D, 0.0D, 6.0D, 13.0D, 2.0D, 10.0D),
							makeCuboidShape(11.0D, 6.0D, 14.0D, 13.0D, 10.0D, 16.0D)
					))
					.build());

	public BasketBlock() {
		super(Properties.create(Material.WOOD).hardnessAndResistance(1.5F).sound(SoundType.WOOD));
		this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.UP).with(WATERLOGGED, false));
	}

	private static VoxelShape cutout(VoxelShape... cutouts) {
		VoxelShape shape = OUT_SHAPE;
		for (VoxelShape cutout : cutouts) {
			shape = VoxelShapes.combine(shape, cutout, IBooleanFunction.ONLY_FIRST);
		}
		return shape.simplify();
	}

	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, ENABLED, WATERLOGGED);
	}

	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (!worldIn.isRemote) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof BasketTileEntity) {
				player.openContainer((BasketTileEntity) tileentity);
			}
		}
		return ActionResultType.SUCCESS;
	}

	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof IInventory) {
				InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
				worldIn.updateComparatorOutputLevel(pos, this);
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	// --- HOPPER STUFF ---

	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		this.updateState(worldIn, pos, state);
	}

	private void updateState(World worldIn, BlockPos pos, BlockState state) {
		boolean flag = !worldIn.isBlockPowered(pos);
		if (flag != state.get(ENABLED)) {
			worldIn.setBlockState(pos, state.with(ENABLED, Boolean.valueOf(flag)), 4);
		}

	}

	// --- BARREL STUFF ---

	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		return Container.calcRedstone(worldIn.getTileEntity(pos));
	}

	@Nullable
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new BasketTileEntity();
	}

	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (stack.hasDisplayName()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof BasketTileEntity) {
				((BasketTileEntity) tileentity).setCustomName(stack.getDisplayName());
			}
		}
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		FluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite()).with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER);
	}

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
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

	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}
}
