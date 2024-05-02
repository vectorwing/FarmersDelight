package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class CuttingBoardBlock extends BaseEntityBlock implements SimpleWaterloggedBlock
{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D);

	public CuttingBoardBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
	}

	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!(level.getBlockEntity(pos) instanceof CuttingBoardBlockEntity cuttingBoard)) {
			return InteractionResult.PASS;
		}

		ItemStack mainHandStack = player.getMainHandItem();

		if (mainHandStack.isEmpty()) {
			if (cuttingBoard.isEmpty() || level.isClientSide) {
				return InteractionResult.sidedSuccess(level.isClientSide);
			}
			ItemStack removedStack = cuttingBoard.removeItem();
			if (!player.isCreative()) {
				player.getInventory().add(removedStack);
			}
			level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.BLOCK_CUTTING_BOARD_REMOVE.get(), SoundSource.BLOCKS, 0.25F, 0.5F);
			return InteractionResult.CONSUME;
		}
		if (cuttingBoard.canAddItem(mainHandStack)) {
			if (level.isClientSide) {
				return InteractionResult.SUCCESS;
			}
			ItemStack remainderStack = cuttingBoard.addItem(player.getAbilities().instabuild ? mainHandStack.copy() : mainHandStack);
			if (!player.isCreative()) {
				player.setItemSlot(EquipmentSlot.MAINHAND, remainderStack);
			}
			level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.BLOCK_CUTTING_BOARD_PLACE.get(), SoundSource.BLOCKS, 1.0F, 0.8F);
			return InteractionResult.CONSUME;
		} else {
			if (cuttingBoard.processStoredItemUsingTool(mainHandStack, player)) {
				return InteractionResult.sidedSuccess(level.isClientSide);
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() == newState.getBlock()) {
			return;
		}

		BlockEntity tileEntity = level.getBlockEntity(pos);
		if (tileEntity instanceof CuttingBoardBlockEntity cuttingBoard) {
			Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), cuttingBoard.getStoredItem());
			level.updateNeighbourForOutputSignal(pos, this);
		}

		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	public boolean isPossibleToRespawnInThis(BlockState state) {
		return true;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite())
				.setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.getValue(WATERLOGGED)) {
			level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
		return facing == Direction.DOWN && !stateIn.canSurvive(level, currentPos)
				? Blocks.AIR.defaultBlockState()
				: super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockPos floorPos = pos.below();
		return canSupportRigidBlock(level, floorPos) || canSupportCenter(level, floorPos, Direction.UP);
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof CuttingBoardBlockEntity) {
			return !((CuttingBoardBlockEntity) blockEntity).isEmpty() ? 15 : 0;
		}
		return 0;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return ModBlockEntityTypes.CUTTING_BOARD.get().create(pos, state);
	}

	@Override
	public BlockState rotate(BlockState pState, Rotation pRot) {
		return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState pState, Mirror pMirror) {
		return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
	}

	public static void spawnCuttingParticles(Level level, BlockPos pos, ItemStack stack, int count) {
		for (int i = 0; i < count; ++i) {
			Vec3 vec3d = new Vec3(((double) level.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, ((double) level.random.nextFloat() - 0.5D) * 0.1D);
			if (level instanceof ServerLevel) {
				((ServerLevel) level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), pos.getX() + 0.5F, pos.getY() + 0.1F, pos.getZ() + 0.5F, 1, vec3d.x, vec3d.y + 0.05D, vec3d.z, 0.0D);
			} else {
				level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), pos.getX() + 0.5F, pos.getY() + 0.1F, pos.getZ() + 0.5F, vec3d.x, vec3d.y + 0.05D, vec3d.z);
			}
		}
	}

	@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class ToolCarvingEvent
	{
		@SubscribeEvent
		@SuppressWarnings("unused")
		public static void onSneakPlaceTool(PlayerInteractEvent.RightClickBlock event) {
			Level level = event.getLevel();
			BlockPos pos = event.getPos();
			Player player = event.getEntity();
			ItemStack heldStack = player.getMainHandItem();
			BlockEntity tileEntity = level.getBlockEntity(event.getPos());

			if (!(level.getBlockEntity(pos) instanceof CuttingBoardBlockEntity cuttingBoard)) {
				return;
			}
			if (player.isSecondaryUseActive() && !heldStack.isEmpty() && tileEntity instanceof CuttingBoardBlockEntity) {
				if (heldStack.getItem() instanceof TieredItem ||
						heldStack.getItem() instanceof TridentItem ||
						heldStack.getItem() instanceof ShearsItem) {
					boolean success = ((CuttingBoardBlockEntity) tileEntity).carveToolOnBoard(player.getAbilities().instabuild ? heldStack.copy() : heldStack);
					if (success) {
						level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 0.8F);
						event.setCanceled(true);
						event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
					}
				}
			}
		}
	}
}
