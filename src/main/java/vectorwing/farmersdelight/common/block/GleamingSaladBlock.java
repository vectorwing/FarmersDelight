package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.registry.ModParticleTypes;

import java.util.function.Supplier;

public class GleamingSaladBlock extends FeastBlock
{
	protected static final VoxelShape SHAPE = Shapes.or(
			Block.box(3.0, 0.0, 3.0, 13.0, 2.0, 13.0),
			Block.box(2.0, 2.0, 2.0, 14.0, 6.0, 14.0)
	);

	public GleamingSaladBlock(Properties properties, Supplier<Item> servingItem, boolean hasLeftovers) {
		super(properties, servingItem, hasLeftovers);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		int servings = state.getValue(SERVINGS);
		if (servings == 0) {
			return;
		}
		if (random.nextFloat() < 0.05F * servings) {
			double x = pos.getX() + 0.5D + (random.nextDouble() * 0.5D - 0.25D);
			double y = pos.getY() + 0.5D;
			double z = pos.getZ() + 0.5D + (random.nextDouble() * 0.5D - 0.25D);
			level.addParticle(ModParticleTypes.SPARKLE.get(), x, y, z, 0.0D, 0.0D, 0.0D);
		}
	}
}
