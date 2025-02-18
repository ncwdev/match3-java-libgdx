package com.ncwdev.match3.components;

import com.ncwdev.ecs.Component;

public class Renderable extends Component {
    public float width;
    public float height;
    public float scale = 1f;

    public Renderable(float width, float height) {
        this.width = width;
        this.height = height;
    }
}
