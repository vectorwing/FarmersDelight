package vectorwing.farmersdelight.items;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vectorwing.farmersdelight.registry.ModItems;
import vectorwing.farmersdelight.utils.Utils;

import javax.annotation.Nonnull;
import java.util.Set;

public class KnifeItem extends ToolItem
{
	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.CAKE, Blocks.COBWEB);

	public KnifeItem(IItemTier tier, float attackDamageIn, float attackSpeedIn, Properties builder)
	{
		super(attackDamageIn, attackSpeedIn, tier, EFFECTIVE_ON, builder);
	}

	public float getDestroySpeed(ItemStack stack, BlockState state) {
		Material material = state.getMaterial();
		return material != Material.WOOL
			&& material != Material.CARPET
			&& material != Material.ORGANIC
			&& material != Material.CAKE
			&& material != Material.WEB
			&& material != Material.LEAVES ? super.getDestroySpeed(stack, state) : this.efficiency;
	}

	public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
		return !player.isCreative();
	}

	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.damageItem(1, attacker, (user) -> {
			user.sendBreakAnimation(EquipmentSlotType.MAINHAND);
		});
		return true;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment)
	{
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

	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		BlockPos blockpos = context.getPos();
		BlockState blockstate = world.getBlockState(blockpos);
		Block block = AxeItem.BLOCK_STRIPPING_MAP.get(blockstate.getBlock());
		if (block != null) {
			PlayerEntity playerentity = context.getPlayer();
			world.playSound(playerentity, blockpos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
			if (!world.isRemote) {
				world.setBlockState(blockpos, block.getDefaultState().with(RotatedPillarBlock.AXIS, blockstate.get(RotatedPillarBlock.AXIS)), 11);

				Direction direction = context.getFace();
				ItemEntity entity = new ItemEntity(world,
						(double)blockpos.getX() + 0.5D + (double)direction.getXOffset() * 0.65D,
						(double)blockpos.getY() + 0.4D + (double)direction.getYOffset() * 0.65D,
						(double)blockpos.getZ() + 0.5D + (double)direction.getZOffset() * 0.65D,
						new ItemStack(ModItems.TREE_BARK.get()));
				entity.setMotion(
						0.05D * (double)direction.getXOffset() + world.rand.nextDouble() * 0.02D,
						0.05D * (double)direction.getYOffset() + world.rand.nextDouble() * 0.02D,
						0.05D * (double)direction.getZOffset() + world.rand.nextDouble() * 0.02D);
				world.addEntity(entity);

				if (playerentity != null) {
					context.getItem().damageItem(2, playerentity, (p_220040_1_) -> {
						p_220040_1_.sendBreakAnimation(context.getHand());
					});
				}
			}

			return ActionResultType.SUCCESS;
		} else {
			return ActionResultType.PASS;
		}
	}

	@Nonnull
	@Override
	public ItemStack getContainerItem(@Nonnull ItemStack stack)
	{
		ItemStack container = stack.copy();
		if(container.attemptDamageItem(1, Utils.RAND, null))
			return ItemStack.EMPTY;
		else
			return container;
	}

	@Override
	public boolean hasContainerItem(@Nonnull ItemStack stack)
	{
		return true;
	}

	@Override
	public boolean isRepairable(@Nonnull ItemStack stack)
	{
		return true;
	}

	public boolean isCustomRepairable(@Nonnull ItemStack stack)
	{
		return super.isRepairable(stack);
	}
}
