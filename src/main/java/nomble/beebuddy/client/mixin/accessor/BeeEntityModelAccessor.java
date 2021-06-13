package nomble.beebuddy.client.mixin.accessor;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BeeEntityModel;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(BeeEntityModel.class)
public interface BeeEntityModelAccessor{
    @Accessor("bodyPitch")
    public void beebuddy$setBodyPitch(float f);
    @Accessor("body")
    public ModelPart beebuddy$getBody();
}
