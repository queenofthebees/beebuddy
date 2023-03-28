package nomble.beebuddy.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin extends PassiveEntity {
    public AnimalEntityMixin(EntityType<? extends PassiveEntity> type
            , World world) {
        super(type, world);
    }


    @Inject(method = "handleStatus", at = @At(value = "HEAD"))
    protected void tameEffect(byte status, CallbackInfo cbi) {
    }
}
