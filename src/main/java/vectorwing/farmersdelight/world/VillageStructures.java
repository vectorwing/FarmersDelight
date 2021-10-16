package vectorwing.farmersdelight.world;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.LegacySinglePoolElement;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.data.worldgen.ProcessorLists;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.data.worldgen.DesertVillagePools;
import net.minecraft.data.worldgen.PlainVillagePools;
import net.minecraft.data.worldgen.SavannaVillagePools;
import net.minecraft.data.worldgen.SnowyVillagePools;
import net.minecraft.data.worldgen.TaigaVillagePools;

public class VillageStructures
{
	public static void init() {
		PlainVillagePools.bootstrap();
		SnowyVillagePools.bootstrap();
		SavannaVillagePools.bootstrap();
		DesertVillagePools.bootstrap();
		TaigaVillagePools.bootstrap();

		Map<String, Integer> biomeChances = (new ImmutableMap.Builder<String, Integer>())
				.put("plains", 5)
				.put("snowy", 3)
				.put("savanna", 4)
				.put("desert", 3)
				.put("taiga", 4)
				.build();

		for (Map.Entry<String, Integer> biome : biomeChances.entrySet()) {
			addToPool(new ResourceLocation("village/" + biome.getKey() + "/houses"), new ResourceLocation(FarmersDelight.MODID, "village/houses/" + biome.getKey() + "_compost_pile"), biome.getValue());
		}
	}

	private static void addToPool(ResourceLocation pool, ResourceLocation toAdd, int weight) {
		/*   */ StructureTemplatePool old = BuiltinRegistries.TEMPLATE_POOL.get(pool);
		List<StructurePoolElement> shuffled = old != null ? old.getShuffledTemplates(MathUtils.RAND) : ImmutableList.of();
		Object2IntMap<StructurePoolElement> newPieces = new Object2IntLinkedOpenHashMap<>();
		for (StructurePoolElement p : shuffled) {
			newPieces.computeInt(new Pair<>(p, 1));
		}
		newPieces.add(new Pair<>(new LegacySinglePoolElement(Either.left(toAdd), () -> ProcessorLists.EMPTY, StructureTemplatePool.Projection.RIGID), weight));
		ResourceLocation name = old.getName();
		Registry.register(BuiltinRegistries.TEMPLATE_POOL, pool, new StructureTemplatePool(pool, name, newPieces));
	}
}
