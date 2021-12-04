package vectorwing.farmersdelight.blocks.signs;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock.Properties;

public class WallCanvasSignBlock extends WallSignBlock implements ICanvasSign
{
	private final DyeColor backgroundColor;

	public WallCanvasSignBlock(Properties properties, @Nullable DyeColor backgroundColor) {
		super(properties, WoodType.SPRUCE);
		this.backgroundColor = backgroundColor;
	}

	@Nullable
	public DyeColor getBackgroundColor() {
		return this.backgroundColor;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModTileEntityTypes.CANVAS_SIGN_TILE.get().create();
	}

	@Override
	public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		TileEntity tileEntity = worldIn.getBlockEntity(pos);
		Block block = state.getBlock();
		if (tileEntity instanceof SignTileEntity && block instanceof ICanvasSign) {
			if (((ICanvasSign) block).isDarkBackground()) {
				((SignTileEntity) tileEntity).setColor(DyeColor.WHITE);
			}
		}
	}
}
