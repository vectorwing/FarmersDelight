package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

@SuppressWarnings("deprecation")
public class WildCropBlock extends FlowerBlock implements BonemealableBlock
{
	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);
	private final boolean renderOffset;

	public WildCropBlock(MobEffect suspiciousStewEffect, int effectDuration, Properties properties) {
		super(suspiciousStewEffect, effectDuration, properties);
		this.renderOffset = true;
	}

	public WildCropBlock(MobEffect suspiciousStewEffect, int effectDuration, Properties properties, boolean hasOffset) {
		super(suspiciousStewEffect, effectDuration, properties);
		this.renderOffset = hasOffset;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.is(BlockTags.DIRT) || state.is(BlockTags.SAND);
	}

	@Override
	public Block.OffsetType getOffsetType() {
		return renderOffset ? OffsetType.XZ : OffsetType.NONE;
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
		return false;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 60;
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 100;
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean isBonemealSuccess(Level worldIn, Random rand, BlockPos pos, BlockState state) {
		return (double) rand.nextFloat() < 0.8F;
	}

	@Override
	public void performBonemeal(ServerLevel worldIn, Random random, BlockPos pos, BlockState state) {
		int wildCropLimit = 10;

		for (BlockPos nearbyPos : BlockPos.betweenClosed(pos.offset(-4, -1, -4), pos.offset(4, 1, 4))) {
			if (worldIn.getBlockState(nearbyPos).is(this)) {
				--wildCropLimit;
				if (wildCropLimit <= 0) {
					return;
				}
			}
		}

		BlockPos randomPos = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);

		for (int k = 0; k < 4; ++k) {
			if (worldIn.isEmptyBlock(randomPos) && state.canSurvive(worldIn, randomPos)) {
				pos = randomPos;
			}

			randomPos = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
		}

		if (worldIn.isEmptyBlock(randomPos) && state.canSurvive(worldIn, randomPos)) {
			worldIn.setBlock(randomPos, state, 2);
		}
	}
}
