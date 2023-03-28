package nomble.beebuddy.mixin.invoker;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityInvoker {
    @Invoker("getSavedEntityId")
    String beebuddy$getSavedEntityId();
}
