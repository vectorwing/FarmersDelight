package vectorwing.farmersdelight.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.items.KnifeItem;
import vectorwing.farmersdelight.registry.ModSounds;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;
import vectorwing.farmersdelight.tile.CuttingBoardTileEntity;

import javax.annotation.Nullable;

public class CuttingBoardBlock extends Block implements IWaterLoggable
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 1.0D, 15.0D);
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public CuttingBoardBlock()
	{
		super(Properties.create(Material.WOOD).hardnessAndResistance(2.0F).sound(SoundType.WOOD));
		this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.NORTH).with(WATERLOGGED, false));
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if (tileentity instanceof CuttingBoardTileEntity) {
			CuttingBoardTileEntity cuttingBoardTE = (CuttingBoardTileEntity) tileentity;
			ItemStack itemHeld = player.getHeldItem(handIn);
			ItemStack itemOffhand = player.getHeldItemOffhand();

			// Placing items on the board. It should prefer off-hand placement, unless it's a BlockItem (since it never passes to off-hand...)
			if (cuttingBoardTE.isEmpty()) {
				if (!itemOffhand.isEmpty() && handIn.equals(Hand.MAIN_HAND) && !(itemHeld.getItem() instanceof BlockItem)) {
					return ActionResultType.PASS; // main-hand passes to off-hand
				}
				if (itemHeld.isEmpty())	{
					return ActionResultType.PASS;
				} else if (cuttingBoardTE.addItem(player.abilities.isCreativeMode ? itemHeld.copy() : itemHeld)) {
					worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 0.8F);
					return ActionResultType.SUCCESS;
				}
			// Processing the item with the held tool
			} else if (!itemHeld.isEmpty()) {
				ItemStack boardItem = cuttingBoardTE.getStoredItem().copy();
				if (cuttingBoardTE.processItemUsingTool(itemHeld, player)) {
					spawnCuttingParticles(worldIn, pos, boardItem, 5);
					playProcessingSound(worldIn, pos, itemHeld, boardItem);
					return ActionResultType.SUCCESS;
				}
				return ActionResultType.PASS;
			// Removing the board's item
			} else if (handIn.equals(Hand.MAIN_HAND)) {
				if (!player.isCreative()) {
					InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), cuttingBoardTE.removeItem());
				} else {
					cuttingBoardTE.removeItem();
				}
				worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_WOOD_HIT, SoundCategory.BLOCKS, 0.25F, 0.5F);
				return ActionResultType.SUCCESS;
			}

		}
		return ActionResultType.PASS;
	}

	// TODO: I am 100% sure there is a less cluttered way to pull off conditional sounds. If you're reading this, please, HELP ME! :(
	public static void playProcessingSound(World worldIn, BlockPos pos, ItemStack toolStack, ItemStack boardStack) {
		if (toolStack.getItem() instanceof ShearsItem) {
			worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
		} else if (toolStack.getItem() instanceof KnifeItem) {
			worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSounds.BLOCK_CUTTING_BOARD_KNIFE.get(), SoundCategory.BLOCKS, 0.8F, 1.0F);
		} else if (boardStack.getItem() instanceof BlockItem) {
			Block block = ((BlockItem) boardStack.getItem()).getBlock();
			if (block instanceof LogBlock) {
				worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 0.9F, 0.8F);
			} else {
				SoundType sound = block.getSoundType(block.getDefaultState());
				worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), sound.getBreakSound(), SoundCategory.BLOCKS, 1.0F, 0.8F);
			}
		} else {
			worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 1.0F, 0.8F);
		}
	}

	public static void spawnCuttingParticles(World worldIn, BlockPos pos, ItemStack stack, int count) {
		for(int i = 0; i < count; ++i) {
			Vec3d vec3d = new Vec3d(((double) worldIn.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, ((double) worldIn.rand.nextFloat() - 0.5D) * 0.1D);
			if (worldIn instanceof ServerWorld) {
				((ServerWorld) worldIn).spawnParticle(new ItemParticleData(ParticleTypes.ITEM, stack), pos.getX() + 0.5F, pos.getY() + 0.1F, pos.getZ() + 0.5F, 1, vec3d.x, vec3d.y + 0.05D, vec3d.z, 0.0D);
			} else {
				worldIn.addParticle(new ItemParticleData(ParticleTypes.ITEM, stack), pos.getX() + 0.5F, pos.getY() + 0.1F, pos.getZ() + 0.5F, vec3d.x, vec3d.y + 0.05D, vec3d.z);
			}
		}
	}

	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof CuttingBoardTileEntity) {
				InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), ((CuttingBoardTileEntity)tileentity).getStoredItem());
				worldIn.updateComparatorOutputLevel(pos, this);
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	public boolean canSpawnInBlock() {
		return true;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite())
				.with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER);
	}

	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}
		return facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos)
				? Blocks.AIR.getDefaultState()
				: super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockPos blockpos = pos.down();
		return hasSolidSideOnTop(worldIn, blockpos) || hasEnoughSolidSide(worldIn, blockpos, Direction.UP);
	}

	@Override
	protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FACING, WATERLOGGED);
	}

	public IFluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

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
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return ModTileEntityTypes.CUTTING_BOARD_TILE.get().create();
	}

	@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class ToolCarvingEvent {
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
