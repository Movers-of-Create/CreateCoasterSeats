package net.villagerzock.createcoasterseats.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.villagerzock.createcoasterseats.CreateCoasterSeatsConfig;
import net.villagerzock.createcoasterseats.Createcoasterseats;

public final class ModCreativeTabs {
    private static final DyeColor[] COLORS = DyeColor.values();
    private static final long ICON_INTERVAL_MILLIS = 750;

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Createcoasterseats.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = CREATIVE_TABS.register(
        "main",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.createcoasterseats"))
            .icon(ModCreativeTabs::getAnimatedIcon)
            .displayItems((parameters, output) ->
                ModItems.SECURABLE_SEATS.values().forEach(item -> output.accept(item.get())))
            .build()
    );

    public static ItemStack getAnimatedIcon() {
        int colorIndex = (int) ((System.currentTimeMillis() / ICON_INTERVAL_MILLIS) % COLORS.length);
        return ModItems.SECURABLE_SEATS.get(COLORS[colorIndex]).get().getDefaultInstance();
    }

    public static DyeColor getAnimatedTint(){
        if (!CreateCoasterSeatsConfig.ENABLE_CREATIVE_TAB_ANIMATION.get()){
            return DyeColor.GRAY;
        }
        int colorIndex = (int) ((System.currentTimeMillis() / ICON_INTERVAL_MILLIS) % COLORS.length);
        return COLORS[colorIndex];
    }

    private ModCreativeTabs() {
    }
}
