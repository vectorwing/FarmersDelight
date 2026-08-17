package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import vectorwing.farmersdelight.common.block.state.CanvasSign;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

import javax.annotation.Nullable;

public class StandingCanvasSignBlock extends StandingSignBlock implements CanvasSign
{
	private final DyeColor backgroundColor;

	public StandingCanvasSignBlock(@Nullable DyeColor backgroundColor) {
		super(WoodType.SPRUCE, Properties.ofFullCopy(Blocks.SPRUCE_SIGN));
		this.backgroundColor = backgroundColor;
	}

	@Nullable
	public DyeColor getBackgroundColor() {
		return this.backgroundColor;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return ModBlockEntityTypes.CANVAS_SIGN.get().create(pos, state);
	}
}
