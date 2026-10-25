package net.villagerzock.createcoasterseats.block;

import net.minecraft.util.StringRepresentable;

public enum SeatStyle implements StringRepresentable {
    BASE,
    BACK_RESTRICTOR,
    LAP_BAR;

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
