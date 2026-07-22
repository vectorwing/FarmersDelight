package vectorwing.farmersdelight.common.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import vectorwing.farmersdelight.common.block.WildRiceBlock;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class WildRiceFeature extends Feature<NoneFeatureConfiguration> {
	public WildRiceFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();

        if (level.getBlockState(origin).getBlock() == Blocks.WATER && level.getBlockState(origin.above()).getBlock() == Blocks.AIR) {
            BlockState bottomRiceState = ModBlocks.WILD_RICE.get().defaultBlockState().setValue(WildRiceBlock.HALF, DoubleBlockHalf.LOWER);
            if (bottomRiceState.canSurvive(level, origin)) {
                DoublePlantBlock.placeAt(level, bottomRiceState, origin, 2);
                return true;
            }
        }
        return false;
	}
}


