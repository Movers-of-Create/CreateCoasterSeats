package net.villagerzock.createcoasterseats.event;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.actors.seat.SeatBlock;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.villagerzock.createcoasterseats.item.RestrictorItem;

public final class SeatRestrictorHandler {
    private SeatRestrictorHandler() {
    }

    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getItemStack().getItem() instanceof RestrictorItem))
            return;

        if (event.getLevel().getBlockState(event.getPos()).getBlock() instanceof SeatBlock seat
                && AllBlocks.SEATS.get(seat.getColor()).get() == seat) {
            event.setUseBlock(TriState.FALSE);
        }
    }
}
