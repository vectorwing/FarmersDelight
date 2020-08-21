package vectorwing.farmersdelight.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class PieBlock extends Block {

	public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 3);
	protected static final VoxelShape[] SHAPES = new VoxelShape[] {
			Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D),
			VoxelShapes.or(
					Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 8.0D),
					Block.makeCuboidShape(2.0D, 0.0D, 8.0D, 8.0D, 6.0D, 14.0D)),
			Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 8.0D),
			Block.makeCuboidShape(8.0D, 0.0D, 2.0D, 14.0D, 6.0D, 8.0D),
	};


	public PieBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(BITES, 0));
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPES[state.get(BITES)];
	}

	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		// TODO: If using a farmersdelight:tools/knife on this block, drop a slice.
		// TODO: If using anything else, and player is hungry, take a bite.
		if (worldIn.isRemote) {
			ItemStack itemstack = player.getHeldItem(handIn);
			if (this.consumeBite(worldIn, pos, state, player) == ActionResultType.SUCCESS) {
				return ActionResultType.SUCCESS;
			}

			if (itemstack.isEmpty()) {
				return ActionResultType.CONSUME;
			}
		}

		return this.consumeBite(worldIn, pos, state, player);
	}

	private ActionResultType consumeBite(IWorld worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn) {
		if (!playerIn.canEat(false)) {
			return ActionResultType.PASS;
		} else {
			playerIn.getFoodStats().addStats(3, 0.3F);
			int i = state.get(BITES);
			if (i < getMaxBites() - 1) {
				worldIn.setBlockState(pos, state.with(BITES, i + 1), 3);
			} else {
				worldIn.removeBlock(pos, false);
			}
			worldIn.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 0.8F, 0.8F);
			return ActionResultType.SUCCESS;
		}
	}

	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.down()).getMaterial().isSolid();
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BITES);
	}

	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		return getMaxBites() - blockState.get(BITES);
	}

	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}

	public int getMaxBites() {
		return 4;
	}

}
