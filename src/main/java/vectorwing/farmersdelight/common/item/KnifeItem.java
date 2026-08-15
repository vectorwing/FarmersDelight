package vectorwing.farmersdelight.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.mixin.accessor.CropBlockAccessor;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.Set;

public class KnifeItem extends DiggerItem
{
	/**
	 * This action is used on cutting recipes which need a knife.
	 */
	public static final ItemAbility KNIFE_DIG = ItemAbility.get("knife_dig");
	/**
	 * This action is used in gameplay interactions where something is harvested.
	 */
	public static final ItemAbility KNIFE_HARVEST = ItemAbility.get("knife_harvest");

	public static final Set<ItemAbility> KNIFE_ACTIONS = Set.of(ItemAbilities.SHEARS_CARVE, ItemAbilities.SWORD_DIG, KNIFE_DIG, KNIFE_HARVEST);

	public KnifeItem(Tier tier, Properties properties) {
		super(tier, ModTags.Blocks.MINEABLE_WITH_KNIFE, properties);
	}

	@Override
	public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
		if (player.isCreative()) return false;

		if (!player.isSecondaryUseActive() && isValidCrop(level, pos, state)) {
			return isCropMature(level, pos, state);
		}

		return true;
	}

	public static boolean isValidCrop(Level level, BlockPos pos, BlockState state) {
		if (state.getBlock() instanceof CropBlock) {
			return true;
		}

		if (state.is(BlockTags.CROPS)) {
			return true;
		}

		if (state.getCollisionShape(level, pos).isEmpty() && state.getBlock().defaultDestroyTime() == 0.0F) {
			for (Property<?> property : state.getProperties()) {
				if (!(property instanceof IntegerProperty))
					continue;
				if (property.equals(BlockStateProperties.AGE_25) || property.equals(BlockStateProperties.AGE_15))
					continue;
				if (!property.getName().equals(BlockStateProperties.AGE_1.getName()))
					continue;
				return true;
			}
		}

		return false;
	}

	public static boolean isCropMature(Level level, BlockPos pos, BlockState state) {
		if (state.getBlock() instanceof CropBlock crop) {
			return crop.isMaxAge(state);
		}

		if (state.getCollisionShape(level, pos).isEmpty() || state.getBlock() instanceof CocoaBlock) {
			for (Property<?> property : state.getProperties()) {
				if (!(property instanceof IntegerProperty ageProperty))
					continue;
				if (!property.getName().equals(BlockStateProperties.AGE_1.getName()))
					continue;
				int age = state.getValue(ageProperty);
				if (state.getBlock() instanceof SweetBerryBushBlock && age <= 1)
					continue;
				if (age == 0 || (ageProperty.getPossibleValues().size() - 1 != age))
					continue;
				return true;
			}
		}

		return false;
	}

	public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
	}

	@Override
	public boolean isPrimaryItemFor(ItemStack stack, Holder<Enchantment> enchantment) {
		if (enchantment.is(Enchantments.SWEEPING_EDGE)) {
			return false;
		}
		return super.isPrimaryItemFor(stack, enchantment);
	}

	@Override
	public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
		if (enchantment.is(Enchantments.SWEEPING_EDGE)) {
			return false;
		}
		return super.supportsEnchantment(stack, enchantment);
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ItemAbility toolAction) {
		return KNIFE_ACTIONS.contains(toolAction);
	}

	public static BlockState getHarvestedCropState(Level world, BlockPos pos, BlockState state) {
		Block block = state.getBlock();
		if (block instanceof CropBlock crop) {
			BlockState newState = crop.getStateForAge(0);
			if (!newState.is(block))
				return newState;
			IntegerProperty ageProperty = ((CropBlockAccessor) crop).fd$getAgeProperty();
			return state.setValue(ageProperty, 0);
		}
		if (block == Blocks.SWEET_BERRY_BUSH) {
			return state.setValue(BlockStateProperties.AGE_3, 1);
		}
		if (state.getCollisionShape(world, pos)
			.isEmpty() || block instanceof CocoaBlock) {
			for (Property<?> property : state.getProperties()) {
				if (!(property instanceof IntegerProperty))
					continue;
				if (!property.getName()
					.equals(BlockStateProperties.AGE_1.getName()))
					continue;
				return state.setValue((IntegerProperty) property, 0);
			}
		}

		if (state.getFluidState()
			.isEmpty())
			return Blocks.AIR.defaultBlockState();
		return state.getFluidState()
			.createLegacyBlock();
	}

	@EventBusSubscriber(modid = FarmersDelight.MODID)
	public static class KnifeEvents
	{
		@SubscribeEvent
		public static void onKnifeBreak(PlayerInteractEvent.LeftClickBlock event) {
			if (event.getAction() != PlayerInteractEvent.LeftClickBlock.Action.START)
				return;

			if (!(event.getItemStack().getItem() instanceof KnifeItem)) {
				return;
			}

			Level level = event.getLevel();
			BlockPos pos = event.getPos();
			BlockState state = level.getBlockState(pos);

			if (!isCropMature(level, pos, state)) {
				return;
			}

			if (!level.isClientSide) {
				level.destroyBlock(pos, true, event.getEntity());
			}

			BlockState cutCrop = getHarvestedCropState(level, pos, state);
			level.setBlockAndUpdate(pos, cutCrop.canSurvive(level, pos) ? cutCrop : Blocks.AIR.defaultBlockState());
		}

		@SubscribeEvent
		public static void onKnifeKnockback(LivingKnockBackEvent event) {
			LivingEntity attacker = event.getEntity().getKillCredit();
			ItemStack toolStack = attacker != null ? attacker.getItemInHand(InteractionHand.MAIN_HAND) : ItemStack.EMPTY;
			if (toolStack.getItem() instanceof KnifeItem) {
				event.setStrength(event.getOriginalStrength() - 0.1F);
			}
		}

		@SubscribeEvent
		public static void onCakeInteraction(PlayerInteractEvent.RightClickBlock event) {
			ItemStack heldStack = event.getEntity().getItemInHand(event.getHand());

			if (!ItemUtils.isKnife(heldStack)) {
				return;
			}

			Level level = event.getLevel();
			BlockPos pos = event.getPos();
			BlockState state = event.getLevel().getBlockState(pos);
			Block block = state.getBlock();

			if (state.is(ModTags.Blocks.DROPS_CAKE_SLICE)) {
				level.setBlock(pos, Blocks.CAKE.defaultBlockState().setValue(CakeBlock.BITES, 1), 3);
				Block.dropResources(state, level, pos);
				ItemUtils.spawnItemEntity(level, new ItemStack(ModItems.CAKE_SLICE.get()),
						pos.getX(), pos.getY() + 0.2, pos.getZ() + 0.5,
						-0.05, 0, 0);
				level.playSound(null, pos, ModSounds.BLOCK_FOOD_SLICE.get(), SoundSource.PLAYERS, 0.8F, 0.8F);

				event.getEntity().awardStat(Stats.ITEM_USED.get(heldStack.getItem()));
				event.setCancellationResult(InteractionResult.SUCCESS);
				event.setCanceled(true);
			}

			if (block == Blocks.CAKE) {
				int bites = state.getValue(CakeBlock.BITES);
				if (bites < 6) {
					level.setBlock(pos, state.setValue(CakeBlock.BITES, bites + 1), 3);
				} else {
					level.removeBlock(pos, false);
				}
				ItemUtils.spawnItemEntity(level, new ItemStack(ModItems.CAKE_SLICE.get()),
						pos.getX() + (bites * 0.1), pos.getY() + 0.2, pos.getZ() + 0.5,
						-0.05, 0, 0);
				level.playSound(null, pos, ModSounds.BLOCK_FOOD_SLICE.get(), SoundSource.PLAYERS, 0.8F, 0.8F);

				event.getEntity().awardStat(Stats.ITEM_USED.get(heldStack.getItem()));
				event.setCancellationResult(InteractionResult.SUCCESS);
				event.setCanceled(true);
			}
		}
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		ItemStack toolStack = context.getItemInHand();
		BlockPos pos = context.getClickedPos();
		BlockState state = level.getBlockState(pos);
		Direction facing = context.getClickedFace();

		if (state.getBlock() == Blocks.PUMPKIN && toolStack.is(ModTags.Items.KNIVES)) {
			Player player = context.getPlayer();
			if (player != null && !level.isClientSide) {
				Direction direction = facing.getAxis() == Direction.Axis.Y ? player.getDirection().getOpposite() : facing;
				level.playSound(null, pos, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
				level.setBlock(pos, Blocks.CARVED_PUMPKIN.defaultBlockState().setValue(CarvedPumpkinBlock.FACING, direction), 11);
				ItemEntity itemEntity = new ItemEntity(level, (double) pos.getX() + 0.5D + (double) direction.getStepX() * 0.65D, (double) pos.getY() + 0.1D, (double) pos.getZ() + 0.5D + (double) direction.getStepZ() * 0.65D, new ItemStack(Items.PUMPKIN_SEEDS, 4));
				itemEntity.setDeltaMovement(0.05D * (double) direction.getStepX() + level.random.nextDouble() * 0.02D, 0.05D, 0.05D * (double) direction.getStepZ() + level.random.nextDouble() * 0.02D);
				level.addFreshEntity(itemEntity);
				toolStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		} else {
			return InteractionResult.PASS;
		}
	}
}
