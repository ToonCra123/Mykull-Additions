package net.mykull.mykulladditions.multiblocks.reactor.states;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum CasingBlockStateTypes implements StringRepresentable {
    HORIZONTAL_NS,
    HORIZONTAL_WE,
    VERTICAL,
    CORNER,
    NONE,
    UNFORMED;

    public static final CasingBlockStateTypes[] VALUES = values();

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
