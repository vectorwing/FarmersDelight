package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import vectorwing.farmersdelight.common.block.entity.CabinetBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

import javax.annotation.Nullable;
import java.util.Random;

@SuppressWarnings("deprecation")
public class CabinetBlock extends BaseEntityBlock
{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

	public CabinetBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, false));
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (!worldIn.isClientSide) {
			BlockEntity tile = worldIn.getBlockEntity(pos);
			if (tile instanceof CabinetBlockEntity) {
				player.openMenu((CabinetBlockEntity) tile);
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity tileEntity = worldIn.getBlockEntity(pos);
			if (tileEntity instanceof Container) {
				Containers.dropContents(worldIn, pos, (Container) tileEntity);
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}
			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		if (tileEntity instanceof CabinetBlockEntity) {
			((CabinetBlockEntity) tileEntity).recheckOpen();
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (stack.hasCustomHoverName()) {
			BlockEntity tileEntity = worldIn.getBlockEntity(pos);
			if (tileEntity instanceof CabinetBlockEntity) {
				((CabinetBlockEntity) tileEntity).setCustomName(stack.getHoverName());
			}
		}
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, OPEN);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(worldIn.getBlockEntity(pos));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return ModBlockEntityTypes.CABINET.get().create(pos, state);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}
}
