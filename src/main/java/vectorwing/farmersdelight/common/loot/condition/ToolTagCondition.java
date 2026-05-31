package vectorwing.farmersdelight.common.loot.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

/**
 * A loot condition that succeeds when the item involved in the drop belongs to a given item {@link TagKey}.
 *
 * <p>Unlike vanilla's {@code minecraft:match_tool} (whose nested {@code ItemPredicate} resolves the tag's
 * <em>members</em> the moment the condition is deserialized), this condition stores only the {@link TagKey}
 * and tests membership lazily, when the loot actually rolls. That sidesteps a NeoForge ordering issue:
 * Global Loot Modifiers are decoded in the off-thread {@code prepare()} phase, before item tags are bound,
 * so a decode-time tag lookup there fails with "Missing tag". By the time {@link #test} runs, tags are bound.
 */
public record ToolTagCondition(TagKey<Item> tag, Source source) implements LootItemCondition
{
	public static final MapCodec<ToolTagCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
			TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter(ToolTagCondition::tag),
			Source.CODEC.optionalFieldOf("source", Source.TOOL).forGetter(ToolTagCondition::source)
	).apply(inst, ToolTagCondition::new));

	@Override
	public MapCodec<? extends LootItemCondition> codec() {
		return CODEC;
	}

	@Override
	public boolean test(LootContext context) {
		ItemInstance used = switch (this.source) {
			case TOOL -> context.getOptionalParameter(LootContextParams.TOOL);
			case ATTACKER_MAINHAND -> context.getOptionalParameter(LootContextParams.ATTACKING_ENTITY) instanceof LivingEntity attacker
					? attacker.getMainHandItem()
					: null;
		};
		return used != null && used.is(this.tag);
	}

	/**
	 * Where to look for the item whose tag membership decides this condition.
	 */
	public enum Source implements StringRepresentable
	{
		/** The tool used to break a block — {@link LootContextParams#TOOL}. Used by block-drop modifiers (slicing, straw). */
		TOOL("tool"),
		/** The main-hand item of the attacking entity — from {@link LootContextParams#ATTACKING_ENTITY}. Used by kill-drop modifiers (scavenging). */
		ATTACKER_MAINHAND("attacker_mainhand");

		public static final Codec<Source> CODEC = StringRepresentable.fromEnum(Source::values);

		private final String name;

		Source(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}
	}
}
