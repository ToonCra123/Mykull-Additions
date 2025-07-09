package net.mykull.mykulladditions.multiblocks.reactor;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.mykull.lib.energy.AdaptedEnergyStorage;

public class ReactorMBLogic {

    public static final String ITEMS_TAG = "Inventory";
    public static final String ENERGY_TAG = "Energy";

    public ReactorMBController controller;

    public ReactorMBLogic(ReactorMBController controller) {
        this.controller = controller;
    }

    public void tick() {
        // nothing jit
    }

    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {

    }

    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    }
}
