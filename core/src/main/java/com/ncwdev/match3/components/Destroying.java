package com.ncwdev.match3.components;

import com.ncwdev.ecs.Component;

public class Destroying extends Component {
    public float timer;

    public Destroying(float duration) {
        this.timer = duration;
    }
}
