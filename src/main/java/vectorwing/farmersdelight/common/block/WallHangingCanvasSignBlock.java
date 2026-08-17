package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import vectorwing.farmersdelight.common.block.state.CanvasSign;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

import javax.annotation.Nullable;

public class WallHangingCanvasSignBlock extends WallHangingSignBlock implements CanvasSign
{
	private final DyeColor backgroundColor;

	public WallHangingCanvasSignBlock(Properties properties, @Nullable DyeColor backgroundColor) {
		super(properties, WoodType.SPRUCE);
		this.backgroundColor = backgroundColor;
	}

	@Nullable
	public DyeColor getBackgroundColor() {
		return this.backgroundColor;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return ModBlockEntityTypes.HANGING_CANVAS_SIGN.get().create(pos, state);
	}
}
