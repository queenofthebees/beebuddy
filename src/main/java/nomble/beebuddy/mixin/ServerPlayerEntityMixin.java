package nomble.beebuddy.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import nomble.beebuddy.duck.IFriendlyPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntityMixin {
    public ServerPlayerEntityMixin(EntityType<? extends LivingEntity> t
            , World w) {
        super(t, w);
    }


    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void copyFriend(ServerPlayerEntity o, boolean a, CallbackInfo cbi) {
        IFriendlyPlayer old = (IFriendlyPlayer) o;
        old.beebuddy$setHeadFriendNbt(this.beebuddy$getHeadFriendNbt());
    }
}
