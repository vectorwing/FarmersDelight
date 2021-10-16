package vectorwing.farmersdelight.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour.OffsetType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class WildCropsBlock extends WildPatchBlock
{
	public WildCropsBlock(Properties properties) {
		super(properties);
	}

	@Override
	public OffsetType getOffsetType() {
		return OffsetType.NONE;
	}
}
