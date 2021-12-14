package vectorwing.farmersdelight.common.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;

public class ModSounds
{
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, FarmersDelight.MODID);

	// Stove
	public static final RegistryObject<SoundEvent> BLOCK_STOVE_CRACKLE = SOUNDS.register("block.stove.crackle",
			() -> new SoundEvent(new ResourceLocation(FarmersDelight.MODID, "block.stove.crackle")));

	// Cooking Pot
	public static final RegistryObject<SoundEvent> BLOCK_COOKING_POT_BOIL = SOUNDS.register("block.cooking_pot.boil",
			() -> new SoundEvent(new ResourceLocation(FarmersDelight.MODID, "block.cooking_pot.boil")));
	public static final RegistryObject<SoundEvent> BLOCK_COOKING_POT_BOIL_SOUP = SOUNDS.register("block.cooking_pot.boil_soup",
			() -> new SoundEvent(new ResourceLocation(FarmersDelight.MODID, "block.cooking_pot.boil_soup")));

	// Cutting Board
	public static final RegistryObject<SoundEvent> BLOCK_CUTTING_BOARD_KNIFE = SOUNDS.register("block.cutting_board.knife",
			() -> new SoundEvent(new ResourceLocation(FarmersDelight.MODID, "block.cutting_board.knife")));

	// Skillet
	public static final RegistryObject<SoundEvent> BLOCK_SKILLET_SIZZLE = SOUNDS.register("block.skillet.sizzle",
			() -> new SoundEvent(new ResourceLocation(FarmersDelight.MODID, "block.skillet.sizzle")));
	public static final RegistryObject<SoundEvent> BLOCK_SKILLET_ADD_FOOD = SOUNDS.register("block.skillet.add_food",
			() -> new SoundEvent(new ResourceLocation(FarmersDelight.MODID, "block.skillet.add_food")));
	public static final RegistryObject<SoundEvent> ITEM_SKILLET_ATTACK_STRONG = SOUNDS.register("item.skillet.attack.strong",
			() -> new SoundEvent(new ResourceLocation(FarmersDelight.MODID, "item.skillet.attack.strong")));
	public static final RegistryObject<SoundEvent> ITEM_SKILLET_ATTACK_WEAK = SOUNDS.register("item.skillet.attack.weak",
			() -> new SoundEvent(new ResourceLocation(FarmersDelight.MODID, "item.skillet.attack.weak")));

	// Tomato Bush
	public static final RegistryObject<SoundEvent> ITEM_TOMATO_PICK_FROM_BUSH = SOUNDS.register("block.tomato_bush.pick_tomatoes",
			() -> new SoundEvent(new ResourceLocation(FarmersDelight.MODID, "block.tomato_bush.pick_tomatoes")));
}
