package vectorwing.farmersdelight.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.IGrowable;
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

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RichSoilFarmlandBlock extends FarmlandBlock
{
	public RichSoilFarmlandBlock(Properties builder) {
		super(builder);
	}
    
	public boolean isFertile(BlockState state, IBlockReader world, BlockPos pos) {
		if (this.getBlock() == this)
            return state.get(RichSoilFarmlandBlock.MOISTURE) > 0;

		return false;
    }

    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
	    if (!state.isValidPosition(worldIn, pos)) {
            turnToRichSoil(state, worldIn, pos);
        }
    }

    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        int i = state.get(MOISTURE);
        if (!hasWater(worldIn, pos) && !worldIn.isRainingAt(pos.up())) {
            if (i > 0) {
                worldIn.setBlockState(pos, state.with(MOISTURE, i - 1), 2);
            }
        } else if (i < 7) {
            worldIn.setBlockState(pos, state.with(MOISTURE, 7), 2);
        } else if (i == 7) {
            BlockState plant = worldIn.getBlockState(pos.up());
            if (plant.getBlock() instanceof IGrowable && MathUtils.RAND.nextInt(10) <= 3) {
                IGrowable growable = (IGrowable) plant.getBlock();
                if (growable.canGrow(worldIn, pos.up(), plant, false)) {
                    growable.grow(worldIn, worldIn.rand, pos.up(), plant);
                }
            }
        }
    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, net.minecraftforge.common.IPlantable plantable) {
        net.minecraftforge.common.PlantType type = plantable.getPlantType(world, pos.offset(facing));
        return type == PlantType.CROP || type == PlantType.PLAINS;
    }

    private static boolean hasWater(IWorldReader worldIn, BlockPos pos) {
        for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4))) {
            if (worldIn.getFluidState(blockpos).isTagged(FluidTags.WATER)) {
                return true;
            }
        }
        return net.minecraftforge.common.FarmlandWaterManager.hasBlockWaterTicket(worldIn, pos);
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return !this.getDefaultState().isValidPosition(context.getWorld(), context.getPos()) ? ModBlocks.RICH_SOIL.get().getDefaultState() : super.getStateForPlacement(context);
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        // Rich Soil is immune to trampling
    }

    public static void turnToRichSoil(BlockState state, World worldIn, BlockPos pos) {
        worldIn.setBlockState(pos, nudgeEntitiesWithNewState(state, ModBlocks.RICH_SOIL.get().getDefaultState(), worldIn, pos));
    }
}
