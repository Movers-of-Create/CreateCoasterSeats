package net.villagerzock.createcoasterseats.mixin;

import com.simibubi.create.content.contraptions.actors.seat.SeatEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.Minecart;
import net.neoforged.fml.common.Mod;
import net.villagerzock.createcoasterseats.block.PlayerModelBundle;
import net.villagerzock.createcoasterseats.block.SecurableSeatBlock;
import net.villagerzock.createcoasterseats.block.entity.SecurableSeatBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public class PlayerModelMixin<T extends LivingEntity> extends HumanoidModel<T> {
    @Shadow
    @Final
    public ModelPart jacket;

    @Shadow
    @Final
    public ModelPart leftPants;

    @Shadow
    @Final
    public ModelPart rightPants;

    @Shadow
    @Final
    public ModelPart leftSleeve;

    @Shadow
    @Final
    public ModelPart rightSleeve;

    public PlayerModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/HumanoidModel;setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", shift = At.Shift.AFTER))
    private void createcoasterseats$changeHandAnimation(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci){
        if (!(entity instanceof AbstractClientPlayer player)){
            return;
        }
        float partialTicks = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
        if (entity.getVehicle() instanceof SeatEntity seat && seat.level().getBlockState(seat.blockPosition()).getBlock() instanceof SecurableSeatBlock securableSeatBlock && seat.level().getBlockEntity(seat.blockPosition()) instanceof SecurableSeatBlockEntity securableSeatBlockEntity && securableSeatBlockEntity.isHangerDown(partialTicks)){
            boolean firstPerson = entity == Minecraft.getInstance().cameraEntity && Minecraft.getInstance().options.getCameraType().isFirstPerson();
            securableSeatBlock.updatePlayerAnimation(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, new PlayerModelBundle(this.head, this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg), seat.blockPosition(), seat.level(), firstPerson);
            this.jacket.visible = this.body.visible;
            this.leftPants.visible = this.leftLeg.visible;
            this.rightPants.visible = this.rightLeg.visible;
            this.leftSleeve.visible = this.leftArm.visible;
            this.rightSleeve.visible = this.rightArm.visible;
            if (firstPerson){
                this.head.visible = false;
                this.hat.visible = false;
            }
        }
    }
}
