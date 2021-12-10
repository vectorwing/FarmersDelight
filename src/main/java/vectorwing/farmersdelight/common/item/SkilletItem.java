package vectorwing.farmersdelight.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.SkilletBlock;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;
import vectorwing.farmersdelight.common.utility.TextUtils;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

public class SkilletItem extends BlockItem
{
	public static final Tiers SKILLET_TIER = Tiers.IRON;
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
			ItemStack tool = attacker != null ? attacker.getItemInHand(InteractionHand.MAIN_HAND) : ItemStack.EMPTY;
			if (tool.getItem() instanceof SkilletItem) {
				event.setStrength(event.getOriginalStrength() * 2.0F);
			}
		}

		@SubscribeEvent
		public static void onSkilletAttack(AttackEntityEvent event) {
			Player player = event.getPlayer();
			float attackPower = player.getAttackStrengthScale(0.0F);
			ItemStack tool = player.getItemInHand(InteractionHand.MAIN_HAND);
			if (tool.getItem() instanceof SkilletItem) {
				if (attackPower > 0.8F) {
					float pitch = 0.9F + (player.getRandom().nextFloat() * 0.2F);
					player.getCommandSenderWorld().playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.ITEM_SKILLET_ATTACK_STRONG.get(), SoundSource.PLAYERS, 1.0F, pitch);
				} else {
					player.getCommandSenderWorld().playSound(player, player.getX(), player.getY(), player.getZ(), ModSounds.ITEM_SKILLET_ATTACK_WEAK.get(), SoundSource.PLAYERS, 0.8F, 0.9F);
				}
			}
		}
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.hurtAndBreak(1, attacker, (user) -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		return true;
	}

	private static boolean isPlayerNearHeatSource(Player player, LevelReader worldIn) {
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
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack skilletStack = player.getItemInHand(hand);
		if (isPlayerNearHeatSource(player, world)) {
			InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
			ItemStack cookingStack = player.getItemInHand(otherHand);

			if (skilletStack.getOrCreateTag().contains("Cooking")) {
				player.startUsingItem(hand);
				return InteractionResultHolder.pass(skilletStack);
			}

			Optional<CampfireCookingRecipe> recipe = getCookingRecipe(cookingStack, world);
			if (recipe.isPresent()) {
				ItemStack cookingStackCopy = cookingStack.copy();
				ItemStack cookingStackUnit = cookingStackCopy.split(1);
				skilletStack.getOrCreateTag().put("Cooking", cookingStackUnit.serializeNBT());
				skilletStack.getOrCreateTag().putInt("CookTimeHandheld", recipe.get().getCookingTime());
				player.startUsingItem(hand);
				player.setItemInHand(otherHand, cookingStackCopy);
				return InteractionResultHolder.consume(skilletStack);
			} else {
				player.displayClientMessage(TextUtils.getTranslation("item.skillet.how_to_cook"), true);
			}
		}
		return InteractionResultHolder.pass(skilletStack);
	}

	@Override
	public void onUseTick(Level worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
		if (livingEntityIn instanceof Player) {
			Player player = (Player) livingEntityIn;
			Vec3 pos = player.position();
			double x = pos.x() + 0.5D;
			double y = pos.y();
			double z = pos.z() + 0.5D;
			if (worldIn.random.nextInt(50) == 0) {
				worldIn.playLocalSound(x, y, z, ModSounds.BLOCK_SKILLET_SIZZLE.get(), SoundSource.BLOCKS, 0.4F, worldIn.random.nextFloat() * 0.2F + 0.9F, false);
			}
		}
	}

	@Override
	public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
		if (!(entityLiving instanceof Player)) {
			return;
		}

		Player player = (Player) entityLiving;
		CompoundTag tag = stack.getOrCreateTag();

		if (tag.contains("Cooking")) {
			ItemStack cookingStack = ItemStack.of(tag.getCompound("Cooking"));
			player.getInventory().placeItemBackInInventory(cookingStack);
			tag.remove("Cooking");
			tag.remove("CookTimeHandheld");
		}
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entityLiving) {
		if (!(entityLiving instanceof Player)) {
			return stack;
		}

		Player player = (Player) entityLiving;
		CompoundTag tag = stack.getOrCreateTag();

		if (tag.contains("Cooking")) {
			ItemStack cookingStack = ItemStack.of(tag.getCompound("Cooking"));
			Optional<CampfireCookingRecipe> cookingRecipe = getCookingRecipe(cookingStack, world);

			cookingRecipe.ifPresent((recipe) -> {
				ItemStack resultStack = recipe.assemble(new SimpleContainer());
				if (!player.getInventory().add(resultStack)) {
					player.drop(resultStack, false);
				}
				if (player instanceof ServerPlayer) {
					CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
				}
			});
			tag.remove("Cooking");
			tag.remove("CookTimeHandheld");
		}

		return stack;
	}

	public static Optional<CampfireCookingRecipe> getCookingRecipe(ItemStack stack, Level world) {
		if (stack.isEmpty()) {
			return Optional.empty();
		}
		return world.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SimpleContainer(stack), world);
	}

	@Override
	protected boolean updateCustomBlockEntityTag(BlockPos pos, Level worldIn, @Nullable Player player, ItemStack stack, BlockState state) {
		super.updateCustomBlockEntityTag(pos, worldIn, player, stack, state);
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		if (tileEntity instanceof SkilletBlockEntity) {
			((SkilletBlockEntity) tileEntity).setSkilletItem(stack);
			return true;
		}
		return false;
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		return SKILLET_TIER.getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
	}

	public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
		if (!worldIn.isClientSide && state.getDestroySpeed(worldIn, pos) != 0.0F) {
			stack.hurtAndBreak(1, entityLiving, (entity) -> {
				entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
			});
		}

		return true;
	}

	@Override
	public InteractionResult place(BlockPlaceContext context) {
		Player player = context.getPlayer();
		if (player != null && player.isShiftKeyDown()) {
			return super.place(context);
		}
		return InteractionResult.PASS;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.world.item.enchantment.Enchantment enchantment) {
		if (enchantment.category.equals(EnchantmentCategory.WEAPON)) {
			Set<Enchantment> DENIED_ENCHANTMENTS = Sets.newHashSet(Enchantments.SWEEPING_EDGE);
			return !DENIED_ENCHANTMENTS.contains(enchantment);
		}
		return enchantment.category.canEnchant(stack.getItem());
	}

	@Override
	public int getEnchantmentValue() {
		return SKILLET_TIER.getEnchantmentValue();
	}

	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
		return equipmentSlot == EquipmentSlot.MAINHAND ? this.toolAttributes : super.getDefaultAttributeModifiers(equipmentSlot);
	}
}
