package vectorwing.farmersdelight.common.utility;

import java.util.List;
import java.util.function.Function;

/**
 * Util for manipulating lists.
 */
public class ListUtils
{
	/**
	 * Map an array of objects into an existing list, using {@link List#set(int, Object)}.
	 *
	 * @param array  The array to map.
	 * @param mapper The function to use to convert from array elements to list elements.
	 * @param list   the list to populate.
	 * @param <F>    The type of the elements in the array.
	 * @param <T>    The type of elements in the list.
	 * @param <L>    The type of the list.
	 * @return The list, with the appropriate elements set.
	 */
	public static <F, T, L extends List<T>> L mapArrayIndexSet(F[] array, Function<F, T> mapper, L list) {
		int i = 0;
		for (F f : array) {
			list.set(i++, mapper.apply(f));
		}
		return list;
	}
}
