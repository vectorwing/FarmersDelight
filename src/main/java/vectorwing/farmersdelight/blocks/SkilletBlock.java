package vectorwing.farmersdelight.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;
import vectorwing.farmersdelight.tile.SkilletTileEntity;

import javax.annotation.Nullable;

public class SkilletBlock extends HorizontalBlock
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 4.0D, 15.0D);

	public SkilletBlock() {
		super(Properties.create(Material.IRON)
				.hardnessAndResistance(0.5F, 6.0F)
				.sound(SoundType.LANTERN));
		this.setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH));
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof SkilletTileEntity) {
			SkilletTileEntity skilletEntity = (SkilletTileEntity) tileEntity;
			if (!worldIn.isRemote) {
				ItemStack heldStack = player.getHeldItem(handIn);
				EquipmentSlotType heldSlot = handIn.equals(Hand.MAIN_HAND) ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
				if (heldStack.isEmpty()) {
					ItemStack extractedStack = skilletEntity.removeItem();
					player.setItemStackToSlot(heldSlot, extractedStack);
					return ActionResultType.SUCCESS;
				} else {
					ItemStack remainderStack = skilletEntity.addItemToCook(heldStack, player);
					if (!remainderStack.equals(heldStack)) {
						player.setItemStackToSlot(heldSlot, remainderStack);
						worldIn.playSound(null, pos, SoundEvents.BLOCK_LANTERN_PLACE, SoundCategory.BLOCKS, 0.7F, 1.0F);
						return ActionResultType.SUCCESS;
					}
				}
			}
			return ActionResultType.CONSUME;
		}
		return ActionResultType.PASS;
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if (tileEntity instanceof SkilletTileEntity) {
				InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), ((SkilletTileEntity) tileEntity).getInventory().getStackInSlot(0));
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing());
	}

	@Override
	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
		ItemStack stack = super.getItem(worldIn, pos, state);
		SkilletTileEntity skilletEntity = (SkilletTileEntity) worldIn.getTileEntity(pos);
		CompoundNBT nbt = skilletEntity.writeSkilletItem(new CompoundNBT());
		if (!nbt.isEmpty()) {
			stack = ItemStack.read(nbt.getCompound("Skillet"));
		}
		return stack;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModTileEntityTypes.SKILLET_TILE.get().create();
	}
}
