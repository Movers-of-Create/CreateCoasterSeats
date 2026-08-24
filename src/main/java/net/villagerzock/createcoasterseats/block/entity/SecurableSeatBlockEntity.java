package net.villagerzock.createcoasterseats.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.content.redstone.link.LinkBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.Direction;
import net.villagerzock.createcoasterseats.block.SecurableSeatBlock;
import net.villagerzock.createcoasterseats.registry.ModBlockEntities;

import java.util.List;

public final class SecurableSeatBlockEntity extends SmartBlockEntity {
    private boolean powered;
    private float previousHangerAngle;
    private float hangerAngle;

    public SecurableSeatBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SECURABLE_SEAT.get(), pos, state);
        powered = state.getValue(SecurableSeatBlock.POWERED);
        hangerAngle = powered ? 0 : 90;
        previousHangerAngle = hangerAngle;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(LinkBehaviour.receiver(
            this,
            ValueBoxTransform.Dual.makeSlots(FrequencySlot::new),
            strength -> setPowered(strength > 0)
        ));
    }

    public boolean isPowered() {
        return powered;
    }

    public static final int MAX_ANGLE = 65;

    @Override
    public void tick() {
        super.tick();

        previousHangerAngle = hangerAngle;
        float targetAngle = getBlockState().getValue(SecurableSeatBlock.POWERED) ? 0 : MAX_ANGLE;
        hangerAngle = Mth.approach(hangerAngle, targetAngle, 9);
    }

    public float getHangerAngle(float partialTicks) {
        return Mth.lerp(partialTicks, previousHangerAngle, hangerAngle);
    }

    public void setPowered(boolean powered) {
        BlockState state = getBlockState();
        if (this.powered == powered && state.getValue(SecurableSeatBlock.POWERED) == powered)
            return;

        this.powered = powered;
        setChanged();

        if (level != null && !level.isClientSide) {
            if (state.getValue(SecurableSeatBlock.POWERED) != powered)
                level.setBlock(worldPosition, state.setValue(SecurableSeatBlock.POWERED, powered), Block.UPDATE_ALL);
            sendData();
        }
    }

    public boolean isHangerDown(float partialTicks) {
        return getHangerAngle(partialTicks) <= 0;
    }

    private static final class FrequencySlot extends ValueBoxTransform.Dual {
        private FrequencySlot(boolean first) {
            super(first);
        }

        @Override
        public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
            Vec3 offset = new Vec3(8D / 16D, -0.01D / 16D, first ? 5.5D / 16D : 10.5D / 16D);
            float rotation = getVisualRotation(state.getValue(SecurableSeatBlock.FACING));
            return VecHelper.rotateCentered(offset, rotation, Direction.Axis.Y);
        }

        @Override
        public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack poseStack) {
            float rotation = getVisualRotation(state.getValue(SecurableSeatBlock.FACING));
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
            poseStack.mulPose(Axis.XP.rotationDegrees(270));
        }

        private static float getVisualRotation(Direction facing) {
            return switch (facing) {
                case NORTH -> 180;
                case SOUTH -> 0;
                case EAST -> 90;
                case WEST -> -90;
                default -> 0;
            };
        }
    }
}
