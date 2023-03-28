package nomble.beebuddy.client.mixin.accessor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BeeEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(BeeEntityModel.class)
public interface BeeEntityModelAccessor {
    @Accessor("bodyPitch")
    void beebuddy$setBodyPitch(float f);

    @Accessor("bone")
    ModelPart beebuddy$getBone();
}
