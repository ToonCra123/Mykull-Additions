package net.mykull.mykulladditions.multiblocks.reactor.glass;

import java.util.HashMap;
import java.util.Map;

public class ConnectedPatterns {

    // This map takes a pattern of four directions (excluding the one we are looking at) and returns the sprite index
    // and rotation for the quad that we are looking at.
    static final Map<Pattern, QuadSetting> PATTERNS = new HashMap<>();

    // Given a pattern of four directions (excluding the one we are looking at) we return the sprite index and rotation
    // for the quad that we are looking at.
    public static QuadSetting findPattern(boolean s1, boolean s2, boolean s3, boolean s4) {
        return PATTERNS.get(new Pattern(s1, s2, s3, s4));
    }

    // This enum represents the type of sprite (texture)
    public enum SpriteIdx {
        SPRITE_NONE,
        SPRITE_END,
        SPRITE_STRAIGHT,
        SPRITE_CORNER,
        SPRITE_THREE,
        SPRITE_CROSS
    }

    // This enum represents the type of sprite (texture) as well as the rotation for that sprite
    public record QuadSetting(SpriteIdx sprite, int rotation) {

        public static QuadSetting of(SpriteIdx sprite, int rotation) {
            return new QuadSetting(sprite, rotation);
        }
    }

    // A pattern represents a configuration (cable or no cable) for the four directions excluding the one we are looking at
    public record Pattern(boolean s1, boolean s2, boolean s3, boolean s4) {

        public static Pattern of(boolean s1, boolean s2, boolean s3, boolean s4) {
            return new Pattern(s1, s2, s3, s4);
        }
    }
}