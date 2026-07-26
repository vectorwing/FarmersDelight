package vectorwing.farmersdelight.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import vectorwing.farmersdelight.FarmersDelight;

import java.util.concurrent.CompletableFuture;

public class EnchantmentTags extends EnchantmentTagsProvider
{
	public EnchantmentTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, FarmersDelight.MODID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(net.minecraft.tags.EnchantmentTags.NON_TREASURE).add(ModEnchantments.BACKSTABBING);
	}
}
