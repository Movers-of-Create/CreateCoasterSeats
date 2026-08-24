package net.villagerzock.createcoasterseats.item;

import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class SecurableSeatItem extends BlockItem {
    public SecurableSeatItem(Block block, Properties properties) {
        super(block, properties);
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
