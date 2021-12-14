package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class SafetyNetBlock extends Block implements SimpleWaterloggedBlock
{
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape SHAPE = Block.box(0.0D, 8.0D, 0.0D, 16.0D, 9.0D, 16.0D);

	public SafetyNetBlock() {
		super(Block.Properties.of(Material.CLOTH_DECORATION).strength(0.2F).sound(SoundType.WOOL));
		this.registerDefaultState(this.getStateDefinition().any().setValue(WATERLOGGED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
		return this.defaultBlockState().setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.getValue(WATERLOGGED)) {
			worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
		}

		return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public void fallOn(Level worldIn, BlockState state, BlockPos pos, Entity entityIn, float fallDistance) {
		if (entityIn.isSuppressingBounce()) {
			super.fallOn(worldIn, state, pos, entityIn, fallDistance);
		} else {
			entityIn.causeFallDamage(fallDistance, 0.0F, DamageSource.FALL);
		}
	}

	@Override
	public void updateEntityAfterFallOn(BlockGetter worldIn, Entity entityIn) {
		if (entityIn.isSuppressingBounce()) {
			super.updateEntityAfterFallOn(worldIn, entityIn);
		} else {
			this.bounceEntity(entityIn);
		}
	}

	private void bounceEntity(Entity entityIn) {
		Vec3 vec3d = entityIn.getDeltaMovement();
		if (vec3d.y < 0.0D) {
			double entityWeightOffset = entityIn instanceof LivingEntity ? 0.6D : 0.8D;
			entityIn.setDeltaMovement(vec3d.x, -vec3d.y * entityWeightOffset, vec3d.z);
		}
	}
}
