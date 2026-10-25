package net.villagerzock.createcoasterseats.registry;

import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.villagerzock.createcoasterseats.Createcoasterseats;
import net.villagerzock.createcoasterseats.item.RestrictorItem;
import net.villagerzock.createcoasterseats.item.SecurableSeatBlockItem;
import net.villagerzock.createcoasterseats.block.SeatType;
import org.openjdk.nashorn.internal.runtime.JSErrorType;

import java.util.EnumMap;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Createcoasterseats.MOD_ID);

    public static final DeferredItem<RestrictorItem> BASIC_RESTRICTOR = ITEMS.register(
            "basic_restrictor", () -> new RestrictorItem(new Item.Properties(), ModRestrictors.BASIC));
    public static final DeferredItem<RestrictorItem> LAPBAR = ITEMS.register(
            "lapbar", () -> new RestrictorItem(new Item.Properties(), ModRestrictors.LAPBAR));
    public static final DeferredItem<Item> PLASTIC_SHEET = ITEMS.register(
            "plastic_sheet", () -> new Item(new Item.Properties())
    );
    public static DeferredItem<Item> POLYMER_RESIN = ITEMS.register(
            "polymer_resin", () -> new Item(new Item.Properties())
    );
    public static DeferredItem<Item> ORGANIC_MASS = ITEMS.register(
            "organic_mass", () -> new Item(new Item.Properties())
    );
    public static DeferredItem<Item> SCULK_POWDER = ITEMS.register(
            "sculk_powder", () -> new Item(new Item.Properties())
    );
    public static final DeferredItem<Item> MOLTEN_PLASTIC_BUCKET = ITEMS.register(
            "molten_plastic_bucket",
            () -> new BucketItem(
                    ModFluids.MOLTEN_PLASTIC.get(),
                    new Item.Properties()
                            .stacksTo(1)
                            .craftRemainder(Items.BUCKET)
            )
    );

    public static final EnumMap<DyeColor, DeferredItem<BlockItem>> COLORED_BLOCKS = new EnumMap<>(DyeColor.class);
    public static final EnumMap<DyeColor, DeferredItem<BlockItem>> WOOL_SEATS = new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()){
            COLORED_BLOCKS.put(color, ITEMS.register(color.getName() + "_plastic_seat",()->(new SecurableSeatBlockItem(ModBlocks.SECURABLE_SEAT.get(), new Item.Properties(), color))));
            WOOL_SEATS.put(color, ITEMS.register(color.getName() + "_wool_seat",
                    () -> new SecurableSeatBlockItem(ModBlocks.SECURABLE_SEAT.get(), new Item.Properties(), color, SeatType.WOOL)));
        }
    }

    private ModItems() {

    }
}
