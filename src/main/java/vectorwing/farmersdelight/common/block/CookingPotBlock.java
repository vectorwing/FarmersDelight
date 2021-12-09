package vectorwing.farmersdelight.common.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.block.state.CookingPotSupport;
import vectorwing.farmersdelight.core.registry.ModBlockEntityTypes;
import vectorwing.farmersdelight.core.registry.ModSounds;
import vectorwing.farmersdelight.core.tag.ModTags;
import vectorwing.farmersdelight.core.util.MathUtils;
import vectorwing.farmersdelight.core.util.TextUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class CookingPotBlock extends BaseEntityBlock implements SimpleWaterloggedBlock
{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final EnumProperty<CookingPotSupport> SUPPORT = EnumProperty.create("support", CookingPotSupport.class);
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 10.0D, 14.0D);
	protected static final VoxelShape SHAPE_WITH_TRAY = Shapes.or(SHAPE, Block.box(0.0D, -1.0D, 0.0D, 16.0D, 0.0D, 16.0D));

	public CookingPotBlock() {
		super(Properties.of(Material.METAL)
				.strength(0.8F, 6.0F)
				.sound(SoundType.LANTERN));
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(SUPPORT, CookingPotSupport.NONE).setValue(WATERLOGGED, false));
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player,
								 InteractionHand handIn, BlockHitResult result) {
		ItemStack heldStack = player.getItemInHand(handIn);
		if (heldStack.isEmpty() && player.isShiftKeyDown()) {
			world.setBlockAndUpdate(pos, state.setValue(SUPPORT, state.getValue(SUPPORT).equals(CookingPotSupport.HANDLE)
					? getTrayState(world, pos) : CookingPotSupport.HANDLE));
			world.playSound(null, pos, SoundEvents.LANTERN_PLACE, SoundSource.BLOCKS, 0.7F, 1.0F);
		} else if (!world.isClientSide) {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if (tileEntity instanceof CookingPotBlockEntity) {
				CookingPotBlockEntity cookingPotEntity = (CookingPotBlockEntity) tileEntity;
				ItemStack servingStack = cookingPotEntity.useHeldItemOnMeal(heldStack);
				if (servingStack != ItemStack.EMPTY) {
					if (!player.getInventory().add(servingStack)) {
						player.drop(servingStack, false);
					}
					world.playSound(null, pos, SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.BLOCKS, 1.0F, 1.0F);
				} else {
					NetworkHooks.openGui((ServerPlayer) player, cookingPotEntity, pos);
				}
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return state.getValue(SUPPORT).equals(CookingPotSupport.TRAY) ? SHAPE_WITH_TRAY : SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos pos = context.getClickedPos();
		Level world = context.getLevel();
		FluidState fluid = world.getFluidState(context.getClickedPos());

		BlockState state = this.defaultBlockState()
				.setValue(FACING, context.getHorizontalDirection().getOpposite())
				.setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);

		if (context.getClickedFace().equals(Direction.DOWN)) {
			return state.setValue(SUPPORT, CookingPotSupport.HANDLE);
		}
		return state.setValue(SUPPORT, getTrayState(world, pos));
	}

	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
		if (state.getValue(WATERLOGGED)) {
			world.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		if (facing.getAxis().equals(Direction.Axis.Y) && !state.getValue(SUPPORT).equals(CookingPotSupport.HANDLE)) {
			return state.setValue(SUPPORT, getTrayState(world, currentPos));
		}
		return state;
	}

	private CookingPotSupport getTrayState(LevelAccessor world, BlockPos pos) {
		if (ModTags.TRAY_HEAT_SOURCES.contains(world.getBlockState(pos.below()).getBlock())) {
			return CookingPotSupport.TRAY;
		}
		return CookingPotSupport.NONE;
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter worldIn, BlockPos pos, BlockState state) {
		ItemStack stack = super.getCloneItemStack(worldIn, pos, state);
		CookingPotBlockEntity cookingPotEntity = (CookingPotBlockEntity) worldIn.getBlockEntity(pos);
		if (cookingPotEntity != null) {
			CompoundTag nbt = cookingPotEntity.writeMeal(new CompoundTag());
			if (!nbt.isEmpty()) {
				stack.addTagElement("BlockEntityTag", nbt);
			}
			if (cookingPotEntity.hasCustomName()) {
				stack.setHoverName(cookingPotEntity.getCustomName());
			}
		}
		return stack;
	}

	@Override
	public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity tileEntity = worldIn.getBlockEntity(pos);
			if (tileEntity instanceof CookingPotBlockEntity) {
				CookingPotBlockEntity cookingPotEntity = (CookingPotBlockEntity) tileEntity;
				Containers.dropContents(worldIn, pos, cookingPotEntity.getDroppableInventory());
				cookingPotEntity.grantStoredRecipeExperience(worldIn, Vec3.atCenterOf(pos));
				worldIn.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		CompoundTag nbt = stack.getTagElement("BlockEntityTag");
		if (nbt != null) {
			CompoundTag inventoryTag = nbt.getCompound("Inventory");
			if (inventoryTag.contains("Items", 9)) {
				ItemStackHandler handler = new ItemStackHandler();
				handler.deserializeNBT(inventoryTag);
				ItemStack mealStack = handler.getStackInSlot(6);
				if (!mealStack.isEmpty()) {
					MutableComponent textServingsOf = mealStack.getCount() == 1
							? TextUtils.getTranslation("tooltip.cooking_pot.single_serving")
							: TextUtils.getTranslation("tooltip.cooking_pot.many_servings", mealStack.getCount());
					tooltip.add(textServingsOf.withStyle(ChatFormatting.GRAY));
					MutableComponent textMealName = mealStack.getHoverName().copy();
					tooltip.add(textMealName.withStyle(mealStack.getRarity().color));
				}
			}
		} else {
			MutableComponent textEmpty = TextUtils.getTranslation("tooltip.cooking_pot.empty");
			tooltip.add(textEmpty.withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, SUPPORT, WATERLOGGED);
	}

	@Override
	public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (stack.hasCustomHoverName()) {
			BlockEntity tileEntity = worldIn.getBlockEntity(pos);
			if (tileEntity instanceof CookingPotBlockEntity) {
				((CookingPotBlockEntity) tileEntity).setCustomName(stack.getHoverName());
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		if (tileEntity instanceof CookingPotBlockEntity && ((CookingPotBlockEntity) tileEntity).isHeated()) {
			CookingPotBlockEntity cookingPotEntity = (CookingPotBlockEntity) tileEntity;
			SoundEvent boilSound = !cookingPotEntity.getMeal().isEmpty()
					? ModSounds.BLOCK_COOKING_POT_BOIL_SOUP.get()
					: ModSounds.BLOCK_COOKING_POT_BOIL.get();
			double x = (double) pos.getX() + 0.5D;
			double y = pos.getY();
			double z = (double) pos.getZ() + 0.5D;
			if (rand.nextInt(10) == 0) {
				worldIn.playLocalSound(x, y, z, boilSound, SoundSource.BLOCKS, 0.5F, rand.nextFloat() * 0.2F + 0.9F, false);
			}
		}
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		if (tileEntity instanceof CookingPotBlockEntity) {
			ItemStackHandler inventory = ((CookingPotBlockEntity) tileEntity).getInventory();
			return MathUtils.calcRedstoneFromItemHandler(inventory);
		}
		return 0;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return ModBlockEntityTypes.COOKING_POT.get().create(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
		if (level.isClientSide) {
			return createTickerHelper(blockEntity, ModBlockEntityTypes.COOKING_POT.get(), CookingPotBlockEntity::animationTick);
		} else {
			return createTickerHelper(blockEntity, ModBlockEntityTypes.COOKING_POT.get(), CookingPotBlockEntity::cookingTick);
		}
	}
}