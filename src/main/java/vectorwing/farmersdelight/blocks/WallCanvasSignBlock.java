package vectorwing.farmersdelight.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WoodType;
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

public class WallCanvasSignBlock extends WallSignBlock
{
	private final DyeColor defaultTextColor;

	public WallCanvasSignBlock(Properties properties) {
		super(properties, WoodType.SPRUCE);
		this.defaultTextColor = DyeColor.BLACK;
	}

	public WallCanvasSignBlock(Properties properties, DyeColor defaultTextColorIn) {
		super(properties, WoodType.SPRUCE);
		this.defaultTextColor = defaultTextColorIn;
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
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof SignTileEntity) {
			((SignTileEntity) tile).setTextColor(defaultTextColor);
		}
	}
}
