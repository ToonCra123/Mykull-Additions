package net.mykull.mykulladditions.multiblocks.reactor;

import net.minecraft.util.Mth;
import net.neoforged.neoforge.energy.EnergyStorage;

public class Heat implements IHeat {

    private final double maxHeat;

    private double heatStored;

    private double ambientHeat;

    public Heat(double maxHeat, double ambientHeat) {
        this.maxHeat = maxHeat;
        this.ambientHeat = ambientHeat;
    }

    @Override
    public double getHeat() {
        return heatStored;
    }

    @Override
    public double getMaxHeat() {
        return maxHeat;
    }

    @Override
    public double addHeat(double heat) {
        if (heat <= 0) return 0;
        double heatReceived = Mth.clamp(this.heatStored + heat, ambientHeat, this.maxHeat);
        this.heatStored = heatReceived;
        return heatReceived;
    }

    @Override
    public double removeHeat(double heat) {
        double heatExtrated = Math.min(this.heatStored - ambientHeat, heat);
        this.heatStored -= heatExtrated;
        return heatExtrated;
    }

    @Override
    public void decay(double coolingRate) {
        heatStored -= (heatStored - ambientHeat) * coolingRate;
    }
}
