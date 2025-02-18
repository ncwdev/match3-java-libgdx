package com.ncwdev.ecs;

import com.badlogic.gdx.utils.IntMap;

public class Entity {
    private final IntMap<Component> components = new IntMap<>();
    private final int id;
    private static int nextId = 0;

    public Entity() {
        this.id = nextId++;
    }

    public <T extends Component> Entity add(T component) {
        components.put(component.getClass().hashCode(), component);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T get(Class<T> type) {
        return (T) components.get(type.hashCode());
    }

    public <T extends Component> boolean has(Class<T> type) {
        return components.containsKey(type.hashCode());
    }

    public <T extends Component> void remove(Class<T> type) {
        components.remove(type.hashCode());
    }
}
