package vectorwing.farmersdelight.blocks;

import net.minecraft.block.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.*;
import net.minecraft.item.crafting.CampfireCookingRecipe;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vectorwing.farmersdelight.registry.ModSounds;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;
import vectorwing.farmersdelight.tile.StoveTileEntity;
import vectorwing.farmersdelight.utils.MathUtils;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

@SuppressWarnings("deprecation")
public class StoveBlock extends HorizontalBlock
{
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public StoveBlock(AbstractBlock.Properties builder) {
		super(builder);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		ItemStack itemstack = player.getHeldItem(handIn);
		Item usedItem = itemstack.getItem();
		if (state.get(LIT)) {
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof StoveTileEntity) {
				StoveTileEntity stovetileentity = (StoveTileEntity) tile;
				Optional<CampfireCookingRecipe> optional = stovetileentity.findMatchingRecipe(itemstack);
				if (optional.isPresent()) {
					if (!worldIn.isRemote && !stovetileentity.isStoveBlockedAbove() && stovetileentity.addItem(player.abilities.isCreativeMode ? itemstack.copy() : itemstack, optional.get().getCookTime())) {
						player.addStat(Stats.INTERACT_WITH_CAMPFIRE);
						return ActionResultType.SUCCESS;
					}
					return ActionResultType.CONSUME;
				} else {
					if (usedItem instanceof ShovelItem) {
						extinguish(state, worldIn, pos);
						return ActionResultType.SUCCESS;
					} else if (usedItem == Items.WATER_BUCKET) {
						extinguish(state, worldIn, pos);
						player.setHeldItem(handIn, new ItemStack(Items.BUCKET));
						return ActionResultType.SUCCESS;
					}
				}
			}
		} else {
			if (itemstack.getItem() instanceof FlintAndSteelItem) {
				worldIn.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, MathUtils.RAND.nextFloat() * 0.4F + 0.8F);
				worldIn.setBlockState(pos, state.with(BlockStateProperties.LIT, Boolean.TRUE), 11);
				itemstack.damageItem(1, player, action -> action.sendBreakAnimation(handIn));

				return ActionResultType.SUCCESS;
			}
		}

		return ActionResultType.PASS;
	}

	public void extinguish(BlockState state, World worldIn, BlockPos pos) {
		worldIn.setBlockState(pos, state.with(LIT, false), 2);
		double d0 = (double) pos.getX() + 0.5D;
		double d1 = pos.getY();
		double d2 = (double) pos.getZ() + 0.5D;
		worldIn.playSound(d0, d1, d2, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F, false);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite()).with(LIT, true);
	}

	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
		boolean isLit = worldIn.getBlockState(pos).get(StoveBlock.LIT);
		if (isLit && !entityIn.isImmuneToFire() && entityIn instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entityIn)) {
			entityIn.attackEntityFrom(DamageSource.HOT_FLOOR, 1.0F);
		}

		super.onEntityWalk(worldIn, pos, entityIn);
	}

	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof StoveTileEntity) {
				InventoryHelper.dropItems(worldIn, pos, ((StoveTileEntity) tile).getInventory());
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(LIT, HORIZONTAL_FACING);
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (stateIn.get(CampfireBlock.LIT)) {
			double d0 = (double) pos.getX() + 0.5D;
			double d1 = pos.getY();
			double d2 = (double) pos.getZ() + 0.5D;
			if (rand.nextInt(10) == 0) {
				worldIn.playSound(d0, d1, d2, ModSounds.BLOCK_STOVE_CRACKLE.get(), SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}

			Direction direction = stateIn.get(HorizontalBlock.HORIZONTAL_FACING);
			Direction.Axis direction$axis = direction.getAxis();
			double d4 = rand.nextDouble() * 0.6D - 0.3D;
			double d5 = direction$axis == Direction.Axis.X ? (double) direction.getXOffset() * 0.52D : d4;
			double d6 = rand.nextDouble() * 6.0D / 16.0D;
			double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getZOffset() * 0.52D : d4;
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
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModTileEntityTypes.STOVE_TILE.get().create();
	}
}
