package com.ncwdev.match3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

public class GameConfig {
    public static final float WORLD_WIDTH = 1400;
    public static final float WORLD_HEIGHT = 1000;

    public static final int FIELD_WIDTH = 15;
    public static final int FIELD_HEIGHT = 15;

    public static final float TILE_SIZE = 64;
    public static final float ITEM_SIZE = 64;

    public static final Vector2 fieldOffset = new Vector2(200, 25);

    public static final float SWAP_DURATION = 0.3f; // seconds

    public static final float DESTRUCTION_DELAY = 0.3f;
    public static final float DESTRUCTION_SPEED = 2f;

    public static final float FALL_SPEED = 12f;
    // public static final float FALL_SPEED = 1f;

    public static final String BACKGROUND = "background.jpeg";

    public static final String CELL = "cell.png";

    public static final String FRUIT0 = "apple.png";
    public static final String FRUIT1 = "banana.png";
    public static final String FRUIT2 = "grape.png";
    public static final String FRUIT3 = "orange.png";
    public static final String FRUIT4 = "kiwi.png";
    public static final String FRUIT5 = "watermelon.png";

    public static final Color[] COLORS = {
        Color.RED,
        Color.BLUE,
        Color.GREEN,
        Color.YELLOW,
        Color.PURPLE,
        Color.ORANGE
    };

    public static final ObjectMap<Color, String> COLOR_TO_PATH = new ObjectMap<>();

    static {
        // Map colors to texture paths
        COLOR_TO_PATH.put(Color.RED, GameConfig.FRUIT0);
        COLOR_TO_PATH.put(Color.BLUE, GameConfig.FRUIT1);
        COLOR_TO_PATH.put(Color.GREEN, GameConfig.FRUIT2);
        COLOR_TO_PATH.put(Color.YELLOW, GameConfig.FRUIT3);
        COLOR_TO_PATH.put(Color.PURPLE, GameConfig.FRUIT4);
        COLOR_TO_PATH.put(Color.ORANGE, GameConfig.FRUIT5);
    }
}
