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

public class KnifeItem extends ToolItem
{
	public static final ToolType KNIFE_TOOL = ToolType.get(FarmersDelight.MODID + "_knife");

	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.HAY_BLOCK, ModBlocks.RICE_BALE.get());

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
		if (EFFECTIVE_ON.contains(state.getBlock())) return this.efficiency;
		return material != Material.WOOL
				&& material != Material.CARPET
				&& material != Material.CAKE
				&& material != Material.WEB
				&& material != Material.LEAVES ? super.getDestroySpeed(stack, state) : this.efficiency;
	}

	@Override
	public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
		return !player.isCreative();
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.damageItem(1, attacker, (user) -> user.sendBreakAnimation(EquipmentSlotType.MAINHAND));
		return true;
	}

	@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class KnifeEvents
	{
		@SubscribeEvent
		public static void onKnifeKnockback(LivingKnockBackEvent event) {
			LivingEntity attacker = event.getEntityLiving().getAttackingEntity();
			ItemStack tool = attacker != null ? attacker.getHeldItem(Hand.MAIN_HAND) : ItemStack.EMPTY;
			if (tool.getItem() instanceof KnifeItem) {
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
			ItemStack tool = event.getPlayer().getHeldItem(event.getHand());

			if (state.getBlock() == Blocks.CAKE && ModTags.KNIVES.contains(tool.getItem())) {
				int bites = state.get(CakeBlock.BITES);
				if (bites < 6) {
					world.setBlockState(pos, state.with(CakeBlock.BITES, bites + 1), 3);
				} else {
					world.removeBlock(pos, false);
				}
				InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.CAKE_SLICE.get()));
				world.playSound(null, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.PLAYERS, 0.8F, 0.8F);

				event.setCancellationResult(ActionResultType.SUCCESS);
				event.setCanceled(true);
			}
		}
	}

	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		ItemStack tool = context.getItem();
		BlockPos pos = context.getPos();
		BlockState state = world.getBlockState(pos);
		Direction facing = context.getFace();

		if (state.getBlock() == Blocks.PUMPKIN && ModTags.KNIVES.contains(tool.getItem())) {
			PlayerEntity player = context.getPlayer();
			if (player != null && !world.isRemote) {
				Direction direction = facing.getAxis() == Direction.Axis.Y ? player.getHorizontalFacing().getOpposite() : facing;
				world.playSound(null, pos, SoundEvents.BLOCK_PUMPKIN_CARVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
				world.setBlockState(pos, Blocks.CARVED_PUMPKIN.getDefaultState().with(CarvedPumpkinBlock.FACING, direction), 11);
				ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + 0.5D + (double) direction.getXOffset() * 0.65D, (double) pos.getY() + 0.1D, (double) pos.getZ() + 0.5D + (double) direction.getZOffset() * 0.65D, new ItemStack(Items.PUMPKIN_SEEDS, 4));
				itemEntity.setMotion(0.05D * (double) direction.getXOffset() + world.rand.nextDouble() * 0.02D, 0.05D, 0.05D * (double) direction.getZOffset() + world.rand.nextDouble() * 0.02D);
				world.addEntity(itemEntity);
				tool.damageItem(1, player, (playerIn) -> playerIn.sendBreakAnimation(context.getHand()));
			}
			return ActionResultType.func_233537_a_(world.isRemote);
		} else {
			return ActionResultType.PASS;
		}
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment) {
		Set<Enchantment> ALLOWED_ENCHANTMENTS = Sets.newHashSet(Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS, Enchantments.KNOCKBACK, Enchantments.FIRE_ASPECT, Enchantments.LOOTING);
		if (ALLOWED_ENCHANTMENTS.contains(enchantment)) {
			return true;
		}
		Set<Enchantment> DENIED_ENCHANTMENTS = Sets.newHashSet(Enchantments.FORTUNE);
		if (DENIED_ENCHANTMENTS.contains(enchantment)) {
			return false;
		}
		return enchantment.type.canEnchantItem(stack.getItem());
	}
}
