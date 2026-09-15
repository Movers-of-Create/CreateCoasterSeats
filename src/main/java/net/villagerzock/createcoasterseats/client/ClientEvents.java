package net.villagerzock.createcoasterseats.client;

import net.createmod.catnip.config.ui.BaseConfigScreen;
import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.ModLifecycleEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.villagerzock.createcoasterseats.Createcoasterseats;
import net.villagerzock.createcoasterseats.ponder.PonderScenes;
import net.villagerzock.createcoasterseats.registry.ModBlockEntities;

import java.lang.reflect.Field;

import static net.villagerzock.createcoasterseats.Createcoasterseats.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public final class ClientEvents {
    private static final ResourceLocation HANGER_MODEL = ResourceLocation.fromNamespaceAndPath(
        MOD_ID,
        "block/securable_seat_hanger"
    );

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new PonderScenes());
        try {
            Field field = ModLifecycleEvent.class.getDeclaredField("container");
            field.setAccessible(true);
            ModContainer modContainer = (ModContainer) field.get(event);
            modContainer.registerExtensionPoint(
                    IConfigScreenFactory.class,
                    (mc, parent) -> new BaseConfigScreen(parent,MOD_ID)
            );
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
