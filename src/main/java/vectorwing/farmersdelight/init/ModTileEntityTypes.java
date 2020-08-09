package vectorwing.farmersdelight.init;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.blocks.BasketTileEntity;
import vectorwing.farmersdelight.blocks.CookingPotTileEntity;
import vectorwing.farmersdelight.blocks.StoveTileEntity;

public class ModTileEntityTypes {
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, FarmersDelight.MODID);

    public static final RegistryObject<TileEntityType<StoveTileEntity>> STOVE_TILE = TILES.register("stove",
            () -> TileEntityType.Builder.create(StoveTileEntity::new, ModBlocks.STOVE.get()).build(null));
    public static final RegistryObject<TileEntityType<CookingPotTileEntity>> COOKING_POT_TILE = TILES.register("cooking_pot",
            () -> TileEntityType.Builder.create(CookingPotTileEntity::new, ModBlocks.COOKING_POT.get()).build(null));
    public static final RegistryObject<TileEntityType<BasketTileEntity>> BASKET_TILE = TILES.register("basket",
            () -> TileEntityType.Builder.create(BasketTileEntity::new, ModBlocks.BASKET.get()).build(null));
}
