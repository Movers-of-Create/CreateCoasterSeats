package net.villagerzock.createcoasterseats.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
            .displayItems((parameters, output) ->{
                for (DyeColor color : DyeColor.values()){
                    output.accept(ModItems.COLORED_BLOCKS.get(color).get());
                }
                for (DyeColor color : DyeColor.values()) {
                    output.accept(ModItems.WOOL_SEATS.get(color).get());
                }
                output.accept(ModItems.BASIC_RESTRICTOR.get());
                output.accept(ModItems.LAPBAR.get());
                output.accept(ModItems.PLASTIC_SHEET.get());
                output.accept(ModItems.POLYMER_RESIN.get());
                output.accept(ModItems.ORGANIC_MASS.get());
                output.accept(ModItems.SCULK_POWDER.get());
                output.accept(ModItems.MOLTEN_PLASTIC_BUCKET.get());
            })
            .build()
    );

    public static ItemStack getAnimatedIcon() {
        int colorIndex = (int) ((System.currentTimeMillis() / ICON_INTERVAL_MILLIS) % COLORS.length);
        return new ItemStack(ModItems.COLORED_BLOCKS.get(DyeColor.values()[colorIndex]).get());
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
