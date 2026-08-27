package net.villagerzock.createcoasterseats.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.contraptions.actors.seat.SeatEntity;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.villagerzock.createcoasterseats.block.entity.SecurableSeatBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Redirect(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isSleeping()Z"))
    private boolean isSleeping(LivingEntity livingEntity, @Local(argsOnly = true) DeltaTracker deltaTracker) {
        if (livingEntity.isSleeping()){
            return true;
        }
        if (livingEntity.getVehicle() instanceof SeatEntity seat && seat.level().getBlockEntity(seat.blockPosition()) instanceof SecurableSeatBlockEntity securableSeatBlockEntity) {
            return securableSeatBlockEntity.isHangerDown(deltaTracker.getGameTimeDeltaPartialTick(true));
        }
        return false;
    }
}
