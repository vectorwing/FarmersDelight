package vectorwing.farmersdelight.blocks;

import net.minecraft.block.Block;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import vectorwing.farmersdelight.init.ModItems;
import vectorwing.farmersdelight.init.TileEntityInit;
import vectorwing.farmersdelight.utils.Text;

import javax.annotation.Nullable;
import java.util.List;

public class CookingPotBlock extends Block
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D);
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final ResourceLocation CONTENTS = new ResourceLocation("contents");

	public CookingPotBlock()
	{
		super(Properties.create(Material.IRON)
				.hardnessAndResistance(2.0F, 6.0F)
				.sound(SoundType.METAL));
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
											 Hand handIn, BlockRayTraceResult result) {
		if (!worldIn.isRemote) {
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof CookingPotTileEntity) {
				ItemStack serving = ((CookingPotTileEntity) tile).useHeldItemOnMeal(player.getHeldItem(handIn));
				if (serving != ItemStack.EMPTY && player != null) {
					player.inventory.addItemStackToInventory(serving);
					worldIn.playSound(null, pos, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.BLOCKS, 1.0F, 1.0F);
				} else {
					NetworkHooks.openGui((ServerPlayerEntity) player, (CookingPotTileEntity) tile, pos);
				}
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.SUCCESS;
	}

	@SuppressWarnings("deprecation")
	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
		ItemStack itemstack = super.getItem(worldIn, pos, state);
		CookingPotTileEntity tile = (CookingPotTileEntity)worldIn.getTileEntity(pos);
		CompoundNBT compoundnbt = tile.writeMealNbt(new CompoundNBT());
		if (!compoundnbt.isEmpty()) {
			itemstack.setTagInfo("BlockEntityTag", compoundnbt);
		}
		if (tile.hasCustomName()) {
			itemstack.setDisplayName(tile.getCustomName());
		}
		return itemstack;
	}

	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof CookingPotTileEntity) {
			CookingPotTileEntity tile = (CookingPotTileEntity)tileentity;

			if (!worldIn.isRemote) {
				ItemStack itemstack = new ItemStack(ModItems.COOKING_POT.get());
				CompoundNBT compoundnbt = tile.writeMealNbt(new CompoundNBT());
				if (!compoundnbt.isEmpty()) {
					itemstack.setTagInfo("BlockEntityTag", compoundnbt);
				}
				if (tile.hasCustomName()) {
					itemstack.setDisplayName(tile.getCustomName());
				}
				ItemEntity itementity = new ItemEntity(worldIn, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), itemstack);
				itementity.setDefaultPickupDelay();
				worldIn.addEntity(itementity);
			}
		}

		super.onBlockHarvested(worldIn, pos, state, player);
	}

	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof CookingPotTileEntity) {
				InventoryHelper.dropItems(worldIn, pos, ((CookingPotTileEntity)tileentity).getDroppableInventory());
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		CompoundNBT compoundnbt = stack.getChildTag("BlockEntityTag");
		if (compoundnbt != null) {
			if (compoundnbt.contains("Items", 9)) {
				NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
				ItemStackHelper.loadAllItems(compoundnbt, nonnulllist);
				ItemStack meal = nonnulllist.get(6);
				if (!meal.isEmpty()) {
					ITextComponent servingsOf = meal.getCount() == 1
							? Text.getTranslation("tooltip.cooking_pot.single_serving")
							: Text.getTranslation("tooltip.cooking_pot.many_servings", meal.getCount());
					tooltip.add(servingsOf.applyTextStyle(TextFormatting.GRAY));
					ITextComponent mealName = meal.getDisplayName().deepCopy();
					tooltip.add(mealName.applyTextStyle(meal.getRarity().color));
				}
			}
		} else {
			ITextComponent empty = Text.getTranslation("tooltip.cooking_pot.empty");
			tooltip.add(empty.applyTextStyle(TextFormatting.GRAY));
		}
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FACING);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityInit.COOKING_POT_TILE.get().create();
	}
}
