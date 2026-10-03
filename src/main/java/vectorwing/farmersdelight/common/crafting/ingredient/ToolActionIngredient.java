package vectorwing.farmersdelight.common.crafting.ingredient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ToolActionIngredient extends Ingredient
{
	public static final Serializer SERIALIZER = new Serializer();

	private static final ThreadLocal<Map<ToolAction, List<Item>>> SHARED_ITEMS = new ThreadLocal<>();
	private static final ThreadLocal<BufferItems> BUFFER_ITEMS = new ThreadLocal<>();

	public final ToolAction toolAction;

	/**
	 * Ingredient that checks if the given stack can perform a ToolAction from Forge.
	 */
	public ToolActionIngredient(ToolAction toolAction) {
		super(getMatchingStacks(toolAction)
				.map(Ingredient.ItemValue::new));
		this.toolAction = toolAction;
	}

	/**
	 * Creates a stack of every item that can perform the given ToolAction.
	 * <p>
	 * Going through the whole item registry is slow in large modpacks, so while a batch of recipes is being read
	 * (see {@link #setSharedItems}), only the first ingredient of each ToolAction does it.
	 * The following ones only check the items it found again, with stacks of their own.
	 */
	private static Stream<ItemStack> getMatchingStacks(ToolAction toolAction) {
		Map<ToolAction, List<Item>> sharedItems = SHARED_ITEMS.get();
		if (sharedItems == null) {
			return createMatchingStacks(ForgeRegistries.ITEMS.getValues(), toolAction);
		}

		List<Item> items = sharedItems.get(toolAction);
		if (items != null) {
			return createMatchingStacks(items, toolAction);
		}

		List<ItemStack> stacks = createMatchingStacks(ForgeRegistries.ITEMS.getValues(), toolAction).toList();
		sharedItems.putIfAbsent(toolAction, stacks.stream().map(ItemStack::getItem).toList());
		return stacks.stream();
	}

	private static Stream<ItemStack> createMatchingStacks(Collection<Item> items, ToolAction toolAction) {
		return items.stream()
				.map(ItemStack::new)
				.filter(stack -> stack.canPerformAction(toolAction));
	}

	/**
	 * Sets the map where ingredients created on the current thread remember the items found for each ToolAction.
	 * It should only be set while a batch of recipes is being read, and put back to the previous one afterwards.
	 *
	 * @return the map that was set before, if any.
	 */
	@Nullable
	public static Map<ToolAction, List<Item>> setSharedItems(@Nullable Map<ToolAction, List<Item>> sharedItems) {
		Map<ToolAction, List<Item>> previousItems = SHARED_ITEMS.get();
		SHARED_ITEMS.set(sharedItems);
		return previousItems;
	}

	/**
	 * Ingredients read from the same buffer, such as the recipes sent by a server, share their items.
	 * The last buffer is only remembered weakly, so that it is not kept in memory.
	 */
	private static Map<ToolAction, List<Item>> getItemsSharedBy(FriendlyByteBuf buffer) {
		BufferItems bufferItems = BUFFER_ITEMS.get();
		if (bufferItems == null || bufferItems.buffer().get() != buffer) {
			bufferItems = new BufferItems(new WeakReference<>(buffer), new HashMap<>());
			BUFFER_ITEMS.set(bufferItems);
		}
		return bufferItems.items();
	}

	@Override
	public boolean test(@Nullable ItemStack stack) {
		return stack != null && stack.canPerformAction(toolAction);
	}

	@Override
	public JsonElement toJson() {
		JsonObject json = new JsonObject();
		json.addProperty("type", CraftingHelper.getID(SERIALIZER).toString());
		json.addProperty("action", toolAction.name());
		return json;
	}

	@Override
	public IIngredientSerializer<? extends Ingredient> getSerializer() {
		return SERIALIZER;
	}

	public static class Serializer implements IIngredientSerializer<ToolActionIngredient>
	{
		@Override
		public ToolActionIngredient parse(JsonObject json) {
			return new ToolActionIngredient(ToolAction.get(json.get("action").getAsString()));
		}

		@Override
		public ToolActionIngredient parse(FriendlyByteBuf buffer) {
			Map<ToolAction, List<Item>> previousItems = setSharedItems(getItemsSharedBy(buffer));
			try {
				return new ToolActionIngredient(ToolAction.get(buffer.readUtf()));
			} finally {
				setSharedItems(previousItems);
			}
		}

		@Override
		public void write(FriendlyByteBuf buffer, ToolActionIngredient ingredient) {
			buffer.writeUtf(ingredient.toolAction.name());
		}
	}

	private record BufferItems(WeakReference<FriendlyByteBuf> buffer, Map<ToolAction, List<Item>> items)
	{
	}
}
