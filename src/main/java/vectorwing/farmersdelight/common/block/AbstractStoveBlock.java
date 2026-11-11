package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.Tags;
import vectorwing.farmersdelight.common.block.entity.AbstractStoveBlockEntity;
import vectorwing.farmersdelight.common.registry.ModDamageTypes;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.MathUtils;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public abstract class AbstractStoveBlock extends BaseEntityBlock
{
	public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	private static final VoxelShape GRILLING_AREA = Block.box(3.0F, 0.0F, 3.0F, 13.0F, 1.0F, 13.0F);

	public AbstractStoveBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(
			this.stateDefinition.any()
				.setValue(FACING, Direction.NORTH)
				.setValue(LIT, false)
		);
	}

	@Override
	public InteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (state.getValue(LIT)) {
			var extinguishResult = tryToExtinguish(heldStack, state, level, pos, player, hand, hit);
			if (extinguishResult != InteractionResult.PASS) return extinguishResult;
		} else {
			var igniteResult = tryToIgnite(heldStack, state, level, pos, player, hand, hit);
			if (igniteResult != InteractionResult.PASS) return igniteResult;
		}

		return tryToPlaceFoodItem(heldStack, state, level, pos, player, hand, hit);
	}

	protected InteractionResult tryToIgnite(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		Item heldItem = heldStack.getItem();

		if (heldItem instanceof FlintAndSteelItem) {
			if (!level.isClientSide()) {
				level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, MathUtils.RAND.nextFloat() * 0.4F + 0.8F);
			}
			ignite(player, level, pos, state);
			heldStack.hurtAndBreak(1, player, hand.asEquipmentSlot());
			return InteractionResult.SUCCESS;
		}

		if (heldItem instanceof FireChargeItem) {
			if (!level.isClientSide()) {
				level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (MathUtils.RAND.nextFloat() - MathUtils.RAND.nextFloat()) * 0.2F + 1.0F);
			}
			ignite(player, level, pos, state);
			if (!player.getAbilities().instabuild) {
				heldStack.shrink(1);
			}
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	protected InteractionResult tryToExtinguish(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (heldStack.canPerformAction(ItemAbilities.SHOVEL_DOUSE)) {
			if (!level.isClientSide()) {
				level.levelEvent(null, LevelEvent.SOUND_EXTINGUISH_FIRE, pos, 0);
			}
			extinguish(player, level, pos, state);
			heldStack.hurtAndBreak(1, player, hand.asEquipmentSlot());
			return InteractionResult.SUCCESS;
		}

		if (heldStack.is(Tags.Items.BUCKETS_WATER)) {
			if (!level.isClientSide()) {
				level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
			}
			extinguish(player, level, pos, state);
			ItemStackTemplate remainder = heldStack.getCraftingRemainder();
			if (remainder != null && !player.getAbilities().instabuild) {
				player.setItemInHand(hand, heldStack.getCraftingRemainder().create());
			}
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	protected InteractionResult tryToPlaceFoodItem(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (isStoveTopCovered(level, pos, state)) return InteractionResult.PASS;
		if (!(level.getBlockEntity(pos) instanceof AbstractStoveBlockEntity stoveEntity)) return InteractionResult.PASS;
		if (!(level instanceof ServerLevel)) return InteractionResult.PASS;

		var maybeRecipe = stoveEntity.getCookingRecipe(heldStack);
		if (maybeRecipe.isEmpty()) return InteractionResult.PASS;
		if (level.isClientSide()) return InteractionResult.CONSUME;
		boolean placeFoodSuccess = stoveEntity.placeFood(player, player.getAbilities().instabuild ? heldStack.copy() : heldStack, maybeRecipe.get());
		if (!placeFoodSuccess) return InteractionResult.CONSUME;
		level.playSound(null, pos, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 0.5F, 1.0F);
		return InteractionResult.SUCCESS_SERVER;
	}

	public void ignite(@Nullable Entity entity, LevelAccessor level, BlockPos pos, BlockState state) {
		if (level.getBlockEntity(pos) instanceof AbstractStoveBlockEntity stoveEntity) {
			stoveEntity.ignite();
		}
		if (level.isClientSide()) return;

		var newState = state.setValue(LIT, true);
		level.setBlock(pos, newState, 11);
		level.gameEvent(entity, GameEvent.BLOCK_CHANGE, pos);
	}

	public void extinguish(@Nullable Entity entity, LevelAccessor level, BlockPos pos, BlockState state) {
		if (level.getBlockEntity(pos) instanceof AbstractStoveBlockEntity stoveEntity) {
			stoveEntity.extinguish();
		}
		if (level.isClientSide()) return;

		var newState = state.setValue(LIT, false);
		level.setBlock(pos, newState, 11);
		level.gameEvent(entity, GameEvent.BLOCK_CHANGE, pos);
	}

	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState()
			.setValue(FACING, context.getHorizontalDirection().getOpposite())
			.setValue(LIT, true);
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		burnEntitySteppingOnStove(level, pos, state, entity);
		super.stepOn(level, pos, state, entity);
	}

	protected void burnEntitySteppingOnStove(Level level, BlockPos pos, BlockState state, Entity entity) {
		if (!state.getValue(LIT)) return;
		if (!entity.getBoundingBox().intersects(GRILLING_AREA.bounds().move(pos.above()))) return;
		if (entity.isSteppingCarefully()) return;
		if (!(entity instanceof LivingEntity)) return;
		entity.hurt(ModDamageTypes.getSimpleDamageSource(level, ModDamageTypes.STOVE_BURN), 1.0F);
	}

	@Override
	public void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
		if (level.getBlockEntity(pos) instanceof AbstractStoveBlockEntity stoveEntity) {
			ItemUtils.dropItems(level, pos, stoveEntity.getItems());
		}
		super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
	}

	/**
	 * Checks if the state is a Stove, and if the grilling area is being obstructed by the block above.
	 */
	public static boolean isStoveTopCovered(Level level, BlockPos pos, BlockState stoveState) {
		if (!(stoveState.getBlock() instanceof StoveBlock)) return false;
		BlockPos abovePos = pos.above();
		BlockState aboveState = level.getBlockState(abovePos);
		return Shapes.joinIsNotEmpty(GRILLING_AREA, aboveState.getShape(level, abovePos), BooleanOp.AND);
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, LIT);
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> createStoveTicker(Level level, BlockEntityType<T> serverType, BlockEntityType<? extends AbstractStoveBlockEntity> clientType) {
		if (level.isClientSide()) return null;
		return createTickerHelper(serverType, clientType, AbstractStoveBlockEntity::serverTick);
	}

	@Nullable
	@Override
	public PathType getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob entity) {
		return state.getValue(LIT) ? PathType.FIRE : null;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}
}
