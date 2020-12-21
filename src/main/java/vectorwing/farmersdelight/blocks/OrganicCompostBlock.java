package vectorwing.farmersdelight.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.utils.tags.ModTags;

import java.util.Random;

public class OrganicCompostBlock extends Block
{
	public static IntegerProperty COMPOSTING = IntegerProperty.create("composting", 0, 7);

	public OrganicCompostBlock(Properties properties) {
		super(properties);
		this.setDefaultState(super.getDefaultState().with(COMPOSTING, 0));
	}

	@Override
	public boolean ticksRandomly(BlockState state) {
		return true;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(COMPOSTING);
		super.fillStateContainer(builder);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (worldIn.isRemote) return;

		float chance = 0F;

		int maxLight = 0;
		for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-1, 0, -1), pos.add(1, 1, 1))) {
			BlockState adjacent = worldIn.getBlockState(blockpos);
			if (adjacent.isIn(ModTags.COMPOST_ACTIVATORS) || adjacent.getFluidState().isTagged(FluidTags.WATER))
				chance += 0.02F;
			int light = worldIn.getLightSubtracted(pos.up(), 0);
			if (light > maxLight)
				maxLight = light;
		}
		chance += maxLight > 12 ? 0.1F : 0.05F;

		if (worldIn.getRandom().nextFloat() <= chance) {
			if (state.get(COMPOSTING) == 7)
				worldIn.setBlockState(pos, ModBlocks.RICH_SOIL.get().getDefaultState(), 2); // finished
			else
				worldIn.setBlockState(pos, state.with(COMPOSTING, state.get(COMPOSTING) + 1), 2); // next stage
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		super.animateTick(stateIn, worldIn, pos, rand);
		if (rand.nextInt(10) == 0) {
			worldIn.addParticle(ParticleTypes.MYCELIUM, (double) pos.getX() + (double) rand.nextFloat(), (double) pos.getY() + 1.1D, (double) pos.getZ() + (double) rand.nextFloat(), 0.0D, 0.0D, 0.0D);
		}
	}
}
