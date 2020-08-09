package vectorwing.farmersdelight.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MushroomBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import vectorwing.farmersdelight.init.ModBlocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class OrganicCompostBlock extends Block {
    public OrganicCompostBlock() {
        super(Properties.from(Blocks.DIRT));
    }

    public OrganicCompostBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (worldIn.isRemote) return;

        float chance = 0.05F;
        if (state.getLightValue() <= 9) {
            chance = chance + 0.1F;
        }
        boolean mushroomNearby = false;
        for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-2, 1, -2), pos.add(2, 1, 2))) {
            if (worldIn.getBlockState(blockpos).getBlock() instanceof MushroomBlock) {
                mushroomNearby = true;
            }
        }
        if (mushroomNearby) {
            chance = chance + 0.15F;
        }
        if (worldIn.getRandom().nextFloat() <= chance) {
            worldIn.setBlockState(pos, ModBlocks.MULCH.get().getDefaultState(), 2);
        }
    }
}
