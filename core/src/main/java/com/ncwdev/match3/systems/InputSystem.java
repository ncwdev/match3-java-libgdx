package com.ncwdev.match3.systems;

import com.ncwdev.match3.GameConfig;
import com.ncwdev.match3.components.Draggable;
import com.ncwdev.match3.components.Position;
import com.ncwdev.match3.components.Selectable;
import com.ncwdev.match3.components.Selected;
import com.ncwdev.match3.components.Tile;
import com.ncwdev.match3.events.InputEnabledEvent;
import com.ncwdev.match3.events.SwapInitiatedEvent;
import com.ncwdev.match3.events.TileSelectedEvent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.ncwdev.ecs.Entity;
import com.ncwdev.ecs.System;
import com.ncwdev.ecs.World;

public class InputSystem extends System {
    private final Array<Entity> draggables = new Array<>();
    private final FitViewport viewport;

    private Entity selectedEntity;

    private boolean inputEnabled = true;

    public InputSystem(World world, FitViewport viewport) {
        this.viewport = viewport;

        world.getEventManager().addListener(InputEnabledEvent.class, event -> {
            this.inputEnabled = event.enabled;
        });
    }

    @Override
    public void update(float deltaTime) {
        draggables.clear();

        // Find all draggable tiles
        for (Entity entity : world.getEntities()) {
            if (entity.has(Draggable.class) && entity.has(Position.class)) {
                draggables.add(entity);
            }
        }
        if (inputEnabled) {
            handleSelectionInput();
        }
    }

    private void handleSelectionInput() {
        if (Gdx.input.justTouched()) {
            Vector2 touchPos = getWorldTouchPosition();
            Entity touchedEntity = getEntityAtPosition(touchPos);

            if (isValidSelection(touchedEntity)) {
                handleEntitySelection(touchedEntity);
            } else {
                clearSelection();
            }
        }
    }

    private Vector2 getWorldTouchPosition() {
        Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(touchPos); // Convert the units to the world units of the viewport
        return touchPos;
    }

    private Entity getEntityAtPosition(Vector2 worldPos) {
        worldPos = worldPos.sub(GameConfig.fieldOffset);

        int gridX = (int) (worldPos.x / GameConfig.TILE_SIZE);
        int gridY = (int) (worldPos.y / GameConfig.TILE_SIZE);

        for (Entity entity : world.getEntities()) {
            Position pos = entity.get(Position.class);
            if (pos != null && pos.x == gridX && pos.y == gridY) {
                return entity;
            }
        }
        return null;
    }

    private boolean isValidSelection(Entity entity) {
        return entity != null 
            && entity.has(Selectable.class);
            // && !entity.has(Matched.class) // If using match detection
            // && !entity.has(Animating.class); // If using animations
    }

    private void handleEntitySelection(Entity touchedEntity) {
        if (selectedEntity == null) {
            selectEntity(touchedEntity);
        } else if (selectedEntity == touchedEntity) {
            clearSelection();
        } else {
            handleSwapAttempt(touchedEntity);
        }
    }

    private void selectEntity(Entity entity) {
        clearSelection();
        selectedEntity = entity;

        entity.add(new Selected());
        world.getEventManager().postEvent(new TileSelectedEvent(entity));

        Tile tile = entity.get(Tile.class);
        Gdx.app.log("InputSystem", "Selected tile: " + tile.typeId);
    }

    private void clearSelection() {
        if (selectedEntity != null) {
            selectedEntity.remove(Selected.class);
            selectedEntity = null;
        }
    }

    private void handleSwapAttempt(Entity targetEntity) {
        if (canSwap(selectedEntity, targetEntity)) {
            world.getEventManager().postEvent(new SwapInitiatedEvent(
                selectedEntity,
                targetEntity
            ));
        }
        clearSelection();
    }

    private boolean canSwap(Entity a, Entity b) {
        Position posA = a.get(Position.class);
        Position posB = b.get(Position.class);

        return Math.abs(posA.x - posB.x) + Math.abs(posA.y - posB.y) == 1;
    }
}
