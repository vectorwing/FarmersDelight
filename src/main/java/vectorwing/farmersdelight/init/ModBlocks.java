package vectorwing.farmersdelight.init;

import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.blocks.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FarmersDelight.MODID);

	// FUNCTIONAL
	public static final RegistryObject<Block> STOVE = BLOCKS.register("stove", StoveBlock::new);
	public static final RegistryObject<Block> COOKING_POT = BLOCKS.register("cooking_pot", CookingPotBlock::new);
	public static final RegistryObject<Block> BASKET = BLOCKS.register("basket", BasketBlock::new);
	public static final RegistryObject<Block> ROPE = BLOCKS.register("rope", RopeBlock::new);
	public static final RegistryObject<Block> SAFETY_NET = BLOCKS.register("safety_net", SafetyNetBlock::new);

	// CROPS
	public static final RegistryObject<Block> CABBAGE_CROP = BLOCKS.register("cabbages",
			() -> new CabbagesBlock(Block.Properties.from(Blocks.WHEAT)));
	public static final RegistryObject<Block> ONION_CROP = BLOCKS.register("onions",
			() -> new OnionsBlock(Block.Properties.from(Blocks.WHEAT)));
	public static final RegistryObject<Block> TOMATO_CROP = BLOCKS.register("tomatoes",
			() -> new TomatoesBlock(Block.Properties.from(Blocks.WHEAT)));
	public static final RegistryObject<Block> RICE_CROP = BLOCKS.register("rice_crop",
			() -> new RiceCropBlock(Block.Properties.from(Blocks.WHEAT)));
	public static final RegistryObject<Block> TALL_RICE_CROP = BLOCKS.register("tall_rice_crop",
			() -> new TallRiceCropBlock(Block.Properties.from(Blocks.WHEAT)));
}
