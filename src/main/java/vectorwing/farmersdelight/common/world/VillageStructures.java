package vectorwing.farmersdelight.common.world;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;

import java.util.ArrayList;
import java.util.List;

public class VillageStructures
{
	public static void addNewVillageBuilding(final ServerAboutToStartEvent event) {
		if (!Configuration.GENERATE_VILLAGE_COMPOST_HEAPS.get()) {
			return;
		}

		Registry<StructureTemplatePool> templatePools = event.getServer().registryAccess().registry(Registry.TEMPLATE_POOL_REGISTRY).get();
		Registry<StructureProcessorList> processorLists = event.getServer().registryAccess().registry(Registry.PROCESSOR_LIST_REGISTRY).get();

		VillageStructures.addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/plains/houses"), FarmersDelight.MODID + ":village/houses/plains_compost_pile", 5);
		VillageStructures.addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/snowy/houses"), FarmersDelight.MODID + ":village/houses/snowy_compost_pile", 3);
		VillageStructures.addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/savanna/houses"), FarmersDelight.MODID + ":village/houses/savanna_compost_pile", 4);
		VillageStructures.addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/desert/houses"), FarmersDelight.MODID + ":village/houses/desert_compost_pile", 3);
		VillageStructures.addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/taiga/houses"), FarmersDelight.MODID + ":village/houses/taiga_compost_pile", 4);
	}

	public static void addBuildingToPool(Registry<StructureTemplatePool> templatePoolRegistry, Registry<StructureProcessorList> processorListRegistry, ResourceLocation poolRL, String nbtPieceRL, int weight) {
		StructureTemplatePool pool = templatePoolRegistry.get(poolRL);
		if (pool == null) return;

		ResourceLocation emptyProcessor = new ResourceLocation("minecraft", "empty");
		Holder<StructureProcessorList> processorHolder = processorListRegistry.getHolderOrThrow(ResourceKey.create(Registry.PROCESSOR_LIST_REGISTRY, emptyProcessor));

		SinglePoolElement piece = SinglePoolElement.single(nbtPieceRL, processorHolder).apply(StructureTemplatePool.Projection.RIGID);

		for (int i = 0; i < weight; i++) {
			pool.templates.add(piece);
		}

		List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList<>(pool.rawTemplates);
		listOfPieceEntries.add(new Pair<>(piece, weight));
		pool.rawTemplates = listOfPieceEntries;
	}
}
