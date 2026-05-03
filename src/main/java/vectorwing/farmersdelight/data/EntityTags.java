package vectorwing.farmersdelight.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
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
		this.tag(ModTags.EntityTypes.DOG_FOOD_USERS).add(EntityType.WOLF);
		this.tag(ModTags.EntityTypes.HORSE_FEED_USERS).add(
			EntityType.HORSE,
			EntityType.SKELETON_HORSE,
			EntityType.ZOMBIE_HORSE,
			EntityType.DONKEY,
			EntityType.MULE,
			EntityType.LLAMA);
		this.tag(ModTags.EntityTypes.HORSE_FEED_TEMPTED).add(
			EntityType.HORSE,
			EntityType.DONKEY,
			EntityType.MULE);
	}
}
