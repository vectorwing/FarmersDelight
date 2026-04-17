package vectorwing.farmersdelight.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.util.function.Supplier;

public class SoundDefinitions extends SoundDefinitionsProvider
{
	protected SoundDefinitions(PackOutput output, ExistingFileHelper helper) {
		super(output, FarmersDelight.MODID, helper);
	}

	@Override
	public void registerSounds() {
		this.generateNewSoundWithSubtitle(ModSounds.BLOCK_COOKING_POT_BOIL, "block/cooking_pot/boil_water", 2);
		this.generateNewSoundCustomSubtitle(ModSounds.BLOCK_COOKING_POT_BOIL_SOUP, "block/cooking_pot/boil_soup", 3, "subtitles.farmersdelight.block.cooking_pot.boil");
		this.generateNewSoundWithSubtitle(ModSounds.BLOCK_CUTTING_BOARD_KNIFE, "block/cutting_board/knife", 2);
		this.generateNewSoundWithSubtitle(ModSounds.BLOCK_SKILLET_ADD_FOOD, "block/skillet/add_food", 2);
		this.generateNewSound(ModSounds.ITEM_SKILLET_ATTACK_STRONG, "block/skillet/attack_strong", 1, false);
		this.generateNewSound(ModSounds.ITEM_SKILLET_ATTACK_WEAK, "block/skillet/attack_weak", 1, false);
		this.generateNewSoundWithSubtitle(ModSounds.BLOCK_SKILLET_SIZZLE, "block/skillet/sizzle", 3);

		this.generateExistingSoundWithSubtitle(ModSounds.BLOCK_CUTTING_BOARD_CARVE, SoundEvents.WOOD_PLACE);
		this.generateExistingSoundWithSubtitle(ModSounds.BLOCK_CUTTING_BOARD_PLACE, SoundEvents.WOOD_PLACE);
		this.generateExistingSoundWithSubtitle(ModSounds.BLOCK_CUTTING_BOARD_REMOVE, SoundEvents.WOOD_HIT);
		this.generateExistingSoundWithSubtitle(ModSounds.BLOCK_STOVE_CRACKLE, SoundEvents.CAMPFIRE_CRACKLE);
		this.generateExistingSoundWithSubtitle(ModSounds.BLOCK_ROPE_FENCE_GATE_CLOSE, SoundEvents.LEASH_KNOT_PLACE);
		this.generateExistingSoundWithSubtitle(ModSounds.BLOCK_ROPE_FENCE_GATE_OPEN, SoundEvents.LEASH_KNOT_BREAK);
		this.generateExistingSoundWithSubtitle(ModSounds.BLOCK_CABINET_CLOSE, SoundEvents.BARREL_CLOSE);
		this.generateExistingSoundWithSubtitle(ModSounds.BLOCK_CABINET_OPEN, SoundEvents.BARREL_OPEN);
		this.generateExistingSoundWithSubtitle(ModSounds.BLOCK_TOMATOES_PICK_TOMATOES, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES);
		this.generateExistingSoundWithSubtitle(ModSounds.BLOCK_FOOD_TAKE_PORTION, SoundEvents.ARMOR_EQUIP_GENERIC.value());
		this.generateExistingSoundWithSubtitle(ModSounds.BLOCK_FOOD_SLICE, SoundEvents.WOOL_BREAK);
		this.generateExistingSoundWithSubtitle(ModSounds.ENTITY_ROTTEN_TOMATO_THROW, SoundEvents.SNOWBALL_THROW);
		this.generateExistingSoundWithSubtitle(ModSounds.ENTITY_ROTTEN_TOMATO_HIT, SoundEvents.SLIME_ATTACK);
	}

	public void generateNewSoundWithSubtitle(Supplier<SoundEvent> event, String baseSoundDirectory, int numberOfSounds) {
		generateNewSound(event, baseSoundDirectory, numberOfSounds, true);
	}

	public void generateNewSound(Supplier<SoundEvent> event, String baseSoundDirectory, int numberOfSounds, boolean subtitle) {
		String formattedSub = subtitle ? TextUtils.subtitleKey(event.get().getLocation().getPath()) : null;
		this.generateNewSoundCustomSubtitle(event, baseSoundDirectory, numberOfSounds, formattedSub);
	}

	public void generateNewSoundCustomSubtitle(Supplier<SoundEvent> event, String baseSoundDirectory, int numberOfSounds, @Nullable String subtitle) {
		SoundDefinition definition = SoundDefinition.definition();
		if (subtitle != null) {
			definition.subtitle(subtitle);
		}
		for (int i = 1; i <= numberOfSounds; i++) {
			definition.with(SoundDefinition.Sound.sound(ResourceLocation.fromNamespaceAndPath(FarmersDelight.MODID, baseSoundDirectory + (numberOfSounds > 1 ? i : "")), SoundDefinition.SoundType.SOUND));
		}
		this.add(event, definition);
	}

	public void generateExistingSoundWithSubtitle(Supplier<SoundEvent> event, SoundEvent referencedSound) {
		this.generateExistingSound(event, referencedSound, true);
	}

	public void generateExistingSound(Supplier<SoundEvent> event, SoundEvent referencedSound, boolean subtitle) {
		SoundDefinition definition = SoundDefinition.definition();
		if (subtitle) {
			definition.subtitle(TextUtils.subtitleKey(event.get().getLocation().getPath()));
		}
		this.add(event, definition
				.with(SoundDefinition.Sound.sound(referencedSound.getLocation(), SoundDefinition.SoundType.EVENT)));
	}
}
