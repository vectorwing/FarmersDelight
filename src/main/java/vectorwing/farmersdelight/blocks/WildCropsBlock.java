package vectorwing.farmersdelight.blocks;

import net.minecraft.block.AbstractBlock.OffsetType;
import net.minecraft.block.AbstractBlock.Properties;

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
