package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import vectorwing.farmersdelight.common.block.AbstractStoveBlock;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

public class StoveBlockEntity extends AbstractStoveBlockEntity
{
	public StoveBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntityTypes.STOVE.get(), pos, state, RecipeType.CAMPFIRE_COOKING);
	}

	public static void particleTick(Level level, BlockPos pos, BlockState state, StoveBlockEntity stoveEntity) {
		if (stoveEntity.isEmpty()) return;
		stoveEntity.addSmokeParticles();
	}

	public void addSmokeParticles() {
		assert this.level != null;

		var items = this.getItems();
		for (int i = 0; i < items.getSlots(); ++i) {
			if (items.getStackInSlot(i).isEmpty()) continue;
			if (level.random.nextFloat() >= 0.2F) continue;
			Vec2 itemOffset = this.getStoveItemOffset(i);
			Direction direction = this.getBlockState().getValue(AbstractStoveBlock.FACING);
			if (direction.get2DDataValue() % 2 != 0) {
				//noinspection SuspiciousNameCombination
				itemOffset = new Vec2(itemOffset.y, itemOffset.x);
			}

			double x = (worldPosition.getX() + 0.5D) - (direction.getStepX() * itemOffset.x) + (direction.getClockWise().getStepX() * itemOffset.x);
			double y = worldPosition.getY() + 1.0D;
			double z = (worldPosition.getZ() + 0.5D) - (direction.getStepZ() * itemOffset.y) + (direction.getClockWise().getStepZ() * itemOffset.y);

			for (int k = 0; k < 3; ++k) {
				level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 5.0E-4D, 0.0D);
			}
		}
	}

	@Override
	protected int getInventorySlotCount() {
		return 6;
	}

	@Override
	public Vec2 getStoveItemOffset(int index) {
		final float X_OFFSET = 0.3F;
		final float Y_OFFSET = 0.2F;
		final Vec2[] OFFSETS = {
			new Vec2(X_OFFSET, Y_OFFSET),
			new Vec2(0.0F, Y_OFFSET),
			new Vec2(-X_OFFSET, Y_OFFSET),
			new Vec2(X_OFFSET, -Y_OFFSET),
			new Vec2(0.0F, -Y_OFFSET),
			new Vec2(-X_OFFSET, -Y_OFFSET),
		};
		return OFFSETS[index];
	}
}
