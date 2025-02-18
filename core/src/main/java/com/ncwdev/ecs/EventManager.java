package com.ncwdev.ecs;

import java.util.function.Consumer;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class EventManager {
    private final ObjectMap<Class<?>, Array<Consumer<Object>>> listeners = new ObjectMap<>();

    public <T> void addListener(Class<T> eventType, Consumer<T> listener) {
        Array<Consumer<Object>> eventListeners = listeners.get(eventType);
        if (eventListeners == null) {
            eventListeners = new Array<>();
            listeners.put(eventType, eventListeners);
        }
        eventListeners.add((Consumer<Object>) listener);
    }

    public void postEvent(Object event) {
        Array<Consumer<Object>> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            for (Consumer<Object> listener : eventListeners) {
                listener.accept(event);
            }
        }
    }
}
