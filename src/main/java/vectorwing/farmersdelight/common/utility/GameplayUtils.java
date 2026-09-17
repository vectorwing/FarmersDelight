package vectorwing.farmersdelight.common.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelReader;
import vectorwing.farmersdelight.common.tag.ModTags;

public class GameplayUtils
{
	public static boolean isPlayerNearHeatSource(Player player, LevelReader level) {
		if (player.isOnFire()) {
			return true;
		}
		BlockPos pos = player.blockPosition();
		for (BlockPos nearbyPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
			if (level.getBlockState(nearbyPos).is(ModTags.Blocks.HEAT_SOURCES)) {
				return true;
			}
		}
		return false;
	}
}
