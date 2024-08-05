package vectorwing.farmersdelight.common.item;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;

import java.util.Set;

public class KnifeItem extends DiggerItem
{
	public static final Set<ToolAction> KNIFE_ACTIONS = Set.of(ToolActions.SHEARS_CARVE);

	public KnifeItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
		super(attackDamage, attackSpeed, tier, ModTags.MINEABLE_WITH_KNIFE, properties);
	}

	@Override
	public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
		return !player.isCreative();
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.hurtAndBreak(1, attacker, (user) -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		return true;
	}

	public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
		return KNIFE_ACTIONS.contains(toolAction);
	}

	@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class KnifeEvents
	{
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
			ItemStack toolStack = event.getEntity().getItemInHand(event.getHand());

			if (!toolStack.is(ModTags.KNIVES)) {
				return;
			}

			Level level = event.getLevel();
			BlockPos pos = event.getPos();
			BlockState state = event.getLevel().getBlockState(pos);
			Block block = state.getBlock();

			if (state.is(ModTags.DROPS_CAKE_SLICE)) {
				level.setBlock(pos, Blocks.CAKE.defaultBlockState().setValue(CakeBlock.BITES, 1), 3);
				Block.dropResources(state, level, pos);
				ItemUtils.spawnItemEntity(level, new ItemStack(ModItems.CAKE_SLICE.get()),
						pos.getX(), pos.getY() + 0.2, pos.getZ() + 0.5,
						-0.05, 0, 0);
				level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);

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
				level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);

				event.setCancellationResult(InteractionResult.SUCCESS);
				event.setCanceled(true);
			}
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
