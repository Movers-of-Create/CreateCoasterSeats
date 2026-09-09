package net.villagerzock.createcoasterseats.ponder;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.villagerzock.createcoasterseats.Createcoasterseats;
import net.villagerzock.createcoasterseats.block.SecurableSeatBlock;

public class PonderScenes implements PonderPlugin {
    @Override
    public String getModId() {
        return Createcoasterseats.MOD_ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderPlugin.super.registerScenes(helper);

        ResourceLocation[] blocks = BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof SecurableSeatBlock)
                .map(BuiltInRegistries.BLOCK::getKey)
                .toArray(ResourceLocation[]::new);
        helper.forComponents(blocks)
                .addStoryBoard(
                        "seat_0",
                        SeatPonder::sceneOne
                )
                .addStoryBoard(
                        "seat_1",
                        SeatPonder::sceneTwo
                );
    }
}
