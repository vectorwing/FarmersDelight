package vectorwing.farmersdelight.common.event;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.world.WildCropGeneration;

@Mod.EventBusSubscriber(modid = FarmersDelight.MODID)
public class CommonEvents
{
	@SubscribeEvent
	public static void onBiomeLoad(BiomeLoadingEvent event) {
		BiomeGenerationSettingsBuilder builder = event.getGeneration();
		Biome.ClimateSettings climate = event.getClimate();

		ResourceLocation biomeName = event.getName();
		if (biomeName != null && biomeName.getPath().equals("beach")) {
			if (Configuration.GENERATE_WILD_BEETROOTS.get()) {
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WildCropGeneration.PATCH_WILD_BEETROOTS);
			}
			if (Configuration.GENERATE_WILD_CABBAGES.get()) {
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WildCropGeneration.PATCH_WILD_CABBAGES);
			}
		}

		if (event.getCategory().equals(Biome.BiomeCategory.SWAMP) || event.getCategory().equals(Biome.BiomeCategory.JUNGLE)) {
			if (Configuration.GENERATE_WILD_RICE.get()) {
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WildCropGeneration.PATCH_WILD_RICE);
			}
		}

		if (climate.temperature >= 1.0F) {
			if (Configuration.GENERATE_WILD_TOMATOES.get()) {
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WildCropGeneration.PATCH_WILD_TOMATOES);
			}
		}

		if (climate.temperature > 0.3F && climate.temperature < 1.0F) {
			if (Configuration.GENERATE_WILD_CARROTS.get()) {
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WildCropGeneration.PATCH_WILD_CARROTS);
			}
			if (Configuration.GENERATE_WILD_ONIONS.get()) {
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WildCropGeneration.PATCH_WILD_ONIONS);
			}
		}

		if (climate.temperature > 0.0F && climate.temperature <= 0.3F) {
			if (Configuration.GENERATE_WILD_POTATOES.get()) {
				builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WildCropGeneration.PATCH_WILD_POTATOES);
			}
		}
	}

	@SubscribeEvent
	public static void handleVanillaSoupEffects(LivingEntityUseItemEvent.Finish event) {
		Item food = event.getItem().getItem();
		LivingEntity entity = event.getEntityLiving();

		if (Configuration.RABBIT_STEW_JUMP_BOOST.get() && food.equals(Items.RABBIT_STEW)) {
			entity.addEffect(new MobEffectInstance(MobEffects.JUMP, 200, 1));
		}

		if (Configuration.VANILLA_SOUP_EXTRA_EFFECTS.get()) {
			FoodProperties soupEffects = FoodValues.VANILLA_SOUP_EFFECTS.get(food);

			if (soupEffects != null) {
				for (Pair<MobEffectInstance, Float> pair : soupEffects.getEffects()) {
					entity.addEffect(pair.getFirst());
				}
			}
		}
	}

	// TODO: Remember to substitute this with an adequate hoe tool check in the future, if possible.
	@SubscribeEvent
	public static void onHoeUse(UseHoeEvent event) {
		UseOnContext context = event.getContext();
		BlockPos pos = context.getClickedPos();
		Level world = context.getLevel();
		BlockState state = world.getBlockState(pos);

		if (context.getClickedFace() != Direction.DOWN && world.getBlockState(pos.above()).isAir() && state.getBlock() == ModBlocks.RICH_SOIL.get()) {
			world.playSound(event.getPlayer(), pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
			world.setBlock(pos, ModBlocks.RICH_SOIL_FARMLAND.get().defaultBlockState(), 11);
			event.setResult(Event.Result.ALLOW);
		}
	}
}
