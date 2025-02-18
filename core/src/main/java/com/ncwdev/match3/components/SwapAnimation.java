package com.ncwdev.match3.components;

import com.badlogic.gdx.math.Vector2;
import com.ncwdev.ecs.Component;
import com.ncwdev.ecs.Entity;

public class SwapAnimation extends Component {
    public final Entity partner;
    public final Vector2 startPos = new Vector2();
    public final Vector2 targetPos = new Vector2();
    public float duration;
    public float elapsed;

    public SwapAnimation(Entity partner, Vector2 start, Vector2 target, float duration) {
        this.partner = partner;
        this.startPos.set(start);
        this.targetPos.set(target);
        this.duration = duration;
    }
}
