package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.FarmlandWaterManager;
import net.neoforged.neoforge.common.util.TriState;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.MathUtils;

public class RichSoilFarmlandBlock extends FarmBlock
{
	public RichSoilFarmlandBlock(Properties properties) {
		super(properties);
	}

	private static boolean hasWater(LevelReader level, BlockPos pos) {
		for (BlockPos nearbyPos : BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 1, 4))) {
			if (level.getFluidState(nearbyPos).is(FluidTags.WATER)) {
				return true;
			}
		}
		return FarmlandWaterManager.hasBlockWaterTicket(level, pos);
	}

	public static void turnToRichSoil(BlockState state, Level level, BlockPos pos) {
		level.setBlockAndUpdate(pos, pushEntitiesUp(state, ModBlocks.RICH_SOIL.get().defaultBlockState(), level, pos));
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockState aboveState = level.getBlockState(pos.above());
		return super.canSurvive(state, level, pos) || aboveState.getBlock().equals(Blocks.MELON) || aboveState.getBlock().equals(Blocks.PUMPKIN);
	}

	@Override
	public boolean isFertile(BlockState state, BlockGetter world, BlockPos pos) {
		if (state.is(ModBlocks.RICH_SOIL_FARMLAND.get()))
			return state.getValue(RichSoilFarmlandBlock.MOISTURE) > 0;

		return false;
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
		if (!state.canSurvive(level, pos)) {
			turnToRichSoil(state, level, pos);
		}
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		int moisture = state.getValue(MOISTURE);
		if (!hasWater(level, pos) && !level.isRainingAt(pos.above())) {
			if (moisture > 0) {
				level.setBlock(pos, state.setValue(MOISTURE, moisture - 1), 2);
			}
		} else if (moisture < 7) {
			level.setBlock(pos, state.setValue(MOISTURE, 7), 2);
		} else if (moisture == 7) {
			if (Configuration.RICH_SOIL_BOOST_CHANCE.get() == 0.0) {
				return;
			}

			BlockPos abovePos = pos.above();
			BlockState aboveState = level.getBlockState(abovePos);
			Block aboveBlock = aboveState.getBlock();

			if (aboveState.is(ModTags.UNAFFECTED_BY_RICH_SOIL) || aboveBlock instanceof TallFlowerBlock) {
				return;
			}

			if (aboveBlock instanceof BonemealableBlock growable && MathUtils.RAND.nextFloat() <= Configuration.RICH_SOIL_BOOST_CHANCE.get()) {
				if (growable.isValidBonemealTarget(level, abovePos, aboveState) && CommonHooks.canCropGrow(level, abovePos, aboveState, true)) {
					growable.performBonemeal(level, level.random, abovePos, aboveState);
					//level.levelEvent(1505, abovePos, 15);
					CommonHooks.fireCropGrowPost(level, abovePos, aboveState);
				}
			}
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
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entityIn, float fallDistance) {
		// Rich Soil is immune to trampling
	}
}
