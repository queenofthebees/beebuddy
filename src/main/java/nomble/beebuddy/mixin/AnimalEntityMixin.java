package nomble.beebuddy.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin extends PassiveEntity{
    public AnimalEntityMixin( EntityType<? extends PassiveEntity> type
                            , World world){
        super(type, world);
    }



    @Inject(method = "interactMob", at = @At("TAIL"), cancellable = true)
    protected void tame( PlayerEntity player, Hand hand
                       , CallbackInfoReturnable<ActionResult> cbir){}

    @Inject(method = "lovePlayer", at = @At("TAIL"))
    protected void doAceReproduction(PlayerEntity player, CallbackInfo cbi){}
}
