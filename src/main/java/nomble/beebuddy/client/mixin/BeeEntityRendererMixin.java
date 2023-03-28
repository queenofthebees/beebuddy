package nomble.beebuddy.client.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.BeeEntityRenderer;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.Identifier;
import nomble.beebuddy.duck.IFriendlyBee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(BeeEntityRenderer.class)
public abstract class BeeEntityRendererMixin {
    @Inject(method = "getTexture", at = @At("HEAD"), cancellable = true)
    private void getPrideTexture(BeeEntity bee
            , CallbackInfoReturnable<Identifier> cbir) {
        String type = ((IFriendlyBee) bee).beebuddy$getNectarType();
        if (!type.equals("default")) {
            String texture = "textures/entity/bee/" + type + "_bee";
            texture += bee.hasAngerTime() ? "_angry" : "";
            texture += bee.hasNectar() ? "_nectar" : "";
            cbir.setReturnValue(new Identifier("beebuddy", texture + ".png"));
        }
    }
}
