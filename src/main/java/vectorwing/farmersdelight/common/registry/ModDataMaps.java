package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import vectorwing.farmersdelight.common.datamap.MushroomColony;
import vectorwing.farmersdelight.common.utility.RecipeUtils;

public class ModDataMaps
{
	public static final DataMapType<Block, MushroomColony> MUSHROOM_COLONIES = DataMapType.builder(
		RecipeUtils.FDLocation("mushroom_colonies"), Registries.BLOCK, MushroomColony.CODEC).synced(MushroomColony.MUSHROOM_COLONY_CODEC, false).build();
}
