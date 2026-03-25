package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.FarmlandWaterManager;
import net.neoforged.neoforge.common.util.TriState;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import javax.annotation.Nullable;

public class RichSoilFarmlandBlock extends FarmBlock
{
	public RichSoilFarmlandBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		int moisture = state.getValue(MOISTURE);
		if (!isNearWater(level, pos) && !level.isRainingAt(pos.above())) {
			if (moisture > 0) {
				level.setBlock(pos, state.setValue(MOISTURE, moisture - 1), 2);
			}
		} else if (moisture < 7) {
			level.setBlock(pos, state.setValue(MOISTURE, 7), 2);
		} else if (moisture == 7) {
			RichSoilBlock.tryBoostingPlantsAboveAndBelow(level, pos, random);
		}
	}

	private static boolean isNearWater(LevelReader level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		for (BlockPos nearbyPos : BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 1, 4))) {
			if (state.canBeHydrated(level, pos, level.getFluidState(nearbyPos), nearbyPos)) {
				return true;
			}
		}
		return FarmlandWaterManager.hasBlockWaterTicket(level, pos);
	}

	public static void turnToRichSoil(@Nullable Entity entity, BlockState state, Level level, BlockPos pos) {
		level.setBlockAndUpdate(pos, pushEntitiesUp(state, ModBlocks.RICH_SOIL.get().defaultBlockState(), level, pos));
		level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, state));
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockState aboveState = level.getBlockState(pos.above());
		return super.canSurvive(state, level, pos) || aboveState.getBlock().equals(Blocks.MELON) || aboveState.getBlock().equals(Blocks.PUMPKIN);
	}

	@Override
	public boolean isFertile(BlockState state, BlockGetter level, BlockPos pos) {
		if (state.is(ModBlocks.RICH_SOIL_FARMLAND.get()))
			return state.getValue(RichSoilFarmlandBlock.MOISTURE) > 0;

		return false;
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (!state.canSurvive(level, pos)) {
			turnToRichSoil(null, state, level, pos);
		}
	}

	@Override
	public TriState canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, BlockState plantState) {
//		PlantType plantType = plantable.getPlantType(world, pos.relative(facing));
//		return plantType == PlantType.CROP || plantType == PlantType.PLAINS;

		// TODO: Revisit this method to filter out plants correctly. Also, there's a chance Rich Soil Farmland won't need it anymore.
		if (plantState.getBlock() instanceof CropBlock) {
			return TriState.TRUE;
		}
		return TriState.DEFAULT;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return !this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos()) ? ModBlocks.RICH_SOIL.get().defaultBlockState() : super.getStateForPlacement(context);
	}

	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		entity.causeFallDamage(fallDistance, 1.0F, entity.damageSources().fall());
	}
}
