package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import vectorwing.farmersdelight.FarmersDelight;

public class ModDataMaps {

	public static final DataMapType<Block, Block> RICH_SOIL_TRANSFORMATION = DataMapType.builder(
		ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, "rich_soil_transformation"), Registries.BLOCK, BuiltInRegistries.BLOCK.byNameCodec())
		.synced(BuiltInRegistries.BLOCK.byNameCodec(), false).build();
}
