package nomble.beebuddy.duck;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.entity.player.PlayerEntity;

public interface IFriendlyBee{
    public void callSetHasNectar(boolean hasNectar);

    public Optional<UUID> beebuddy$getFriend();
    public Optional<PlayerEntity> beebuddy$getFriendPlayer();
    public void beebuddy$setFriend(Optional<UUID> friend);
    public boolean beebuddy$isSitting();
    public boolean beebuddy$getPollinateGoalRunning();
    public boolean beebuddy$getMoveToFlowerGoalRunning();
    public String beebuddy$getNectarType();
    public void beebuddy$setNectarType(String type);
    public void beebuddy$friendify();
}
