package net.mykull.mykulladditions.multiblocks.reactor;

public interface IFuel {
    double getFuelCapacity();
    double getFuel();
    double getWaste();

    double addFuel(double amount);
    double removeFuel(double amount);   // returns amount actually removed

    double addWaste(double amount);
    double removeWaste(double amount);  // returns amount actually removed
}
