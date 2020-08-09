package vectorwing.farmersdelight.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.PlantType;
import vectorwing.farmersdelight.init.ModBlocks;
import vectorwing.farmersdelight.utils.Utils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MulchBlock extends Block {
    public MulchBlock() {
        super(Properties.from(Blocks.DIRT));
    }

    public MulchBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!worldIn.isRemote) {
            BlockState plant = worldIn.getBlockState(pos.up());
            if (plant.getBlock() == Blocks.BROWN_MUSHROOM) {
                worldIn.setBlockState(pos.up(), ModBlocks.BROWN_MUSHROOM_COLONY.get().getDefaultState().with(MushroomColonyBlock.COLONY_AGE, 0));
            }
            if (plant.getBlock() == Blocks.RED_MUSHROOM) {
                worldIn.setBlockState(pos.up(), ModBlocks.RED_MUSHROOM_COLONY.get().getDefaultState().with(MushroomColonyBlock.COLONY_AGE, 0));
            }
            if (plant.getBlock() instanceof IGrowable && Utils.RAND.nextInt(10) <= 2) {
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
        return type != PlantType.CROP && type != PlantType.NETHER;
    }
}
