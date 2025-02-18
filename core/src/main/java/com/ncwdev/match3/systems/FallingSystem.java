package com.ncwdev.match3.systems;

import com.ncwdev.ecs.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.ncwdev.ecs.Entity;
import com.ncwdev.ecs.System;
import com.ncwdev.match3.GameConfig;
import com.ncwdev.match3.Grid;
import com.ncwdev.match3.components.Falling;
import com.ncwdev.match3.components.Position;
import com.ncwdev.match3.components.Tile;
import com.ncwdev.match3.components.Transform;
import com.ncwdev.match3.events.TilesDestroyedEvent;
import com.ncwdev.match3.events.TilesLandedEvent;

public class FallingSystem extends System {
    private Grid grid = null;

    public FallingSystem(World world) {
        world.getEventManager().addListener(TilesDestroyedEvent.class, event -> {
            rebuildGrid();
            processColumnFall();
        });
    }

    @Override
    public void update(float delta) {
        Array<Entity> fallingTiles = new Array<>();

        for (Entity entity : world.getEntities()) {
            if (entity.has(Falling.class)) {
                fallingTiles.add(entity);
            }
        }

        boolean allLanded = true;
        for (Entity entity : fallingTiles) {
            Falling falling = entity.get(Falling.class);
            Transform transform = entity.get(Transform.class);

            // Update position
            transform.y = Math.max(falling.targetPosition.y, transform.y - falling.speed * delta);

            if (transform.y > falling.targetPosition.y) {
                allLanded = false;
            } else {
                entity.remove(Falling.class);
            }
        }

        if (allLanded && fallingTiles.size > 0) {
            world.getEventManager().postEvent(new TilesLandedEvent());
        }
    }

    private void rebuildGrid() {
        grid = new Grid(GameConfig.FIELD_WIDTH, GameConfig.FIELD_HEIGHT);

        for (Entity entity : world.getEntities()) {
            if (entity.has(Position.class) && entity.has(Tile.class)) {
                Position pos = entity.get(Position.class);
                grid.set(pos.x, pos.y, entity);
            }
        }
    }

    private void processColumnFall() {
        for (int x = 0; x < grid.width; x++) {
            processSingleColumn(x);
        }
    }

    private void processSingleColumn(int x) {
        int lowestEmptyY = -1;

        // Bottom-up search for empty spaces
        for (int y = 0; y < grid.height; y++) {
            Entity tile = grid.get(x, y);

            if (tile == null) {
                if (lowestEmptyY == -1) {
                    lowestEmptyY = y;
                }
            } else if (lowestEmptyY != -1) {
                // Found tile above empty space, make it fall
                Position pos = tile.get(Position.class);
                pos.y = lowestEmptyY;

                Falling falling = new Falling();
                falling.setTarget(x, lowestEmptyY, GameConfig.FALL_SPEED);
                tile.add(falling);

                grid.set(x, lowestEmptyY, tile);
                grid.set(x, y, null);
                lowestEmptyY++;
            }
        }
    }
}
