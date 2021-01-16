package vectorwing.farmersdelight.world;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.LegacySingleJigsawPiece;
import net.minecraft.world.gen.feature.structure.*;
import net.minecraft.world.gen.feature.template.ProcessorLists;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VillageStructures
{
	public static void init() {
		PlainsVillagePools.init();
		SnowyVillagePools.init();
		SavannaVillagePools.init();
		DesertVillagePools.init();
		TaigaVillagePools.init();

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
		JigsawPattern old = WorldGenRegistries.JIGSAW_POOL.getOrDefault(pool);
		List<JigsawPiece> shuffled = old.getShuffledPieces(MathUtils.RAND);
		List<Pair<JigsawPiece, Integer>> newPieces = new ArrayList<>();
		for (JigsawPiece p : shuffled) {
			newPieces.add(new Pair<>(p, 1));
		}
		newPieces.add(new Pair<>(new LegacySingleJigsawPiece(Either.left(toAdd), () -> ProcessorLists.field_244101_a, JigsawPattern.PlacementBehaviour.RIGID), weight));
		ResourceLocation name = old.getName();
		Registry.register(WorldGenRegistries.JIGSAW_POOL, pool, new JigsawPattern(pool, name, newPieces));
	}
}
