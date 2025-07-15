package net.mykull.mykulladditions.multiblocks.reactor;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.mykull.lib.energy.AdaptedEnergyStorage;
import net.mykull.mykulladditions.Registration;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ReactorMBLogic {

    public static final String ITEMS_TAG = "Inventory";
    public static final String ENERGY_TAG = "Energy";

    public static int CAPACITY = 10000000;
    public static int MAXTRANSFER = 10000000;

    public static final int SLOT_COUNT = 2;

    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    public final EnergyStorage energy = createEnergyStorage();
    private final Lazy<IEnergyStorage> energyHandler = Lazy.of(() -> new AdaptedEnergyStorage(energy) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            return 0;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            return 0;
        }

        @Override
        public boolean canExtract() {
            return true;
        }

        @Override
        public boolean canReceive() {
            return false;
        }
    });

    private final Heat heat = new Heat(4000.0, 0.0);
    private static final double energyTransferEfficiency = 0.2;

    private Fuel fuel = null;
    public int fuelRodAmount = 0;

    private double fissionRate;
    private static final double baseFissionRate = 0.017;
    private static final double HEAT_PER_FISSION = 200.0;

    private ItemStackHandler items = createItemHandler();


    public ReactorMBController controller;

    private boolean reactorState = true; // for testing purposes set to true

    public ReactorMBLogic(ReactorMBController controller) {
        this.controller = controller;
    }

    public void tick() {
        if (fuel == null) return;
        if (!reactorState) return; // reactor is off


        insertFuel();
        exportWaste();

        convertHeat();

        produceEnergy();

        heat.decay(0.01);
    }

    private void exportWaste() {
        if (items.getStackInSlot(OUTPUT_SLOT).getCount() < 64) {
            if (fuel.getWaste() > 200) {
                items.insertItem(OUTPUT_SLOT, new ItemStack(Registration.URANIUM_INGOT.get(), 1), false);
                fuel.removeWaste(200.0);
            }
        }
    }

    private void updateFissionRate(double baseFissionRate, double controlRodInsertion) {
        // Control rods absorb neutrons: higher insertion → fewer fissions
        double reactivity = 1.0 - controlRodInsertion;

        // Apply temperature feedback: hotter fuel slightly reduces reactivity
        double temperatureCoefficient = -0.0001;
        reactivity += temperatureCoefficient * (heat.getHeat() - 300.0); // 300°C is nominal design temp

        // Clamp to avoid negative reactivity
        reactivity = Math.max(0.0, reactivity);

        this.fissionRate = baseFissionRate * reactivity;
    }

    public double getFissionRate() {
        return fissionRate;
    }


    private void convertHeat() {
        // For now, control rods are out (0% inserted)
        updateFissionRate(baseFissionRate, 0.0);

        // Burn fuel if available
        double fuelToBurn = fissionRate * fuelRodAmount; // e.g., burn 1 unit of fuel per fission
        double burned = fuel.removeFuel(fuelToBurn);
        if (burned > 0) {
            double heatProduced = burned * HEAT_PER_FISSION;
            heat.addHeat(heatProduced);
            fuel.addWaste(burned);
        }
    }

    private void insertFuel() {
        if (fuel.getRemainingSpace() < 200) return;
        if (!items.getStackInSlot(INPUT_SLOT).isEmpty() && items.getStackInSlot(INPUT_SLOT).is(Registration.LEU_PELLET.get())) {
            items.extractItem(INPUT_SLOT, 1, false);
            fuel.addFuel(200.0);
        }
    }

    private void produceEnergy() {
        double heatToEnergy = heat.getHeat() * energyTransferEfficiency;
        this.energy.receiveEnergy((int) heatToEnergy, false);
    }

    private static final double scale = 3.0;

    public void initFuel(int fuelRodCount, double efficiency) {
        fuelRodAmount = fuelRodCount;
        fuel = new Fuel(fuelRodCount * 1000);
    }

    public IEnergyStorage getEnergyStorage() {
        return energyHandler.get();
    }

    public IItemHandler getItemHandler() {
        return items;
    }

    public ItemStackHandler getItems() {
        return items;
    }

    public IHeat getHeat() {
        return heat;
    }

    public IFuel getFuel() {
        return fuel;
    }

    private EnergyStorage createEnergyStorage() {
        return new EnergyStorage(CAPACITY, MAXTRANSFER, MAXTRANSFER);
    }

    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(SLOT_COUNT) {
            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                if (slot == INPUT_SLOT) {
                    return (stack.is(Registration.LEU_PELLET.get()));
                }
                if (slot == OUTPUT_SLOT) {
                    return (stack.is(Registration.URANIUM_INGOT.get()));
                }
                return false;
            }
        };
    }

    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        if (tag.contains(ENERGY_TAG)) {
            System.out.println("kys");
            energy.deserializeNBT(registries, tag.get(ENERGY_TAG));
        }
        if (tag.contains(ITEMS_TAG))
            items.deserializeNBT(registries, tag.getCompound(ITEMS_TAG));
    }

    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put(ENERGY_TAG, energy.serializeNBT(registries));
        tag.put(ITEMS_TAG, items.serializeNBT(registries));
    }
}
