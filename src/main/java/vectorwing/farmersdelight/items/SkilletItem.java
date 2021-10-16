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
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.blocks.SkilletBlock;
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
		super(blockIn, builderIn.defaultDurability(SKILLET_TIER.getUses()));
		float attackDamage = 4.0F + SKILLET_TIER.getAttackDamageBonus();
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", attackDamage, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -3.1F, AttributeModifier.Operation.ADDITION));
		this.toolAttributes = builder.build();
	}

	@Mod.EventBusSubscriber(modid = FarmersDelight.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
	public static class SkilletEvents
	{
		@SubscribeEvent
		public static void onSkilletKnockback(LivingKnockBackEvent event) {
			LivingEntity attacker = event.getEntityLiving().getKillCredit();
			ItemStack tool = attacker != null ? attacker.getItemInHand(Hand.MAIN_HAND) : ItemStack.EMPTY;
			if (tool.getItem() instanceof SkilletItem) {
				event.setStrength(event.getOriginalStrength() * 2.0F);
			}
		}

		@SubscribeEvent
		public static void onSkilletAttack(AttackEntityEvent event) {
			PlayerEntity player = event.getPlayer();
			float attackPower = player.getAttackStrengthScale(0.0F);
			ItemStack tool = player.getItemInHand(Hand.MAIN_HAND);
			if (tool.getItem() instanceof SkilletItem) {
				if (attackPower > 0.8F) {
					float pitch = 0.9F + (random.nextFloat() * 0.2F);
					player.getCommandSenderWorld().playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.ITEM_SKILLET_ATTACK_STRONG.get(), SoundCategory.PLAYERS, 1.0F, pitch);
				} else {
					player.getCommandSenderWorld().playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.ITEM_SKILLET_ATTACK_WEAK.get(), SoundCategory.PLAYERS, 0.8F, 0.9F);
				}
			}
		}
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.hurtAndBreak(1, attacker, (user) -> user.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
		return true;
	}

	private static boolean isPlayerNearHeatSource(PlayerEntity player, IWorldReader worldIn) {
		if (player.isOnFire()) {
			return true;
		}
		BlockPos pos = player.blockPosition();
		for (BlockPos nearbyPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
			if (worldIn.getBlockState(nearbyPos).is(ModTags.HEAT_SOURCES)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		int fireAspectLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
		int cookingTime = stack.getOrCreateTag().getInt("CookTimeHandheld");
		return SkilletBlock.getSkilletCookingTime(cookingTime, fireAspectLevel);
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack skilletStack = player.getItemInHand(hand);
		if (isPlayerNearHeatSource(player, world)) {
			Hand otherHand = hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND;
			ItemStack cookingStack = player.getItemInHand(otherHand);

			if (skilletStack.getOrCreateTag().contains("Cooking")) {
				player.startUsingItem(hand);
				return ActionResult.pass(skilletStack);
			}

			Optional<CampfireCookingRecipe> recipe = getCookingRecipe(cookingStack, world);
			if (recipe.isPresent()) {
				ItemStack cookingStackCopy = cookingStack.copy();
				ItemStack cookingStackUnit = cookingStackCopy.split(1);
				skilletStack.getOrCreateTag().put("Cooking", cookingStackUnit.serializeNBT());
				skilletStack.getOrCreateTag().putInt("CookTimeHandheld", recipe.get().getCookingTime());
				player.startUsingItem(hand);
				player.setItemInHand(otherHand, cookingStackCopy);
				return ActionResult.consume(skilletStack);
			} else {
				player.displayClientMessage(TextUtils.getTranslation("item.skillet.how_to_cook"), true);
			}
		}
		return ActionResult.pass(skilletStack);
	}

	@Override
	public void onUseTick(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
		if (livingEntityIn instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) livingEntityIn;
			Vector3d pos = player.position();
			double x = pos.x() + 0.5D;
			double y = pos.y();
			double z = pos.z() + 0.5D;
			if (worldIn.random.nextInt(50) == 0) {
				worldIn.playLocalSound(x, y, z, ModSounds.BLOCK_SKILLET_SIZZLE.get(), SoundCategory.BLOCKS, 0.4F, worldIn.random.nextFloat() * 0.2F + 0.9F, false);
			}
		}
	}

	@Override
	public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		if (!(entityLiving instanceof PlayerEntity)) {
			return;
		}

		PlayerEntity player = (PlayerEntity) entityLiving;
		CompoundNBT tag = stack.getOrCreateTag();

		if (tag.contains("Cooking")) {
			ItemStack cookingStack = ItemStack.of(tag.getCompound("Cooking"));
			player.inventory.placeItemBackInInventory(worldIn, cookingStack);
			tag.remove("Cooking");
			tag.remove("CookTimeHandheld");
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entityLiving) {
		if (!(entityLiving instanceof PlayerEntity)) {
			return stack;
		}

		PlayerEntity player = (PlayerEntity) entityLiving;
		CompoundNBT tag = stack.getOrCreateTag();

		if (tag.contains("Cooking")) {
			ItemStack cookingStack = ItemStack.of(tag.getCompound("Cooking"));
			Optional<CampfireCookingRecipe> cookingRecipe = getCookingRecipe(cookingStack, world);

			cookingRecipe.ifPresent((recipe) -> {
				ItemStack resultStack = recipe.assemble(new Inventory());
				if (!player.inventory.add(resultStack)) {
					player.drop(resultStack, false);
				}
				if (player instanceof ServerPlayerEntity) {
					CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) player, stack);
				}
			});
			tag.remove("Cooking");
			tag.remove("CookTimeHandheld");
		}

		return stack;
	}

	public static Optional<CampfireCookingRecipe> getCookingRecipe(ItemStack stack, World world) {
		if (stack.isEmpty()) {
			return Optional.empty();
		}
		return world.getRecipeManager().getRecipeFor(IRecipeType.CAMPFIRE_COOKING, new Inventory(stack), world);
	}

	@Override
	protected boolean updateCustomBlockEntityTag(BlockPos pos, World worldIn, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
		super.updateCustomBlockEntityTag(pos, worldIn, player, stack, state);
		TileEntity tileEntity = worldIn.getBlockEntity(pos);
		if (tileEntity instanceof SkilletTileEntity) {
			((SkilletTileEntity) tileEntity).setSkilletItem(stack);
			return true;
		}
		return false;
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		return SKILLET_TIER.getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
	}

	public boolean mineBlock(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
		if (!worldIn.isClientSide && state.getDestroySpeed(worldIn, pos) != 0.0F) {
			stack.hurtAndBreak(1, entityLiving, (entity) -> {
				entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
			});
		}

		return true;
	}

	@Override
	public ActionResultType place(BlockItemUseContext context) {
		PlayerEntity player = context.getPlayer();
		if (player != null && player.isShiftKeyDown()) {
			return super.place(context);
		}
		return ActionResultType.PASS;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment) {
		if (enchantment.category.equals(EnchantmentType.WEAPON)) {
			Set<Enchantment> DENIED_ENCHANTMENTS = Sets.newHashSet(Enchantments.SWEEPING_EDGE);
			return !DENIED_ENCHANTMENTS.contains(enchantment);
		}
		return enchantment.category.canEnchant(stack.getItem());
	}

	@Override
	public int getEnchantmentValue() {
		return SKILLET_TIER.getEnchantmentValue();
	}

	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
		return equipmentSlot == EquipmentSlotType.MAINHAND ? this.toolAttributes : super.getDefaultAttributeModifiers(equipmentSlot);
	}
}
