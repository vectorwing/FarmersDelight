package vectorwing.farmersdelight.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.Entity;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.PlantType;
import vectorwing.farmersdelight.init.ModBlocks;
import vectorwing.farmersdelight.utils.Utils;

import java.util.Random;

public class MulchFarmlandBlock extends FarmlandBlock
{
	public MulchFarmlandBlock(Properties builder)
	{
		super(builder);
	}

	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if (!worldIn.isRemote) {
			if (!state.isValidPosition(worldIn, pos)) {
				turnToMulch(state, worldIn, pos);
			} else {
				int i = state.get(MOISTURE);
				if (!hasWater(worldIn, pos) && !worldIn.isRainingAt(pos.up())) {
					if (i > 0) {
						worldIn.setBlockState(pos, state.with(MOISTURE, i - 1), 2);
					}
				} else if (i < 7) {
					worldIn.setBlockState(pos, state.with(MOISTURE, 7), 2);
				} else if (i == 7) {
					BlockState plant = worldIn.getBlockState(pos.up());
					if (plant.getBlock() instanceof IGrowable && Utils.RAND.nextInt(10) <= 4) {
						IGrowable growable = (IGrowable) plant.getBlock();
						if (growable.canGrow(worldIn, pos.up(), plant, false)) {
							growable.grow(worldIn, worldIn.rand, pos.up(), plant);
						}
					}
				}
			}
		}
	}

	public boolean isFertile(BlockState state, IBlockReader world, BlockPos pos) {
		if (this.getBlock() == this)
			return state.get(MulchFarmlandBlock.MOISTURE) > 0;

		return false;
	}

	@Override
	public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, net.minecraftforge.common.IPlantable plantable) {
		net.minecraftforge.common.PlantType type = plantable.getPlantType(world, pos.offset(facing));
		return type == PlantType.Crop || type == PlantType.Plains;
	}

	private boolean hasCrops(IBlockReader worldIn, BlockPos pos) {
		BlockState state = worldIn.getBlockState(pos.up());
		return state.getBlock() instanceof net.minecraftforge.common.IPlantable && canSustainPlant(state, worldIn, pos, Direction.UP, (net.minecraftforge.common.IPlantable)state.getBlock());
	}

	private static boolean hasWater(IWorldReader worldIn, BlockPos pos) {
		for(BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4))) {
			if (worldIn.getFluidState(blockpos).isTagged(FluidTags.WATER)) {
				return true;
			}
		}

		return net.minecraftforge.common.FarmlandWaterManager.hasBlockWaterTicket(worldIn, pos);
	}

	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		// Mulch is immune to trampling
	}

	public static void turnToMulch(BlockState state, World worldIn, BlockPos pos) {
		worldIn.setBlockState(pos, nudgeEntitiesWithNewState(state, ModBlocks.MULCH.get().getDefaultState(), worldIn, pos));
	}
}
