package vectorwing.farmersdelight.common.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

public record MushroomColony(Block colonyBlock)
{
	public static final Codec<MushroomColony> MUSHROOM_COLONY_CODEC = BuiltInRegistries.BLOCK.byNameCodec()
		.xmap(MushroomColony::new, MushroomColony::colonyBlock);
	public static final Codec<MushroomColony> CODEC = Codec.withAlternative(
		RecordCodecBuilder.create(in -> in.group(
			BuiltInRegistries.BLOCK.byNameCodec().fieldOf("colony_block").forGetter(MushroomColony::colonyBlock)).apply(in, MushroomColony::new)),
		MUSHROOM_COLONY_CODEC);
}
