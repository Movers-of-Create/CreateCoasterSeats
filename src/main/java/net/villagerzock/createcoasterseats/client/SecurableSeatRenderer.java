package net.villagerzock.createcoasterseats.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.villagerzock.createcoasterseats.Createcoasterseats;
import net.villagerzock.createcoasterseats.block.entity.SecurableSeatBlockEntity;
import net.villagerzock.createcoasterseats.block.SecurableSeatBlock;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

public final class SecurableSeatRenderer extends SmartBlockEntityRenderer<SecurableSeatBlockEntity> {

    public SecurableSeatRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(SecurableSeatBlockEntity blockEntity, float partialTicks, PoseStack poseStack,
                              MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(blockEntity, partialTicks, poseStack, buffer, light, overlay);

        Direction facing = blockEntity.getBlockState().getValue(SecurableSeatBlock.FACING);
        float horizontalRotation = switch (facing) {
            case NORTH -> 180;
            case SOUTH -> 0;
            case EAST -> 90;
            case WEST -> -90;
            default -> 0;
        };

        CachedBuffers.partial(blockEntity.getRestrictorModel(), blockEntity.getBlockState())
            .center()
            .rotateYDegrees(horizontalRotation)
            .uncenter()
            .translate(blockEntity.getRestrictorOffset())
            .rotateXDegrees(-blockEntity.getHangerAngle(partialTicks))
            .light(light)
            .renderInto(poseStack, buffer.getBuffer(RenderType.cutoutMipped()));
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(@NotNull SecurableSeatBlockEntity blockEntity) {
        return super.getRenderBoundingBox(blockEntity).inflate(0,1,0);
    }
}
