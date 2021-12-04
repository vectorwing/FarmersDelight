package vectorwing.farmersdelight.items;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.common.ToolType;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.registry.ModBlocks;
import vectorwing.farmersdelight.registry.ModItems;
import vectorwing.farmersdelight.utils.tags.ModTags;

import javax.annotation.Nonnull;
import java.util.Set;

import net.minecraft.item.Item.Properties;

public class KnifeItem extends ToolItem
{
	public static final ToolType KNIFE_TOOL = ToolType.get(FarmersDelight.MODID + "_knife");

	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet();

	public KnifeItem(IItemTier tier, float attackDamageIn, float attackSpeedIn, Properties properties) {
		super(attackDamageIn, attackSpeedIn, tier, EFFECTIVE_ON, properties);
	}

	@Nonnull
	@Override
	public Set<ToolType> getToolTypes(ItemStack stack) {
		return ImmutableSet.of(KNIFE_TOOL);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Material material = state.getMaterial();
		if (EFFECTIVE_ON.contains(state.getBlock())) return this.speed;
		return material != Material.WOOL
				&& material != Material.CLOTH_DECORATION
				&& material != Material.CAKE
				&& material != Material.WEB ? super.getDestroySpeed(stack, state) : this.speed;
	}

	@Override
	public boolean canAttackBlock(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
		return !player.isCreative();
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.hurtAndBreak(1, attacker, (user) -> user.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
		return true;
	}

	@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class KnifeEvents
	{
		@SubscribeEvent
		public static void onKnifeKnockback(LivingKnockBackEvent event) {
			LivingEntity attacker = event.getEntityLiving().getKillCredit();
			ItemStack toolStack = attacker != null ? attacker.getItemInHand(Hand.MAIN_HAND) : ItemStack.EMPTY;
			if (toolStack.getItem() instanceof KnifeItem) {
				float f = event.getOriginalStrength();
				event.setStrength(event.getOriginalStrength() - 0.1F);
			}
		}

		@SubscribeEvent
		@SuppressWarnings("unused")
		public static void onCakeInteraction(PlayerInteractEvent.RightClickBlock event) {
			World world = event.getWorld();
			BlockPos pos = event.getPos();
			BlockState state = event.getWorld().getBlockState(pos);
			ItemStack toolStack = event.getPlayer().getItemInHand(event.getHand());

			if (state.getBlock() == Blocks.CAKE && ModTags.KNIVES.contains(toolStack.getItem())) {
				int bites = state.getValue(CakeBlock.BITES);
				if (bites < 6) {
					world.setBlock(pos, state.setValue(CakeBlock.BITES, bites + 1), 3);
				} else {
					world.removeBlock(pos, false);
				}
				InventoryHelper.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.CAKE_SLICE.get()));
				world.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundCategory.PLAYERS, 0.8F, 0.8F);

				event.setCancellationResult(ActionResultType.SUCCESS);
				event.setCanceled(true);
			}
		}
	}

	public ActionResultType useOn(ItemUseContext context) {
		World world = context.getLevel();
		ItemStack toolStack = context.getItemInHand();
		BlockPos pos = context.getClickedPos();
		BlockState state = world.getBlockState(pos);
		Direction facing = context.getClickedFace();

		if (state.getBlock() == Blocks.PUMPKIN && ModTags.KNIVES.contains(toolStack.getItem())) {
			PlayerEntity player = context.getPlayer();
			if (player != null && !world.isClientSide) {
				Direction direction = facing.getAxis() == Direction.Axis.Y ? player.getDirection().getOpposite() : facing;
				world.playSound(null, pos, SoundEvents.PUMPKIN_CARVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
				world.setBlock(pos, Blocks.CARVED_PUMPKIN.defaultBlockState().setValue(CarvedPumpkinBlock.FACING, direction), 11);
				ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + 0.5D + (double) direction.getStepX() * 0.65D, (double) pos.getY() + 0.1D, (double) pos.getZ() + 0.5D + (double) direction.getStepZ() * 0.65D, new ItemStack(Items.PUMPKIN_SEEDS, 4));
				itemEntity.setDeltaMovement(0.05D * (double) direction.getStepX() + world.random.nextDouble() * 0.02D, 0.05D, 0.05D * (double) direction.getStepZ() + world.random.nextDouble() * 0.02D);
				world.addFreshEntity(itemEntity);
				toolStack.hurtAndBreak(1, player, (playerIn) -> playerIn.broadcastBreakEvent(context.getHand()));
			}
			return ActionResultType.sidedSuccess(world.isClientSide);
		} else {
			return ActionResultType.PASS;
		}
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment) {
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
