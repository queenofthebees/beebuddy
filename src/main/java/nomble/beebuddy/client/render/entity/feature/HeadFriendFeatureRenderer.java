package nomble.beebuddy.client.render.entity.feature;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BeeEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import nomble.beebuddy.client.mixin.accessor.BeeEntityModelAccessor;
import nomble.beebuddy.duck.IFriendlyPlayer;

@Environment(EnvType.CLIENT)
public class HeadFriendFeatureRenderer<T extends PlayerEntity>
             extends FeatureRenderer<T, PlayerEntityModel<T>>{
    private final BeeEntityModel<FakeBeeEntity> bee;
    private FakeBeeEntity fake;
    private CompoundTag nbtCache = null;

    public HeadFriendFeatureRenderer(FeatureRendererContext
                                         <T, PlayerEntityModel<T>> frc){
        super(frc);
        bee = new BeeEntityModel();
        ((BeeEntityModelAccessor)(Object)bee).beebuddy$setBodyPitch(0F);
    }

    @Override
    public void render( MatrixStack matrices, VertexConsumerProvider vcp
                      , int light, T player, float limbAngle
                      , float limbDistance, float tickDelta
                      , float animationProgress, float headYaw
                      , float headPitch){
        IFriendlyPlayer friend = (IFriendlyPlayer)(Object)player;
        if(friend.beebuddy$hasHeadFriend()){
            CompoundTag t = friend.beebuddy$getHeadFriendNbt();

            if(fake == null){
                fake = new FakeBeeEntity(player.world);
            }

            Identifier tex = null;
            if(t.contains("BeeBuddyNectar")){
                String type = t.getString("BeeBuddyNectar");
                if(!t.getString("BeeBuddyNectar").equals("default")){
                    String texture = "textures/entity/bee/" + type + "_bee.png";
                    tex = new Identifier("beebuddy", texture);
                }
            }
            if(tex == null){
                tex = new Identifier("textures/entity/bee/bee.png");
            }
            VertexConsumer v = vcp.getBuffer(bee.getLayer(tex));

            ModelPart h = this.getContextModel().getHead();
            matrices.push();
            if(player.isInSneakingPose()){ // offsets from BipedEntityModel
                matrices.translate(0F, 0.2625F, 0F);
            }
            matrices.multiply(Vector3f.POSITIVE_Z.getRadialQuaternion(h.roll));
            matrices.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(h.yaw));
            matrices.multiply(Vector3f.POSITIVE_X.getRadialQuaternion(h.pitch));
            float ageMod = t.getInt("Age") >= 0 ? 2F : 1F;
            float headPos = player.isInSneakingPose() ? -1.4875F : -1.5F;
            matrices.translate(0F, ageMod * headPos - 0.4375, 0F);
            matrices.scale(ageMod, ageMod, ageMod);

            BeeEntityModelAccessor bea = (BeeEntityModelAccessor)(Object)bee;
            bee.setAngles(fake, 0F, 0F, animationProgress, 0F, 0F);
            bea.beebuddy$getBody().pitch = 0F;
            bea.beebuddy$getBody().pivotY = 19F;
            bee.render( matrices, v, light, OverlayTexture.DEFAULT_UV
                       , 1F, 1F, 1F, 1F);
            matrices.pop();
        }
    }

    private class FakeBeeEntity extends BeeEntity{
        public FakeBeeEntity(World w){
            super(EntityType.BEE, w);
        }

        @Override
        public boolean isOnGround(){
            return false;
        }

        @Override
        public Vec3d getVelocity(){
            return Vec3d.ZERO;
        }
    }
}
