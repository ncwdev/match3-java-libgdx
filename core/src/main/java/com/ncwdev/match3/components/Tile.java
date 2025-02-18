package com.ncwdev.match3.components;

import com.badlogic.gdx.graphics.Color;
import com.ncwdev.ecs.Component;

public class Tile extends Component {
    public Color color;
    public boolean matched;
    public boolean beingCleared;
    public int typeId;

    public Tile(Color color, int typeId) {
        this.color = color;
        this.matched = false;
        this.beingCleared = false;
        this.typeId = typeId;
    }
}
