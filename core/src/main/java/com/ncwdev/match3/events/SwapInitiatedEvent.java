package com.ncwdev.match3.events;

import com.ncwdev.ecs.Entity;

public class SwapInitiatedEvent {
    public Entity tileA;
    public Entity tileB;

    public SwapInitiatedEvent(Entity tileA, Entity tileB) {
        this.tileA = tileA;
        this.tileB = tileB;
    }
}
