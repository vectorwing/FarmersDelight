package vectorwing.farmersdelight.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.blocks.state.CookingPotSupport;
import vectorwing.farmersdelight.registry.ModSounds;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;
import vectorwing.farmersdelight.tile.CookingPotTileEntity;
import vectorwing.farmersdelight.utils.MathUtils;
import vectorwing.farmersdelight.utils.TextUtils;
import vectorwing.farmersdelight.utils.tags.ModTags;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class CookingPotBlock extends HorizontalBlock implements IWaterLoggable
{
	public static final EnumProperty<CookingPotSupport> SUPPORT = EnumProperty.create("support", CookingPotSupport.class);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D);
	protected static final VoxelShape SHAPE_WITH_TRAY = VoxelShapes.or(SHAPE, Block.makeCuboidShape(0.0D, -1.0D, 0.0D, 16.0D, 0.0D, 16.0D));
	protected static final VoxelShape HANGING_AREA = Block.makeCuboidShape(7.0D, 0.0D, 7.0D, 9.0D, 10.0D, 9.0D);

	public CookingPotBlock() {
		super(Properties.create(Material.IRON)
				.hardnessAndResistance(2.0F, 6.0F)
				.sound(SoundType.LANTERN));
		this.setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH).with(SUPPORT, CookingPotSupport.NONE).with(WATERLOGGED, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return state.get(SUPPORT).equals(CookingPotSupport.TRAY) ? SHAPE_WITH_TRAY : SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockPos pos = context.getPos();
		World world = context.getWorld();
		FluidState ifluidstate = world.getFluidState(context.getPos());

		BlockState state = this.getDefaultState()
				.with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite())
				.with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER);

		if (context.getFace().equals(Direction.DOWN) && canHangFrom(world, pos.up())) {
			return state.with(SUPPORT, CookingPotSupport.HANDLE);
		}
		return state.with(SUPPORT, getTrayState(world, pos));
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}
		if (facing.getAxis().equals(Direction.Axis.Y) && !canHangFrom(worldIn, currentPos.up())) {
			return stateIn.with(SUPPORT, getTrayState(worldIn, currentPos));
		}
		return stateIn;
	}

	private CookingPotSupport getTrayState(IWorld world, BlockPos pos) {
		if (world.getBlockState(pos.down()).getBlock().isIn(ModTags.TRAY_HEAT_SOURCES)) {
			return CookingPotSupport.TRAY;
		}
		return CookingPotSupport.NONE;
	}

	public static boolean canHangFrom(IWorldReader worldIn, BlockPos pos) {
		BlockState state = worldIn.getBlockState(pos);
		boolean canSupport = !VoxelShapes.compare(state.getShape(worldIn, pos).project(Direction.DOWN), HANGING_AREA, IBooleanFunction.ONLY_SECOND);
		return !state.isIn(BlockTags.UNSTABLE_BOTTOM_CENTER) && canSupport;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
											 Hand handIn, BlockRayTraceResult result) {
		if (!worldIn.isRemote) {
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof CookingPotTileEntity) {
				ItemStack serving = ((CookingPotTileEntity) tile).useHeldItemOnMeal(player.getHeldItem(handIn));
				if (serving != ItemStack.EMPTY) {
					if (!player.inventory.addItemStackToInventory(serving)) {
						player.dropItem(serving, false);
					}
					worldIn.playSound(null, pos, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.BLOCKS, 1.0F, 1.0F);
				} else {
					NetworkHooks.openGui((ServerPlayerEntity) player, (CookingPotTileEntity) tile, pos);
				}
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
		ItemStack itemstack = super.getItem(worldIn, pos, state);
		CookingPotTileEntity tile = (CookingPotTileEntity) worldIn.getTileEntity(pos);
		CompoundNBT compoundnbt = tile.writeMeal(new CompoundNBT());
		if (!compoundnbt.isEmpty()) {
			itemstack.setTagInfo("BlockEntityTag", compoundnbt);
		}
		if (tile.hasCustomName()) {
			itemstack.setDisplayName(tile.getCustomName());
		}
		return itemstack;
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof CookingPotTileEntity) {
				InventoryHelper.dropItems(worldIn, pos, ((CookingPotTileEntity) tileentity).getDroppableInventory());
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		CompoundNBT compoundnbt = stack.getChildTag("BlockEntityTag");
		if (compoundnbt != null) {
			CompoundNBT inventoryTag = compoundnbt.getCompound("Inventory");
			if (inventoryTag.contains("Items", 9)) {
				ItemStackHandler handler = new ItemStackHandler();
				handler.deserializeNBT(inventoryTag);
				ItemStack meal = handler.getStackInSlot(6);
				if (!meal.isEmpty()) {
					IFormattableTextComponent servingsOf = meal.getCount() == 1
							? TextUtils.getTranslation("tooltip.cooking_pot.single_serving")
							: TextUtils.getTranslation("tooltip.cooking_pot.many_servings", meal.getCount());
					tooltip.add(servingsOf.mergeStyle(TextFormatting.GRAY));
					IFormattableTextComponent mealName = meal.getDisplayName().deepCopy();
					tooltip.add(mealName.mergeStyle(meal.getRarity().color));
				}
			}
		} else {
			IFormattableTextComponent empty = TextUtils.getTranslation("tooltip.cooking_pot.empty");
			tooltip.add(empty.mergeStyle(TextFormatting.GRAY));
		}
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(HORIZONTAL_FACING, SUPPORT, WATERLOGGED);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (stack.hasDisplayName()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof CookingPotTileEntity) {
				((CookingPotTileEntity) tileentity).setCustomName(stack.getDisplayName());
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof CookingPotTileEntity && ((CookingPotTileEntity) tileentity).isAboveLitHeatSource()) {
			double d0 = (double) pos.getX() + 0.5D;
			double d1 = pos.getY();
			double d2 = (double) pos.getZ() + 0.5D;
			if (rand.nextInt(10) == 0) {
				worldIn.playSound(d0, d1, d2, ModSounds.BLOCK_COOKING_POT_BOIL.get(), SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.2F + 0.9F, false);
			}
		}
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof CookingPotTileEntity) {
			ItemStackHandler inventory = ((CookingPotTileEntity) tile).getInventory();
			return MathUtils.calcRedstoneFromItemHandler(inventory);
		}
		return 0;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModTileEntityTypes.COOKING_POT_TILE.get().create();
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}
}
