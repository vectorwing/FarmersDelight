package vectorwing.farmersdelight.common.world;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class VillageStructures
{
	public static void addNewVillageBuilding(final ServerAboutToStartEvent event) {
		if (Configuration.GENERATE_VILLAGE_COMPOST_HEAPS.get()) {
			Registry<StructureTemplatePool> templatePools = event.getServer().registryAccess().registry(Registries.TEMPLATE_POOL).get();
			Registry<StructureProcessorList> processorLists = event.getServer().registryAccess().registry(Registries.PROCESSOR_LIST).get();

			VillageStructures.addBuildingToPool(templatePools, processorLists, ResourceLocation.parse("minecraft:village/plains/houses"), FarmersDelight.MODID + ":village/houses/plains_compost_pile", 5);
			VillageStructures.addBuildingToPool(templatePools, processorLists, ResourceLocation.parse("minecraft:village/snowy/houses"), FarmersDelight.MODID + ":village/houses/snowy_compost_pile", 3);
			VillageStructures.addBuildingToPool(templatePools, processorLists, ResourceLocation.parse("minecraft:village/savanna/houses"), FarmersDelight.MODID + ":village/houses/savanna_compost_pile", 4);
			VillageStructures.addBuildingToPool(templatePools, processorLists, ResourceLocation.parse("minecraft:village/desert/houses"), FarmersDelight.MODID + ":village/houses/desert_compost_pile", 3);
			VillageStructures.addBuildingToPool(templatePools, processorLists, ResourceLocation.parse("minecraft:village/taiga/houses"), FarmersDelight.MODID + ":village/houses/taiga_compost_pile", 4);
		}

		if (Configuration.GENERATE_VILLAGE_FARM_FD_CROPS.get()) {
			Registry<StructureProcessorList> processorLists = event.getServer().registryAccess().registry(Registries.PROCESSOR_LIST).orElseThrow();

			StructureProcessor temperateCropProcessor = new RuleProcessor(List.of(
					new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), AlwaysTrueTest.INSTANCE, ModBlocks.CABBAGE_CROP.get().defaultBlockState()),
					new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), AlwaysTrueTest.INSTANCE, ModBlocks.TOMATO_CROP.get().defaultBlockState()),
					new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), AlwaysTrueTest.INSTANCE, ModBlocks.ONION_CROP.get().defaultBlockState())
			));

			StructureProcessor coldCropProcessor = new RuleProcessor(List.of(
					new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), AlwaysTrueTest.INSTANCE, ModBlocks.CABBAGE_CROP.get().defaultBlockState()),
					new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), AlwaysTrueTest.INSTANCE, ModBlocks.ONION_CROP.get().defaultBlockState()),
					new ProcessorRule(new RandomBlockMatchTest(Blocks.POTATOES, 0.2F), AlwaysTrueTest.INSTANCE, ModBlocks.CABBAGE_CROP.get().defaultBlockState()),
					new ProcessorRule(new RandomBlockMatchTest(Blocks.POTATOES, 0.2F), AlwaysTrueTest.INSTANCE, ModBlocks.ONION_CROP.get().defaultBlockState())
			));

			StructureProcessor aridCropProcessor = new RuleProcessor(List.of(
					new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), AlwaysTrueTest.INSTANCE, ModBlocks.CABBAGE_CROP.get().defaultBlockState()),
					new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, 0.3F), AlwaysTrueTest.INSTANCE, ModBlocks.TOMATO_CROP.get().defaultBlockState())
			));

			addNewRuleToProcessorList(ResourceLocation.parse("minecraft:farm_plains"), temperateCropProcessor, processorLists);
			addNewRuleToProcessorList(ResourceLocation.parse("minecraft:farm_savanna"), aridCropProcessor, processorLists);
			addNewRuleToProcessorList(ResourceLocation.parse("minecraft:farm_snowy"), coldCropProcessor, processorLists);
			addNewRuleToProcessorList(ResourceLocation.parse("minecraft:farm_taiga"), temperateCropProcessor, processorLists);
			addNewRuleToProcessorList(ResourceLocation.parse("minecraft:farm_desert"), aridCropProcessor, processorLists);
		}
	}

	public static void addBuildingToPool(Registry<StructureTemplatePool> templatePoolRegistry, Registry<StructureProcessorList> processorListRegistry, ResourceLocation poolRL, String nbtPieceRL, int weight) {
		StructureTemplatePool pool = templatePoolRegistry.get(poolRL);
		if (pool == null) return;

		ResourceLocation emptyProcessor = ResourceLocation.withDefaultNamespace("empty");
		Holder<StructureProcessorList> processorHolder = processorListRegistry.getHolderOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, emptyProcessor));

		SinglePoolElement piece = SinglePoolElement.single(nbtPieceRL, processorHolder).apply(StructureTemplatePool.Projection.RIGID);

		for (int i = 0; i < weight; i++) {
			pool.templates.add(piece);
		}

		List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList<>(pool.rawTemplates);
		listOfPieceEntries.add(new Pair<>(piece, weight));
		pool.rawTemplates = listOfPieceEntries;
	}

	private static void addNewRuleToProcessorList(ResourceLocation targetProcessorList, StructureProcessor processorToAdd, Registry<StructureProcessorList> processorListRegistry) {
		processorListRegistry.getOptional(targetProcessorList)
				.ifPresent(processorList -> {
					List<StructureProcessor> newSafeList = new ArrayList<>(processorList.list());
					newSafeList.add(processorToAdd);
					processorList.list = newSafeList;
				});
	}
}
