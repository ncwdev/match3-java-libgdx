package com.ncwdev.match3.events;

import com.badlogic.gdx.utils.Array;
import com.ncwdev.ecs.Entity;

public class SpecialMatchEvent extends MatchesDetectedEvent {
    public enum MatchType { CROSS, LSHAPE, FIVE_IN_A_ROW }
    public MatchType type;
    
    public SpecialMatchEvent(Array<Entity> matches, MatchType type) {
        super(matches);
        this.type = type;
    }
}
