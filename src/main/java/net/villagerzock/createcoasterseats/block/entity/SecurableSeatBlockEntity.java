package net.villagerzock.createcoasterseats.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.content.redstone.link.LinkBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.CenteredSideValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.gui.AllIcons;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.lang.Lang;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.Direction;
import net.neoforged.fml.ISystemReportExtender;
import net.villagerzock.createcoasterseats.block.ISecurableSeat;
import net.villagerzock.createcoasterseats.block.SeatStyle;
import net.villagerzock.createcoasterseats.block.SecurableSeatBlock;
import net.villagerzock.createcoasterseats.icons.SeatsAllIcons;
import net.villagerzock.createcoasterseats.registry.ModBlockEntities;
import net.villagerzock.createcoasterseats.registry.ModRegistries;
import net.villagerzock.createcoasterseats.restrictions.RestrictorType;

import java.util.List;

public class SecurableSeatBlockEntity extends SmartBlockEntity {
    private boolean linkPowered;
    private boolean redstonePowered;
    private float previousHangerAngle;
    private float hangerAngle;
    private RestrictorType restrictorType;
    private RestrictorType animatedRestrictorType;
    private boolean hangerAngleInitialized;

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        ResourceLocation typeId = restrictorType == null ? null : ModRegistries.RESTRICTOR_TYPES.getKey(restrictorType);
        if (typeId != null) {
            tag.putString("RestrictorType", typeId.toString());
        } else {
            tag.remove("RestrictorType");
        }
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        ResourceLocation typeId = ResourceLocation.tryParse(tag.getString("RestrictorType"));
        restrictorType = typeId == null ? null : ModRegistries.RESTRICTOR_TYPES.get(typeId);
        updateRestrictorAngles();
    }

    public boolean hasRestrictor() {
        return restrictorType != null;
    }

    public ItemStack getRestrictorItem() {
        return restrictorType == null ? ItemStack.EMPTY : new ItemStack(restrictorType.getItem());
    }

    public RestrictorType getRestrictorType() {
        return restrictorType;
    }

    public ActivationMode getActivationMode() {
        return activationMode.get();
    }

    public PartialModel getRestrictorModel() {
        if (restrictorType != null) {
            return restrictorType.getModel();
        }
        return null;
    }

    public enum ActivationMode implements INamedIconOptions {
        LINK_ONLY(SeatsAllIcons.I_SEAT_LINK_ONLY),
        LINK_AND_REDSTONE(SeatsAllIcons.I_SEAT_LINK_AND_REDSTONE),
        REDSTONE_ONLY(SeatsAllIcons.I_SEAT_REDSTONE_ONLY)
        ;

        private final String translationKey;
        private final AllIcons icon;
        ActivationMode(AllIcons icon){
            this.icon = icon;
            this.translationKey = "createcoasterseats.securable_seat.activation_mode." + Lang.asId(name());
        }

        @Override
        public AllIcons getIcon() {
            return icon;
        }

        @Override
        public String getTranslationKey() {
            return translationKey;
        }
    }

    protected ScrollOptionBehaviour<ActivationMode> activationMode;

    public SecurableSeatBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SECURABLE_SEAT.get(), pos, state);

        maxAngle = 0;
        minAngle = 0;

        linkPowered = state.getValue(SecurableSeatBlock.POWERED);
        redstonePowered = state.getValue(SecurableSeatBlock.POWERED);
        hangerAngle = linkPowered ? minAngle : maxAngle;
        previousHangerAngle = hangerAngle;

    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(LinkBehaviour.receiver(
            this,
            ValueBoxTransform.Dual.makeSlots(FrequencySlot::new),
            strength -> setLinkPowered(strength > 0)
        ));

        activationMode = new ScrollOptionBehaviour<>(ActivationMode.class,
                Component.translatable("createcoasterseats.seats.activation_method"), this, new SelectionModeValueBox());
        activationMode.requiresWrench()
                .withCallback(activationMode -> {
                    updateState();
                });
        behaviours.add(activationMode);

    }

    public boolean isLinkPowered() {
        return linkPowered;
    }

    public boolean isRedstonePowered() {
        return redstonePowered;
    }

    public float maxAngle;
    public float minAngle;

    private void updateRestrictorAngles() {
        if (restrictorType != null) {
            minAngle = restrictorType.getClosedAngle();
            maxAngle = restrictorType.getOpenAngle();
        } else {
            minAngle = 0;
            maxAngle = 0;
        }
        // Keep an ongoing animation when an ordinary data packet is received.
        // Initialize both interpolation endpoints only on load or a restrictor change.
        if (!hangerAngleInitialized || animatedRestrictorType != restrictorType) {
            hangerAngle = getBlockState().getValue(SecurableSeatBlock.POWERED) ? minAngle : maxAngle;
            previousHangerAngle = hangerAngle;
            animatedRestrictorType = restrictorType;
            hangerAngleInitialized = true;
        }
    }

    @Override
    public void tick() {
        super.tick();

        previousHangerAngle = hangerAngle;
        float targetAngle = getBlockState().getValue(SecurableSeatBlock.POWERED) ? minAngle : maxAngle;
        hangerAngle = Mth.approach(hangerAngle, targetAngle, 9);
    }

    public float getHangerAngle(float partialTicks) {
        return Mth.lerp(partialTicks, previousHangerAngle, hangerAngle);
    }

    public void setLinkPowered(boolean linkPowered) {
        if (this.linkPowered == linkPowered)
            return;

        this.linkPowered = linkPowered;
        updateState();
    }

    public void setRedstonePowered(boolean redstonePowered) {
        if (this.redstonePowered == redstonePowered)
            return;

        this.redstonePowered = redstonePowered;
        updateState();
    }

    public boolean isPowered(){
        return switch (activationMode.get()) {
            case LINK_AND_REDSTONE -> linkPowered || redstonePowered;
            case REDSTONE_ONLY -> redstonePowered;
            case LINK_ONLY -> linkPowered;
        };
    }

    public void updateRestrictor(RestrictorType type){
        if (this.getLevel().isClientSide()){
            return;
        }

        this.restrictorType = type;
        updateRestrictorAngles();
        BlockPos pos = getBlockPos();
        Level lvl = getLevel();
        BlockState state = getBlockState();
        state = state.setValue(SecurableSeatBlock.SEAT_STYLE, type == null ? SeatStyle.BASE : type.getStyle());
        assert lvl != null;
        lvl.setBlockAndUpdate(pos,state);
        setChanged();
        sendData();
    }

    public void updateState(){
        BlockState state = getBlockState();
        if (state.getValue(SecurableSeatBlock.POWERED) == isPowered())
            return;
        setChanged();

        if (level != null) {
            if (state.getValue(SecurableSeatBlock.POWERED) != isPowered())
                level.setBlock(worldPosition, state.setValue(SecurableSeatBlock.POWERED, isPowered()), Block.UPDATE_ALL);
            if (!level.isClientSide)
                sendData();
        }
    }

    public boolean isHangerDown(float partialTicks) {
        return hasRestrictor() && Math.abs(getHangerAngle(partialTicks) - minAngle) < 0.1;
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

    public Vec3 getRestrictorOffset(){
        if (restrictorType != null){
            return restrictorType.getOffset();
        }
        return Vec3.ZERO;
    }

    private class SelectionModeValueBox extends CenteredSideValueBoxTransform {

        public SelectionModeValueBox() {
            super((blockState, direction) -> !direction.getAxis()
                    .isVertical());
        }

        @Override
        public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
            int yPos = 3;
            Vec3 location = VecHelper.voxelSpace(8, yPos, 15.5);
            location = VecHelper.rotateCentered(location, AngleHelper.horizontalAngle(getSide()), Direction.Axis.Y);
            return location;
        }

        @Override
        public float getScale() {
            return super.getScale();
        }

    }
}
