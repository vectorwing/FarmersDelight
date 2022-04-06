package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.PlantType;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.MathUtils;

import java.util.Random;

public class RichSoilFarmlandBlock extends FarmBlock
{
	public RichSoilFarmlandBlock(Properties properties) {
		super(properties);
	}

	private static boolean hasWater(LevelReader worldIn, BlockPos pos) {
		for (BlockPos nearbyPos : BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 1, 4))) {
			if (worldIn.getFluidState(nearbyPos).is(FluidTags.WATER)) {
				return true;
			}
		}
		return net.minecraftforge.common.FarmlandWaterManager.hasBlockWaterTicket(worldIn, pos);
	}

	public static void turnToRichSoil(BlockState state, Level worldIn, BlockPos pos) {
		worldIn.setBlockAndUpdate(pos, pushEntitiesUp(state, ModBlocks.RICH_SOIL.get().defaultBlockState(), worldIn, pos));
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		BlockState aboveState = worldIn.getBlockState(pos.above());
		return super.canSurvive(state, worldIn, pos) || aboveState.getBlock() instanceof StemGrownBlock;
	}

	@Override
	public boolean isFertile(BlockState state, BlockGetter world, BlockPos pos) {
		if (state.is(ModBlocks.RICH_SOIL_FARMLAND.get()))
			return state.getValue(RichSoilFarmlandBlock.MOISTURE) > 0;

		return false;
	}

	@Override
	public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
		if (!state.canSurvive(worldIn, pos)) {
			turnToRichSoil(state, worldIn, pos);
		}
	}

	@Override
	public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
		int moisture = state.getValue(MOISTURE);
		if (!hasWater(worldIn, pos) && !worldIn.isRainingAt(pos.above())) {
			if (moisture > 0) {
				worldIn.setBlock(pos, state.setValue(MOISTURE, moisture - 1), 2);
			}
		} else if (moisture < 7) {
			worldIn.setBlock(pos, state.setValue(MOISTURE, 7), 2);
		} else if (moisture == 7) {
			if (Configuration.RICH_SOIL_BOOST_CHANCE.get() == 0.0) {
				return;
			}

			BlockState aboveState = worldIn.getBlockState(pos.above());
			Block aboveBlock = aboveState.getBlock();

			if (aboveState.is(ModTags.UNAFFECTED_BY_RICH_SOIL) || aboveBlock instanceof TallFlowerBlock) {
				return;
			}

			if (aboveBlock instanceof BonemealableBlock growable && MathUtils.RAND.nextFloat() <= Configuration.RICH_SOIL_BOOST_CHANCE.get()) {
				if (growable.isValidBonemealTarget(worldIn, pos.above(), aboveState, false) && ForgeHooks.onCropsGrowPre(worldIn, pos.above(), aboveState, true)) {
					growable.performBonemeal(worldIn, worldIn.random, pos.above(), aboveState);
					if (!worldIn.isClientSide) {
						worldIn.levelEvent(2005, pos.above(), 0);
					}
					ForgeHooks.onCropsGrowPost(worldIn, pos.above(), aboveState);
				}
			}
		}
	}

	@Override
	public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, net.minecraftforge.common.IPlantable plantable) {
		net.minecraftforge.common.PlantType plantType = plantable.getPlantType(world, pos.relative(facing));
		return plantType == PlantType.CROP || plantType == PlantType.PLAINS;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return !this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos()) ? ModBlocks.RICH_SOIL.get().defaultBlockState() : super.getStateForPlacement(context);
	}

	@Override
	public void fallOn(Level worldIn, BlockState state, BlockPos pos, Entity entityIn, float fallDistance) {
		// Rich Soil is immune to trampling
	}
}
