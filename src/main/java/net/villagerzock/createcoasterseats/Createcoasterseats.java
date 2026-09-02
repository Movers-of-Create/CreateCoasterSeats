package net.villagerzock.createcoasterseats;

import net.createmod.catnip.config.ui.BaseConfigScreen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.villagerzock.createcoasterseats.datagen.ModBlockStateProvider;
import net.villagerzock.createcoasterseats.datagen.ModItemModelProvider;
import net.villagerzock.createcoasterseats.event.SeatMountHandler;
import net.villagerzock.createcoasterseats.registry.ModBlockEntities;
import net.villagerzock.createcoasterseats.registry.ModBlocks;
import net.villagerzock.createcoasterseats.registry.ModItems;
import net.villagerzock.createcoasterseats.registry.ModCreativeTabs;

@Mod(Createcoasterseats.MOD_ID)
public final class Createcoasterseats {
    public static final String MOD_ID = "createcoasterseats";

    public Createcoasterseats(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(
                ModConfig.Type.CLIENT,
                CreateCoasterSeatsConfig.SPEC
        );
        modContainer.registerExtensionPoint(
                IConfigScreenFactory.class,
                (mc, parent) -> new BaseConfigScreen(parent,MOD_ID)
        );
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModCreativeTabs.CREATIVE_TABS.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(SeatMountHandler::onEntityMount);
    }

    @EventBusSubscriber(modid = Createcoasterseats.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static class ModDataGenerators {

        @SubscribeEvent
        public static void gatherData(GatherDataEvent event) {
            event.getGenerator().addProvider(
                    event.includeClient(),
                    new ModBlockStateProvider(
                            event.getGenerator().getPackOutput(),
                            Createcoasterseats.MOD_ID,
                            event.getExistingFileHelper()
                    )
            );

            event.getGenerator().addProvider(
                    event.includeClient(),
                    new ModItemModelProvider(
                            event.getGenerator().getPackOutput(),
                            Createcoasterseats.MOD_ID,
                            event.getExistingFileHelper()
                    )
            );
        }
    }
}
