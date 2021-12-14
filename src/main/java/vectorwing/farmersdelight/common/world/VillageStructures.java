package vectorwing.farmersdelight.common.world;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.structures.SinglePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import vectorwing.farmersdelight.FarmersDelight;

import java.util.ArrayList;
import java.util.List;

public class VillageStructures
{
	public static void addNewVillageBuilding(final ServerAboutToStartEvent event) {
		Registry<StructureTemplatePool> templatePool = event.getServer().registryAccess().registry(Registry.TEMPLATE_POOL_REGISTRY).get();

		VillageStructures.addBuildingToPool(templatePool, new ResourceLocation("minecraft:village/plains/houses"), FarmersDelight.MODID + ":village/houses/plains_compost_pile", 5);
		VillageStructures.addBuildingToPool(templatePool, new ResourceLocation("minecraft:village/snowy/houses"), FarmersDelight.MODID + ":village/houses/snowy_compost_pile", 3);
		VillageStructures.addBuildingToPool(templatePool, new ResourceLocation("minecraft:village/savanna/houses"), FarmersDelight.MODID + ":village/houses/savanna_compost_pile", 4);
		VillageStructures.addBuildingToPool(templatePool, new ResourceLocation("minecraft:village/desert/houses"), FarmersDelight.MODID + ":village/houses/desert_compost_pile", 3);
		VillageStructures.addBuildingToPool(templatePool, new ResourceLocation("minecraft:village/taiga/houses"), FarmersDelight.MODID + ":village/houses/taiga_compost_pile", 4);
	}

	public static void addBuildingToPool(Registry<StructureTemplatePool> templatePoolRegistry, ResourceLocation poolRL, String nbtPieceRL, int weight) {
		StructureTemplatePool pool = templatePoolRegistry.get(poolRL);
		if (pool == null) return;

		SinglePoolElement piece = SinglePoolElement.single(nbtPieceRL).apply(StructureTemplatePool.Projection.RIGID);

		for (int i = 0; i < weight; i++) {
			pool.templates.add(piece);
		}

		List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList<>(pool.rawTemplates);
		listOfPieceEntries.add(new Pair<>(piece, weight));
		pool.rawTemplates = listOfPieceEntries;
	}
}
