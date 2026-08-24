package net.villagerzock.createcoasterseats.block;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IPlayerAnimationModificator {
    void updatePlayerAnimation(AbstractClientPlayer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTicks, PlayerModelBundle playerModelBundle, BlockPos pos, Level level);
}
