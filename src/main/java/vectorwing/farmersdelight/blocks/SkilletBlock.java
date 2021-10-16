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
import net.minecraft.item.crafting.CampfireCookingRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
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

import net.minecraft.block.AbstractBlock.Properties;

@SuppressWarnings("deprecation")
public class SkilletBlock extends HorizontalBlock
{
	public static final int MINIMUM_COOKING_TIME = 60;

	public static final BooleanProperty SUPPORT = BooleanProperty.create("support");

	protected static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 4.0D, 15.0D);
	protected static final VoxelShape SHAPE_WITH_TRAY = VoxelShapes.or(SHAPE, Block.box(0.0D, -1.0D, 0.0D, 16.0D, 0.0D, 16.0D));

	public SkilletBlock() {
		super(Properties.of(Material.METAL)
				.strength(0.5F, 6.0F)
				.sound(SoundType.LANTERN));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(SUPPORT, false));
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		TileEntity tileEntity = worldIn.getBlockEntity(pos);
		if (tileEntity instanceof SkilletTileEntity) {
			SkilletTileEntity skilletEntity = (SkilletTileEntity) tileEntity;
			if (!worldIn.isClientSide) {
				ItemStack heldStack = player.getItemInHand(handIn);
				EquipmentSlotType heldSlot = handIn.equals(Hand.MAIN_HAND) ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
				if (heldStack.isEmpty()) {
					ItemStack extractedStack = skilletEntity.removeItem();
					if (!player.isCreative()) {
						player.setItemSlot(heldSlot, extractedStack);
					}
					return ActionResultType.SUCCESS;
				} else {
					ItemStack remainderStack = skilletEntity.addItemToCook(heldStack, player);
					if (remainderStack.getCount() != heldStack.getCount()) {
						if (!player.isCreative()) {
							player.setItemSlot(heldSlot, remainderStack);
						}
						worldIn.playSound(null, pos, SoundEvents.LANTERN_PLACE, SoundCategory.BLOCKS, 0.7F, 1.0F);
						return ActionResultType.SUCCESS;
					}
				}
			}
			return ActionResultType.CONSUME;
		}
		return ActionResultType.PASS;
	}

	@Override
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileEntity = worldIn.getBlockEntity(pos);
			if (tileEntity instanceof SkilletTileEntity) {
				InventoryHelper.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), ((SkilletTileEntity) tileEntity).getInventory().getStackInSlot(0));
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return state.getValue(SUPPORT).equals(true) ? SHAPE_WITH_TRAY : SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState()
				.setValue(FACING, context.getHorizontalDirection())
				.setValue(SUPPORT, getTrayState(context.getLevel(), context.getClickedPos()));
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
		if (facing.getAxis().equals(Direction.Axis.Y)) {
			return state.setValue(SUPPORT, getTrayState(world, currentPos));
		}
		return state;
	}

	@Override
	public ItemStack getCloneItemStack(IBlockReader worldIn, BlockPos pos, BlockState state) {
		ItemStack stack = super.getCloneItemStack(worldIn, pos, state);
		SkilletTileEntity skilletEntity = (SkilletTileEntity) worldIn.getBlockEntity(pos);
		CompoundNBT nbt = skilletEntity.writeSkilletItem(new CompoundNBT());
		if (!nbt.isEmpty()) {
			stack = ItemStack.of(nbt.getCompound("Skillet"));
		}
		return stack;
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, SUPPORT);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		TileEntity tileEntity = worldIn.getBlockEntity(pos);
		if (tileEntity instanceof SkilletTileEntity) {
			SkilletTileEntity skilletEntity = (SkilletTileEntity) tileEntity;
			if (skilletEntity.isCooking()) {
				double x = (double) pos.getX() + 0.5D;
				double y = pos.getY();
				double z = (double) pos.getZ() + 0.5D;
				if (rand.nextInt(10) == 0) {
					worldIn.playLocalSound(x, y, z, ModSounds.BLOCK_SKILLET_SIZZLE.get(), SoundCategory.BLOCKS, 0.4F, rand.nextFloat() * 0.2F + 0.9F, false);
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
		return world.getBlockState(pos.below()).getBlock().is(ModTags.TRAY_HEAT_SOURCES);
	}

	/**
	 * Calculates the total cooking time for the Skillet, affected by Fire Aspect.
	 * Assuming a default of 30 seconds (600 ticks), the time is divided by 5, then reduced further per level of Fire Aspect, to a minimum of 3 seconds.
	 * Times are always rounded to a multiple of 20, to ensure exact seconds.
	 */
	public static int getSkilletCookingTime(int originalCookingTime, int fireAspectLevel) {
		int cookingTime = originalCookingTime > 0 ? originalCookingTime : 600;
		int cookingSeconds = cookingTime / 20;
		float cookingTimeReduction = 0.2F;

		if (fireAspectLevel > 0) {
			cookingTimeReduction -= fireAspectLevel * 0.05;
		}

		int result = (int) (cookingSeconds * cookingTimeReduction) * 20;

		return MathHelper.clamp(result, MINIMUM_COOKING_TIME, originalCookingTime);
	}
}
