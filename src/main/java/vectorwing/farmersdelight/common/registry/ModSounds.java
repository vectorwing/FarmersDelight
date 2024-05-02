package vectorwing.farmersdelight.common.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;

import java.util.function.Supplier;

public class ModSounds
{
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, FarmersDelight.MODID);

	// Stove
	public static final Supplier<SoundEvent> BLOCK_STOVE_CRACKLE = SOUNDS.register("block.stove.crackle",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FarmersDelight.MODID, "block.stove.crackle")));

	// Cooking Pot
	public static final Supplier<SoundEvent> BLOCK_COOKING_POT_BOIL = SOUNDS.register("block.cooking_pot.boil",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FarmersDelight.MODID, "block.cooking_pot.boil")));
	public static final Supplier<SoundEvent> BLOCK_COOKING_POT_BOIL_SOUP = SOUNDS.register("block.cooking_pot.boil_soup",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FarmersDelight.MODID, "block.cooking_pot.boil_soup")));

	// Cutting Board
	public static final Supplier<SoundEvent> BLOCK_CUTTING_BOARD_PLACE = SOUNDS.register("block.cutting_board.place",
			() -> new SoundEvent(new ResourceLocation(FarmersDelight.MODID, "block.cutting_board.place")));
	public static final Supplier<SoundEvent> BLOCK_CUTTING_BOARD_REMOVE = SOUNDS.register("block.cutting_board.remove",
			() -> new SoundEvent(new ResourceLocation(FarmersDelight.MODID, "block.cutting_board.remove")));
	public static final Supplier<SoundEvent> BLOCK_CUTTING_BOARD_CARVE = SOUNDS.register("block.cutting_board.carve",
			() -> new SoundEvent(new ResourceLocation(FarmersDelight.MODID, "block.cutting_board.carve")));
	public static final Supplier<SoundEvent> BLOCK_CUTTING_BOARD_KNIFE = SOUNDS.register("block.cutting_board.knife",
			() -> new SoundEvent(new ResourceLocation(FarmersDelight.MODID, "block.cutting_board.knife")));

	// Cabinet
	public static final Supplier<SoundEvent> BLOCK_CABINET_OPEN = SOUNDS.register("block.cabinet.open",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FarmersDelight.MODID, "block.cabinet.open")));
	public static final Supplier<SoundEvent> BLOCK_CABINET_CLOSE = SOUNDS.register("block.cabinet.close",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FarmersDelight.MODID, "block.cabinet.close")));

	// Skillet
	public static final Supplier<SoundEvent> BLOCK_SKILLET_SIZZLE = SOUNDS.register("block.skillet.sizzle",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FarmersDelight.MODID, "block.skillet.sizzle")));
	public static final Supplier<SoundEvent> BLOCK_SKILLET_ADD_FOOD = SOUNDS.register("block.skillet.add_food",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FarmersDelight.MODID, "block.skillet.add_food")));
	public static final Supplier<SoundEvent> ITEM_SKILLET_ATTACK_STRONG = SOUNDS.register("item.skillet.attack.strong",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FarmersDelight.MODID, "item.skillet.attack.strong")));
	public static final Supplier<SoundEvent> ITEM_SKILLET_ATTACK_WEAK = SOUNDS.register("item.skillet.attack.weak",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FarmersDelight.MODID, "item.skillet.attack.weak")));

	// Tomato Bush
	public static final Supplier<SoundEvent> ITEM_TOMATO_PICK_FROM_BUSH = SOUNDS.register("block.tomato_bush.pick_tomatoes",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FarmersDelight.MODID, "block.tomato_bush.pick_tomatoes")));
	
	public static final Supplier<SoundEvent> ENTITY_ROTTEN_TOMATO_THROW = SOUNDS.register("entity.rotten_tomato.throw",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FarmersDelight.MODID, "entity.rotten_tomato.throw")));
	public static final Supplier<SoundEvent> ENTITY_ROTTEN_TOMATO_HIT = SOUNDS.register("entity.rotten_tomato.hit",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FarmersDelight.MODID, "entity.rotten_tomato.hit")));
}
