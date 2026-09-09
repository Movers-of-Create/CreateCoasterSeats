package net.villagerzock.createcoasterseats.client;

import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.villagerzock.createcoasterseats.Createcoasterseats;
import net.villagerzock.createcoasterseats.ponder.PonderScenes;
import net.villagerzock.createcoasterseats.registry.ModBlockEntities;

@EventBusSubscriber(modid = Createcoasterseats.MOD_ID, value = Dist.CLIENT)
public final class ClientEvents {
    private static final ResourceLocation HANGER_MODEL = ResourceLocation.fromNamespaceAndPath(
        Createcoasterseats.MOD_ID,
        "block/securable_seat_hanger"
    );

    private ClientEvents() {
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new PonderScenes());
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.SECURABLE_SEAT.get(), SecurableSeatRenderer::new);
    }

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(ModelResourceLocation.standalone(HANGER_MODEL));
    }
}
