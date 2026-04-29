package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class TatamiBlock extends Block
{
	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
	public static final BooleanProperty PAIRED = BooleanProperty.create("paired");

	public TatamiBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.DOWN).setValue(PAIRED, false));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction face = context.getClickedFace();
		BlockPos targetPos = context.getClickedPos().relative(face.getOpposite());
		BlockState targetState = context.getLevel().getBlockState(targetPos);
		boolean pairing = false;

		// TODO: IDEA says this can be simplified. Try it once the mod is booting!
		if (context.getPlayer() != null && !context.getPlayer().isShiftKeyDown() && targetState.getBlock() == this && !targetState.getValue(PAIRED)) {
			pairing = true;
		}

		return this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite()).setValue(PAIRED, pairing);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(level, pos, state, placer, stack);
		if (!level.isClientSide()) {
			if (placer != null && placer.isShiftKeyDown()) {
				return;
			}
			BlockPos facingPos = pos.relative(state.getValue(FACING));
			BlockState facingState = level.getBlockState(facingPos);
			if (facingState.getBlock() == this && !facingState.getValue(PAIRED)) {
				level.setBlock(facingPos, state.setValue(FACING, state.getValue(FACING).getOpposite()).setValue(PAIRED, true), 3);
				level.updateNeighborsAt(pos, Blocks.AIR);
				state.updateNeighbourShapes(level, pos, 3);
			}
		}
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
		if (directionToNeighbour.equals(state.getValue(FACING)) && state.getValue(PAIRED) && level.getBlockState(neighbourPos).getBlock() != this) {
			return state.setValue(PAIRED, false);
		}
		return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, PAIRED);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}
}
