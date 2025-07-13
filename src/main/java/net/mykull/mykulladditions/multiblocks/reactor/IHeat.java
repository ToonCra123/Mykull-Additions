package net.mykull.mykulladditions.multiblocks.reactor;

// No storage yet since I am lazy and if you stop game just think of it
// as you are waiting a while and it cools down.

// Mykull
public interface IHeat {
     /// @return amount of heat
    int getHeat();

    /// @return maximum heat (like explosion amount or sum IDK)
    int getMaxHeat();

    /**
     * @param heat amount of heat to be added
     * @return the amount of heat added
     */
    int addHeat(int heat);

    /**
     * @param heat amount of heat wanting to be removed
     * @return the amount of heat actually removed
     */
    int removeHeat(int heat);
}
