package vectorwing.farmersdelight.init;

import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.blocks.CookingPotTileEntity;
import vectorwing.farmersdelight.blocks.StoveTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityInit
{
	public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, FarmersDelight.MODID);

	public static final RegistryObject<TileEntityType<StoveTileEntity>> STOVE_TILE = TILES.register("stove",
			() -> TileEntityType.Builder.create(StoveTileEntity::new, BlockInit.STOVE.get()).build(null));
	public static final RegistryObject<TileEntityType<CookingPotTileEntity>> COOKING_POT_TILE = TILES.register("cooking_pot",
			() -> TileEntityType.Builder.create(CookingPotTileEntity::new, BlockInit.COOKING_POT.get()).build(null));
}
