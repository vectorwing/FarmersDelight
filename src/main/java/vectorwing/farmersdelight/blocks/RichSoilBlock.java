package vectorwing.farmersdelight.blocks;

import net.minecraft.block.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.PlantType;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.utils.MathUtils;
import vectorwing.farmersdelight.utils.tags.ModTags;

import java.util.Random;

@SuppressWarnings("deprecation")
public class RichSoilBlock extends Block
{
	public static final int MUSHROOM_COLONY_LIGHT_LEVEL = 12;

	public RichSoilBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if (!worldIn.isRemote) {
			BlockPos abovePos = pos.up();
			BlockState aboveState = worldIn.getBlockState(abovePos);
			Block aboveBlock = aboveState.getBlock();

			// Do nothing if the plant is unaffected by rich soil
			if (ModTags.UNAFFECTED_BY_RICH_SOIL.contains(aboveBlock) || aboveBlock instanceof TallFlowerBlock) {
				return;
			}

			// Convert mushrooms to colonies if it's dark enough
			if (aboveBlock == Blocks.BROWN_MUSHROOM) {
				if (worldIn.getLightSubtracted(pos.up(), 0) <= MUSHROOM_COLONY_LIGHT_LEVEL) {
					worldIn.setBlockState(pos.up(), ModBlocks.BROWN_MUSHROOM_COLONY.get().getDefaultState());
				}
				return;
			}
			if (aboveBlock == Blocks.RED_MUSHROOM) {
				if (worldIn.getLightSubtracted(pos.up(), 0) <= MUSHROOM_COLONY_LIGHT_LEVEL) {
					worldIn.setBlockState(pos.up(), ModBlocks.RED_MUSHROOM_COLONY.get().getDefaultState());
				}
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
		return type != PlantType.CROP && type != PlantType.NETHER;
	}
}
