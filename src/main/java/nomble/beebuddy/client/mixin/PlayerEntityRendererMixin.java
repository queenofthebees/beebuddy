package nomble.beebuddy.client.mixin;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.PlayerEntityRenderer;

import nomble.beebuddy.client.mixin.invoker.LivingEntityRendererInvoker;
import nomble.beebuddy.client.render.entity.feature.HeadFriendFeatureRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin{
    @Inject( method = "<init>(Lnet/minecraft/client/render/entity"
                    +            "/EntityRenderDispatcher;Z)V"
           , at = @At("TAIL"))
    public void addHeadFriendFeature(EntityRenderDispatcher e, boolean b
                                    , CallbackInfo cbi){
        PlayerEntityRenderer us = (PlayerEntityRenderer)(Object)this;
        LivingEntityRendererInvoker inv = (LivingEntityRendererInvoker)(Object)
                                          this;
        inv.beebuddy$addFeature(new HeadFriendFeatureRenderer(us));
    }
}
