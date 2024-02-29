package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import vectorwing.farmersdelight.common.block.state.CanvasSign;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

import javax.annotation.Nullable;

public class WallHangingCanvasSignBlock extends WallHangingSignBlock implements CanvasSign
{
	private final DyeColor backgroundColor;

	public WallHangingCanvasSignBlock(Properties properties, @Nullable DyeColor backgroundColor) {
		super(WoodType.SPRUCE, properties);
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

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(level, pos, state, placer, stack);

		BlockEntity tileEntity = level.getBlockEntity(pos);
		Block block = state.getBlock();
		if (tileEntity instanceof SignBlockEntity signEntity && block instanceof CanvasSign canvasSignBlock) {
			if (canvasSignBlock.isDarkBackground()) {
				signEntity.updateText((signText) -> {
					return signText.setColor(DyeColor.WHITE);
				}, true);
				signEntity.updateText((signText) -> {
					return signText.setColor(DyeColor.WHITE);
				}, false);
			}
		}
	}
}
