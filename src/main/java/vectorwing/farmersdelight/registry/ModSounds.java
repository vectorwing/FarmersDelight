package vectorwing.farmersdelight.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;

public class ModSounds {
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, FarmersDelight.MODID);

	public static final RegistryObject<SoundEvent> BLOCK_COOKING_POT_BOIL = SOUNDS.register("block.cooking_pot.boil",
			() -> new SoundEvent(new ResourceLocation(FarmersDelight.MODID, "block.cooking_pot.boil")));
	public static final RegistryObject<SoundEvent> BLOCK_COOKING_POT_BOIL_SOUP = SOUNDS.register("block.cooking_pot.boil_soup",
			() -> new SoundEvent(new ResourceLocation(FarmersDelight.MODID, "block.cooking_pot.boil_soup")));
	public static final RegistryObject<SoundEvent> BLOCK_CUTTING_BOARD_KNIFE = SOUNDS.register("block.cutting_board.knife",
			() -> new SoundEvent(new ResourceLocation(FarmersDelight.MODID, "block.cutting_board.knife")));
	public static final RegistryObject<SoundEvent> BLOCK_STOVE_CRACKLE = SOUNDS.register("block.stove.crackle",
			() -> new SoundEvent(new ResourceLocation(FarmersDelight.MODID, "block.stove.crackle")));
	public static final RegistryObject<SoundEvent> ITEM_TOMATO_PICK_FROM_BUSH = SOUNDS.register("item.tomato.pick_from_bush",
			() -> new SoundEvent(new ResourceLocation(FarmersDelight.MODID, "item.tomato.pick_from_bush")));
}
