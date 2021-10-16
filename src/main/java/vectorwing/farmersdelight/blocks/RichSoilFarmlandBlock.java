package vectorwing.farmersdelight.blocks;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.PlantType;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.setup.Configuration;
import vectorwing.farmersdelight.utils.MathUtils;
import vectorwing.farmersdelight.utils.tags.ModTags;

import java.util.Random;

import net.minecraft.block.AbstractBlock.Properties;

public class RichSoilFarmlandBlock extends FarmlandBlock
{
	public RichSoilFarmlandBlock(Properties properties) {
		super(properties);
	}

	private static boolean hasWater(IWorldReader worldIn, BlockPos pos) {
		for (BlockPos nearbyPos : BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 1, 4))) {
			if (worldIn.getFluidState(nearbyPos).is(FluidTags.WATER)) {
				return true;
			}
		}
		return net.minecraftforge.common.FarmlandWaterManager.hasBlockWaterTicket(worldIn, pos);
	}

	public static void turnToRichSoil(BlockState state, World worldIn, BlockPos pos) {
		worldIn.setBlockAndUpdate(pos, pushEntitiesUp(state, ModBlocks.RICH_SOIL.get().defaultBlockState(), worldIn, pos));
	}

	@Override
	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockState aboveState = worldIn.getBlockState(pos.above());
		return super.canSurvive(state, worldIn, pos) || aboveState.getBlock() instanceof StemGrownBlock;
	}

	@Override
	public boolean isFertile(BlockState state, IBlockReader world, BlockPos pos) {
		if (this.getBlock() == this)
			return state.getValue(RichSoilFarmlandBlock.MOISTURE) > 0;

		return false;
	}

	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if (!state.canSurvive(worldIn, pos)) {
			turnToRichSoil(state, worldIn, pos);
		}
	}

	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
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

			// Do nothing if the plant is unaffected by rich soil farmland
			if (ModTags.UNAFFECTED_BY_RICH_SOIL.contains(aboveBlock) || aboveBlock instanceof TallFlowerBlock) {
				return;
			}

			// If all else fails, and it's a plant, give it a growth boost now and then!
			if (aboveBlock instanceof IGrowable && MathUtils.RAND.nextFloat() <= Configuration.RICH_SOIL_BOOST_CHANCE.get()) {
				IGrowable growable = (IGrowable) aboveBlock;
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
	public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, net.minecraftforge.common.IPlantable plantable) {
		net.minecraftforge.common.PlantType plantType = plantable.getPlantType(world, pos.relative(facing));
		return plantType == PlantType.CROP || plantType == PlantType.PLAINS;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return !this.defaultBlockState().canSurvive(context.getLevel(), context.getClickedPos()) ? ModBlocks.RICH_SOIL.get().defaultBlockState() : super.getStateForPlacement(context);
	}

	@Override
	public void fallOn(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		// Rich Soil is immune to trampling
	}
}
