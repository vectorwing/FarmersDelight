package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolActions;
import vectorwing.farmersdelight.common.block.entity.StoveBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.MathUtils;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

@SuppressWarnings("deprecation")
public class StoveBlock extends BaseEntityBlock
{
	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	public StoveBlock(BlockBehaviour.Properties builder) {
		super(builder);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		ItemStack heldStack = player.getItemInHand(handIn);
		Item heldItem = heldStack.getItem();

		if (state.getValue(LIT)) {
			if (heldStack.canPerformAction(ToolActions.SHOVEL_DIG)) {
				extinguish(state, worldIn, pos);
				heldStack.hurtAndBreak(1, player, action -> action.broadcastBreakEvent(handIn));
				return InteractionResult.SUCCESS;
			} else if (heldItem == Items.WATER_BUCKET) {
				if (!worldIn.isClientSide()) {
					worldIn.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
				}
				extinguish(state, worldIn, pos);
				if (!player.isCreative()) {
					player.setItemInHand(handIn, new ItemStack(Items.BUCKET));
				}
				return InteractionResult.SUCCESS;
			}
		} else {
			if (heldItem instanceof FlintAndSteelItem) {
				worldIn.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, MathUtils.RAND.nextFloat() * 0.4F + 0.8F);
				worldIn.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
				heldStack.hurtAndBreak(1, player, action -> action.broadcastBreakEvent(handIn));
				return InteractionResult.SUCCESS;
			} else if (heldItem instanceof FireChargeItem) {
				worldIn.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (MathUtils.RAND.nextFloat() - MathUtils.RAND.nextFloat()) * 0.2F + 1.0F);
				worldIn.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
				if (!player.isCreative()) {
					heldStack.shrink(1);
				}
				return InteractionResult.SUCCESS;
			}
		}

		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		if (tileEntity instanceof StoveBlockEntity stoveEntity) {
			int stoveSlot = stoveEntity.getNextEmptySlot();
			if (stoveSlot < 0 || stoveEntity.isStoveBlockedAbove()) {
				return InteractionResult.PASS;
			}
			Optional<CampfireCookingRecipe> recipe = stoveEntity.getMatchingRecipe(new SimpleContainer(heldStack), stoveSlot);
			if (recipe.isPresent()) {
				if (!worldIn.isClientSide && stoveEntity.addItem(player.getAbilities().instabuild ? heldStack.copy() : heldStack, recipe.get(), stoveSlot)) {
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

	public void extinguish(BlockState state, Level worldIn, BlockPos pos) {
		worldIn.setBlock(pos, state.setValue(LIT, false), 2);
		double x = (double) pos.getX() + 0.5D;
		double y = pos.getY();
		double z = (double) pos.getZ() + 0.5D;
		worldIn.playLocalSound(x, y, z, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F, false);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(LIT, true);
	}

	@Override
	public void stepOn(Level world, BlockPos pos, BlockState state, Entity entityIn) {
		boolean isLit = world.getBlockState(pos).getValue(StoveBlock.LIT);
		if (isLit && !entityIn.fireImmune() && entityIn instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entityIn)) {
			entityIn.hurt(DamageSource.HOT_FLOOR, 1.0F);
		}

		super.stepOn(world, pos, state, entityIn);
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity tileEntity = worldIn.getBlockEntity(pos);
			if (tileEntity instanceof StoveBlockEntity) {
				ItemUtils.dropItems(worldIn, pos, ((StoveBlockEntity) tileEntity).getInventory());
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LIT, FACING);
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
		if (stateIn.getValue(CampfireBlock.LIT)) {
			double x = (double) pos.getX() + 0.5D;
			double y = pos.getY();
			double z = (double) pos.getZ() + 0.5D;
			if (rand.nextInt(10) == 0) {
				worldIn.playLocalSound(x, y, z, ModSounds.BLOCK_STOVE_CRACKLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
			}

			Direction direction = stateIn.getValue(HorizontalDirectionalBlock.FACING);
			Direction.Axis direction$axis = direction.getAxis();
			double horizontalOffset = rand.nextDouble() * 0.6D - 0.3D;
			double xOffset = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52D : horizontalOffset;
			double yOffset = rand.nextDouble() * 6.0D / 16.0D;
			double zOffset = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52D : horizontalOffset;
			worldIn.addParticle(ParticleTypes.SMOKE, x + xOffset, y + yOffset, z + zOffset, 0.0D, 0.0D, 0.0D);
			worldIn.addParticle(ParticleTypes.FLAME, x + xOffset, y + yOffset, z + zOffset, 0.0D, 0.0D, 0.0D);
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

	@Override
	public BlockState rotate(BlockState pState, Rotation pRot) {
		return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState pState, Mirror pMirror) {
		return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
	}
}
