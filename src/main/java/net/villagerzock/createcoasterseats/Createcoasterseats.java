package net.villagerzock.createcoasterseats;

import net.createmod.catnip.config.ui.BaseConfigScreen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
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
}
