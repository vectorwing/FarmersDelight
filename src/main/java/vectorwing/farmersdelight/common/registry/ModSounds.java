package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;

import java.util.function.Supplier;

public class ModSounds
{
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, FarmersDelight.MODID);

	// Stove
	public static final Supplier<SoundEvent> BLOCK_STOVE_CRACKLE = SOUNDS.register("block.stove.crackle",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "block.stove.crackle")));

	// Cooking Pot
	public static final Supplier<SoundEvent> BLOCK_COOKING_POT_BOIL = SOUNDS.register("block.cooking_pot.boil",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "block.cooking_pot.boil")));
	public static final Supplier<SoundEvent> BLOCK_COOKING_POT_BOIL_SOUP = SOUNDS.register("block.cooking_pot.boil_soup",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "block.cooking_pot.boil_soup")));

	// Cutting Board
	public static final Supplier<SoundEvent> BLOCK_CUTTING_BOARD_PLACE = SOUNDS.register("block.cutting_board.place_item",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "block.cutting_board.place_item")));
	public static final Supplier<SoundEvent> BLOCK_CUTTING_BOARD_REMOVE = SOUNDS.register("block.cutting_board.remove_item",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "block.cutting_board.remove_item")));
	public static final Supplier<SoundEvent> BLOCK_CUTTING_BOARD_CARVE = SOUNDS.register("block.cutting_board.carve_tool",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "block.cutting_board.carve_tool")));
	public static final Supplier<SoundEvent> BLOCK_CUTTING_BOARD_KNIFE = SOUNDS.register("block.cutting_board.knife_cut",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "block.cutting_board.knife_cut")));

	// Rope Fence Gate
	public static final Supplier<SoundEvent> BLOCK_ROPE_FENCE_GATE_OPEN = SOUNDS.register("block.rope_fence_gate.open",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "block.rope_fence_gate.open")));
	public static final Supplier<SoundEvent> BLOCK_ROPE_FENCE_GATE_CLOSE = SOUNDS.register("block.rope_fence_gate.close",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "block.rope_fence_gate.close")));

	// Cabinet
	public static final Supplier<SoundEvent> BLOCK_CABINET_OPEN = SOUNDS.register("block.cabinet.open",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "block.cabinet.open")));
	public static final Supplier<SoundEvent> BLOCK_CABINET_CLOSE = SOUNDS.register("block.cabinet.close",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "block.cabinet.close")));

	// Skillet
	public static final Supplier<SoundEvent> BLOCK_SKILLET_SIZZLE = SOUNDS.register("block.skillet.sizzle",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "block.skillet.sizzle")));
	public static final Supplier<SoundEvent> BLOCK_SKILLET_ADD_FOOD = SOUNDS.register("block.skillet.add_food",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "block.skillet.add_food")));
	public static final Supplier<SoundEvent> ITEM_SKILLET_ATTACK_STRONG = SOUNDS.register("item.skillet.attack.strong",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "item.skillet.attack.strong")));
	public static final Supplier<SoundEvent> ITEM_SKILLET_ATTACK_WEAK = SOUNDS.register("item.skillet.attack.weak",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "item.skillet.attack.weak")));

	// Tomato Bush
	public static final Supplier<SoundEvent> BLOCK_TOMATOES_PICK_TOMATOES = SOUNDS.register("block.tomatoes.pick_tomatoes",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "block.tomatoes.pick_tomatoes")));

	// Food
	public static final Supplier<SoundEvent> BLOCK_FOOD_TAKE_PORTION = SOUNDS.register("block.food.take_portion",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "block.food.take_portion")));
	public static final Supplier<SoundEvent> BLOCK_FOOD_SLICE = SOUNDS.register("block.food.slice",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "block.food.slice")));

	public static final Supplier<SoundEvent> ENTITY_ROTTEN_TOMATO_THROW = SOUNDS.register("entity.rotten_tomato.throw",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "entity.rotten_tomato.throw")));
	public static final Supplier<SoundEvent> ENTITY_ROTTEN_TOMATO_HIT = SOUNDS.register("entity.rotten_tomato.hit",
			() -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "entity.rotten_tomato.hit")));
}
