package vectorwing.farmersdelight.common.item;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Containers;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Set;

public class KnifeItem extends DiggerItem
{
	public KnifeItem(Tier tier, float attackDamageIn, float attackSpeedIn, Properties properties) {
		super(attackDamageIn, attackSpeedIn, tier, ModTags.MINEABLE_WITH_KNIFE, properties);
	}

	@Override
	public boolean canAttackBlock(BlockState state, Level worldIn, BlockPos pos, Player player) {
		return !player.isCreative();
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.hurtAndBreak(1, attacker, (user) -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		return true;
	}

	@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class KnifeEvents
	{
		@SubscribeEvent
		public static void onKnifeKnockback(LivingKnockBackEvent event) {
			LivingEntity attacker = event.getEntityLiving().getKillCredit();
			ItemStack toolStack = attacker != null ? attacker.getItemInHand(InteractionHand.MAIN_HAND) : ItemStack.EMPTY;
			if (toolStack.getItem() instanceof KnifeItem) {
				float f = event.getOriginalStrength();
				event.setStrength(event.getOriginalStrength() - 0.1F);
			}
		}

		@SubscribeEvent
		@SuppressWarnings("unused")
		public static void onCakeInteraction(PlayerInteractEvent.RightClickBlock event) {
			ItemStack toolStack = event.getPlayer().getItemInHand(event.getHand());

			if (!toolStack.is(ModTags.KNIVES)) {
				return;
			}

			Level world = event.getWorld();
			BlockPos pos = event.getPos();
			BlockState state = event.getWorld().getBlockState(pos);
			Block block = state.getBlock();

			if (state.is(ModTags.DROPS_CAKE_SLICE)) {
				world.setBlock(pos, Blocks.CAKE.defaultBlockState().setValue(CakeBlock.BITES, 1), 3);
				Block.dropResources(state, world, pos);
				Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.CAKE_SLICE.get()));
				world.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);

				event.setCancellationResult(InteractionResult.SUCCESS);
				event.setCanceled(true);
			}

			if (block == Blocks.CAKE) {
				int bites = state.getValue(CakeBlock.BITES);
				if (bites < 6) {
					world.setBlock(pos, state.setValue(CakeBlock.BITES, bites + 1), 3);
				} else {
					world.removeBlock(pos, false);
				}
				Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.CAKE_SLICE.get()));
				world.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);

				event.setCancellationResult(InteractionResult.SUCCESS);
				event.setCanceled(true);
			}
		}
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		ItemStack toolStack = context.getItemInHand();
		BlockPos pos = context.getClickedPos();
		BlockState state = world.getBlockState(pos);
		Direction facing = context.getClickedFace();

		if (state.getBlock() == Blocks.PUMPKIN && toolStack.is(ModTags.KNIVES)) {
			Player player = context.getPlayer();
			if (player != null && !world.isClientSide) {
				Direction direction = facing.getAxis() == Direction.Axis.Y ? player.getDirection().getOpposite() : facing;
				world.playSound(null, pos, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
				world.setBlock(pos, Blocks.CARVED_PUMPKIN.defaultBlockState().setValue(CarvedPumpkinBlock.FACING, direction), 11);
				ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + 0.5D + (double) direction.getStepX() * 0.65D, (double) pos.getY() + 0.1D, (double) pos.getZ() + 0.5D + (double) direction.getStepZ() * 0.65D, new ItemStack(Items.PUMPKIN_SEEDS, 4));
				itemEntity.setDeltaMovement(0.05D * (double) direction.getStepX() + world.random.nextDouble() * 0.02D, 0.05D, 0.05D * (double) direction.getStepZ() + world.random.nextDouble() * 0.02D);
				world.addFreshEntity(itemEntity);
				toolStack.hurtAndBreak(1, player, (playerIn) -> playerIn.broadcastBreakEvent(context.getHand()));
			}
			return InteractionResult.sidedSuccess(world.isClientSide);
		} else {
			return InteractionResult.PASS;
		}
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.world.item.enchantment.Enchantment enchantment) {
		Set<Enchantment> ALLOWED_ENCHANTMENTS = Sets.newHashSet(Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, Enchantments.KNOCKBACK, Enchantments.FIRE_ASPECT, Enchantments.MOB_LOOTING);
		if (ALLOWED_ENCHANTMENTS.contains(enchantment)) {
			return true;
		}
		Set<Enchantment> DENIED_ENCHANTMENTS = Sets.newHashSet(Enchantments.BLOCK_FORTUNE);
		if (DENIED_ENCHANTMENTS.contains(enchantment)) {
			return false;
		}
		return enchantment.category.canEnchant(stack.getItem());
	}
}
