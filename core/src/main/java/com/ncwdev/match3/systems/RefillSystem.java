package com.ncwdev.match3.systems;

import com.ncwdev.ecs.World;
import com.badlogic.gdx.graphics.Color;
import com.ncwdev.ecs.Entity;
import com.ncwdev.ecs.System;
import com.ncwdev.match3.BoardInitializer;
import com.ncwdev.match3.GameConfig;
import com.ncwdev.match3.Grid;
import com.ncwdev.match3.components.Draggable;
import com.ncwdev.match3.components.Falling;
import com.ncwdev.match3.components.Position;
import com.ncwdev.match3.components.Renderable;
import com.ncwdev.match3.components.Selectable;
import com.ncwdev.match3.components.Swappable;
import com.ncwdev.match3.components.Tile;
import com.ncwdev.match3.components.Transform;
import com.ncwdev.match3.events.TilesDestroyedEvent;

public class RefillSystem extends System {
    private Grid grid = null;

    public RefillSystem(World world) {
        world.getEventManager().addListener(TilesDestroyedEvent.class, event -> {
            rebuildGrid();
            refillEmptyColumns();
        });
    }

    @Override
    public void update(float delta) {
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

    private void refillEmptyColumns() {
        for (int x = 0; x < grid.width; x++) {
            int missingTiles = countMissingTilesInColumn(x, grid);
            createNewTilesForColumn(x, missingTiles, grid);
        }
    }

    private int countMissingTilesInColumn(int x, Grid grid) {
        int missing = 0;
        for (int y = 0; y < grid.height; y++) {
            if (grid.get(x, y) == null) missing++;
        }
        return missing;
    }

    private void createNewTilesForColumn(int x, int count, Grid grid) {
        int highestExistingY = -1;

        // Find the highest existing tile in column
        for (int y = grid.height - 1; y >= 0; y--) {
            if (grid.get(x, y) != null) {
                highestExistingY = y;
                break;
            }
        }

        for (int i = 0; i < count; i++) {
            int targetY = highestExistingY + i + 1;
            if (targetY >= grid.height) targetY = grid.height - 1 - i;

            createNewTile(x, targetY, grid.height + i);
        }
    }

    private Entity createNewTile(int targetX, int targetY, int spawnY) {
        Color color = BoardInitializer.getRandomColor();
        Entity tile = world.createEntity()
                .add(new Position(targetX, targetY))
                .add(new Transform(targetX, spawnY))
                .add(new Tile(color, color.hashCode()))
                .add(new Renderable(GameConfig.ITEM_SIZE, GameConfig.ITEM_SIZE))
                .add(new Selectable())
                .add(new Swappable())
                .add(new Draggable());

        Falling fallingComponent = new Falling();
        fallingComponent.setTarget(targetX, targetY, GameConfig.FALL_SPEED);
        tile.add(fallingComponent);

        return tile;
    }
}
