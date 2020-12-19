package vectorwing.farmersdelight.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.*;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;
import vectorwing.farmersdelight.tile.CuttingBoardTileEntity;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class CuttingBoardBlock extends Block implements IWaterLoggable
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D);
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public CuttingBoardBlock() {
		super(Properties.create(Material.WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD));
		this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if (tile instanceof CuttingBoardTileEntity) {
			CuttingBoardTileEntity cuttingBoardTile = (CuttingBoardTileEntity) tile;
			ItemStack itemHeld = player.getHeldItem(handIn);
			ItemStack itemOffhand = player.getHeldItemOffhand();

			// Placing items on the board. It should prefer off-hand placement, unless it's a BlockItem (since it never passes to off-hand...)
			if (cuttingBoardTile.isEmpty()) {
				if (!itemOffhand.isEmpty() && handIn.equals(Hand.MAIN_HAND) && !(itemHeld.getItem() instanceof BlockItem)) {
					return ActionResultType.PASS; // main-hand passes to off-hand
				}
				if (itemHeld.isEmpty()) {
					return ActionResultType.PASS;
				}
				else if (cuttingBoardTile.addItem(player.abilities.isCreativeMode ? itemHeld.copy() : itemHeld)) {
					worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
					return ActionResultType.SUCCESS;
				}

			// Processing the item with the held tool
			} else if (!itemHeld.isEmpty()) {
				ItemStack boardItem = cuttingBoardTile.getStoredItem().copy();
				if (cuttingBoardTile.processItemUsingTool(itemHeld, player)) {
					spawnCuttingParticles(worldIn, pos, boardItem, 5);
					return ActionResultType.SUCCESS;
				}
				return ActionResultType.PASS;

			// Removing the board's item
			} else if (handIn.equals(Hand.MAIN_HAND)) {
				if (!player.isCreative()) {
					InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), cuttingBoardTile.removeItem());
				}
				else {
					cuttingBoardTile.removeItem();
				}
				worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_WOOD_HIT, SoundCategory.BLOCKS, 0.25F, 0.5F);
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}

	public static void spawnCuttingParticles(World worldIn, BlockPos pos, ItemStack stack, int count) {
		for (int i = 0; i < count; ++i) {
			Vector3d vec3d = new Vector3d(((double) worldIn.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, ((double) worldIn.rand.nextFloat() - 0.5D) * 0.1D);
			if (worldIn instanceof ServerWorld) {
				((ServerWorld) worldIn).spawnParticle(new ItemParticleData(ParticleTypes.ITEM, stack), pos.getX() + 0.5F, pos.getY() + 0.1F, pos.getZ() + 0.5F, 1, vec3d.x, vec3d.y + 0.05D, vec3d.z, 0.0D);
			} else {
				worldIn.addParticle(new ItemParticleData(ParticleTypes.ITEM, stack), pos.getX() + 0.5F, pos.getY() + 0.1F, pos.getZ() + 0.5F, vec3d.x, vec3d.y + 0.05D, vec3d.z);
			}
		}
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof CuttingBoardTileEntity) {
				InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), ((CuttingBoardTileEntity) tile).getStoredItem());
				worldIn.updateComparatorOutputLevel(pos, this);
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public boolean canSpawnInBlock() {
		return true;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		FluidState fluid = context.getWorld().getFluidState(context.getPos());
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite())
				.with(WATERLOGGED, fluid.getFluid() == Fluids.WATER);
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}
		return facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos)
				? Blocks.AIR.getDefaultState()
				: super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockPos floorPos = pos.down();
		return hasSolidSideOnTop(worldIn, floorPos) || hasEnoughSolidSide(worldIn, floorPos, Direction.UP);
	}

	@Override
	protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FACING, WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		if (worldIn.getTileEntity(pos) instanceof CuttingBoardTileEntity) {
			ItemStack boardItem = ((CuttingBoardTileEntity) worldIn.getTileEntity(pos)).getStoredItem();
			return !boardItem.isEmpty() ? 15 : 0;
		}
		return 0;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModTileEntityTypes.CUTTING_BOARD_TILE.get().create();
	}

	@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class ToolCarvingEvent
	{
		@SubscribeEvent
		public static void onSneakPlaceTool(PlayerInteractEvent.RightClickBlock event) {
			World world = event.getWorld();
			BlockPos pos = event.getPos();
			PlayerEntity player = event.getPlayer();
			ItemStack heldItem = player.getHeldItemMainhand();
			if (player.isSecondaryUseActive() && !heldItem.isEmpty() && world.getTileEntity(event.getPos()) instanceof CuttingBoardTileEntity) {
				if (heldItem.getItem() instanceof TieredItem ||
						heldItem.getItem() instanceof TridentItem ||
						heldItem.getItem() instanceof ShearsItem) {
					boolean success = ((CuttingBoardTileEntity) world.getTileEntity(event.getPos())).carveToolOnBoard(player.abilities.isCreativeMode ? heldItem.copy() : heldItem);
					if (success) {
						world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
						event.setCanceled(true);
						event.setCancellationResult(ActionResultType.SUCCESS);
					}
				}
			}
		}
	}
}
