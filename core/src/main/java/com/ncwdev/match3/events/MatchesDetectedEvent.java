package com.ncwdev.match3.events;

import com.badlogic.gdx.utils.Array;
import com.ncwdev.ecs.Entity;

public class MatchesDetectedEvent {
    public Array<Entity> matches;
    
    public MatchesDetectedEvent(Array<Entity> matches) {
        this.matches = matches;
    }
}
