package com.ncwdev.match3.components;

import com.badlogic.gdx.math.Vector2;
import com.ncwdev.ecs.Component;

public class Falling extends Component {
    public float speed;
    public Vector2 targetPosition = new Vector2();

    // public final Vector2 startPosition = new Vector2();
    // public final Vector2 targetPosition = new Vector2();
    // public float speed; // pixels per second
    // public float progress; // 0-1

    public void setTarget(float x, float y, float speed) {
        targetPosition.set(x, y);
        this.speed = speed;
    }
}
