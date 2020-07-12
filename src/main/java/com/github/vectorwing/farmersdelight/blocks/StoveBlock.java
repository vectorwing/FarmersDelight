package com.github.vectorwing.farmersdelight.blocks;

import com.github.vectorwing.farmersdelight.init.TileEntityInit;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CampfireCookingRecipe;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class StoveBlock extends Block
{
	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	public StoveBlock()
	{
		super(Properties.create(Material.ROCK)
				.hardnessAndResistance(2.0F, 6.0F)
				.sound(SoundType.STONE)
				.lightValue(13));
	}

	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (state.get(LIT)) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof StoveBlockTile) {
				StoveBlockTile stovetileentity = (StoveBlockTile)tileentity;
				ItemStack itemstack = player.getHeldItem(handIn);
				Optional<CampfireCookingRecipe> optional = stovetileentity.findMatchingRecipe(itemstack);
				if (optional.isPresent()) {
					if (!worldIn.isRemote && stovetileentity.addItem(player.abilities.isCreativeMode ? itemstack.copy() : itemstack, optional.get().getCookTime())) {
						player.addStat(Stats.INTERACT_WITH_CAMPFIRE);
						return ActionResultType.SUCCESS;
					}

					return ActionResultType.CONSUME;
				}
			}
		}

		return ActionResultType.PASS;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite())
				.with(LIT, context.getWorld().isBlockPowered(context.getPos()));
	}

	public int getLightValue(BlockState state) {
		return state.get(LIT) ? super.getLightValue(state) : 0;
	}

	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (!worldIn.isRemote) {
			boolean flag = state.get(LIT);
			if (flag != worldIn.isBlockPowered(pos)) {
				if (flag) {
					worldIn.getPendingBlockTicks().scheduleTick(pos, this, 4);
				} else {
					worldIn.setBlockState(pos, state.cycle(LIT), 2);
				}
			}
		}
	}
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if (state.get(LIT) && !worldIn.isBlockPowered(pos)) {
			worldIn.setBlockState(pos, state.cycle(LIT), 2);
		}
	}

	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof StoveBlockTile) {
				InventoryHelper.dropItems(worldIn, pos, ((StoveBlockTile)tileentity).getInventory());
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(LIT, FACING);
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (stateIn.get(CampfireBlock.LIT)) {
			double d0 = (double)pos.getX() + 0.5D;
			double d1 = (double)pos.getY();
			double d2 = (double)pos.getZ() + 0.5D;
			if (rand.nextInt(10) == 0) {
				worldIn.playSound(d0, d1, d2, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}

			Direction direction = stateIn.get(HorizontalBlock.HORIZONTAL_FACING);
			Direction.Axis direction$axis = direction.getAxis();
			double d3 = 0.52D;
			double d4 = rand.nextDouble() * 0.6D - 0.3D;
			double d5 = direction$axis == Direction.Axis.X ? (double)direction.getXOffset() * 0.52D : d4;
			double d6 = rand.nextDouble() * 6.0D / 16.0D;
			double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getZOffset() * 0.52D : d4;
			worldIn.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
			worldIn.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return TileEntityInit.STOVE_TILE.get().create();
	}
}
