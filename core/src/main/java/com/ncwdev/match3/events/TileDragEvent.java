package com.ncwdev.match3.events;

import com.ncwdev.ecs.Entity;

public class TileDragEvent {
    public Entity tile;
    public int startX, startY;
    public int targetX, targetY;
}
