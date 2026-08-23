package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.JugBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

public class GlassJugBlock extends JugBlock
{
	public GlassJugBlock(Properties properties) {
		super(properties);
	}

	@javax.annotation.Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
		if (level.isClientSide) {
			return null;
		}
		return createTickerHelper(blockEntity, ModBlockEntityTypes.GLASS_JUG.get(), JugBlockEntity::jugTick);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return ModBlockEntityTypes.GLASS_JUG.get().create(pos, state);
	}
}
