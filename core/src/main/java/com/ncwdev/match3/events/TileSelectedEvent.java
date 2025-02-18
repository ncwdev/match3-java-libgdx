package com.ncwdev.match3.events;

import com.ncwdev.ecs.Entity;

public class TileSelectedEvent {
    public Entity selectedTile;

    public TileSelectedEvent(Entity selectedTile) {
        this.selectedTile = selectedTile;
    }
}
