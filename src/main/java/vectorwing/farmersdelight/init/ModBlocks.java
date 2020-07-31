package vectorwing.farmersdelight.init;

import net.minecraft.block.BushBlock;
import net.minecraft.block.HayBlock;
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
	public static final RegistryObject<Block> RICE_BALE = BLOCKS.register("rice_bale",
			() -> new RiceBaleBlock(Block.Properties.from(Blocks.HAY_BLOCK)));
	public static final RegistryObject<Block> ORGANIC_COMPOST = BLOCKS.register("organic_compost",
			() -> new OrganicCompostBlock(Block.Properties.from(Blocks.DIRT)));
	public static final RegistryObject<Block> MULCH = BLOCKS.register("mulch",
			() -> new MulchBlock(Block.Properties.from(Blocks.DIRT)));
	public static final RegistryObject<Block> MULCH_FARMLAND = BLOCKS.register("mulch_farmland",
			() -> new MulchFarmlandBlock(Block.Properties.from(Blocks.FARMLAND)));

	// WILD PATCHES
	public static final RegistryObject<Block> WILD_CABBAGES = BLOCKS.register("wild_cabbages",
			() -> new WildPatchBlock(Block.Properties.from(Blocks.TALL_GRASS)));
	public static final RegistryObject<Block> WILD_ONIONS = BLOCKS.register("wild_onions",
			() -> new WildPatchBlock(Block.Properties.from(Blocks.TALL_GRASS)));
	public static final RegistryObject<Block> WILD_TOMATOES = BLOCKS.register("wild_tomatoes",
			() -> new WildPatchBlock(Block.Properties.from(Blocks.TALL_GRASS)));
	public static final RegistryObject<Block> WILD_CARROTS = BLOCKS.register("wild_carrots",
			() -> new WildPatchBlock(Block.Properties.from(Blocks.TALL_GRASS)));
	public static final RegistryObject<Block> WILD_POTATOES = BLOCKS.register("wild_potatoes",
			() -> new WildPatchBlock(Block.Properties.from(Blocks.TALL_GRASS)));
	public static final RegistryObject<Block> WILD_BEETROOTS = BLOCKS.register("wild_beetroots",
			() -> new WildPatchBlock(Block.Properties.from(Blocks.TALL_GRASS)));

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
