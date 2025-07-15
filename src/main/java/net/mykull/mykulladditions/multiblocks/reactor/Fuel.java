package net.mykull.mykulladditions.multiblocks.reactor;

public class Fuel implements IFuel {

    private final double fuelCapacity;

    private double fuel = 0;
    private double waste = 0;

    public Fuel(double capacity) {
        this.fuelCapacity = capacity;
    }

    public double getRemainingSpace() {
        return fuelCapacity - (fuel + waste);
    }

    @Override
    public double getFuelCapacity() {
        return fuelCapacity;
    }

    @Override
    public double getFuel() {
        return fuel;
    }

    @Override
    public double getWaste() {
        return waste;
    }

    @Override
    public double addFuel(double amount) {
        if (amount <= 0) return 0;

        // Total space left is capacity - (fuel + waste)
        double spaceLeft = fuelCapacity - (fuel + waste);
        double toAdd = Math.min(spaceLeft, amount);

        fuel += toAdd;
        return toAdd;
    }

    @Override
    public double removeFuel(double amount) {
        if (amount <= 0) return 0;

        double toRemove = Math.min(fuel, amount);
        fuel -= toRemove;
        return toRemove;
    }

    @Override
    public double addWaste(double amount) {
        if (amount <= 0) return 0;

        double spaceLeft = fuelCapacity - (fuel + waste);
        double toAdd = Math.min(spaceLeft, amount);

        waste += toAdd;
        return toAdd;
    }

    @Override
    public double removeWaste(double amount) {
        if (amount <= 0) return 0;

        double toRemove = Math.min(waste, amount);
        waste -= toRemove;
        return toRemove;
    }
}