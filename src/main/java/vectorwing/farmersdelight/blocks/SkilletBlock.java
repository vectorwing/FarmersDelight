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
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vectorwing.farmersdelight.registry.ModSounds;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;
import vectorwing.farmersdelight.tile.SkilletTileEntity;
import vectorwing.farmersdelight.utils.tags.ModTags;

import javax.annotation.Nullable;
import java.util.Random;

@SuppressWarnings("deprecation")
public class SkilletBlock extends HorizontalBlock
{
	public static final BooleanProperty SUPPORT = BooleanProperty.create("support");

	protected static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 4.0D, 15.0D);
	protected static final VoxelShape SHAPE_WITH_TRAY = VoxelShapes.or(SHAPE, Block.makeCuboidShape(0.0D, -1.0D, 0.0D, 16.0D, 0.0D, 16.0D));

	public SkilletBlock() {
		super(Properties.create(Material.IRON)
				.hardnessAndResistance(0.5F, 6.0F)
				.sound(SoundType.LANTERN));
		this.setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH).with(SUPPORT, false));
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
					if (!player.isCreative()) {
						player.setItemStackToSlot(heldSlot, extractedStack);
					}
					return ActionResultType.SUCCESS;
				} else {
					ItemStack remainderStack = skilletEntity.addItemToCook(heldStack, player);
					if (remainderStack.getCount() != heldStack.getCount()) {
						if (!player.isCreative()) {
							player.setItemStackToSlot(heldSlot, remainderStack);
						}
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
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return state.get(SUPPORT).equals(true) ? SHAPE_WITH_TRAY : SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState()
				.with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing())
				.with(SUPPORT, getTrayState(context.getWorld(), context.getPos()));
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
		if (facing.getAxis().equals(Direction.Axis.Y)) {
			return state.with(SUPPORT, getTrayState(world, currentPos));
		}
		return state;
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
		builder.add(HORIZONTAL_FACING, SUPPORT);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof SkilletTileEntity) {
			SkilletTileEntity skilletEntity = (SkilletTileEntity) tileEntity;
			if (skilletEntity.isCooking()) {
				double x = (double) pos.getX() + 0.5D;
				double y = pos.getY();
				double z = (double) pos.getZ() + 0.5D;
				if (rand.nextInt(10) == 0) {
					worldIn.playSound(x, y, z, ModSounds.BLOCK_SKILLET_SIZZLE.get(), SoundCategory.BLOCKS, 0.4F, rand.nextFloat() * 0.2F + 0.9F, false);
				}
			}
		}
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

	private boolean getTrayState(IWorld world, BlockPos pos) {
		return world.getBlockState(pos.down()).getBlock().isIn(ModTags.TRAY_HEAT_SOURCES);
	}
}
