package nomble.beebuddy.duck;

import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.nbt.NbtCompound;

public interface IFriendlyPlayer {
    NbtCompound beebuddy$getHeadFriendNbt();

    boolean beebuddy$hasHeadFriend();

    void beebuddy$setHeadFriendNbt(NbtCompound t);

    boolean beebuddy$makeHeadFriend(BeeEntity bee);
}
