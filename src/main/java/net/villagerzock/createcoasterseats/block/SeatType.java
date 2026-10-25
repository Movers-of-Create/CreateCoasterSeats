package net.villagerzock.createcoasterseats.block;

import net.minecraft.util.StringRepresentable;

public enum SeatType implements StringRepresentable {
    WOOL,
    PLASTIC;

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
