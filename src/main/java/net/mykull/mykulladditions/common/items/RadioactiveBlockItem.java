package net.mykull.mykulladditions.common.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class RadioactiveBlockItem extends BlockItem {

    private final long radiationLevel;

    public RadioactiveBlockItem(Block block, Properties properties, long radiationLevel) {
        super(block, properties);
        this.radiationLevel = radiationLevel;
    }

    public long getRadiationLevel() {
        return radiationLevel;
    }
}
