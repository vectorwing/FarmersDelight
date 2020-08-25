package vectorwing.farmersdelight.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MushroomBlock;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.server.ServerWorld;
import vectorwing.farmersdelight.registry.ModBlocks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class OrganicCompostBlock extends Block {
	public static IntegerProperty COMPOSTATION = IntegerProperty.create("compostation", 0, 7);

	public OrganicCompostBlock() {
		this(Properties.from(Blocks.DIRT));
	}

	public OrganicCompostBlock(Properties properties) {
		super(properties);
		this.setDefaultState(super.getDefaultState().with(COMPOSTATION, 0));
	}

	@Override
	public boolean ticksRandomly(BlockState state) {
		return true;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(COMPOSTATION);
		super.fillStateContainer(builder);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (worldIn.isRemote) return;

		float chance = 0F;

		int maxLight = 0;
		for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-1, 0, -1), pos.add(1, 0, 1))) {
			BlockState adjacent = worldIn.getBlockState(blockpos);
			if (adjacent.getBlock() instanceof MushroomBlock || adjacent.getBlock() instanceof OrganicCompostBlock || adjacent.getFluidState().isTagged(FluidTags.WATER))
				chance += 0.04F;
			int light = worldIn.getLightFor(LightType.BLOCK, blockpos);
			if(light > maxLight)
				maxLight = light;
		}
		chance += 0.015F * (15 - maxLight);

		if (worldIn.getRandom().nextFloat() <= chance) {
			if (state.get(COMPOSTATION) == 7)
				worldIn.setBlockState(pos, ModBlocks.MULCH.get().getDefaultState(), 2); // finished
			else
				worldIn.setBlockState(pos, state.with(COMPOSTATION, state.get(COMPOSTATION) + 1), 2); // next stage
		}
	}
}
