package com.github.vectorwing.farmersdelight.init;

import com.github.vectorwing.farmersdelight.FarmersDelight;
import com.github.vectorwing.farmersdelight.blocks.StoveBlockTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityInit
{
	public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, FarmersDelight.MODID);

	public static final RegistryObject<TileEntityType<StoveBlockTile>> STOVE_TILE = TILES.register("stove",
			() -> TileEntityType.Builder.create(StoveBlockTile::new, BlockInit.STOVE.get()).build(null));
}
