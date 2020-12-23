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
import net.minecraftforge.common.PlantType;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.utils.MathUtils;
import vectorwing.farmersdelight.utils.tags.ModTags;

import java.util.Random;

public class RichSoilFarmlandBlock extends FarmlandBlock
{
	public RichSoilFarmlandBlock(Properties properties) {
		super(properties);
	}

	private static boolean hasWater(IWorldReader worldIn, BlockPos pos) {
		for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4))) {
			if (worldIn.getFluidState(blockpos).isTagged(FluidTags.WATER)) {
				return true;
			}
		}
		return net.minecraftforge.common.FarmlandWaterManager.hasBlockWaterTicket(worldIn, pos);
	}

	public static void turnToRichSoil(BlockState state, World worldIn, BlockPos pos) {
		worldIn.setBlockState(pos, nudgeEntitiesWithNewState(state, ModBlocks.RICH_SOIL.get().getDefaultState(), worldIn, pos));
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockState aboveState = worldIn.getBlockState(pos.up());
		return !aboveState.getMaterial().isSolid() || aboveState.getBlock() instanceof FenceGateBlock || aboveState.getBlock() instanceof MovingPistonBlock || aboveState.getBlock() instanceof StemGrownBlock;
	}

	@Override
	public boolean isFertile(BlockState state, IBlockReader world, BlockPos pos) {
		if (this.getBlock() == this)
			return state.get(RichSoilFarmlandBlock.MOISTURE) > 0;

		return false;
	}

	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if (!state.isValidPosition(worldIn, pos)) {
			turnToRichSoil(state, worldIn, pos);
		}
	}

	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		int moisture = state.get(MOISTURE);
		if (!hasWater(worldIn, pos) && !worldIn.isRainingAt(pos.up())) {
			if (moisture > 0) {
				worldIn.setBlockState(pos, state.with(MOISTURE, moisture - 1), 2);
			}
		} else if (moisture < 7) {
			worldIn.setBlockState(pos, state.with(MOISTURE, 7), 2);
		} else if (moisture == 7) {
			BlockState aboveState = worldIn.getBlockState(pos.up());
			Block aboveBlock = aboveState.getBlock();

			// Do nothing if the plant is unaffected by rich soil farmland
			if (ModTags.UNAFFECTED_BY_RICH_SOIL.contains(aboveBlock) || aboveBlock instanceof TallFlowerBlock) {
				return;
			}

			// If all else fails, and it's a plant, give it a growth boost now and then!
			if (aboveBlock instanceof IGrowable && MathUtils.RAND.nextFloat() <= 0.2F) {
				IGrowable growable = (IGrowable) aboveBlock;
				if (growable.canGrow(worldIn, pos.up(), aboveState, false)) {
					growable.grow(worldIn, worldIn.rand, pos.up(), aboveState);
				}
			}
		}
	}

	@Override
	public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, net.minecraftforge.common.IPlantable plantable) {
		net.minecraftforge.common.PlantType type = plantable.getPlantType(world, pos.offset(facing));
		return type == PlantType.CROP || type == PlantType.PLAINS;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return !this.getDefaultState().isValidPosition(context.getWorld(), context.getPos()) ? ModBlocks.RICH_SOIL.get().getDefaultState() : super.getStateForPlacement(context);
	}

	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		// Rich Soil is immune to trampling
	}
}
