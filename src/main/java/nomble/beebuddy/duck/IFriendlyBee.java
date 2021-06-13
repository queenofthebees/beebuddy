package nomble.beebuddy.duck;

import java.util.Optional;

import net.minecraft.entity.player.PlayerEntity;

public interface IFriendlyBee{
    public Optional<PlayerEntity> beebuddy$getFriendPlayer();
    public boolean beebuddy$isSitting();
    public boolean beebuddy$getPollinateGoalRunning();
    public boolean beebuddy$getMoveToFlowerGoalRunning();
    public String beebuddy$getNectarType();
    public void callSetHasNectar(boolean hasNectar);
}
