package vectorwing.farmersdelight.common;

import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;
import vectorwing.farmersdelight.common.entity.RottenTomatoEntity;
import vectorwing.farmersdelight.common.registry.ModAdvancements;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Arrays;
import java.util.Collections;

public class CommonSetup
{
	public static void init(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			registerCompostables();
			registerDispenserBehaviors();
			registerAnimalFeeds();
			registerStackSizeOverrides();
		});
	}

	public static void registerStackSizeOverrides() {
		if (!Configuration.ENABLE_STACKABLE_SOUP_ITEMS.get()) return;

		Configuration.SOUP_ITEM_LIST.get().forEach((key) -> {
			Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(key));
			if (item instanceof BowlFoodItem) {
				ObfuscationReflectionHelper.setPrivateValue(Item.class, item, 16, "maxStackSize");
			}
		});
	}

	public static void registerDispenserBehaviors() {
		DispenserBlock.registerBehavior(ModItems.ROTTEN_TOMATO.get(), new AbstractProjectileDispenseBehavior()
		{
			@Override
			protected Projectile getProjectile(Level pLevel, Position pPosition, ItemStack pStack) {
				return new RottenTomatoEntity(pLevel, pPosition.x(), pPosition.y(), pPosition.z());
			}
		});
	}

	public static void registerCompostables() {
		// 30% chance
		ComposterBlock.COMPOSTABLES.put(ModItems.TREE_BARK.get(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ModItems.STRAW.get(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ModItems.CABBAGE_SEEDS.get(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ModItems.TOMATO_SEEDS.get(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ModItems.RICE.get(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ModItems.RICE_PANICLE.get(), 0.3F);
		ComposterBlock.COMPOSTABLES.put(ModItems.SANDY_SHRUB.get(), 0.3F);

		// 50% chance
		ComposterBlock.COMPOSTABLES.put(ModItems.PUMPKIN_SLICE.get(), 0.5F);
		ComposterBlock.COMPOSTABLES.put(ModItems.CABBAGE_LEAF.get(), 0.5F);
		ComposterBlock.COMPOSTABLES.put(ModItems.KELP_ROLL_SLICE.get(), 0.5F);

		// 65% chance
		ComposterBlock.COMPOSTABLES.put(ModItems.CABBAGE.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModItems.ONION.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModItems.TOMATO.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModItems.WILD_CABBAGES.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModItems.WILD_ONIONS.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModItems.WILD_TOMATOES.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModItems.WILD_CARROTS.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModItems.WILD_POTATOES.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModItems.WILD_BEETROOTS.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModItems.WILD_RICE.get(), 0.65F);
		ComposterBlock.COMPOSTABLES.put(ModItems.PIE_CRUST.get(), 0.65F);

		// 85% chance
		ComposterBlock.COMPOSTABLES.put(ModItems.RICE_BALE.get(), 0.85F);
		ComposterBlock.COMPOSTABLES.put(ModItems.SWEET_BERRY_COOKIE.get(), 0.85F);
		ComposterBlock.COMPOSTABLES.put(ModItems.HONEY_COOKIE.get(), 0.85F);
		ComposterBlock.COMPOSTABLES.put(ModItems.CAKE_SLICE.get(), 0.85F);
		ComposterBlock.COMPOSTABLES.put(ModItems.APPLE_PIE_SLICE.get(), 0.85F);
		ComposterBlock.COMPOSTABLES.put(ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get(), 0.85F);
		ComposterBlock.COMPOSTABLES.put(ModItems.CHOCOLATE_PIE_SLICE.get(), 0.85F);
		ComposterBlock.COMPOSTABLES.put(ModItems.RAW_PASTA.get(), 0.85F);
		ComposterBlock.COMPOSTABLES.put(ModItems.ROTTEN_TOMATO.get(), 0.85F);
		ComposterBlock.COMPOSTABLES.put(ModItems.KELP_ROLL.get(), 0.85F);

		// 100% chance
		ComposterBlock.COMPOSTABLES.put(ModItems.APPLE_PIE.get(), 1.0F);
		ComposterBlock.COMPOSTABLES.put(ModItems.SWEET_BERRY_CHEESECAKE.get(), 1.0F);
		ComposterBlock.COMPOSTABLES.put(ModItems.CHOCOLATE_PIE.get(), 1.0F);
		ComposterBlock.COMPOSTABLES.put(ModItems.DUMPLINGS.get(), 1.0F);
		ComposterBlock.COMPOSTABLES.put(ModItems.STUFFED_PUMPKIN_BLOCK.get(), 1.0F);
		ComposterBlock.COMPOSTABLES.put(ModItems.BROWN_MUSHROOM_COLONY.get(), 1.0F);
		ComposterBlock.COMPOSTABLES.put(ModItems.RED_MUSHROOM_COLONY.get(), 1.0F);
	}

	public static void registerAnimalFeeds() {
		Ingredient newChickenFood = Ingredient.of(ModItems.CABBAGE_SEEDS.get(), ModItems.TOMATO_SEEDS.get(), ModItems.RICE.get());
		Chicken.FOOD_ITEMS = new CompoundIngredient(Arrays.asList(Chicken.FOOD_ITEMS, newChickenFood))
		{
		};

		Ingredient newPigFood = Ingredient.of(ModItems.CABBAGE.get(), ModItems.TOMATO.get());
		Pig.FOOD_ITEMS = new CompoundIngredient(Arrays.asList(Pig.FOOD_ITEMS, newPigFood))
		{
		};

		Collections.addAll(Parrot.TAME_FOOD, ModItems.CABBAGE_SEEDS.get(), ModItems.TOMATO_SEEDS.get(), ModItems.RICE.get());
	}
}
