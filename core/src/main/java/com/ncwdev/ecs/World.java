package com.ncwdev.ecs;

import com.badlogic.gdx.utils.Array;

public class World {
    private final Array<Entity> entities = new Array<>();
    private final Array<System> systems = new Array<>();
    private final EventManager eventManager = new EventManager();

    public void update(float deltaTime) {
        for (System system : systems) {
            system.update(deltaTime);
        }
    }

    public Array<Entity> getEntities() {
        return entities;
    }

    public Entity createEntity() {
        Entity entity = new Entity();
        getEntities().add(entity);
        return entity;
    }

    public void removeEntity(Entity entity) {
        entities.removeValue(entity, true);
    }

    public void addSystem(System system) {
        system.setWorld(this);
        systems.add(system);
    }

    public EventManager getEventManager() {
        return eventManager;
    }
}
