package vectorwing.farmersdelight.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
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
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import vectorwing.farmersdelight.tile.BasketTileEntity;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock.Properties;

@SuppressWarnings("deprecation")
public class BasketBlock extends ContainerBlock implements IWaterLoggable
{
	public static final DirectionProperty FACING = BlockStateProperties.FACING;
	public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public static final VoxelShape OUT_SHAPE = VoxelShapes.block();
	public static final VoxelShape RENDER_SHAPE = box(1.0D, 1.0D, 1.0D, 15.0D, 15.0D, 15.0D);
	@SuppressWarnings("UnstableApiUsage")
	public static final ImmutableMap<Direction, VoxelShape> COLLISION_SHAPE_FACING =
			Maps.immutableEnumMap(ImmutableMap.<Direction, VoxelShape>builder()
					.put(Direction.DOWN, makeHollowCubeShape(box(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D)))
					.put(Direction.UP, makeHollowCubeShape(box(2.0D, 2.0D, 2.0D, 14.0D, 16.0D, 14.0D)))
					.put(Direction.NORTH, makeHollowCubeShape(box(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 14.0D)))
					.put(Direction.SOUTH, makeHollowCubeShape(box(2.0D, 2.0D, 2.0D, 14.0D, 14.0D, 16.0D)))
					.put(Direction.WEST, makeHollowCubeShape(box(0.0D, 2.0D, 2.0D, 14.0D, 14.0D, 14.0D)))
					.put(Direction.EAST, makeHollowCubeShape(box(2.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D)))
					.build());

	private static VoxelShape makeHollowCubeShape(VoxelShape cutout) {
		return VoxelShapes.joinUnoptimized(OUT_SHAPE, cutout, IBooleanFunction.ONLY_FIRST).optimize();
	}

	public BasketBlock() {
		super(Properties.of(Material.WOOD).strength(1.5F).sound(SoundType.WOOD));
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.UP).setValue(WATERLOGGED, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return COLLISION_SHAPE_FACING.get(state.getValue(FACING));
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return RENDER_SHAPE;
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, ENABLED, WATERLOGGED);
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (!worldIn.isClientSide) {
			TileEntity tileEntity = worldIn.getBlockEntity(pos);
			if (tileEntity instanceof BasketTileEntity) {
				player.openMenu((BasketTileEntity) tileEntity);
			}
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileEntity = worldIn.getBlockEntity(pos);
			if (tileEntity instanceof IInventory) {
				InventoryHelper.dropContents(worldIn, pos, (IInventory) tileEntity);
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.getValue(WATERLOGGED)) {
			worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
		}

		return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	// --- HOPPER STUFF ---

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		boolean isPowered = !worldIn.hasNeighborSignal(pos);
		if (isPowered != state.getValue(ENABLED)) {
			worldIn.setBlock(pos, state.setValue(ENABLED, isPowered), 4);
		}
	}

	// --- BARREL STUFF ---

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, World worldIn, BlockPos pos) {
		return Container.getRedstoneSignalFromBlockEntity(worldIn.getBlockEntity(pos));
	}

	@Nullable
	public TileEntity newBlockEntity(IBlockReader worldIn) {
		return new BasketTileEntity();
	}

	@Override
	public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (stack.hasCustomHoverName()) {
			TileEntity tileEntity = worldIn.getBlockEntity(pos);
			if (tileEntity instanceof BasketTileEntity) {
				((BasketTileEntity) tileEntity).setCustomName(stack.getHoverName());
			}
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
		return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()).setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	public boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}

	@Override
	public VoxelShape getInteractionShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return OUT_SHAPE;
	}

	@Override
	public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}
}
