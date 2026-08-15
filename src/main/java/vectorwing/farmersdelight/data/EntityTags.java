package vectorwing.farmersdelight.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypeIds;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.concurrent.CompletableFuture;

public class EntityTags extends EntityTypeTagsProvider
{
	public EntityTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, FarmersDelight.MODID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(ModTags.EntityTypes.DOG_FOOD_USERS).add(EntityTypeIds.WOLF);
		this.tag(ModTags.EntityTypes.HORSE_FEED_USERS).add(
				EntityTypeIds.HORSE,
				EntityTypeIds.SKELETON_HORSE,
				EntityTypeIds.ZOMBIE_HORSE,
				EntityTypeIds.DONKEY,
				EntityTypeIds.MULE,
				EntityTypeIds.LLAMA);
		this.tag(ModTags.EntityTypes.HORSE_FEED_TEMPTED).add(
				EntityTypeIds.HORSE,
				EntityTypeIds.DONKEY,
				EntityTypeIds.MULE);
	}

}
