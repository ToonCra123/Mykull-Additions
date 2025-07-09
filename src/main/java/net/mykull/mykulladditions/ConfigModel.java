package net.mykull.mykulladditions;

public class ConfigModel {
    public ReactorSize reactorSize = new ReactorSize();

    public static class ReactorSize {
        public int maxWidth = 16;
        public int maxHeight = 16;
        public int maxDepth = 16;
    }
}
