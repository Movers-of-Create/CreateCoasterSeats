package net.villagerzock.createcoasterseats.registry;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.villagerzock.createcoasterseats.Createcoasterseats;
import net.villagerzock.createcoasterseats.block.SecurableSeatBlock;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Createcoasterseats.MOD_ID);
    public static final Map<DyeColor, DeferredBlock<SecurableSeatBlock>> SECURABLE_SEATS;
    public static final DeferredBlock<SecurableSeatBlock> BLACK_SECURABLE_SEAT;

    static {
        Map<DyeColor, DeferredBlock<SecurableSeatBlock>> seats = new EnumMap<>(DyeColor.class);
        for (DyeColor color : DyeColor.values()) {
            seats.put(color, BLOCKS.register(
                color.getName() + "_securable_seat",
                () -> new SecurableSeatBlock(BlockBehaviour.Properties.of().strength(1.0F), color, ((entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, playerModelBundle, pos, level, isFpv) -> {
                    playerModelBundle.rightArm().xRot = (float) Math.toRadians(-60);
                    playerModelBundle.rightArm().z += 2f;
                    playerModelBundle.rightArm().yRot = 0;
                    playerModelBundle.rightArm().zRot = (float) Math.toRadians(20);
                    playerModelBundle.leftArm().xRot = (float) Math.toRadians(-60);
                    playerModelBundle.leftArm().z += 2f;
                    playerModelBundle.leftArm().yRot = 0;
                    playerModelBundle.leftArm().zRot = (float) Math.toRadians(-20);

                    if (isFpv){
                        playerModelBundle.body().visible = false;
                        playerModelBundle.leftLeg().visible = false;
                        playerModelBundle.rightLeg().visible = false;
                    }
                }))
            ));
        }
        SECURABLE_SEATS = Collections.unmodifiableMap(seats);
        BLACK_SECURABLE_SEAT = SECURABLE_SEATS.get(DyeColor.BLACK);
    }

    private ModBlocks() {
    }
}
