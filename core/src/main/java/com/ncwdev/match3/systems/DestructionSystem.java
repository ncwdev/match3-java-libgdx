package com.ncwdev.match3.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.ncwdev.ecs.Entity;
import com.ncwdev.ecs.System;
import com.ncwdev.match3.GameConfig;
import com.ncwdev.match3.components.Destroying;
import com.ncwdev.match3.components.Renderable;
import com.ncwdev.match3.components.Selectable;
import com.ncwdev.match3.events.MatchesDetectedEvent;
import com.ncwdev.match3.events.TilesDestroyedEvent;
import com.ncwdev.ecs.World;

public class DestructionSystem extends System {
    public DestructionSystem(World world) {
        // Handle matched tiles
        world.getEventManager().addListener(MatchesDetectedEvent.class, event -> {
            Gdx.app.log("DestructionSystem", "Matches detected: " + event.matches.size);

            for (Entity tile : event.matches) {
                if (!tile.has(Destroying.class)) {
                    // Start destruction process
                    tile.add(new Destroying(GameConfig.DESTRUCTION_DELAY));
                    tile.remove(Selectable.class);
                }
            }
        });
    }

    @Override
    public void update(float delta) {
        // Process destroying tiles
        float scaleDelta = GameConfig.DESTRUCTION_SPEED * delta;

        Array<Entity> toRemove = new Array<>();
        for (Entity entity : world.getEntities()) {
            if (entity.has(Destroying.class)) {
                Destroying destroying = entity.get(Destroying.class);
                destroying.timer -= delta;

                Renderable renderable = entity.get(Renderable.class);
                renderable.scale -= scaleDelta;
                if (renderable.scale <= 0) {
                    renderable.scale = 0;
                }
                if (destroying.timer <= 0) {
                    toRemove.add(entity);
                }
            }
        }
        // Remove destroyed tiles
        if (toRemove.size > 0) {
            for (Entity entity : toRemove) {
                world.removeEntity(entity);
            }
            world.getEventManager().postEvent(new TilesDestroyedEvent(toRemove));
        }
    }
}
