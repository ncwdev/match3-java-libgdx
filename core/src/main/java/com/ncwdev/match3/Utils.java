package com.ncwdev.match3;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

public class Utils {
    public static final ObjectMap<Color, Texture> COLOR_TO_TEXTURE = new ObjectMap<>();

    public static void loadTilesTextures() {
        for (int i = 0; i < GameConfig.COLORS.length; i++) {
            Color color = GameConfig.COLORS[i];
            Texture texture = new Texture(GameConfig.COLOR_TO_PATH.get(color));
            // texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            // // texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
            // texture.getTextureData().prepare();
            COLOR_TO_TEXTURE.put(color, texture);
        }
    }

    public static Texture getTexture(Color color) {
        return COLOR_TO_TEXTURE.get(color);
    }

    public static Vector2 getFieldPosition(float x, float y) {
        return new Vector2(x * GameConfig.TILE_SIZE + GameConfig.fieldOffset.x, y * GameConfig.TILE_SIZE + GameConfig.fieldOffset.y);
    }
}
