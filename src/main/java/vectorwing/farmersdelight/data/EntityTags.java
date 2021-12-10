package vectorwing.farmersdelight.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import vectorwing.farmersdelight.common.tag.ModTags;

import javax.annotation.Nullable;

public class EntityTags extends EntityTypeTagsProvider
{
	public EntityTags(DataGenerator generator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
		super(generator, modId, existingFileHelper);
	}

	@Override
	protected void addTags() {
		this.tag(ModTags.DOG_FOOD_USERS).add(EntityType.WOLF);
		this.tag(ModTags.HORSE_FEED_USERS).add(EntityType.HORSE, EntityType.SKELETON_HORSE, EntityType.ZOMBIE_HORSE, EntityType.DONKEY, EntityType.MULE, EntityType.LLAMA);
	}
}
