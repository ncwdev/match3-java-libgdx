package com.ncwdev.match3.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.ncwdev.match3.components.Position;
import com.ncwdev.match3.components.Tile;
import com.ncwdev.match3.components.Transform;
import com.ncwdev.match3.components.Renderable;
import com.ncwdev.match3.components.Selected;
import com.ncwdev.ecs.Entity;
import com.ncwdev.ecs.System;
import com.ncwdev.match3.GameConfig;
import com.ncwdev.match3.Utils;

public class RenderSystem extends System {
    private final SpriteBatch batch;
    private final ShapeRenderer shapeRenderer;

    public RenderSystem(SpriteBatch batch) {
        this.batch = batch;
        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void update(float deltaTime) {
        // Draw all renderable entities
        // shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Entity entity : world.getEntities()) {
            if (entity.has(Position.class) && entity.has(Tile.class) && entity.has(Renderable.class)) {
                Tile tile = entity.get(Tile.class);
                Renderable renderable = entity.get(Renderable.class);
                Transform transform = entity.get(Transform.class);

                Vector2 itemPos;
                if (tile != null) {
                    if (transform != null) {
                        itemPos = Utils.getFieldPosition(transform.x, transform.y);
                    } else {
                        Position pos = entity.get(Position.class);
                        itemPos = Utils.getFieldPosition(pos.x, pos.y);
                    }
                    Texture texture = Utils.getTexture(tile.color);
                    batch.draw(texture,
                        itemPos.x, itemPos.y,
                        renderable.width / 2, renderable.height / 2,
                        renderable.width, renderable.height,
                        renderable.scale, renderable.scale,
                        0, // Rotation
                        0, 0, // Source coordinates
                        texture.getWidth(), texture.getHeight(),
                        false, false
                    );

                    // Draw tile content
                    // shapeRenderer.setColor(tile.color);

                    // // Calculate the position to center the content within the tile
                    // float contentX = itemPos.x + (GameConfig.TILE_SIZE - renderable.width) / 2;
                    // float contentY = itemPos.y + (GameConfig.TILE_SIZE - renderable.height) / 2;
                    // shapeRenderer.rect(contentX, contentY, renderable.width, renderable.height);
                }
            }
        }
        // shapeRenderer.end();

        // Draw selection overlay
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (Entity entity : world.getEntities()) {
            if (entity.has(Selected.class)) {
                Position pos = entity.get(Position.class);
                Vector2 itemPos = Utils.getFieldPosition(pos.x, pos.y);

                shapeRenderer.setColor(Color.GOLD);
                shapeRenderer.rect(itemPos.x, itemPos.y, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
            }
        }
        shapeRenderer.end();
    }
}
