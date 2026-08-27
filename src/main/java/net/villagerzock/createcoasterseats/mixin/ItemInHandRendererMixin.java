package net.villagerzock.createcoasterseats.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.contraptions.actors.seat.SeatEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.villagerzock.createcoasterseats.block.PlayerModelBundle;
import net.villagerzock.createcoasterseats.block.SecurableSeatBlock;
import net.villagerzock.createcoasterseats.block.entity.SecurableSeatBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    @Inject(method = "renderHandsWithItems", at = @At("HEAD"), cancellable = true)
    private void createcoasterseats$renderHandsWithItems(float partialTicks, PoseStack poseStack, MultiBufferSource.BufferSource buffer, LocalPlayer playerEntity, int combinedLight, CallbackInfo ci){
        if (playerEntity.getVehicle() instanceof SeatEntity seat && seat.level().getBlockState(seat.blockPosition()).getBlock() instanceof SecurableSeatBlock securableSeatBlock && seat.level().getBlockEntity(seat.blockPosition()) instanceof SecurableSeatBlockEntity securableSeatBlockEntity && securableSeatBlockEntity.isHangerDown(partialTicks)){
            ci.cancel();
        }
    }
}
