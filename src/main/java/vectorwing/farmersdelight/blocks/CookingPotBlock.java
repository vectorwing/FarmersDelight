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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
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

	public CookingPotBlock() {
		super(Properties.create(Material.IRON)
				.hardnessAndResistance(2.0F, 6.0F)
				.sound(SoundType.LANTERN));
		this.setDefaultState(this.stateContainer.getBaseState().with(HORIZONTAL_FACING, Direction.NORTH).with(SUPPORT, CookingPotSupport.NONE).with(WATERLOGGED, false));
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player,
											 Hand handIn, BlockRayTraceResult result) {
		ItemStack heldStack = player.getHeldItem(handIn);
		if (heldStack.isEmpty() && player.isSneaking()) {
			world.setBlockState(pos, state.with(SUPPORT, state.get(SUPPORT).equals(CookingPotSupport.HANDLE)
					? getTrayState(world, pos) : CookingPotSupport.HANDLE));
			world.playSound(null, pos, SoundEvents.BLOCK_LANTERN_PLACE, SoundCategory.BLOCKS, 0.7F, 1.0F);
		} else if (!world.isRemote) {
			TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity instanceof CookingPotTileEntity) {
				CookingPotTileEntity cookingPotEntity = (CookingPotTileEntity) tileEntity;
				ItemStack servingStack = cookingPotEntity.useHeldItemOnMeal(heldStack);
				if (servingStack != ItemStack.EMPTY) {
					if (!player.inventory.addItemStackToInventory(servingStack)) {
						player.dropItem(servingStack, false);
					}
					world.playSound(null, pos, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.BLOCKS, 1.0F, 1.0F);
				} else {
					NetworkHooks.openGui((ServerPlayerEntity) player, cookingPotEntity, pos);
				}
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.SUCCESS;
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
		FluidState fluid = world.getFluidState(context.getPos());

		BlockState state = this.getDefaultState()
				.with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite())
				.with(WATERLOGGED, fluid.getFluid() == Fluids.WATER);

		if (context.getFace().equals(Direction.DOWN)) {
			return state.with(SUPPORT, CookingPotSupport.HANDLE);
		}
		return state.with(SUPPORT, getTrayState(world, pos));
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
		if (state.get(WATERLOGGED)) {
			world.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		if (facing.getAxis().equals(Direction.Axis.Y) && !state.get(SUPPORT).equals(CookingPotSupport.HANDLE)) {
			return state.with(SUPPORT, getTrayState(world, currentPos));
		}
		return state;
	}

	private CookingPotSupport getTrayState(IWorld world, BlockPos pos) {
		if (world.getBlockState(pos.down()).getBlock().isIn(ModTags.TRAY_HEAT_SOURCES)) {
			return CookingPotSupport.TRAY;
		}
		return CookingPotSupport.NONE;
	}

	@Override
	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
		ItemStack stack = super.getItem(worldIn, pos, state);
		CookingPotTileEntity cookingPotEntity = (CookingPotTileEntity) worldIn.getTileEntity(pos);
		if (cookingPotEntity != null) {
			CompoundNBT nbt = cookingPotEntity.writeMeal(new CompoundNBT());
			if (!nbt.isEmpty()) {
				stack.setTagInfo("BlockEntityTag", nbt);
			}
			if (cookingPotEntity.hasCustomName()) {
				stack.setDisplayName(cookingPotEntity.getCustomName());
			}
		}
		return stack;
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if (tileEntity instanceof CookingPotTileEntity) {
				CookingPotTileEntity cookingPotEntity = (CookingPotTileEntity) tileEntity;
				InventoryHelper.dropItems(worldIn, pos, cookingPotEntity.getDroppableInventory());
				cookingPotEntity.grantStoredRecipeExperience(worldIn, Vector3d.copyCentered(pos));
				worldIn.updateComparatorOutputLevel(pos, this);
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		CompoundNBT nbt = stack.getChildTag("BlockEntityTag");
		if (nbt != null) {
			CompoundNBT inventoryTag = nbt.getCompound("Inventory");
			if (inventoryTag.contains("Items", 9)) {
				ItemStackHandler handler = new ItemStackHandler();
				handler.deserializeNBT(inventoryTag);
				ItemStack mealStack = handler.getStackInSlot(6);
				if (!mealStack.isEmpty()) {
					IFormattableTextComponent textServingsOf = mealStack.getCount() == 1
							? TextUtils.getTranslation("tooltip.cooking_pot.single_serving")
							: TextUtils.getTranslation("tooltip.cooking_pot.many_servings", mealStack.getCount());
					tooltip.add(textServingsOf.mergeStyle(TextFormatting.GRAY));
					IFormattableTextComponent textMealName = mealStack.getDisplayName().deepCopy();
					tooltip.add(textMealName.mergeStyle(mealStack.getRarity().color));
				}
			}
		} else {
			IFormattableTextComponent textEmpty = TextUtils.getTranslation("tooltip.cooking_pot.empty");
			tooltip.add(textEmpty.mergeStyle(TextFormatting.GRAY));
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
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if (tileEntity instanceof CookingPotTileEntity) {
				((CookingPotTileEntity) tileEntity).setCustomName(stack.getDisplayName());
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof CookingPotTileEntity && ((CookingPotTileEntity) tileEntity).isHeated()) {
			CookingPotTileEntity cookingPotEntity = (CookingPotTileEntity) tileEntity;
			SoundEvent boilSound = !cookingPotEntity.getMeal().isEmpty()
					? ModSounds.BLOCK_COOKING_POT_BOIL_SOUP.get()
					: ModSounds.BLOCK_COOKING_POT_BOIL.get();
			double x = (double) pos.getX() + 0.5D;
			double y = pos.getY();
			double z = (double) pos.getZ() + 0.5D;
			if (rand.nextInt(10) == 0) {
				worldIn.playSound(x, y, z, boilSound, SoundCategory.BLOCKS, 0.5F, rand.nextFloat() * 0.2F + 0.9F, false);
			}
		}
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof CookingPotTileEntity) {
			ItemStackHandler inventory = ((CookingPotTileEntity) tileEntity).getInventory();
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
