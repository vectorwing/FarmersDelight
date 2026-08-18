package vectorwing.farmersdelight.common.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import vectorwing.farmersdelight.common.mixin.accessor.CropBlockAccessor;

public class BlockUtils
{
	public static boolean isValidCrop(Level level, BlockPos pos, BlockState state) {
		if (state.getBlock() instanceof CropBlock) {
			return true;
		}

		if (state.is(BlockTags.CROPS)) {
			return true;
		}

		if (state.getCollisionShape(level, pos).isEmpty() && state.getBlock().defaultDestroyTime() == 0.0F) {
			for (Property<?> property : state.getProperties()) {
				if (!(property instanceof IntegerProperty))
					continue;
				if (property.equals(BlockStateProperties.AGE_25) || property.equals(BlockStateProperties.AGE_15))
					continue;
				if (!property.getName().equals(BlockStateProperties.AGE_1.getName()))
					continue;
				return true;
			}
		}

		return false;
	}

	public static boolean isCropMature(Level level, BlockPos pos, BlockState state) {
		if (state.getBlock() instanceof CropBlock crop) {
			return crop.isMaxAge(state);
		}

		if (state.is(BlockTags.CROPS) || state.getBlock() instanceof CocoaBlock) {
			for (Property<?> property : state.getProperties()) {
				if (!(property instanceof IntegerProperty ageProperty))
					continue;
				if (!property.getName().equals(BlockStateProperties.AGE_1.getName()))
					continue;
				int age = state.getValue(ageProperty);
				if (state.getBlock() instanceof SweetBerryBushBlock && age <= 1)
					continue;
				if (age == 0 || (ageProperty.getPossibleValues().size() - 1 != age))
					continue;
				return true;
			}
		}

		return false;
	}

	public static BlockState getHarvestedCropState(Level world, BlockPos pos, BlockState state) {
		Block block = state.getBlock();
		if (block instanceof CropBlock crop) {
			BlockState newState = crop.getStateForAge(0);
			if (!newState.is(block))
				return newState;
			IntegerProperty ageProperty = ((CropBlockAccessor) crop).fd$getAgeProperty();
			return state.setValue(ageProperty, 0);
		}
		if (block == Blocks.SWEET_BERRY_BUSH) {
			return state.setValue(BlockStateProperties.AGE_3, 1);
		}
		if (state.getCollisionShape(world, pos)
			.isEmpty() || block instanceof CocoaBlock) {
			for (Property<?> property : state.getProperties()) {
				if (!(property instanceof IntegerProperty))
					continue;
				if (!property.getName()
					.equals(BlockStateProperties.AGE_1.getName()))
					continue;
				return state.setValue((IntegerProperty) property, 0);
			}
		}

		if (state.getFluidState()
			.isEmpty())
			return Blocks.AIR.defaultBlockState();
		return state.getFluidState()
			.createLegacyBlock();
	}
}
