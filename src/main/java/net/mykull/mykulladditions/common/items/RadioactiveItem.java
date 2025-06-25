package net.mykull.mykulladditions.common.items;

import net.minecraft.world.item.Item;

public class RadioactiveItem extends Item {

    private final long radiationLevel;

    public RadioactiveItem(Properties properties, long radiationLevel) {
        super(properties);
        this.radiationLevel = radiationLevel;
    }

    public long getRadiationLevel() {
        return radiationLevel;
    }
}
