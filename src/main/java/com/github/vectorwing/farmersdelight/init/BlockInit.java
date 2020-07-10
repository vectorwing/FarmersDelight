package com.github.vectorwing.farmersdelight.init;

import com.github.vectorwing.farmersdelight.FarmersDelight;
import com.github.vectorwing.farmersdelight.blocks.OnionCropBlock;
import com.github.vectorwing.farmersdelight.blocks.StoveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit
{
	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, FarmersDelight.MODID);

	public static final RegistryObject<Block> STOVE = BLOCKS.register("stove",
			() -> new StoveBlock(Block.Properties.create(Material.ROCK)
					.hardnessAndResistance(0.5f, 15.0f).sound(SoundType.STONE)
					.lightValue(13)));

	public static final RegistryObject<Block> ONION_CROP = BLOCKS.register("onions",
			() -> new OnionCropBlock(Block.Properties.from(Blocks.WHEAT)));
}
