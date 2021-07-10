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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import vectorwing.farmersdelight.registry.ModSounds;
import vectorwing.farmersdelight.registry.ModTileEntityTypes;
import vectorwing.farmersdelight.tile.StoveTileEntity;
import vectorwing.farmersdelight.utils.ItemUtils;
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
		ItemStack heldStack = player.getHeldItem(handIn);
		Item heldItem = heldStack.getItem();

		if (state.get(LIT)) {
			if (heldStack.getToolTypes().contains(ToolType.SHOVEL)) {
				extinguish(state, worldIn, pos);
				return ActionResultType.SUCCESS;
			} else if (heldItem == Items.WATER_BUCKET) {
				extinguish(state, worldIn, pos);
				player.setHeldItem(handIn, new ItemStack(Items.BUCKET));
				return ActionResultType.SUCCESS;
			}
		} else {
			if (heldItem instanceof FlintAndSteelItem) {
				worldIn.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, MathUtils.RAND.nextFloat() * 0.4F + 0.8F);
				worldIn.setBlockState(pos, state.with(BlockStateProperties.LIT, Boolean.TRUE), 11);
				heldStack.damageItem(1, player, action -> action.sendBreakAnimation(handIn));
				return ActionResultType.SUCCESS;
			} else if (heldItem instanceof FireChargeItem) {
				worldIn.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, (MathUtils.RAND.nextFloat() - MathUtils.RAND.nextFloat()) * 0.2F + 1.0F);
				worldIn.setBlockState(pos, state.with(BlockStateProperties.LIT, Boolean.TRUE), 11);
				if (!player.isCreative()) {
					heldStack.shrink(1);
				}
				return ActionResultType.SUCCESS;
			}
		}

		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof StoveTileEntity) {
			StoveTileEntity stoveEntity = (StoveTileEntity) tileEntity;
			Optional<CampfireCookingRecipe> recipe = stoveEntity.findMatchingRecipe(heldStack);
			if (recipe.isPresent()) {
				if (!worldIn.isRemote && !stoveEntity.isStoveBlockedAbove() && stoveEntity.addItem(player.abilities.isCreativeMode ? heldStack.copy() : heldStack, recipe.get().getCookTime())) {
					return ActionResultType.SUCCESS;
				}
				return ActionResultType.CONSUME;
			}
		}

		return ActionResultType.PASS;
	}

	public void extinguish(BlockState state, World worldIn, BlockPos pos) {
		worldIn.setBlockState(pos, state.with(LIT, false), 2);
		double x = (double) pos.getX() + 0.5D;
		double y = pos.getY();
		double z = (double) pos.getZ() + 0.5D;
		worldIn.playSound(x, y, z, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F, false);
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
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if (tileEntity instanceof StoveTileEntity) {
//				InventoryHelper.dropItems(worldIn, pos, ((StoveTileEntity) tileEntity).getOldInventory());
				ItemUtils.dropItems(worldIn, pos, ((StoveTileEntity) tileEntity).getInventory());
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
			double x = (double) pos.getX() + 0.5D;
			double y = pos.getY();
			double z = (double) pos.getZ() + 0.5D;
			if (rand.nextInt(10) == 0) {
				worldIn.playSound(x, y, z, ModSounds.BLOCK_STOVE_CRACKLE.get(), SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}

			Direction direction = stateIn.get(HorizontalBlock.HORIZONTAL_FACING);
			Direction.Axis direction$axis = direction.getAxis();
			double horizontalOffset = rand.nextDouble() * 0.6D - 0.3D;
			double xOffset = direction$axis == Direction.Axis.X ? (double) direction.getXOffset() * 0.52D : horizontalOffset;
			double yOffset = rand.nextDouble() * 6.0D / 16.0D;
			double zOffset = direction$axis == Direction.Axis.Z ? (double) direction.getZOffset() * 0.52D : horizontalOffset;
			worldIn.addParticle(ParticleTypes.SMOKE, x + xOffset, y + yOffset, z + zOffset, 0.0D, 0.0D, 0.0D);
			worldIn.addParticle(ParticleTypes.FLAME, x + xOffset, y + yOffset, z + zOffset, 0.0D, 0.0D, 0.0D);
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
