package net.villagerzock.createcoasterseats.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.villagerzock.createcoasterseats.Createcoasterseats;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Createcoasterseats.MOD_ID);
    public static final Map<DyeColor, DeferredItem<BlockItem>> SECURABLE_SEATS;
    public static final DeferredItem<BlockItem> BLACK_SECURABLE_SEAT;

    static {
        Map<DyeColor, DeferredItem<BlockItem>> seats = new EnumMap<>(DyeColor.class);
        ModBlocks.SECURABLE_SEATS.forEach((color, block) -> seats.put(color, ITEMS.register(
            color.getName() + "_securable_seat",
            () -> new BlockItem(block.get(), new Item.Properties())
        )));
        SECURABLE_SEATS = Collections.unmodifiableMap(seats);
        BLACK_SECURABLE_SEAT = SECURABLE_SEATS.get(DyeColor.BLACK);
    }

    private ModItems() {
    }
}
