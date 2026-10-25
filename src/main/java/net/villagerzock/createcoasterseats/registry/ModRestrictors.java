package net.villagerzock.createcoasterseats.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.villagerzock.createcoasterseats.Createcoasterseats;
import net.villagerzock.createcoasterseats.block.SeatStyle;
import net.villagerzock.createcoasterseats.restrictions.RestrictorType;

import java.lang.reflect.Field;

public class ModRestrictors {
    public static final DeferredRegister<RestrictorType> RESTRICTOR_TYPES =
            DeferredRegister.create(
                    ModRegistries.RESTRICTOR_TYPES_KEY,
                    Createcoasterseats.MOD_ID
            );

    public static final DeferredHolder<RestrictorType, RestrictorType> BASIC =
            RESTRICTOR_TYPES.register(
                    "basic",
                    ()-> new RestrictorType(ResourceLocation.fromNamespaceAndPath(Createcoasterseats.MOD_ID, "default_restrictor"),0, 65, new Vec3(0, 20 / 16D, -2 / 16D),SeatStyle.BACK_RESTRICTOR, ModItems.BASIC_RESTRICTOR.get(), ((entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, playerModelBundle, pos, level, isFpv) -> {
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
            );

    public static final DeferredHolder<RestrictorType, RestrictorType> LAPBAR =
            RESTRICTOR_TYPES.register(
                    "lapbar",
                    () -> new RestrictorType(ResourceLocation.fromNamespaceAndPath(Createcoasterseats.MOD_ID, "lapbar"), 10, -20, new Vec3(0, 0, 18 / 16D),SeatStyle.LAP_BAR, ModItems.LAPBAR.get(), ((entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, playerModelBundle, pos, level, isFpv) -> {
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
                    }))
            );

    static {
        try {
            Field field = RestrictorType.class.getDeclaredField("model");
            System.out.println("Field 'model' found. Type is: " + field.getType().getSimpleName());
        } catch (NoSuchFieldException e) {
            System.out.println("Field 'model' not found. YAY!");
        }
    }

    private ModRestrictors() {
    }
}
