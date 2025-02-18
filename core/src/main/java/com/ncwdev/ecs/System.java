package com.ncwdev.ecs;

public abstract class System {
    protected World world;

    public void setWorld(World world) {
        this.world = world;
    }

    public abstract void update(float deltaTime);
}
