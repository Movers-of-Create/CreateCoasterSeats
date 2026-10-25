package net.villagerzock.createcoasterseats.item;

import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.villagerzock.createcoasterseats.block.SeatType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SecurableSeatBlockItem extends BlockItem {
    private final DyeColor color;
    private final SeatType seatType;

    public SecurableSeatBlockItem(Block block, Properties properties, DyeColor color) {
        this(block, properties, color, SeatType.PLASTIC);
    }

    public SecurableSeatBlockItem(Block block, Properties properties, DyeColor color, SeatType seatType) {
        super(block, properties);
        this.color = color;
        this.seatType = seatType;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    public @NotNull String getDescriptionId() {
        return getOrCreateDescriptionId();
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        tooltip.add(CreateLang.translateDirect(
                        "tooltip.holdForDescription",
                        CreateLang.translateDirect("tooltip.keyShift")
                                .withStyle(flag.hasShiftDown() ? ChatFormatting.WHITE : ChatFormatting.GRAY)
                )
                .withStyle(ChatFormatting.DARK_GRAY));

        if (flag.hasShiftDown()) {
            tooltip.add(CommonComponents.EMPTY);
            tooltip.add(Component.translatable("tooltip.createcoasterseats.securable_seat.description")
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}
