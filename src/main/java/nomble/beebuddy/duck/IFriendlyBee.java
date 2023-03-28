package nomble.beebuddy.duck;

import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;
import java.util.UUID;

public interface IFriendlyBee {
    void callSetHasNectar(boolean hasNectar);

    Optional<UUID> beebuddy$getFriend();

    Optional<PlayerEntity> beebuddy$getFriendPlayer();

    void beebuddy$setFriend(Optional<UUID> friend);

    boolean beebuddy$isSitting();

    boolean beebuddy$getPollinateGoalRunning();

    boolean beebuddy$getMoveToFlowerGoalRunning();

    String beebuddy$getNectarType();

    void beebuddy$setNectarType(String type);

    void beebuddy$friendify();
}
