package net.villagerzock.createcoasterseats.registry;

import net.minecraft.resources.ResourceLocation;
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
    public static final DeferredBlock<SecurableSeatBlock> SECURABLE_SEAT;

    static {
        Map<DyeColor, DeferredBlock<SecurableSeatBlock>> seats = new EnumMap<>(DyeColor.class);
        for (DyeColor color : DyeColor.values()) {
            BLOCKS.addAlias(ResourceLocation.fromNamespaceAndPath(Createcoasterseats.MOD_ID,color.getName()+"_securable_seat"), ResourceLocation.fromNamespaceAndPath(Createcoasterseats.MOD_ID, color.getName() + "_restrictor_seat"));
            BLOCKS.addAlias(ResourceLocation.fromNamespaceAndPath(Createcoasterseats.MOD_ID, color.getName() + "_restrictor_seat"),ResourceLocation.fromNamespaceAndPath(Createcoasterseats.MOD_ID,  "securable_seat"));
            /*seats.put(color, BLOCKS.register(
                color.getName() + "_restrictor_seat",
                () -> new SecurableSeatBlock(BlockBehaviour.Properties.of().strength(1.0F), ((entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, playerModelBundle, pos, level, isFpv) -> {
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
                }), 0, 65, new Vec3(0, 20 / 16D, -2 / 16D), ResourceLocation.fromNamespaceAndPath(Createcoasterseats.MOD_ID, "restrictor/default_restrictor"), seats)
            ));*/
        }
        SECURABLE_SEAT = BLOCKS.register(
                "securable_seat",
                () -> new SecurableSeatBlock(BlockBehaviour.Properties.of().strength(1.0F))
        );
        for (DyeColor color : DyeColor.values()) {
            BLOCKS.addAlias(ResourceLocation.fromNamespaceAndPath(Createcoasterseats.MOD_ID, color.getName() + "_lapbar_seat"),ResourceLocation.fromNamespaceAndPath(Createcoasterseats.MOD_ID,  "securable_seat"));
            /*lapbarSeats.put(color, BLOCKS.register(
                    color.getName() + "_lapbar_seat",
                    () -> new SecurableSeatBlock(BlockBehaviour.Properties.of().strength(1.0F), color, ((entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, playerModelBundle, pos, level, isFpv) -> {
                        playerModelBundle.rightArm().xRot = (float) Math.toRadians(-60);
                        playerModelBundle.rightArm().z += 2f;
                        playerModelBundle.rightArm().yRot = 0;
                        playerModelBundle.leftArm().xRot = (float) Math.toRadians(-60);
                        playerModelBundle.leftArm().z += 2f;
                        playerModelBundle.leftArm().yRot = 0;

                        if (isFpv){
                            playerModelBundle.body().visible = false;
                            playerModelBundle.leftLeg().visible = false;
                            playerModelBundle.rightLeg().visible = false;
                        }
                    }), 10, -20, new Vec3(0, 0, 18 / 16D), ResourceLocation.fromNamespaceAndPath(Createcoasterseats.MOD_ID, "restrictor/lapbar"), lapbarSeats)
            ));*/
        }
    }

    private ModBlocks() {
    }
}
