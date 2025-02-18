package com.ncwdev.match3.events;

import com.badlogic.gdx.utils.Array;
import com.ncwdev.ecs.Entity;

public class TilesDestroyedEvent {
    public Array<Entity> destroyedTiles;
    public TilesDestroyedEvent(Array<Entity> destroyedTiles) {
        this.destroyedTiles = destroyedTiles;
    }
}
