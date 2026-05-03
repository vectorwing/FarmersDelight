package vectorwing.farmersdelight.data.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import vectorwing.farmersdelight.data.loot.FDBlockLoot;
import vectorwing.farmersdelight.data.loot.FDChestLoot;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LootTables extends LootTableProvider
{
	public LootTables(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, Collections.emptySet(), List.of(
			new LootTableProvider.SubProviderEntry(FDBlockLoot::new, LootContextParamSets.BLOCK),
			new LootTableProvider.SubProviderEntry(FDChestLoot::new, LootContextParamSets.CHEST)
		), registries);
	}
}
