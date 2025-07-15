package net.mykull.mykulladditions;

public class ConfigModel {
    public ReactorSize reactorSize = new ReactorSize();

    public static class ReactorSize {
        public int maxWidth = 16;
        public int maxHeight = 16;
        public int maxDepth = 16;
    }

    public static class ReactorCommon {
        public double reactorFEConversionFactor = 10.0;
        public double reactorSteamConversionFactor = 30.0;
    }
}
