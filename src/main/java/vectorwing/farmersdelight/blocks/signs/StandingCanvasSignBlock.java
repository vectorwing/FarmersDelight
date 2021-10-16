package vectorwing.farmersdelight.blocks.signs;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;

import javax.annotation.Nullable;

public class StandingCanvasSignBlock extends StandingSignBlock implements ICanvasSign
{
	private final DyeColor backgroundColor;

	public StandingCanvasSignBlock(@Nullable DyeColor backgroundColor) {
		super(Properties.copy(Blocks.OAK_SIGN), WoodType.SPRUCE);
		this.backgroundColor = backgroundColor;
	}

	@Nullable
	public DyeColor getBackgroundColor() {
		return this.backgroundColor;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return ModTileEntityTypes.CANVAS_SIGN_TILE.get().create(pos, state);
	}

	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		Block block = state.getBlock();
		if (tileEntity instanceof SignBlockEntity && block instanceof ICanvasSign) {
			if (((ICanvasSign) block).isDarkBackground()) {
				((SignBlockEntity) tileEntity).setColor(DyeColor.WHITE);
			}
		}
	}
}
