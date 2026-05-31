package vectorwing.farmersdelight.data;

import vectorwing.farmersdelight.FarmersDelight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Item-model datagen helpers.
 * <p>
 * Since Minecraft 1.21.4 / NeoForge 26.1, blockstates, block models and item models are all produced by a single
 * vanilla {@link net.minecraft.client.data.models.ModelProvider} (see {@link BlockStates}). This class no longer
 * extends a provider; it only retains the {@code takeAll} utilities used while mass-assigning item models.
 * <p>
 * Credits to Vazkii and team for some references on mass-reading blocks to datagen!
 */
public final class ItemModels
{
	private ItemModels() {
	}

	@SafeVarargs
	@SuppressWarnings("varargs")
	public static <T> Collection<T> takeAll(Set<? extends T> src, T... items) {
		List<T> ret = Arrays.asList(items);
		for (T item : items) {
			if (!src.contains(item)) {
				FarmersDelight.LOGGER.warn("Item {} not found in set", item);
			}
		}
		if (!src.removeAll(ret)) {
			FarmersDelight.LOGGER.warn("takeAll array didn't yield anything ({})", Arrays.toString(items));
		}
		return ret;
	}

	public static <T> Collection<T> takeAll(Set<T> src, Predicate<T> pred) {
		List<T> ret = new ArrayList<>();

		Iterator<T> iter = src.iterator();
		while (iter.hasNext()) {
			T item = iter.next();
			if (pred.test(item)) {
				iter.remove();
				ret.add(item);
			}
		}

		if (ret.isEmpty()) {
			FarmersDelight.LOGGER.warn("takeAll predicate yielded nothing", new Throwable());
		}
		return ret;
	}
}
