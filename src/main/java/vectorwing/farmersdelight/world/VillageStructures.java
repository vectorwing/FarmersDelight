package vectorwing.farmersdelight.world;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.structure.*;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VillageStructures {

	public static void init() {
		PlainsVillagePools.init();
		SnowyVillagePools.init();
		SavannaVillagePools.init();
		DesertVillagePools.init();
		TaigaVillagePools.init();

		Map<String, Integer> biomeChances = (new ImmutableMap.Builder<String, Integer>())
				.put("plains", 5)
				.put("snowy", 2)
				.put("savanna", 3)
				.put("desert", 3)
				.put("taiga", 3)
				.build();

		for (Map.Entry<String, Integer> biome : biomeChances.entrySet()) {
			addToPool(new ResourceLocation("village/"+biome.getKey()+"/houses"),	new ResourceLocation(FarmersDelight.MODID, "village/houses/"+biome.getKey()+"_compost_pile"), biome.getValue());
		}
	}

	private static void addToPool(ResourceLocation pool, ResourceLocation toAdd, int weight) {
		JigsawPattern old = JigsawManager.REGISTRY.get(pool);
		List<JigsawPiece> shuffled = old.getShuffledPieces(MathUtils.RAND);
		List<Pair<JigsawPiece, Integer>> newPieces = new ArrayList<>();
		for(JigsawPiece p : shuffled)
		{
			newPieces.add(new Pair<>(p, 1));
		}
		newPieces.add(new Pair<>(new SingleJigsawPiece(toAdd.toString()), weight));
		ResourceLocation name = old.getName();
		JigsawManager.REGISTRY.register(new JigsawPattern(pool, name, newPieces, JigsawPattern.PlacementBehaviour.RIGID));
	}
}
