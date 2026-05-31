package vectorwing.farmersdelight.common.loot.modifier;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import vectorwing.farmersdelight.common.Configuration;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * Credits to Commoble for this implementation!
 */
public class FDAddTableLootModifier extends AddTableLootModifier
{
	public static final Supplier<MapCodec<FDAddTableLootModifier>> CODEC = Suppliers.memoize(() ->
			RecordCodecBuilder.mapCodec(inst -> codecStart(inst)
					.and(ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("lootTable").forGetter((m) -> m.lootTable))
					.apply(inst, FDAddTableLootModifier::new)));

	private final ResourceKey<LootTable> lootTable;

	protected FDAddTableLootModifier(LootItemCondition[] conditionsIn, int priority, ResourceKey<LootTable> lootTable) {
		super(conditionsIn, priority, lootTable);
		this.lootTable = lootTable;
	}

	@Nonnull
	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		if (Configuration.GENERATE_FD_CHEST_LOOT.get()) {
			return super.doApply(generatedLoot, context);
		}
		return generatedLoot;
	}
}
