package nomble.beebuddy.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(BeeEntity.class)
public abstract class BeeEntityMixin extends AnimalEntityMixin {
    public BeeEntityMixin(EntityType<? extends PassiveEntity> type
            , World world) {
        super(type, world);
    }


    @Override
    protected void tameEffect(byte status, CallbackInfo cbi) {
        if (status == 6 || status == 7) {
            ParticleEffect p = status == 7 ? ParticleTypes.HEART
                    : ParticleTypes.SMOKE;

            for (int i = 0; i < 7; i++) {
                double d = this.random.nextGaussian() * 0.02;
                double e = this.random.nextGaussian() * 0.02;
                double f = this.random.nextGaussian() * 0.02;
                this.world.addParticle(p, this.getParticleX(1.0D)
                        , this.getRandomBodyY() + 0.5D
                        , this.getParticleZ(1.0D)
                        , d, e, f);
            }
        }
    }
}
