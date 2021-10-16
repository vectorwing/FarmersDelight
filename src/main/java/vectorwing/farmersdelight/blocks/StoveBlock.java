package vectorwing.farmersdelight.blocks;

import net.minecraft.block.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.item.*;
import net.minecraft.item.crafting.CampfireCookingRecipe;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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

import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("deprecation")
public class StoveBlock extends HorizontalDirectionalBlock
{
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public StoveBlock(BlockBehaviour.Properties builder) {
		super(builder);
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		ItemStack heldStack = player.getItemInHand(handIn);
		Item heldItem = heldStack.getItem();

		if (state.getValue(LIT)) {
			if (ItemTags. heldStack.getToolTypes().contains(ToolType.SHOVEL)) {
				extinguish(state, worldIn, pos);
				heldStack.hurtAndBreak(1, player, action -> action.broadcastBreakEvent(handIn));
				return InteractionResult.SUCCESS;
			} else if (heldItem == Items.WATER_BUCKET) {
				if (!worldIn.isClientSide()) {
					worldIn.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
				}
				extinguish(state, worldIn, pos);
				if (!player.isCreative()) {
					player.setItemInHand(handIn, new ItemStack(Items.BUCKET));
				}
				return InteractionResult.SUCCESS;
			}
		} else {
			if (heldItem instanceof FlintAndSteelItem) {
				worldIn.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, MathUtils.RAND.nextFloat() * 0.4F + 0.8F);
				worldIn.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
				heldStack.hurtAndBreak(1, player, action -> action.broadcastBreakEvent(handIn));
				return InteractionResult.SUCCESS;
			} else if (heldItem instanceof FireChargeItem) {
				worldIn.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (MathUtils.RAND.nextFloat() - MathUtils.RAND.nextFloat()) * 0.2F + 1.0F);
				worldIn.setBlock(pos, state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
				if (!player.isCreative()) {
					heldStack.shrink(1);
				}
				return InteractionResult.SUCCESS;
			}
		}

		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		if (tileEntity instanceof StoveTileEntity) {
			StoveTileEntity stoveEntity = (StoveTileEntity) tileEntity;
			if (!worldIn.isClientSide && !stoveEntity.isStoveBlockedAbove() && stoveEntity.addItem(player.abilities.instabuild ? heldStack.copy() : heldStack)) {
				return InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.PASS;
	}

	public void extinguish(BlockState state, Level worldIn, BlockPos pos) {
		worldIn.setBlock(pos, state.setValue(LIT, false), 2);
		double x = (double) pos.getX() + 0.5D;
		double y = pos.getY();
		double z = (double) pos.getZ() + 0.5D;
		worldIn.playLocalSound(x, y, z, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F, false);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(LIT, true);
	}

	@Override
	public void stepOn(Level worldIn, BlockPos pos, Entity entityIn) {
		boolean isLit = worldIn.getBlockState(pos).getValue(StoveBlock.LIT);
		if (isLit && !entityIn.fireImmune() && entityIn instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entityIn)) {
			entityIn.hurt(DamageSource.HOT_FLOOR, 1.0F);
		}

		super.stepOn(worldIn, pos, entityIn);
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity tileEntity = worldIn.getBlockEntity(pos);
			if (tileEntity instanceof StoveTileEntity) {
				ItemUtils.dropItems(worldIn, pos, ((StoveTileEntity) tileEntity).getInventory());
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LIT, FACING);
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
		if (stateIn.getValue(CampfireBlock.LIT)) {
			double x = (double) pos.getX() + 0.5D;
			double y = pos.getY();
			double z = (double) pos.getZ() + 0.5D;
			if (rand.nextInt(10) == 0) {
				worldIn.playLocalSound(x, y, z, ModSounds.BLOCK_STOVE_CRACKLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
			}

			Direction direction = stateIn.getValue(HorizontalDirectionalBlock.FACING);
			Direction.Axis direction$axis = direction.getAxis();
			double horizontalOffset = rand.nextDouble() * 0.6D - 0.3D;
			double xOffset = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52D : horizontalOffset;
			double yOffset = rand.nextDouble() * 6.0D / 16.0D;
			double zOffset = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52D : horizontalOffset;
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
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return ModTileEntityTypes.STOVE_TILE.get().create();
	}
}
