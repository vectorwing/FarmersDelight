package vectorwing.farmersdelight.common.crafting.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.conditions.ICondition;

public record FluidTagEmptyCondition(TagKey<Fluid> tag) implements ICondition
{
	public static final MapCodec<FluidTagEmptyCondition> CODEC = RecordCodecBuilder.mapCodec(
		builder -> builder
			.group(
				ResourceLocation.CODEC.xmap(loc -> TagKey.create(Registries.FLUID, loc), TagKey::location).fieldOf("tag").forGetter(FluidTagEmptyCondition::tag))
			.apply(builder, FluidTagEmptyCondition::new));

	public FluidTagEmptyCondition(String location) {
		this(ResourceLocation.parse(location));
	}

	public FluidTagEmptyCondition(String namespace, String path) {
		this(ResourceLocation.fromNamespaceAndPath(namespace, path));
	}

	public FluidTagEmptyCondition(ResourceLocation tag) {
		this(TagKey.create(Registries.FLUID, tag));
	}

	@Override
	public boolean test(IContext context) {
		return context.getTag(tag).isEmpty();
	}

	@Override
	public MapCodec<? extends ICondition> codec() {
		return CODEC;
	}

	@Override
	public String toString() {
		return "tag_empty(\"" + tag.location() + "\")";
	}
}
