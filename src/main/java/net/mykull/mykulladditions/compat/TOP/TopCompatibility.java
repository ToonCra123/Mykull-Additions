package net.mykull.mykulladditions.compat.TOP;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.ITheOneProbe;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModList;

import java.util.function.Function;

public class TopCompatibility {

    public static void register() {
        if (ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", GetTheOneProbe::new);
        }
    }

    public static class GetTheOneProbe implements Function<ITheOneProbe, Void> {
        public static ITheOneProbe probe;

        @Override
        public Void apply(ITheOneProbe top) {
            top.registerProvider(new MykullProbeProvider());
            return null;
        }
    }
}