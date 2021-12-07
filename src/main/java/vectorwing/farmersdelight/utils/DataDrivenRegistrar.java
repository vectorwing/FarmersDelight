package vectorwing.farmersdelight.utils;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;

public class DataDrivenRegistrar<IN> {

	public final String key;
	protected Function<IN, Block> blockFactory;
	protected Function<Block, Item> itemFactory;
	private List<Item> itemsToAdd;

	public DataDrivenRegistrar(IEventBus bus, String key) {
		this.key = key;
		bus.addGenericListener(Block.class, this::registerBlocks);
		bus.addGenericListener(Item.class, this::registerItems);
	}

	public DataDrivenRegistrar<IN> blockFactory(Function<IN, Block> blockFactory) {
		this.blockFactory = blockFactory;
		return this;
	}

	public DataDrivenRegistrar<IN> itemFactory(Function<Block, Item> itemFactory) {
		this.itemFactory = itemFactory;
		return this;
	}

	private void registerBlocks(RegistryEvent.Register<Block> event) {
	    itemsToAdd = stream()
				.map(blockFactory::apply)
				.filter(Objects::nonNull)
				.peek(event.getRegistry()::register)
				.map(itemFactory::apply)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	private void registerItems(RegistryEvent.Register<Item> event) {
		itemsToAdd.forEach(event.getRegistry()::register);
		itemsToAdd = null;
	}

	protected Stream<IN> stream() {
		return ImmutableList.copyOf(ModList.get().getMods())
				.stream()
				.map(IModInfo::getModProperties)
				.filter($ -> $.containsKey(key))
				.map($ -> (List<IN>) $.get(key))
				.flatMap($ -> $.stream());
	}

}
