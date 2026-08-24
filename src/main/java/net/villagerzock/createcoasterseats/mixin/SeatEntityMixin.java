package net.villagerzock.createcoasterseats.mixin;

import com.simibubi.create.content.contraptions.actors.seat.SeatEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.villagerzock.createcoasterseats.block.SecurableSeatBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SeatEntity.class)
public abstract class SeatEntityMixin extends Entity {
    public SeatEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "onPassengerTurned", at = @At("TAIL"))
    private void createcoasterseats$onPassengerTurned(Entity entity, CallbackInfo ci){
        BlockState state = this.level().getBlockState(this.blockPosition());
        if (state.getBlock() instanceof SecurableSeatBlock securableSeatBlock){
            createcoasterseats$clampRotation(securableSeatBlock,state,entity);
        }
    }

    @Inject(method = "positionRider", at=@At("TAIL"))
    private void createcoasterseats$positionRider(Entity pEntity, MoveFunction pCallback, CallbackInfo ci){
        BlockState state = this.level().getBlockState(this.blockPosition());
        if (state.getBlock() instanceof SecurableSeatBlock securableSeatBlock){
            pEntity.setYRot(pEntity.getYRot());
            pEntity.setYHeadRot(pEntity.getYHeadRot());
            createcoasterseats$clampRotation(securableSeatBlock,state,pEntity);
        }
    }

    private void createcoasterseats$clampRotation(SecurableSeatBlock securableSeatBlock, BlockState state, Entity entity){
        float yRot = 90 * securableSeatBlock.getRotation(state);
        entity.setYBodyRot(90 * securableSeatBlock.getRotation(state));
        float f = Mth.wrapDegrees(entity.getYRot() - yRot);
        float f1 = Mth.clamp(f, -105.0F, 105.0F);
        entity.yRotO += f1 - f;
        entity.setYRot(entity.getYRot() + f1 - f);
        entity.setYHeadRot(entity.getYRot());
    }
}
