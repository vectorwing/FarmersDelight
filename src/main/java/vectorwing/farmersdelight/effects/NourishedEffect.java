package vectorwing.farmersdelight.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.FoodStats;
import net.minecraft.world.GameRules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NourishedEffect extends Effect {
    private static final Logger LOGGER = LogManager.getLogger();

    public NourishedEffect() {
        super(EffectType.BENEFICIAL, 0);
    }

    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        if (!entityLivingBaseIn.getEntityWorld().isRemote && entityLivingBaseIn instanceof PlayerEntity) {
            PlayerEntity player = ((PlayerEntity) entityLivingBaseIn);
            FoodStats foodStats = player.getFoodStats();
            boolean isPlayerHealingWithSaturation =
                    player.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION)
                            && player.shouldHeal()
                            && foodStats.getSaturationLevel() > 0.0F
                            && foodStats.getFoodLevel() >= 20;
            if (!isPlayerHealingWithSaturation) {
                float exhaustion = player.getFoodStats().foodExhaustionLevel;
                if (exhaustion > 0.1F) {
                    player.addExhaustion(-0.1F);
                }
            }
        }
    }

    public boolean isReady(int duration, int amplifier) {
        return true;
    }

}
