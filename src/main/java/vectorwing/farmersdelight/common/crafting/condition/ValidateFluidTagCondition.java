package vectorwing.farmersdelight.common.crafting.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.conditions.ICondition;

/**
 * Validates the given fluid tag to ensure it isn't empty.
 * @param tag The tag to be validated
 */
public record ValidateFluidTagCondition(TagKey<Fluid> tag) implements ICondition
{
	public static final MapCodec<ValidateFluidTagCondition> CODEC = RecordCodecBuilder.mapCodec(
		builder -> builder
			.group(
				ResourceLocation.CODEC.xmap(loc -> TagKey.create(Registries.FLUID, loc), TagKey::location).fieldOf("tag").forGetter(ValidateFluidTagCondition::tag))
			.apply(builder, ValidateFluidTagCondition::new));

	public ValidateFluidTagCondition(String location) {
		this(ResourceLocation.parse(location));
	}

	public ValidateFluidTagCondition(String namespace, String path) {
		this(ResourceLocation.fromNamespaceAndPath(namespace, path));
	}

	public ValidateFluidTagCondition(ResourceLocation tag) {
		this(TagKey.create(Registries.FLUID, tag));
	}

	@Override
	public boolean test(IContext context) {
		return !context.getTag(tag).isEmpty();
	}

	@Override
	public MapCodec<? extends ICondition> codec() {
		return CODEC;
	}

	@Override
	public String toString() {
		return "validate_fluid_tag(\"" + tag.location() + "\")";
	}
}
