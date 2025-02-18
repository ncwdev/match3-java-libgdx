package com.ncwdev.match3.components;

import com.ncwdev.ecs.Component;

public class Position extends Component {
    public int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void swap(Position other) {
        int tempX = this.x;
        this.x = other.x;
        other.x = tempX;

        int tempY = this.y;
        this.y = other.y;
        other.y = tempY;
    }
}
