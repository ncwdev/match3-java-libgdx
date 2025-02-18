package com.ncwdev.match3.components;

import com.ncwdev.ecs.Component;

public class Transform extends Component {
    public float x;
    public float y;

    public Transform(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
