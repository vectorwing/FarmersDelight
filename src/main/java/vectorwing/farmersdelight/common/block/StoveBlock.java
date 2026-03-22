package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolActions;
import vectorwing.farmersdelight.common.block.entity.StoveBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModDamageTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.MathUtils;

import javax.annotation.Nullable;
import java.util.Optional;

@SuppressWarnings("deprecation")
public class StoveBlock extends BaseEntityBlock
{
	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	private static final VoxelShape GRILLING_AREA = Block.box(3.0F, 0.0F, 3.0F, 13.0F, 1.0F, 13.0F);

	public StoveBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack heldStack = player.getItemInHand(hand);
		Item heldItem = heldStack.getItem();

		if (state.getValue(LIT)) {
			if (heldStack.canPerformAction(ToolActions.SHOVEL_DIG)) {
				extinguish(state, level, pos);
				heldStack.hurtAndBreak(1, player, action -> action.broadcastBreakEvent(hand));
				return InteractionResult.SUCCESS;
			} else if (heldStack.is(CommonTags.Items.BUCKETS_WATER)) {
				if (!level.isClientSide()) {
					level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
				}
				extinguish(state, level, pos);
				if (!player.isCreative()) {
					player.setItemInHand(hand, heldStack.getCraftingRemainingItem());
				}
				return InteractionResult.SUCCESS;
			}
		} else {
			if (heldItem instanceof FlintAndSteelItem) {
				level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, MathUtils.RAND.nextFloat() * 0.4F + 0.8F);
				level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
				heldStack.hurtAndBreak(1, player, action -> action.broadcastBreakEvent(hand));
				return InteractionResult.SUCCESS;
			} else if (heldItem instanceof FireChargeItem) {
				level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (MathUtils.RAND.nextFloat() - MathUtils.RAND.nextFloat()) * 0.2F + 1.0F);
				level.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
				if (!player.isCreative()) {
					heldStack.shrink(1);
				}
				return InteractionResult.SUCCESS;
			}
		}

		if (!isStoveTopCovered(level, pos, state) && level.getBlockEntity(pos) instanceof StoveBlockEntity stoveEntity) {
			int stoveSlot = stoveEntity.getNextEmptySlot();
			if (stoveSlot < 0) {
				return InteractionResult.PASS;
			}
			Optional<CampfireCookingRecipe> recipe = stoveEntity.getMatchingRecipe(new SimpleContainer(heldStack), stoveSlot);
			if (recipe.isPresent()) {
				if (!level.isClientSide && stoveEntity.addItem(player.getAbilities().instabuild ? heldStack.copy() : heldStack, recipe.get(), stoveSlot)) {
					return InteractionResult.SUCCESS;
				}
				return InteractionResult.CONSUME;
			}
		}

		return InteractionResult.PASS;
	}

	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}

	public void extinguish(BlockState state, Level level, BlockPos pos) {
		level.setBlock(pos, state.setValue(LIT, false), 2);
		double x = (double) pos.getX() + 0.5D;
		double y = pos.getY();
		double z = (double) pos.getZ() + 0.5D;
		level.playLocalSound(x, y, z, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F, false);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(LIT, true);
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		if (entity.getBoundingBox().intersects(GRILLING_AREA.bounds().move(pos.above()))) {
			boolean isLit = level.getBlockState(pos).getValue(StoveBlock.LIT);
			if (isLit && !entity.isSteppingCarefully() && !entity.fireImmune() && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entity)) {
				entity.hurt(ModDamageTypes.getSimpleDamageSource(level, ModDamageTypes.STOVE_BURN), 1.0F);
			}
		}

		super.stepOn(level, pos, state, entity);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			if (level.getBlockEntity(pos) instanceof StoveBlockEntity stove) {
				ItemUtils.dropItems(level, pos, stove.getInventory());
			}

			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

	/**
	 * Checks if the state is a Stove, and if the grilling area is being obstructed by the block above.
	 */
	public static boolean isStoveTopCovered(Level level, BlockPos pos, BlockState stoveState) {
		if (stoveState.getBlock() instanceof StoveBlock) {
			BlockPos abovePos = pos.above();
			BlockState aboveState = level.getBlockState(abovePos);
			return Shapes.joinIsNotEmpty(GRILLING_AREA, aboveState.getShape(level, abovePos), BooleanOp.AND);
		}
		return false;
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LIT, FACING);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
		if (state.getValue(CampfireBlock.LIT)) {
			double x = (double) pos.getX() + 0.5D;
			double y = pos.getY();
			double z = (double) pos.getZ() + 0.5D;
			if (rand.nextInt(10) == 0) {
				level.playLocalSound(x, y, z, ModSounds.BLOCK_STOVE_CRACKLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
			}

			Direction direction = state.getValue(HorizontalDirectionalBlock.FACING);
			Direction.Axis direction$axis = direction.getAxis();
			double horizontalOffset = rand.nextDouble() * 0.6D - 0.3D;
			double xOffset = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52D : horizontalOffset;
			double yOffset = rand.nextDouble() * 6.0D / 16.0D;
			double zOffset = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52D : horizontalOffset;
			level.addParticle(ParticleTypes.SMOKE, x + xOffset, y + yOffset, z + zOffset, 0.0D, 0.0D, 0.0D);
			level.addParticle(ParticleTypes.FLAME, x + xOffset, y + yOffset, z + zOffset, 0.0D, 0.0D, 0.0D);
		}
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return ModBlockEntityTypes.STOVE.get().create(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		if (state.getValue(LIT)) {
			return createTickerHelper(blockEntityType, ModBlockEntityTypes.STOVE.get(), level.isClientSide
					? StoveBlockEntity::animationTick
					: StoveBlockEntity::cookingTick);
		}
		return null;
	}

	@Nullable
	@Override
	public BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob entity) {
		return state.getValue(LIT) ? BlockPathTypes.DAMAGE_FIRE : null;
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
