package nomble.beebuddy.client.mixin.invoker;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntityRenderer.class)
public interface LivingEntityRendererInvoker{
    @Invoker("addFeature")
    public boolean beebuddy$addFeature(FeatureRenderer<?, ?> f);
}
