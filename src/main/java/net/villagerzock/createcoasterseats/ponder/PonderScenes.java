package net.villagerzock.createcoasterseats.ponder;

import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.villagerzock.createcoasterseats.Createcoasterseats;
import net.villagerzock.createcoasterseats.block.SecurableSeatBlock;
import net.villagerzock.createcoasterseats.registry.ModBlocks;

public class PonderScenes implements PonderPlugin {
    @Override
    public String getModId() {
        return Createcoasterseats.MOD_ID;
    }


    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        helper.addToTag(AllCreatePonderTags.REDSTONE)
                .add(ModBlocks.RED_RESTRICTOR_SEAT.getId());
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
                        SeatPonder::sceneOne,
                        AllCreatePonderTags.REDSTONE
                )
                .addStoryBoard(
                        "seat_1",
                        SeatPonder::sceneTwo
                )
                .addStoryBoard(
                        "seat_2",
                        SeatPonder::sceneThree
                );
    }
}
