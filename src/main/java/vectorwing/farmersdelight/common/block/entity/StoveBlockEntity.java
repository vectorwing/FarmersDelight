package vectorwing.farmersdelight.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

public class StoveBlockEntity extends AbstractStoveBlockEntity<CampfireCookingRecipe, RecipeType<CampfireCookingRecipe>> {
	public StoveBlockEntity(BlockPos pos, BlockState state) {
		super(
				ModBlockEntityTypes.STOVE.get(),
				6,
				RecipeType.CAMPFIRE_COOKING,
				pos,
				state,
				new Vec2[]{
						new Vec2(0.3F, 0.2F),
						new Vec2(0.0F, 0.2F),
						new Vec2(-0.3F, 0.2F),
						new Vec2(0.3F, -0.2F),
						new Vec2(0.0F, -0.2F),
						new Vec2(-0.3F, -0.2F)
				}
		);
	}
}

