package com.ncwdev.match3.events;

import com.ncwdev.ecs.Entity;

public class SwapCompletedEvent {
    public final Entity tileA;
    public final Entity tileB;

    public SwapCompletedEvent(Entity tileA, Entity tileB) {
        this.tileA = tileA;
        this.tileB = tileB;
    }
}
