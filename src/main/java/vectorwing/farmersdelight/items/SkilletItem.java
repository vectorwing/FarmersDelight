package vectorwing.farmersdelight.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.*;
import net.minecraft.item.crafting.CampfireCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.registry.ModSounds;
import vectorwing.farmersdelight.tile.SkilletTileEntity;
import vectorwing.farmersdelight.utils.TextUtils;
import vectorwing.farmersdelight.utils.tags.ModTags;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

public class SkilletItem extends BlockItem
{
	public static final ItemTier SKILLET_TIER = ItemTier.IRON;
	private final Multimap<Attribute, AttributeModifier> toolAttributes;

	public SkilletItem(Block blockIn, Item.Properties builderIn) {
		super(blockIn, builderIn.defaultMaxDamage(SKILLET_TIER.getMaxUses()));
		float attackDamage = 4.5F + SKILLET_TIER.getAttackDamage();
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", attackDamage, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", -3.0F, AttributeModifier.Operation.ADDITION));
		this.toolAttributes = builder.build();
	}

	@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class SkilletEvents
	{
		@SubscribeEvent
		public static void onSkilletKnockback(LivingKnockBackEvent event) {
			LivingEntity attacker = event.getEntityLiving().getAttackingEntity();
			ItemStack tool = attacker != null ? attacker.getHeldItem(Hand.MAIN_HAND) : ItemStack.EMPTY;
			if (tool.getItem() instanceof SkilletItem) {
				event.setStrength(event.getOriginalStrength() * 2.0F);
			}
		}
	}

	private static boolean isPlayerNearHeatSource(PlayerEntity player, IWorldReader worldIn) {
		if (player.isBurning()) {
			return true;
		}
		BlockPos pos = player.getPosition();
		for (BlockPos nearbyPos : BlockPos.getAllInBoxMutable(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
			if (worldIn.getBlockState(nearbyPos).isIn(ModTags.HEAT_SOURCES)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.NONE;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		int fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
		int cookingTimeReduction = 0;
		if (fireAspectLevel > 0) {
			cookingTimeReduction = ((MathHelper.clamp(fireAspectLevel, 0, 2) * 20) + 20);
		}
		return 120 - cookingTimeReduction;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack skilletStack = player.getHeldItem(hand);
		if (isPlayerNearHeatSource(player, world)) {
			Hand otherHand = hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
			ItemStack cookingStack = player.getHeldItem(otherHand);

			if (skilletStack.getOrCreateTag().contains("Cooking")) {
				player.setActiveHand(hand);
				return ActionResult.resultPass(skilletStack);
			}

			if (getCookingRecipe(cookingStack, world).isPresent()) {
				ItemStack cookingStackCopy = cookingStack.copy();
				ItemStack cookingStackUnit = cookingStackCopy.split(1);
				skilletStack.getOrCreateTag().put("Cooking", cookingStackUnit.serializeNBT());
				player.setActiveHand(hand);
				player.setHeldItem(otherHand, cookingStackCopy);
				return ActionResult.resultConsume(skilletStack);
			} else {
				player.sendStatusMessage(TextUtils.getTranslation("item.skillet.how_to_cook"), true);
			}
		}
		return ActionResult.resultPass(skilletStack);
	}

	@Override
	public void onUse(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
		if (livingEntityIn instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) livingEntityIn;
			Vector3d pos = player.getPositionVec();
			double x = pos.getX() + 0.5D;
			double y = pos.getY();
			double z = pos.getZ() + 0.5D;
			if (worldIn.rand.nextInt(50) == 0) {
				worldIn.playSound(x, y, z, ModSounds.BLOCK_SKILLET_SIZZLE.get(), SoundCategory.BLOCKS, 0.4F, worldIn.rand.nextFloat() * 0.2F + 0.9F, false);
			}
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		if (!(entityLiving instanceof PlayerEntity)) {
			return;
		}

		PlayerEntity player = (PlayerEntity) entityLiving;
		CompoundNBT tag = stack.getOrCreateTag();

		if (tag.contains("Cooking")) {
			ItemStack cookingStack = ItemStack.read(tag.getCompound("Cooking"));
			player.inventory.placeItemBackInInventory(worldIn, cookingStack);
			tag.remove("Cooking");
		}
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World world, LivingEntity entityLiving) {
		if (!(entityLiving instanceof PlayerEntity)) {
			return stack;
		}

		PlayerEntity player = (PlayerEntity) entityLiving;
		CompoundNBT tag = stack.getOrCreateTag();

		if (tag.contains("Cooking")) {
			ItemStack cookingStack = ItemStack.read(tag.getCompound("Cooking"));
			Optional<CampfireCookingRecipe> cookingRecipe = getCookingRecipe(cookingStack, world);

			cookingRecipe.ifPresent((recipe) -> {
				ItemStack resultStack = recipe.getCraftingResult(new Inventory());
				if (!player.inventory.addItemStackToInventory(resultStack)) {
					player.dropItem(resultStack, false);
				}
				if (player instanceof ServerPlayerEntity) {
					CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) player, stack);
				}
			});
			tag.remove("Cooking");
		}

		return stack;

	}

	public static Optional<CampfireCookingRecipe> getCookingRecipe(ItemStack stack, World world) {
		if (stack.isEmpty()) {
			return Optional.empty();
		}
		return world.getRecipeManager().getRecipe(IRecipeType.CAMPFIRE_COOKING, new Inventory(stack), world);
	}

	@Override
	protected boolean onBlockPlaced(BlockPos pos, World worldIn, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
		super.onBlockPlaced(pos, worldIn, player, stack, state);
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof SkilletTileEntity) {
			((SkilletTileEntity) tileEntity).setSkilletItem(stack);
			return true;
		}
		return false;
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return SKILLET_TIER.getRepairMaterial().test(repair) || super.getIsRepairable(toRepair, repair);
	}

	public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
		if (!worldIn.isRemote && state.getBlockHardness(worldIn, pos) != 0.0F) {
			stack.damageItem(1, entityLiving, (entity) -> {
				entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
			});
		}

		return true;
	}

	@Override
	public ActionResultType tryPlace(BlockItemUseContext context) {
		PlayerEntity player = context.getPlayer();
		if (player != null && player.isSneaking()) {
			return super.tryPlace(context);
		}
		return ActionResultType.PASS;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment) {
		if (enchantment.type.equals(EnchantmentType.WEAPON)) {
			Set<Enchantment> DENIED_ENCHANTMENTS = Sets.newHashSet(Enchantments.SWEEPING);
			return !DENIED_ENCHANTMENTS.contains(enchantment);
		}
		return enchantment.type.canEnchantItem(stack.getItem());
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.damageItem(1, attacker, (user) -> user.sendBreakAnimation(EquipmentSlotType.MAINHAND));
		return true;
	}

	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
		return equipmentSlot == EquipmentSlotType.MAINHAND ? this.toolAttributes : super.getAttributeModifiers(equipmentSlot);
	}
}
