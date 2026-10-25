package net.villagerzock.createcoasterseats;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.villagerzock.createcoasterseats.datagen.ModBlockStateProvider;
import net.villagerzock.createcoasterseats.datagen.ModRecipeProvider;
import net.villagerzock.createcoasterseats.event.SeatMountHandler;
import net.villagerzock.createcoasterseats.event.SeatRestrictorHandler;
import net.villagerzock.createcoasterseats.registry.*;

import java.util.ServiceLoader;

@Mod(Createcoasterseats.MOD_ID)
public final class Createcoasterseats {
    public static final String MOD_ID = "createcoasterseats";

    public Createcoasterseats(IEventBus modEventBus, ModContainer modContainer) {

        ServiceLoader.load(
                cpw.mods.modlauncher.serviceapi.ILaunchPluginService.class
        ).stream().forEach(provider ->
                System.out.println("[CCS] Service: " + provider.type().getName())
        );

        modContainer.registerConfig(
                ModConfig.Type.CLIENT,
                CreateCoasterSeatsConfig.SPEC
        );
        ModFluids.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModCreativeTabs.CREATIVE_TABS.register(modEventBus);
        ModRestrictors.RESTRICTOR_TYPES.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(SeatMountHandler::onEntityMount);
        NeoForge.EVENT_BUS.addListener(SeatRestrictorHandler::onRightClickBlock);
        modEventBus.addListener(NewRegistryEvent.class, ModRegistries::register);
    }

    @EventBusSubscriber(modid = Createcoasterseats.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static class ModDataGenerators {

        @SubscribeEvent
        public static void gatherData(GatherDataEvent event) {
            event.getGenerator().addProvider(
                    event.includeServer(),
                    new ModRecipeProvider(
                            event.getGenerator().getPackOutput(),
                            event.getLookupProvider()
                    )
            );
            event.getGenerator().addProvider(
                    event.includeClient(),
                    new ModBlockStateProvider(
                            event.getGenerator().getPackOutput(),
                            Createcoasterseats.MOD_ID,
                            event.getExistingFileHelper()
                    )
            );
        }
    }
}
