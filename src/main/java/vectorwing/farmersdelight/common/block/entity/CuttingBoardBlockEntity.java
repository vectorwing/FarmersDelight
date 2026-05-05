package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipeInput;
import vectorwing.farmersdelight.common.registry.ModAdvancements;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = FarmersDelight.MODID)
public class CuttingBoardBlockEntity extends SyncedBlockEntity implements Clearable
{
	private final ItemStackHandler inventory;
	private final RecipeManager.CachedCheck<CuttingBoardRecipeInput, CuttingBoardRecipe> quickCheck;
	private ResourceLocation lastRecipeID;
	private boolean isItemCarvingBoard;

	public CuttingBoardBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntityTypes.CUTTING_BOARD.get(), pos, state);
		inventory = createHandler();
		isItemCarvingBoard = false;
		quickCheck = RecipeManager.createCheck(ModRecipeTypes.CUTTING.get());
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(
				Capabilities.ItemHandler.BLOCK,
				ModBlockEntityTypes.CUTTING_BOARD.get(),
				(be, context) -> be.getInventory()
		);
	}

	@Override
	public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.loadAdditional(compound, registries);
		isItemCarvingBoard = compound.getBoolean("IsItemCarved");
		inventory.deserializeNBT(registries, compound.getCompound("Inventory"));
	}

	@Override
	public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
		super.saveAdditional(compound, registries);
		compound.put("Inventory", inventory.serializeNBT(registries));
		compound.putBoolean("IsItemCarved", isItemCarvingBoard);
	}

	public boolean processStoredItemUsingTool(ItemStack toolStack, @Nullable Player player) {
		if (level == null) return false;

		if (isItemCarvingBoard) return false;

		Optional<RecipeHolder<CuttingBoardRecipe>> matchingRecipe = getMatchingRecipe(toolStack, player);

		matchingRecipe.ifPresent(recipe -> {
			List<ItemStack> results = recipe.value().rollResults(level.random, ItemUtils.getValidatedEnchantmentLevel(Enchantments.FORTUNE, level.registryAccess(), toolStack), new RecipeWrapper(inventory));
			for (ItemStack resultStack : results) {
				Direction direction = getBlockState().getValue(CuttingBoardBlock.FACING).getCounterClockWise();
				ItemUtils.spawnItemEntity(level, resultStack.copy(),
						worldPosition.getX() + 0.5 + (direction.getStepX() * 0.2), worldPosition.getY() + 0.2, worldPosition.getZ() + 0.5 + (direction.getStepZ() * 0.2),
						direction.getStepX() * 0.2F, 0.0F, direction.getStepZ() * 0.2F);
			}
			if (!level.isClientSide) {
				toolStack.hurtAndBreak(1, (ServerLevel) level, player, (item) -> {
				});
				if (player != null) {
					player.awardStat(Stats.ITEM_USED.get(toolStack.getItem()));
				}
			}
			if (level instanceof ServerLevel serverLevel) {
				spawnCuttingParticles(serverLevel, getBlockPos(), getStoredItem());
			}
			playProcessingSound(recipe.value().getSoundEvent().orElse(null), toolStack, getStoredItem());
			inventory.extractItem(0, 1, false);
			if (player instanceof ServerPlayer) {
				ModAdvancements.USE_CUTTING_BOARD.get().trigger((ServerPlayer) player);
				if (!getStoredItem().isEmpty()) {
					player.displayClientMessage(TextUtils.block("cutting_board.remaining_items", getStoredItem().getCount()), true);
				} else {
					player.displayClientMessage(Component.empty(), true);
				}
			}
		});

		return matchingRecipe.isPresent();
	}

	private Optional<RecipeHolder<CuttingBoardRecipe>> getMatchingRecipe(ItemStack toolStack, @Nullable Player player) {
		if (level == null) return Optional.empty();

		Optional<RecipeHolder<CuttingBoardRecipe>> recipe = quickCheck.getRecipeFor(new CuttingBoardRecipeInput(getStoredItem(), toolStack), level);
		if (recipe.isPresent()) {
			if (recipe.get().value().getTool().test(toolStack)) {
				return recipe;
			} else if (player != null) {
				player.displayClientMessage(TextUtils.block("cutting_board.invalid_item"), true);
			}
		} else if (player != null) {
			player.displayClientMessage(TextUtils.block("cutting_board.invalid_tool"), true);
		}

		return Optional.empty();
	}

	public void spawnCuttingParticles(ServerLevel level, BlockPos pos, ItemStack stack) {
		level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, 5, 0.1, 0.1, 0.1, 0.05D);
	}

	public void playProcessingSound(@Nullable SoundEvent sound, ItemStack tool, ItemStack boardItem) {
		if (sound != null) {
			playSound(sound, 1.0F, 1.0F);
		} else if (tool.is(Tags.Items.TOOLS_SHEAR)) {
			playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
		} else if (tool.is(CommonTags.Items.TOOLS_KNIFE)) {
			playSound(ModSounds.BLOCK_CUTTING_BOARD_KNIFE.get(), 0.8F, 1.0F);
		} else if (boardItem.getItem() instanceof BlockItem blockItem) {
			Block block = blockItem.getBlock();
			SoundType soundType = block.defaultBlockState().getSoundType();
			playSound(soundType.getBreakSound(), 1.0F, 0.8F);
		} else {
			playSound(SoundEvents.WOOD_BREAK, 1.0F, 0.8F);
		}
	}

	public void playSound(SoundEvent sound, float volume, float pitch) {
		if (level != null)
			level.playSound(null, worldPosition.getX() + 0.5F, worldPosition.getY() + 0.5F, worldPosition.getZ() + 0.5F, sound, SoundSource.BLOCKS, volume, pitch);
	}

	public boolean canAddItem(ItemStack addedStack) {
		if (isItemCarvingBoard || addedStack.isEmpty()) {
			return false;
		}
		return inventory.insertItem(0, addedStack.copy(), true).getCount() != addedStack.getCount();
	}

	public ItemStack addItem(ItemStack addedStack) {
		if (!isItemCarvingBoard) {
			return inventory.insertItem(0, addedStack.copy(), false);
		}
		return addedStack;
	}

	public ItemStack removeItem() {
		isItemCarvingBoard = false;
		return inventory.extractItem(0, getMaxStackSize(), false);
	}

	public boolean carveToolOnBoard(ItemStack toolStack) {
		if (toolStack.getItem() instanceof TieredItem || toolStack.getItem() instanceof TridentItem || toolStack.getItem() instanceof ShearsItem) {
			if (addItem(toolStack) == ItemStack.EMPTY) {
				isItemCarvingBoard = true;
				return true;
			}
		}
		return false;
	}

	public IItemHandler getInventory() {
		return inventory;
	}

	public ItemStack getStoredItem() {
		return inventory.getStackInSlot(0);
	}

	public int getMaxStackSize() {
		return inventory.getSlotLimit(0);
	}

	public boolean isEmpty() {
		return inventory.getStackInSlot(0).isEmpty();
	}

	public boolean isItemCarvingBoard() {
		return isItemCarvingBoard;
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
	}

	private ItemStackHandler createHandler() {
		return new ItemStackHandler()
		{
			@Override
			protected void onContentsChanged(int slot) {
				inventoryChanged();
			}
		};
	}

	@Override
	public void clearContent() {
		ItemUtils.clearItems(inventory);
	}
}