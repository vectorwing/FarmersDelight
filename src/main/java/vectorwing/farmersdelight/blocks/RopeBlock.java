package vectorwing.farmersdelight.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
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
import vectorwing.farmersdelight.registry.ModBlocks;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class RopeBlock extends PaneBlock
{
	public static final BooleanProperty TIED_TO_BELL = BooleanProperty.create("tied_to_bell");

	public RopeBlock() {
		super(Properties.create(Material.CARPET).doesNotBlockMovement().notSolid().hardnessAndResistance(0.1F).sound(SoundType.CLOTH));
		this.setDefaultState(this.stateContainer.getBaseState().with(TIED_TO_BELL, false));
	}

	@Override
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return true;
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IBlockReader iblockreader = context.getWorld();
		BlockPos posAbove = context.getPos().up();
		BlockState state = super.getStateForPlacement(context);
		return state != null ? state.with(TIED_TO_BELL, iblockreader.getBlockState(posAbove).getBlock() == Blocks.BELL) : null;
	}

	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (player.getHeldItem(handIn).isEmpty()) {
			BlockPos.Mutable blockpos$mutable = (new BlockPos.Mutable(pos)).move(Direction.UP);

			for (int i = 0; i < 24; i++) {
				BlockState blockStateAbove = worldIn.getBlockState(blockpos$mutable);
				Block blockAbove = blockStateAbove.getBlock();
				if (blockAbove == Blocks.BELL) {
					((BellBlock)blockAbove).func_226885_a_(worldIn, blockpos$mutable, blockStateAbove.get(BellBlock.HORIZONTAL_FACING).rotateY());
					return ActionResultType.SUCCESS;
				} else if (blockAbove == ModBlocks.ROPE.get()) {
					blockpos$mutable.move(Direction.UP);
				} else {
					return ActionResultType.PASS;
				}
			}
		}

		return ActionResultType.PASS;
	}

	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return VoxelShapes.empty();
	}

	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
		return useContext.getItem().getItem() == this.asItem();
	}

	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}

		boolean tiedToBell = stateIn.get(TIED_TO_BELL);
		if (facing == Direction.UP) {
			tiedToBell = worldIn.getBlockState(facingPos).getBlock() == Blocks.BELL;
		}

		return facing.getAxis().isHorizontal()
				? stateIn.with(TIED_TO_BELL, tiedToBell).with(FACING_TO_PROPERTY_MAP.get(facing), this.canAttachTo(facingState, facingState.isSolidSide(worldIn, facingPos, facing.getOpposite())))
				: super.updatePostPlacement(stateIn.with(TIED_TO_BELL, tiedToBell), facing, facingState, worldIn, currentPos, facingPos);
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, WEST, SOUTH, WATERLOGGED, TIED_TO_BELL);
	}

	@Override
	public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, net.minecraft.entity.LivingEntity entity) {
		return true;
	}

}